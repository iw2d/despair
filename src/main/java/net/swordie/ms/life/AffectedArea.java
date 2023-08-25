package net.swordie.ms.life;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Zero;
import net.swordie.ms.client.jobs.BeastTamer;
import net.swordie.ms.client.jobs.adventurer.archer.Bowmaster;
import net.swordie.ms.client.jobs.adventurer.magician.FirePoison;
import net.swordie.ms.client.jobs.adventurer.thief.NightLord;
import net.swordie.ms.client.jobs.adventurer.thief.Shadower;
import net.swordie.ms.client.jobs.cygnus.BlazeWizard;
import net.swordie.ms.client.jobs.legend.Aran;
import net.swordie.ms.client.jobs.legend.Shade;
import net.swordie.ms.client.jobs.resistance.BattleMage;
import net.swordie.ms.client.jobs.resistance.Xenon;
import net.swordie.ms.client.jobs.sengoku.Kanna;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSword;
import net.swordie.ms.life.mob.boss.demian.sword.DemianFlyingSwordPath;
import net.swordie.ms.life.mob.skill.MobSkillStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.loaders.containerclasses.MobSkillInfo;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.List;
import java.util.Random;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.client.jobs.resistance.Mechanic.*;

/**
 * Created on 1/6/2018.
 */
public class AffectedArea extends Life {

    private Char owner;
    private Rect rect;
    private int skillID;
    private int force;
    private int option;
    private int elemAttr;
    private int idk;
    private byte slv;
    private byte mobOrigin;
    private short delay;
    private boolean flip;
    private int duration;
    private boolean removeSkill;
    private int mobOwnerOID;

    public AffectedArea(int templateId) {
        super(templateId);
    }

