package net.swordie.ms.client.character.skills.temp;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.BodyPart;
import net.swordie.ms.client.character.items.Equip;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.jobs.legend.Luminous;
import net.swordie.ms.client.jobs.resistance.demon.DemonAvenger;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.enums.TSIndex;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import net.swordie.ms.world.field.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.client.jobs.resistance.Mechanic.*;
import static net.swordie.ms.constants.SkillConstants.DICE_STAT_VALUE;
import static net.swordie.ms.constants.SkillConstants.DICE_STAT_VALUE_DD;

public class TemporaryStatManager {
    private static final Logger log = LogManager.getLogger(TemporaryStatManager.class);
    private final Map<CharacterTemporaryStat, Option> stats = new ConcurrentHashMap<>();
    private final Map<CharacterTemporaryStat, List<Option>> indieStats = new ConcurrentHashMap<>();
    private final Map<CharacterTemporaryStat, ScheduledFuture> statSchedules = new ConcurrentHashMap<>();
    private final Map<Tuple<CharacterTemporaryStat, Option>, ScheduledFuture> indieStatSchedules = new ConcurrentHashMap<>();
    private final TemporaryStatMask setStatMask = new TemporaryStatMask();
    private final TemporaryStatMask resetStatMask = new TemporaryStatMask();
    private final Lock lock = new ReentrantLock();
    private final Char chr;

    private Map<BaseStat, Integer> baseStatCache = new HashMap<>();
    private Map<BaseStat, Set<Integer>> nonAddBaseStatCache = new HashMap<>();
    private boolean baseStatChanged = false;
    private long lastStatResetRequestTime = Util.getCurrentTimeLong();
    private List<OutPacket> toBroadcastAfterMigrate = new ArrayList<>();
    private List<TemporaryStatBase> twoStates = new ArrayList<>();
    private Set<AffectedArea> affectedAreas = new HashSet<>();
    private int viperEnergyCharge;
    private int[] diceInfo = new int[22];
    private LarknessManager larknessManager;
    private StopForceAtom stopForceAtom;


    public TemporaryStatManager(Char chr) {
        this.chr = chr;
        for (CharacterTemporaryStat cts : TSIndex.getAllCTS()) {
            switch (cts) {
                case PartyBooster:
                    twoStates.add(new PartyBooster());
                    break;
                case GuidedBullet:
                    twoStates.add(new GuidedBullet());
                    break;
                case EnergyCharged:
                    twoStates.add(new TemporaryStatBase(true));
                    break;
                case RideVehicle:
                    twoStates.add(new TwoStateTemporaryStat(false));
                    break;
                default:
                    twoStates.add(new TwoStateTemporaryStat(true));
                    break;
            }
        }
    }



    // TEMPORARY STAT INTERFACE ----------------------------------------------------------------------------------------

    public Map<CharacterTemporaryStat, Option> getCurrentStats() {
        return stats;
    }

    public Map<CharacterTemporaryStat, List<Option>> getCurrentIndieStats() {
        return indieStats;
    }
    public Option getOption(CharacterTemporaryStat cts) {
        if (!cts.isIndie()) {
            if (stats.containsKey(cts)) {
                return stats.get(cts);
            }
        } else {
            if (indieStats.containsKey(cts)) {
                return indieStats.get(cts).get(0);
            }
        }
        return new Option();
    }

    public List<Option> getIndieOptions(CharacterTemporaryStat cts) {
        if (indieStats.containsKey(cts)) {
            return indieStats.get(cts);
        }
        return List.of();
    }

    public Option getOptByCTSAndSkill(CharacterTemporaryStat cts, int skillId) {
        if (!cts.isIndie()) {
            if (stats.containsKey(cts)) {
                Option option = stats.get(cts);
                if (option.rOption == skillId) {
                    return option;
                }
            }
        } else {
            if (indieStats.containsKey(cts)) {
                for (Option option : indieStats.get(cts)) {
                    if (option.nReason == skillId) {
                        return option;
                    }
                }
            }
        }
        return null;
    }

    public int getRemainingTime(CharacterTemporaryStat cts, int skillId) {
        if (getOptByCTSAndSkill(cts, skillId) != null) {
            Option opt = getOptByCTSAndSkill(cts, skillId);
            return (opt.tStart + ((opt.isInMillis ? 1 : 1000) * (cts.isIndie() ? opt.tTerm : opt.tOption))) - Util.getCurrentTime();
        }
        return 0;
    }

    public boolean hasStat(CharacterTemporaryStat cts) {
        return stats.containsKey(cts) || indieStats.containsKey(cts);
    }

    public boolean hasStatBySkillId(int skillId) {
        for (Option option : stats.values()) {
            if (option.rOption == skillId) {
                return true;
            }
        }
        for (List<Option> options : indieStats.values()) {
            for (Option option : options) {
                if (option.nReason == skillId) {
                    return true;
                }
            }
        }
        return false;
    }

