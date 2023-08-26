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
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.UserLocal;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Mihile extends Job {
    public static final int IMPERIAL_RECALL = 50001245;

    public static final int ROYAL_GUARD_BUFF = 51001005;
    public static final int ROYAL_GUARD_1 = 51001006;
    public static final int ROYAL_GUARD_2 = 51001007;
    public static final int ROYAL_GUARD_3 = 51001008;
    public static final int ROYAL_GUARD_4 = 51001009;
    public static final int ROYAL_GUARD_5 = 51001010;

    public static final int SWORD_BOOSTER = 51101003; //Buff
    public static final int RALLY = 51101004; //Buff

    public static final int SELF_RECOVERY = 51110000;
    public static final int ENDURING_SPIRIT = 51111004; //Buff
    public static final int SOUL_LINK = 51111008; //Special Buff (ON/OFF)
    public static final int SOUL_LINK_HIDE = SOUL_LINK + 100; // Hide buff icon
    public static final int MAGIC_CRASH = 51111005; //Special Skill (Debuff Mobs)
    public static final int ADVANCED_ROYAL_GUARD = 51110009; //Upgrade on Royal Guard

    public static final int SOUL_ASYLUM = 51120003;
    public static final int ROILING_SOUL = 51121006; //Buff (ON/OFF)
    public static final int FOUR_POINT_ASSAULT = 51121007; //Special Attack (Accuracy Debuff)
    public static final int RADIANT_CROSS = 51121009; //Special Attack (Accuracy Debuff)    Creates an Area of Effect
    public static final int CALL_OF_CYGNUS_MIHILE = 51121005; //Buff

    public static final int FINAL_ATTACK_MIHILE = 51100002;
    public static final int ADVANCED_FINAL_ATTACK_MIHILE = 51120002;

    public static final int ENDURING_SPIRIT_PERSIST = 51120043;
    public static final int ENDURING_SPIRIT_STEEL_SKIN = 51120043;
    public static final int ENDURING_SPIRIT_PREPARATION = 51120043;
    public static final int FOUR_POINT_ASSAULT_OPPORTUNITY = 51120050;
    public static final int RADIANT_CROSS_SPREAD = 51120057;
    public static final int CHARGING_LIGHT = 51121052;
    public static final int QUEEN_OF_TOMORROW = 51121053;
    public static final int SACRED_CUBE = 51121054;

    private ScheduledFuture selfRecoveryTimer;
    private ScheduledFuture soulLinkTimer;

    public Mihile(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            selfRecoveryTimer = EventManager.addFixedRateEvent(this::selfRecovery, 4000, 4000);
            soulLinkTimer = EventManager.addFixedRateEvent(this::soulLink, 1000, 1000);
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return id == JobConstants.JobEnum.NAMELESS_WARDEN.getJobId() || JobConstants.isMihile(id);
    }

    private void selfRecovery() {
        if (chr.hasSkill(SELF_RECOVERY) && chr.getHP() > 0) {
            chr.heal(chr.getSkillStatValue(hp, SELF_RECOVERY));
            chr.healMP(chr.getSkillStatValue(mp, SELF_RECOVERY));
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(SOUL_LINK) && tsm.hasStat(MichaelStanceLink) && chr.getHP() > 0) {
            int healRate = (int) (chr.getMaxHP() / 100D / chr.getSkillStatValue(s, SOUL_LINK));
            chr.heal(healRate);
        }
    }

    private void soulLink() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!chr.hasSkill(SOUL_LINK) || !tsm.hasStat(MichaelSoulLink)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(SOUL_LINK);
        int slv = chr.getSkillLevel(SOUL_LINK);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        // handle party buff
        int count = 1;
        Party party = chr.getParty();
        if (party != null) {
            Rect rect = chr.getRectAround(si.getFirstRect());
            List<Char> pChrList = party.getPartyMembersInSameField(chr).stream().filter(pc -> rect.hasPositionInside(pc.getPosition())).toList();
            for (Char ptChr : pChrList) {
                if (ptChr.getHP() > 0) {
                    TemporaryStatManager ptTsm = ptChr.getTemporaryStatManager();
                    o1.nOption = 1;
                    o1.rOption = SOUL_LINK;
                    o1.tOption = 2;
                    o1.bOption = 1;
                    o1.cOption = chr.getId();
                    o1.yOption = si.getValue(q, slv);
                    ptTsm.putCharacterStatValue(MichaelSoulLink, o1);
                    if (tsm.hasStatBySkillId(ROYAL_GUARD_BUFF)) {
                        o2.nValue = (int) (tsm.getOptByCTSAndSkill(IndiePAD, ROYAL_GUARD_BUFF).nValue * (si.getValue(x, slv) / 100D));
                        o2.nReason = SOUL_LINK;
                        o2.tStart = Util.getCurrentTime();
                        o2.tTerm = 2;
                        ptTsm.putCharacterStatValue(IndiePAD, o2);
                    }
                    if (tsm.hasStatBySkillId(ENDURING_SPIRIT)) {
                        o3.nValue = si.getValue(y, slv);
                        o3.nReason = SOUL_LINK;
                        o3.tStart = Util.getCurrentTime();
                        o3.tTerm = 2;
                        ptTsm.putCharacterStatValue(IndieAsrR, o3);
                        o4.nValue = si.getValue(w, slv);
                        o4.nReason = SOUL_LINK;
                        o4.tStart = Util.getCurrentTime();
                        o4.tTerm = 2;
                        ptTsm.putCharacterStatValue(IndiePADR, o4);
                        ptTsm.putCharacterStatValue(IndieMADR, o4);
                    }
                    ptTsm.sendSetStatPacket();
                }
            }
            count = pChrList.size();
        }
        // handle self buff
        Option o5 = new Option();
        o5.nValue = si.getValue(indieDamR, slv) * count;
        o5.nReason = SOUL_LINK_HIDE;
        o5.tStart = Util.getCurrentTime();
        o5.tTerm = 0;
        tsm.putCharacterStatValue(IndieAsrR, o5);
        tsm.sendSetStatPacket();
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
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case FOUR_POINT_ASSAULT:
                int proc = si.getValue(prop, slv) + chr.getSkillStatValue(prop, FOUR_POINT_ASSAULT_OPPORTUNITY);
                o1.nOption = -si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) / 2;
                o1.nOption = -si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || !Util.succeedProp(proc)) {
                        continue;
                    }
                    if (mob.isBoss()) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.ACC, o1);
                    } else {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.ACC, o2);
                    }
                }
                break;
            case RADIANT_CROSS:
                o1.nOption = -si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) / 2;
                o1.nOption = -si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || !Util.succeedProp(si.getValue(prop, slv))) {
                        continue;
                    }
                    if (mob.isBoss()) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.ACC, o1);
                    } else {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.ACC, o2);
                    }
                }
                break;
            case RADIANT_CROSS_SPREAD:
                int duration = si.getValue(time, slv);
                o1.nOption = -si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = duration / 2;
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = duration / 2;
                o3.nOption = -si.getValue(x, slv);
                o3.rOption = skillID;
                o3.tOption = duration;
                o4.nOption = 1;
                o4.rOption = skillID;
                o4.tOption = duration;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) { // || !Util.succeedProp(si.getValue(z, slv))) { // 100%
                        continue;
                    }
                    if (mob.isBoss()) {
                        mob.getTemporaryStat().addStatOptions(MobStat.ACC, o1);
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Blind, o2);
                    } else {
                        mob.getTemporaryStat().addStatOptions(MobStat.ACC, o3);
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Blind, o4);
                    }
                }
                break;
            case CHARGING_LIGHT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.pOption = chr.getPartyID();
                o1.wOption = chr.getId();
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.DeadlyCharge, o1);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }


    @Override
    public int getFinalAttackSkill() {
        int skillId = 0;
        if (chr.hasSkill(ADVANCED_FINAL_ATTACK_MIHILE)) {
            skillId = ADVANCED_FINAL_ATTACK_MIHILE;
        } else if (chr.hasSkill(FINAL_ATTACK_MIHILE)) {
            skillId = FINAL_ATTACK_MIHILE;
        }
        if (skillId > 0) {
            SkillInfo si = SkillData.getSkillInfoById(skillId);
            int slv = chr.getSkillLevel(skillId);
            int proc = si.getValue(prop, slv);
            if (Util.succeedProp(proc)) {
                return skillId;
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
        switch (skillID) {
            case ROYAL_GUARD_BUFF:
                if (ServerConstants.CLIENT_SIDED_SKILL_HOOK) {
                    // hook CUserLocal::IsRoyalGuardSuccess to send USER_SKILL_USE_REQUEST packet
                    royalGuardBuff();
                    tsm.removeStat(RoyalGuardPrepare, false);
                    tsm.sendResetStatPacket();
                }
                break;
            case ROYAL_GUARD_1:
            case ROYAL_GUARD_2:
            case ROYAL_GUARD_3:
            case ROYAL_GUARD_4:
            case ROYAL_GUARD_5:
                if (ServerConstants.CLIENT_SIDED_SKILL_HOOK) {
                    chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(skillID, (byte) slv, 0)), chr);
                    // cooldown handled by client hook
                } else {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(x, slv) + (tsm.hasStatBySkillId(SACRED_CUBE) ? chr.getSkillStatValue(y, SACRED_CUBE) : 0);
                    o1.setInMillis(true);
                    tsm.putCharacterStatValue(RoyalGuardPrepare, o1);
                    chr.write(UserPacket.effect(Effect.skillUse(skillID, (byte) slv, 0)));
                    chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(skillID, (byte) slv, 0)), chr);
                    chr.setSkillCooldown(ROYAL_GUARD_BUFF, slv);
                }
                break;
            case MAGIC_CRASH:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                Position pos = inPacket.decodePosition();
                Rect rect = pos.getRectAround(si.getFirstRect());
                if (!chr.isLeft()) {
                    rect = rect.horizontalFlipAround(pos.getX());
                }
                for (Life life : chr.getField().getLifesInRect(rect)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (Util.succeedProp(si.getValue(prop, slv))) {
                            mts.removeBuffs();
                            mts.addStatOptionsAndBroadcast(MobStat.MagicCrash, o1);
                        }
                    }
                }
                break;
            case SWORD_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case RALLY:
                o1.nValue = si.getValue(indiePad, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                break;
            case ENDURING_SPIRIT: // PDDR(DEF%) = x  |  AsrR & TerR = y & z
                int duration = si.getValue(time, slv) + this.chr.getSkillStatValue(time, ENDURING_SPIRIT_PERSIST);
                o1.nValue = si.getValue(indiePddR, slv) + this.chr.getSkillStatValue(x, ENDURING_SPIRIT_STEEL_SKIN);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = duration;
                tsm.putCharacterStatValue(IndieDEFR, o1);
                o2.nValue = si.getValue(y, slv) + this.chr.getSkillStatValue(y, ENDURING_SPIRIT_PREPARATION);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = duration;
                tsm.putCharacterStatValue(AsrR, o2);
                o3.nValue = si.getValue(z, slv) + this.chr.getSkillStatValue(z, ENDURING_SPIRIT_PREPARATION);
                o3.nReason = skillID;
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = duration;
                tsm.putCharacterStatValue(TerR, o3);
                break;
            case SOUL_LINK:
                if (tsm.hasStatBySkillId(SOUL_LINK)) {
                    tsm.removeStatsBySkill(SOUL_LINK);
                    tsm.removeStatsBySkill(SOUL_LINK_HIDE);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = 1;
                    o1.rOption = SOUL_LINK;
                    o1.cOption = chr.getId();
                    tsm.putCharacterStatValue(MichaelSoulLink, o1);
                    soulLink();
                }
                break;
            case RADIANT_CROSS_SPREAD:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(inPacket.decodePosition());
                inPacket.decodeShort(); // unk
                inPacket.decodeByte(); // unk
                boolean flipped = inPacket.decodeByte() == 0;
                rect = aa.getPosition().getRectAround(si.getFirstRect());
                if (flipped) {
                    rect = rect.horizontalFlipAround(aa.getPosition().getX());
                }
                aa.setRect(rect);
                aa.setFlip(flipped);
                chr.getField().spawnAffectedAreaAndRemoveOld(aa);
                break;
            case ROILING_SOUL:
                o1.nOption = 100 * si.getValue(x, slv) + si.getValue(mobCount, slv); // fd = n / 100, mobsHit = n % 100
                o1.rOption = skillID;
                tsm.putCharacterStatValue(Enrage, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                tsm.putCharacterStatValue(EnrageCrDam, o2);
                break;
            case SACRED_CUBE:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMhpR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o2);
                o3.nOption = si.getValue(x, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamageReduce, o3);
                break;
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        if (cts == MichaelSoulLink) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            tsm.removeStatsBySkill(SOUL_LINK_HIDE);
        }
        super.handleRemoveCTS(cts);
    }

    @Override
    public void handleCancelTimer() {
        if (selfRecoveryTimer != null && !selfRecoveryTimer.isDone()) {
            selfRecoveryTimer.cancel(true);
        }
        if (soulLinkTimer != null && !soulLinkTimer.isDone()) {
            soulLinkTimer.cancel(true);
        }
        super.handleCancelTimer();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        if (ServerConstants.CLIENT_SIDED_SKILL_HOOK) {
            // do nothing - buff handled in handleSkill
        } else {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            if (tsm.hasStat(RoyalGuardPrepare)) {
                chr.write(UserLocal.royalGuardAttack(true));
                royalGuardBuff();
                tsm.removeStat(RoyalGuardPrepare, false);
                tsm.sendResetStatPacket();
            }
        }
        super.handleHit(chr, hitInfo);
    }

    private void royalGuardBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(ROYAL_GUARD_BUFF);
        int slv = chr.getSkillLevel(ROYAL_GUARD_BUFF);
        int maxStacks = chr.hasSkill(ADVANCED_ROYAL_GUARD) ? 5 : 3;
        int stacks = Math.min(tsm.getOption(RoyalGuardState).nOption + 1, maxStacks);
        int padAmount;
        if (stacks == 1) {
            padAmount = si.getValue(dotTime, slv);
        } else if (stacks == 2) {
            padAmount = si.getValue(dotInterval, slv);
        } else if (stacks == 3) {
            padAmount = si.getValue(range, slv);
        } else if (stacks == 4) {
            padAmount = chr.getSkillStatValue(w2, ADVANCED_ROYAL_GUARD);
        } else {
            padAmount = chr.getSkillStatValue(u, ADVANCED_ROYAL_GUARD);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        o1.nOption = 1;
        o1.rOption = ROYAL_GUARD_BUFF;
        o1.tOption = si.getValue(dot, slv);
        tsm.putCharacterStatValue(NotDamaged, o1);
        o2.nOption = stacks;
        o2.xOption = stacks;
        o2.rOption = ROYAL_GUARD_BUFF;
        o2.tOption = si.getValue(x, slv);
        tsm.putCharacterStatValue(RoyalGuardState, o2);
        o3.nValue = padAmount;
        o3.nReason = ROYAL_GUARD_BUFF;
        o3.tStart = Util.getCurrentTime();
        o3.tTerm = si.getValue(x, slv);
        tsm.putCharacterStatValue(IndiePAD, o3);
        tsm.sendSetStatPacket();
    }

    public static void handleSoulLink(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(MichaelSoulLink)) {
            return;
        }
        Option slOption = tsm.getOption(MichaelSoulLink);
        if (slOption.cOption == chr.getId()) { // no effect for caster
            return;
        }
        Party party = chr.getParty();
        Char mihileChr = chr.getField().getCharByID(slOption.cOption);
        if (party != null && mihileChr != null && party.isPartyMember(mihileChr)) {
            int hpDmg = hitInfo.hpDamage;
            int mihileDmg = (int) (hpDmg * (slOption.yOption / 100D));

            hitInfo.hpDamage = hpDmg - mihileDmg;
            if (!mihileChr.getTemporaryStatManager().hasStat(NotDamaged)) {
                mihileChr.heal(-mihileDmg);
            }
        } else {
            tsm.removeStatsBySkill(SOUL_LINK);
            tsm.sendResetStatPacket();
        }
    }
    
    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        chr.getAvatarData().getCharacterStat().setPosMap(913070000);
    }
}
