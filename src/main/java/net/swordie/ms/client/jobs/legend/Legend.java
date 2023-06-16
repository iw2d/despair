package net.swordie.ms.client.jobs.legend;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;

/**
 * Created on 12/14/2017.
 */
public class Legend extends Job {
    public Legend(Char chr) {
        super(chr);
    }

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {

        super.handleAttack(chr, attackInfo);
    }

    @Override
    public void handleSkill(Char c, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);

    }

    @Override
    public void handleHit(Char chr, InPacket inPacket, HitInfo hitInfo) {

        super.handleHit(chr, inPacket, hitInfo);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return id == JobConstants.JobEnum.LEGEND.getJobId();
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
    }
}
