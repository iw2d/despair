package net.swordie.ms.client.jobs.adventurer.warrior;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.Beginner;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Warrior extends Beginner {

    public static final int MAPLE_RETURN = 1281;

    private int[] addedSkills = new int[]{
            MAPLE_RETURN,
    };

    public Warrior(Char chr) {
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
        return id == JobConstants.JobEnum.WARRIOR.getJobId();
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
        }
        super.handleAttack(chr, attackInfo);
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
            case MAPLE_RETURN:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case Hero.MAGIC_CRASH_HERO:
            case Paladin.MAGIC_CRASH_PALADIN:
            case DarkKnight.MAGIC_CRASH_DRK:
                Rect rect2 = chr.getPosition().getRectAround(si.getRects().get(0));
                if (!chr.isLeft()) {
                    rect2 = rect2.moveRight();
                }
                for (Life life : chr.getField().getLifesInRect(rect2)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (Util.succeedProp(si.getValue(prop, slv))) {
                            mts.removeBuffs();
                            o1.nOption = 1;
                            o1.rOption = skillID;
                            o1.tOption = si.getValue(time, slv);
                            mts.addStatOptionsAndBroadcast(MobStat.MagicCrash, o1);
                        }
                    }
                }
                break;
            case Hero.HEROS_WILL_HERO:
            case Paladin.HEROS_WILL_PALADIN:
            case DarkKnight.HEROS_WILL_DRK:
                tsm.removeAllDebuffs();
                break;
            case Hero.WEAPON_BOOSTER_FIGHTER:
            case Paladin.WEAPON_BOOSTER_PAGE:
            case DarkKnight.WEAPON_BOOSTER_SPEARMAN:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case Hero.MAPLE_WARRIOR_HERO:
            case Paladin.MAPLE_WARRIOR_PALADIN:
            case DarkKnight.MAPLE_WARRIOR_DARK_KNIGHT:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1);
                break;
            case Hero.EPIC_ADVENTURE_HERO:
            case Paladin.EPIC_ADVENTURE_PALA:
            case DarkKnight.EPIC_ADVENTURE_DRK:
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

