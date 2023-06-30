package net.swordie.ms.client.jobs.adventurer.thief;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.Beginner;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.drop.Drop;
import net.swordie.ms.life.drop.DropInfo;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class DualBlade extends Thief {
    public static final int SELF_HASTE = 4301003; //Buff
    public static final int KATARA_BOOSTER = 4311009; //Buff
    public static final int FLYING_ASSAULTER = 4321006; //Special Attack (Stun Debuff)
    public static final int FLASHBANG = 4321002; //Special Attack
    public static final int CHAINS_OF_HELL = 4331006; //Special Attack (Stun Debuff)
    public static final int MIRROR_IMAGE = 4331002; //Buff
    public static final int ADVANCED_DARK_SIGHT_DB = 4330001;
    public static final int SHADOW_MELD = 4330009;
    public static final int VENOM_DB = 4320005;
    public static final int LIFE_DRAIN = 4330007;
    public static final int FINAL_CUT = 4341002; //Special Attack
    public static final int SUDDEN_RAID_DB = 4341011; //Special Attack
    public static final int MAPLE_WARRIOR_DB = 4341000; //Buff
    public static final int MIRRORED_TARGET = 4341006; //Summon
    public static final int TOXIC_VENOM_DB = 4340012;
    public static final int HEROS_WILL_DB = 4341008;

    public static final int EPIC_ADVENTURE_DB = 4341053;
    public static final int BLADE_CLONE = 4341054;
    public static final int ASURAS_ANGER = 4341052;

    public static long lastShadowMeld = Long.MIN_VALUE;

    private int[] addedSkills = new int[] {
            MAPLE_RETURN,
    };

    public DualBlade(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isDualBlade(id);
    }

    public void giveShadowMeld() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(SHADOW_MELD)) {
            if (tsm.getOptByCTSAndSkill(IndiePAD, SHADOW_MELD) == null) {
                Skill skill = chr.getSkill(SHADOW_MELD);
                byte slv = (byte) skill.getCurrentLevel();
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());

                if (lastShadowMeld + 5000 < System.currentTimeMillis()) {
                    Option o1 = new Option();
                    Option o2 = new Option();
                    o1.nOption = 100;
                    o1.rOption = skill.getSkillId();
                    o1.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(CriticalBuff, o1);
                    o2.nReason = skill.getSkillId();
                    o2.nValue = si.getValue(indiePad, slv);
                    o2.tStart = (int) System.currentTimeMillis();
                    o2.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndiePAD, o2); //Indie
                    tsm.sendSetStatPacket();
                    lastShadowMeld = System.currentTimeMillis();
                }
            }
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
        byte slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            skillID = SkillConstants.getActualSkillIDfromSkillID(skill.getSkillId());
        }
        if (hasHitMobs) {
            recoverHPByLifeDrain();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case FLASHBANG:
                o1.nOption = -si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o2.nOption = 10; // no SkillStat assigned, literally just  10
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                // boss effect, halved duration
                o3.nOption = -si.getValue(x, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv) / 2;
                o4.nOption = 10;
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv) / 2;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (!mob.isBoss()) {
                            mts.addStatOptionsAndBroadcast(MobStat.ACC, o1);
                            mts.addStatOptionsAndBroadcast(MobStat.AddDamSkill2, o2);
                        } else {
                            mts.addStatOptionsAndBroadcast(MobStat.ACC, o3);
                            mts.addStatOptionsAndBroadcast(MobStat.AddDamSkill2, o4);
                        }
                    }
                }
                break;
            case FLYING_ASSAULTER:
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null || mob.isBoss()) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case CHAINS_OF_HELL:
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null || mob.isBoss()) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                o2.nOption = 1;
                o2.tOption = 3;
                tsm.putCharacterStatValue(NotDamaged, o2);
                tsm.sendSetStatPacket();
                break;
            case FINAL_CUT:
                int hpCost = (int) (chr.getMaxHP() / ( 100D / si.getValue(x, slv)));
                if (chr.getHP() > hpCost) {
                    chr.heal(-hpCost);
                }
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(FinalCut, o1);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = 3;
                tsm.putCharacterStatValue(NotDamaged, o2);
                o3.nOption = si.getValue(w, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o3);
                tsm.sendSetStatPacket();
                chr.addSkillCoolTime(skillID, si.getValue(cooltime, slv) * 1000);
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void recoverHPByLifeDrain() {
        if (chr.hasSkill(LIFE_DRAIN)) {
            Skill skill = chr.getSkill(LIFE_DRAIN);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int proc = si.getValue(prop, slv);
            int amounthealed = si.getValue(x, slv);
            if (Util.succeedProp(proc)) {
                int healamount = (int) ((chr.getMaxHP()) / ((double)100 / amounthealed));
                chr.heal(healamount);
            }
        }
    }

    @Override
    public int getFinalAttackSkill() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.getOptByCTSAndSkill(WindBreakerFinal, BLADE_CLONE) != null) {
            return BLADE_CLONE;
        }
        return 0;
    }



    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case MIRRORED_TARGET:
                if (tsm.getOptByCTSAndSkill(ShadowPartner, MIRROR_IMAGE) != null) {
                    summon = Summon.getSummonBy(chr, skillID, slv);
                    field = chr.getField();
                    summon.setFlyMob(false);
                    summon.setMoveAction((byte) 0);
                    summon.setMoveAbility(MoveAbility.Stop);
                    summon.setAssistType(AssistType.None);
                    summon.setAttackActive(false);
                    summon.setAvatarLook(chr.getAvatarData().getAvatarLook());
                    summon.setMaxHP(si.getValue(x, slv));
                    summon.setHp(summon.getMaxHP());
                    field.spawnSummon(summon);

                    tsm.removeStatsBySkill(MIRROR_IMAGE);
                }
                break;
            case BLADE_CLONE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(WindBreakerFinal, o1);
                o2.nOption = 10;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o2);
                break;
            case ASURAS_ANGER:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 10;
                tsm.putCharacterStatValue(Asura, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, InPacket inPacket, HitInfo hitInfo) {
        if (hitInfo.hpDamage <= 0) {
            giveShadowMeld();
        }
        super.handleHit(chr, inPacket, hitInfo);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}