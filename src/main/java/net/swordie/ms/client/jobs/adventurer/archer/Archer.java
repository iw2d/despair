package net.swordie.ms.client.jobs.adventurer.archer;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.Beginner;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.life.Summon;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Archer extends Beginner {
    public static final int CRITICAL_SHOT = 3000001;

    private int[] addedSkills = new int[] {
            MAPLE_RETURN,
    };

    public Archer(Char chr) {
        super(chr);
        if(chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
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
        return id == JobConstants.JobEnum.BOWMAN.getJobId();
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

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        super.handleRemoveCTS(cts);
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
            case Bowmaster.SOUL_ARROW_BOW:
            case Marksman.SOUL_ARROW_XBOW:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SoulArrow, o1);
                o2.nOption = si.getValue(epad, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EPAD, o2);
                o3.nOption = si.getValue(x, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NoBulletConsume, o3);
                break;
            case Bowmaster.BOW_BOOSTER:
            case Marksman.XBOW_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case Bowmaster.PHOENIX:
            case Marksman.FREEZER:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(true);
                summon.setMoveAbility(MoveAbility.Fly);
                field.spawnSummon(summon);
                break;
            case Bowmaster.SHARP_EYES_BOW:
            case Marksman.SHARP_EYES_XBOW:
                int cr = si.getValue(x, slv) +
                        this.chr.getSkillStatValue(x, Bowmaster.SHARP_EYES_BOW_CR_H) +
                        this.chr.getSkillStatValue(x, Marksman.SHARP_EYES_XBOW_CR_H);
                o1.nOption = (cr << 8) + si.getValue(y, slv); // (cr << 8) + crDmg;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) +
                        this.chr.getSkillStatValue(time, Bowmaster.SHARP_EYES_BOW_PERSIST) +
                        this.chr.getSkillStatValue(time, Marksman.SHARP_EYES_XBOW_PERSIST);
                o1.mOption =
                        this.chr.getSkillStatValue(ignoreMobpdpR, Bowmaster.SHARP_EYES_BOW_IED_H) +
                        this.chr.getSkillStatValue(ignoreMobpdpR, Marksman.SHARP_EYES_XBOW_IED_H);
                tsm.putCharacterStatValue(SharpEyes, o1);
                break;
            case Bowmaster.ILLUSION_STEP_BOW:
            case Marksman.ILLUSION_STEP_XBOW:
                o1.nOption = si.getValue(dex, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DEX, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IllusionStep, o2);
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        if (hitInfo.hpDamage == 0 && hitInfo.mpDamage == 0) {
            // Dodged
            int skillId = 0;
            if (chr.hasSkill(Bowmaster.EVASION_BOOST)) {
                skillId = Bowmaster.EVASION_BOOST;
            } else if (chr.hasSkill(Marksman.EVASION_BOOST_XBOW)) {
                skillId = Marksman.EVASION_BOOST_XBOW;
            }
            if (skillId > 0) {
                TemporaryStatManager tsm = chr.getTemporaryStatManager();
                SkillInfo si = SkillData.getSkillInfoById(skillId);
                int slv = chr.getSkillLevel(skillId);
                Option o = new Option();
                o.nOption = 100;
                o.rOption = skillId;
                o.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o);
                tsm.sendSetStatPacket();
            }
        }
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}