    public void putCharacterStatValue(CharacterTemporaryStat cts, int skillID, int value, int duration) {
        Option option = new Option();
        if (cts.isIndie()) {
            option.nReason = skillID;
            option.nValue = value;
            option.tTerm = duration;
        } else {
            option.rOption = skillID;
            option.nOption = value;
            option.tOption = duration;
        }
        putCharacterStatValue(cts, option);
    }

    public void putCharacterStatValue(CharacterTemporaryStat cts, Option option) {
        putCharacterStatValue(cts, option, false);
    }

    public void putCharacterStatValueFromMobSkill(CharacterTemporaryStat cts, Option o) {
        o.rOption |= o.slv << 16; // mob skills are encoded differently: not an int, but short (skill ID), then short (slv).
        putCharacterStatValue(cts, o);
        chr.getJobHandler().handleMobDebuffSkill(chr);
    }

    public void putCharacterStatValue(CharacterTemporaryStat cts, Option option, boolean noIncBuffDuration) {
        boolean indie = cts.isIndie();
        TSIndex tsi = TSIndex.getTSEFromCTS(cts);
        TemporaryStatBase tsb = tsi != null ? getTSBByTSIndex(tsi) : null;
        option.setTimeToMillis();
        SkillInfo skillinfo = SkillData.getSkillInfoById(indie ? option.nReason : option.rOption);
        if (skillinfo != null && !skillinfo.isNotIncBuffDuration() && !noIncBuffDuration) {
            if (indie) {
                option.tTerm = chr.getJobHandler().getBuffedSkillDuration(option.tTerm);
            } else {
                option.tOption = chr.getJobHandler().getBuffedSkillDuration(option.tOption);
            }
        }
        if (cts == CombatOrders) {
            chr.setCombatOrders(option.nOption);
        }
        lock.lock();
        try {
            baseStatChanged = true;
            setStatMask.put(cts);
            if (!indie) {
                stats.put(cts, option);
                if (statSchedules.containsKey(cts)) {
                    statSchedules.remove(cts).cancel(false);
                }
                if (option.tOption > 0 || (tsi != null && !tsb.hasExpired() && tsb.getExpireTerm() != 0)) {
                    int delay = tsi != null ? tsb.getExpireTerm() : option.tOption;
                    statSchedules.put(cts, EventManager.addEvent(() -> removeStat(cts, true), delay, TimeUnit.MILLISECONDS));
                }
            } else {
                if (!indieStats.containsKey(cts)) {
                    indieStats.put(cts, new ArrayList<>());
                }
                Tuple<CharacterTemporaryStat, Option> tuple = new Tuple<>(cts, option);
                if (indieStatSchedules.containsKey(tuple)) {
                    indieStatSchedules.remove(tuple).cancel(false);
                }
                if (option.tTerm > 0) {
                    indieStatSchedules.put(tuple, EventManager.addEvent(() -> removeIndieStat(tuple, true), option.tTerm, TimeUnit.MILLISECONDS));
                }
                indieStats.get(cts).add(option);
            }
        } finally {
            lock.unlock();
        }
    }

    public void removeStat(CharacterTemporaryStat cts, Predicate<Option> predicate, boolean sendPacket) {
        if (!hasStat(cts) || cts.isIndie()) {
            return;
        }
        // pre-remove tasks
        if (cts == CombatOrders) {
            chr.setCombatOrders(0);
        }
        chr.getJobHandler().handleRemoveCTS(cts);

        // remove stat
        Option option = getOption(cts);
        lock.lock();
        try {
            if (predicate.test(option)) {
                stats.remove(cts);
                statSchedules.remove(cts);
                resetStatMask.put(cts);
                baseStatChanged = true;
            }
        } finally {
            lock.unlock();
        }

        // post-remove tasks
        if (sendPacket) {
            sendResetStatPacket();
        }
        if (TSIndex.isTwoStat(cts)) {
            getTSBByTSIndex(TSIndex.getTSEFromCTS(cts)).reset();
        }
        if (cts == Reincarnation && option.xOption != 0) {
            chr.heal(-chr.getMaxHP());
        }
        if (cts == HolyMagicShell && option.nOption != 0) {
            // apply cooldown
            Option o1 = new Option();
            o1.nOption = 0;
            o1.rOption = option.rOption;
            o1.tOption = (option.tStart + (option.xOption * 1000)) - Util.getCurrentTime();
            o1.setInMillis(true);
            putCharacterStatValue(HolyMagicShell, o1, true);
            sendSetStatPacket();
        }
        if (JobConstants.isLuminous(chr.getJob()) && cts == Larkness) {
            ((Luminous) chr.getJobHandler()).handleRemoveLarkness(option.rOption);
        }
        if (JobConstants.isDemonAvenger(chr.getJob())) {
            ((DemonAvenger) chr.getJobHandler()).sendHpUpdate();
        }
    }

    public void removeStat(CharacterTemporaryStat cts, boolean sendPacket) {
        removeStat(cts, (option) -> true, sendPacket);
    }

    public void removeStat(CharacterTemporaryStat cts) {
        removeStat(cts, (option) -> true, false);
    }

