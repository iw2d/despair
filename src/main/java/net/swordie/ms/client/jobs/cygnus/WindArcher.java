package net.swordie.ms.client.jobs.cygnus;

import net.swordie.ms.client.Client;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.life.Life;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.Summon;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.ChatMsgColour;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.connection.packet.CField;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;

import java.util.Arrays;
import java.util.Random;

import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.client.character.skills.SkillStat.*;

/**
 * Created on 12/14/2017.
 */
public class WindArcher extends Job {


    public static final int IMPERIAL_RECALL = 10001245;
    public static final int ELEMENTAL_EXPERT = 10000250;
    public static final int ELEMENTAL_SLASH = 10001244;
    public static final int NOBLE_MIND = 10000202;
    public static final int ELEMENTAL_SHIFT = 10001254;
    public static final int ELEMENTAL_SHIFT2 = 10000252;
    public static final int ELEMENTAL_HARMONY_DEX = 10000247;

    public static final int STORM_ELEMENTAL = 13001022; //Buff

    public static final int TRIFLING_WIND_I = 13101022; //Special Buff (Proc) (ON/OFF) //TODO
    public static final int TRIFLING_WIND_ATOM = 13100027;
    public static final int BOW_BOOSTER = 13101023; //Buff
    public static final int SYLVAN_AID = 13101024; //Buff

    public static final int TRIFLING_WIND_II = 13110022; //Special Buff Upgrade
    public static final int ALBATROSS = 13111023; //Buff //TODO new ID upon levelling the 4th Job upgrade
    public static final int EMERALD_FLOWER = 13111024; //Summon (Stationary, No Attack, Aggros)

    public static final int ALBATROSS_MAX = 13120008; //Upgrade on Albatross
    public static final int TRIFLING_WIND_III = 13120003; //Special Buff Upgrade
    public static final int SHARP_EYES = 13121005; //Buff
    public static final int TOUCH_OF_THE_WIND = 13121004; //Buff
    public static final int CALL_OF_CYGNUS_WA = 13121000; //Buff
    public static final int EMERALD_DUST = 13120007;

    public static final int GLORY_OF_THE_GUARDIANS_WA = 13121053;
    public static final int STORM_BRINGER = 13121054;

    private int[] addedSkills = new int[] {
            ELEMENTAL_HARMONY_DEX,
            IMPERIAL_RECALL,
            ELEMENTAL_EXPERT,
            ELEMENTAL_SLASH,
            NOBLE_MIND,
            ELEMENTAL_SHIFT,
            ELEMENTAL_SHIFT2
    };

    private int[] buffs = new int[] {
            STORM_ELEMENTAL,
            BOW_BOOSTER,
            SYLVAN_AID,
            ALBATROSS,
            ALBATROSS_MAX,
            EMERALD_FLOWER, //Summon
            EMERALD_DUST, //Summon Upgrade
            SHARP_EYES,
            TOUCH_OF_THE_WIND,
            CALL_OF_CYGNUS_WA,
            TRIFLING_WIND_I, //ON/OFF Skill
            GLORY_OF_THE_GUARDIANS_WA,
            STORM_BRINGER,
    };

    public WindArcher(Char chr) {
        super(chr);
        if(isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
        }
    }

