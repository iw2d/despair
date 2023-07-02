package net.swordie.ms.client.jobs.adventurer.thief;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
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
import net.swordie.ms.life.mob.MobTemporaryStat;
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
    public static final int MESOGUARD = 4201011; //Buff
    public static final int CRITICAL_GROWTH = 4200013; //Passive Crit increasing buff
    public static final int SHADOW_PARTNER_SHAD = 4211008; //Buff
    public static final int DARK_FLARE_SHAD = 4211007; //Summon
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
        if (critGrowthTimer != null && !critGrowthTimer.isDone()) {
            critGrowthTimer.cancel(true);
        }
        critGrowthTimer = EventManager.addFixedRateEvent(this::incrementCritGrowing, 2000, 2000);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isShadower(id);
    }


    private void incrementFlipTheCoinStack(TemporaryStatManager tsm) {
        Option o = new Option();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        SkillInfo FlipTheCoinInfo = SkillData.getSkillInfoById(FLIP_THE_COIN);
        int amount = 1;
        if (tsm.hasStat(FlipTheCoin)) {
            amount = tsm.getOption(FlipTheCoin).nOption;
            if (amount < FlipTheCoinInfo.getValue(y, 1)) {
                amount++;
            }
        }
        o.nOption = amount;
        o.rOption = FLIP_THE_COIN;
        o.tOption = FlipTheCoinInfo.getValue(time, 1);
        tsm.putCharacterStatValue(FlipTheCoin, o);

        //Stats
        o1.nOption = (amount * FlipTheCoinInfo.getValue(x, 1));
        o1.rOption = FLIP_THE_COIN;
        o1.tOption = FlipTheCoinInfo.getValue(time, 1);
        tsm.putCharacterStatValue(CriticalBuff, o1);
        o2.nReason = FLIP_THE_COIN;
        o2.nValue = (amount * FlipTheCoinInfo.getValue(indieDamR, 1));
        o2.tStart = (int) System.currentTimeMillis();
        o2.tTerm = FlipTheCoinInfo.getValue(time, 1);
        tsm.putCharacterStatValue(IndieDamR, o2);
        o3.nReason = FLIP_THE_COIN;
        o3.nValue = (amount * FlipTheCoinInfo.getValue(indieMaxDamageOver, 1));
        o3.tStart = (int) System.currentTimeMillis();
        o3.tTerm = FlipTheCoinInfo.getValue(time, 1);
        tsm.putCharacterStatValue(IndieMaxDamageOver, o3);
        tsm.sendSetStatPacket();
    }

    private void activateFlipTheCoin(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        long totalCrit = chr.getBaseStats().get(BaseStat.cr);
        totalCrit += tsm.getOption(CriticalBuff).nOption + tsm.getOption(CriticalGrowing).nOption;
        if (Util.succeedProp((int) (totalCrit > 100 ? 100 : totalCrit))) {
            chr.write(WvsContext.flipTheCoinEnabled((byte) 1));
        }
    }

    private void updatecrit() {
        if (chr.hasSkill(PRIME_CRITICAL)) {
            supposedCrit = supposedCrit + chr.getSkillStatValue(x, PRIME_CRITICAL);
        } else if (chr.hasSkill(CRITICAL_GROWTH)) {
            supposedCrit = supposedCrit + chr.getSkillStatValue(x, CRITICAL_GROWTH);
        } else {
            return;
        }
        Option o = new Option();
        int critGrowth = critAmount;
        o.nOption = (getPrimeCritMulti() * critGrowth);
        o.rOption = getCritGrowIcon();
        chr.getTemporaryStatManager().putCharacterStatValue(CriticalGrowing, o);
        chr.getTemporaryStatManager().sendSetStatPacket();
    }

    private void incrementCritGrowth(int stackIncrease) {
        if(supposedCrit > 100) {
            critAmount = 1; //TODO returns to starting crit% even if another crit buff is active
            supposedCrit = 0;
        } else {
            critAmount += stackIncrease;
        }
        critAmount = Math.min(MAX_CRIT, critAmount);
        updatecrit();
    }

    public void incrementCritGrowing() {
        incrementCritGrowth(1);   //Crit Growing
    }

    private int getCritGrowIcon() {
        if(chr.hasSkill(PRIME_CRITICAL)) {
            return PRIME_CRITICAL;
        } else {
            return CRITICAL_GROWTH;
        }
    }

    private int getPrimeCritMulti() {
        int multiplier = 2;
        if(chr.hasSkill(PRIME_CRITICAL)) {
            multiplier = 4;
        }
        return multiplier;
    }

    @Override
    public void handleCancelTimer(Char chr) {
        if (critGrowthTimer != null) {
            critGrowthTimer.cancel(true);
        }
    }

    private void incrementShadowInstinct(int skillId, TemporaryStatManager tsm) {
        Option o = new Option();
        SkillInfo InstinctInfo = SkillData.getSkillInfoById(SHADOWER_INSTINCT);
        Skill skill = chr.getSkill(SHADOWER_INSTINCT);
        byte slv = (byte) skill.getCurrentLevel();
        int amount = 1;
        if (tsm.hasStat(KillingPoint)) {
            if (chr.hasSkill(SHADOWER_INSTINCT)) {
                amount = tsm.getOption(KillingPoint).nOption;
                if (amount < 5) {
                    amount++;
                }
            }
        }
        o.nOption = amount;
        tsm.putCharacterStatValue(KillingPoint, o);
        tsm.sendSetStatPacket();
    }



    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        byte slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            skillID = SkillConstants.getActualSkillIDfromSkillID(skill.getSkillId());
        }
        if (hasHitMobs) {
            if (chr.hasSkill(CRITICAL_GROWTH)) {
                incrementCritGrowing();
            }
            if (attackInfo.skillId != MESO_EXPLOSION_ATOM) {
                dropFromPickPocket(attackInfo);
            }
            if (chr.hasSkill(SHADOWER_INSTINCT) && tsm.hasStat(IgnoreMobpdpR)) {
                incrementShadowInstinct(skillID, tsm);
            }
            if (chr.hasSkill(FLIP_THE_COIN)) {
                activateFlipTheCoin(attackInfo);
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case STEAL:
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        Field field = chr.getField();
                        int itemId = 2431835;
                        if (mob.isBoss()) {
                            itemId = 2431850;
                        }
                        Item item = ItemData.getItemDeepCopy(itemId);
                        Drop drop = new Drop(item.getItemId(), item);
                        field.drop(drop, mob.getPosition());

                        if (!mob.isBoss()) {
                            mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                        }
                    }
                }
                break;
            case BOOMERANG_STAB:
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null || mob.isBoss()) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void createMesoExplosionForceAtom(List<Drop> droplist) {
        if (!chr.hasSkill(MESO_EXPLOSION)) {
            return;
        }
        Field field = chr.getField();
        ForceAtomEnum fae = ForceAtomEnum.FLYING_MESO;
        List<Integer> targetList = new ArrayList<>();
        List<ForceAtomInfo> faiList = new ArrayList<>();
        for (Drop drop : droplist) {
            Mob mob = Util.getRandomFromCollection(field.getMobs());
            if (mob != null) {
                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc(), 2, 3,
                        0, 0, Util.getCurrentTime(), 1, 0,
                        drop.getPosition());
                targetList.add(mob.getObjectId());
                faiList.add(forceAtomInfo);
                field.removeDrop(drop.getObjectId(), 0, true, -1);
            }
        }
        chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(),
                true, targetList, MESO_EXPLOSION_ATOM, faiList, new Rect(), 0, 300,
                new Position(), MESO_EXPLOSION_ATOM, new Position()));
    }

    private void dropFromPickPocket(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Field field = chr.getField();
        if(!chr.hasSkill(PICK_POCKET)) {
            return;
        }
        if(tsm.getOptByCTSAndSkill(PickPocket, PICK_POCKET) != null) {
            for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) field.getLifeByObjectID(mai.mobId);
                if(mob == null) {
                    continue;
                }
                Skill skill = chr.getSkill(PICK_POCKET);
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                byte slv = (byte) skill.getCurrentLevel();
                Set<DropInfo> dropInfoSet = new HashSet<>();
                int proc = si.getValue(prop, slv) + chr.getSkillStatValue(prop, MESO_EXPLOSION_ENHANCE);
                for (int i = 0; i < slv; i++) {
                    if (Util.succeedProp(proc)) {
                        dropInfoSet.add(new DropInfo(GameConstants.MAX_DROP_CHANCE, 50, 150)); // min 50; max 150;
                    }
                }
                if (dropInfoSet.size() > 0) {
                    field.drop(dropInfoSet, mob.getPosition(), chr.getId());
                }
            }
        }
    }



    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case SMOKE_SCREEN:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                aa.setDelay((short) 4);
                chr.getField().spawnAffectedArea(aa);
                break;
            case MESO_EXPLOSION:
                field = chr.getField();
                int rectRange = si.getValue(range, slv);
                Rect rect = new Rect(
                        new Position(
                                chr.getPosition().getX() - rectRange,
                                chr.getPosition().getY() - rectRange),
                        new Position(
                                chr.getPosition().getX() + rectRange,
                                chr.getPosition().getY() + rectRange)
                );
                List<Drop> dropList = field.getDropsInRect(rect).stream()
                        .filter(d -> d.getOwnerID() == chr.getId()
                                && d.isMoney())
                        .limit(si.getValue(bulletCount, slv) + chr.getSkillStatValue(bulletCount, MESO_EXPLOSION_ENHANCE))
                        .collect(Collectors.toList());
                createMesoExplosionForceAtom(dropList);
                break;
            case SHADOW_VEIL:
                chr.write(UserLocal.skillUseResult((byte) 1, skillID));
                break;
            case MESOGUARD:
                o1.nOption = si.getValue(x, slv);
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
                }
                o1.nOption = pad;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PAD, o1);
                o2.nOption = si.getValue(ignoreMobpdpR, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IgnoreMobpdpR, o2);
                break;
            case FLIP_THE_COIN:
                incrementFlipTheCoinStack(tsm);
                chr.write(WvsContext.flipTheCoinEnabled((byte) 0));
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, InPacket inPacket, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(MesoGuard)) {
            Skill skill = chr.getSkill(MESOGUARD);
            SkillInfo si = SkillData.getSkillInfoById(MESOGUARD);
            int dmg = hitInfo.hpDamage;
            int mesoLoss = (int) (dmg * (si.getValue(x, skill.getCurrentLevel()) / 100D));
            if (chr.getMoney() < mesoLoss) {
                chr.deductMoney(chr.getMoney());
                tsm.removeStat(MesoGuard, false);
            } else {
                chr.deductMoney(mesoLoss);
            }
            hitInfo.hpDamage = (int) (dmg * 0.5);
        }
        super.handleHit(chr, inPacket, hitInfo);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}