    public void removeIndieStat(CharacterTemporaryStat cts, Predicate<Option> predicate, boolean sendPacket) {
        if (!hasStat(cts) || !cts.isIndie()) {
            return;
        }
        lock.lock();
        try {
            var optionIter = indieStats.getOrDefault(cts, List.of()).iterator();
            while (optionIter.hasNext()) {
                Option option = optionIter.next();
                if (predicate.test(option)) {
                    optionIter.remove();
                    if (option.summon != null) {
                        option.summon.setExpired(true);
                        option.summon = null;
                    }
                    indieStatSchedules.remove(new Tuple<>(cts, option));
                    resetStatMask.put(cts);
                    baseStatChanged = true;
                }
            }
            if (indieStats.getOrDefault(cts, List.of()).isEmpty()) {
                indieStats.remove(cts);
            }
        } finally {
            lock.unlock();
        }

        if (sendPacket) {
            sendResetStatPacket();
        }
    }

    public void removeIndieStat(Tuple<CharacterTemporaryStat, Option> tuple, boolean sendPacket) {
        removeIndieStat(tuple.getLeft(), tuple.getRight()::equals, sendPacket);
    }

    public void removeIndieStat(Tuple<CharacterTemporaryStat, Option> tuple) {
        removeIndieStat(tuple.getLeft(), tuple.getRight()::equals, false);
    }

    public void removeStatsBySkill(CharacterTemporaryStat cts, int skillId) {
        if (!cts.isIndie()) {
            removeStat(cts, (option) -> option.rOption == skillId, false);
        } else {
            removeIndieStat(cts, (option) -> option.nReason == skillId, false);
        }
    }

    public void removeStatsByPredicate(Predicate<Option> predicate) {
        List<CharacterTemporaryStat> statsToRemove = new ArrayList<>();
        List<Tuple<CharacterTemporaryStat, Option>> indieStatsToRemove = new ArrayList<>();
        stats.forEach((cts, option) -> {
            if (predicate.test(option)) {
                statsToRemove.add(cts);
            }
        });
        indieStats.forEach((cts, options) -> {
            for (Option option : options) {
                if (predicate.test(option)) {
                    indieStatsToRemove.add(new Tuple<>(cts, option));
                }
            }
        });
        statsToRemove.forEach(this::removeStat);
        indieStatsToRemove.forEach(this::removeIndieStat);
    }

    public void removeStatsBySkill(int skillId) {
        removeStatsByPredicate((option) -> option.rOption == skillId || option.nReason == skillId);
    }

    public void removeAllStats() {
        removeStatsByPredicate((option) -> true);
    }

    public void resetByTime(int curTime) {
        if (curTime - lastStatResetRequestTime < 500) {
            return;
        }
        List<CharacterTemporaryStat> toRemove = new ArrayList<>();
        stats.forEach((cts, option) -> {
            if (RESET_BY_TIME_CTS.contains(cts)) {
                if (option.tOption != 0 && curTime - option.tStart >= option.tOption) {
                    toRemove.add(cts);
                }
            }
        });
        toRemove.forEach(this::removeStat);
        sendResetStatPacket();
        this.lastStatResetRequestTime = curTime;
    }



    // PACKET SENDING INTERFACE ----------------------------------------------------------------------------------------

    public void sendSetStatPacket() {
        lock.lock();
        try {
            if (!setStatMask.isEmpty()) {
                // send packets
                chr.getField().broadcastPacket(UserRemote.setTemporaryStat(chr, (short) 0), chr);
                chr.write(WvsContext.temporaryStatSet(this));
                setStatMask.clear();
            }
        } finally {
            lock.unlock();
        }
    }

    public void sendResetStatPacket() {
        lock.lock();
        try {
            if (!resetStatMask.isEmpty()) {
                chr.getField().broadcastPacket(UserRemote.resetTemporaryStat(chr), chr);
                chr.write(WvsContext.temporaryStatReset(
                        this,
                        resetStatMask.has(RideVehicle) || resetStatMask.has(RideVehicleExpire),
                        chr.isInCashShop() || chr.isChangingChannel())
                );
                resetStatMask.clear();
            }
        } finally {
            lock.unlock();
        }
    }

    public TemporaryStatMask getResetStatMask() {
        return resetStatMask;
    }

    public boolean hasNewMovingAffectingStat() {
        return MOVING_AFFECTING_STAT.stream().anyMatch(setStatMask::has);
    }

    public boolean hasRemovedMovingAffectingStat() {
        return MOVING_AFFECTING_STAT.stream().anyMatch(resetStatMask::has);
    }

