package net.swordie.ms.client.jobs.resistance;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Summoned;
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
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class BattleMage extends Citizen {
    public static final int CONDEMNATION = 32001014; //Special Buff (ON/OFF)
    public static final int HASTY_AURA = 32001016; //Buff (Unlimited Duration)

    public static final int STAFF_MASTERY = 32100006;
    public static final int GRIM_CONTRACT = 32100010;
    public static final int DRAINING_AURA = 32101009; //Buff (Unlimited Duration)
    public static final int STAFF_BOOST = 32101005; //Buff
    public static final int DARK_CHAIN = 32101001; //Special Attack (Stun Debuff)

    public static final int GRIM_CONTRACT_II = 32110017;
    public static final int BLUE_AURA = 32111012; //Buff (Unlimited Duration
    public static final int DARK_SHOCK = 32111016; //Buff (ON/OFF)

    public static final int STAFF_EXPERT = 32120016;
    public static final int GRIM_CONTRACT_III = 32120019;
    public static final int DARK_GENESIS = 32121004; //Special Attack (Stun Debuff) (Special Properties if on Cooldown)
    public static final int DARK_GENESIS_FA = 32121011; // Final Attack  attack if DarkGenesis is on CD
    public static final int DARK_AURA = 32121017; //Buff (Unlimited Duration)
    public static final int WEAKENING_AURA = 32121018; //Buff (Unlimited Duration)
    public static final int PARTY_SHIELD = 32121006;
    public static final int BATTLE_RAGE = 32121010; //Buff (ON/OFF)
    public static final int MAPLE_WARRIOR_BAM = 32121007; //Buff
    public static final int HEROS_WILL_BAM = 32121008;

    public static final int DARK_AURA_BOSS = 32120060;
    public static final int WEAKENING_AURA_ELEMENTAL = 32120061;
    public static final int BLUE_AURA_DISPEL = 32120062;
    public static final int PARTY_SHIELD_PERSIST = 32120064;
    public static final int PARTY_SHIELD_ENHANCE = 32120065;
    public static final int FOR_LIBERTY_BAM = 32121053;
    public static final int MASTER_OF_DEATH = 32121056;

    private static final int[] AURA_SKILLS = new int[] {
            HASTY_AURA,
            DRAINING_AURA,
            BLUE_AURA,
            DARK_AURA,
            WEAKENING_AURA,
    };

    private AtomicInteger bossAttackCounter = new AtomicInteger(0);
    private ScheduledFuture applyAuraTimer;
    private ScheduledFuture drainingAuraPassiveTimer;
    private long blueAuraDispelCD = Long.MIN_VALUE;
    private long drainingAuraCD = Long.MIN_VALUE;

    public BattleMage(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            applyAuraTimer = EventManager.addFixedRateEvent(this::applyAura, 1000, 1000);
            drainingAuraPassiveTimer = EventManager.addFixedRateEvent(this::drainingAuraPassive, 4000, 4000);
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isBattleMage(id);
    }

    private Summon getDeath() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        for (Option option : tsm.getIndieOptions(IndieEmpty)) {
            if (option.nReason != CONDEMNATION && option.nReason != GRIM_CONTRACT &&
                    option.nReason != GRIM_CONTRACT_II && option.nReason != GRIM_CONTRACT_III) {
                continue;
            }
            return option.summon;
        }
        return null;
    }

    private void applyAura() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(BMageAura)) {
            return;
        }
        int skillId = tsm.getOption(BMageAura).rOption;
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        Rect rect = chr.getRectAround(si.getFirstRect());
        int slv = chr.getSkillLevel(skillId);

        // cancel buff if no more MP
        if (!chr.applyHpMpCon(skillId, slv)) {
            tsm.removeStatsBySkill(skillId);
            tsm.sendResetStatPacket();
            return;
        }

        // handle weakening aura
        if (skillId == WEAKENING_AURA) {
            Option o1 = new Option();
            Option o2 = new Option();
            Option o3 = new Option();
            // get_element_dec
            o1.nOption = 1;
            o1.rOption = WEAKENING_AURA;
            o1.tOption = si.getValue(time, slv);
            o1.cOption = chr.getId();                                           // dwOwnerId
            o1.pOption = chr.getPartyID();                                      // nPartyId
            o1.uOption = chr.getSkillStatValue(x, WEAKENING_AURA_ELEMENTAL);    // nDecRate
            o1.wOption = chr.getParty() == null ? 0 : 1;                        // nOption (in party or not)
            // final damage the enemy receives increased by s%
            o2.nOption = chr.getSkillStatValue(s, WEAKENING_AURA_ELEMENTAL);
            o2.rOption = WEAKENING_AURA;
            o2.tOption = si.getValue(time, slv);
            // enemy DEF -x%
            o3.nOption = si.getValue(x, slv);
            o3.rOption = WEAKENING_AURA;
            o3.tOption = si.getValue(time, slv);
            for (Mob mob : chr.getField().getMobsInRect(rect)) {
                MobTemporaryStat mts = mob.getTemporaryStat();
                if (chr.hasSkill(WEAKENING_AURA_ELEMENTAL)) {
                    mts.addStatOptions(MobStat.TrueSight, o1);
                    mts.addStatOptions(MobStat.AddDamSkill2, o2);
                }
                mts.addStatOptionsAndBroadcast(MobStat.BMageDebuff, o1);
            }
            return;
        }

        // handle Blue Aura dispel
        boolean doDispel = false;
        if (skillId == BLUE_AURA && chr.hasSkill(BLUE_AURA_DISPEL)) {
            // check and set cooldown for dispel
            if (blueAuraDispelCD < Util.getCurrentTimeLong()) {
                blueAuraDispelCD = Util.getCurrentTimeLong() + (chr.getSkillStatValue(time, BLUE_AURA_DISPEL) * 1000L);
                doDispel = true;
            }
        }
        if (doDispel) {
            tsm.removeAllDebuffs();
        }

        // give party buff
        Party party = chr.getParty();
        if (party != null) {
            List<Char> pChrList = party.getPartyMembersInSameField(chr).stream().filter(pc -> rect.hasPositionInside(pc.getPosition())).toList();
            for (Char pChr : pChrList) {
                if (pChr.getHP() > 0) {
                    giveAuraBuff(skillId, 2, pChr);
                    if (doDispel) {
                        pChr.getTemporaryStatManager().removeAllDebuffs();
                    }
                }
            }
        }
    }

    private void drainingAuraPassive() {
        if (chr.hasSkill(DRAINING_AURA) && chr.getHP() > 0) {
            chr.heal(chr.getSkillStatValue(hp, DRAINING_AURA));
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
        boolean hasHitBoss = attackInfo.mobAttackInfo.stream().anyMatch(mai -> {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            return mob != null && mob.isBoss();
        });
        if (hasHitMobs) {
            handleDrainingAura(attackInfo);
            if (si != null && si.getElemAttr().contains("d") &&
                    attackInfo.skillId != CONDEMNATION && attackInfo.skillId != GRIM_CONTRACT &&
                    attackInfo.skillId != GRIM_CONTRACT_II && attackInfo.skillId != GRIM_CONTRACT_III) {
                chr.reduceSkillCoolTime(CONDEMNATION, 500);
            }
            if (hasHitBoss) {
                if (bossAttackCounter.incrementAndGet() % 2 == 0) { // chr.getSkillStatValue(y, CONDEMNATION)
                    incrementCondemnation();
                }
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case DARK_CHAIN:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(hcProp, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case DARK_GENESIS:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void handleDrainingAura(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStatBySkillId(DRAINING_AURA)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(DRAINING_AURA);
        int slv = chr.getSkillLevel(DRAINING_AURA);
        // check and set cooldown
        if (drainingAuraCD > Util.getCurrentTimeLong()) {
            return;
        }
        drainingAuraCD = Util.getCurrentTimeLong() + (si.getValue(subTime, slv) * 1000L);
        // convert damage to hp
        int damage = attackInfo.mobAttackInfo.stream().mapToInt(mai -> Arrays.stream(mai.damages).sum()).sum();
        int damageR = si.getValue(x, slv);
        int healAmount = (int) (damage * ((double) damageR / 100D));
        int healMaxR = 15; // in description
        Field field = chr.getField();
        if (chr.getParty() == null) {
            int healMax = (int) (chr.getMaxHP() * ((double) healMaxR / 100D));
            if (chr.getHP() > 0) {
                chr.heal(Math.min(healMax, healAmount), true);
            }
        } else {
            Rect rect = chr.getRectAround(si.getFirstRect());
            List<PartyMember> partyMembers = field.getPartyMembersInRect(chr, rect).stream()
                    .filter(pml -> pml.getChr().getHP() > 0)
                    .toList();
            int partyHealAmount = healAmount / partyMembers.size();
            for (PartyMember partyMember : partyMembers) {
                Char partyChr = partyMember.getChr();
                int healMax = (int) (partyChr.getMaxHP() * ((double) healMaxR / 100D));
                partyChr.heal(Math.min(healMax, partyHealAmount), true);
            }
        }
    }

    private void incrementCondemnation() {
        int skillId = 0;
        if (chr.hasSkill(GRIM_CONTRACT_III)) {
            skillId = GRIM_CONTRACT_III;
        } else if (chr.hasSkill(GRIM_CONTRACT_II)) {
            skillId = GRIM_CONTRACT_II;
        } else if (chr.hasSkill(GRIM_CONTRACT)) {
            skillId = GRIM_CONTRACT;
        } else if (chr.hasSkill(CONDEMNATION)) {
            skillId = CONDEMNATION;
        }
        if (skillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(BMageDeath)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        int stacks = tsm.getOption(BMageDeath).nOption + 1;
        int maxStacks = tsm.hasStatBySkillId(MASTER_OF_DEATH) ? chr.getSkillStatValue(y, MASTER_OF_DEATH) : si.getValue(x, slv);
        if (stacks >= maxStacks && !chr.hasSkillOnCooldown(CONDEMNATION)) {
            // do attack
            Summon death = getDeath();
            if (death != null) {
                chr.write(Summoned.summonedAssistAttackRequest(death));
                chr.addSkillCooldown(CONDEMNATION, getBuffedSkillCooldown(si.getValue(time, slv) * 1000));
                stacks = 0;
            }
        }
        Option o1 = new Option();
        o1.nOption = Math.min(stacks, 10); // icon number goes up to 10
        o1.rOption = skillId;
        tsm.putCharacterStatValue(BMageDeath, o1);
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleMobDeath(Mob mob) {
        incrementCondemnation();
        // handle Draining Aura passive
        if (chr.hasSkill(DRAINING_AURA)) {
            int healRate = chr.getSkillStatValue(killRecoveryR, DRAINING_AURA);
            chr.heal((int) (chr.getMaxHP() * ((double) healRate / 100D)));
        }
        super.handleMobDeath(mob);
    }

    @Override
    public int getFinalAttackSkill() {
        if (chr.hasSkillOnCooldown(DARK_GENESIS)) {
            SkillInfo si = SkillData.getSkillInfoById(DARK_GENESIS_FA);
            int slv = chr.getSkillLevel(DARK_GENESIS);
            if (Util.succeedProp(si.getValue(prop, slv))) {
                return DARK_GENESIS_FA;
            }
        }
        return super.getFinalAttackSkill();
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
        Summon summon;
        switch (skillID) {
            case PARTY_SHIELD:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(inPacket.decodePosition());
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                aa.setDelay((short) 16);
                chr.getField().spawnAffectedArea(aa);
                break;
            case CONDEMNATION:
            case GRIM_CONTRACT:
            case GRIM_CONTRACT_II:
            case GRIM_CONTRACT_III:
                summon = new Summon(skillID);
                summon.setChr(chr);
                summon.setSkillID(skillID);
                summon.setSlv((byte) slv);
                summon.setCharLevel((byte) chr.getStat(Stat.level));
                summon.setPosition(chr.getPosition().deepCopy());
                summon.setMoveAction((byte) 1);
                summon.setCurFoothold((short) chr.getField().findFootHoldBelow(summon.getPosition()).getId());
                summon.setEnterType(EnterType.Animation);
                summon.setFlyMob(true);
                summon.setSummonTerm(0);
                summon.setMoveAbility(MoveAbility.Walk);
                summon.setAssistType(AssistType.AttackManual);
                summon.setAttackActive(false);
                summon.setBeforeFirstAttack(false);
                chr.getField().spawnSummon(summon);
                o1.nValue = 1;
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = 0;
                o1.summon = summon;
                tsm.putCharacterStatValue(IndieEmpty, o1);
                o2.nOption = 0;
                o2.rOption = skillID;
                o2.tOption = 0;
                tsm.putCharacterStatValue(BMageDeath, o2);
                break;
            case STAFF_BOOST:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case HASTY_AURA:
            case DRAINING_AURA:
            case BLUE_AURA:
            case DARK_AURA:
            case WEAKENING_AURA:
                if (tsm.hasStat(BMageAura)) {
                    for (int auraSkillId : AURA_SKILLS) {
                        tsm.removeStatsBySkill(auraSkillId);
                    }
                    tsm.sendResetStatPacket();
                }
                giveAuraBuff(skillID, 0, chr); // permanent buff for caster
                applyAura();
                break;
            case DARK_SHOCK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(DarkLighting, o1);
                break;
            case BATTLE_RAGE:
                o1.nOption = 100 * si.getValue(x, slv) + si.getValue(mobCount, slv); // damR = n / 100, mobsHit = n % 100 (for Mages)
                o1.rOption = skillID;
                tsm.putCharacterStatValue(Enrage, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                tsm.putCharacterStatValue(EnrageCrDam, o2);
                o3.nOption = si.getValue(z, slv);
                o3.rOption = skillID;
                tsm.putCharacterStatValue(CriticalBuff, o3);
                break;
            case MASTER_OF_DEATH:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AttackCountX, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void giveAuraBuff(int skillId, int duration, Char target) {
        TemporaryStatManager tsm = target.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = this.chr.getSkillLevel(skillId);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        o1.nOption = skillId == BLUE_AURA ? si.getValue(y, slv) : 1; // dmgReduce handled by BMageAura CTS
        o1.rOption = skillId;
        o1.tOption = duration;
        o1.bOption = chr == target ? 1 : 0;
        tsm.putCharacterStatValue(BMageAura, o1);
        if (skillId == HASTY_AURA) {
            o2.nReason = skillId;
            o2.nValue = si.getValue(indieSpeed, slv);
            o2.tStart = Util.getCurrentTime();
            o2.tTerm = duration;
            tsm.putCharacterStatValue(IndieSpeed, o2);
            o3.nReason = skillId;
            o3.nValue = si.getValue(indieBooster, slv);
            o3.tStart = Util.getCurrentTime();
            o3.tTerm = duration;
            tsm.putCharacterStatValue(IndieBooster, o3);
        } else if (skillId == BLUE_AURA) {
            o2.nReason = skillId;
            o2.nValue = si.getValue(indieAsrR, slv);
            o2.tStart = Util.getCurrentTime();
            o2.tTerm = duration;
            tsm.putCharacterStatValue(IndieIDK, o2);
        } else if (skillId == DARK_AURA) {
            o2.nReason = skillId;
            o2.nValue = si.getValue(indieDamR, slv);
            o2.tStart = Util.getCurrentTime();
            o2.tTerm = duration;
            tsm.putCharacterStatValue(IndieAsrR, o2);
            if (this.chr.hasSkill(DARK_AURA_BOSS)) {
                o3.nReason = skillId;
                o3.nValue = this.chr.getSkillStatValue(indieBDR, DARK_AURA_BOSS);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = duration;
                tsm.putCharacterStatValue(IndieBDR, o3);
            }
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleCancelTimer() {
        if (applyAuraTimer != null && !applyAuraTimer.isDone()) {
            applyAuraTimer.cancel(true);
        }
        if (drainingAuraPassiveTimer != null && !drainingAuraPassiveTimer.isDone()) {
            drainingAuraPassiveTimer.cancel(true);
        }
        super.handleCancelTimer();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }
}