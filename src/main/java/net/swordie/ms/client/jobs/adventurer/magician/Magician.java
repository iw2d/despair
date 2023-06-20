package net.swordie.ms.client.jobs.adventurer.magician;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.Beginner;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Magician extends Beginner {
    public static final int MAPLE_RETURN = 1281;
    public static final int TELEPORT = 2001009;
    public static final int MAGIC_GUARD = 2001002;

    private int[] addedSkills = new int[]{
            MAPLE_RETURN,
    };

    private int infinityStack = 0;
    private ScheduledFuture infinityTimer;

    public Magician(Char chr) {
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
        return id == JobConstants.JobEnum.MAGICIAN.getJobId();
    }



    // Buff related methods --------------------------------------------------------------------------------------------

    private void infinity() {
        int skillId = getInfinitySkill();
        if(!chr.hasSkill(skillId)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Skill skill = chr.getSkill(skillId);
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        infinityStack++;
        if(tsm.hasStat(Infinity)) {
            o1.nValue = infinityStack * si.getValue(damage, slv);
            o1.nReason = skillId + 100; //To make the buff icon hidden
            o1.tStart = Util.getCurrentTime();
            tsm.putCharacterStatValue(IndieMADR, o1);
            tsm.sendSetStatPacket();
            chr.heal((int) (chr.getMaxHP() / ((double) 100 / si.getValue(y, slv))));
            infinityTimer = EventManager.addEvent(this::infinity, 4, TimeUnit.SECONDS);
        } else {
            tsm.removeStatsBySkill(skillId + 100);
            tsm.sendResetStatPacket();
            infinityStack = 0;
        }
    }

    private int getInfinitySkill() {
        int skill = 0;
        if(chr.hasSkill(FirePoison.INFINITY_FP)) {
            skill = FirePoison.INFINITY_FP;
        }
        if(chr.hasSkill(IceLightning.INFINITY_IL)) {
            skill = IceLightning.INFINITY_IL;
        }
        if(chr.hasSkill(Bishop.INFINITY_BISH)) {
            skill = Bishop.INFINITY_BISH;
        }
        return skill;
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        byte slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }

        if (hasHitMobs) {
            incrementArcaneAim();
            mpEaterEffect(attackInfo);
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
        }

        super.handleAttack(chr, attackInfo);
    }

    private void incrementArcaneAim() {
        int skillId = getArcaneAimSkill();
        if (!chr.hasSkill(skillId)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        if (!Util.succeedProp(si.getValue(prop, slv))) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        Option o1 = new Option();
        Option o2 = new Option();
        int amount = 1;
        if (tsm.hasStat(ArcaneAim)) {
            amount = tsm.getOption(ArcaneAim).nOption;
            if (amount < si.getValue(y, slv)) {
                amount++;
            }
        }
        o.nOption = amount;
        o.rOption = skillId;
        o.tOption = 5; // No Time Variable
        tsm.putCharacterStatValue(ArcaneAim, o);
        o1.nOption = si.getValue(ignoreMobpdpR, slv);
        o1.rOption = skillId;
        o1.tOption = 5; // No Time Variable
        tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o1);
        o2.nOption = (amount * si.getValue(x, slv));
        o2.rOption = skillId;
        o2.tOption = 5; // No Time Variable
        tsm.putCharacterStatValue(DamR, o2);
        tsm.sendSetStatPacket();
    }

    private int getArcaneAimSkill() {
        int res = 0;
        if (chr.hasSkill(FirePoison.ARCANE_AIM_FP)) {
            res = FirePoison.ARCANE_AIM_FP;
        } else if (chr.hasSkill(IceLightning.ARCANE_AIM_IL)) {
            res = IceLightning.ARCANE_AIM_IL;
        } else if (chr.hasSkill(Bishop.ARCANE_AIM_BISH)) {
            res = Bishop.ARCANE_AIM_BISH;
        }
        return res;
    }


    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case MAPLE_RETURN:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case FirePoison.HEROS_WILL_FP:
            case IceLightning.HEROS_WILL_IL:
            case Bishop.HEROS_WILL_BISH:
                tsm.removeAllDebuffs();
                break;
            case MAGIC_GUARD:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(MagicGuard, o1);
                break;
            case FirePoison.MAGIC_BOOSTER_FP:
            case IceLightning.MAGIC_BOOSTER_IL:
            case Bishop.MAGIC_BOOSTER_BISH:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case FirePoison.MEDITATION_FP:
            case IceLightning.MEDITATION_IL:
                o1.nValue = si.getValue(indieMad, slv);
                o1.nReason = skillID;
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMAD, o1);
                break;
            case FirePoison.ELEMENTAL_DECREASE_FP:
            case IceLightning.ELEMENTAL_DECREASE_IL:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ElementalReset, o1);
                break;
            case FirePoison.TELEPORT_MASTERY_FP:
            case IceLightning.TELEPORT_MASTERY_IL:
            case IceLightning.TELEPORT_MASTERY_RANGE_IL:
            case Bishop.TELEPORT_MASTERY_BISH:
                CharacterTemporaryStat masteryStat = skillID == IceLightning.TELEPORT_MASTERY_RANGE_IL ? TeleportMasteryRange : TeleportMasteryOn;
                if (tsm.hasStat(masteryStat)) {
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = si.getValue(x, slv);
                    o1.rOption = skillID;
                    o1.tOption = 0;
                    tsm.putCharacterStatValue(masteryStat, o1);
                }
                break;
            case FirePoison.INFINITY_FP:
            case IceLightning.INFINITY_IL:
            case Bishop.INFINITY_BISH:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Infinity, o1);
                o2.nOption = si.getValue(prop, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stance, o2);
                infinityStack = 0;
                if(infinityTimer != null && !infinityTimer.isDone()) {
                    infinityTimer.cancel(true);
                }
                infinity();
                break;
            case FirePoison.IFRIT:
            case IceLightning.ELQUINES:
            case Bishop.BAHAMUT:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(true);
                summon.setMoveAbility(MoveAbility.Walk);
                field.spawnSummon(summon);
                break;
            case FirePoison.MAPLE_WARRIOR_FP:
            case IceLightning.MAPLE_WARRIOR_IL:
            case Bishop.MAPLE_WARRIOR_BISH:
                o1.nValue = si.getValue(x, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case FirePoison.EPIC_ADVENTURE_FP:
            case IceLightning.EPIC_ADVENTURE_IL:
            case Bishop.EPIC_ADVENTURE_BISH:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMaxDamageOverR, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMaxDamageOverR, o2);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void mpEaterEffect(AttackInfo attackInfo) {
        Skill skill = getMPEaterSkill();
        if (skill == null || attackInfo.skillId == 0) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        int slv = skill.getCurrentLevel();
        int mpStolen = si.getValue(x, slv);
        boolean showedEffect = false;
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            if (Util.succeedProp(si.getValue(prop, slv))) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null || mob.getMp() <= 0) {
                    continue;
                }
                long mobMaxMP = mob.getMaxMp();
                int mpAbsorbed = (int) (mobMaxMP * ((double) mpStolen / 100));
                mob.healMP(-(Math.min(mpAbsorbed, 500)));
                chr.healMP(Math.min(mpAbsorbed, 500));

                if (!showedEffect) {
                    showedEffect = true;
                    chr.write(UserPacket.effect(Effect.skillUse(getMPEaterSkill().getSkillId(), (byte) slv, 0)));
                    chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(getMPEaterSkill().getSkillId(), (byte) slv, 0)));
                }
            }
        }
    }

    private Skill getMPEaterSkill() {
        Skill skill = null;
        if (chr.hasSkill(FirePoison.MP_EATER_FP)) {
            skill = chr.getSkill(FirePoison.MP_EATER_FP);

        } else if (chr.hasSkill(IceLightning.MP_EATER_IL)) {
            skill = chr.getSkill(IceLightning.MP_EATER_IL);

        } else if (chr.hasSkill(Bishop.MP_EATER_BISHOP)) {
            skill = chr.getSkill(Bishop.MP_EATER_BISHOP);
        }
        return skill;
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    public void handleMobDebuffSkill(Char chr) {
        super.handleMobDebuffSkill(chr);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