    public void encodeIndieTempStat(TemporaryStatMask mask, OutPacket outPacket) {
        int curTime = Util.getCurrentTime();
        for (CharacterTemporaryStat cts : mask.sorted(getIndieOrderList())) {
            List<Option> options = indieStats.getOrDefault(cts, List.of());
            outPacket.encodeInt(options.size());
            for (Option option : options) {
                outPacket.encodeInt(option.nReason);
                outPacket.encodeInt(option.nValue);
                outPacket.encodeInt(option.nKey);
                outPacket.encodeInt(curTime - option.tStart); // elapsedTime
                outPacket.encodeInt(option.tTerm);
                int size = 0;
                outPacket.encodeInt(size);
                for (int i = 0; i < size; i++) {
                    outPacket.encodeInt(0); // MValueKey
                    outPacket.encodeInt(0); // MValue
                }
            }
        }
    }

    public void encodeIndieTempStat(OutPacket outPacket) {
        encodeIndieTempStat(setStatMask, outPacket);
    }

    public void encodeRemovedIndieTempStat(OutPacket outPacket) {
        encodeIndieTempStat(resetStatMask, outPacket);
    }

    public void encodeForLocal(OutPacket outPacket) {
        setStatMask.encode(outPacket);
        boolean isEncodeInt = getEncodeIntList().stream().anyMatch(setStatMask::has);
        for (CharacterTemporaryStat cts : setStatMask.sorted(getOrderList())) {
            Option option = getOption(cts);
            if (isEncodeInt) {
                outPacket.encodeInt(option.nOption);
            } else {
                outPacket.encodeShort(option.nOption);
            }
            outPacket.encodeInt(option.rOption);
            outPacket.encodeInt(option.tOption);
        }
        if (setStatMask.has(SoulMP)) {
            outPacket.encodeInt(getOption(SoulMP).xOption);
            outPacket.encodeInt(getOption(SoulMP).rOption);
        }
        if (setStatMask.has(FullSoulMP)) {
            outPacket.encodeInt(getOption(FullSoulMP).xOption);
        }
        short size = 0;
        outPacket.encodeShort(size);
        for (int i = 0; i < size; i++) {
            outPacket.encodeInt(0); // nKey
            outPacket.encodeByte(0); // bEnable
        }
        if (setStatMask.has(HayatoStance)) {
            outPacket.encodeInt(getOption(HayatoStance).xOption);
        }
        if (setStatMask.has(Unk460)) {
            outPacket.encodeInt(getOption(Unk460).xOption);
        }
        outPacket.encodeByte(0); // DefenseAtt
        outPacket.encodeByte(0); // DefenseState
        outPacket.encodeByte(0); // PvPDamage
        if (setStatMask.has(Dice)) {
            for (int i = 0; i < diceInfo.length; i++) {
                outPacket.encodeInt(diceInfo[i]);
            }
        }
        if (setStatMask.has(KillingPoint)) {
            outPacket.encodeByte(getOption(KillingPoint).nOption);
        }
        if (setStatMask.has(PinkbeanRollingGrade)) {
            outPacket.encodeByte(getOption(PinkbeanRollingGrade).nOption);
        }
        if (setStatMask.has(Judgement)) {
            outPacket.encodeInt(getOption(Judgement).xOption); // byte would  err38
        }
        if (setStatMask.has(StackBuff)) {
            outPacket.encodeByte(getOption(StackBuff).mOption);
        }
        if (setStatMask.has(Trinity)) {
            outPacket.encodeByte(getOption(Trinity).mOption);
        }
        if (setStatMask.has(ElementalCharge)) {
            outPacket.encodeByte(getOption(ElementalCharge).mOption);
            outPacket.encodeShort(getOption(ElementalCharge).wOption);
            outPacket.encodeByte(getOption(ElementalCharge).uOption);
            outPacket.encodeByte(getOption(ElementalCharge).zOption);
        }
        if (setStatMask.has(LifeTidal)) {
            outPacket.encodeInt(getOption(LifeTidal).mOption);
        }
        if (setStatMask.has(AntiMagicShell)) {
            outPacket.encodeByte(getOption(AntiMagicShell).bOption);
        }
        if (setStatMask.has(Larkness)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
            getLarknessManager().encode(outPacket);
        }
        if (setStatMask.has(IgnoreTargetDEF)) {
            outPacket.encodeInt(getOption(IgnoreTargetDEF).mOption);
        }
        if (setStatMask.has(StopForceAtomInfo)) {
            // getStopForceAtom().encode(outPacket);
        }
        if (setStatMask.has(SmashStack)) {
            outPacket.encodeInt(getOption(SmashStack).xOption);
        }
        if (setStatMask.has(MobZoneState)) {
            // for (int zoneState : getMobZoneStates()) {
            //     outPacket.encodeInt(zoneState);
            // }
            // outPacket.encodeInt(0); // notify end
        }
        if (setStatMask.has(Slow)) {
            outPacket.encodeByte(getOption(Slow).bOption);
        }
        if (setStatMask.has(IceAura)) {
            outPacket.encodeByte(getOption(IceAura).bOption);
        }
        if (setStatMask.has(KnightsAura)) {
            outPacket.encodeByte(getOption(KnightsAura).bOption);
        }
        if (setStatMask.has(IgnoreMobpdpR)) {
            outPacket.encodeByte(getOption(IgnoreMobpdpR).bOption);
        }
        if (setStatMask.has(BdR)) {
            outPacket.encodeByte(getOption(BdR).bOption);
        }
        if (setStatMask.has(DropRIncrease)) {
            outPacket.encodeInt(getOption(DropRIncrease).xOption);
            outPacket.encodeByte(getOption(DropRIncrease).bOption);
        }
        if (setStatMask.has(PoseType)) {
            outPacket.encodeByte(getOption(PoseType).bOption);
        }
        if (setStatMask.has(Beholder)) {
            outPacket.encodeInt(getOption(Beholder).sOption);
            outPacket.encodeInt(getOption(Beholder).ssOption);
        }
        if (setStatMask.has(CrossOverChain)) {
            outPacket.encodeInt(getOption(CrossOverChain).xOption);
        }
        if (setStatMask.has(Reincarnation)) {
            outPacket.encodeInt(getOption(Reincarnation).xOption);
        }
        if (setStatMask.has(ExtremeArchery)) {
            outPacket.encodeInt(getOption(ExtremeArchery).bOption);
            outPacket.encodeInt(getOption(ExtremeArchery).xOption);
        }
        if (setStatMask.has(QuiverCatridge)) {
            outPacket.encodeInt(getOption(QuiverCatridge).xOption);
        }
        if (setStatMask.has(ImmuneBarrier)) {
            outPacket.encodeInt(getOption(ImmuneBarrier).xOption);
        }
        if (setStatMask.has(ZeroAuraStr)) {
            outPacket.encodeByte(getOption(ZeroAuraStr).bOption);
        }
        if (setStatMask.has(ZeroAuraSpd)) {
            outPacket.encodeByte(getOption(ZeroAuraSpd).bOption);
        }
        if (setStatMask.has(ArmorPiercing)) {
            outPacket.encodeInt(getOption(ArmorPiercing).bOption);
        }
        if (setStatMask.has(SharpEyes)) {
            outPacket.encodeInt(getOption(SharpEyes).mOption);
        }
        if (setStatMask.has(AdvancedBless)) {
            outPacket.encodeInt(getOption(AdvancedBless).xOption);
        }
        if (setStatMask.has(DotHealHPPerSecond)) {
            outPacket.encodeInt(getOption(DotHealHPPerSecond).xOption);
        }
        if (setStatMask.has(SpiritGuard)) {
            outPacket.encodeInt(getOption(SpiritGuard).nOption);
        }
        if (setStatMask.has(KnockBack)) {
            outPacket.encodeInt(getOption(KnockBack).nOption);
            outPacket.encodeInt(getOption(KnockBack).bOption);
        }
        if (setStatMask.has(ShieldAttack)) {
            outPacket.encodeInt(getOption(ShieldAttack).xOption);
        }
        if (setStatMask.has(SSFShootingAttack)) {
            outPacket.encodeInt(getOption(SSFShootingAttack).xOption);
        }
        if (setStatMask.has(BMageAura)) {
            outPacket.encodeInt(getOption(BMageAura).xOption);
            outPacket.encodeByte(getOption(BMageAura).bOption);
        }
        if (setStatMask.has(BattlePvPHelenaMark)) {
            outPacket.encodeInt(getOption(BattlePvPHelenaMark).cOption);
        }
        if (setStatMask.has(PinkbeanAttackBuff)) {
            outPacket.encodeInt(getOption(PinkbeanAttackBuff).bOption);
        }
        if (setStatMask.has(RoyalGuardState)) {
            outPacket.encodeInt(getOption(RoyalGuardState).bOption);
            outPacket.encodeInt(getOption(RoyalGuardState).xOption);
        }
        if (setStatMask.has(MichaelSoulLink)) {
            outPacket.encodeInt(getOption(MichaelSoulLink).xOption);
            outPacket.encodeByte(getOption(MichaelSoulLink).bOption);
            outPacket.encodeInt(getOption(MichaelSoulLink).cOption);
            outPacket.encodeInt(getOption(MichaelSoulLink).yOption);
        }
        if (setStatMask.has(AdrenalinBoost)) {
            outPacket.encodeByte(getOption(AdrenalinBoost).cOption);
        }
        if (setStatMask.has(RWCylinder)) {
            outPacket.encodeByte(getOption(RWCylinder).bOption);
            outPacket.encodeShort(getOption(RWCylinder).cOption);
        }
        if (setStatMask.has(RWMagnumBlow)) {
            outPacket.encodeShort(getOption(RWMagnumBlow).bOption);
            outPacket.encodeByte(getOption(RWMagnumBlow).xOption);
        }
        outPacket.encodeInt(getViperEnergyCharge());
        if (setStatMask.has(BladeStance)) {
            outPacket.encodeInt(getOption(BladeStance).xOption);
        }
        if (setStatMask.has(DarkSight)) {
            outPacket.encodeInt(getOption(DarkSight).cOption);
        }
        if (setStatMask.has(Stigma)) {
            outPacket.encodeInt(getOption(Stigma).bOption);
        }
        for (int i = 0; i < TSIndex.values().length; i++) {
            if (setStatMask.has(TSIndex.getCTSFromTwoStatIndex(i))) {
                getTwoStates().get(i).encode(outPacket);
            }
        }
        encodeIndieTempStat(outPacket);
        if (setStatMask.has(DarkSight)) {
            outPacket.encodeInt(getOption(DarkSight).mOption);
        }
        if (setStatMask.has(UsingScouter)) {
            outPacket.encodeInt(getOption(UsingScouter).nOption);
            outPacket.encodeInt(getOption(UsingScouter).xOption);
        }
        // from here on: new stats found in 176 idb
        if (setStatMask.has(NewFlying)) {
            outPacket.encodeInt(getOption(NewFlying).xOption);
        }
        if (setStatMask.has(Unk485)) {
            /* not exactly 2 bytes:
            c = 0;
            while(true) {
                a = decode1();
                b = (a & 0x7F) << c;
                if(a >= 0) {
                    break;
                }
                c += 7;
            }
             */
            outPacket.encodeByte(getOption(Unk485).xOption);
            outPacket.encodeByte(getOption(Unk485).yOption);
        }
        if (setStatMask.has(Unk486)) {
            // 1st byte is normal, 2nd one is like in Unk485
            outPacket.encodeByte(getOption(Unk486).xOption);
            outPacket.encodeByte(getOption(Unk486).yOption);
        }
    }

