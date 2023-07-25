package net.swordie.ms.client.jobs.cygnus;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.connection.packet.UserLocal;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.AssistType;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.ArrayList;
import java.util.List;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class WindArcher extends Noblesse {
    public static final int STORM_ELEMENTAL = 13001022; //Buff

    public static final int TRIFLING_WIND_I = 13101022; //Special Buff (Proc) (ON/OFF)
    public static final int TRIFLING_WIND_ATOM = 13100027;
    public static final int BOW_BOOSTER = 13101023; //Buff
    public static final int BOW_MASTERY = 13100025;
    public static final int SYLVAN_AID = 13101024; //Buff

    public static final int TRIFLING_WIND_II = 13110022; //Special Buff Upgrade
    public static final int FEATHERWEIGHT = 13110025;
    public static final int ALBATROSS = 13111023; //Buff
    public static final int EMERALD_FLOWER = 13111024; //Summon (Stationary, No Attack, Aggros)
    public static final int SECOND_WIND = 13110026; //
    public static final int SENTIENT_ARROW = 13111020;
    public static final int PINPOINT_PIERCE = 13111021;

    public static final int BOW_EXPERT = 13120006;
    public static final int ALBATROSS_MAX = 13120008; //Upgrade on Albatross
    public static final int TRIFLING_WIND_III = 13120003; //Special Buff Upgrade
    public static final int SHARP_EYES = 13121005; //Buff
    public static final int TOUCH_OF_THE_WIND = 13121004; //Buff
    public static final int CALL_OF_CYGNUS_WA = 13121000; //Buff
    public static final int EMERALD_DUST = 13120007;
    public static final int SPIRALING_VORTEX = 13121002;
    public static final int SPIRALING_VORTEX_EXPLOSION = 13121009;

    public static final int TRIFLING_WIND_ENHANCE = 13120044;
    public static final int TRIFLING_WIND_DOUBLE_CHANCE = 13120045;
    public static final int GLORY_OF_THE_GUARDIANS_WA = 13121053;
    public static final int STORM_BRINGER = 13121054;
    public static final int MONSOON = 13121052;

    private int[] addedSkills = new int[] {
            IMPERIAL_RECALL,
    };

    public WindArcher(Char chr) {
        super(chr);
        if (isHandlerOfJob(chr.getJob())) {
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
        return JobConstants.isWindArcher(id);
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
            if (attackInfo.skillId != 0 && attackInfo.skillId != SENTIENT_ARROW &&
                    attackInfo.skillId != TRIFLING_WIND_ATOM && attackInfo.skillId != STORM_BRINGER) {
                createTriflingWindForceAtom(attackInfo);
                createStormBringerForceAtom(attackInfo);
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case MONSOON:
                for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
            case PINPOINT_PIERCE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.AddDamSkill, o1);
                }
                break;
            case SPIRALING_VORTEX:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    chr.getField().broadcastPacket(UserLocal.explosionAttack(SPIRALING_VORTEX_EXPLOSION, mob.getPosition(), mob.getObjectId(), 1));
                    break;
                }
        }
        super.handleAttack(chr, attackInfo);
    }

    private int getTriflingWindSkill() {
        int skillId = 0;
        if (chr.hasSkill(TRIFLING_WIND_III)) {
            skillId = TRIFLING_WIND_III;
        } else if (chr.hasSkill(TRIFLING_WIND_II)) {
            skillId = TRIFLING_WIND_II;
        } else if (chr.hasSkill(TRIFLING_WIND_I)) {
            skillId = TRIFLING_WIND_I;
        }
        return skillId;
    }

    private void createTriflingWindForceAtom(AttackInfo attackInfo) {
        int skillId = getTriflingWindSkill();
        if (skillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(TriflingWhimOnOff)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        int proc = si.getValue(prop, slv) + chr.getSkillStatValue(prop, TRIFLING_WIND_ENHANCE);
        int subProc = si.getValue(subProp, slv);
        List<ForceAtomInfo> faiList = new ArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        int mobCount = si.getValue(x, slv);
        int hitCount = chr.hasSkill(TRIFLING_WIND_DOUBLE_CHANCE) ? 2 : 1;
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob target = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (target == null || !Util.succeedProp(proc)) {
                continue;
            }
            for (int i = 0; i < hitCount; i++) {
                int firstImpact = Util.getRandom(31, 41);
                int secondImpact = 6;
                int angle = Util.getRandom(1) == 1 ? 0 : 180;
                ForceAtomEnum fae = Util.succeedProp(subProc) ? ForceAtomEnum.WA_ARROW_2 : ForceAtomEnum.WA_ARROW_1;
                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc(), firstImpact, secondImpact,
                        angle, 0, Util.getCurrentTime(), 1, 0, new Position(35, 0));
                faiList.add(forceAtomInfo);
                targetList.add(target.getObjectId());
            }
            mobCount -= hitCount;
            if (mobCount <= 0) {
                break;
            }
        }
        chr.getField().broadcastPacket(FieldPacket.createForceAtom(
                false, 0, chr.getId(), ForceAtomEnum.WA_ARROW_1.getForceAtomType(),
                true, targetList, TRIFLING_WIND_ATOM, faiList, null, 0, 0,
                null, 0, null));
    }

    private void createStormBringerForceAtom(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(StormBringer)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(STORM_BRINGER);
        int slv = chr.getSkillLevel(STORM_BRINGER);
        int proc = si.getValue(prop, slv);
        ForceAtomEnum fae = ForceAtomEnum.WA_ARROW_HYPER;
        List<ForceAtomInfo> faiList = new ArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob target = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (target == null || !Util.succeedProp(proc)) {
                continue;
            }
            int ranY = Util.getRandom(150) - 100;
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc(), 5, 5,
                    270, 0, Util.getCurrentTime(), 1, 0, new Position(35, ranY));
            faiList.add(forceAtomInfo);
            targetList.add(target.getObjectId());
        }
        if (faiList.size() > 0) {
            chr.getField().broadcastPacket(FieldPacket.createForceAtom(
                    false, 0, chr.getId(), fae.getForceAtomType(),
                    true, targetList, STORM_BRINGER, faiList, null, 0, 300,
                    null, 0, null));
        }
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
        Option o4 = new Option();
        Option o5 = new Option();
        Option o6 = new Option();
        Option o7 = new Option();
        Option o8 = new Option();
        Summon summon;
        Field field;
        switch(skillID) {
            case STORM_ELEMENTAL:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CygnusElementSkill, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieDamR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o2); //Indie
                break;
            case BOW_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case SYLVAN_AID:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1); //Indie
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o2);
                o3.nOption = 1;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SoulArrow, o3);
                o4.nOption = 1;
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NoBulletConsume, o4);
                break;
            case ALBATROSS:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieBooster, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o1); //Indie
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieCr, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o2); //Indie
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieMhp, slv);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHP, o3); //Indie
                o4.nReason = skillID;
                o4.nValue = si.getValue(indiePad, slv);
                o4.tStart = Util.getCurrentTime();
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o4); //Indie
                o5.nOption = slv;
                o5.rOption = skillID;
                o5.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Albatross, o5);
                break;
            case ALBATROSS_MAX:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1); //Indie
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieDamR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o2); //Indie
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieCr, slv);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o3); //Indie
                o4.nReason = skillID;
                o4.nValue = si.getValue(indieAsrR, slv);
                o4.tStart = Util.getCurrentTime();
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o4); //Indie
                o5.nReason = skillID;
                o5.nValue = si.getValue(indieTerR, slv);
                o5.tStart = Util.getCurrentTime();
                o5.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieTerR, o5); //Indie
                o6.nReason = skillID;
                o6.nValue = si.getValue(indieBooster, slv);
                o6.tStart = Util.getCurrentTime();
                o6.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o6); //Indie
                o7.nOption = si.getValue(ignoreMobpdpR, slv);
                o7.rOption = skillID;
                o7.tOption = si.getValue(time, slv);
                o7.bOption = 1;
                tsm.putCharacterStatValue(IgnoreMobpdpR, o7);
                o8.nOption = slv;
                o8.rOption = skillID;
                o8.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Albatross, o8);
                break;
            case SHARP_EYES:
                int cr = si.getValue(x, slv);
                o1.nOption = (cr << 8) + si.getValue(y, slv); // (cr << 8) + crDmg;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SharpEyes, o1);
                break;
            case TOUCH_OF_THE_WIND:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePadR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePADR, o1); //Indie
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMhpR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o2); //Indie
                o3.nOption = si.getValue(x, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DEXR, o3);
                o4.nOption = si.getValue(y, slv);
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ACCR, o4);
                o5.nOption = si.getValue(prop, slv);
                o5.rOption = skillID;
                o5.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EVAR, o5);
                break;
            case TRIFLING_WIND_I:
                o1.nOption = 1;
                o1.rOption = getTriflingWindSkill();
                o1.tOption = 0;
                tsm.putCharacterStatValue(TriflingWhimOnOff, o1);
                break;
            case EMERALD_FLOWER:
            case EMERALD_DUST:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setMoveAction((byte) 0);
                summon.setMoveAbility(MoveAbility.Stop);
                Position position = inPacket.decodePosition();
                summon.setCurFoothold((short) chr.getField().findFootHoldBelow(position).getId());
                summon.setPosition(position);
                summon.setAttackActive(false);
                summon.setAssistType(AssistType.None);
                summon.setMaxHP(si.getValue(x, slv));
                summon.setHp(summon.getMaxHP());
                field.spawnSummon(summon);
                applyEmeraldFlowerDebuffToMob(summon);
                break;
            case STORM_BRINGER:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(StormBringer, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    public void applyEmeraldFlowerDebuffToMob(Summon summon) {
        Position pos = summon.getPosition();
        Rect rect = new Rect( // no rect in wz
                pos.getX() - 200, pos.getY() - 200,
                pos.getX() + 200, pos.getY() + 200
        );
        int skillId = summon.getSkillID();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = summon.getSlv();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        o1.nOption = si.getValue(z, slv);
        o1.rOption = skillId;
        o1.tOption = si.getValue(time, slv);
        o2.nOption = si.getValue(w, slv); // already negative
        o2.rOption = skillId;
        o2.tOption = si.getValue(time, slv);
        o3.nOption = -si.getValue(y, slv);
        o3.rOption = skillId;
        o3.tOption = si.getValue(time, slv);
        for (Mob mob : summon.getField().getMobsInRect(rect)) {
            if (mob == null) {
                continue;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            if (skillId == EMERALD_DUST) {
                mts.addStatOptions(MobStat.PDR, o2);
                mts.addStatOptions(MobStat.MDR, o2);
                mts.addStatOptions(MobStat.PAD, o3);
                mts.addStatOptions(MobStat.MAD, o3);
            }
            mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
        }
    }

    @Override
    public void handleRemoveSkill(int skillID) {
        if (skillID == TRIFLING_WIND_I) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            tsm.removeStat(TriflingWhimOnOff, true);
            tsm.sendResetStatPacket();
        }
        super.handleRemoveSkill(skillID);
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        if (hitInfo.hpDamage == 0 && hitInfo.mpDamage == 0 && chr.hasSkill(SECOND_WIND)) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            SkillInfo si = SkillData.getSkillInfoById(SECOND_WIND);
            int slv = chr.getSkillLevel(SECOND_WIND);
            Option o1 = new Option();
            o1.nValue = si.getValue(indiePad, slv);
            o1.nReason = SECOND_WIND;
            o1.tStart = Util.getCurrentTime();
            o1.tTerm = 5; // time isn't a variable in the skill Info
            tsm.putCharacterStatValue(IndiePAD, o1);
            tsm.sendSetStatPacket();
        }
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}
