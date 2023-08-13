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
import net.swordie.ms.loaders.SkillData;


/**
 * Created on 12/14/2017.
 */
public class Noblesse extends Job {
    public static final int THREE_SNAILS = 10001000;
    public static final int RECOVERY = 10001001;
    public static final int NIMBLE_FEET = 10001002;
    public static final int IMPERIAL_RECALL = 10001245;
    public static final int ELEMENTAL_SLASH = 10001244;
    public static final int ELEMENTAL_SHIFT_BASE = 10000252;
    public static final int ELEMENTAL_SHIFT_HIGH = 10001253;
    public static final int ELEMENTAL_SHIFT_FLASH = 10001254;
    public static final int ELEMENTAL_EXPERT = 10000250; // given with 4th job
    public static final int NOBLE_MIND = 10000202;

    public Noblesse(Char chr) {
        super(chr);
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

    @Override
    public void handleSetJob(short jobId) {
        if (JobConstants.JobEnum.getJobById(jobId) == JobConstants.JobEnum.NOBLESSE) {
            chr.addSkill(THREE_SNAILS, 0, 3);
            chr.addSkill(RECOVERY, 0, 3);
            chr.addSkill(NIMBLE_FEET, 0, 3);
            chr.addSkill(ELEMENTAL_SLASH, 1, 1);
            chr.addSkill(ELEMENTAL_SHIFT_BASE, 1, 1);
            chr.addSkill(ELEMENTAL_SHIFT_HIGH, 1, 1);
            chr.addSkill(ELEMENTAL_SHIFT_FLASH, 1, 1);
        }
        super.handleSetJob(jobId);
    }
}