    public void encodeForRemote(OutPacket outPacket, boolean enterField) {
        TemporaryStatMask statMask = enterField ? TemporaryStatMask.from(stats.keySet()) : setStatMask;
        statMask.remove(EnergyCharged); // causes error38
        statMask.encode(outPacket);
        List<CharacterTemporaryStat> remoteStatList = statMask.sorted(getRemoteOrderList());
        for (CharacterTemporaryStat cts : remoteStatList) {
            Option option = getOption(cts);
            switch (cts) {
                case Poison: // Why does this get encoded, then immediately overwritten?
                    outPacket.encodeShort(option.nOption);
                    break;
            }
            if (!cts.isNotEncodeAnything()) {
                if (cts.isRemoteEncode1()) {
                    outPacket.encodeByte(option.nOption);
                } else if (cts.isRemoteEncode4()) {
                    outPacket.encodeInt(option.nOption);
                } else {
                    outPacket.encodeShort(option.nOption);
                }
                if (!cts.isNotEncodeReason()) {
                    outPacket.encodeInt(option.rOption);
                }
            }
            switch (cts) {
                case Contagion:
                    outPacket.encodeInt(option.tOption);
                    break;
                case BladeStance:
                case ImmuneBarrier:
                case Unk460:
                    outPacket.encodeInt(option.xOption);
                    break;
                case FullSoulMP:
                    outPacket.encodeInt(option.rOption);
                    outPacket.encodeInt(option.xOption);
                    break;
                case AntiMagicShell:
                    outPacket.encodeByte(option.bOption);
                    break;
            }
        }
        Set<CharacterTemporaryStat> ctsSet = Set.copyOf(remoteStatList);
        if (ctsSet.contains(PoseType)) {
            outPacket.encodeByte(getOption(PoseType).bOption);
        }
        outPacket.encodeByte(0); // DefenseAtt
        outPacket.encodeByte(0); // DefenseState
        outPacket.encodeByte(0); // PvPDamage
        if (ctsSet.contains(ZeroAuraStr)) {
            outPacket.encodeByte(getOption(ZeroAuraStr).bOption);
        }
        if (ctsSet.contains(ZeroAuraSpd)) {
            outPacket.encodeByte(getOption(ZeroAuraSpd).bOption);
        }
        if (ctsSet.contains(BMageAura)) {
            outPacket.encodeByte(getOption(BMageAura).bOption);
        }
        if (ctsSet.contains(BattlePvPHelenaMark)) {
            outPacket.encodeInt(getOption(BattlePvPHelenaMark).nOption);
            outPacket.encodeInt(getOption(BattlePvPHelenaMark).rOption);
            outPacket.encodeInt(getOption(BattlePvPHelenaMark).cOption);
        }
        if (ctsSet.contains(BattlePvPLangEProtection)) {
            outPacket.encodeInt(getOption(BattlePvPLangEProtection).nOption);
            outPacket.encodeInt(getOption(BattlePvPLangEProtection).rOption);
        }
        if (ctsSet.contains(MichaelSoulLink)) {
            outPacket.encodeInt(getOption(MichaelSoulLink).xOption);
            outPacket.encodeByte(getOption(MichaelSoulLink).bOption);
            outPacket.encodeInt(getOption(MichaelSoulLink).cOption);
            outPacket.encodeInt(getOption(MichaelSoulLink).yOption);
        }
        if (ctsSet.contains(AdrenalinBoost)) {
            outPacket.encodeByte(getOption(AdrenalinBoost).cOption);
        }
        if (ctsSet.contains(Stigma)) {
            outPacket.encodeInt(getOption(Stigma).bOption);
        }
        if (getStopForceAtom() != null) {
            getStopForceAtom().encode(outPacket);
        } else {
            new StopForceAtom().encode(outPacket);
        }
        outPacket.encodeInt(getViperEnergyCharge());
        for (int i = 0; i < TSIndex.values().length; i++) {
            if (ctsSet.contains(TSIndex.getCTSFromTwoStatIndex(i))) {
                getTwoStates().get(i).encode(outPacket);
            }
        }
        if (ctsSet.contains(NewFlying)) {
            outPacket.encodeInt(getOption(NewFlying).xOption);
        }
    }



