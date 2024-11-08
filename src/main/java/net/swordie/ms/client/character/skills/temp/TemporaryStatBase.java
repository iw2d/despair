package net.swordie.ms.client.character.skills.temp;

import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.util.Util;

/**
 * Created on 2/3/2018.
 */
public class TemporaryStatBase {
    private Option option;
    private FileTime lastUpdated;
    protected int expireTerm;
    private boolean dynamicTermSet;

    public TemporaryStatBase(boolean dynamicTermSet) {
        this.option = new Option();
        this.option.nOption = 0;
        this.option.rOption = 0;
        this.lastUpdated = FileTime.currentTime();
        this.dynamicTermSet = dynamicTermSet;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public FileTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(FileTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getExpireTerm() {
        if (isDynamicTermSet()) {
            return 1000 * expireTerm;
        }
        return Integer.MAX_VALUE;
    }

    public void setExpireTerm(int expireTerm) {
        this.expireTerm = expireTerm;
    }

    public boolean isDynamicTermSet() {
        return dynamicTermSet;
    }

    public void setDynamicTermSet(boolean dynamicTermSet) {
        this.dynamicTermSet = dynamicTermSet;
    }

    public int getMaxValue() {
        return 10000;
    }

    public boolean isActive() {
        return getOption().nOption >= 10000;
    }

    public boolean hasExpired() {
        return hasExpired(Util.getCurrentTime());
    }

    public boolean hasExpired(long time) {
        boolean result = false;
        if(isDynamicTermSet()) {
            result = getExpireTerm() > time - getLastUpdated().getLongValue();
        }
        return result;
    }

    public void reset() {
        setOption(new Option());
        setLastUpdated(Util.getCurrentTimeLong());
    }

    private void setLastUpdated(long epochMillis) {
        FileTime.fromEpochMillis(epochMillis);
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getOption().nOption);
        outPacket.encodeInt(getOption().rOption);
        outPacket.encodeByte(isDynamicTermSet());
        outPacket.encodeInt(getExpireTerm());
        if (isDynamicTermSet()) {
            outPacket.encodeShort(getExpireTerm() / 1000);
        }
    }
}