    public static AffectedArea getMobAA(Mob mob, short skill, short slv, MobSkillInfo msi) {
        AffectedArea aa = new AffectedArea(0);

        aa.setMobOrigin((byte) 1);
        aa.setMobOwnerOID(mob.getObjectId());
        aa.setSkillID(skill);
        aa.setSlv((byte) slv);
        aa.setDuration(msi.getSkillStatIntValue(MobSkillStat.time) * 1000);
        aa.setRect(mob.getPosition().getRectAround(new Rect(msi.getLt(), msi.getRb())));

        return aa;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public int getCharID() {
        return owner.getId();
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public Char getOwner() {
        return owner;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getElemAttr() {
        return elemAttr;
    }

    public void setElemAttr(int elemAttr) {
        this.elemAttr = elemAttr;
    }

    public int getIdk() {
        return idk;
    }

    public void setIdk(int idk) {
        this.idk = idk;
    }

    public byte getSlv() {
        return slv;
    }

    public void setSlv(byte slv) {
        this.slv = slv;
    }

    public byte getMobOrigin() {
        return mobOrigin;
    }

    public void setMobOrigin(byte mobOrigin) {
        this.mobOrigin = mobOrigin;
    }

    public short getDelay() {
        return delay;
    }

    public void setDelay(short delay) {
        this.delay = delay;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public boolean isRemoveSkill() {
        return removeSkill;
    }

    public void setRemoveSkill(boolean removeSkill) {
        this.removeSkill = removeSkill;
    }

    public int getMobOwnerOID() {
        return mobOwnerOID;
    }

    public void setMobOwnerOID(int mobOwnerOID) {
        this.mobOwnerOID = mobOwnerOID;
    }

    public static AffectedArea getAffectedArea(Char chr, AttackInfo attackInfo) {
        AffectedArea aa = new AffectedArea(-1);
        aa.setSkillID(attackInfo.skillId);
        aa.setSlv(attackInfo.slv);
        aa.setElemAttr(attackInfo.elemAttr);
        aa.setForce(attackInfo.force);
        aa.setOption(attackInfo.option);
        aa.setOwner(chr);
        return aa;
    }

    public static AffectedArea getPassiveAA(Char chr, int skillID, int slv) {
        AffectedArea aa = new AffectedArea(-1);
        aa.setOwner(chr);
        aa.setSkillID(skillID);
        aa.setSlv((byte) slv);
        aa.setRemoveSkill(true);
        return aa;
    }

    public void handleMobInside(Mob mob) {
        if (getOwner() == null) {
            return;
        }
        Char chr = getField().getCharByID(getCharID());
        if (chr == null) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = getSkillID();
        int slv = getSlv();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        MobTemporaryStat mts = mob.getTemporaryStat();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (skillID) {
            case FirePoison.POISON_MIST:
                if (!mts.hasBurnFromSkillAndOwner(skillID, getCharID())) {
                    int dotDmg = si.getValue(dot, slv);
                    int dotTime = ((FirePoison) chr.getJobHandler()).getExtendedDoTTime(si.getValue(SkillStat.dotTime, slv));
                    if (chr.hasSkill(FirePoison.MIST_ERUPTION)) {
                        dotDmg = chr.getSkillStatValue(SkillStat.x, FirePoison.MIST_ERUPTION); // passive DoT dmg boost to Poison Mist
                    }
                    if (chr.hasSkill(FirePoison.POISON_MIST_CRIPPLE)) {
                        dotDmg += chr.getSkillStatValue(dot, FirePoison.POISON_MIST_CRIPPLE);
                    }
                    if (chr.hasSkill(FirePoison.POISON_MIST_AFTERMATH)) {
                        dotTime += chr.getSkillStatValue(SkillStat.dotTime, FirePoison.POISON_MIST_AFTERMATH);
                    }
                    mts.createAndAddBurnedInfo(chr, skillID, slv, dotDmg, si.getValue(dotInterval, slv), dotTime, 1);
                }
                break;
            case Bowmaster.FLAME_SURGE:
            case Kanna.NIMBUS_CURSE:
                if (!mts.hasBurnFromSkillAndOwner(skillID, getCharID())) {
                    mts.createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
            case NightLord.FRAILTY_CURSE:
                if (!mob.isBoss() || chr.hasSkill(NightLord.FRAILTY_CURSE_BOSS_RUSH)) {
                    o1.nOption = si.getValue(SkillStat.y, slv) - chr.getSkillStatValue(s, NightLord.FRAILTY_CURSE_SLOW); // already negative in SI
                    o1.rOption = skillID;
                    o1.tOption = 5;
                    mts.addStatOptions(MobStat.Speed, o1);
                    o2.nOption = -si.getValue(SkillStat.w, slv) - chr.getSkillStatValue(v, NightLord.FRAILTY_CURSE_ENHANCE);
                    o2.rOption = skillID;
                    o2.tOption = 5;
                    mts.addStatOptions(MobStat.PAD, o2);
                    mts.addStatOptions(MobStat.PDR, o2);
                    mts.addStatOptions(MobStat.MAD, o2);
                    mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                }
                break;
            case Shade.SPIRIT_TRAP:
                if (!mts.hasStat(MobStat.Freeze)) {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                }
                break;
            case SUPPORT_UNIT_HEX: // Mechanic
            case ENHANCED_SUPPORT_UNIT:
                o1.nOption = si.getValue(SkillStat.y, slv); // SkillStat.w in description, but y = -w, and present in both skills
                o1.rOption = skillID;
                o1.tOption = 5;
                mts.addStatOptions(MobStat.PDR, o1);
                mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                break;
            case Zero.TIME_DISTORTION:
                mts.removeBuffs();
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 5;
                mts.addStatOptions(MobStat.Freeze, o1);
                o2.nOption = si.getValue(SkillStat.x, slv);
                o2.rOption = skillID;
                o2.tOption = 5;
                mts.addStatOptionsAndBroadcast(MobStat.AddDamSkill2, o2);
                break;
        }
    }

    public void handleCharInside(Char chr) {
        if (getOwner() == null) {
            return;
        }
        if (getOwner() != chr && (getOwner().getParty() == null || !getOwner().getParty().isPartyMember(chr))) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasAffectedArea(this)) {
            return;
        }
        tsm.addAffectedArea(this);
        int skillID = getSkillID();
        int slv = getSlv();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (skillID) {
            case Shadower.SMOKE_SCREEN:
                o1.nValue = si.getValue(SkillStat.x, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieCrDmg, o1);
                break;
            case BlazeWizard.BURNING_CONDUIT:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieAsrR, o1); // Indie
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieBooster, slv);
                o2.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieBooster, o2); // Indie
                break;
            case Aran.MAHAS_DOMAIN:
                if (chr.getHP() > 0) {
                    chr.heal((int) (chr.getMaxHP() / (100D / si.getValue(w, slv))));
                    chr.healMP((int) (chr.getMaxHP() / (100D / si.getValue(w, slv))));
                    tsm.removeAllDebuffs();
                }
                break;
            case BattleMage.PARTY_SHIELD:
                o1.nOption = si.getValue(SkillStat.y, slv);
                o1.rOption = skillID;
                tsm.putCharacterStatValue(PartyBarrier, o1);
                break;
            case ENHANCED_SUPPORT_UNIT: // Mechanic
                o1.nValue = si.getValue(z, slv) + getOwner().getSkillStatValue(SkillStat.x, SUPPORT_UNIT_HEX_PARTY);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieAsrR, o1);
                break;
            case Xenon.TEMPORAL_POD:
                o1.nOption = 2;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(OnCapsule, o1);
                Xenon.temporalPodTimer(chr);
                break;
            case Kanna.BELLFLOWER_BARRIER:
                o1.nReason = skillID;
                o1.nValue = si.getValue(bdR, slv);
                o1.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieBDR, o1); // Indie
                break;
            case Kanna.BLOSSOM_BARRIER:
                o1.nOption = si.getValue(SkillStat.x, slv);
                o1.rOption = skillID;
                tsm.putCharacterStatValue(PartyBarrier, o1);
                o2.nOption = si.getValue(SkillStat.y, slv);
                o2.rOption = skillID;
                tsm.putCharacterStatValue(AsrR, o2);
                tsm.putCharacterStatValue(TerR, o2);
                break;
            case Zero.TIME_DISTORTION:
                tsm.removeAllDebuffs();
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieBooster, slv);
                o2.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieBooster, o2); // Indie
                break;
            case BeastTamer.PURR_ZONE:
                if (chr.getHP() > 0) {
                    chr.heal(si.getValue(hp, slv), true);
                }
                break;
        }
        tsm.sendSetStatPacket();
    }

    public void handleAARemoved() {
        Field field = getField();
        if (getMobOrigin() > 0) {
            // Mob Affected Areas

            // Demian Flying Sword Affected Area.
            if (getSkillID() == 131 && getSlv() == 28) {
                DemianFlyingSword sword = (DemianFlyingSword) field.getLifeByObjectID(getIdk());
                if (sword != null) {
                    List<Position> path;
                    if (new Random().nextBoolean()) {
                        path = DemianFlyingSwordPath.flyingSwordPathBouncing1;
                    } else {
                        path = DemianFlyingSwordPath.flyingSwordPathBouncing2;
                    }
                    sword.setDemianFlyingSwordPath(DemianFlyingSwordPath.flyingSwordBouncingPath(path));
                    sword.startPath();
                    sword.target();
                }
            }
        } else  {
            // Char Affected Areas
            for (Char chr : field.getChars()) {
                TemporaryStatManager tsm = chr.getTemporaryStatManager();
                chr.getTemporaryStatManager().removeAffectedArea(this);
            }
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void broadcastSpawnPacket(Char onlyChar) {
        OutPacket outPacket = FieldPacket.affectedAreaCreated(this);
        Field field = getField();
        if (onlyChar == null) {
            field.broadcastPacket(outPacket);
            for (Char chr : field.getCharsInRect(getRect())) {
                handleCharInside(chr);
            }
        } else {
            onlyChar.write(outPacket);
            field.checkCharInAffectedAreas(onlyChar);
        }
    }

    @Override
    public void broadcastLeavePacket() {
        handleAARemoved();
        getField().broadcastPacket(FieldPacket.affectedAreaRemoved(this, false));
    }
}
