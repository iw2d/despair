package net.swordie.ms.client.jobs.resistance.demon;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.enums.Stat;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class DemonAvenger extends Job {
    public static final int EXCEED = 30010230;
    public static final int HYPER_POTION_MASTERY = 30010231;
    public static final int STAR_FORCE_CONVERSION = 30010232;
    public static final int BLOOD_PACT = 30010242;

    public static final int EXCEED_DOUBLE_SLASH_1 = 31011000;
    public static final int EXCEED_DOUBLE_SLASH_2 = 31011004;
    public static final int EXCEED_DOUBLE_SLASH_3 = 31011005;
    public static final int EXCEED_DOUBLE_SLASH_4 = 31011006;
    public static final int EXCEED_DOUBLE_SLASH_PURPLE = 31011007;
    public static final int OVERLOAD_RELEASE = 31011001;
    public static final int LIFE_SAP = 31010002;

    public static final int EXCEED_DEMON_STRIKE_1 = 31201000;
    public static final int EXCEED_DEMON_STRIKE_2 = 31201007;
    public static final int EXCEED_DEMON_STRIKE_3 = 31201008;
    public static final int EXCEED_DEMON_STRIKE_4 = 31201009;
    public static final int EXCEED_DEMON_STRIKE_PURPLE = 31201010;
    public static final int DESPERADO_MASTERY = 31200005;
    public static final int BATTLE_PACT_DA = 31201002;
    public static final int BAT_SWARM = 31201001;

    public static final int EXCEED_LUNAR_SLASH_1 = 31211000;
    public static final int EXCEED_LUNAR_SLASH_2 = 31211007;
    public static final int EXCEED_LUNAR_SLASH_3 = 31211008;
    public static final int EXCEED_LUNAR_SLASH_4 = 31211009;
    public static final int EXCEED_LUNAR_SLASH_PURPLE = 31211010;
    public static final int VITALITY_VEIL = 31211001;
    public static final int SHIELD_CHARGE = 31211002;
    public static final int SHIELD_CHARGE_PUSH = 31211011;
    public static final int DIABOLIC_RECOVERY = 31211004;
    public static final int WARD_EVIL = 31211003;
    public static final int ADVANCED_LIFE_SAP = 31210006;
    public static final int PAIN_DAMPENER = 31210005;

    public static final int EXCEED_EXECUTION_1 = 31221000;
    public static final int EXCEED_EXECUTION_2 = 31221009;
    public static final int EXCEED_EXECUTION_3 = 31221010;
    public static final int EXCEED_EXECUTION_4 = 31221011;
    public static final int EXCEED_EXECUTION_PURPLE = 31221012;
    public static final int DEFENSE_EXPERTISE = 31220005;
    public static final int NETHER_SHIELD = 31221001;
    public static final int NETHER_SHIELD_ATOM = 31221014;
    public static final int NETHER_SLICE = 31221002;
    public static final int BLOOD_PRISON = 31221003;
    public static final int MAPLE_WARRIOR_DA = 31221008;
    public static final int INFERNAL_EXCEED = 31220007;

    public static final int EXCEED_REDUCE_OVERLOAD = 31220044;
    public static final int EXCEED_OPPORTUNITY = 31220044;
    public static final int WARD_EVIL_HARDEN = 31220046;
    public static final int WARD_EVIL_IMMUNITY_1 = 31220047;
    public static final int WARD_EVIL_IMMUNITY_2 = 31220048;
    public static final int NETHER_SHIELD_SPREAD = 31220050;
    public static final int NETHER_SHIELD_RANGE = 31220051;
    public static final int DEMONIC_FORTITUDE_DA = 31221053;
    public static final int FORBIDDEN_CONTRACT = 31221054;
    public static final int THOUSAND_SWORDS = 31221052;

    private Map<Integer, Integer> shieldBounceMap = new ConcurrentHashMap<>();
    private ScheduledFuture diabolicRecoveryTimer;

    public DemonAvenger(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isDemonAvenger(id);
    }

    public void sendHpUpdate() {
        // Used for client side damage calculation for DAs
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        o.nOption = 3; // Hp -> damage conversion
        o.mOption = chr.getStat(Stat.mhp);
        tsm.putCharacterStatValue(LifeTidal, o);
        tsm.sendSetStatPacket();
    }

    private void diabolicRecovery() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
		if (tsm.hasStat(DiabolikRecovery)) {
            SkillInfo si = SkillData.getSkillInfoById(DIABOLIC_RECOVERY);
            int slv = chr.getSkillLevel(DIABOLIC_RECOVERY);
            int healRate = si.getValue(x, slv);
            chr.heal((int) (chr.getMaxHP() * ((double) healRate / 100D)));
            diabolicRecoveryTimer = EventManager.addEvent(this::diabolicRecovery, si.getValue(w, slv), TimeUnit.SECONDS);
        }
    }

    private boolean isExceedOverloadSkill(int skillId) {
        switch (skillId) {
            case EXCEED_DOUBLE_SLASH_PURPLE:
            case EXCEED_DEMON_STRIKE_PURPLE:
            case EXCEED_LUNAR_SLASH_PURPLE:
            case EXCEED_EXECUTION_PURPLE:
                return true;
            default:
                return false;
        }
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        if (hasHitMobs) {
            handleLifeSap(attackInfo);
        }
        if (isExceedOverloadSkill(attackInfo.skillId)) {
            incrementExceedOverload(1);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case BLOOD_PRISON:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                }
                break;
            case SHIELD_CHARGE_PUSH:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                }
                break;
            case NETHER_SLICE:
                o1.nOption = -si.getValue(y, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptions(MobStat.PDR, o1);
                    mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                }
                break;
            case VITALITY_VEIL:
                int healRate = si.getValue(y, slv);
                if (chr.getHP() > 0) {
                    chr.heal((int) (chr.getMaxHP() * ((double) healRate / 100D)));
                }
                break;
            case THOUSAND_SWORDS:
                incrementExceedOverload(si.getValue(x, slv));
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void handleLifeSap(AttackInfo attackInfo) {
        if (!chr.hasSkill(LIFE_SAP)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(LIFE_SAP);
        int slv = chr.getSkillLevel(LIFE_SAP);
        int healRate = si.getValue(x, slv);
        // handle advanced life sap
        if (chr.hasSkill(ADVANCED_LIFE_SAP)) {
            healRate = chr.getSkillStatValue(x, ADVANCED_LIFE_SAP);
        }
        // handle reduced heal rate from exceed overload skills
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int reduceHealRate = 0;
        if (isExceedOverloadSkill(attackInfo.skillId)) {
            if (tsm.hasStat(OverloadCount)) {
                reduceHealRate = tsm.getOption(OverloadCount).nOption * chr.getSkillStatValue(y, EXCEED);
            }
            if (chr.hasSkill(PAIN_DAMPENER)) {
                reduceHealRate -= chr.getSkillStatValue(x, PAIN_DAMPENER);
            }
            healRate -= reduceHealRate;
        }
        if (healRate <= 0) {
            return;
        }
        // % healed per mob hit
        int totalHealRate = 0;
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            if (!Util.succeedProp(si.getValue(prop, slv))) {
                continue;
            }
            totalHealRate += healRate;
        }
        if (totalHealRate > 0 && chr.getHP() > 0) {
            chr.heal((int) (chr.getMaxHP() * ((double) healRate / 100D)));
        }
    }

    private int getMaxExceedOverload() {
        return chr.getSkillStatValue(x, EXCEED) - chr.getSkillStatValue(x, EXCEED_REDUCE_OVERLOAD);
    }

    private void incrementExceedOverload(int amount) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int stacks = 0;
        if (tsm.hasStat(OverloadCount)) {
            stacks = tsm.getOption(OverloadCount).nOption;
        }
        Option o1 = new Option();
        o1.nOption = Math.min(stacks + amount, getMaxExceedOverload());
        o1.rOption = EXCEED;
        o1.tOption = 0; // client handles skill cancel request
        tsm.putCharacterStatValue(OverloadCount, o1);
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleRemoveBuff(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        switch (skillId) {
            case EXCEED_DOUBLE_SLASH_1:
            case EXCEED_DEMON_STRIKE_1:
            case EXCEED_LUNAR_SLASH_1:
            case EXCEED_EXECUTION_1:
                tsm.removeStat(OverloadCount, false);
                tsm.sendResetStatPacket();
                break;
        }
        super.handleRemoveBuff(skillId);
    }

    @Override
    public int getFinalAttackSkill() {
        if (chr.hasSkill(INFERNAL_EXCEED)) {
            int proc = chr.getSkillStatValue(prop, INFERNAL_EXCEED) + chr.getSkillStatValue(prop, EXCEED_OPPORTUNITY);
            if (Util.succeedProp(proc)) {
                return INFERNAL_EXCEED;
            }
        }
        return super.getFinalAttackSkill();
    }



    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (skillID) {
            case NETHER_SHIELD:
                Position pos = inPacket.decodePosition();
                Rect rect = pos.getRectAround(getNetherShieldRect());
                if (!chr.isLeft()) {
                    rect = rect.horizontalFlipAround(pos.getX());
                }
                for (int i = 0; i < si.getValue(bulletCount, slv); i++) {
                    createNetherShieldForceAtom(rect);
                }
                break;
            case OVERLOAD_RELEASE:
                // FD and heal
                double ratio = (double) tsm.getOption(OverloadCount).nOption / getMaxExceedOverload();
                o1.nValue = (int) ((double) si.getValue(indiePMdR, slv) * ratio);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePMdR, o1);
                if (chr.getHP() > 0) {
                    int healRate = (int) ((double) si.getValue(x, slv) * ratio);
                    chr.heal((int) (chr.getMaxHP() * ((double) healRate / 100D)));
                }
                // reset overload
                tsm.removeStat(OverloadCount, false);
                tsm.sendResetStatPacket();
                break;
            case WARD_EVIL:
                o1.nOption = si.getValue(x, slv) + this.chr.getSkillStatValue(x, WARD_EVIL_HARDEN);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamageReduce, o1);
                o2.nOption = si.getValue(z, slv) + this.chr.getSkillStatValue(x, WARD_EVIL_IMMUNITY_1);
                o2.nReason = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o2);
                o3.nOption = si.getValue(y, slv) + this.chr.getSkillStatValue(x, WARD_EVIL_IMMUNITY_2);
                o3.nReason = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(TerR, o3);
                break;
            case DIABOLIC_RECOVERY: // x = HP restored at interval
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieMhpR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o1);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DiabolikRecovery, o2);
                if(diabolicRecoveryTimer != null && !diabolicRecoveryTimer.isDone()) {
                    diabolicRecoveryTimer.cancel(true);
                }
                diabolicRecovery();
                break;
            case FORBIDDEN_CONTRACT:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                // HP consumption from Skills = 0;
                break;
        }
        tsm.sendSetStatPacket();
    }

    private Rect getNetherShieldRect() {
        if (chr.hasSkill(NETHER_SHIELD_RANGE)) {
            return SkillData.getSkillInfoById(NETHER_SHIELD_RANGE).getFirstRect();
        }
        return SkillData.getSkillInfoById(NETHER_SHIELD).getFirstRect();
    }

    private int getNetherShieldMaxBounce() {
        return chr.getSkillStatValue(z, NETHER_SHIELD) + chr.getSkillStatValue(z, NETHER_SHIELD_SPREAD);
    }

    private void createNetherShieldForceAtom(Rect rect) {
        Field field = chr.getField();
        Mob target = Util.getRandomFromCollection(field.getMobsInRect(rect));
        if (target == null) {
            return;
        }
        // create force atom
        int forceAtomKey = chr.getNewForceAtomKey();
        shieldBounceMap.put(forceAtomKey, getNetherShieldMaxBounce() - 1);
        int atomSkillId = NETHER_SHIELD_ATOM;
        ForceAtomEnum fae = ForceAtomEnum.NETHER_SHIELD;
        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(forceAtomKey, fae.getInc(), 15, 30,
                0, 300, Util.getCurrentTime(), 1, 0,
                new Position(0, -100));
        field.broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(),
                true, target.getObjectId(), atomSkillId, forceAtomInfo, null, 0, 0,
                null, 0, null));
    }

    @Override
    public void handleForceAtomCollision(int forceAtomKey, int mobId) {
        // handle shield bouncing
        Integer bouncesLeft = shieldBounceMap.remove(forceAtomKey);
        if (bouncesLeft == null || bouncesLeft <= 0) {
            return;
        }
        // get source mob
        Field field = chr.getField();
        Mob source = (Mob) field.getLifeByObjectID(mobId);
        if (source == null) {
            return;
        }
        // get target mob
        Position pos = source.getPosition();
        int range = 350 + (chr.hasSkill(NETHER_SHIELD_RANGE) ? 50 : 0);
        Rect rect = new Rect( // range = 350
                pos.getX() - range, pos.getY() - range,
                pos.getX() + range, pos.getY() + range
        );
        Mob target = Util.getRandomFromCollection(field.getMobsInRect(rect));
        if (target == null) {
            return;
        }
        // create force atom
        int angle = Util.getRandom(360);
        ForceAtomEnum fae = ForceAtomEnum.NETHER_SHIELD_RECREATION;
        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(forceAtomKey, fae.getInc(), 40, 6,
                angle, 0, Util.getCurrentTime(), 1, 0,
                new Position());
        field.broadcastPacket(FieldPacket.createForceAtom(true, chr.getId(), source.getObjectId(), fae.getForceAtomType(),
                true, target.getObjectId(), NETHER_SHIELD_ATOM, forceAtomInfo, null, 0, 0,
                null, 0, null));
        shieldBounceMap.put(forceAtomKey, bouncesLeft - 1);
    }

    @Override
    public int getHpCon(int skillId, int slv) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStatBySkillId(FORBIDDEN_CONTRACT)) {
            return 0;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        if (si == null) {
            return 0;
        }
        int hpRCon = si.getValue(SkillStat.hpRCon, slv);
        if (hpRCon > 0) {
            int hpCon = (int) (chr.getMaxHP() * (hpRCon / 100D));
            if (hpCon >= chr.getHP()) {
                return 0;
            } else {
                return hpCon;
            }
        } else {
            return si.getValue(SkillStat.hpCon, slv);
        }
    }

    @Override
    public void handleRemoveSkill(int skillID) {
        if (skillID == DIABOLIC_RECOVERY && diabolicRecoveryTimer != null && !diabolicRecoveryTimer.isDone()) {
            diabolicRecoveryTimer.cancel(true);
        }
        super.handleRemoveSkill(skillID);
    }

    @Override
    public void handleCancelTimer(Char chr) {
        if (diabolicRecoveryTimer != null && !diabolicRecoveryTimer.isDone()) {
            diabolicRecoveryTimer.cancel(true);
        }
        super.handleCancelTimer(chr);
    }

    @Override
    public void handleWarp() {
        // clear bounce map
        shieldBounceMap.clear();
        super.handleWarp();
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleSetJob(short jobId) {
        if (JobConstants.isDemonAvenger(jobId)) {
            chr.addSkill(EXCEED, 1, 1);
            chr.addSkill(BLOOD_PACT, 1, 1);
            chr.addSkill(HYPER_POTION_MASTERY, 1, 1);
            // chr.addSkill(STAR_FORCE_CONVERSION, 1, 1); // TODO
        }
        super.handleSetJob(jobId);
    }
}
