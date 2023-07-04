package net.swordie.ms.client.jobs.adventurer.thief;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.Beginner;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Thief extends Beginner {
    public static final int MAPLE_RETURN = 1281;

    public static final int HASTE = 4001005; //Buff
    public static final int DARK_SIGHT = 4001003; //Buff

    private int[] addedSkills = new int[] {
            MAPLE_RETURN,
    };

    public Thief(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob()) && chr.getSubJob() == 0) {
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
        return id == JobConstants.JobEnum.THIEF.getJobId();
    }

    private void procAdvDarkSight() {
        int skillId = 0;
        if (chr.hasSkill(Shadower.ADVANCED_DARK_SIGHT_SHAD)) {
            skillId = Shadower.ADVANCED_DARK_SIGHT_SHAD;
        } else if (chr.hasSkill(DualBlade.ADVANCED_DARK_SIGHT_DB)) {
            skillId = DualBlade.ADVANCED_DARK_SIGHT_DB;
        }
        if (skillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        int proc = si.getValue(x, slv);
        int maintainProc = si.getValue(prop, slv);
        if (tsm.hasStat(DarkSight) && !(Util.succeedProp(maintainProc) || tsm.hasStatBySkillId(Shadower.SMOKE_SCREEN))) {
            tsm.removeStat(DarkSight, true);
        } else if (Util.succeedProp(proc)) {
            handleSkill(chr, DARK_SIGHT, chr.getSkillLevel(DARK_SIGHT), null);
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
        if(hasHitMobs) {
            applyPassiveDoTSkillsOnMob(attackInfo);
            procAdvDarkSight();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case DualBlade.SUDDEN_RAID_DB:
                chr.reduceSkillCoolTime(DualBlade.FINAL_CUT, (long) (chr.getRemainingCoolTime(DualBlade.FINAL_CUT) * 0.2F));
                // Fallthrough intended
            case NightLord.SUDDEN_RAID_NL:
            case Shadower.SUDDEN_RAID_SHAD:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void applyPassiveDoTSkillsOnMob(AttackInfo attackInfo) {
        int skillId = 0;
        if (chr.hasSkill(NightLord.TOXIC_VENOM_NL)) {
            skillId = NightLord.TOXIC_VENOM_NL;
        } else if (chr.hasSkill(NightLord.VENOM_NL)) {
            skillId = NightLord.VENOM_NL;
        } else if (chr.hasSkill(Shadower.TOXIC_VENOM_SHAD)) {
            skillId = Shadower.TOXIC_VENOM_SHAD;
        } else if (chr.hasSkill(Shadower.VENOM_SHAD)) {
            skillId = Shadower.VENOM_SHAD;
        } else if (chr.hasSkill(DualBlade.TOXIC_VENOM_DB)) {
            skillId = DualBlade.TOXIC_VENOM_DB;
        } else if (chr.hasSkill(DualBlade.VENOM_DB)) {
            skillId = DualBlade.VENOM_DB;
        }
        if (skillId > 0) {
            SkillInfo si = SkillData.getSkillInfoById(skillId);
            int slv = chr.getSkillLevel(skillId);
            int proc = si.getValue(prop, slv);
            int maxStacks = si.getValue(dotSuperpos, slv);
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if(mob == null) {
                    continue;
                }
                if (Util.succeedProp(proc)) {
                    mob.getTemporaryStat().createAndAddBurnedInfo(chr, skillId, slv, maxStacks > 0 ? maxStacks : 1);
                }
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
        Summon summon;
        Field field;
        switch (skillID) {
            case MAPLE_RETURN:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case HASTE:
            case DualBlade.SELF_HASTE:
                o1.nOption = si.getValue(speed, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Speed, o1);
                o2.nOption = si.getValue(jump, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Jump, o2);
                break;
            case DARK_SIGHT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DarkSight, o1); // TODO: test/implement cooldown increase on dodging hp% attack
                break;
            case NightLord.CLAW_BOOSTER:
            case Shadower.DAGGER_BOOSTER:
            case DualBlade.KATARA_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case NightLord.SHADOW_PARTNER_NL:
            case Shadower.SHADOW_PARTNER_SHAD:
            case DualBlade.MIRROR_IMAGE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ShadowPartner, o1);
                break;
            case NightLord.MAPLE_WARRIOR_NL:
            case Shadower.MAPLE_WARRIOR_SHAD:
            case DualBlade.MAPLE_WARRIOR_DB:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1); //Indie
                break;
            case NightLord.DARK_FLARE_NL:
            case Shadower.DARK_FLARE_SHAD:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setMoveAction((byte) 0);
                summon.setMoveAbility(MoveAbility.Stop);
                field.spawnSummon(summon);
                break;
            case NightLord.EPIC_ADVENTURE_NL:
            case Shadower.EPIC_ADVENTURE_SHAD:
            case DualBlade.EPIC_ADVENTURE_DB:
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
            case NightLord.HEROS_WILL_NL:
            case Shadower.HEROS_WILL_SHAD:
            case DualBlade.HEROS_WILL_DB:
                tsm.removeAllDebuffs();
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}