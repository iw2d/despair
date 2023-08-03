package net.swordie.ms.client.jobs.legend;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.life.mob.skill.BurnedInfo;
import net.swordie.ms.loaders.MobData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Shade extends Job {
    public static final int CLOSE_CALL = 20050286;
    public static final int CLOSE_CALL_LINK = 80000169;

    public static final int SPIRIT_BOND_I = 20050285;
    public static final int FOX_TROT = 20051284;

    public static final int KNUCKLE_MASTERY = 25100106;
    public static final int FOX_SPIRITS = 25101009; //Buff (ON/OFF)
    public static final int FOX_SPIRIT_MASTERY = 25100009;
    public static final int FOX_SPIRITS_ATOM = 25100010;
    public static final int FOX_SPIRITS_ATOM_2 = 25120115; //Upgrade
    public static final int GROUND_POUND_FIRST = 25101000;
    public static final int GROUND_POUND_SECOND = 25100001;
    public static final int GROUND_POUND_SHOCKWAVE = 25100002;

    public static final int SUMMON_OTHER_SPIRIT = 25111209; //Passive Buff (Icon)
    public static final int SPIRIT_TRAP = 25111206; //Tile
    public static final int WEAKEN = 25110210; //Passive Debuff

    public static final int SPIRIT_WARD = 25121209; //Special Buff
    public static final int MAPLE_WARRIOR_SH = 25121108; //Buff
    public static final int BOMB_PUNCH = 25121000;
    public static final int BOMB_PUNCH_FINAL = 25120003; //Special Attack (Stun Debuff)
    public static final int DEATH_MARK = 25121006; //Special Attack (Mark Debuff)
    public static final int SOUL_SPLITTER = 25121007; //Special Attack (Split)
    public static final int FIRE_FOX_SPIRIT_MASTERY = 25120110;
    public static final int HEROS_WILL_SH = 25121211;
    public static final int SPIRIT_FRENZY = 25111005;

    public static final int FIRE_FOX_SPIRITS_REPEATED = 25120153;
    public static final int HEROIC_MEMORIES_SH = 25121132;
    public static final int SPIRIT_BOND_MAX = 25121131;
    public static final int SPIRIT_INCARNATION = 25121030;

    private int[] addedSkills = new int[] {
            FOX_TROT,
            SPIRIT_BOND_I,
    };

    private Map<Integer, Integer> foxBounceMap = new ConcurrentHashMap<>();


    public Shade(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
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
        return JobConstants.isShade(id);
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
            handleWeaken(attackInfo);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Field field;
        switch (attackInfo.skillId) {
            case BOMB_PUNCH_FINAL:
                o1.nOption = 1;
                o1.rOption = BOMB_PUNCH_FINAL;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case DEATH_MARK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(dotTime, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
            case SOUL_SPLITTER:
                field = chr.getField();
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob parent = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (parent == null) {
                        continue;
                    }
                    MobTemporaryStat mts = parent.getTemporaryStat();
                    if (parent.isSplit() ||
                            Arrays.stream(mai.damages).sum() > parent.getHp() ||
                            mts.hasCurrentMobStat(MobStat.SeperateSoulP) ||
                            mts.hasCurrentMobStat(MobStat.SeperateSoulC)) {
                        continue;
                    }
                    // Parent
                    o1.nOption = si.getValue(z, slv);
                    o1.tOption = si.getValue(time, slv);
                    o1.uOption = skillID;
                    // Child
                    o2.nOption = si.getValue(x, slv);
                    o2.tOption = si.getValue(time, slv);

                    Mob child = MobData.getMobDeepCopyById(parent.getTemplateId());
                    child.setPosition(parent.getPosition());
                    child.setMaxHp(parent.getMaxHp());
                    child.setMaxMp(parent.getMaxMp());
                    child.setHp(parent.getHp());
                    child.setMp(parent.getMp());
                    child.setNotRespawnable(true);
                    child.setField(field);
                    child.setSplit(true);
                    field.spawnLife(child, null);

                    o1.rOption = child.getObjectId();
                    mts.addStatOptionsAndBroadcast(MobStat.SeperateSoulP, o1);
                    o2.rOption = parent.getObjectId();
                    child.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.SeperateSoulC, o2);
                }
                break;
            case SPIRIT_INCARNATION:
                if (!tsm.hasStatBySkillId(skillID)) {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(NotDamaged, o1);
                    tsm.sendSetStatPacket();
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void handleWeaken(AttackInfo attackInfo) {
        if (!chr.hasSkill(WEAKEN)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(WEAKEN);
        int slv = chr.getSkillLevel(WEAKEN);
        int proc = si.getValue(prop, slv);
        int duration = si.getValue(time, slv);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        o1.nOption = si.getValue(x, slv);
        o1.rOption = WEAKEN;
        o1.tOption = duration;
        o2.nOption = -si.getValue(y, slv);
        o2.rOption = WEAKEN;
        o2.tOption = duration;
        o3.nOption = -si.getValue(z, slv);
        o3.rOption = WEAKEN;
        o3.tOption = duration;
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            if (Util.succeedProp(proc)) {
                MobTemporaryStat mts = mob.getTemporaryStat();
                mts.addStatOptionsAndBroadcast(MobStat.AddDamSkill2, o1);
                mts.addStatOptionsAndBroadcast(MobStat.ACC, o2);
                mts.addStatOptionsAndBroadcast(MobStat.EVA, o3);
            }
        }
    }

    @Override
    public void handleMobBurn(Mob mob, BurnedInfo bi) {
        if (bi.getSkillId() == DEATH_MARK) {
            if (chr.getHP() > 0) {
                int healRate = chr.getSkillStatValue(x, DEATH_MARK);
                chr.heal((int) (bi.getDamage() * healRate / 100D));
            }
        }
        super.handleMobBurn(mob, bi);
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
        switch (skillID) {
            case GROUND_POUND_SHOCKWAVE:
                o1.nOption = -si.getValue(y, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                Rect rect = chr.getRectAround(si.getFirstRect());
                if (!chr.isLeft()) {
                    rect = rect.horizontalFlipAround(chr.getPosition().getX());
                }
                for (Life life : chr.getField().getLifesInRect(rect)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Speed, o1);
                    }
                }
                break;
            case SPIRIT_TRAP:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(inPacket.decodePosition());
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                aa.setDelay((short) 4);
                chr.getField().spawnAffectedArea(aa);
                break;
            case FIRE_FOX_SPIRIT_MASTERY:
            case FOX_SPIRIT_MASTERY:
                createFoxSpiritForceAtom(skillID);
                break;
            case FOX_SPIRITS:
                if (tsm.hasStat(HiddenPossession)) {
                    tsm.removeStat(HiddenPossession, false);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(HiddenPossession, o1);
                }
                break;
            case SUMMON_OTHER_SPIRIT:
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(ReviveOnce, o1);
                chr.resetSkillCoolTime(skillID);
                break;
            case SPIRIT_WARD:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SpiritGuard, o1);
                break;
            case SPIRIT_BOND_MAX:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indiePad, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o2);
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieBDR, slv);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBDR, o3);
                o4.nReason = skillID;
                o4.nValue = si.getValue(indieBooster, slv);
                o4.tStart = Util.getCurrentTime();
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o4);
                o5.nReason = skillID;
                o5.nValue = si.getValue(indieIgnoreMobpdpR, slv);
                o5.tStart = Util.getCurrentTime();
                o5.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o5);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private int getFoxMaxBounce() {
        if (chr.hasSkill(FIRE_FOX_SPIRIT_MASTERY)) {
            return chr.getSkillStatValue(y, FIRE_FOX_SPIRIT_MASTERY) +
                    chr.getSkillStatValue(z, FIRE_FOX_SPIRITS_REPEATED);
        } else {
            return chr.getSkillStatValue(y, FOX_SPIRIT_MASTERY);
        }
    }

    private void createFoxSpiritForceAtom(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(HiddenPossession)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        Field field = chr.getField();
        Position pos = chr.getPosition();
        Rect rect = pos.getRectAround(si.getFirstRect());
        Mob target = Util.getRandomFromCollection(field.getMobsInRect(rect));
        if (target == null) {
            return;
        }
        // create force atom
        int forceAtomKey = chr.getNewForceAtomKey();
        foxBounceMap.put(forceAtomKey, getFoxMaxBounce() - 1);
        int atomSkillId = chr.hasSkill(FIRE_FOX_SPIRIT_MASTERY) ? FOX_SPIRITS_ATOM_2 : FOX_SPIRITS_ATOM;
        ForceAtomEnum fae = chr.hasSkill(FIRE_FOX_SPIRIT_MASTERY) ? ForceAtomEnum.FLAMING_RABBIT_ORB : ForceAtomEnum.RABBIT_ORB;
        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(forceAtomKey, fae.getInc(), 15, 30,
                305, 0, Util.getCurrentTime(), 1, 0,
                new Position(-10, -10));
        field.broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(),
                true, target.getObjectId(), atomSkillId, forceAtomInfo, null, 0, 40,
                null, 0, pos));
    }

    @Override
    public void handleForceAtomCollision(int forceAtomKey, int mobId) {
        // handle fox bouncing
        Integer bouncesLeft = foxBounceMap.remove(forceAtomKey);
        if (bouncesLeft == null || bouncesLeft <= 0) {
            return;
        }
        // get source mob
        Field field = chr.getField();
        Mob source = (Mob) field.getLifeByObjectID(mobId);
        if (source == null) {
            return;
        }
        // get target mob
        Position pos = source.getPosition();
        Rect rect = new Rect( // set bounce range to 500
                pos.getX() - 500, pos.getY() - 500,
                pos.getX() + 500, pos.getY() + 500
        );
        Mob target = Util.getRandomFromCollection(field.getMobsInRect(rect));
        if (target == null) {
            return;
        }
        // create force atom
        int atomSkillId = chr.hasSkill(FIRE_FOX_SPIRIT_MASTERY) ? FOX_SPIRITS_ATOM_2 : FOX_SPIRITS_ATOM;
        ForceAtomEnum fae = chr.hasSkill(FIRE_FOX_SPIRIT_MASTERY) ? ForceAtomEnum.FLAMING_RABBIT_ORB_RECREATION : ForceAtomEnum.RABBIT_ORB_RECREATION;
        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(forceAtomKey, fae.getInc(), 40, 6,
                305, 0, Util.getCurrentTime(), 1, 0,
                new Position(-10, -10));
        field.broadcastPacket(FieldPacket.createForceAtom(true, chr.getId(), source.getObjectId(), fae.getForceAtomType(),
                true, target.getObjectId(), atomSkillId, forceAtomInfo, null, 0, 40,
                null, 0, pos));
        foxBounceMap.put(forceAtomKey, bouncesLeft - 1);
    }

    private void deductSpiritWard() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int count = tsm.getOption(SpiritGuard).nOption;
        count--;
        if (count <= 0) {
            tsm.removeStat(SpiritGuard, false);
            tsm.sendResetStatPacket();
        } else {
            Option o1 = new Option();
            o1.nOption = count;
            o1.rOption = SPIRIT_WARD;
            o1.tOption = tsm.getRemainingTime(SpiritGuard, SPIRIT_WARD);
            o1.setInMillis(true);
            tsm.putCharacterStatValue(SpiritGuard, o1);
            tsm.sendSetStatPacket();
        }
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(SpiritGuard) && hitInfo.hpDamage > 0) {
            deductSpiritWard();
            hitInfo.hpDamage = 0;
            hitInfo.mpDamage = 0;
        }
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleMobDebuffSkill(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(SpiritGuard)) {
            tsm.removeAllDebuffs();
            deductSpiritWard();
            return;
        }
        super.handleMobDebuffSkill(chr);
    }

    @Override
    public void handleWarp() {
        // clear bounce map
        foxBounceMap.clear();
        super.handleWarp();
    }

    public void reviveBySummonOtherSpirit() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        chr.heal(chr.getMaxHP());
        tsm.removeStatsBySkill(SUMMON_OTHER_SPIRIT);
        tsm.sendResetStatPacket();
        chr.chatMessage("You have been revived by Summon Other Spirit.");
        chr.write(UserPacket.effect(Effect.skillSpecial(SUMMON_OTHER_SPIRIT)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillSpecial(SUMMON_OTHER_SPIRIT)), chr);

        chr.write(UserPacket.effect(Effect.skillUse(25111211, (byte) 1, 0)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(25111211, (byte) 1, 0)), chr);

        Option o1 = new Option();
        o1.nOption = 1;
        o1.rOption = SUMMON_OTHER_SPIRIT;
        o1.tOption = chr.getSkillStatValue(y, SUMMON_OTHER_SPIRIT);
        tsm.putCharacterStatValue(NotDamaged, o1);
        tsm.sendSetStatPacket();
        chr.setSkillCooldown(SUMMON_OTHER_SPIRIT, chr.getSkillLevel(SUMMON_OTHER_SPIRIT));
    }

    public static boolean tryReviveByCloseCall(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillId = tsm.getOption(PreReviveOnce).rOption;
        int proc = chr.getSkillStatValue(prop, skillId);
        if (!Util.succeedProp(proc)) {
            return false;
        }
        tsm.removeStatsBySkill(skillId);
        chr.chatMessage("You have survived a fatal attack with Close Call.");
        chr.write(UserPacket.effect(Effect.skillUse(CLOSE_CALL, (byte) 1, 0)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(CLOSE_CALL, (byte) 1, 0)), chr);
        return true;
    }

    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        chr.getAvatarData().getCharacterStat().setPosMap(927030050);
    }
}
