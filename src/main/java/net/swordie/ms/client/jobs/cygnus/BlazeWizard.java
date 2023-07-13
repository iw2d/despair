package net.swordie.ms.client.jobs.cygnus;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class BlazeWizard extends Noblesse {
    public static final int ORBITAL_FLAME = 12001020;
    public static final int GREATER_ORBITAL_FLAME = 12100020;
    public static final int GRAND_ORBITAL_FLAME = 12110020;
    public static final int FINAL_ORBITAL_FLAME = 12120006;

    public static final int ORBITAL_FLAME_ATOM = 12000026;
    public static final int GREATER_ORBITAL_FLAME_ATOM = 12100028;
    public static final int GRAND_ORBITAL_FLAME_ATOM = 12110028;
    public static final int FINAL_ORBITAL_FLAME_ATOM = 12120010;


    public static final int FIRE_REPULSION = 12000024;
    public static final int SPELL_CONTROL = 12100027;
    public static final int IGNITION = 12101024; //Buff
    public static final int IGNITION_EXPLOSION = 12100029; // Explosion Attack
    public static final int FLASHFIRE = 12101025; //Special Skill
    public static final int WORD_OF_FIRE = 12101023; //Buff
    public static final int CONTROLLED_BURN = 12101022; //Special Skill

    public static final int CINDER_MAELSTROM = 12111022; //Special Skill
    public static final int PHOENIX_RUN = 12111023; //Special Buff
    public static final int PHOENIX_RUN_EFFECTS = 12111029;

    public static final int PURE_MAGIC = 12120009;
    public static final int TOWERING_INFERNO = 12121002;
    public static final int BURNING_CONDUIT = 12121005;
    public static final int FIRES_OF_CREATION = 12121004; //only used for visual cooldown
    public static final int FIRES_OF_CREATION_FOX = 12120014; //Buff
    public static final int FIRES_OF_CREATION_LION = 12120013; //Buff
    public static final int FLAME_BARRIER = 12121003; //Buff
    public static final int CALL_OF_CYGNUS_BW = 12121000; //Buff
    public static final int ORBITAL_FLAME_RANGE = 12121043; // Buff - toggle

    public static final int CATACLYSM = 12121052;
    public static final int GLORY_OF_THE_GUARDIANS_BW = 12121053;
    public static final int DRAGON_BLAZE = 12121054;
    public static final int DRAGON_BLAZE_FINAL = 12121055;

    public static final int FLAME_ELEMENT = 12000022;
    public static final int GREATER_FLAME_ELEMENT = 12100026;
    public static final int GRAND_FLAME_ELEMENT = 12110024;
    public static final int FINAL_FLAME_ELEMENT = 12120007;

    private int[] addedSkills = new int[] {
            IMPERIAL_RECALL
    };

    private FlashFire flashFire;


    public BlazeWizard(Char chr) {
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
        return JobConstants.isBlazeWizard(id);
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
            if (attackInfo.skillId != IGNITION_EXPLOSION) {
                applyIgnition(attackInfo);
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Summon summon;
        switch (attackInfo.skillId) {
            case ORBITAL_FLAME_ATOM:
            case GREATER_ORBITAL_FLAME_ATOM:
            case GRAND_ORBITAL_FLAME_ATOM:
            case FINAL_ORBITAL_FLAME_ATOM:
                summonFlameElement();
                break;
            case CATACLYSM:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 5;
                tsm.putCharacterStatValue(NotDamaged, o1);
                tsm.sendSetStatPacket();
                break;
            case DRAGON_BLAZE_FINAL:
                chr.setSkillCooldown(DRAGON_BLAZE, slv);
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void applyIgnition(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(WizardIgnite)) {
            SkillInfo si = SkillData.getSkillInfoById(IGNITION);
            int slv = chr.getSkillLevel(IGNITION);
            Option o1 = new Option();
            Option o2 = new Option();
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                MobTemporaryStat mts = mob.getTemporaryStat();
                if (Util.succeedProp(si.getValue(prop, slv))) {
                    if (tsm.hasStatBySkillId(FIRES_OF_CREATION_FOX) || tsm.hasStatBySkillId(FIRES_OF_CREATION_LION)) {
                        o1.nOption = chr.getSkillStatValue(z, FIRES_OF_CREATION);
                        o1.rOption = FIRES_OF_CREATION;
                        o1.tOption = si.getValue(dotTime, slv);
                        mts.addStatOptions(MobStat.ElementResetBySummon, o1);
                    }
                    o2.nOption = chr.getSkillStatValue(x, TOWERING_INFERNO);
                    o2.rOption = IGNITION;
                    o2.tOption = si.getValue(dotTime, slv);
                    mts.addStatOptions(MobStat.Ember, o2);
                    mts.createAndAddBurnedInfo(chr, IGNITION, slv);
                    EventManager.addEvent(() -> explodeIgnition(mob), si.getValue(dotTime, slv), TimeUnit.SECONDS);
                }
            }
        }
    }

    private void explodeIgnition(Mob mob) {
        Life checkMob = chr.getField().getLifeByObjectID(mob.getObjectId());
        if (checkMob instanceof Mob) {
            chr.write(UserLocal.explosionAttack(IGNITION_EXPLOSION, mob.getPosition(), mob.getObjectId(), 8));
        }
    }

    private void summonFlameElement() {
        int skillId = 0;
        if (chr.hasSkill(FINAL_FLAME_ELEMENT)) {
            skillId = FINAL_FLAME_ELEMENT;
        } else if (chr.hasSkill(GRAND_FLAME_ELEMENT)) {
            skillId = GRAND_FLAME_ELEMENT;
        } else if (chr.hasSkill(GREATER_FLAME_ELEMENT)) {
            skillId = GREATER_FLAME_ELEMENT;
        } else if (chr.hasSkill(FLAME_ELEMENT)) {
            skillId = FLAME_ELEMENT;
        }
        if (skillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        // create/refresh summon
        Summon summon = null;
        if (tsm.hasStatBySkillId(skillId)) {
            summon = tsm.getOptByCTSAndSkill(IndieEmpty, skillId).summon;
        }
        if (summon == null) {
            summon = new Summon(skillId);
            summon.setChr(chr);
            summon.setSkillID(skillId);
            summon.setSlv((byte) slv);
            summon.setSummonTerm(chr.getJobHandler().getBuffedSkillDuration(si.getValue(time, slv)));
            summon.setCharLevel((byte) chr.getStat(Stat.level));
            summon.setPosition(chr.getPosition().deepCopy());
            summon.setMoveAction((byte) 1);
            summon.setCurFoothold((short) chr.getField().findFootHoldBelow(summon.getPosition()).getId());
            summon.setMoveAbility(MoveAbility.Walk);
            summon.setEnterType(EnterType.Animation);
            summon.setFlyMob(false);
            summon.setAttackActive(false);
            summon.setAssistType(AssistType.None);
            chr.getField().spawnSummon(summon);
        } else {
            summon.setSummonTerm(chr.getJobHandler().getBuffedSkillDuration(si.getValue(time, slv)));
        }
        // set stat
        Option o1 = new Option();
        Option o2 = new Option();
        o1.nReason = skillId;
        o1.nValue = 1;
        o1.summon = summon;
        o1.tStart = Util.getCurrentTime();
        o1.tTerm = si.getValue(time, slv);
        tsm.putCharacterStatValue(IndieEmpty, o1);
        o2.nOption = si.getValue(x, slv);
        o2.rOption = skillId;
        o2.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(MAD, o2);
        tsm.sendSetStatPacket();
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
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
        switch(skillID) {
            case FLASHFIRE:
                if (flashFire != null && flashFire.getFieldId() == chr.getFieldID() && tsm.hasStatBySkillId(skillID)) {
                    // clear blink + teleport
                    flashFire.blink();
                    flashFire = null;
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    // set blink
                    flashFire = new FlashFire(chr, inPacket.decodePosition());
                    o1.nValue = 1;
                    o1.nReason = skillID;
                    o1.tStart = Util.getCurrentTime();
                    o1.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieEmpty, o1);
                }
                break;
            case CONTROLLED_BURN:
                chr.healMP((int) (chr.getMaxMP() / (100D / si.getValue(x, slv))));
                break;
            case IMPERIAL_RECALL:
                o1.nValue = si.getValue(x, slv);
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                chr.warp(toField);
                break;
            case BURNING_CONDUIT:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                aa.setDelay((short) 15);
                chr.getField().spawnAffectedArea(aa);
                break;
            case WORD_OF_FIRE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case FLAME_BARRIER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamageReduce, o1);
                break;
            case CALL_OF_CYGNUS_BW:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1); //Indie
                break;
            case IGNITION:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(WizardIgnite, o1);
                break;
            case FIRES_OF_CREATION_FOX:
            case FIRES_OF_CREATION_LION:
                handleFiresOfCreation(skillID);
                break;
            case CINDER_MAELSTROM:  //Special Summon
                summon = Summon.getSummonBy(chr, skillID, slv);
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                chr.getField().spawnSummon(summon);
                break;
            case PHOENIX_RUN:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ReviveOnce, o1);
                break;
            case GLORY_OF_THE_GUARDIANS_BW:
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
            case ORBITAL_FLAME_RANGE:
                if (tsm.hasStat(AddRangeOnOff)) {
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = si.getValue(range, slv);
                    o1.rOption = skillID;
                    o1.tOption = 0;
                    tsm.putCharacterStatValue(AddRangeOnOff, o1);
                }
                break;
        }
        tsm.sendSetStatPacket();
    }

    public void handleFiresOfCreation(int skillId) {
        if (chr.hasSkillOnCooldown(FIRES_OF_CREATION)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(FIRES_OF_CREATION);
        int slv = chr.getSkillLevel(FIRES_OF_CREATION);
        if (tsm.hasStatBySkillId(FIRES_OF_CREATION_FOX) || tsm.hasStatBySkillId(FIRES_OF_CREATION_LION)) {
            tsm.removeStatsBySkill(FIRES_OF_CREATION_FOX);
            tsm.removeStatsBySkill(FIRES_OF_CREATION_LION);
            tsm.sendResetStatPacket();
        }

        Summon summon = new Summon(skillId);
        summon.setChr(chr);
        summon.setSkillID(skillId);
        summon.setSlv((byte) slv);
        summon.setSummonTerm(chr.getJobHandler().getBuffedSkillDuration(si.getValue(time, slv)));
        summon.setCharLevel((byte) chr.getStat(Stat.level));
        summon.setPosition(chr.getPosition().deepCopy());
        summon.setMoveAction((byte) 1);
        summon.setCurFoothold((short) chr.getField().findFootHoldBelow(summon.getPosition()).getId());
        summon.setMoveAbility(MoveAbility.Walk);
        summon.setEnterType(EnterType.Animation);
        summon.setFlyMob(skillId == FIRES_OF_CREATION_FOX);
        summon.setAttackActive(false);
        summon.setAssistType(AssistType.None);
        chr.getField().spawnSummon(summon);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        o1.nReason = skillId;
        o1.nValue = 1;
        o1.summon = summon;
        o1.tStart = Util.getCurrentTime();
        o1.tTerm = si.getValue(time, slv);
        tsm.putCharacterStatValue(IndieEmpty, o1);
        o2.nOption = si.getValue(v, slv);
        o2.rOption = skillId;
        o2.tOption = si.getValue(time, slv);
        // tsm.putCharacterStatValue(); // mpConIncrease?
        o3.nReason = skillId;
        o3.nValue = si.getValue(y, slv);
        o3.tStart = Util.getCurrentTime();
        o3.tTerm = si.getValue(time, slv);
        tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o3);
        tsm.sendSetStatPacket();
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {

        super.handleHit(chr, hitInfo);
    }

    public void reviveByPhoenixRun() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        Skill skill = chr.getSkill(PHOENIX_RUN);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();

        chr.heal(chr.getMaxHP() / 2); // 50%
        tsm.removeStatsBySkill(PHOENIX_RUN);
        tsm.sendResetStatPacket();
        chr.chatMessage("You have been revived by Phoenix Run.");

        Position position = chr.getPosition();
        chr.write(FieldPacket.teleport(new Position(position.getX() + (chr.isLeft() ? + 350 : - 350), position.getY()), chr));

        // Hit effect
        chr.write(UserPacket.effect(Effect.skillUse(PHOENIX_RUN_EFFECTS, slv, 0)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(PHOENIX_RUN_EFFECTS, slv, 0)));

        // Backstep effect
        chr.write(UserPacket.effect(Effect.skillAffected(PHOENIX_RUN_EFFECTS, slv, 0)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffected(PHOENIX_RUN_EFFECTS, slv, 0)));

        o.nOption = 1;
        o.rOption = PHOENIX_RUN;
        o.tOption = si.getValue(x, slv); // duration
        tsm.putCharacterStatValue(NotDamaged, o);
        tsm.sendSetStatPacket();
    }


    private class FlashFire {
        private Char chr;
        private Position position;
        private int fieldId;

        public FlashFire(Char chr, Position position) {
            this.chr = chr;
            this.fieldId = chr.getFieldID();
            this.position = position;
            chr.write(WvsContext.flameWizardFlareBlink(chr, position, false));
        }

        public int getFieldId() {
            return fieldId;
        }

        public void blink() {
            chr.write(WvsContext.flameWizardFlareBlink(chr, position, true));
        }
    }
}
