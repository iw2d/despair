package net.swordie.ms.client.jobs.adventurer;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.loaders.SkillData;


/**
 * Created on 12/14/2017.
 */
public class Beginner extends Job {
    public static final int RECOVERY = 1001;
    public static final int NIMBLE_FEET = 1002;
    public static final int THREE_SNAILS = 1000;
    public static final int MAPLE_RETURN = 1281;

    public Beginner(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.JobEnum.getJobById(id) == JobConstants.JobEnum.BEGINNER;
    }

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        super.handleAttack(chr, attackInfo);
    }

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
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
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        if (chr.getSubJob() == 1) {
            cs.setPosMap(103050900);
        } else if (chr.getSubJob() == 2) {
            cs.setPosMap(3000600);
        } else {
            cs.setPosMap(4000011);
        }
    }

    @Override
    public void handleSetJob(short jobId) {
        if (JobConstants.JobEnum.getJobById(jobId) == JobConstants.JobEnum.BEGINNER) {
            chr.addSkill(THREE_SNAILS, 0, 3);
            chr.addSkill(RECOVERY, 0, 3);
            chr.addSkill(NIMBLE_FEET, 0, 3);
        }
        super.handleSetJob(jobId);
    }
}
