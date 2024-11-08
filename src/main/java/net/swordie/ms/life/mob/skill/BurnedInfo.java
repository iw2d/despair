package net.swordie.ms.life.mob.skill;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.util.Util;

/**
 * Created on 1/4/2018.
 */
public class BurnedInfo {
    private int characterId, skillId, interval, end, dotAnimation, dotCount, superPos, attackDelay, dotTickIdx, dotTickDamR;
    private int startTime;
    private int lastUpdate;
    private Char chr;
    private int damage;

    public BurnedInfo deepCopy() {
        BurnedInfo copy = new BurnedInfo();
        copy.setCharacterId(getCharacterId());
        copy.setChr(getChr());
        copy.setSkillId(getSkillId());
        copy.setDamage(getDamage());
        copy.setInterval(getInterval());
        copy.setEnd(getEnd());
        copy.setDotAnimation(getDotAnimation());
        copy.setDotCount(getDotCount());
        copy.setSuperPos(getSuperPos());
        copy.setAttackDelay(getAttackDelay());
        copy.setDotTickIdx(getDotTickIdx());
        copy.setDotTickDamR(getDotTickDamR());
        copy.setLastUpdate(getLastUpdate());
        copy.setStartTime(getStartTime());
        return copy;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getDotAnimation() {
        return dotAnimation;
    }

    public void setDotAnimation(int dotAnimation) {
        this.dotAnimation = dotAnimation;
    }

    public int getDotCount() {
        return dotCount;
    }

    public void setDotCount(int dotCount) {
        this.dotCount = dotCount;
    }

    public int getSuperPos() {
        return superPos;
    }

    public void setSuperPos(int superPos) {
        this.superPos = superPos;
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    public int getDotTickIdx() {
        return dotTickIdx;
    }

    public void setDotTickIdx(int dotTickIdx) {
        this.dotTickIdx = dotTickIdx;
    }

    public int getDotTickDamR() {
        return dotTickDamR;
    }

    public void setDotTickDamR(int dotTickDamR) {
        this.dotTickDamR = dotTickDamR;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getCharacterId());
        outPacket.encodeInt(getSkillId());
        outPacket.encodeLong(getDamage());
        outPacket.encodeInt(getInterval());
        outPacket.encodeInt(getEnd());
        outPacket.encodeInt(getDotAnimation());
        outPacket.encodeInt(getDotCount());
        outPacket.encodeInt(getSuperPos());
        outPacket.encodeInt(getAttackDelay());
        outPacket.encodeInt(getDotTickIdx());
        outPacket.encodeInt(getDotTickDamR());
        outPacket.encodeInt(0); // BURNED_INFO[14]
        outPacket.encodeInt(0); // BURNED_INFO[15]
        outPacket.encodeInt(0); // BURNED_INFO[19]
        outPacket.encodeInt(0); // BURNED_INFO[20]
    }

    public Char getChr() {
        return chr;
    }

    public void setChr(Char chr) {
        this.chr = chr;
    }
}