    // BASE STAT INTERFACE ---------------------------------------------------------------------------------------------

    public Map<BaseStat, Integer> getBaseStats() {
        if (baseStatChanged) {
            recomputeBaseStats();
        }
        return baseStatCache;
    }

    public Map<BaseStat, Set<Integer>> getNonAddBaseStats() {
        if (baseStatChanged) {
            recomputeBaseStats();
        }
        return nonAddBaseStatCache;
    }

    private void recomputeBaseStats() {
        lock.lock();
        try {
            Map<BaseStat, Integer> baseStats = new HashMap<>();
            Map<BaseStat, Set<Integer>> nonAddBaseStats = new HashMap<>();
            stats.forEach((cts, option) -> {
                BaseStat.getFromCTS(chr, cts, option).forEach((bs, value) -> {
                    if (!bs.isNonAdditiveStat()) {
                        baseStats.put(bs, baseStats.getOrDefault(bs, 0) + value);
                    } else {
                        if (!nonAddBaseStats.containsKey(bs)) {
                            nonAddBaseStats.put(bs, new HashSet<>());
                        }
                        nonAddBaseStats.get(bs).add(value);
                    }
                });
            });
            indieStats.forEach((cts, options) -> {
                options.forEach((option) -> {
                    BaseStat.getFromCTS(chr, cts, option).forEach((bs, value) -> {
                        if (!bs.isNonAdditiveStat()) {
                            baseStats.put(bs, baseStats.getOrDefault(bs, 0) + value);
                        } else {
                            if (!nonAddBaseStats.containsKey(bs)) {
                                nonAddBaseStats.put(bs, new HashSet<>());
                            }
                            nonAddBaseStats.get(bs).add(value);
                        }
                    });
                });
            });
            // update cache
            baseStatCache = baseStats;
            nonAddBaseStatCache = nonAddBaseStats;
            baseStatChanged = false;
        } finally {
            lock.unlock();
        }
    }



