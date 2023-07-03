package net.swordie.ms.client.character.skills.info;

import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;

public class MobAttackInfo {
    public int mobId;
    public byte hitAction;
    public byte left;
    public byte idk3;
    public byte foreAction;
    public byte frameIdx;
    public byte calcDamageStatIndex;
    public short hitX;
    public short hitY;
    public short oldPosX;
    public short oldPosY;
    public short hpPerc;
    public boolean[] crits = new boolean[15];
    public int[] damages;
    public int mobUpDownYRange;
    public byte type;
    public String currentAnimationName;
    public int animationDeltaL;
    public String[] hitPartRunTimes;
    public int templateID;
    public short idk6;
    public boolean isResWarriorLiftPress;
    public Position pos1;
    public Position pos2;
    public Rect rect;
    public int idkInt;
    public byte idkByte;
    public int psychicLockInfo;
    public byte rocketRushInfo;
    public byte forceActionAndLeft;
    public byte calcDamageStatIndexAndDoomed;
    public int hitPartRunTimesSize;
    public short magicInfo;
    public Mob mob; // only used for damage calc (prevents having to search for a mob twice)

    public MobAttackInfo deepCopy() {
        MobAttackInfo mai = new MobAttackInfo();
        mai.mobId = mobId;
        mai.hitAction = hitAction;
        mai.left = left;
        mai.idk3 = idk3;
        mai.forceActionAndLeft = forceActionAndLeft;
        mai.frameIdx = frameIdx;
        mai.calcDamageStatIndex = calcDamageStatIndex;
        mai.hitX = hitX;
        mai.hitY = hitY;
        mai.oldPosX = oldPosX;
        mai.oldPosY = oldPosY;
        mai.hpPerc = hpPerc;
        mai.crits = new boolean[crits.length];
        if (crits != null && crits.length > 0) {
            System.arraycopy(crits, 0, mai.crits, 0, crits.length);
        }
        mai.damages = new int[damages.length];
        if (damages != null && damages.length > 0) {
            System.arraycopy(damages, 0, mai.damages, 0, damages.length);
        }
        mai.mobUpDownYRange = mobUpDownYRange;
        mai.animationDeltaL = animationDeltaL;
        if (hitPartRunTimes != null && hitPartRunTimes.length > 0) {
            System.arraycopy(hitPartRunTimes, 0, mai.hitPartRunTimes, 0, hitPartRunTimes.length);
        }
        mai.templateID = templateID;
        mai.idk6 = idk6;
        mai.isResWarriorLiftPress = isResWarriorLiftPress;
        mai.pos1 = pos1 != null ? pos1.deepCopy() : null;
        mai.pos2 = pos2 != null ? pos2.deepCopy() : null;
        mai.rect = rect != null ? rect.deepCopy() : null;
        mai.idkInt = idkInt;
        mai.idkByte = idkByte;
        mai.psychicLockInfo = psychicLockInfo;
        mai.rocketRushInfo = rocketRushInfo;
        return mai;
    }
}
