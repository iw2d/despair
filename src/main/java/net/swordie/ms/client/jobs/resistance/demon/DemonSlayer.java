package net.swordie.ms.client.jobs.resistance.demon;

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
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class DemonSlayer extends Job {
    public static final int CURSE_OF_FURY = 30010111;
    public static final int FURY_UNLEASHED = 30010112;

    public static final int GRIM_SCYTHE = 31001000;
    public static final int BATTLE_PACT_DS = 31001001;

    public static final int WEAPON_MASTERY = 31100004;
    public static final int SOUL_EATER = 31101000;
    public static final int DARK_THRUST = 31101001;
    public static final int CHAOS_LOCK = 31101002;
    public static final int VENGEANCE = 31101003;

    public static final int JUDGEMENT = 31111000;
    public static final int VORTEX_OF_DOOM = 31111001;
    public static final int RAVEN_STORM = 31111003;
    public static final int CARRION_BREATH = 31111005;
    public static final int POSSESSED_AEGIS = 31110008;
    public static final int MAX_FURY = 31110009;

    public static final int OBSIDIAN_SKIN = 31120009;
    public static final int INFERNAL_CONCUSSION = 31121000;
    public static final int DEMON_IMPACT = 31121001;
    public static final int DEMON_CRY = 31121003;
    public static final int BINDING_DARKNESS = 31121006;
    public static final int DARK_METAMORPHOSIS = 31121005;
    public static final int BOUNDLESS_RAGE = 31121007;
    public static final int LEECH_AURA = 31121002;
    public static final int MAPLE_WARRIOR_DS = 31121004;

    public static final int CARRION_BREATH_REDUCE = 31120045;
    public static final int DARK_METAMORPHOSIS_ENHANCE = 31120046;
    public static final int DARK_METAMORPHOSIS_REDUCE = 31120048;
    public static final int DEMON_IMPACT_REDUCE = 31120051;
    public static final int BLUE_BLOOD = 31121054;
    public static final int DEMONIC_FORTITUDE_DS = 31121053;
    public static final int CERBERUS_CHOMP = 31121052;

    public static final int DEMON_LASH = 31000004;
    public static final int DEMON_LASH_2 = 31001006;
    public static final int DEMON_LASH_3 = 31001007;
    public static final int DEMON_LASH_4 = 31001008;


    private int[] addedSkills = new int[] {
            CURSE_OF_FURY,
    };

    private ScheduledFuture maxFuryTimer;
    private long leechAuraCD = Long.MIN_VALUE;


    public DemonSlayer(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
            maxFuryTimer = EventManager.addFixedRateEvent(this::maxFury, 4000, 4000);
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isDemonSlayer(id);
    }


    public void maxFury() {
        if (chr.hasSkill(MAX_FURY) && chr.getHP() > 0) {
            chr.healMP(chr.getSkillStatValue(y, MAX_FURY));
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
        if (hasHitMobs) {
            handleFuryAtom(attackInfo);
            handleLeechAura(attackInfo);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case CHAOS_LOCK:
            case VORTEX_OF_DOOM:
                o1.nOption = 1;
                o1.rOption = skillID;
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
            case CARRION_BREATH: //DoT
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
            case BINDING_DARKNESS: // bind + dot
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptions(MobStat.Freeze, o1);
                    mts.createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
            case DEMON_CRY:
                o1.nOption = -si.getValue(y, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o2.nOption = -si.getValue(z, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                o3.nOption = 1;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                o3.xOption = si.getValue(w, slv); // exp
                o3.yOption = si.getValue(w, slv); // dropRate
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptions(MobStat.PAD, o1);
                    mts.addStatOptions(MobStat.PDR, o1);
                    mts.addStatOptions(MobStat.MAD, o1);
                    mts.addStatOptions(MobStat.MDR, o1);
                    mts.addStatOptionsAndBroadcast(MobStat.ACC, o2);
                    mts.addStatOptionsAndBroadcast(MobStat.Treasure, o3);
                }
                break;
            case DEMON_IMPACT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Speed, o1);
                }
                break;
            case CERBERUS_CHOMP:
                chr.healMP(si.getValue(x, slv));
                break;
            case RAVEN_STORM:
                int healRate = si.getValue(x, slv);
                chr.heal((int) (chr.getMaxHP() * ((double) healRate / 100D)));
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void createFuryAtom(Mob mob, int fury) {
        if (fury <= 0) {
            return;
        }
        int firstImpact = Util.getRandom(35, 45);
        int secondImpact = Util.getRandom(4, 6);
        int angle = Util.getRandom(30, 70);
        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), Math.min(fury, 10), firstImpact, secondImpact,
                angle, 0, Util.getCurrentTime(), 1, 0,
                new Position(0, 0));
        chr.write(FieldPacket.createForceAtom(true, chr.getId(), mob.getObjectId(), 0,
                false, null, 0, Collections.singletonList(forceAtomInfo), null, 0, 300,
                null, 0, null));
        chr.healMP(fury);
    }

    private void handleFuryAtom(AttackInfo attackInfo) {
        // fury gain sources
        int curseOfFuryGain = chr.getSkillStatValue(z, CURSE_OF_FURY);
        int furyUnleashedGain = chr.getSkillStatValue(x, FURY_UNLEASHED);
        int demonLashGain = chr.getSkillStatValue(s, DEMON_LASH);
        int maxFuryProc = chr.getSkillStatValue(prop, MAX_FURY);
        int maxFuryGain = chr.getSkillStatValue(x, MAX_FURY);
        // create force atoms
        Field field = chr.getField();
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) field.getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            // Curse of Fury (DF gain on mob death)
            if (chr.hasSkill(CURSE_OF_FURY) && Arrays.stream(mai.damages).sum() >= mob.getHp()) {
                createFuryAtom(mob, curseOfFuryGain);
            }
            // Fury Unleashed (DF gain on boss)
            if (chr.hasSkill(FURY_UNLEASHED) && mob.isBoss()) {
                createFuryAtom(mob, furyUnleashedGain);
            }
            // Demon Lash DF gain
            if (attackInfo.skillId == DEMON_LASH || attackInfo.skillId == DEMON_LASH_2 ||
                    attackInfo.skillId == DEMON_LASH_3 || attackInfo.skillId == DEMON_LASH_4) {
                int fury = demonLashGain;
                if (Util.succeedProp(maxFuryProc)) {
                    fury += maxFuryGain;
                }
                createFuryAtom(mob, fury);
            }
        }
    }

    private void handleLeechAura(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(VampiricTouch)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(LEECH_AURA);
        int slv = chr.getSkillLevel(LEECH_AURA);
        // check and set cooldown
        if (leechAuraCD > Util.getCurrentTimeLong()) {
            return;
        }
        leechAuraCD = Util.getCurrentTimeLong() + (si.getValue(y, slv) * 1000L);
        // convert damage to hp
        int damage = attackInfo.mobAttackInfo.stream().mapToInt(mai -> Arrays.stream(mai.damages).sum()).sum();
        int damageR = tsm.getOption(VampiricTouch).nOption;
        int healAmount = (int) (damage * ((double) damageR / 100D));
        int healMaxR = si.getValue(w, slv);
        Field field = chr.getField();
        if (chr.getParty() == null) {
            int healMax = (int) (chr.getMaxHP() * ((double) healMaxR / 100D));
            if (chr.getHP() > 0) {
                chr.heal(Math.min(healMax, healAmount), true);
                chr.write(UserPacket.effect(Effect.skillAffected(LEECH_AURA, slv, 1)));
                field.broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffected(LEECH_AURA, slv, 1)), chr);
            }
        } else {
            Rect rect = chr.getRectAround(si.getFirstRect());
            System.out.println(rect);
            List<PartyMember> partyMembers = field.getPartyMembersInRect(chr, rect).stream()
                    .filter(pml -> pml.getChr().getHP() > 0)
                    .toList();
            int partyHealAmount = healAmount / partyMembers.size();
            for (PartyMember partyMember : partyMembers) {
                Char partyChr = partyMember.getChr();
                int healMax = (int) (partyChr.getMaxHP() * ((double) healMaxR / 100D));
                partyChr.heal(Math.min(healMax, partyHealAmount), true);
                partyChr.write(UserPacket.effect(Effect.skillAffected(LEECH_AURA, slv, 1)));
                field.broadcastPacket(UserRemote.effect(partyChr.getId(), Effect.skillAffected(LEECH_AURA, slv, 1)), partyChr);
            }
        }
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
        switch (skillID) {
            case VENGEANCE: //stun chance = prop | stun dur. = subTime
                o1.nOption = si.getValue(y, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PowerGuard, o1);
                break;
            case DARK_METAMORPHOSIS:
                o1.nOption = si.getValue(damR, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMhpR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o2);
                o3.nOption = 1;
                o3.rOption = skillID;
                o3.tOption = 2;
                tsm.putCharacterStatValue(NotDamaged, o3); // invincible while casting
                o4.nOption = 1;
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DevilishPower, o4);
                break;
            case BOUNDLESS_RAGE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(InfinityForce, o1);
                break;
            case LEECH_AURA:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(VampiricTouch, o1);
                break;
            case BLUE_BLOOD:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ShadowPartner, o1);
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public int alterCooldownSkill(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        switch (skillId) {
            case DEMON_CRY:
                if (tsm.hasStat(InfinityForce)) {
                    return super.alterCooldownSkill(skillId) - (chr.getSkillStatValue(s, DEMON_CRY) * 1000);
                }
                break;
        }
        return super.alterCooldownSkill(skillId);
    }

    @Override
    public int getMpCon(int skillId, int slv) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(InfinityForce)) {
            return 0;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        if (si == null) {
            return 0;
        }
        int forceCon = si.getValue(SkillStat.forceCon, slv);
        int reduceRate = 0;
        // handle hyper passives
        if (skillId == CARRION_BREATH) {
            reduceRate = chr.getSkillStatValue(reduceForceR, CARRION_BREATH_REDUCE);
        } else if (skillId == DARK_METAMORPHOSIS) {
            reduceRate = chr.getSkillStatValue(reduceForceR, DARK_METAMORPHOSIS_REDUCE);
        } else if (skillId == DEMON_IMPACT) {
            reduceRate = chr.getSkillStatValue(reduceForceR, DEMON_IMPACT_REDUCE);
        }
        // handle Blue Blood passive effect
        if (chr.hasSkill(BLUE_BLOOD)) {
            reduceRate += chr.getSkillStatValue(reduceForceR, BLUE_BLOOD);
        }
        if (reduceRate > 0) {
            forceCon -= (int) (forceCon * ((double) reduceRate / 100D));
        }
        return forceCon;
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        if (chr.hasSkill(POSSESSED_AEGIS) && hitInfo.reflect != 0) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(hitInfo.mobID);
            if (mob != null) {
                SkillInfo si = SkillData.getSkillInfoById(POSSESSED_AEGIS);
                int slv = chr.getSkillLevel(POSSESSED_AEGIS);
                if (chr.getHP() > 0) {
                    chr.heal((int) (chr.getMaxHP() * ((double) si.getValue(y, slv) / 100D)), true);
                    createFuryAtom(mob, si.getValue(z, slv));
                }
            }
        }
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleCancelTimer(Char chr) {
        if (maxFuryTimer != null && !maxFuryTimer.isDone()) {
            maxFuryTimer.cancel(true);
        }
        super.handleCancelTimer(chr);
    }

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        chr.getAvatarData().getCharacterStat().setMaxMp(0);
        chr.getAvatarData().getCharacterStat().setPosMap(927000000);
    }
}
