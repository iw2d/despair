package net.swordie.ms.client.jobs.adventurer.pirate;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.Beginner;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Pirate extends Beginner {
    public static final int MAPLE_RETURN = 1281;
    public static final int DASH = 5001005; //Buff

    private int[] addedSkills = new int[] {
            MAPLE_RETURN,
    };

    public Pirate(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return id == JobConstants.JobEnum.PIRATE.getJobId();
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
        switch (attackInfo.skillId) {
        }
        super.handleAttack(chr, attackInfo);
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
        switch (skillID) {
            case MAPLE_RETURN:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case Buccaneer.HEROS_WILL_BUCC:
            case Corsair.HEROS_WILL_SAIR:
            case Cannoneer.HEROS_WILL_CANNON:
                tsm.removeAllDebuffs();
                break;
            case DASH:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Speed, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Jump, o2);
                break;
            case Buccaneer.KNUCKLE_BOOSTER:
            case Corsair.GUN_BOOSTER:
            case Cannoneer.CANNON_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case Buccaneer.MAPLE_WARRIOR_BUCC:
            case Corsair.MAPLE_WARRIOR_SAIR:
            case Cannoneer.MAPLE_WARRIOR_CANNON:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case Buccaneer.ROLL_OF_THE_DICE_BUCC:
            case Corsair.ROLL_OF_THE_DICE_SAIR:
            case Cannoneer.LUCK_OF_THE_DIE:
                int roll = Util.getRandom(1, 6);
                chr.write(UserPacket.effect(Effect.skillAffectedSelect(skillID, slv, roll, false)));
                chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffectedSelect(skillID, slv, roll, false)));
                if (roll == 1) {
                    return;
                }
                o1.nOption = roll;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.throwDice(roll);
                tsm.putCharacterStatValue(Dice, o1);
                break;
            case Buccaneer.ROLL_OF_THE_DICE_BUCC_DD:
            case Corsair.ROLL_OF_THE_DICE_SAIR_DD:
            case Cannoneer.LUCK_OF_THE_DIE_DD:
                List<Integer> choices = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
                if (chr.hasSkill(Buccaneer.ROLL_OF_THE_DICE_BUCC_ADDITION) || chr.hasSkill(Corsair.ROLL_OF_THE_DICE_SAIR_ADDITION)) {
                    choices.add(7);
                }
                if (chr.hasSkill(Buccaneer.ROLL_OF_THE_DICE_BUCC_ENHANCE) || chr.hasSkill(Corsair.ROLL_OF_THE_DICE_SAIR_ENHANCE)) {
                    // WZ has prop = 5, but I'll just to this instead
                    choices.add(4);
                    choices.add(5);
                    choices.add(6);
                }

                int roll1 = Util.getRandomFromCollection(choices);
                int roll2 = Util.getRandomFromCollection(choices);

                // Saving Grace Hyper handling, when DD does not activate, 40% chance for cooldown to not apply
                // Original description: "After this, you can activate at least 4 Double Downs"
                // I will interpret this as: "The next cast after this will activate Double Down with 4 or above"
                int savingGraceSkillId = 0;
                if (chr.hasSkill(Buccaneer.ROLL_OF_THE_DICE_BUCC_SAVING_GRACE)) {
                    savingGraceSkillId = Buccaneer.ROLL_OF_THE_DICE_BUCC_SAVING_GRACE;
                } else if (chr.hasSkill(Corsair.ROLL_OF_THE_DICE_SAIR_SAVING_GRACE)) {
                    savingGraceSkillId = Corsair.ROLL_OF_THE_DICE_SAIR_SAVING_GRACE;
                }
                if (savingGraceSkillId != 0 && tsm.hasStatBySkillId(savingGraceSkillId)) {
                    tsm.removeStatsBySkill(savingGraceSkillId);
                    roll1 = Util.getRandom(4, (chr.hasSkill(Buccaneer.ROLL_OF_THE_DICE_BUCC_ADDITION) || chr.hasSkill(Corsair.ROLL_OF_THE_DICE_SAIR_ADDITION)) ? 7 : 6);
                    roll2 = roll1;
                } else if (Util.succeedProp(si.getValue(prop, slv))) { // prop% chance to roll double down
                    roll2 = roll1;
                }

                if (roll1 != roll2 && savingGraceSkillId != 0 && Util.succeedProp(chr.getSkillStatValue(prop, savingGraceSkillId))) {
                    chr.resetSkillCoolTime(skillID);
                    o2.nOption = 1;
                    o2.rOption = savingGraceSkillId;
                    tsm.putCharacterStatValue(LUK, o2);
                }

                chr.write(UserPacket.effect(Effect.skillAffectedSelect(skillID, slv, roll1, false)));
                chr.write(UserPacket.effect(Effect.skillAffectedSelect(skillID, slv, roll2, true)));
                chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffectedSelect(skillID, slv, roll1, false)));
                chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffectedSelect(skillID, slv, roll2, true)));

                if (roll1 == 1 && roll2 == 1) {
                    return;
                }

                o1.nOption = (roll1 * 10) + roll2; // if rolled: 3 and 5, the DoubleDown nOption = 35
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);

                tsm.throwDice(roll1, roll2);
                tsm.putCharacterStatValue(Dice, o1);
                break;
            case Buccaneer.EPIC_ADVENTURER_BUCC:
            case Corsair.EPIC_ADVENTURER_SAIR:
            case Cannoneer.EPIC_ADVENTURER_CANNON:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMaxDamageOverR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMaxDamageOverR, o2);
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        if (chr.hasSkill(Buccaneer.PIRATE_REVENGE_BUCC) || chr.hasSkill(Corsair.PIRATE_REVENGE_SAIR)) {
            applyPirateRevenge();
        }
        super.handleHit(chr, hitInfo);
    }

    private void applyPirateRevenge() {
        int skillId = 0;
        if (chr.hasSkill(Buccaneer.PIRATE_REVENGE_BUCC)) {
            skillId = Buccaneer.PIRATE_REVENGE_BUCC;
        } else if (chr.hasSkill(Corsair.PIRATE_REVENGE_SAIR)) {
            skillId = Corsair.PIRATE_REVENGE_SAIR;
        }
        if (skillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        Option o1 = new Option();
        Option o2 = new Option();
        int prop = si.getValue(SkillStat.prop, slv);
        if (Util.succeedProp(prop)) {
            o1.nOption = si.getValue(y, slv);
            o1.rOption = skillId;
            o1.tOption = si.getValue(time, slv);
            tsm.putCharacterStatValue(DamageReduce, o1);
            o2.nReason = skillId;
            o2.nValue = si.getValue(indieDamR, slv);
            o2.tStart = Util.getCurrentTime();
            o2.tTerm = si.getValue(time, slv);
            tsm.putCharacterStatValue(IndieDamR, o2);
            tsm.sendSetStatPacket();
        }
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}
