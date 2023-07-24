package net.swordie.ms.client.jobs.legend;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.UserLocal;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Aran extends Job {
    public static final int COMBO_ABILITY = 21000000;
    public static final int COMBAT_STEP = 20001295;
    public static final int REGAINED_MEMORY = 20000194;
    public static final int RETURN_TO_RIEN = 20001296;

    public static final int POLEARM_MASTERY = 21100000;
    public static final int POLEARM_BOOSTER = 21001003; //Buff
    public static final int BODY_PRESSURE = 21001008; //Buff (ON/OFF)

    public static final int SWING_STUDIES_1 = 21100015;
    public static final int SNOW_CHARGE = 21101006; //Buff
    public static final int DRAIN = 21101005; //Special Skill (HP Recovery) (ON/OFF)
    public static final int ADVANCED_COMBO_ABILITY = 21110000;
    public static final int MAHA_BLESSING = 21111012; //Buff
    public static final int ADRENALINE_RUSH = 21110016; //at 1000 combo activated

    public static final int HIGH_MASTERY = 21120001;
    public static final int HIGH_DEFENSE = 21120004;
    public static final int SWING_STUDIES_2 = 21120021;
    public static final int MAPLE_WARRIOR_ARAN = 21121000; //Buff
    public static final int HEROS_WILL_ARAN = 21121008;

    public static final int SURGING_ADRENALINE = 21120064;
    public static final int HEROIC_MEMORIES_ARAN = 21121053;
    public static final int ADRENALINE_BURST = 21121058;
    public static final int MAHAS_DOMAIN = 21121068; //AoE Effect

    //Final Attack
    public static final int FINAL_ATTACK = 21100010;
    public static final int ADVANCED_FINAL_ATTACK = 21120012;

    //Attacking Skills:
    public static final int SMASH_WAVE = 21001009;
    public static final int SMASH_WAVE_COMBO = 21000004;

    public static final int SMASH_SWING_1 = 21001010;
    public static final int SMASH_SWING_2 = 21000006;
    public static final int SMASH_SWING_3 = 21000007;
    public static final int SMASH_SWING_2_FINAL_BLOW = 21120025;

    public static final int AERO_SWING_1 = 21110026;
    public static final int AERO_SWING_2 = 21110022;
    public static final int AERO_SWING_3 = 21110023;

    public static final int FINAL_CHARGE = 21101011;
    public static final int FINAL_CHARGE_COMBO = 21100002; //Special Attack (Stun Debuff) (Special Skill from Key-Command)

    public static final int FINAL_TOSS = 21101016;
    public static final int FINAL_TOSS_COMBO = 21100012;

    public static final int ROLLING_SPIN = 21101017;
    public static final int ROLLING_SPIN_COMBO = 21100013; //Special Attack (Stun Debuff) (Special Skill from Key-Command)

    public static final int GATHERING_HOOK = 21111019;
    public static final int GATHERING_HOOK_COMBO = 21110018;

    public static final int FINAL_BLOW = 21111021;
    public static final int FINAL_BLOW_COMBO = 21110020; //Special Attack (Stun Debuff) (Special Skill from Key-Command)
    public static final int FINAL_BLOW_SMASH_SWING_COMBO = 21110028; //Special Attack (Stun Debuff) (Special Skill from Key-Command)
    public static final int FINAL_BLOW_ADRENALINE_SHOCKWAVE = 21110027; //Shockwave after final blow when in Adrenaline Rush

    public static final int JUDGEMENT_DRAW = 21111017;
    public static final int JUDGEMENT_DRAW_COMBO_DOWN = 21110011; //Special Attack (Freeze Debuff) (Special Skill from Key-Command)
    public static final int JUDGEMENT_DRAW_COMBO_LEFT = 21110024; //Special Attack (Freeze Debuff) (Special Skill from Key-Command)
    public static final int JUDGEMENT_DRAW_COMBO_RIGHT = 21110025; //Special Attack (Freeze Debuff) (Special Skill from Key-Command)

    public static final int BEYOND_BLADE_1 = 21120022;
    public static final int BEYOND_BLADE_2 = 21121016;
    public static final int BEYOND_BLADE_3 = 21121017;

    public static final int FINISHER_HUNTER_PREY = 21120019;
    public static final int FINISHER_STORM_OF_FEAR = 21120023;
    public static final int FINISHER_STORM_OF_FEAR_ATTACK = 21120018;
    public static final int FINISHER_HUNTER_PREY_FINAL = 21120026;
    public static final int FINISHER_STORM_OF_FEAR_FINAL = 21120027;


    private int[] addedSkills = new int[] {
            RETURN_TO_RIEN,
    };

    private int combo;

    public Aran(Char chr) {
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
        return JobConstants.isAran(id);
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
            handleCombo(attackInfo);
            handleSnowCharge(attackInfo);
            handleDrain();
        }
        // handle aero swing
        if (!ServerConstants.CLIENT_SIDED_SKILL_HOOK) {
            if (attackInfo.skillId == AERO_SWING_1) {
                chr.write(UserLocal.setSlowDown(30, 360));
            } else if (attackInfo.skillId == AERO_SWING_2 || attackInfo.skillId == SMASH_SWING_2_FINAL_BLOW) {
                if (chr.getMoveAction() == 6 || chr.getMoveAction() == 7) {
                    chr.write(UserLocal.setSlowDown(30, 300));
                }
            } else if (attackInfo.skillId == AERO_SWING_3 || attackInfo.skillId == FINAL_BLOW_SMASH_SWING_COMBO) {
                if (chr.getMoveAction() == 6 || chr.getMoveAction() == 7) {
                    chr.write(UserLocal.setSlowDown(30, 648));
                }
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case SMASH_SWING_1:
            case AERO_SWING_1:
                handleSwingStudies();
                break;
            case FINAL_BLOW_COMBO:
            case FINAL_BLOW_SMASH_SWING_COMBO:
            case FINAL_CHARGE_COMBO:
                o1.nOption = 1;
                o1.rOption = FINAL_CHARGE;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                }
            case FINAL_CHARGE:
                // Fallthrough intended
                if (tsm.hasStat(AdrenalinBoost)) {
                    AffectedArea aa = AffectedArea.getPassiveAA(chr, FINAL_CHARGE, slv);
                    aa.setPosition(chr.getPosition());
                    aa.setRect(aa.getPosition().getRectAround(si.getLastRect()));
                    if (!attackInfo.left) {
                        aa.setRect(aa.getRect().moveRight());
                    }
                    chr.getField().spawnAffectedArea(aa);
                }
                break;
            case ROLLING_SPIN_COMBO:
                o1.nOption = 1;
                o1.rOption = ROLLING_SPIN;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                }
                // Fallthrough intended
            case FINAL_TOSS:
            case FINAL_TOSS_COMBO:
            case ROLLING_SPIN:
                o1.nOption = chr.getSkillStatValue(x, FINAL_TOSS);
                o1.rOption = skillID;
                o1.tOption = 1;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.RiseByToss, o1);
                }
                break;
            case JUDGEMENT_DRAW_COMBO_DOWN:
            case JUDGEMENT_DRAW_COMBO_LEFT:
            case JUDGEMENT_DRAW_COMBO_RIGHT:
                o1.nOption = 1;
                o1.rOption = JUDGEMENT_DRAW;
                o1.tOption = si.getValue(time, slv);
                o2.nOption = chr.getSkillStatValue(x, FINAL_TOSS);
                o2.rOption = JUDGEMENT_DRAW;
                o2.tOption = 1;
                for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (!mob.isBoss()) {
                        mts.addStatOptions(MobStat.Freeze, o1);
                        mts.addStatOptions(MobStat.RiseByToss, o2);
                    }
                    mts.createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
            case FINISHER_STORM_OF_FEAR_FINAL: // storm of fear final is attack, hunter prey is skill..
                tsm.removeStat(AranBoostEndHunt, false);
                tsm.removeStat(AdrenalinBoost, false);
                tsm.sendResetStatPacket();
                break;
            case SMASH_WAVE_COMBO:
                chr.setSkillCooldown(SMASH_WAVE, chr.getSkillLevel(SMASH_WAVE));
                break;
            case GATHERING_HOOK_COMBO:
                chr.setSkillCooldown(GATHERING_HOOK, chr.getSkillLevel(GATHERING_HOOK));
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    public void handleDecCombo() {
        setCombo(getCombo() - 10);
    }

    private int getCombo() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(ComboAbilityBuff)) {
            return tsm.getOption(ComboAbilityBuff).nOption;
        }
        return 0;
    }

    private void setCombo(int combo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        if (combo >= 1000) {
            combo = 1000;
            if (chr.hasSkill(ADRENALINE_RUSH) && !tsm.hasStat(AdrenalinBoost)) {
                int duration = chr.getSkillStatValue(time, ADRENALINE_RUSH) + chr.getSkillStatValue(time, SURGING_ADRENALINE);;
                o1.nOption = 1;
                o1.rOption = ADRENALINE_RUSH;
                o1.tOption = duration;
                o1.cOption = 1;
                tsm.putCharacterStatValue(AdrenalinBoost, o1);
                o2.nValue = 1;
                o2.nReason = ADRENALINE_RUSH;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = duration;
                tsm.putCharacterStatValue(IndieEmpty, o2);
            }
        }
        if (combo > 0) {
            o3.nOption = combo;
            o3.rOption = COMBO_ABILITY;
            tsm.putCharacterStatValue(ComboAbilityBuff, o3);
            tsm.sendSetStatPacket();
        } else {
            tsm.removeStat(ComboAbilityBuff, false);
            tsm.sendResetStatPacket();
            combo = 0;
        }
        chr.write(WvsContext.modComboResponse(combo));
    }

    private void handleCombo(AttackInfo attackInfo) {
        if (!chr.hasSkill(COMBO_ABILITY)) {
            return;
        }
        int combo = getCombo();
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            combo += mai.damages.length;
            break;
        }
        setCombo(combo);
    }

    private void handleSnowCharge(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStatBySkillId(SNOW_CHARGE)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(SNOW_CHARGE);
        int slv = chr.getSkillLevel(SNOW_CHARGE);
        Option o1 = new Option();
        Option o2 = new Option();
        o1.nOption = -si.getValue(q, slv);
        o1.rOption = SNOW_CHARGE;
        o1.tOption = si.getValue(y, slv) / 2;
        o2.nOption = -si.getValue(q, slv);
        o2.rOption = SNOW_CHARGE;
        o2.tOption = si.getValue(y, slv);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            if (mob.isBoss()) {
                mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
            } else {
                mts.addStatOptionsAndBroadcast(MobStat.Speed, o2);
            }
        }
    }

    private void handleDrain() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStatBySkillId(DRAIN)) {
            return;
        }
        if (chr.getHP() > 0) {
            chr.heal((int) (chr.getMaxHP() * chr.getSkillStatValue(x, DRAIN) / 100D));
        }
    }

    private void handleSwingStudies() {
        int skillId = 0;
        if (chr.hasSkill(SWING_STUDIES_2)) {
            skillId = SWING_STUDIES_2;
        } else if (chr.hasSkill(SWING_STUDIES_1)) {
            skillId = SWING_STUDIES_1;
        }
        if (skillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        Option o1 = new Option();
        o1.nOption = si.getValue(w, slv);
        o1.rOption = skillId;
        o1.tOption = si.getValue(z, slv);
        tsm.putCharacterStatValue(NextAttackEnhance, o1);
        tsm.sendSetStatPacket();
    }

    @Override
    public int getFinalAttackSkill() {
        int skillId = 0;
        if (chr.hasSkill(ADVANCED_FINAL_ATTACK)) {
            skillId = ADVANCED_FINAL_ATTACK;
        } else if (chr.hasSkill(FINAL_ATTACK)) {
            skillId = FINAL_ATTACK;
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
        switch (skillID) {
            case FINISHER_HUNTER_PREY_FINAL:
                tsm.removeStat(AranBoostEndHunt, false);
                tsm.removeStat(AdrenalinBoost, false);
                tsm.sendResetStatPacket();
                break;
            case ADRENALINE_BURST:
                setCombo(1000);
                break;
            case RETURN_TO_RIEN:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case MAHAS_DOMAIN:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(inPacket.decodePosition());
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                chr.getField().spawnAffectedArea(aa);
                break;
            case HEROS_WILL_ARAN:
                tsm.removeAllDebuffs();
                break;
            case POLEARM_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case BODY_PRESSURE:
                if(tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(skillID);
                } else {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(BodyPressure, o1);
                }
                break;
            case DRAIN:
                if(tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(skillID);
                } else {
                    o1.nOption = si.getValue(x, slv);
                    o1.rOption = skillID;
                    tsm.putCharacterStatValue(AranDrain, o1);
                }
                break;
            case SNOW_CHARGE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(WeaponCharge, o1);
                break;
            case MAHA_BLESSING:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieMad, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMAD, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indiePad, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o2);
                break;
            case MAPLE_WARRIOR_ARAN:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case HEROIC_MEMORIES_ARAN:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMaxDamageOverR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMaxDamageOverR, o2);
                break;
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleSkillPrepare(int prepareSkillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(prepareSkillId);
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        int slv = chr.getSkillLevel(skillID);
        Option o1 = new Option();
        switch (prepareSkillId) {
            case FINISHER_HUNTER_PREY:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 5;
                tsm.putCharacterStatValue(AranBoostEndHunt, o1);
                tsm.sendSetStatPacket();
                break;
            case FINISHER_STORM_OF_FEAR_ATTACK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 10;
                tsm.putCharacterStatValue(AranBoostEndHunt, o1);
                tsm.sendSetStatPacket();
                break;
        }
        super.handleSkillPrepare(prepareSkillId);
    }

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        if (cts == AdrenalinBoost) {
            chr.getTemporaryStatManager().removeStatsBySkill(IndieEmpty, ADRENALINE_RUSH);
            setCombo(500);
        }
        super.handleRemoveCTS(cts);
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        cs.setPosMap(914000000);
    }
}
