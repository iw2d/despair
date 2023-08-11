package net.swordie.ms.client.jobs.resistance;

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

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.client.jobs.adventurer.thief.Thief.DARK_SIGHT;

/**
 * Created on 12/14/2017.
 */
public class Citizen extends Job {
    public static final int CRYSTAL_THROW = 30001000;
    public static final int INFILTRATE = 30001001;
    public static final int POTION_MASTERY = 30000002;
    public static final int SECRET_ASSEMBLY = 30001281;

    private int[] addedSkills = new int[] {
            CRYSTAL_THROW,
            INFILTRATE,
            POTION_MASTERY
    };

    public Citizen(Char chr) {
        super(chr);

        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setRootId(3000);
                    skill.setMasterLevel(3);
                    skill.setMaxLevel(3);
                    chr.addSkill(skill);
                }
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return id == JobConstants.JobEnum.CITIZEN.getJobId();
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
        Option o3 = new Option();
        switch (skillID) {
            case INFILTRATE:
                o1.nOption = si.getValue(speed, slv);
                o1.rOption = DARK_SIGHT;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Speed, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = DARK_SIGHT;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DarkSight, o2);
                chr.addSkillCooldown(skillID, 60000);
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        chr.getAvatarData().getCharacterStat().setPosMap(931000000);
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
    }
}
