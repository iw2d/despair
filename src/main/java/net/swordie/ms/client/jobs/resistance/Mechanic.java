package net.swordie.ms.client.jobs.resistance;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatBase;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.Mechanic;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.enums.ForceAtomEnum.*;

/**
 * Created on 12/14/2017.
 */
public class Mechanic extends Citizen {

    public static final int MECH_VEHICLE = 1932016;

    public static final int MECHANIC_DASH = 30001068;
    public static final int HIDDEN_PEACE = 30000227;

    public static final int HUMANOID_MECH = 35001002; //Mech Suit
    public static final int TANK_MECH = 35111003; //Tank Mech Suit

    public static final int MECHANIC_MASTERY = 35100000;
    public static final int MECHANIC_RAGE = 35101006; //Buff
    public static final int PERFECT_ARMOR = 35101007; //Buff (ON/OFF)
    public static final int OPEN_PORTAL_GX9 = 35101005; //Special Skill
    public static final int ROBO_LAUNCHER_RM7 = 35101012; //Summon
    public static final int HOMING_BEACON = 35101002;

    public static final int BATTLE_PROGRAM = 35110019;
    public static final int MECHANIZED_DEFENSE_SYSTEM = 35110018;
    public static final int ROCK_N_SHOCK = 35111002; //Special Summon
    public static final int ROLL_OF_THE_DICE = 35111013; //Special Buff
    public static final int SUPPORT_UNIT_HEX = 35111008; //Summon
    public static final int ADV_HOMING_BEACON = 35110017;

    public static final int EXTREME_MECH = 35120000;
    public static final int ROBOT_MASTERY = 35120001; // DamR handled client side, doesn't show on range
    public static final int MECH_ALLOY_RESEARCH = 35120018;
    public static final int BOTS_N_TOTS = 35121009; //Special Summon
    public static final int BOTS_N_TOTS_SUB_SUMMON = 35121011; // Summon that spawn from the main BotsNtots
    public static final int MAPLE_WARRIOR_MECH = 35121007; //Buff
    public static final int ENHANCED_SUPPORT_UNIT = 35120002;
    public static final int HEROS_WILL_MECH = 35121008;
    public static final int HOMING_BEACON_RESEARCH = 35120017;
    public static final int ROLL_OF_THE_DICE_DD = 35120014; //Special Buff
    public static final int GIANT_ROBOT_SG_88 = 35121003;

    public static final int ROCK_N_SHOCK_PERSIST = 35120044;
    public static final int ROCK_N_SHOCK_COOLDOWN = 35120045;
    public static final int SUPPORT_UNIT_HEX_PARTY = 35120047;
    public static final int SUPPORT_UNIT_HEX_PERSIST = 35120048;
    public static final int FOR_LIBERTY_MECH = 35121053;
    public static final int FULL_SPREAD = 35121055;
    public static final int DISTORTION_BOMB = 35121052;

    private ScheduledFuture openGateTimer;
    private ScheduledFuture rockNShockTimer;
    private ScheduledFuture botsNTotsTimer;


    public Mechanic(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isMechanic(id);
    }

    private static Rect getSupportUnitRect(int skillId) {
        if (skillId == SUPPORT_UNIT_HEX) {
            // does not have rect in WZ, enhanced rect is (-400, -400, 400, 400)
            return new Rect(-300, -300, 300, 300);
        }
        return SkillData.getSkillInfoById(skillId).getFirstRect();
    }

    public static void healBySupportUnit(Summon summon) {
        int skillId = summon.getSkillID();
        int slv = summon.getSlv();
        int healRate = SkillData.getSkillInfoById(skillId).getValue(hp, slv);
        Char owner = summon.getChr();
        Field field = summon.getField();
        Effect effect = Effect.skillAffected(skillId, slv, 0);
        for (Char target : field.getCharsInRect(summon.getRectAround(getSupportUnitRect(skillId)))) {
            if (target == owner || (owner.getParty() != null && owner.getParty().isPartyMember(target))) {
                if (target.getHP() > 0) {
                    target.heal((int) (target.getMaxHP() * ((double) healRate / 100D)), true);
                    target.write(UserPacket.effect(effect));
                    field.broadcastPacket(UserRemote.effect(target.getId(), effect), target);
                }
            }
        }
    }

