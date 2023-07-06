package net.swordie.ms.client.jobs.resistance;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatBase;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.Mechanic;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.enums.ForceAtomEnum.*;

/**
 * Created on 12/14/2017.
 */
public class Mechanic extends Citizen {

    public static final int MECH_VEHICLE = 1932016;

    public static final int SECRET_ASSEMBLY = 30001281;
    public static final int MECHANIC_DASH = 30001068;
    public static final int HIDDEN_PEACE = 30000227;

    public static final int HUMANOID_MECH = 35001002; //Mech Suit
    public static final int TANK_MECH = 35111003; //Tank Mech Suit

    public static final int MECHANIC_RAGE = 35101006; //Buff
    public static final int PERFECT_ARMOR = 35101007; //Buff (ON/OFF)
    public static final int OPEN_PORTAL_GX9 = 35101005; //Special Skill
    public static final int ROBO_LAUNCHER_RM7 = 35101012; //Summon
    public static final int HOMING_BEACON = 35101002;

    public static final int ROCK_N_SHOCK = 35111002; //Special Summon
    public static final int ROLL_OF_THE_DICE = 35111013; //Special Buff
    public static final int SUPPORT_UNIT_HEX = 35111008; //Summon
    public static final int ADV_HOMING_BEACON = 35110017;

    public static final int ROBOT_MASTERY = 35120001;
    public static final int BOTS_N_TOTS = 35121009; //Special Summon
    public static final int BOTS_N_TOTS_SUB_SUMMON = 35121011; // Summon that spawn from the main BotsNtots
    public static final int MAPLE_WARRIOR_MECH = 35121007; //Buff
    public static final int ENHANCED_SUPPORT_UNIT = 35120002;
    public static final int HEROS_WILL_MECH = 35121008;
    public static final int HOMING_BEACON_RESEARCH = 35120017;
    public static final int ROLL_OF_THE_DICE_DD = 35120014; //Special Buff
    public static final int GIANT_ROBOT_SG_88 = 35121003;

    public static final int FOR_LIBERTY_MECH = 35121053;
    public static final int FULL_SPREAD = 35121055;
    public static final int DISTORTION_BOMB = 35121052;

    private int[] addedSkills = new int[] {
            SECRET_ASSEMBLY,
            MECHANIC_DASH,
            HIDDEN_PEACE,
    };

    private int[] homingBeacon = new int[] {
            HOMING_BEACON,
            ADV_HOMING_BEACON,
            HOMING_BEACON_RESEARCH,
    };

    private ScheduledFuture botsNTotsTimer;
    private ScheduledFuture supportUnitTimer;
    private byte gateId = 0;

    public Mechanic(Char chr) {
        super(chr);
        if(chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isMechanic(id);
    }


    public void healFromSupportUnit(Summon summon) {
        Char summonOwner = summon.getChr();
        if (summonOwner.hasSkill(ENHANCED_SUPPORT_UNIT) || summonOwner.hasSkill(SUPPORT_UNIT_HEX)) {
            SkillInfo si = SkillData.getSkillInfoById(SUPPORT_UNIT_HEX);
            byte slv = (byte) summonOwner.getSkill(SUPPORT_UNIT_HEX).getCurrentLevel();
            int healrate = si.getValue(hp, slv);
            chr.heal((int) (chr.getMaxHP() * ((double) healrate / 100)));
        }
    }

    private void spawnBotsNTotsSubSummons(Summon summon) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Position position = summon.getPosition();

        if((tsm.getOptByCTSAndSkill(IndieEmpty, BOTS_N_TOTS) != null) && chr.hasSkill(BOTS_N_TOTS)) {
            Skill skill = chr.getSkill(BOTS_N_TOTS);
            byte slv = (byte) skill.getCurrentLevel();
            Summon subSummon = Summon.getSummonBy(chr, BOTS_N_TOTS_SUB_SUMMON, slv);
            subSummon.setCurFoothold((short) chr.getField().findFootHoldBelow(position).getId());
            subSummon.setPosition(position);
            subSummon.setAttackActive(false);
            subSummon.setMoveAbility(MoveAbility.WalkRandom);

            chr.getField().spawnAddSummon(subSummon);

            botsNTotsTimer = EventManager.addEvent(() -> spawnBotsNTotsSubSummons(summon), 3, TimeUnit.SECONDS);
        }
    }



    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (skillID) {
            case DISTORTION_BOMB:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setPosition(chr.getPosition().deepCopy());
                Rect rect = aa.getPosition().getRectAround(si.getRects().get(0));
                aa.setRect(rect);
                aa.setFlip(!chr.isLeft());
                chr.getField().spawnAffectedAreaAndRemoveOld(aa);
                break;
        }

