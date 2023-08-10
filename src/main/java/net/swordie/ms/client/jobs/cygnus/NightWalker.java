package net.swordie.ms.client.jobs.cygnus;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class NightWalker extends Noblesse {
    public static final int LUCKY_SEVEN = 14001020;
    public static final int DARK_ELEMENTAL = 14001021; //Buff (Mark of Darkness)
    public static final int HASTE = 14001022; //Buff
    public static final int DARK_SIGHT = 14001023; //Buff
    public static final int THROWING_BOOSTER = 14101022; //Buff


    public static final int CRITICAL_THROW = 14100024; //Buff
    public static final int DARK_SERVANT = 14111024; //Buff
    public static final int SPIRIT_PROJECTION = 14111025; //Buff
    public static final int DARKNESS_ASCENDING = 14110030; //Special Buff
    public static final int SHADOW_SPARK = 14111022;
    public static final int SHADOW_SPARK_EXPLOSION = 14111023;

    public static final int THROWING_EXPERT = 14120005;
    public static final int DARK_OMEN = 14121003; //Summon
    public static final int SHADOW_STITCH = 14121004; //Special Attack (Bind Debuff)
    public static final int CALL_OF_CYGNUS_NW = 14121000; //Buff
    public static final int VITALITY_SIPHON = 14120009;

    public static final int VITALITY_SIPHON_EXTRA_POINT = 14120049;
    public static final int VITALITY_SIPHON_STEEL_SKIN = 14120050;
    public static final int VITALITY_SIPHON_PREPARATION = 14120051;
    public static final int GLORY_OF_THE_GUARDIANS_NW = 14121053;
    public static final int SHADOW_ILLUSION = 14121054;
    public static final int DOMINION = 14121052;

    //Bats
    public static final int SHADOW_BAT = 14001027; //Buff (Shadow Bats) (ON/OFF)
    public static final int SHADOW_BAT_SUMMON = 14000027;
    public static final int SHADOW_BAT_ATTACK = 14000028;
    public static final int SHADOW_BAT_ATTACK_BOUND = 14000029;
    public static final int BAT_AFFINITY = 14100027; //Summon Upgrade
    public static final int BAT_AFFINITY_II = 14110029; //Summon Upgrade
    public static final int BAT_AFFINITY_III = 14120008; //Summon Upgrade

    //Dark Elemental
    public static final int ADAPTIVE_DARKNESS = 14100026;
    public static final int ADAPTIVE_DARKNESS_II = 14110028;
    public static final int ADAPTIVE_DARKNESS_III = 14120007;

    //Attacks
    public static final int QUINTUPLE_STAR = 14121001;
    public static final int QUINTUPLE_STAR_FINISHER = 14121002;

    public static final int QUAD_STAR = 14111020;
    public static final int QUAD_STAR_FINISHER = 14111021;

    public static final int TRIPLE_THROW = 14101020;
    public static final int TRIPLE_THROW_FINISHER = 14101021;

    private static final int DARKNESS_SKILLS[] = {
            DARK_ELEMENTAL,
            ADAPTIVE_DARKNESS,
            ADAPTIVE_DARKNESS_II,
            ADAPTIVE_DARKNESS_III
    };

    private int[] addedSkills = new int[] {
            IMPERIAL_RECALL,
    };

    private Map<Integer, Integer> batBounceMap = new ConcurrentHashMap<>();
    private AtomicInteger shadowBatAttackCounter = new AtomicInteger(0);
    private AtomicInteger darkOmenAttackCounter = new AtomicInteger(0);

    public NightWalker(Char chr) {
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
        return JobConstants.isNightWalker(id);
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
            // handle bat force atom attack and healing
            handleShadowBat(attackInfo);
            // handle dark elemental proc and bat creation
            if (SkillConstants.isThrowingStarSkill(attackInfo.skillId)) {
                handleDarkElemental(attackInfo);
                if (tsm.hasStat(Dominion) || shadowBatAttackCounter.incrementAndGet() % 3 == 0) {
                    summonShadowBat(false);
                }
            } else if (attackInfo.skillId == DARK_OMEN) {
                if (darkOmenAttackCounter.incrementAndGet() % 3 == 0) {
                    summonShadowBat(true);
                }
            }
            // handle dark omen cooldown reduce
            if (chr.hasSkillOnCooldown(DARK_OMEN)) {
                switch (attackInfo.skillId) {
                    case LUCKY_SEVEN:
                    case TRIPLE_THROW:
                    case QUAD_STAR:
                    case QUINTUPLE_STAR:
                    case SHADOW_BAT_ATTACK:
                    case SHADOW_BAT_ATTACK_BOUND:
                    case DARK_OMEN:
                        break;
                    default:
                        chr.reduceSkillCoolTime(DARK_OMEN, 500);
                        break;
                }
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        switch (attackInfo.skillId) {
            case SHADOW_STITCH:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = Math.min(si.getValue(time, slv) + attackInfo.mobAttackInfo.size(), si.getValue(s, slv));
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                }
                break;
            case DOMINION:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 5;
                tsm.putCharacterStatValue(NotDamaged, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieCr, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o2);
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieStance, slv);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStance, o3);
                o4.nReason = skillID;
                o4.nValue = si.getValue(indieDamR, slv);
                o4.tStart = Util.getCurrentTime();
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o4);
                o5.nOption = 1;
                o5.rOption = skillID;
                o5.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Dominion, o5);
                tsm.sendSetStatPacket();
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void handleDarkElemental(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(ElementDarkness)) {
            return;
        }
        // get debuff values
        int proc = 0;
        int maxStacks = 0;
        int defIgnore = 0;
        int dotDamage = 0;
        int interval = 0;
        int duration = 0;
        for (int skillId : DARKNESS_SKILLS) {
            if (!chr.hasSkill(skillId)) {
                continue;
            }
            SkillInfo si = SkillData.getSkillInfoById(skillId);
            int slv = chr.getSkillLevel(skillId);
            proc += si.getValue(prop, slv);
            maxStacks += si.getValue(x, slv);
            defIgnore += si.getValue(y, slv);
            dotDamage += si.getValue(dot, slv);
            interval += si.getValue(dotInterval, slv);
            duration += si.getValue(dotTime, slv);
        }
        // apply debuff
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            if (!Util.succeedProp(proc) && !tsm.hasStat(Dominion)) {
                continue;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            int stacks = 0;
            if (tsm.hasStat(Dominion)) {
                // max stacks when dominion is active
                stacks = maxStacks;
            } else {
                Option oldO = mts.getCurrentOptionsByMobStat(MobStat.ElementDarkness);
                if (oldO != null && oldO.nOption > 0) {
                    stacks = oldO.nOption;
                }
                if (stacks < maxStacks) {
                    stacks++;
                }
            }
            Option o1 = new Option();
            Option o2 = new Option();
            o1.nOption = stacks;
            o1.rOption = DARK_ELEMENTAL;
            o1.tOption = duration;
            mts.addStatOptions(MobStat.ElementDarkness, o1);
            o2.nOption = - (stacks * defIgnore);
            o2.rOption = DARK_ELEMENTAL;
            o2.tOption = duration;
            mts.addStatOptions(MobStat.PDR, o2);
            mts.addStatOptions(MobStat.MDR, o2);
            if (tsm.hasStat(Dominion)) {
                for (int i = 0; i < maxStacks; i++) {
                    mts.createAndAddBurnedInfo(chr, DARK_ELEMENTAL, chr.getSkillLevel(DARK_ELEMENTAL), dotDamage, interval, duration, maxStacks);
                }
            } else {
                mts.createAndAddBurnedInfo(chr, DARK_ELEMENTAL, chr.getSkillLevel(DARK_ELEMENTAL), dotDamage, interval, duration, maxStacks);
            }
            // increment vitality siphon buff
            updateVitalitySiphon();
        }
    }

    private void updateVitalitySiphon() {
        if (!chr.hasSkill(VITALITY_SIPHON)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(VITALITY_SIPHON);
        int slv = chr.getSkillLevel(VITALITY_SIPHON);
        int duration = si.getValue(time, slv);
        int stacks = tsm.getOption(SiphonVitality).nOption;
        if (stacks < si.getValue(x, slv)) {
            stacks++;
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        o1.nOption = stacks;
        o1.rOption = VITALITY_SIPHON;
        o1.tOption = duration;
        tsm.putCharacterStatValue(SiphonVitality, o1);
        o2.nOption = stacks * (si.getValue(y, slv) + chr.getSkillStatValue(x, VITALITY_SIPHON_EXTRA_POINT));
        o2.rOption = VITALITY_SIPHON;
        o2.tOption = duration;
        tsm.putCharacterStatValue(IncMaxHP, o2);
        if (chr.hasSkill(VITALITY_SIPHON_STEEL_SKIN)) {
            o3.nOption = stacks * chr.getSkillStatValue(x, VITALITY_SIPHON_STEEL_SKIN);
            o3.rOption = VITALITY_SIPHON;
            o3.tOption = duration;
            tsm.putCharacterStatValue(PDD, o3);
            tsm.putCharacterStatValue(MDD, o3);
        }
        if (chr.hasSkill(VITALITY_SIPHON_PREPARATION)) {
            o4.nOption = stacks * chr.getSkillStatValue(x, VITALITY_SIPHON_PREPARATION);
            o4.rOption = VITALITY_SIPHON;
            o4.tOption = duration;
            tsm.putCharacterStatValue(AsrR, o4);
        }
        tsm.sendSetStatPacket();
    }

    private int getShadowBatStat(SkillStat stat) {
        return chr.getSkillStatValue(stat, SHADOW_BAT) +
                chr.getSkillStatValue(stat, BAT_AFFINITY) +
                chr.getSkillStatValue(stat, BAT_AFFINITY_II) +
                chr.getSkillStatValue(stat, BAT_AFFINITY_III);
    }

    private void handleShadowBat(AttackInfo attackInfo) {
        if (attackInfo.skillId == SHADOW_BAT_ATTACK) {
            // handle heal on first damage
            if (chr.getHP() >= 0) {
                int healRate = chr.getSkillStatValue(x, SHADOW_BAT);
                chr.heal((int) (chr.getMaxHP() / 100D / healRate));
            }
        } else if (attackInfo.skillId == SHADOW_BAT_ATTACK_BOUND) {
            // do nothing, bounces are handled in handleForceAtomCollision
        } else {
            // initial summon -> force atom conversion
            int proc = getShadowBatStat(prop);
            if (!Util.succeedProp(proc)) {
                return;
            }
            Field field = chr.getField();
            Summon summon = field.getSummons().stream()
                    .filter(s -> s != null && s.getChr().getId() == chr.getId() &&
                            (s.getSkillID() == SHADOW_BAT_SUMMON || s.getSkillID() == BAT_AFFINITY_III))
                    .findFirst().orElse(null);
            if (summon == null) {
                return;
            }
            // get target
            Position pos = chr.getPosition();
            Rect rect = new Rect( // range = 350
                    pos.getX() - 350, pos.getY() - 350,
                    pos.getX() + 350, pos.getY() + 350
            );
            Mob target = Util.getRandomFromCollection(field.getMobsInRect(rect));
            if (target == null) {
                return;
            }
            // create force atom
            int angle = pos.getX() > target.getX() ? 90 : 270;
            int forceAtomKey = chr.getNewForceAtomKey();
            batBounceMap.put(forceAtomKey, getShadowBatStat(mobCount) - 1);
            ForceAtomEnum fae = chr.hasSkill(BAT_AFFINITY_III) ? ForceAtomEnum.NIGHT_WALKER_BAT_4 : ForceAtomEnum.NIGHT_WALKER_BAT;
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(forceAtomKey, fae.getInc(), 5, 6,
                    angle, 0, Util.getCurrentTime(), 1, 0,
                    new Position());
            field.broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(),
                    true, target.getObjectId(), SHADOW_BAT_ATTACK, forceAtomInfo, rect, 0, 30,
                    null, 0, null));
            // remove summon
            field.removeLife(summon);
        }
    }

    private void summonShadowBat(boolean fromDarkOmen) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if(!tsm.hasStat(NightWalkerBat)) {
            return;
        }
        int summonLimit = fromDarkOmen ? chr.getSkillStatValue(z, DARK_OMEN) : getShadowBatStat(y);
        int summonCount = (int) chr.getField().getSummons().stream()
                .filter(s -> s != null && s.getChr().getId() == chr.getId() &&
                        (s.getSkillID() == SHADOW_BAT_SUMMON || s.getSkillID() == BAT_AFFINITY_III) &&
                        (!fromDarkOmen || s.getFromDarkOmen()))
                .count();
        if (summonCount >= summonLimit) {
            return;
        }
        Summon summon = Summon.getSummonBy(chr, SHADOW_BAT_SUMMON, chr.getSkillLevel(SHADOW_BAT_SUMMON));
        summon.setTemplateId(chr.hasSkill(BAT_AFFINITY_III) ? BAT_AFFINITY_III : SHADOW_BAT_SUMMON);
        summon.setFlyMob(true);
        summon.setMoveAbility(MoveAbility.Fly);
        summon.setAttackActive(false);
        summon.setFromDarkOmen(fromDarkOmen);
        chr.getField().spawnAddSummon(summon);
    }


    @Override
    public void handleForceAtomCollision(int forceAtomKey, int mobId) {
        // handle bat bouncing
        Integer bouncesLeft = batBounceMap.remove(forceAtomKey);
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
        Rect rect = new Rect( // range = 350
                pos.getX() - 350, pos.getY() - 350,
                pos.getX() + 350, pos.getY() + 350
        );
        Mob target = Util.getRandomFromCollection(field.getMobsInRect(rect));
        if (target == null) {
            return;
        }
        // create force atom
        int angle = source.getX() > target.getX() ? 90 : 270;
        ForceAtomEnum fae = chr.hasSkill(BAT_AFFINITY_III) ? ForceAtomEnum.NIGHT_WALKER_FROM_MOB_4 : ForceAtomEnum.NIGHT_WALKER_FROM_MOB;
        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(forceAtomKey, fae.getInc(), 5, 6,
                angle, 0, Util.getCurrentTime(), 1, 0,
                new Position());
        field.broadcastPacket(FieldPacket.createForceAtom(true, chr.getId(), source.getObjectId(), fae.getForceAtomType(),
                true, target.getObjectId(), SHADOW_BAT_ATTACK_BOUND, forceAtomInfo, null, 0, 30,
                null, 0, pos));
        batBounceMap.put(forceAtomKey, bouncesLeft - 1);
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
        Summon summon;
        Field field;
        switch (skillID) {
            case DARK_ELEMENTAL:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ElementDarkness, o1);
                break;
            case HASTE:
                o1.nOption = si.getValue(speed, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Speed, o1);
                o2.nOption = si.getValue(jump, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Jump, o2);
                break;
            case DARK_SIGHT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DarkSight, o1);
                break;
            case THROWING_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case DARK_SERVANT:
                summonDarkServant();
                break;
            case SPIRIT_PROJECTION:
                o1.nOption = chr.getBulletIDForAttack() - 2069999;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NoBulletConsume, o1);
                break;
            case DARKNESS_ASCENDING:
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(ReviveOnce, o1);
                chr.resetSkillCoolTime(skillID);
                break;
            case SHADOW_BAT:
                if (tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(SHADOW_BAT_SUMMON);
                    tsm.removeStatsBySkill(BAT_AFFINITY_III);
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = 0;
                    tsm.putCharacterStatValue(NightWalkerBat, o1);
                }
                break;
            case SHADOW_ILLUSION:
                if (tsm.hasStatBySkillId(DARK_SERVANT)) {
                    tsm.removeStatsBySkill(DARK_SERVANT);
                    tsm.sendResetStatPacket();
                }
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ShadowIllusion, o1);
                for (int i = skillID; i < skillID + 3; i++) {
                    summon = Summon.getSummonBy(chr, i, slv);
                    field = chr.getField();
                    summon.setFlyMob(false);
                    summon.setAvatarLook(chr.getAvatarData().getAvatarLook());
                    summon.setMoveAbility(MoveAbility.WalkClone);
                    field.spawnSummon(summon);
                }
                break;
            case DARK_OMEN:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                field.spawnSummon(summon);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void summonDarkServant() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(DARK_SERVANT);
        int slv = chr.getSkillLevel(DARK_SERVANT);
        // creat esummon
        Summon summon = new Summon(DARK_SERVANT);
        summon.setChr(chr);
        summon.setSkillID(DARK_SERVANT);
        summon.setSlv((byte) slv);
        summon.setSummonTerm(chr.getJobHandler().getBuffedSkillDuration(si.getValue(time, slv)));
        summon.setCharLevel((byte) chr.getStat(Stat.level));
        summon.setPosition(chr.getPosition().deepCopy());
        summon.setMoveAction((byte) 1);
        summon.setCurFoothold((short) chr.getField().findFootHoldBelow(summon.getPosition()).getId());
        summon.setEnterType(EnterType.Animation);
        summon.setFlyMob(false);
        summon.setAttackActive(false);
        summon.setAssistType(AssistType.None);
        summon.setAvatarLook(chr.getAvatarData().getAvatarLook());
        summon.setMoveAbility(MoveAbility.WalkClone);
        chr.getField().spawnSummon(summon);
        // set buff
        Option o1 = new Option();
        o1.nOption = si.getValue(x, slv);
        o1.rOption = DARK_SERVANT;
        o1.tOption = si.getValue(time, slv);
        o1.summon = summon;
        tsm.putCharacterStatValue(ShadowServant, o1);
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        if (cts == ShadowIllusion) {
            summonDarkServant();
        }
        super.handleRemoveCTS(cts);
    }

    @Override
    public void handleWarp() {
        // clear bounce map
        batBounceMap.clear();
        super.handleWarp();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    public void reviveByDarknessAscending() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        chr.heal(chr.getMaxHP());
        tsm.removeStatsBySkill(DARKNESS_ASCENDING);
        tsm.sendResetStatPacket();
        chr.chatMessage("You have been revived by Darkness Ascending.");
        chr.write(UserPacket.effect(Effect.skillSpecial(DARKNESS_ASCENDING)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillSpecial(DARKNESS_ASCENDING)), chr);
        chr.setSkillCooldown(DARKNESS_ASCENDING, chr.getSkillLevel(DARKNESS_ASCENDING));
    }
}
