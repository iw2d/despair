package net.swordie.ms.client.character.skills;

import net.swordie.ms.client.character.skills.temp.TwoStateTemporaryStat;
import net.swordie.ms.connection.OutPacket;

/**
 * Created on 2/3/2018.
 */
public class PartyBooster extends TwoStateTemporaryStat {

    private int currentTime;

    public PartyBooster() {
        super(false);
        currentTime = 0;
        expireTerm = 0;
    }

    @Override
    public int getExpireTerm() {
        return expireTerm;
    }

    @Override
    public boolean hasExpired(long tCur) {
        return getExpireTerm() * 1000L < tCur - getCurrentTime();
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void reset() {
        super.reset();
        setCurrentTime(0);
    }

    @Override
    public void encode(OutPacket outPacket) {
        super.encode(outPacket);
        outPacket.encodeTime(getCurrentTime());
        outPacket.encodeShort(getExpireTerm());
    }
}