        super.handleAttack(chr, attackInfo);
    }


    private void createHumanoidMechRocketForceAtom() { // Humanoid Rockets are spread around
        Field field = chr.getField();
        SkillInfo si = SkillData.getSkillInfoById((chr.hasSkill(ADV_HOMING_BEACON) ? ADV_HOMING_BEACON : HOMING_BEACON));
        byte slv = (byte) getHomingBeaconSkill().getCurrentLevel();
        Rect rect = chr.getPosition().getRectAround(si.getRects().get(0));
        if(!chr.isLeft()) {
            rect = rect.moveRight();
        }
        List<Mob> mobs = field.getMobsInRect(rect);
        if(mobs.size() <= 0) {
            return;
        }
        for(int i = 0; i < getHomingBeaconBulletCount(); i++) {
            Mob mob = Util.getRandomFromCollection(mobs);
            int inc = getHomingBeaconForceAtomEnum().getInc();
            int type = getHomingBeaconForceAtomEnum().getForceAtomType();
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, 30, 25,
                    0, 200 + (i * 2), Util.getCurrentTime(), 1, 0,
                    new Position());
            field.broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), type,
                    true, mob.getObjectId(), HOMING_BEACON, forceAtomInfo, rect, 90, 30,
                    mob.getPosition(), 0, mob.getPosition()));
        }

    }

    private void createTankMechRocketForceAtom() { // Tank Rockets are focused on 1 enemy
        Field field = chr.getField();
        SkillInfo si = SkillData.getSkillInfoById((chr.hasSkill(ADV_HOMING_BEACON) ? ADV_HOMING_BEACON : HOMING_BEACON));
        byte slv = (byte) getHomingBeaconSkill().getCurrentLevel();
        Rect rect = chr.getPosition().getRectAround(si.getRects().get(0));
        if(!chr.isLeft()) {
            rect = rect.moveRight();
        }
        if(field.getMobsInRect(rect).size() <= 0) {
            return;
        }
        Mob mob = Util.getRandomFromCollection(field.getMobsInRect(rect));
        if(field.getBossMobsInRect(rect).size() > 0) {
            mob = Util.getRandomFromCollection(field.getBossMobsInRect(rect));
        }


        for(int i = 0; i < getHomingBeaconBulletCount(); i++) {
            int inc = getHomingBeaconForceAtomEnum().getInc();
            int type = getHomingBeaconForceAtomEnum().getForceAtomType();
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, 30, 25,
                    0, 200, Util.getCurrentTime(), 1, 0,
                    new Position());
            field.broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), type,
                    true, mob.getObjectId(), HOMING_BEACON, forceAtomInfo, rect, 90, 30,
                    mob.getPosition(), 0, mob.getPosition()));
        }
    }

    private ForceAtomEnum getHomingBeaconForceAtomEnum() {
        switch (getHomingBeaconSkill().getSkillId()) {
            case ADV_HOMING_BEACON:
                return MECH_MEGA_ROCKET_1;
            case HOMING_BEACON_RESEARCH:
                return MECH_MEGA_ROCKET_2;
            case HOMING_BEACON:
            default:
                return MECH_ROCKET;
        }
    }

    private Skill getHomingBeaconSkill() {
        Skill skill = null;
        for(int skillId : homingBeacon) {
            if(chr.hasSkill(skillId)) {
                skill = chr.getSkill(skillId);
            }
        }

        return skill;
    }

    private int getHomingBeaconBulletCount() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int forceAtomCount = 0;
        for(int skillId : homingBeacon) {
            if(chr.hasSkill(skillId)) {
                Skill skill = chr.getSkill(skillId);
                SkillInfo si = SkillData.getSkillInfoById(skillId);
                byte slv = (byte) skill.getCurrentLevel();
                forceAtomCount += si.getValue(bulletCount, slv);
            }
        }
        if(tsm.getOptByCTSAndSkill(BombTime, FULL_SPREAD) != null) {
            forceAtomCount += chr.hasSkill(FULL_SPREAD) ? SkillData.getSkillInfoById(FULL_SPREAD).getValue(x, chr.getSkill(FULL_SPREAD).getCurrentLevel()) : 0;
        }
        return forceAtomCount;
    }

    private void applySupportUnitDebuffOnMob(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if(!chr.hasSkill(SUPPORT_UNIT_HEX) || tsm.getOptByCTSAndSkill(IndieEmpty, skillId) == null) {
            return;
        }
        Skill skill = chr.getSkill(skillId);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        SkillInfo suhInfo = SkillData.getSkillInfoById(SUPPORT_UNIT_HEX);
        byte slv = (byte) skill.getCurrentLevel();

        Option o = new Option();
        Field field = chr.getField();
        for(Mob mob : field.getMobs()) {
            MobTemporaryStat mts = mob.getTemporaryStat();
            o.nOption = -suhInfo.getValue(w, chr.getSkill(SUPPORT_UNIT_HEX).getCurrentLevel()); // enhancement doesn't contain the debuff info
            o.rOption = skill.getSkillId();
            o.tOption = 6;
            mts.addStatOptionsAndBroadcast(MobStat.PDR, o);
            mts.addStatOptionsAndBroadcast(MobStat.MDR, o);
        }

        supportUnitTimer = EventManager.addEvent(() -> applySupportUnitDebuffOnMob(skillId), si.getValue(x, slv), TimeUnit.SECONDS);
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
        TemporaryStatBase tsb = tsm.getTSBByTSIndex(TSIndex.RideVehicle);
        SkillInfo si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        Option o6 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case SECRET_ASSEMBLY:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case OPEN_PORTAL_GX9:
                field = chr.getField();
                int duration = si.getValue(time, slv);
                if (chr.hasSkill(ROBOT_MASTERY)) {
                    SkillInfo robotMastery = SkillData.getSkillInfoById(ROBOT_MASTERY);
                    duration *= 1 + (double) (robotMastery.getValue(x, chr.getSkillLevel(ROBOT_MASTERY)) / 100);
                }
                OpenGate openGate = new OpenGate(chr, chr.getPosition(), chr.getParty(), gateId, duration);
                if (gateId == 0) {
                    gateId = 1;
                } else if (gateId == 1) {
                    gateId = 0;
                }
                openGate.spawnOpenGate(field);
                break;
            case HOMING_BEACON: //4
            case ADV_HOMING_BEACON: // 4thJob upgrade +5 -> 9
                if(tsm.hasStat(Mechanic) && tsm.getOption(Mechanic).nOption <= 0) {
                    createHumanoidMechRocketForceAtom();
                } else if (tsm.hasStat(Mechanic) && tsm.getOption(Mechanic).nOption == 1) {
                    createTankMechRocketForceAtom();
                }
                break;
            case HEROS_WILL_MECH:
                tsm.removeAllDebuffs();
                break;
            case ROCK_N_SHOCK:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.None);
                if (chr.hasSkill(ROBOT_MASTERY)) {
                    SkillInfo robotMastery = SkillData.getSkillInfoById(ROBOT_MASTERY);
                    summon.setSummonTerm((int) (summon.getSummonTerm() * (double) (1 + (robotMastery.getValue(x, chr.getSkillLevel(ROBOT_MASTERY)) / 100))));
                }
                field.spawnAddSummon(summon);
                int rockNShockSize = inPacket.decodeByte();
                if (rockNShockSize == 2) {
                    List<Summon> rockNshockLifes = field.getSummons().stream().filter(s -> s.getSkillID() == ROCK_N_SHOCK && s.getChr() == chr).collect(Collectors.toList());
                    field.spawnAddSummon(summon);
                    field.broadcastPacket(UserPacket.teslaTriangle(rockNshockLifes, chr.getId()));
                } else {
                    chr.resetSkillCoolTime(skillID);
                }
                break;
            case GIANT_ROBOT_SG_88:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.Attack);
                summon.setMoveAction((byte) 1);
                summon.setSummonTerm(5);
                tsm.removeStatsBySkill(skillID);
                field.spawnAddSummon(summon);
                break;
            case HUMANOID_MECH:
                o1.nOption = si.getCurrentLevel();
                o1.rOption = skillID;
                tsm.putCharacterStatValue(Mechanic, o1);
                o2.nOption = si.getValue(epad, slv);
                o2.rOption = skillID;
                o2.tOption = 0;
                tsm.putCharacterStatValue(CharacterTemporaryStat.PAD, o2);
                o3.nOption = si.getValue(emmp, slv);
                o3.rOption = skillID;
                o3.tOption = 0;
                tsm.putCharacterStatValue(EMMP, o3);
                o4.nOption = si.getValue(emhp, slv);
                o4.rOption = skillID;
                o4.tOption = 0;
                tsm.putCharacterStatValue(EMHP, o4);
                o5.nOption = si.getValue(indieSpeed, slv);
                o5.rOption = skillID;
                o5.tOption = 0;
                tsm.putCharacterStatValue(IndieSpeed, o5);

                o6.nOption = MECH_VEHICLE;
                o6.rOption = skillID + 100;
                tsb.setOption(o6);
                tsm.putCharacterStatValue(RideVehicle, tsb.getOption());
                break;
            case TANK_MECH:
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(Mechanic, o1);
                tsm.sendSetStatPacket();

                o2.nOption = MECH_VEHICLE;
                o2.rOption = skillID + 100;
                tsb.setOption(o2);
                tsm.putCharacterStatValue(RideVehicle, tsb.getOption());
                break;
            case MECHANIC_RAGE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case PERFECT_ARMOR:
                if(tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(skillID);
                } else {
                    o1.nOption = si.getValue(x, slv);
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(PowerGuard, o1);
                }
                break;
            case ROLL_OF_THE_DICE:
                int random = new Random().nextInt(6)+1;

                chr.write(UserPacket.effect(Effect.avatarOriented("Skill/"+ (skillID/10000) +".img/skill/"+ skillID +"/affected/" + random)));
                chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.avatarOriented("Skill/"+ (skillID/10000) +".img/skill/"+ skillID +"/affected/" + random)));

                if(random < 2) {
                    return;
                }

                o1.nOption = random;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);

                tsm.throwDice(random);
                tsm.putCharacterStatValue(Dice, o1);
                break;
            case ROLL_OF_THE_DICE_DD:
                random = new Random().nextInt(6)+1;
                int randomDD = new Random().nextInt(6)+1;

                chr.write(UserPacket.effect(Effect.avatarOriented("Skill/"+ (skillID/10000) +".img/skill/"+ skillID +"/affected/" + random)));
                chr.write(UserPacket.effect(Effect.avatarOriented("Skill/"+ (skillID/10000) +".img/skill/"+ skillID +"/specialAffected/" + randomDD)));
                chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.avatarOriented("Skill/"+ (skillID/10000) +".img/skill/"+ skillID +"/affected/" + random)));
                chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.avatarOriented("Skill/"+ (skillID/10000) +".img/skill/"+ skillID +"/specialAffected/" + randomDD)));

                if(random < 2 && randomDD < 2) {
                    return;
                }

                o1.nOption = (random * 10) + randomDD; // if rolled: 5 and 7, the DoubleDown nOption = 57
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);

                tsm.throwDice(random, randomDD);
                tsm.putCharacterStatValue(Dice, o1);
                break;
            case MAPLE_WARRIOR_MECH:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case ENHANCED_SUPPORT_UNIT:
                o2.nReason = skillID;
                o2.nValue = si.getValue(z, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = 80;
                tsm.putCharacterStatValue(IndieDamR, o2);
                // Fallthrough intended
            case SUPPORT_UNIT_HEX:

                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.None);
                summon.setAttackActive(false);
                field.spawnSummon(summon);

                if(supportUnitTimer != null && !supportUnitTimer.isDone()) {
                    supportUnitTimer.cancel(true);
                }
                applySupportUnitDebuffOnMob(skillID);
                break;
            case ROBO_LAUNCHER_RM7:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(true);
                summon.setMoveAbility(MoveAbility.Stop);
                field.spawnSummon(summon);
                break;
            case BOTS_N_TOTS:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.None);
                summon.setAttackActive(false);
                field.spawnSummon(summon);

                if(botsNTotsTimer != null && !botsNTotsTimer.isDone()) {
                    botsNTotsTimer.cancel(true);
                }
                botsNTotsTimer = EventManager.addEvent(() -> spawnBotsNTotsSubSummons(summon), 3, TimeUnit.SECONDS);
                break;
            case FOR_LIBERTY_MECH:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMaxDamageOverR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMaxDamageOverR, o2);
                break;
            case FULL_SPREAD:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BombTime, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }
}
