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
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Hero extends Warrior {

    public static final int MAPLE_RETURN = 1281;

    public static final int WEAPON_BOOSTER_FIGHTER = 1101004;
    public static final int COMBO_ATTACK = 1101013;
    public static final int RAGE = 1101006;
    public static final int FINAL_ATTACK_FIGHTER = 1100002;
    public static final int MAPLE_WARRIOR_HERO = 1121000;
    public static final int COMBO_FURY = 1101012;
    public static final int COMBO_FURY_DOWN = 1100012;
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

    public static final int EPIC_ADVENTURE_HERO = 1121053; //Lv200
    public static final int CRY_VALHALLA = 1121054; //Lv150

    private int[] addedSkills = new int[]{
            MAPLE_RETURN,
    };

    private long lastPanicHit = Long.MIN_VALUE;

    public Hero(Char chr) {
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
            int slv = chr.getSkillLevel(ADVANCED_COMBO);
            SkillInfo si = SkillData.getSkillInfoById(ADVANCED_COMBO);
            if (slv > 0 && Util.succeedProp(si.getValue(prop, slv))) {
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
        Skill skill = null;
        if (chr.hasSkill(COMBO_SYNERGY)) {
            skill = chr.getSkill(COMBO_SYNERGY);
        } else if (chr.hasSkill(COMBO_ATTACK)) {
            skill = chr.getSkill(COMBO_ATTACK);
        }
        if (skill == null) {
            return 0;
        }
        return SkillData.getSkillInfoById(skill.getSkillId()).getValue(prop, skill.getCurrentLevel());
    }

    public int getComboCount() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(ComboCounter)) {
            return tsm.getOption(ComboCounter).nOption;
        }
        return -1;
    }

    private int getMaxCombo() {
        int num = 0;
        if (chr.hasSkill(COMBO_ATTACK)) {
            num = 6;
        }
        if (chr.hasSkill(ADVANCED_COMBO)) {
            num = 11;
        }
        return num;
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

        if (hasHitMobs && !isComboIgnoreSkill(attackInfo.skillId)) {
            //Combo
            int comboProp = getComboProp();
            if (Util.succeedProp(comboProp)) {
                addCombo();
            }
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case COMBO_FURY:
                removeCombo(1);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (Util.succeedProp(si.getValue(prop, skill.getCurrentLevel()))) {
                        if (!mob.isBoss()) {
                            o1.nOption = 1;
                            o1.rOption = skill.getSkillId();
                            o1.tOption = si.getValue(time, skill.getCurrentLevel());
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
                    int dur = si.getValue(time, slv);

                    o1.nOption = -si.getValue(w, slv);
                    o1.rOption = skill.getSkillId();
                    o1.tOption = dur;
                    o2.nOption = -si.getValue(w, slv);
                    o2.rOption = skill.getSkillId();
                    o2.tOption = dur / 2;

                    o3.nOption = -si.getValue(x, slv);
                    o3.rOption = skill.getSkillId();
                    o3.tOption = dur;
                    o4.nOption = -si.getValue(x, slv);
                    o4.rOption = skill.getSkillId();
                    o4.tOption = dur / 2;
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
                            mts.addStatOptionsAndBroadcast(MobStat.Blind, mob.isBoss() ? o4 : o3);
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
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = si.getValue(x, slv);
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptions(MobStat.HitCriDamR, o1); // TODO: check party effect - si.getValue(u, slv)
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mts.createAndAddBurnedInfo(chr, skill);
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

    private Skill getFinalAtkSkill() {
        Skill skill = null;
        if (chr.hasSkill(FINAL_ATTACK_FIGHTER)) {
            skill = chr.getSkill(FINAL_ATTACK_FIGHTER);
        }
        if (chr.hasSkill(ADVANCED_FINAL_ATTACK)) {
            skill = chr.getSkill(ADVANCED_FINAL_ATTACK); // Hero Adv FA
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
            case MAPLE_RETURN:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case RAGE:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PowerGuard, o2);
                break;
            case COMBO_ATTACK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(ComboCounter, o1);
                break;
            case ENRAGE:
                removeCombo(1);
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(Enrage, o1); // max mobs hit
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                tsm.putCharacterStatValue(EnrageCrDamMin, o2);
                o3.nOption = si.getValue(x, slv);
                o3.rOption = skillID;
                tsm.putCharacterStatValue(DamR, o3);
                break;
            case CRY_VALHALLA:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieCr, slv);
                o1.tStart = (int) System.currentTimeMillis();
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
    public void handleHit(Char chr, InPacket inPacket, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(COMBO_SYNERGY)) {
            int comboProp = chr.getSkillStatValue(subProp, COMBO_SYNERGY);
            if (Util.succeedProp(comboProp)) {
                addCombo();
            }
        }
        super.handleHit(chr, inPacket, hitInfo);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}
