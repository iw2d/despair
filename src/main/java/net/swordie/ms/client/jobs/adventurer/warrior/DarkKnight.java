package net.swordie.ms.client.jobs.adventurer.warrior;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.Arrays;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class DarkKnight extends Warrior {
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
    public static final int MAPLE_WARRIOR_DARK_KNIGHT = 1321000;
    public static final int REVENGE_OF_THE_EVIL_EYE = 1320012;
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

    private int[] addedSkills = new int[]{
            MAPLE_RETURN,
    };

    private Summon evilEye;
    private long revengeEvilEye = Long.MIN_VALUE;
    private long finishFinalPact;
    private int killCount;

    public DarkKnight(Char chr) {
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
        return JobConstants.isDarkKnight(id);
    }

    public void spawnEvilEye(int skillID, int slv) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(skillID);

        Field field;
        evilEye = Summon.getSummonBy(c.getChr(), skillID, slv);
        field = c.getChr().getField();
        evilEye.setFlyMob(true);
        evilEye.setMoveAbility(MoveAbility.Fly);
        evilEye.setAssistType(AssistType.Heal);
        evilEye.setAttackActive(true);
        field.spawnSummon(evilEye);

        o1.nReason = skillID;
        o1.nValue = 1;
        o1.summon = evilEye;
        o1.tStart = (int) System.currentTimeMillis();
        o1.tTerm = si.getValue(time, slv);
        tsm.putCharacterStatValue(IndieEmpty, o1);
        tsm.sendSetStatPacket();
    }

    public void removeEvilEye(TemporaryStatManager tsm, Client c) {
        tsm.removeStatsBySkill(EVIL_EYE);
        tsm.sendResetStatPacket();
        c.getChr().getField().broadcastPacket(Summoned.summonedRemoved(evilEye, LeaveType.ANIMATION));
    }

    public void healByEvilEye() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(EVIL_EYE) && tsm.hasStatBySkillId(EVIL_EYE)) {
            Skill skill = chr.getSkill(EVIL_EYE);
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            byte slv = (byte) skill.getCurrentLevel();
            chr.heal(si.getValue(hp, slv));
        }
    }

    public void giveHexOfTheEvilEyeBuffs() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Skill skill = chr.getSkill(HEX_OF_THE_EVIL_EYE);
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        if (tsm.getOptByCTSAndSkill(EPDD, HEX_OF_THE_EVIL_EYE) == null) {
            o1.nOption = si.getValue(epad, slv);
            o1.rOption = skill.getSkillId();
            o1.tOption = si.getValue(time, slv);
            tsm.putCharacterStatValue(EPAD, o1);

            o2.nOption = si.getValue(epdd, slv);
            o2.rOption = skill.getSkillId();
            o2.tOption = si.getValue(time, slv);
            tsm.putCharacterStatValue(EPDD, o2);
            tsm.putCharacterStatValue(EMDD, o2);

            o3.nReason = skill.getSkillId();
            o3.nValue = si.getValue(indieCr, slv);
            o3.tStart = (int) System.currentTimeMillis();
            o3.tTerm = si.getValue(time, slv);
            tsm.putCharacterStatValue(IndieCr, o3);

            o4.nOption = si.getValue(acc, slv);
            o4.rOption = skill.getSkillId();
            o4.tOption = si.getValue(time, slv);
            tsm.putCharacterStatValue(ACC, o4);
            tsm.putCharacterStatValue(EVA, o4);
            tsm.sendSetStatPacket();
        }
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        int slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }

        if (hasHitMobs) {
            //Lord of Darkness
            lordOfDarkness();

            killCountFinalPactOnMob(attackInfo);

            //Dark Thirst
            darkThirst(tsm);
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case SPEAR_SWEEP:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (!mob.isBoss()) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skill.getSkillId();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case EVIL_EYE:
                if (tsm.getOption(Beholder).ssOption > 0) //If can use EVIL_EYE_SHOCK
                {
                    skill = chr.getSkill(EVIL_EYE_SHOCK);
                    si = SkillData.getSkillInfoById(skill.getSkillId());
                    slv = skill.getCurrentLevel();

                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                            if (mob == null) {
                                continue;
                            }
                            if (!mob.isBoss()) {
                                MobTemporaryStat mts = mob.getTemporaryStat();
                                o1.nOption = 1;
                                o1.rOption = skill.getSkillId();
                                o1.tOption = si.getValue(time, slv);
                                mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                            }
                            //TODO add removal of shock
                        }
                    }
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void darkThirst(TemporaryStatManager tsm) {
        if (tsm.getOptByCTSAndSkill(IndiePAD, DARK_THIRST) != null) {
            Skill skill = chr.getSkill(DARK_THIRST);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int heal = si.getValue(x, slv);
            chr.heal((int) (chr.getMaxHP() / ((double) 100 / heal)));
        }
    }

    public void lordOfDarkness() {
        if (chr.hasSkill(LORD_OF_DARKNESS)) {
            Skill skill = chr.getSkill(LORD_OF_DARKNESS);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int proc = si.getValue(prop, slv);
            if (Util.succeedProp(proc)) {
                int heal = si.getValue(x, slv);
                chr.heal((int) (chr.getMaxHP() / ((double) 100 / heal)));
            }
        }
    }

    private Skill getFinalAtkSkill() {
        Skill skill = null;
        if (chr.hasSkill(FINAL_ATTACK_SPEARMAN)) {
            skill = chr.getSkill(FINAL_ATTACK_SPEARMAN);
        }
        return skill;
    }

    @Override
    public int getFinalAttackSkill() {
        Skill faSkill = getFinalAtkSkill();
        if (faSkill != null) {
            SkillInfo si = SkillData.getSkillInfoById(faSkill.getSkillId());
            byte slv = (byte) faSkill.getCurrentLevel();
            int proc = si.getValue(prop, slv);

            if (Util.succeedProp(proc)) {
                return faSkill.getSkillId();
            }
        }
        return 0;
    }


    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = null;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skillID);
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (skillID) {
            case IRON_WILL:
                o1.nOption = si.getValue(pdd, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PDD, o1);
                o2.nOption = si.getValue(mdd, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(MDD, o2);
                break;
            case HYPER_BODY:
                o1.nOption = si.getValue(x, slv) + chr.getSkillStatValue(x, HYPER_BODY_VITALITY);;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) + chr.getSkillStatValue(time, HYPER_BODY_PERSIST);
                tsm.putCharacterStatValue(MaxHP, o1);
                o2.nOption = si.getValue(y, slv) + chr.getSkillStatValue(y, HYPER_BODY_SPIRIT);;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv) + chr.getSkillStatValue(time, HYPER_BODY_PERSIST);
                tsm.putCharacterStatValue(MaxMP, o2);
                break;
            case CROSS_SURGE:
                int totalHP = c.getChr().getMaxHP();
                int currentHP = c.getChr().getHP();
                o1.nOption = (int) ((si.getValue(x, slv) * ((double) currentHP) / totalHP));
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o1);
                o2.nOption = (int) Math.min((0.08 * totalHP - currentHP), si.getValue(z, slv));
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamageReduce, o2);
                break;
            case EVIL_EYE:
                spawnEvilEye(skillID, slv);
                break;
            case EVIL_EYE_OF_DOMINATION:
                if (tsm.hasStat(Beholder)) {
                    tsm.removeStatsBySkill(EVIL_EYE_OF_DOMINATION);
                    spawnEvilEye(EVIL_EYE, slv);
                } else {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = 0;
                    o1.sOption = skillID;
                    o1.ssOption = 0;
                    tsm.putCharacterStatValue(Beholder, o1);
                }
                break;
            case EVIL_EYE_SHOCK:
                if (tsm.getOption(Beholder).nOption > 0 && tsm.getOption(Beholder).sOption > 0) //If evil eye and domination is active
                {
                    slv = (byte) chr.getSkill(EVIL_EYE).getCurrentLevel();
                    Option o = new Option();
                    o.nOption = tsm.getOption(Beholder).nOption;
                    o.rOption = tsm.getOption(Beholder).rOption;
                    o.tOption = tsm.getOption(Beholder).tOption;
                    o.sOption = tsm.getOption(Beholder).sOption;
                    o.ssOption = EVIL_EYE_SHOCK;
                    //Remove after getting the options
                    tsm.removeStatsBySkill(EVIL_EYE_OF_DOMINATION);
                    spawnEvilEye(EVIL_EYE, slv);
                    tsm.putCharacterStatValue(Beholder, o);
                }
                break;
            case SACRIFICE:
                if (tsm.hasStatBySkillId(EVIL_EYE)) {
                    o2.nReason = skillID;
                    o2.nValue = si.getValue(x, slv);
                    o2.tStart = (int) System.currentTimeMillis();
                    o2.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o2);

                    o3.nReason = skillID;
                    o3.nValue = si.getValue(indieBDR, slv);
                    o3.tStart = (int) System.currentTimeMillis();
                    o3.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieBDR, o3);


                    tsm.removeStatsBySkill(EVIL_EYE_OF_DOMINATION);
                    tsm.removeStatsBySkill(EVIL_EYE_SHOCK);
                    removeEvilEye(tsm, c);

                    chr.heal((int) (chr.getMaxHP() / ((double) 100 / si.getValue(y, slv))));
                    chr.write(UserLocal.skillCooltimeSetM(EVIL_EYE, 0));
                }
                break;
            case DARK_THIRST:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = (int) System.currentTimeMillis();
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

    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        super.handleRemoveCTS(cts);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (cts == Reincarnation) {
            if (tsm.getOption(Reincarnation).xOption > 0) {
                chr.setStatAndSendPacket(Stat.hp, 0);
                chr.write(UserLocal.openUIOnDead(true, chr.getBuffProtectorItem() != null,
                        false, false, false,
                        ReviveType.NORMAL.getVal(), 0));
            }
        }
    }

    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, InPacket inPacket, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(REVENGE_OF_THE_EVIL_EYE)) {
            Skill skill = chr.getSkill(REVENGE_OF_THE_EVIL_EYE);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int proc = si.getValue(prop, slv);
            int cd = 1000 * si.getValue(cooltime, slv);
            int heal = si.getValue(x, slv);
            if (chr.hasSkill(EVIL_EYE) && tsm.hasStatBySkillId(EVIL_EYE)) {
                if (cd + revengeEvilEye < System.currentTimeMillis()) {
                    if (Util.succeedProp(proc)) {
                        c.write(Summoned.summonBeholderRevengeAttack(evilEye, hitInfo.mobID));
                        chr.heal((int) (chr.getMaxHP() / ((double) 100 / heal)));
                        revengeEvilEye = System.currentTimeMillis();
                    }
                }
            }
        }
        super.handleHit(chr, inPacket, hitInfo);
    }

    public void reviveByFinalPact() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!chr.hasSkill(FINAL_PACT_INFO) || chr.hasSkillOnCooldown(FINAL_PACT_INFO)) {
            return;
        }

        Skill skill = chr.getSkill(FINAL_PACT_INFO);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();

        chr.heal(chr.getMaxHP());
        chr.healMP(chr.getMaxMP());

        o1.setInMillis(true);
        o1.nOption = 1;
        o1.rOption = FINAL_PACT;
        o1.tOption = si.getValue(time, slv) * 1000;
        o1.xOption = si.getValue(z, slv);
        tsm.putCharacterStatValue(Reincarnation, o1);
        o2.nOption = 1;
        o2.rOption = FINAL_PACT;
        o2.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(NotDamaged, o2);
        if (chr.hasSkill(FINAL_PACT_DAMAGE)) {
            o3.nReason = FINAL_PACT;
            o3.nValue = 20;
            o3.tTerm = si.getValue(time, slv);
            tsm.putCharacterStatValue(IndieDamR, o3);
        }
        tsm.sendSetStatPacket();

        finishFinalPact = System.currentTimeMillis() + (si.getValue(time, slv) * 1000L);
        int finalPactCooltime = si.getValue(cooltime, slv);
        if (chr.hasSkill(FINAL_PACT_COOLDOWN)) {
            finalPactCooltime *= 0.9;
        }
        chr.addSkillCoolTime(FINAL_PACT_INFO, finalPactCooltime * 1000L);
        chr.resetSkillCoolTime(GUNGNIR_DESCENT);
        chr.write(UserPacket.effect(Effect.showFinalPactEffect(FINAL_PACT, (byte) 1, 0, true))); // Manually broadcasting Effect packet, as FINAL PACT isn't actually ever called.
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.showFinalPactEffect(FINAL_PACT, (byte) 1, 0, true)));
    }

    private void lowerFinalPactKillCount() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        Skill skill = chr.getSkill(FINAL_PACT_INFO);
        if (skill == null || !tsm.hasStat(Reincarnation)) {
            return;
        }
        int duration = (int) (finishFinalPact - System.currentTimeMillis());

        killCount = tsm.getOption(Reincarnation).xOption;
        if (killCount > 0) {
            killCount--;

            if (duration > 0) {
                o.setInMillis(true);
                o.nOption = 1;
                o.rOption = FINAL_PACT;
                o.tOption = duration;
                o.xOption = killCount;
                tsm.putCharacterStatValue(Reincarnation, o);
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
                    int totaldmg = Arrays.stream(mai.damages).sum();

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

