package net.swordie.ms.client.jobs.adventurer.warrior;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;

import java.util.concurrent.ScheduledFuture;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Hero extends Warrior {
    public static final int WEAPON_MASTERY_FIGHTER = 1100000;
    public static final int WEAPON_BOOSTER_FIGHTER = 1101004;
    public static final int COMBO_ATTACK = 1101013;
    public static final int RAGE = 1101006;
    public static final int ENDURE = 1110011;
    public static final int FINAL_ATTACK_FIGHTER = 1100002;
    public static final int MAPLE_WARRIOR_HERO = 1121000;
    public static final int COMBO_FURY = 1101012;
    public static final int COMBO_FURY_DOWN = 1100012;
    public static final int SELF_RECOVERY = 1110000;
    public static final int CHANCE_ATTACK = 1110009;
    public static final int COMBO_SYNERGY = 1110013;
    public static final int PANIC = 1111003;
    public static final int SHOUT = 1111008;
    public static final int SHOUT_DOWN = 1111014;
    public static final int ADVANCED_FINAL_ATTACK = 1120013;
    public static final int ENRAGE = 1121010;
    public static final int PUNCTURE = 1121015;
    public static final int MAGIC_CRASH_HERO = 1121016;
    public static final int HEROS_WILL_HERO = 1121011;
    public static final int ADVANCED_COMBO = 1120003;

    public static final int ADVANCED_COMBO_REINFORCE = 1120043;
    public static final int ADVANCED_COMBO_BOSS_RUSH = 1120045;
    public static final int ADVANCED_FINAL_ATTACK_ACCURACY = 1120046;
    public static final int ADVANCED_FINAL_ATTACK_FEROCITY = 1120047;
    public static final int EPIC_ADVENTURE_HERO = 1121053;
    public static final int CRY_VALHALLA = 1121054;


    private ScheduledFuture selfRecoveryTimer;

    public Hero(Char chr) {
        super(chr);
        selfRecoveryTimer = EventManager.addFixedRateEvent(this::selfRecovery, 4000, 4000);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isHero(id);
    }

    private void addCombo() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int currentCount = getComboCount();
        if (currentCount < 0) {
            return;
        }
        int maxCombo = getMaxCombo();
        int added = 1;
        if (chr.hasSkill(ADVANCED_COMBO)) {
            if (Util.succeedProp(chr.getSkillStatValue(prop, ADVANCED_COMBO))) {
                added = 2;
            }
        }
        Option o = new Option();
        o.nOption = Math.min(maxCombo, currentCount + added);
        o.rOption = COMBO_ATTACK;
        tsm.putCharacterStatValue(ComboCounter, o);
        tsm.sendSetStatPacket();
    }

    private void removeCombo(int count) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int currentCount = getComboCount();
        Option o = new Option();
        if (currentCount > count + 1) {
            o.nOption = currentCount - count;
        } else {
            o.nOption = 0;
        }
        o.rOption = COMBO_ATTACK;
        tsm.putCharacterStatValue(ComboCounter, o);
        tsm.sendSetStatPacket();
    }

    private int getComboProp() {
        int skillId = 0;
        if (chr.hasSkill(COMBO_SYNERGY)) {
            skillId = COMBO_SYNERGY;
        } else if (chr.hasSkill(COMBO_ATTACK)) {
            skillId = COMBO_ATTACK;
        }
        if (skillId == 0) {
            return 0;
        }
        return chr.getSkillStatValue(prop, skillId);
    }

    private int getComboCount() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(ComboCounter)) {
            return tsm.getOption(ComboCounter).nOption;
        }
        return -1;
    }

    private int getMaxCombo() {
        int num = 0;
        if (chr.hasSkill(ADVANCED_COMBO)) {
            num = 11;
        } else if (chr.hasSkill(COMBO_ATTACK)) {
            num = 6;
        }
        return num;
    }

    public int getComboAttackSkill() {
        if (chr.hasSkill(ADVANCED_COMBO)) {
            return ADVANCED_COMBO;
        } else if (chr.hasSkill(COMBO_SYNERGY)) {
            return COMBO_SYNERGY;
        } else if (chr.hasSkill(COMBO_ATTACK)) {
            return COMBO_ATTACK;
        }
        return 0;
    }

    private void selfRecovery() {
        if (chr.hasSkill(SELF_RECOVERY) && chr.getHP() > 0) {
            chr.heal(chr.getSkillStatValue(hp, SELF_RECOVERY));
            chr.healMP(chr.getSkillStatValue(mp, SELF_RECOVERY));
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
        if (hasHitMobs && !isComboIgnoreSkill(attackInfo.skillId)) {
            int comboProp = getComboProp();
            if (Util.succeedProp(comboProp)) {
                addCombo();
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        Option o6 = new Option();
        switch (attackInfo.skillId) {
            case COMBO_FURY:
                removeCombo(1);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        if (!mob.isBoss()) {
                            o1.nOption = 1;
                            o1.rOption = skillID;
                            o1.tOption = si.getValue(time, slv);
                            mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                        }
                        addCombo();
                    }
                }
                break;
            case COMBO_FURY_DOWN:
                removeCombo(1);
                break;
            case PANIC:
                removeCombo(!tsm.hasStat(ComboCostInc) ? 2 : tsm.getOption(ComboCostInc).nOption + 2);
                panicComboCostInc();
                if (hasHitMobs) {
                    int duration = si.getValue(time, slv);

                    o1.nOption = -si.getValue(w, slv);
                    o1.rOption = skillID;
                    o1.tOption = duration;
                    o2.nOption = -si.getValue(w, slv);
                    o2.rOption = skillID;
                    o2.tOption = duration / 2;

                    o3.nOption = -si.getValue(x, slv);
                    o3.rOption = skillID;
                    o3.tOption = duration;
                    o4.nOption = -si.getValue(x, slv);
                    o4.rOption = skillID;
                    o4.tOption = duration / 2;

                    o5.nOption = 1;
                    o5.rOption = skillID;
                    o5.tOption = duration;
                    o6.nOption = 1;
                    o6.rOption = skillID;
                    o6.tOption = duration / 2;
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.PAD, mob.isBoss() ? o2 : o1);
                        mts.addStatOptionsAndBroadcast(MobStat.MAD, mob.isBoss() ? o2 : o1);
                        if (Util.succeedProp(si.getValue(prop, slv))) {
                            mts.addStatOptionsAndBroadcast(MobStat.ACC, mob.isBoss() ? o4 : o3);
                            mts.addStatOptionsAndBroadcast(MobStat.Blind, mob.isBoss() ? o6 : o5);
                        }
                    }
                }
                break;
            case SHOUT:
                if (hasHitMobs) {
                    removeCombo(si.getValue(y, slv));
                }
                break;
            case SHOUT_DOWN:
                Skill orig = chr.getSkill(SHOUT);
                slv = orig.getCurrentLevel();
                si = SkillData.getSkillInfoById(SHOUT_DOWN);
                o1.nOption = -10;
                o1.rOption = SHOUT_DOWN;
                o1.tOption = si.getValue(time, slv);

                o2.nOption = 1;
                o2.rOption = SHOUT_DOWN;
                o2.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (mob.isBoss()) {
                        mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                    } else {
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o2);
                    }
                }
                removeCombo(1);
                chr.setSkillCooldown(orig.getSkillId(), slv);
                break;
            case PUNCTURE:
                removeCombo(si.getValue(y, slv));
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptions(MobStat.HitCriDamR, o1);
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mts.createAndAddBurnedInfo(chr, skillID, slv);
                    }
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void panicComboCostInc() {
        if (!chr.hasSkill(PANIC)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        Skill skill = chr.getSkill(PANIC);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        int slv = skill.getCurrentLevel();

        int count = 1;
        if (tsm.hasStat(ComboCostInc)) {
            count = tsm.getOption(ComboCostInc).nOption;
            if (count < 8) {
                count++;
            }
        }
        o.nOption = count;
        o.rOption = skill.getSkillId();
        o.tOption = si.getValue(subTime, slv);
        tsm.putCharacterStatValue(ComboCostInc, o);
        tsm.sendSetStatPacket();
    }

    @Override
    public int getFinalAttackSkill() {
        int skillId = 0;
        if (chr.hasSkill(ADVANCED_FINAL_ATTACK)) {
            skillId = ADVANCED_FINAL_ATTACK;
        } else if (chr.hasSkill(FINAL_ATTACK_FIGHTER)) {
            skillId = FINAL_ATTACK_FIGHTER;
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

    public boolean isComboIgnoreSkill(int skillID) {
        return skillID == SHOUT ||
                skillID == SHOUT_DOWN ||
                skillID == PANIC ||
                skillID == COMBO_FURY ||
                skillID == COMBO_FURY_DOWN ||
                skillID == PUNCTURE;
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
            case RAGE:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                if (this.chr == chr) {
                    // Power Guard effect only for caster
                    o2.nOption = si.getValue(x, slv);
                    o2.rOption = skillID;
                    o2.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(PowerGuard, o2);
                }
                break;
            case COMBO_ATTACK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(ComboCounter, o1);
                break;
            case ENRAGE:
                removeCombo(1);
                o1.nOption = 100 * si.getValue(x, slv) + si.getValue(mobCount, slv);; // fd = n / 100, mobsHit = n % 100
                o1.rOption = skillID;
                tsm.putCharacterStatValue(Enrage, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                tsm.putCharacterStatValue(EnrageCrDamMin, o2);
                break;
            case CRY_VALHALLA:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieCr, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o2);
                tsm.putCharacterStatValue(TerR, o2);
                o3.nOption = 100;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stance, o3);
                break;
        }
        tsm.sendSetStatPacket();
    }

    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(COMBO_SYNERGY)) {
            int comboProp = chr.getSkillStatValue(subProp, COMBO_SYNERGY);
            if (Util.succeedProp(comboProp)) {
                addCombo();
            }
        }
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleRemoveSkill(int skillID) {
        if (skillID == SELF_RECOVERY && selfRecoveryTimer != null && !selfRecoveryTimer.isDone()) {
            selfRecoveryTimer.cancel(true);
        }
        super.handleRemoveSkill(skillID);
    }

    @Override
    public void handleCancelTimer(Char chr) {
        if (selfRecoveryTimer != null && !selfRecoveryTimer.isDone()) {
            selfRecoveryTimer.cancel(true);
        }
        super.handleCancelTimer(chr);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

