package net.swordie.ms.client.character.skills;

import net.swordie.ms.client.character.skills.PsychicLockBall;
import net.swordie.ms.connection.OutPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 1/13/2018.
 */
public class PsychicLock {
    public int key;
    public int skillID;
    public short slv;
    public int action;
    public int actionSpeed;
    public int time;
    public boolean success = true;
    public List<PsychicLockBall> psychicLockBalls = new ArrayList<>();

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(skillID);
        outPacket.encodeShort(slv);
        outPacket.encodeInt(action);
        outPacket.encodeInt(actionSpeed);
        outPacket.encodeByte(psychicLockBalls.size());
        for(PsychicLockBall plb : psychicLockBalls) {
            plb.encode(outPacket);
        }
        outPacket.encodeByte(0);
    }
}