    public void handleBuff(Client c, InPacket inPacket, int skillID, byte slv) {
        Char chr = c.getChr();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        TemporaryStatManager tsm = c.getChr().getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        Option o6 = new Option();
        Option o7 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case STORM_ELEMENTAL:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1); //Indie
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
                o1.tStart = (int) System.currentTimeMillis();
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
                break;
            case ALBATROSS:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieBooster, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o1); //Indie
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieCr, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o2); //Indie
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieMhp, slv);
                o3.tStart = (int) System.currentTimeMillis();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHP, o3); //Indie
                o4.nReason = skillID;
                o4.nValue = si.getValue(indiePad, slv);
                o4.tStart = (int) System.currentTimeMillis();
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o4); //Indie
                break;
            case ALBATROSS_MAX:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePad, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1); //Indie
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieDamR, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o2); //Indie
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieCr, slv);
                o3.tStart = (int) System.currentTimeMillis();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o3); //Indie
                o4.nReason = skillID;
                o4.nValue = si.getValue(indieAsrR, slv);
                o4.tStart = (int) System.currentTimeMillis();
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o4); //Indie
                o5.nReason = skillID;
                o5.nValue = si.getValue(indieAsrR, slv);
                o5.tStart = (int) System.currentTimeMillis();
                o5.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieTerR, o5); //Indie
                o6.nReason = skillID;
                o6.nValue = -2; //si.getValue(indieBooster, slv);
                o6.tStart = (int) System.currentTimeMillis();
                o6.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o6); //Indie
                o7.nOption = si.getValue(ignoreMobpdpR, slv);
                o7.rOption = skillID;
                o7.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IgnoreMobpdpR, o7);
                break;
            case SHARP_EYES: // x = crit rate    y = max crit dmg
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SharpEyes, o2);
                break;
            case TOUCH_OF_THE_WIND: // x = Dex%   avoid/acc = y
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePadR, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePADR, o1); //Indie
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMhpR, slv);
                o2.tStart = (int) System.currentTimeMillis();
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
                o5.nOption = si.getValue(y, slv);
                o5.rOption = skillID;
                o5.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EVAR, o5);
                break;
            case CALL_OF_CYGNUS_WA:
                o1.nReason = skillID;
                o1.nValue = si.getValue(x, slv);
                o1.tStart = (int) System.currentTimeMillis();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieStatR, o1); //Indie
                break;

            case TRIFLING_WIND_I:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(TriflingWhimOnOff, o1);
                break;

            case EMERALD_FLOWER:
            case EMERALD_DUST:
                summon = Summon.getSummonBy(c.getChr(), skillID, slv);
                field = c.getChr().getField();
                summon.setFlyMob(false);
                summon.setMoveAction((byte) 0);
                summon.setMoveAbility(MoveAbility.STATIC.getVal());
                summon.setAttackActive(false);
                summon.setAssistType((byte) 0);
                field.spawnSummon(summon);
                break;

            case GLORY_OF_THE_GUARDIANS_WA:
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

            case STORM_BRINGER:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(StormBringer, o1);
                break;
        }
        c.write(WvsContext.temporaryStatSet(tsm));
    }

    private void handleTriflingWind(int skillID, byte slv, AttackInfo attackInfo) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            if (tsm.hasStat(TriflingWhimOnOff)) {
                SkillInfo si = SkillData.getSkillInfoById(TRIFLING_WIND_I);
                int firstImpact = 36;
                int secondImpact = 6;
                int anglenum;
                if (new Random().nextBoolean()) {
                    anglenum = 0;
                } else {
                    anglenum = 180;
                }
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Life life = chr.getField().getLifeByObjectID(mai.mobId);
                    if (!(life instanceof Mob)) {
                        continue;
                    }
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    int TW2subprop = getSubProp(chr);
                    int TW1prop = getProp(chr);
                    if (Util.succeedProp(TW1prop)) {
                        if (Util.succeedProp(TW2subprop)) {
                            int mobID = mai.mobId;
                            int inc = ForceAtomEnum.WA_ARROW_2.getInc();
                            int type = ForceAtomEnum.WA_ARROW_2.getForceAtomType();
                            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(1, inc, firstImpact, secondImpact,
                                    anglenum, 0, (int) System.currentTimeMillis(), 1, 0,
                                    new Position(0, 0)); //Slightly behind the player
                            chr.getField().broadcastPacket(CField.createForceAtom(false, 0, chr.getId(), type,
                                    true, mobID, TRIFLING_WIND_ATOM, forceAtomInfo, new Rect(), 0, 300,
                                    mob.getPosition(), TRIFLING_WIND_ATOM, mob.getPosition()));
                        } else {
                            int mobID = mai.mobId;
                            int inc = ForceAtomEnum.WA_ARROW_1.getInc();
                            int type = ForceAtomEnum.WA_ARROW_1.getForceAtomType();
                            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(1, inc, firstImpact, secondImpact,
                                    anglenum, 0, (int) System.currentTimeMillis(), 1, 0,
                                    new Position(0, 0)); //Slightly behind the player
                            chr.getField().broadcastPacket(CField.createForceAtom(false, 0, chr.getId(), type,
                                    true, mobID, TRIFLING_WIND_ATOM, forceAtomInfo, new Rect(), 0, 300,
                                    mob.getPosition(), TRIFLING_WIND_ATOM, mob.getPosition()));
                        }
                    }
                }
            }
    }

    private void handleStormBringer(int skillID, byte slv, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(StormBringer)) {
            SkillInfo si = SkillData.getSkillInfoById(STORM_BRINGER);
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                int ranY = new Random().nextInt(150) -100;
                int hyperprop = si.getValue(prop, 1);
                if (Util.succeedProp(hyperprop)) {
                        int mobID = mai.mobId;
                        int inc = ForceAtomEnum.WA_ARROW_HYPER.getInc();
                        int type = ForceAtomEnum.WA_ARROW_HYPER.getForceAtomType();
                        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(1, inc, 5, 5,
                                270, 0, (int) System.currentTimeMillis(), 1, 0,
                                new Position(35, ranY)); //Slightly behind the player
                        chr.getField().broadcastPacket(CField.createForceAtom(false, 0, chr.getId(), type,
                                true, mobID, STORM_BRINGER, forceAtomInfo, new Rect(), 0, 300,
                                mob.getPosition(), STORM_BRINGER, mob.getPosition()));
                }
            }
        }
    }

    private int getProp(Char chr) {
        Skill skill = null;
        if (chr.hasSkill(TRIFLING_WIND_I)) {
            skill = chr.getSkill(TRIFLING_WIND_I);
        } else if (chr.hasSkill(TRIFLING_WIND_II)) {
            skill = chr.getSkill(TRIFLING_WIND_II);
        } else if (chr.hasSkill(TRIFLING_WIND_III)) {
            skill = chr.getSkill(TRIFLING_WIND_III);
        }
        return SkillData.getSkillInfoById(skill.getSkillId()).getValue(prop, skill.getCurrentLevel());
    }

    private int getSubProp(Char chr) {
        Skill skill = null;
        if (chr.hasSkill(TRIFLING_WIND_I)) {
            skill = chr.getSkill(TRIFLING_WIND_I);
        }
        if (chr.hasSkill(TRIFLING_WIND_II)) {
            skill = chr.getSkill(TRIFLING_WIND_II);
        }
        if (chr.hasSkill(TRIFLING_WIND_III)) {
            skill = chr.getSkill(TRIFLING_WIND_III);
        }
        return SkillData.getSkillInfoById(skill.getSkillId()).getValue(subProp, skill.getCurrentLevel());
    }

    private int getMaxTriffling(Char chr) {
        Skill skill = null;
        if (chr.hasSkill(TRIFLING_WIND_I)) {
            skill = chr.getSkill(TRIFLING_WIND_I);
        }
        if (chr.hasSkill(TRIFLING_WIND_II)) {
            skill = chr.getSkill(TRIFLING_WIND_II);
        }
        if (chr.hasSkill(TRIFLING_WIND_III)) {
            skill = chr.getSkill(TRIFLING_WIND_III);
        }
        return skill != null ? SkillData.getSkillInfoById(skill.getSkillId()).getValue(x, skill.getCurrentLevel())
                : 0;
    }

    public boolean isBuff(int skillID) {
        return Arrays.stream(buffs).anyMatch(b -> b == skillID);
    }

    @Override
    public void handleAttack(Client c, AttackInfo attackInfo) {
        Char chr = c.getChr();
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
        if(hasHitMobs) {
            if(attackInfo.skillId != TRIFLING_WIND_ATOM && attackInfo.skillId != 0 && attackInfo.skillId != STORM_BRINGER) {
                handleStormBringer(skillID, slv, attackInfo);

                int maxtrif = getMaxTriffling(chr);
                for (int i = 0; i < maxtrif; i++) {
                    handleTriflingWind(skillID, slv, attackInfo);

                }
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {

        }
    }

    @Override
    public void handleSkill(Client c, int skillID, byte slv, InPacket inPacket) {
        Char chr = c.getChr();
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = null;
        if(skill != null) {
            si = SkillData.getSkillInfoById(skillID);
        }
        chr.chatMessage(ChatMsgColour.YELLOW, "SkillID: " + skillID);
        if (isBuff(skillID)) {
            handleBuff(c, inPacket, skillID, slv);
        } else {
            Option o1 = new Option();
            Option o2 = new Option();
            Option o3 = new Option();
            switch(skillID) {
                case IMPERIAL_RECALL:
                    o1.nValue = si.getValue(x, slv);
                    Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
                    chr.warp(toField);
                    break;
            }
        }
    }

    @Override
    public void handleHit(Client c, InPacket inPacket, HitInfo hitInfo) {

        super.handleHit(c, inPacket, hitInfo);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isWindArcher(id);
    }

    @Override
    public int getFinalAttackSkill() {
        return 0;
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
        if (chr.getLevel() == 60) {
            chr.getQuestManager().addQuest(20880);
        }
    }
}