    public static void despawnOpenGates(Field field, Char owner) {
        List<OpenGate> toRemove = new ArrayList<>();
        for (OpenGate openGate : field.getOpenGates()) {
            if (openGate.getChr() == owner) {
                toRemove.add(openGate);
            }
        }
        toRemove.forEach((openGate) -> openGate.despawnOpenGate(field));
    }

    public static void despawnRockNShocks(Field field, Char owner) {
        List<Integer> toRemove = new ArrayList<>();
        for (Summon summon : field.getSummons().stream().filter(s -> s.getSkillID() == ROCK_N_SHOCK && s.getChr() == owner).toList()) {
            toRemove.add(summon.getObjectId());
        }
        toRemove.forEach((objectId) -> field.removeLife(objectId, false));
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
            case ROCK_N_SHOCK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = GameConstants.DEFAULT_STUN_DURATION; // maybe x = 1?
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(y, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case DISTORTION_BOMB:
                if (attackInfo.attackHeader != OutHeader.REMOTE_MELEE_ATTACK) {
                    break;
                }
                Position position = new Position(attackInfo.x, attackInfo.y);
                Rect rect = position.getRectAround(si.getFirstRect());
                if (!attackInfo.left) {
                    rect = rect.horizontalFlipAround(position.getX());
                }
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setPosition(position);
                aa.setRect(rect);
                aa.setFlip(!attackInfo.left);
                chr.getField().spawnAffectedAreaAndRemoveOld(aa);
                chr.setSkillCooldown(skillID, slv);
                break;
        }
        super.handleAttack(chr, attackInfo);
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
        Position position;
        Summon summon;
        Field field;
        int duration;
        switch (skillID) {
            case HUMANOID_MECH:
            case TANK_MECH:
                handleMech(skillID);
                break;
            case HOMING_BEACON:
            case ADV_HOMING_BEACON:
                field = chr.getField();
                List<Mob> targets = new ArrayList<>();
                int count = inPacket.decodeByte();
                for (int i = 0; i < count; i++) {
                    int mobId = inPacket.decodeInt();
                    Mob mob = (Mob) field.getLifeByObjectID(mobId);
                    if (mob != null) {
                        targets.add(mob);
                    }
                }
                if (targets.size() == 0 || !tsm.hasStat(Mechanic)) {
                    break;
                }
                createHomingBeaconForceAtom(skillID, targets, tsm.getOption(Mechanic).rOption == TANK_MECH);
                break;
            case MECHANIC_RAGE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case PERFECT_ARMOR:
                if (tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = si.getValue(x, slv);
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(PowerGuard, o1);
                }
                break;
            case OPEN_PORTAL_GX9:
                if (openGateTimer != null && !openGateTimer.isDone()) {
                    openGateTimer.cancel(false);
                }
                position = inPacket.decodePosition();
                field = chr.getField();
                int gateId = 0;
                List<OpenGate> existingOpenGates = field.getOpenGates().stream().filter(g -> g.getChr() == chr).toList();
                if (existingOpenGates.size() >= 2) {
                    despawnOpenGates(field, chr);
                } else if (existingOpenGates.size() > 0) {
                    gateId = 1;
                }
                OpenGate openGate = new OpenGate(chr, position, chr.getParty(), gateId);
                openGate.spawnOpenGate(chr.getField());
                openGateTimer = EventManager.addEvent(() -> despawnOpenGates(chr.getField(), chr), getBuffedSummonDuration(si.getValue(time, slv)), TimeUnit.SECONDS);
                break;
            case ROBO_LAUNCHER_RM7:
                position = inPacket.decodePosition();
                summon = Summon.getSummonBy(chr, skillID, slv);
                summon.setPosition(position);
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.Attack);
                summon.setAttackActive(true);
                chr.getField().spawnSummon(summon);
                break;
            case ROCK_N_SHOCK:
                if (rockNShockTimer != null && !rockNShockTimer.isDone()) {
                    rockNShockTimer.cancel(false);
                }
                int rockNShockCount = inPacket.decodeByte();
                List<Integer> rockNShockIds = new ArrayList<>();
                if (rockNShockCount == 2) {
                    rockNShockIds.add(inPacket.decodeInt());
                    rockNShockIds.add(inPacket.decodeInt());
                }
                position = inPacket.decodePosition();
                field = chr.getField();
                if (rockNShockCount >= 3) {
                    despawnRockNShocks(field, chr);
                }
                summon = new Summon(skillID);
                summon.setChr(chr);
                summon.setSkillID(skillID);
                summon.setSlv((byte) slv);
                summon.setCharLevel((byte) chr.getStat(Stat.level));
                summon.setPosition(position);
                summon.setMoveAction((byte) 1);
                summon.setCurFoothold((short) field.findFootholdBelow(summon.getPosition()).getId());
                summon.setEnterType(EnterType.Animation);
                summon.setFlyMob(false);
                summon.setSummonTerm(0);
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.None);
                summon.setAttackActive(false);
                summon.setBeforeFirstAttack(false);
                field.spawnAddSummon(summon);
                // try creating triangle
                duration = getBuffedSummonDuration(si.getValue(time, slv) + this.chr.getSkillStatValue(time, ROCK_N_SHOCK_PERSIST));
                rockNShockIds.add(summon.getObjectId());
                if (rockNShockIds.size() == 3) {
                    field.broadcastPacket(UserPacket.teslaTriangle(rockNShockIds, chr.getId()));
                    int cooldown = si.getValue(cooltime, slv);
                    if (chr.hasSkill(ROCK_N_SHOCK_COOLDOWN)) {
                        cooldown -= (int) (cooldown * ((double) chr.getSkillStatValue(coolTimeR, ROCK_N_SHOCK_COOLDOWN) / 100D));
                    }
                    chr.addSkillCooldown(skillID, getBuffedSkillCooldown(cooldown * 1000));
                }
                rockNShockTimer = EventManager.addEvent(() -> despawnRockNShocks(chr.getField(), chr), duration, TimeUnit.SECONDS);
                break;
            case SUPPORT_UNIT_HEX:
            case ENHANCED_SUPPORT_UNIT:
                // spawn summon
                field = chr.getField();
                position = inPacket.decodePosition();
                duration = getBuffedSummonDuration(si.getValue(time, slv) + this.chr.getSkillStatValue(time, SUPPORT_UNIT_HEX_PERSIST));
                summon = Summon.getSummonBy(chr, skillID, slv, duration);
                summon.setPosition(position);
                summon.setCurFoothold((short) field.findFootholdBelow(summon.getPosition()).getId());
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.None);
                summon.setAttackActive(false);
                field.spawnSummon(summon);
                // spawn AA
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setRemoveSkill(false);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(position);
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                aa.setDelay((short) 9);
                aa.setDuration(summon.getSummonTerm());
                field.spawnAffectedArea(aa);
                break;
            case GIANT_ROBOT_SG_88:
                position = inPacket.decodePosition();
                field = chr.getField();
                summon = Summon.getSummonBy(chr, skillID, slv);
                summon.setPosition(position);
                summon.setCurFoothold((short) field.findFootholdBelow(summon.getPosition()).getId());
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.AttackManual);
                summon.setAttackActive(true);
                field.spawnSummon(summon);
                break;
            case BOTS_N_TOTS:
                position = inPacket.decodePosition();
                field = chr.getField();
                summon = Summon.getSummonBy(chr, skillID, slv);
                summon.setPosition(position);
                summon.setCurFoothold((short) field.findFootholdBelow(summon.getPosition()).getId());
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                summon.setAssistType(AssistType.None);
                summon.setAttackActive(false);
                field.spawnSummon(summon);
                if (botsNTotsTimer != null && !botsNTotsTimer.isDone()) {
                    botsNTotsTimer.cancel(true);
                }
                EventManager.addEvent(() -> handleBotsNTots(summon), si.getValue(x, slv), TimeUnit.SECONDS);
                break;
            case FULL_SPREAD:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BombTime, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void handleMech(int mechSkillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int statSkillId = chr.hasSkill(EXTREME_MECH) ? EXTREME_MECH : HUMANOID_MECH; // mastery / indieMhpR for Extreme Mech handled as passive
        SkillInfo si = SkillData.getSkillInfoById(statSkillId);
        int slv = chr.getSkillLevel(statSkillId);
        // general stats - CriticalBuff is used for CR here so it gets overwritten
        tsm.putCharacterStatValue(EMHP, mechSkillId, si.getValue(emhp, slv), 0);
        tsm.putCharacterStatValue(EMMP, mechSkillId, si.getValue(emmp, slv), 0);
        tsm.putCharacterStatValue(Speed, mechSkillId, si.getValue(indieSpeed, slv) + chr.getSkillStatValue(indieSpeed, BATTLE_PROGRAM), 0);
        tsm.putCharacterStatValue(EPAD, mechSkillId, si.getValue(epad, slv), 0);
        tsm.putCharacterStatValue(EDEF, mechSkillId, si.getValue(epdd, slv), 0);
        tsm.putCharacterStatValue(CriticalBuff, mechSkillId, mechSkillId == TANK_MECH ? chr.getSkillStatValue(cr, TANK_MECH) : 0, 0);
        // handle Mechanized Defense System here, Metal Alloy Research is handled as passive
        if (chr.hasSkill(MECHANIZED_DEFENSE_SYSTEM)) {
            SkillInfo mdsSi = SkillData.getSkillInfoById(MECHANIZED_DEFENSE_SYSTEM);
            int mdsSlv = chr.getSkillLevel(MECHANIZED_DEFENSE_SYSTEM);
            tsm.putCharacterStatValue(IndieMHPR, MECHANIZED_DEFENSE_SYSTEM, mdsSi.getValue(mhpR, mdsSlv), 0);
            tsm.putCharacterStatValue(IndieMMPR, MECHANIZED_DEFENSE_SYSTEM, mdsSi.getValue(mmpR, mdsSlv), 0);
            tsm.putCharacterStatValue(IndieDEF, MECHANIZED_DEFENSE_SYSTEM, mdsSi.getValue(pddX, mdsSlv), 0);
            tsm.putCharacterStatValue(DamageReduce, MECHANIZED_DEFENSE_SYSTEM, mdsSi.getValue(ignoreMobDamR, mdsSlv), 0);
        }
        // put Mechanic and RideVehicle CTS and send
        Option o1 = new Option();
        Option o2 = new Option();
        o1.nOption = slv;
        o1.rOption = mechSkillId;
        tsm.putCharacterStatValue(Mechanic, o1);
        TemporaryStatBase tsb = tsm.getTSBByTSIndex(TSIndex.RideVehicle);
        o2.nOption = MECH_VEHICLE;
        o2.rOption = mechSkillId;
        tsb.setOption(o2);
        tsm.putCharacterStatValue(RideVehicle, tsb.getOption()); // Note: HiddenPieceOn for MechanicHUE
        tsm.sendSetStatPacket();
    }

    private ForceAtomEnum getHomingBeaconForceAtomEnum() {
        if (chr.hasSkill(HOMING_BEACON_RESEARCH)) {
            return MECH_MEGA_ROCKET_2;
        } else if (chr.hasSkill(ADV_HOMING_BEACON)) {
            return MECH_MEGA_ROCKET_1;
        } else {
            return MECH_ROCKET;
        }
    }

    private int getHomingBeaconMaxCount(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int maxCount = chr.getSkillStatValue(bulletCount, skillId);
        if (chr.hasSkill(HOMING_BEACON_RESEARCH)) {
            maxCount += chr.getSkillStatValue(bulletCount, HOMING_BEACON_RESEARCH);
        }
        if (tsm.hasStat(BombTime)) {
            maxCount += tsm.getOption(BombTime).nOption;
        }
        return maxCount;
    }

    private void createHomingBeaconForceAtom(int skillId, List<Mob> targets, boolean isTankMech) {
        ForceAtomEnum fae = getHomingBeaconForceAtomEnum();
        List<ForceAtomInfo> faiList = new ArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        int count = getHomingBeaconMaxCount(skillId);
        long highestHp = -1;
        Mob highestHpTarget = null;
        for (Mob target : targets) {
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc(), 50, Util.getRandom(10, 15),
                    Util.getRandom(25), 200 + (count * 20), Util.getCurrentTime(), 1, 0, new Position());
            faiList.add(forceAtomInfo);
            targetList.add(target.getObjectId());
            if (highestHp < target.getHp()) {
                highestHp = target.getHp();
                highestHpTarget = target;
            }
            count--;
        }
        if (isTankMech && highestHpTarget != null) {
            // the remaining beacons target highest hp mob in tank mode
            while (count > 0) {
                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc(), 50, Util.getRandom(10, 15),
                        Util.getRandom(25), 200 + (count * 20), Util.getCurrentTime(), 1, 0, new Position());
                faiList.add(forceAtomInfo);
                targetList.add(highestHpTarget.getObjectId());
                count--;
            }
        }
        if (faiList.size() == 0) {
            return;
        }
        chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(),
                true, targetList, HOMING_BEACON, faiList, null, 0, 0,
                null, 0, null));
    }

    private void handleBotsNTots(Summon parent) {
        if (!chr.hasSkill(BOTS_N_TOTS) || parent == null) {
            return;
        }
        Field field = parent.getField();
        if (field.getLifeByObjectID(parent.getObjectId()) == null) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(BOTS_N_TOTS);
        int slv = chr.getSkillLevel(BOTS_N_TOTS);

        Summon summon = new Summon(BOTS_N_TOTS_SUB_SUMMON);
        summon.setChr(chr);
        summon.setSkillID(BOTS_N_TOTS_SUB_SUMMON);
        summon.setSlv((byte) slv);
        summon.setCharLevel((byte) chr.getStat(Stat.level));
        summon.setPosition(parent.getPosition());
        summon.setMoveAction((byte) 1);
        summon.setCurFoothold((short) field.findFootholdBelow(parent.getPosition()).getId());
        summon.setEnterType(EnterType.Animation);
        summon.setFlyMob(false);
        summon.setSummonTerm(0);
        summon.setMoveAbility(MoveAbility.WalkRandom);
        summon.setAssistType(AssistType.None);
        summon.setAttackActive(false);
        summon.setBeforeFirstAttack(false);
        field.spawnAddSummon(summon);
        botsNTotsTimer = EventManager.addEvent(() -> handleBotsNTots(parent), si.getValue(x, slv), TimeUnit.SECONDS);
    }

    @Override
    public int alterCooldownSkill(int skillId) {
        if (skillId == ROCK_N_SHOCK) {
            return 0;
        }
        return super.alterCooldownSkill(skillId);
    }

    @Override
    public void handleWarp() {
        // despawning handled in Field.removeChar
        if (openGateTimer != null && !openGateTimer.isDone()) {
            openGateTimer.cancel(false);
        }
        if (rockNShockTimer != null && !rockNShockTimer.isDone()) {
            rockNShockTimer.cancel(false);
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStatBySkillId(ROCK_N_SHOCK)) {
            tsm.removeStatsBySkill(ROCK_N_SHOCK);
            tsm.sendResetStatPacket();
        }
        super.handleWarp();
    }

    @Override
    public void handleCancelTimer() {
        if (openGateTimer != null && !openGateTimer.isDone()) {
            openGateTimer.cancel(false);
        }
        if (rockNShockTimer != null && !rockNShockTimer.isDone()) {
            rockNShockTimer.cancel(false);
        }
        super.handleCancelTimer();
    }

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        if (cts == Mechanic) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            tsm.removeStatsBySkill(MECHANIZED_DEFENSE_SYSTEM);
        }
        super.handleRemoveCTS(cts);
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }


    @Override
    public void handleSetJob(short jobId) {
        if (JobConstants.isMechanic(jobId)) {
            chr.addSkill(MECHANIC_DASH, 1, 1);
        }
        super.handleSetJob(jobId);
    }
}
