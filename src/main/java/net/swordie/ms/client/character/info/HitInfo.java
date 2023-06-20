package net.swordie.ms.client.character.info;

import net.swordie.ms.util.Position;

/**
 * Created on 1/11/2018.
 */
public class HitInfo {
    public int damagedTime;
    public byte type = - 1;
    public byte elemAttr;
    public int hpDamage;
    public boolean isCrit;
    public int templateID;
    public int mobID;
    public boolean isLeft;
    public int blockSkillID;
    public int blockSkillDamage;
    public byte reflect;
    public byte guard;
    public byte powerGuard;
    public int reflectMobID;
    public byte hitAction;
    public Position hitPos;
    public Position userHitPos;
    public byte stance;
    public int stanceSkillID;
    public int cancelSkillID;
    public int reductionSkillID;
    public int mobSkillID;
    public int userID;

    public int mpDamage;
    public byte specialEffectSkill; // mask: 0x1 if true, 0x2 if custom skillID. Default is 33110000 (jaguar boost)
    public int userSkillID; // accessed on miss, for skills like shadow shifter

    @Override
    public String toString() {
        return "HitInfo {, " +
                "damagedTime = " + this.damagedTime + ", " +
                "type = " + this.type + ", " +
                "elemAttr = " + this.elemAttr + ", " +
                "hpDamage = " + this.hpDamage + ", " +
                "isCrit = " + this.isCrit + ", " +
                "templateID = " + this.templateID + ", " +
                "mobID = " + this.mobID + ", " +
                "isLeft = " + this.isLeft + ", " +
                "blockSkillId = " + this.blockSkillID + ", " +
                "blockSkillDamage = " + this.blockSkillDamage + ", " +
                "reflect = " + this.reflect + ", " +
                "guard = " + this.guard + ", " +
                "powerGuard = " + this.powerGuard + ", " +
                "reflectMobID = " + this.reflectMobID + ", " +
                "hitAction = " + this.hitAction + ", " +
                "hitPos = " + this.hitPos + ", " +
                "userHitPos = " + this.userHitPos + ", " +
                "stance = " + this.stance + ", " +
                "stanceSkillID = " + this.stanceSkillID + ", " +
                "cancelSkillID = " + this.cancelSkillID + ", " +
                "reductionSkillID = " + this.reductionSkillID + ", " +
                "mpDamage = " + this.mpDamage + ", " +
        "}";
    }
}
