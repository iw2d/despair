package net.swordie.ms.client.jobs.cygnus;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.life.Summon;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.SkillStat.indieMaxDamageOverR;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Noblesse extends Job {
    public static final int IMPERIAL_RECALL = 10001245;
    public static final int ELEMENTAL_SLASH = 10001244;
    public static final int ELEMENTAL_SHIFT_BASE = 10000252;
    public static final int ELEMENTAL_SHIFT_HIGH = 10001253;
    public static final int ELEMENTAL_SHIFT_FLASH = 10001254;
    public static final int ELEMENTAL_EXPERT = 10000250; // given with 4th job
    public static final int NOBLE_MIND = 10000202;

    private int[] addedSkills = {
            ELEMENTAL_SLASH,
            ELEMENTAL_SHIFT_BASE,
            ELEMENTAL_SHIFT_HIGH,
            ELEMENTAL_SHIFT_FLASH
    };

    public Noblesse(Char chr) {
        super(chr);
        if (chr.getId() != 0 && JobConstants.isCygnusKnight(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    if (skill != null) {
                        skill.setCurrentLevel(1);
                        chr.addSkill(skill);
                    }
                }
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return id == JobConstants.JobEnum.NOBLESSE.getJobId();
    }

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        super.handleAttack(chr, attackInfo);
    }

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        switch (skillID) {
            case IMPERIAL_RECALL:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case DawnWarrior.CALL_OF_CYGNUS_DW:
            case BlazeWizard.CALL_OF_CYGNUS_BW:
            case WindArcher.CALL_OF_CYGNUS_WA:
            case NightWalker.CALL_OF_CYGNUS_NW:
            case ThunderBreaker.CALL_OF_CYGNUS_TB:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1); //Indie
                break;
            case DawnWarrior.GLORY_OF_THE_GUARDIANS_DW:
            case BlazeWizard.GLORY_OF_THE_GUARDIANS_BW:
            case WindArcher.GLORY_OF_THE_GUARDIANS_WA:
            case NightWalker.GLORY_OF_THE_GUARDIANS_NW:
            case ThunderBreaker.GLORY_OF_THE_GUARDIANS_TB:
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

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {

        super.handleHit(chr, hitInfo);
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
    }

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        chr.getAvatarData().getCharacterStat().setPosMap(130030000);
    }
}