    // OTHER MEMBERS ---------------------------------------------------------------------------------------------------

    public List<OutPacket> getToBroadcastAfterMigrate() {
        return toBroadcastAfterMigrate;
    }

    public List<TemporaryStatBase> getTwoStates() {
        return twoStates;
    }

    public TemporaryStatBase getTSBByTSIndex(TSIndex tsi) {
        return getTwoStates().get(tsi.getIndex());
    }

    public Set<AffectedArea> getAffectedAreas() {
        return affectedAreas;
    }

    public void addAffectedArea(AffectedArea aa) {
        getAffectedAreas().add(aa);
    }

    public void removeAffectedArea(AffectedArea aa) {
        getAffectedAreas().remove(aa);
        if (aa.isRemoveSkill()) {
            removeStatsBySkill(aa.getSkillID());
            sendResetStatPacket();
        } else if (aa.getSkillID() == SUPPORT_UNIT_HEX || aa.getSkillID() == ENHANCED_SUPPORT_UNIT) {
            removeStatsBySkill(IndieAsrR, aa.getSkillID());
            sendResetStatPacket();
        }
    }

    public boolean hasAffectedArea(AffectedArea affectedArea) {
        return getAffectedAreas().contains(affectedArea);
    }

    public boolean hasDebuffs() {
        return hasStat(CharacterTemporaryStat.Stun) ||
                hasStat(CharacterTemporaryStat.Poison) ||
                hasStat(CharacterTemporaryStat.Seal) ||
                hasStat(CharacterTemporaryStat.Darkness) ||
                hasStat(CharacterTemporaryStat.Thaw) ||
                hasStat(CharacterTemporaryStat.Weakness) ||
                hasStat(CharacterTemporaryStat.Curse) ||
                hasStat(CharacterTemporaryStat.Slow) ||
                hasStat(CharacterTemporaryStat.Blind);
    }

    public void removeAllDebuffs() {
        removeStat(CharacterTemporaryStat.Stun, false);
        removeStat(CharacterTemporaryStat.Poison, false);
        removeStat(CharacterTemporaryStat.Seal, false);
        removeStat(CharacterTemporaryStat.Darkness, false);
        removeStat(CharacterTemporaryStat.Thaw, false);
        removeStat(CharacterTemporaryStat.Weakness, false);
        removeStat(CharacterTemporaryStat.Curse, false);
        removeStat(CharacterTemporaryStat.Slow, false);
        removeStat(CharacterTemporaryStat.Blind, false);
        sendResetStatPacket();
    }

