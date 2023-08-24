package net.swordie.ms.client.jobs.cygnus;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class DawnWarrior extends Noblesse {
    public static final int SOUL_ELEMENT = 11001022; //Buff  (Immobility Debuff)
    public static final int SWORD_MASTERY = 11100025;
    public static final int SOUL_SPEED = 11101024; //Buff
    public static final int FALLING_MOON = 11101022; //Buff (Unlimited Duration)

    public static final int RISING_SUN = 11111022; //Buff (Unlimited Duration)
    public static final int TRUE_SIGHT = 11111023; //Buff (Mob Def Debuff & Final DmgUp Debuff)
    public static final int WILL_OF_STEEL = 11110025;

    public static final int STUDENT_OF_THE_BLADE = 11120007;
    public static final int EQUINOX_CYCLE = 11121005; //Buff
    public static final int EQUINOX_CYCLE_MOON = 11121011;
    public static final int EQUINOX_CYCLE_SUN = 11121012;
    public static final int IMPALING_RAYS = 11121004; //Special Attack (Incapacitate Debuff)
    public static final int IMPALING_RAYS_EXPLOSION = 11121013;
    public static final int CALL_OF_CYGNUS_DW = 11121000; //Buff
    public static final int MASTER_OF_THE_SWORD = 11120009;

    public static final int TRUE_SIGHT_PERSIST = 11120043;
    public static final int TRUE_SIGHT_ENHANCE = 11120044;
    public static final int TRUE_SIGHT_GUARDBREAK = 11120045;
    public static final int SOUL_FORGE = 11121054;
    public static final int STYX_CROSSING = 11121052;
    public static final int STYX_CROSSING_CHARGED = 11121055;
    public static final int GLORY_OF_THE_GUARDIANS_DW = 11121053;

    private ScheduledFuture willOfSteelTimer;

    public DawnWarrior(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            willOfSteelTimer = EventManager.addEvent(this::handleWillOfSteel, getWillOfSteelDelay(), TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isDawnWarrior(id);
    }

    private void handleWillOfSteel() {
        if (chr.hasSkill(WILL_OF_STEEL) && chr.getHP() > 0) {
            int healRate = chr.getSkillStatValue(y, WILL_OF_STEEL);
            chr.heal((int) (chr.getMaxHP() / (100D / healRate)));
        }
        if (willOfSteelTimer != null && !willOfSteelTimer.isDone()) {
            willOfSteelTimer.cancel(true);
        }
        willOfSteelTimer = EventManager.addEvent(this::handleWillOfSteel, getWillOfSteelDelay(), TimeUnit.SECONDS);
    }

    private int getWillOfSteelDelay() {
        if (!chr.hasSkill(WILL_OF_STEEL)) {
            return 4;
        }
        return chr.getSkillStatValue(w, WILL_OF_STEEL);
    }



    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        if (hasHitMobs) {
            applySoulElementDebuff(attackInfo);
        }
        if (!ServerConstants.CLIENT_SIDED_SKILL_HOOK) {
            handleEquinoxCycle();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case IMPALING_RAYS:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.wOption = chr.getId();
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptions(MobStat.Freeze, o1);
                        mts.addStatOptionsAndBroadcast(MobStat.SoulExplosion, o2);
                    }
                }
                break;
            case STYX_CROSSING_CHARGED:
                chr.setSkillCooldown(STYX_CROSSING, chr.getSkillLevel(STYX_CROSSING));
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void applySoulElementDebuff(AttackInfo attackInfo) {
        if (!chr.hasSkill(SOUL_ELEMENT)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(SoulMasterFinal)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(SOUL_ELEMENT);
        int slv = chr.getSkillLevel(SOUL_ELEMENT);
        Option o1 = new Option();
        o1.nOption = 1;
        o1.rOption = SOUL_ELEMENT;
        o1.tOption = si.getValue(subTime, slv);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null || mob.isBoss()) {
                continue;
            }
            if (Util.succeedProp(si.getValue(prop, slv))) {
                mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
            }
        }
    }

    private void handleEquinoxCycle() {
        if (!chr.hasSkill(RISING_SUN) || !chr.hasSkill(FALLING_MOON)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(GlimmeringTime)) {
            return;
        }
        SkillInfo siRS = SkillData.getSkillInfoById(RISING_SUN);
        SkillInfo siFM = SkillData.getSkillInfoById(FALLING_MOON);
        int slvRS = chr.getSkillLevel(RISING_SUN);
        int slvFM = chr.getSkillLevel(FALLING_MOON);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        if (tsm.getOption(PoseType).nOption == 1) {
            o1.nOption = 2;
            o1.rOption = RISING_SUN;
            o1.bOption = 1;
            tsm.putCharacterStatValue(PoseType, o1);
            o2.nValue = siRS.getValue(indieDamR, slvRS) + chr.getSkillStatValue(v, MASTER_OF_THE_SWORD);
            o2.nReason = RISING_SUN;
            o2.tStart = Util.getCurrentTime();
            tsm.putCharacterStatValue(IndieDamR, o2);
            o3.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? chr.getSkillStatValue(w, MASTER_OF_THE_SWORD) : siRS.getValue(indieBooster, slvRS);
            o3.nReason = RISING_SUN;
            o3.tStart = Util.getCurrentTime();
            tsm.putCharacterStatValue(IndieBooster, o3);
            // Invisible FM buff
            o4.nValue = siFM.getValue(indieCr, slvFM) + chr.getSkillStatValue(indieCr, MASTER_OF_THE_SWORD);
            o4.nReason = EQUINOX_CYCLE_MOON;
            o4.tStart = Util.getCurrentTime();
            tsm.putCharacterStatValue(IndieCr, o4);
            o5.nOption = 1;
            o5.rOption = EQUINOX_CYCLE_MOON;
            tsm.putCharacterStatValue(BuckShot, o5);

            tsm.removeStatsBySkill(FALLING_MOON);
            tsm.removeStatsBySkill(EQUINOX_CYCLE_SUN);
            tsm.sendResetStatPacket();
        } else {
            o1.nOption = 1;
            o1.rOption = FALLING_MOON;
            o1.bOption = 1;
            tsm.putCharacterStatValue(PoseType, o1);
            o2.nValue = siFM.getValue(indieCr, slvFM) + chr.getSkillStatValue(indieCr, MASTER_OF_THE_SWORD);
            o2.nReason = FALLING_MOON;
            o2.tStart = Util.getCurrentTime();
            tsm.putCharacterStatValue(IndieCr, o2);
            o3.nOption = 1;
            o3.rOption = FALLING_MOON;
            tsm.putCharacterStatValue(BuckShot, o3);
            // Invisible RS buff
            o4.nValue = siRS.getValue(indieDamR, slvRS) + chr.getSkillStatValue(v, MASTER_OF_THE_SWORD);
            o4.nReason = EQUINOX_CYCLE_SUN;
            o4.tStart = Util.getCurrentTime();
            tsm.putCharacterStatValue(IndieDamR, o4);
            o5.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? chr.getSkillStatValue(w, MASTER_OF_THE_SWORD) : siRS.getValue(indieBooster, slvRS);
            o5.nReason = EQUINOX_CYCLE_SUN;
            o5.tStart = Util.getCurrentTime();
            tsm.putCharacterStatValue(IndieBooster, o5);

            tsm.removeStatsBySkill(RISING_SUN);
            tsm.removeStatsBySkill(EQUINOX_CYCLE_MOON);
            tsm.sendResetStatPacket();
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
    }



    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        Option o6 = new Option();
        Summon summon;
        Field field;
        switch(skillID) {
            case TRUE_SIGHT:
                // get_element_dec
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) + chr.getSkillStatValue(time, TRUE_SIGHT_PERSIST);
                o1.cOption = chr.getId();                                   // dwOwnerId
                o1.pOption = chr.getPartyID();                              // nPartyId
                o1.uOption = chr.getSkillStatValue(w, TRUE_SIGHT_ENHANCE);  // nDecRate
                o1.wOption = chr.getParty() == null ? 0 : 1;                // nOption (in party or not)
                // enemy DEF -v%
                o2.nOption = -si.getValue(v, slv) - chr.getSkillStatValue(w, TRUE_SIGHT_GUARDBREAK);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv) + chr.getSkillStatValue(time, TRUE_SIGHT_PERSIST);
                // final damage the enemy receives increased by s%
                o3.nOption = si.getValue(s, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv) + chr.getSkillStatValue(time, TRUE_SIGHT_PERSIST);
                Rect rect = inPacket.decodePosition().getRectAround(si.getFirstRect());
                for (Life life : chr.getField().getLifesInRect(rect)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (Util.succeedProp(si.getValue(prop, slv))) {
                            if (chr.hasSkill(TRUE_SIGHT_ENHANCE)) {
                                mts.addStatOptions(MobStat.TrueSight, o1);
                            }
                            mts.addStatOptions(MobStat.PDR, o2);
                            mts.addStatOptions(MobStat.MDR, o2);
                            mts.addStatOptionsAndBroadcast(MobStat.AddDamSkill2, o3);
                        }
                    }
                }
                break;
            case SOUL_ELEMENT:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SoulMasterFinal, o1);
                break;
            case SOUL_SPEED:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case FALLING_MOON:
                if (tsm.getOption(PoseType).nOption != 1) {
                    tsm.removeStatsBySkill(RISING_SUN);
                    tsm.sendResetStatPacket();
                }
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(PoseType, o1);
                o2.nValue = si.getValue(indieCr, slv) + chr.getSkillStatValue(indieCr, MASTER_OF_THE_SWORD);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieCr, o2);
                o3.nOption = 1;
                o3.rOption = skillID;
                tsm.putCharacterStatValue(BuckShot, o3);
                break;
            case RISING_SUN:
                if (tsm.getOption(PoseType).nOption != 2) {
                    tsm.removeStatsBySkill(FALLING_MOON);
                    tsm.sendResetStatPacket();
                }
                o1.nOption = 2;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(PoseType, o1);
                o2.nValue = si.getValue(indieDamR, slv) + chr.getSkillStatValue(v, MASTER_OF_THE_SWORD);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieDamR, o2);
                o3.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? chr.getSkillStatValue(w, MASTER_OF_THE_SWORD) : si.getValue(indieBooster, slv);
                o3.nReason = skillID;
                o3.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieBooster, o3);
                break;
            case EQUINOX_CYCLE:
                // Reset FM / RS stats and replace with equinox cycle buff
                if (ServerConstants.CLIENT_SIDED_SKILL_HOOK) {
                    // remove all stats except posetype
                    tsm.removeStatsBySkill(IndieCr, FALLING_MOON);
                    tsm.removeStatsBySkill(BuckShot, FALLING_MOON);
                    tsm.removeStatsBySkill(IndieDamR, RISING_SUN);
                    tsm.removeStatsBySkill(IndieBooster, RISING_SUN);
                    tsm.removeStatsBySkill(EQUINOX_CYCLE);
                    tsm.removeStatsBySkill(EQUINOX_CYCLE_MOON);
                    tsm.removeStatsBySkill(EQUINOX_CYCLE_SUN);
                    tsm.sendResetStatPacket();
                    o1.nOption = tsm.getOption(PoseType).nOption != 0 ? tsm.getOption(PoseType).nOption : 1; // for client side buff icon
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(GlimmeringTime, o1);
                    o2.nValue = chr.getSkillStatValue(indieCr, FALLING_MOON) + chr.getSkillStatValue(indieCr, MASTER_OF_THE_SWORD);
                    o2.nReason = EQUINOX_CYCLE_MOON;
                    o2.tStart = Util.getCurrentTime();
                    o2.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieCr, o2);
                    o3.nOption = 1;
                    o3.rOption = EQUINOX_CYCLE_MOON;
                    o3.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(BuckShot, o3);
                    o4.nValue = chr.getSkillStatValue(indieDamR, RISING_SUN) + chr.getSkillStatValue(v, MASTER_OF_THE_SWORD);
                    o4.nReason = EQUINOX_CYCLE_SUN;
                    o4.tStart = Util.getCurrentTime();
                    o4.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieDamR, o4);
                    o5.nValue = chr.hasSkill(MASTER_OF_THE_SWORD) ? chr.getSkillStatValue(w, MASTER_OF_THE_SWORD) : chr.getSkillStatValue(indieBooster, RISING_SUN);
                    o5.nReason = EQUINOX_CYCLE_SUN;
                    o5.tStart = Util.getCurrentTime();
                    o5.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieBooster, o5);
                } else {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(GlimmeringTime, o1);
                }
                break;
            case SOUL_FORGE:
                o1.nValue = si.getValue(indiePad, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o3.nOption = 1;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(LightOfSpirit, o3);
                break;
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        if (cts == GlimmeringTime) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            if (ServerConstants.CLIENT_SIDED_SKILL_HOOK) {
                tsm.removeStatsBySkill(FALLING_MOON);
                tsm.removeStatsBySkill(RISING_SUN);
                chr.write(WvsContext.clientTemporaryStatReset(PoseType));
            }
            tsm.removeStatsBySkill(EQUINOX_CYCLE_MOON);
            tsm.removeStatsBySkill(EQUINOX_CYCLE_SUN);
        }
        super.handleRemoveCTS(cts);
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleCancelTimer() {
        if (willOfSteelTimer != null && !willOfSteelTimer.isDone()) {
            willOfSteelTimer.cancel(true);
        }
        super.handleCancelTimer();
    }
}
