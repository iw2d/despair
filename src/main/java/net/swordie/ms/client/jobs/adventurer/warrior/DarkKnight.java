package net.swordie.ms.client.jobs.adventurer.warrior;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;

import java.util.Arrays;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class DarkKnight extends Warrior {
    public static final int WEAPON_MASTERY_SPEARMAN = 1300000;
    public static final int FINAL_ATTACK_SPEARMAN = 1300002;
    public static final int WEAPON_BOOSTER_SPEARMAN = 1301004;
    public static final int IRON_WILL = 1301006;
    public static final int HYPER_BODY = 1301007;
    public static final int SPEAR_SWEEP = 1301012;
    public static final int EVIL_EYE = 1301013;
    public static final int EVIL_EYE_OF_DOMINATION = 1311013;
    public static final int EVIL_EYE_SHOCK = 1311014;
    public static final int CROSS_SURGE = 1311015;
    public static final int HEX_OF_THE_EVIL_EYE = 1310016;
    public static final int LORD_OF_DARKNESS = 1310009;
    public static final int ENDURE = 1310010;
    public static final int MAPLE_WARRIOR_DARK_KNIGHT = 1321000;
    public static final int REVENGE_OF_THE_EVIL_EYE = 1320011;
    public static final int BARRICADE_MASTERY = 1320018;
    public static final int FINAL_PACT_INFO = 1320016;
    public static final int FINAL_PACT = 1320019;
    public static final int MAGIC_CRASH_DRK = 1321014;
    public static final int SACRIFICE = 1321015; //Resets summon
    public static final int HEROS_WILL_DRK = 1321010;
    public static final int GUNGNIR_DESCENT = 1321013;

    public static final int EPIC_ADVENTURE_DRK = 1321053; //Lv200
    public static final int DARK_THIRST = 1321054; //Lv150
    public static final int HYPER_BODY_PERSIST = 1320043;
    public static final int HYPER_BODY_SPIRIT = 1320044;
    public static final int HYPER_BODY_VITALITY = 1320045;
    public static final int FINAL_PACT_DAMAGE = 1320046;
    public static final int FINAL_PACT_COOLDOWN = 1320047;
    public static final int FINAL_PACT_CRITICAL = 1320048;

    private long evilEyeEnd = Long.MIN_VALUE;
    private long evilEyeRevenge = Long.MIN_VALUE;

    public DarkKnight(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isDarkKnight(id);
    }

    private Summon getEvilEye() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        for (Option option : tsm.getIndieOptions(IndieEmpty)) {
            if (option.nReason != EVIL_EYE) {
                continue;
            }
            return option.summon;
        }
        return null;
    }

    private void spawnEvilEye(boolean refresh) {
        removeEvilEye();

        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(EVIL_EYE);
        int summonSlv = chr.getSkillLevel(EVIL_EYE);
        int summonTerm = getBuffedSummonDuration(si.getValue(SkillStat.time, summonSlv));

        long remaining = evilEyeEnd - Util.getCurrentTimeLong();
        if (!refresh && remaining > 0) {
            summonTerm = (int) (remaining / 1000);
        }

        Summon summon = new Summon(EVIL_EYE);
        summon.setChr(chr);
        summon.setSkillID(EVIL_EYE);
        summon.setSlv((byte) summonSlv);
        summon.setSummonTerm(summonTerm);
        summon.setCharLevel((byte) chr.getStat(Stat.level));
        summon.setPosition(chr.getPosition().deepCopy());
        summon.setMoveAction((byte) 1);
        summon.setCurFoothold((short) chr.getField().findFootholdBelow(summon.getPosition()).getId());
        summon.setFlyMob(true);
        summon.setMoveAbility(MoveAbility.Fly);
        summon.setAssistType(AssistType.Heal);
        summon.setEnterType(EnterType.Animation);
        summon.setBeforeFirstAttack(false);
        summon.setTemplateId(EVIL_EYE);
        summon.setAttackActive(true);
        chr.getField().spawnSummon(summon);

        evilEyeEnd = Util.getCurrentTimeLong() + (summonTerm * 1000L);

        Option o1 = new Option();
        Option o2 = new Option();
        o1.nValue = 1;
        o1.nReason = EVIL_EYE;
        o1.tStart = Util.getCurrentTime();
        o1.tTerm = summonTerm;
        o1.summon = summon;
        o1.setInMillis(true);
        tsm.putCharacterStatValue(IndieEmpty, o1, true);
        o2.nOption = 1;
        o2.rOption = EVIL_EYE;
        o2.tOption = summonTerm;
        o2.sOption = 0;
        o2.ssOption = 0;
        o2.setInMillis(true);
        tsm.putCharacterStatValue(Beholder, o2, true);
        tsm.sendSetStatPacket();
    }

    public void updateEvilEye(int skillID) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        o1.nReason = skillID; // not sent, used as ID
        o1.sOption = skillID == EVIL_EYE_OF_DOMINATION ? skillID : 0;
        o1.ssOption = skillID == EVIL_EYE_SHOCK ? skillID : 0;
        tsm.putCharacterStatValue(Beholder, o1);
        tsm.sendSetStatPacket();
    }

    public void removeEvilEye() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(Beholder)) {
            tsm.removeStatsBySkill(EVIL_EYE);
            tsm.sendResetStatPacket();
        }
    }

    public static void healByEvilEye(Char owner) {
        if (owner.getHP() > 0) {
            SkillInfo si = SkillData.getSkillInfoById(EVIL_EYE);
            int slv = owner.getSkillLevel(EVIL_EYE);
            owner.heal(si.getValue(hp, slv), true);
        }
    }

    public static void giveHexOfTheEvilEyeBuffs(Char owner) {
        TemporaryStatManager tsm = owner.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(HEX_OF_THE_EVIL_EYE);
        int slv = owner.getSkillLevel(HEX_OF_THE_EVIL_EYE);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        o1.nOption = si.getValue(epad, slv);
        o1.rOption = HEX_OF_THE_EVIL_EYE;
        o1.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(EPAD, o1);

        o2.nOption = si.getValue(epdd, slv);
        o2.rOption = HEX_OF_THE_EVIL_EYE;
        o2.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(EDEF, o2);

        o3.nReason = HEX_OF_THE_EVIL_EYE;
        o3.nValue = si.getValue(indieCr, slv);
        o3.tStart = Util.getCurrentTime();
        o3.tTerm = si.getValue(time, slv);
        tsm.putCharacterStatValue(IndieCr, o3);

        o4.nOption = si.getValue(acc, slv);
        o4.rOption = HEX_OF_THE_EVIL_EYE;
        o4.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(ACC, o4);
        tsm.putCharacterStatValue(EVA, o4);
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
        if (hasHitMobs) {
            killCountFinalPactOnMob(attackInfo);
            handleLordOfDarkness();
            handleDarkThirst();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case SPEAR_SWEEP:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = GameConstants.DEFAULT_STUN_DURATION;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (!mob.isBoss()) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case EVIL_EYE:
                if (tsm.hasStat(Beholder) && tsm.getOption(Beholder).ssOption == EVIL_EYE_SHOCK) {
                    skillID = EVIL_EYE_SHOCK;
                    si = SkillData.getSkillInfoById(EVIL_EYE_SHOCK);
                    slv = chr.getSkillLevel(EVIL_EYE_SHOCK);

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
                    EventManager.addEvent(() -> spawnEvilEye(false), 1500);
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    public void handleLordOfDarkness() {
        if (chr.hasSkill(LORD_OF_DARKNESS) && chr.getHP() > 0) {
            SkillInfo si = SkillData.getSkillInfoById(LORD_OF_DARKNESS);
            int slv = chr.getSkillLevel(LORD_OF_DARKNESS);
            int proc = si.getValue(prop, slv);
            if (Util.succeedProp(proc)) {
                int heal = si.getValue(x, slv);
                chr.heal((int) (chr.getMaxHP() / ((double) 100 / heal)));
            }
        }
    }

    private void handleDarkThirst() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStatBySkillId(DARK_THIRST) && chr.getHP() > 0) {
            int heal = chr.getSkillStatValue(x, DARK_THIRST);
            chr.heal((int) (chr.getMaxHP() / ((double) 100 / heal)));
        }
    }

    @Override
    public int getFinalAttackSkill() {
        if (chr.hasSkill(FINAL_ATTACK_SPEARMAN)) {
            SkillInfo si = SkillData.getSkillInfoById(FINAL_ATTACK_SPEARMAN);
            int slv = chr.getSkillLevel(FINAL_ATTACK_SPEARMAN);
            int proc = si.getValue(prop, slv);
            if (Util.succeedProp(proc)) {
                return FINAL_ATTACK_SPEARMAN;
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
            case IRON_WILL:
                o1.nOption = si.getValue(pdd, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DEF, o1);
                break;
            case HYPER_BODY:
                o1.nOption = si.getValue(x, slv) + this.chr.getSkillStatValue(x, HYPER_BODY_VITALITY);;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) + this.chr.getSkillStatValue(time, HYPER_BODY_PERSIST);
                tsm.putCharacterStatValue(MaxHP, o1);
                o2.nOption = si.getValue(y, slv) + this.chr.getSkillStatValue(y, HYPER_BODY_SPIRIT);;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv) + this.chr.getSkillStatValue(time, HYPER_BODY_PERSIST);
                tsm.putCharacterStatValue(MaxMP, o2);
                break;
            case CROSS_SURGE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.xOption = si.getValue(z, slv);
                tsm.putCharacterStatValue(CrossOverChain, o1);
                break;
            case EVIL_EYE:
                spawnEvilEye(true);
                break;
            case EVIL_EYE_OF_DOMINATION:
                if (tsm.hasStatBySkillId(EVIL_EYE_OF_DOMINATION)) {
                    tsm.removeStatsBySkill(EVIL_EYE_OF_DOMINATION);
                    tsm.sendResetStatPacket();
                    spawnEvilEye(false);
                } else {
                    updateEvilEye(EVIL_EYE_OF_DOMINATION);
                }
                break;
            case EVIL_EYE_SHOCK:
                updateEvilEye(EVIL_EYE_SHOCK);
                break;
            case SACRIFICE:
                if (tsm.hasStat(Beholder)) {
                    o1.nReason = skillID;
                    o1.nValue = si.getValue(x, slv);
                    o1.tStart = Util.getCurrentTime();
                    o1.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o1);

                    o2.nReason = skillID;
                    o2.nValue = si.getValue(indieBDR, slv);
                    o2.tStart = Util.getCurrentTime();
                    o2.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieBDR, o2);
                    if (chr.getHP() > 0) {
                        chr.heal((int) (chr.getMaxHP() / ((double) 100 / si.getValue(y, slv))));
                    }
                    removeEvilEye();
                }
                break;
            case DARK_THIRST:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public int alterCooldownSkill(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        switch (skillId) {
            case GUNGNIR_DESCENT:
                if (tsm.hasStatBySkillId(SACRIFICE) || tsm.hasStatBySkillId(FINAL_PACT)) {
                    return 0;
                }
        }
        return super.alterCooldownSkill(skillId);
    }

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (cts == Reincarnation) {
            if (tsm.getOption(Reincarnation).xOption > 0) {
                chr.setStatAndSendPacket(Stat.hp, 0);
                chr.write(UserLocal.openUIOnDead(true, chr.getBuffProtectorItem() != null,
                        false, false, false,
                        ReviveType.NORMAL.getVal(), 0));
            }
        }
        super.handleRemoveCTS(cts);
    }

    @Override
    public void handleCalcDamageStatSet() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(FINAL_PACT_INFO)) {
            SkillInfo si = SkillData.getSkillInfoById(FINAL_PACT_INFO);
            int slv = chr.getSkillLevel(FINAL_PACT_INFO);
            int currentRatio = (int) ((double) chr.getHP() / chr.getMaxHP() * 100D);
            int targetRatio = si.getValue(x, slv);
            int damR = si.getValue(damage, slv);
            if (tsm.hasStat(Reincarnation) && chr.hasSkill(FINAL_PACT_DAMAGE)) {
                damR += chr.getSkillStatValue(damage, FINAL_PACT_DAMAGE);
            }
            if (currentRatio < targetRatio || damR == 0) {
                if (tsm.hasStatBySkillId(FINAL_PACT_INFO)) {
                    tsm.removeStatsBySkill(FINAL_PACT_INFO);
                    tsm.sendResetStatPacket();
                }
            } else if (!tsm.hasStatBySkillId(FINAL_PACT_INFO) || tsm.getOptByCTSAndSkill(IndieAsrR, FINAL_PACT_INFO).nValue != damR) {
                Option o1 = new Option();
                o1.nValue = damR;
                o1.nReason = FINAL_PACT_INFO;
                o1.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieAsrR, o1);
            }
        } else if (tsm.hasStatBySkillId(FINAL_PACT_INFO)) {
            tsm.removeStatsBySkill(FINAL_PACT_INFO);
            tsm.sendResetStatPacket();
        }
    }

    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(REVENGE_OF_THE_EVIL_EYE)) {
            Skill skill = chr.getSkill(REVENGE_OF_THE_EVIL_EYE);
            int slv = skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int proc = si.getValue(prop, slv);
            int heal = si.getValue(x, slv);
            if (chr.hasSkill(EVIL_EYE) && tsm.hasStatBySkillId(EVIL_EYE) && chr.getHP() > 0) {
                if (evilEyeRevenge < Util.getCurrentTimeLong()) {
                    Summon evilEye = getEvilEye();
                    if (evilEye != null && Util.succeedProp(proc)) {
                        chr.write(Summoned.summonBeholderRevengeAttack(evilEye, hitInfo.mobID));
                        chr.heal((int) (chr.getMaxHP() / ((double) 100 / heal)), true);
                        evilEyeRevenge = Util.getCurrentTimeLong() + (si.getValue(cooltime, slv) * 1000L);
                    }
                }
            }
        }
        super.handleHit(chr, hitInfo);
    }

    public void reviveByFinalPact() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(FINAL_PACT_INFO);
        int slv = chr.getSkillLevel(FINAL_PACT_INFO);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();

        chr.heal(chr.getMaxHP());
        chr.healMP(chr.getMaxMP());

        o1.nOption = 1;
        o1.rOption = FINAL_PACT;
        o1.tOption = si.getValue(time, slv);
        o1.xOption = si.getValue(z, slv);
        tsm.putCharacterStatValue(Reincarnation, o1);
        o2.nOption = 1;
        o2.rOption = FINAL_PACT;
        o2.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(NotDamaged, o2);
        tsm.sendSetStatPacket();

        chr.setSkillCooldown(FINAL_PACT_INFO, slv);
        chr.resetSkillCoolTime(GUNGNIR_DESCENT);
        chr.write(UserPacket.effect(Effect.showFinalPactEffect(FINAL_PACT, (byte) 1, 0, true))); // Manually broadcasting Effect packet, as FINAL PACT isn't actually ever called.
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.showFinalPactEffect(FINAL_PACT, (byte) 1, 0, true)));
    }

    private void lowerFinalPactKillCount() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(FINAL_PACT_INFO);
        if (skill == null || !tsm.hasStat(Reincarnation)) {
            return;
        }
        Option oldOption = tsm.getOption(Reincarnation);
        if (oldOption.xOption > 0) {
            oldOption.xOption--;
            if (oldOption.xOption > 0) {
                tsm.putCharacterStatValue(Reincarnation, oldOption);
                tsm.sendSetStatPacket();
            } else {
                tsm.removeStatsBySkill(FINAL_PACT);
                tsm.sendResetStatPacket();
            }
        }
    }

    private void killCountFinalPactOnMob(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(Reincarnation)) {
            return;
        }
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob != null) {
                if (mob.isBoss()) {
                    lowerFinalPactKillCount();
                } else {
                    long totaldmg = Arrays.stream(mai.damages).sum();
                    if (totaldmg >= mob.getHp()) {
                        lowerFinalPactKillCount();
                    }
                }
            }
        }
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

