package net.swordie.ms.client.jobs.adventurer.pirate;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.GuidedBullet;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.enums.TSIndex;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.List;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Corsair extends Pirate {
    public static final int GUN_MASTERY = 5200000;
    public static final int SCURVY_SUMMONS = 5201012; //Summon
    public static final int INFINITY_BLAST = 5201008; //Buff
    public static final int GUN_BOOSTER = 5201003; //Buff

    public static final int ALL_ABOARD = 5210015; //Summon
    public static final int ROLL_OF_THE_DICE_SAIR = 5211007; //Buff
    public static final int OCTO_CANNON = 5211014; //Summon

    public static final int QUICKDRAW = 5221021; //Buff
    public static final int PARROTARGETTING = 5221015; //Special Attack
    public static final int NAUTILUS_STRIKE_SAIR = 5221013; //Special Attack
    public static final int MAPLE_WARRIOR_SAIR = 5221000; //Buff
    public static final int JOLLY_ROGER = 5221018; //Buff
    public static final int PIRATE_REVENGE_SAIR = 5220012;
    public static final int ROLL_OF_THE_DICE_SAIR_DD = 5220014;
    public static final int HEROS_WILL_SAIR = 5221010;
    public static final int MAJESTIC_PRESENCE = 5220020;
    public static final int AHOY_MATEYS = 5220019;

    public static final int EPIC_ADVENTURER_SAIR = 5221053;
    public static final int WHALERS_POTION = 5221054;
    public static final int ROLL_OF_THE_DICE_SAIR_ADDITION = 5220044;
    public static final int ROLL_OF_THE_DICE_SAIR_SAVING_GRACE = 5220043;
    public static final int ROLL_OF_THE_DICE_SAIR_ENHANCE = 5220045;

    public static final List<Integer> SCURVY_SUMMONS_SUMMONS = List.of(5201012, 5201013, 5201014);
    public static final List<Integer> ALL_ABOARD_SUMMONS = List.of(5210015, 5210016, 5210017, 5210018);

    public Corsair(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isCorsair(id);
    }



    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        if (hasHitMobs) {
            activateQuickdraw();
        }
        switch (attackInfo.skillId) {
            case PARROTARGETTING:
                if (tsm.hasStat(GuidedBullet)) {
                    GuidedBullet gb = (GuidedBullet) tsm.getTSBByTSIndex(TSIndex.GuidedBullet);
                    gb.setMobID(0);
                    gb.setUserID(0);
                    tsm.removeStat(GuidedBullet, true);
                    tsm.sendResetStatPacket();
                }
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    GuidedBullet gb = (GuidedBullet) tsm.getTSBByTSIndex(TSIndex.GuidedBullet);
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    gb.setOption(o1);
                    gb.setMobID(mai.mobId);
                    gb.setUserID(chr.getId());
                    tsm.putCharacterStatValue(GuidedBullet, gb.getOption());
                    tsm.sendSetStatPacket();
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void activateQuickdraw() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.getOption(QuickDraw).nOption > 1) {
            tsm.removeStatsBySkill(QUICKDRAW);
            tsm.sendResetStatPacket();
        } else {
            SkillInfo si = SkillData.getSkillInfoById(QUICKDRAW);
            int slv = chr.getSkillLevel(QUICKDRAW);
            if (Util.succeedProp(si.getValue(prop, slv))) {
                Option o1 = new Option();
                o1.nOption = 1;
                o1.rOption = QUICKDRAW;
                tsm.putCharacterStatValue(QuickDraw, o1);
                tsm.sendSetStatPacket();
            }
        }
    }

    @Override
    public int getFinalAttackSkill() {
        if (chr.hasSkill(MAJESTIC_PRESENCE)) {
            return MAJESTIC_PRESENCE;
        }
        return super.getFinalAttackSkill();
    }

    @Override
    public void handleMobDeath(Mob mob) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(GuidedBullet)) {
            GuidedBullet gb = (GuidedBullet) tsm.getTSBByTSIndex(TSIndex.GuidedBullet);
            if (gb.getMobID() == mob.getObjectId()) {
                gb.setMobID(0);
                gb.setUserID(0);
                tsm.removeStat(GuidedBullet, true);
                tsm.sendResetStatPacket();
            }
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
        Option o4 = new Option();
        Option o5 = new Option();
        Option o6 = new Option();
        Option o7 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case INFINITY_BLAST:
                o1.nOption = chr.getBulletIDForAttack() - 2329999;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NoBulletConsume, o1);
                break;
            case QUICKDRAW:
                o1.nOption = si.getValue(damR, slv);
                o1.rOption = skillID;
                tsm.putCharacterStatValue(QuickDraw, o1);
                break;
            case JOLLY_ROGER:
                o1.nOption = si.getValue(eva, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EVA, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indiePadR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePADR, o2);
                o3.nOption = si.getValue(z, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stance, o3);
                o4.nOption = si.getValue(x, slv);
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o4);
                o5.nOption = si.getValue(x, slv);
                o5.rOption = skillID;
                o5.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(TerR, o5);
                break;
            case WHALERS_POTION:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieMhpR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o2);
                tsm.putCharacterStatValue(TerR, o2);
                o3.nOption = si.getValue(w, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamageReduce, o3);
                break;
            case OCTO_CANNON: //Stationary, Attacks
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setMoveAction((byte) 0);
                summon.setMoveAbility(MoveAbility.Stop);
                field.spawnAddSummon(summon);
                break;
            case SCURVY_SUMMONS: //Moves, Attacks
            case ALL_ABOARD: //Moves, Attacks
                corsairSummons();
                // Fallthrough intended
            case ROLL_OF_THE_DICE_SAIR:
            case ROLL_OF_THE_DICE_SAIR_DD:
                chr.reduceSkillCoolTime(NAUTILUS_STRIKE_SAIR, (long) (chr.getRemainingCoolTime(NAUTILUS_STRIKE_SAIR) * 0.5D));
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void corsairSummons() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        // remove existing summons and buff
        for (int summonSkillId : SCURVY_SUMMONS_SUMMONS) {
            tsm.removeStatsBySkill(summonSkillId);
        }
        for (int summonSkillId : ALL_ABOARD_SUMMONS) {
            tsm.removeStatsBySkill(summonSkillId);
        }
        tsm.removeStatsBySkill(AHOY_MATEYS);
        tsm.sendResetStatPacket();
        // create summons
        if (chr.hasSkill(ALL_ABOARD)) {
            int summonId1 = Util.getRandomFromCollection(ALL_ABOARD_SUMMONS);
            int summonId2 = Util.getRandomFromCollection(ALL_ABOARD_SUMMONS.stream().filter(id -> id != summonId1).toList());
            createSummon(summonId1);
            createSummon(summonId2);
        } else {
            createSummon(Util.getRandomFromCollection(SCURVY_SUMMONS_SUMMONS));
        }
    }

    private void createSummon(int summonId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Summon summon = Summon.getSummonBy(chr, summonId, (byte) 1);
        summon.setFlyMob(false);
        summon.setMoveAbility(MoveAbility.WalkRandom);
        chr.getField().spawnSummon(summon);
        // apply buff
        int duration = chr.getSkillStatValue(time, ALL_ABOARD);
        if (chr.hasSkill(ALL_ABOARD) && ALL_ABOARD_SUMMONS.contains(summonId)) {
            if (tsm.getOptByCTSAndSkill(IndieEmpty, summonId) != null) {
                // use xOption to flag whether it has blocked a debuff, not sent to client
                tsm.getOptByCTSAndSkill(IndieEmpty, summonId).xOption = 1;
            }
        }
        if (chr.hasSkill(AHOY_MATEYS)) {
            SkillInfo si = SkillData.getSkillInfoById(AHOY_MATEYS);
            int slv = chr.getSkillLevel(AHOY_MATEYS);
            Option o1 = new Option();
            Option o2 = new Option();
            switch (summonId) {
                case 5210015: // Murat
                    o1.nOption = si.getValue(z, slv);
                    o1.rOption = AHOY_MATEYS;
                    o1.tOption = duration;
                    tsm.putCharacterStatValue(IncCriticalDamMin, o1);
                    tsm.putCharacterStatValue(IncCriticalDamMax, o1);
                    break;
                case 5210016: // Valerie
                    o1.nOption = si.getValue(s, slv);
                    o1.rOption = AHOY_MATEYS;
                    o1.tOption = duration;
                    tsm.putCharacterStatValue(CriticalBuff, o1);
                    break;
                case 5210017: // Jack
                    o1.nValue = si.getValue(x, slv);
                    o1.nReason = AHOY_MATEYS;
                    o1.tStart = Util.getCurrentTime();
                    o1.tTerm = duration;
                    tsm.putCharacterStatValue(IndieMHPR, o1);
                    tsm.putCharacterStatValue(IndieMMPR, o1);
                    o2.nValue = si.getValue(u, slv);
                    o2.nReason = AHOY_MATEYS;
                    o2.tStart = Util.getCurrentTime();
                    o2.tTerm = duration;
                    tsm.putCharacterStatValue(IndieSpeed, o2);
                    break;
                case 5210018: // Cutter
                    o1.nOption = si.getValue(y, slv);
                    o1.rOption = AHOY_MATEYS;
                    o1.tOption = duration;
                    tsm.putCharacterStatValue(DamageReduce, o1);
                    break;
            }
        }
        tsm.sendSetStatPacket();
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleMobDebuffSkill(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        for (int summonId : ALL_ABOARD_SUMMONS) {
            Option summonOpt = tsm.getOptByCTSAndSkill(IndieEmpty, summonId);
            if (summonOpt != null && summonOpt.xOption > 0) {
                summonOpt.xOption = 0;
                tsm.removeAllDebuffs();
                return;
            }
        }
        super.handleMobDebuffSkill(chr);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}
