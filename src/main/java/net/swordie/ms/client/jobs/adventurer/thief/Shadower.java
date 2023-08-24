package net.swordie.ms.client.jobs.adventurer.thief;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.drop.Drop;
import net.swordie.ms.life.drop.DropInfo;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Shadower extends Thief {
    public static final int DAGGER_MASTERY = 4200000;
    public static final int STEAL = 4201004; //Special Attack (Steal Debuff)?
    public static final int DAGGER_BOOSTER = 4201002; //Buff
    public static final int SHIELD_MASTERY = 4200010;
    public static final int MESO_GUARD = 4201011; //Buff
    public static final int CRITICAL_GROWTH = 4200013; //Passive Crit increasing buff
    public static final int SHADOW_PARTNER_SHAD = 4211008; //Buff
    public static final int DARK_FLARE_SHAD = 4211007; //Summon
    public static final int MESO_MASTERY = 4210012;
    public static final int PICK_POCKET = 4211003; //Buff
    public static final int MESO_EXPLOSION = 4211006; //CreateForceAtom Attack
    public static final int MESO_EXPLOSION_ATOM = 4210014;
    public static final int ADVANCED_DARK_SIGHT_SHAD = 4210015;
    public static final int VENOM_SHAD = 4210010; //Passive DoT
    public static final int DAGGER_EXPERT = 4220012;
    public static final int BOOMERANG_STAB = 4221007; //Special Attack (Stun Debuff)
    public static final int MAPLE_WARRIOR_SHAD = 4221000; //Buff
    public static final int SHADOWER_INSTINCT = 4221013; //Buff //Stacks (Body Count)
    public static final int SUDDEN_RAID_SHAD = 4221010; //Special Attack
    public static final int SMOKE_SCREEN = 4221006; //Affected Area
    public static final int PRIME_CRITICAL = 4220015; //Passive Buff
    public static final int ASSASSINATE = 4221014;
    public static final int TOXIC_VENOM_SHAD = 4220011; //Passive DoT
    public static final int HEROS_WILL_SHAD = 4221008;

    public static final int EPIC_ADVENTURE_SHAD = 4221053;
    public static final int SHADOW_VEIL = 4221052;
    public static final int FLIP_THE_COIN = 4221054;
    public static final int MESO_EXPLOSION_ENHANCE = 4220045;

    private int critAmount;
    private int supposedCrit;
    private final int MAX_CRIT = 100;
    private ScheduledFuture critGrowthTimer;

    public Shadower(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            critGrowthTimer = EventManager.addFixedRateEvent(this::incrementCritGrowing, 2000, 2000); // 4200013 subTime
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isShadower(id);
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
            if (skillID != MESO_EXPLOSION_ATOM) {
                handlePickPocket(attackInfo);
            }
            incrementCritGrowing();
            handleBodyCount(skillID);
            activateFlipTheCoin(attackInfo);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case STEAL:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                Field field = chr.getField();
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (!mob.isBoss()) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                    if (!mob.isSteal() && Util.succeedProp(si.getValue(prop, slv))) {
                        int itemId = 2431835; // si.getValue(x, slv);
                        if (mob.isBoss()) {
                            itemId = 2431850; // si.getValue(y, slv);
                        }
                        Item item = ItemData.getItemDeepCopy(itemId);
                        Drop drop = new Drop(item.getItemId(), item);
                        field.drop(drop, mob.getPosition());
                        mob.setSteal(true);
                        chr.write(MobPool.stealEffect(mob, itemId));
                    }
                }
                break;
            case BOOMERANG_STAB:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void incrementCritGrowing() {
        int skillId = 0;
        if (chr.hasSkill(PRIME_CRITICAL)) {
            skillId = PRIME_CRITICAL;
        } else if (chr.hasSkill(CRITICAL_GROWTH)) {
            skillId = CRITICAL_GROWTH;
        }
        if (skillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int cr = 0;
        if (tsm.hasStat(CriticalGrowing)) {
            cr = tsm.getOption(CriticalGrowing).nOption;
            if (cr >= 100) {
                cr = 0;
            }
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        Option o1 = new Option();
        o1.nOption = cr + si.getValue(x, slv);
        o1.rOption = skillId;
        tsm.putCharacterStatValue(CriticalGrowing, o1);
        tsm.sendSetStatPacket();
    }

    private void handlePickPocket(AttackInfo attackInfo) {
        if (!chr.hasSkill(PICK_POCKET)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(PickPocket)) {
            SkillInfo si = SkillData.getSkillInfoById(PICK_POCKET);
            int slv = chr.getSkillLevel(PICK_POCKET);
            int proc = si.getValue(prop, slv) + chr.getSkillStatValue(u, MESO_MASTERY) + chr.getSkillStatValue(prop, MESO_EXPLOSION_ENHANCE);
            int meso = si.getValue(x, slv);
            Field field = chr.getField();
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) field.getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                Set<DropInfo> dropInfoSet = new HashSet<>();
                for (int i = 0; i < mai.damages.length; i++) {
                    if (mai.damages[i] > 0 && dropInfoSet.size() < GameConstants.PICK_POCKET_MAX_MESOS && Util.succeedProp(proc)) {
                        dropInfoSet.add(new DropInfo(GameConstants.MAX_DROP_CHANCE, meso, meso));
                    }
                }
                if (dropInfoSet.size() > 0) {
                    field.drop(dropInfoSet, mob.getPosition(), chr.getId());
                }
            }
        }
    }

    private void handleBodyCount(int skillId) {
        if (!chr.hasSkill(SHADOWER_INSTINCT)) {
            return;
        }
        int stacks = 0;
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(KillingPoint)) {
            stacks = tsm.getOption(KillingPoint).nOption;
            // consume 3+ killing point stacks with assassinate
            if (stacks >= 3 && skillId == ASSASSINATE) {
                tsm.removeStat(KillingPoint, false);
                tsm.sendResetStatPacket();
                return;
            }
        }
        if (stacks < 5) {
            stacks++;
        }
        Option o1 = new Option();
        o1.nOption = stacks;
        o1.rOption = SHADOWER_INSTINCT;
        tsm.putCharacterStatValue(KillingPoint, o1);
        tsm.sendSetStatPacket();
    }

    private void activateFlipTheCoin(AttackInfo attackInfo) {
        if (!chr.hasSkill(FLIP_THE_COIN)) {
            return;
        }
        if (attackInfo.didCrit(chr)) {
            chr.write(WvsContext.flipTheCoinEnabled((byte) 1));
        }
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
        Summon summon;
        Field field;
        switch (skillID) {
            case SMOKE_SCREEN:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(inPacket.decodePosition());
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                aa.setDelay((short) 4);
                chr.getField().spawnAffectedArea(aa);
                break;
            case MESO_EXPLOSION:
                int rectRange = si.getValue(range, slv);
                Rect rect = new Rect(
                        new Position(
                                chr.getPosition().getX() - rectRange,
                                chr.getPosition().getY() - rectRange),
                        new Position(
                                chr.getPosition().getX() + rectRange,
                                chr.getPosition().getY() + rectRange)
                );
                List<Drop> dropList = chr.getField().getDropsInRect(rect).stream()
                        .filter(d -> d.isMoney() &&
                                (d.getOwnerID() == 0 || d.getOwnerID() == chr.getId()))
                        .limit(si.getValue(bulletCount, slv) + this.chr.getSkillStatValue(bulletCount, MESO_EXPLOSION_ENHANCE))
                        .collect(Collectors.toList());
                createMesoExplosionForceAtom(dropList);
                break;
            case SHADOW_VEIL:
                chr.write(UserLocal.skillUseResult((byte) 1, skillID));
                break;
            case MESO_GUARD:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(MesoGuard, o1);
                break;
            case PICK_POCKET:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PickPocket, o1);
                break;
            case SHADOWER_INSTINCT:
                int pad = si.getValue(x, slv);
                if (tsm.hasStat(KillingPoint)) {
                    pad += si.getValue(kp, slv) * tsm.getOption(KillingPoint).nOption;
                    tsm.removeStat(KillingPoint, false);
                    tsm.sendResetStatPacket();
                }
                o1.nOption = pad;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PAD, o1);
                break;
            case FLIP_THE_COIN:
                int stacks = 1;
                if (tsm.hasStat(FlipTheCoin)) {
                    stacks = tsm.getOption(FlipTheCoin).nOption;
                    if (stacks < si.getValue(y, 1)) {
                        stacks++;
                    }
                }
                o1.nOption = stacks;
                o1.rOption = FLIP_THE_COIN;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(FlipTheCoin, o1);
                o2.nOption = stacks * si.getValue(x, slv);
                o2.rOption = FLIP_THE_COIN;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o1);
                o2.nReason = FLIP_THE_COIN;
                o2.nValue = stacks * si.getValue(indieDamR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o2);
                chr.write(WvsContext.flipTheCoinEnabled((byte) 0));
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void createMesoExplosionForceAtom(List<Drop> droplist) {
        int dropCount = droplist.size();
        if (dropCount == 0) {
            return;
        }
        Rect rect = new Rect(
                chr.getPosition().getX() - 800, chr.getPosition().getY() - 800,
                chr.getPosition().getX() + 800, chr.getPosition().getY() + 800
        );
        List<Mob> targets = chr.getField().getMobsInRect(rect);
        if (targets.size() == 0) {
            return;
        }
        Field field = chr.getField();
        ForceAtomEnum fae = ForceAtomEnum.FLYING_MESO;
        List<ForceAtomInfo> faiList = new ArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        int angleStart = Util.getRandom((360 / dropCount)-1);
        for (int i = 0; i < dropCount; i++) {
            Drop drop = droplist.get(i);
            Mob target = Util.getRandomFromCollection(targets);
            if (target == null) {
                continue;
            }
            int angle = (360 / dropCount) * i;
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc(), 45, 4,
                    angleStart + angle, 0, Util.getCurrentTime(), 1, 0, chr.getPosition().delta(drop.getPosition()));
            faiList.add(forceAtomInfo);
            targetList.add(target.getObjectId());
            field.removeDrop(drop.getObjectId(), 0, true, -1);
        }
        chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(),
                true, targetList, MESO_EXPLOSION_ATOM, faiList, null, 0, 0,
                null, 0, null));
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (hitInfo.hpDamage > 2 && tsm.hasStat(MesoGuard)) {
            int guardRate = 50 + chr.getSkillStatValue(v, MESO_MASTERY);
            int dmgGuarded = (int) (hitInfo.hpDamage / (100D / guardRate));
            int mesoLoss = (int) (dmgGuarded / (100D / tsm.getOption(MesoGuard).nOption));
            if (chr.getMoney() < mesoLoss) {
                chr.deductMoney(chr.getMoney());
                tsm.removeStat(MesoGuard, false);
                tsm.sendResetStatPacket();
            } else {
                chr.deductMoney(mesoLoss);
            }
            hitInfo.hpDamage = hitInfo.hpDamage - dmgGuarded;
        }
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleRemoveSkill(int skillID) {
        if (skillID == CRITICAL_GROWTH && critGrowthTimer != null && !critGrowthTimer.isDone()) {
            critGrowthTimer.cancel(true);
        }
        super.handleRemoveSkill(skillID);
    }

    @Override
    public void handleCancelTimer() {
        if (critGrowthTimer != null && !critGrowthTimer.isDone()) {
            critGrowthTimer.cancel(true);
        }
        super.handleCancelTimer();
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}