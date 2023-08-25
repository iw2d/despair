package net.swordie.ms.client.jobs.adventurer.archer;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.enums.Stat;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Marksman extends Archer {
    public static final int CROSSBOW_MASTERY = 3200000;
    public static final int FINAL_ATTACK_XBOW = 3200001;
    public static final int SOUL_ARROW_XBOW = 3201004;
    public static final int XBOW_BOOSTER = 3201002;
    public static final int FREEZER = 3211005;
    public static final int HOOKSHOT = 3211010;
    public static final int NET_TOSS = 3201008;
    public static final int PAIN_KILLER = 3211011;
    public static final int RECKLESS_HUNT_XBOW = 3211012;
    public static final int MORTAL_BLOW_XBOW = 3210001;
    public static final int AGGRESSIVE_RESISTANCE = 3210013;
    public static final int EVASION_BOOST_XBOW = 3210007;
    public static final int CROSSBOW_EXPERT = 3220004;
    public static final int MAPLE_WARRIOR_XBOW = 3221000;
    public static final int SNIPE = 3221007;
    public static final int ARROW_ILLUSION = 3221014;
    public static final int SHARP_EYES_XBOW = 3221002;
    public static final int ILLUSION_STEP_XBOW = 3221006;
    public static final int HEROS_WILL_MM = 3221008;

    public static final int EPIC_ADVENTURE_XBOW = 3221053;
    public static final int BULLSEYE_SHOT = 3221054;
    public static final int SNIPE_COOLDOWN_CUTTER = 3220051;
    public static final int SHARP_EYES_XBOW_PERSIST = 3220043;
    public static final int SHARP_EYES_XBOW_IED_H = 3220044;
    public static final int SHARP_EYES_XBOW_CR_H = 3220045;
    public Marksman(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isMarksman(id);
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
            giveAggressiveResistanceBuff(attackInfo);
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case NET_TOSS:
                // boss effect
                o1.nOption = si.getValue(y, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) / 2;
                // non-boss effect
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        if (mob.isBoss()) {
                            mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Speed, o1);
                        } else {
                            mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Speed, o2);
                        }
                    }
                }
                break;
            case ARROW_ILLUSION:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(subTime, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        return;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case FREEZER:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(x, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }


    private void giveAggressiveResistanceBuff(AttackInfo ai) {
        if(!chr.hasSkill(AGGRESSIVE_RESISTANCE)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(AGGRESSIVE_RESISTANCE);
        int slv = chr.getSkillLevel(AGGRESSIVE_RESISTANCE);

        long totalDamage = 0;
        for (MobAttackInfo mai : ai.mobAttackInfo) {
            for(int dmg : mai.damages) {
                totalDamage += dmg;
            }
        }
        int shield = (int) (totalDamage / (100D / si.getValue(y, slv)));
        int shieldMax = (int) (chr.getStat(Stat.mhp) / (100D / si.getValue(z, slv)));
        Option o = tsm.getOptByCTSAndSkill(PowerTransferGauge, AGGRESSIVE_RESISTANCE);
        if (o != null) {
            shield += o.nOption;
        }
        if (shield > shieldMax) {
            shield = shieldMax;
        }

        Option o1 = new Option();
        o1.nOption = shield;
        o1.rOption = AGGRESSIVE_RESISTANCE;
        o1.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(PowerTransferGauge, o1);
        tsm.sendSetStatPacket();
    }

    @Override
    public int getFinalAttackSkill() {
        if (chr.hasSkill(FINAL_ATTACK_XBOW)) {
            SkillInfo si = SkillData.getSkillInfoById(FINAL_ATTACK_XBOW);
            int slv = chr.getSkillLevel(FINAL_ATTACK_XBOW);
            if (Util.succeedProp(si.getValue(prop, slv))) {
                return FINAL_ATTACK_XBOW;
            }
        }
        return super.getFinalAttackSkill();
    }

    @Override
    public int alterCooldownSkill(int skillId) {
        switch (skillId) {
            case SNIPE:
                if (chr.hasSkill(SNIPE_COOLDOWN_CUTTER)) {
                    return 0;
                }
        }
        return super.alterCooldownSkill(skillId);
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
        Option o5 = new Option();
        Summon summon;
        Field field;
        switch(skillID) {
            case PAIN_KILLER:
                tsm.removeAllDebuffs();
                o1.nOption = 100;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o1);
                break;
            case RECKLESS_HUNT_XBOW:
                if (tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = si.getValue(y, slv); // maxCd
                    o1.rOption = skillID;
                    o1.tOption = 0;
                    o1.xOption = si.getValue(z, slv); // minCd
                    o1.bOption = si.getValue(x, slv); // -evaR
                    tsm.putCharacterStatValue(ExtremeArchery, o1);
                }
                break;
            case ARROW_ILLUSION:
                summon = Summon.getSummonBy(chr, skillID, slv);
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setMaxHP(si.getValue(x, slv));
                Position position = inPacket.decodePosition();
                summon.setCurFoothold((short) chr.getField().findFootHoldBelow(position).getId());
                summon.setPosition(position);
                summon.setMaxHP(si.getValue(x, slv));
                summon.setHp(summon.getMaxHP());
                field = chr.getField();
                field.spawnSummon(summon);
                // reflect
                o1.nOption = si.getValue(y, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ReflectDamR, o1);
                break;
            case BULLSEYE_SHOT:
                o1.nOption = (si.getValue(x, slv) << 8) + si.getValue(y, slv); // cr << 8 + maxCd
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BullsEye, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieDamR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o2);
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieIgnoreMobpdpR, slv);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o3);
                break;
        }
        tsm.sendSetStatPacket();
    }


    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}
