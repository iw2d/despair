package net.swordie.ms.client.character.info;

import net.swordie.ms.util.Position;

/**
 * Created on 1/11/2018.
 */
public class HitInfo {
    public int hpDamage;
    public int templateID;
    public int mobID;
    public int mpDamage;
    public int type = -1;
    public int blockSkillId;
    public int otherUserID;
    public boolean isCrit;
    public int action;
    public int hitAction;
    public int specialEffectSkill; // mask: 0x1 if true, 0x2 if custom skillID. Default is 33110000 (jaguar boost)
    public int reflectDamage;
    public int userSkillID;
    public byte attackIdx;
    public short obstacle;
    public byte elemAttr;
    public int damagedTime;
    public boolean isLeft;
    public int reducedDamage;
    public byte reflect;
    public byte isGuard;
    public boolean bodyAttack;
    public byte stance;
    public int stanceSkillID;
    public int cancelSkillID;
    public int reductionSkillID;

    @Override
    public String toString() {
        return "HitInfo {" +
                "damagedTime = " + damagedTime +
                ", type = " + type +
                ", elemAttr = " + elemAttr +
                ", hpDamage = " + hpDamage +
                ", isCrit = " + isCrit +
                ", templateID = " + templateID +
                ", blockSkillId = " + blockSkillId +
                ", reducedDamage = " + reducedDamage +
                ", isGuard = " + isGuard +
                ", stance = " + stance +
                ", stanceSkillID = " + stanceSkillID +
                ", cancelSkillID = " + cancelSkillID +
                ", specialEffectSkill = " + specialEffectSkill +
                ", reductionSkillID = " + reductionSkillID +
                ", bodyAttack = " + bodyAttack +
                '}';
    }
}