    public void handleWarp(Field toField) {
        // clean up affected areas
        for (AffectedArea aa : getAffectedAreas()) {
            removeStatsBySkill(aa.getSkillID());
        }
        getAffectedAreas().clear();
        // remove other skills
        for (int skill : Job.REMOVE_ON_WARP) {
            if (hasStatBySkillId(skill)) {
                removeStatsBySkill(skill);
            }
        }
        if (hasStat(Flying) && !toField.isFly()) {
            removeStat(Flying, false);
        }
        // handle summons
        List<Tuple<CharacterTemporaryStat, Option>> indieStatsToRemove = new ArrayList<>();
        indieStats.forEach((cts, options) -> {
            for (Option option : options) {
                Summon summon = option.summon;
                if (summon != null) {
                    if (summon.getMoveAbility().changeFieldWithOwner()) {
                        summon.setObjectId(toField.getNewObjectID());
                        toField.spawnSummon(summon);
                    } else {
                        indieStatsToRemove.add(new Tuple<>(cts, option));
                    }
                }
            }
        });
        indieStatsToRemove.forEach(this::removeIndieStat);
        sendResetStatPacket();
    }

    public void addSoulMPFromMobDeath() {
        if (hasStat(CharacterTemporaryStat.SoulMP)) {
            Option o1 = getOption(SoulMP);
            o1.nOption = Math.min(ItemConstants.MAX_SOUL_CAPACITY, o1.nOption + ItemConstants.MOB_DEATH_SOUL_MP_COUNT);
            putCharacterStatValue(SoulMP, o1);
            if (o1.nOption >= ItemConstants.MAX_SOUL_CAPACITY && !hasStat(FullSoulMP)) {
                Option o2 = new Option();
                o2.rOption = ItemConstants.getSoulSkillFromSoulID(((Equip) chr.getEquippedItemByBodyPart(BodyPart.Weapon)).getSoulOptionId());
                if (o2.rOption == 0) {
                    chr.chatMessage(String.format("Unknown corresponding skill for soul socket id %d!",
                            ((Equip) chr.getEquippedItemByBodyPart(BodyPart.Weapon)).getSoulOptionId()));
                }
                o2.nOption = ItemConstants.MAX_SOUL_CAPACITY;
                o2.xOption = ItemConstants.MAX_SOUL_CAPACITY;
                putCharacterStatValue(FullSoulMP, o2);
            }
            sendSetStatPacket();
        }
    }

    public void increaseGolluxStack() {
        int maxStacks = 5;
        int stacks = hasStat(Stigma) ? getOption(Stigma).nOption : 0;
        stacks++;
        if (stacks >= maxStacks) {
            chr.heal(-chr.getMaxHP());
            stacks = maxStacks;
        }
        Option o1 = new Option();
        o1.nOption = stacks;
        o1.rOption = 800;
        o1.bOption = maxStacks;
        putCharacterStatValue(Stigma, o1);
        sendSetStatPacket();
    }



    // JOB-SPECIFIC MEMBERS --------------------------------------------------------------------------------------------

    public int getViperEnergyCharge() {
        return viperEnergyCharge;
    }

    public void setViperEnergyCharge(int viperEnergyCharge) {
        this.viperEnergyCharge = viperEnergyCharge;
    }

    public void throwDice(int roll) {
        throwDice(roll, 0);
    }

    public void throwDice(int roll1, int roll2) {
        int[] diceOption = new int[8];
        if (roll1 == roll2) {
            diceOption[roll1] = DICE_STAT_VALUE_DD[roll1];
        } else {
            diceOption[roll1] = DICE_STAT_VALUE[roll1];
            diceOption[roll2] = DICE_STAT_VALUE[roll2];
        }
        diceInfo = new int[] {
                diceOption[3],  //nOption 3 (MHPR)
                diceOption[3],  //nOption 3 (MMPR)
                diceOption[4],  //nOption 4 (Cr)
                0,  // CritDamage Min
                0,  // ???  ( CritDamage Max (?) )
                0,  // EVAR
                0,  // AR
                0,  // ER
                diceOption[2],  //nOption 2 (PDDR)
                diceOption[2],  //nOption 2 (MDDR)
                0,  // PDR
                0,  // MDR
                diceOption[5],  //nOption 5 (PIDR)
                0,  // PDamR
                0,  // MDamR
                0,  // PADR
                0,  // MADR
                diceOption[6], //nOption 6 (EXP)
                diceOption[7], //nOption 7 (IED)
                0,  // ASRR
                0,  // TERR
                0,  // MesoRate
                0,
        };
    }

    public LarknessManager getLarknessManager() {
        return larknessManager;
    }

    public void setLarknessManager(LarknessManager larknessManager) {
        this.larknessManager = larknessManager;
    }

    public StopForceAtom getStopForceAtom() {
        return stopForceAtom;
    }

    public void setStopForceAtom(StopForceAtom stopForceAtom) {
        this.stopForceAtom = stopForceAtom;
    }

}
