package net.swordie.ms.client.jobs.resistance;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.quest.Quest;
import net.swordie.ms.client.character.quest.QuestManager;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatBase;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.QuestConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Life;
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
import java.util.Arrays;
import java.util.List;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class WildHunter extends Citizen {

    //Jaguar Summon
    public static final int SUMMON_JAGUAR_GREY = 33001007;           //No Special Jaguar Stats
    public static final int SUMMON_JAGUAR_YELLOW = 33001008;         //No Special Jaguar Stats
    public static final int SUMMON_JAGUAR_RED = 33001009;            //No Special Jaguar Stats
    public static final int SUMMON_JAGUAR_PURPLE = 33001010;         //No Special Jaguar Stats
    public static final int SUMMON_JAGUAR_BLUE = 33001011;           //No Special Jaguar Stats
    public static final int SUMMON_JAGUAR_JAIRA = 33001012;          //Critical Rate +5%
    public static final int SUMMON_JAGUAR_SNOW_WHITE = 33001013;     //Buff Duration +10%
    public static final int SUMMON_JAGUAR_ONYX = 33001014;           //Buff Duration +10%
    public static final int SUMMON_JAGUAR_CRIMSON = 33001015;        //Dmg Absorption +10%
    public static final int[] SUMMONS = new int[]{
            SUMMON_JAGUAR_GREY,
            SUMMON_JAGUAR_YELLOW,
            SUMMON_JAGUAR_RED,
            SUMMON_JAGUAR_PURPLE,
            SUMMON_JAGUAR_BLUE,
            SUMMON_JAGUAR_JAIRA,
            SUMMON_JAGUAR_SNOW_WHITE,
            SUMMON_JAGUAR_ONYX,
            SUMMON_JAGUAR_CRIMSON
    };

    //Jaguar Mount
    public static final int MOUNT_JAGUAR_GREY = 1932015;
    public static final int MOUNT_JAGUAR_YELLOW = 1932030;
    public static final int MOUNT_JAGUAR_RED = 1932031;
    public static final int MOUNT_JAGUAR_PURPLE = 1932032;
    public static final int MOUNT_JAGUAR_BLUE = 1932033;
    public static final int MOUNT_JAGUAR_JAIRA = 1932036;
    public static final int MOUNT_JAGUAR_SNOW_WHITE = 1932100;
    public static final int MOUNT_JAGUAR_ONYX = 1932149;
    public static final int MOUNT_JAGUAR_CRIMSON = 1932215;
    public static final int[] MOUNTS = new int[]{
            MOUNT_JAGUAR_GREY,
            MOUNT_JAGUAR_YELLOW,
            MOUNT_JAGUAR_RED,
            MOUNT_JAGUAR_PURPLE,
            MOUNT_JAGUAR_BLUE,
            MOUNT_JAGUAR_JAIRA,
            MOUNT_JAGUAR_SNOW_WHITE,
            MOUNT_JAGUAR_ONYX,
            MOUNT_JAGUAR_CRIMSON
    };

    public static final int CAPTURE = 30001061;
    public static final int CALL_OF_THE_HUNTER = 30001062;

    public static final int RIDE_JAGUAR = 33001001; //Special Buff
    public static final int SWIPE = 33001016 ; //Special Attack (Bite Debuff)
    public static final int WILD_LURE = 33001025 ;
    public static final int ANOTHER_BITE = 33000036;

    public static final int SOUL_ARROW_CROSSBOW = 33101003; //Buff
    public static final int CROSSBOW_BOOSTER = 33101012; //Buff
    public static final int CALL_OF_THE_WILD = 33101005; //Buff
    public static final int DASH_N_SLASH_JAGUAR_SUMMONED = 33101115; //Special Attack (Stun Debuff) + (Bite Debuff)
    public static final int DASH_N_SLASH_JAGUAR_ON = 33101215; //Special Attack (Stun Debuff)

    public static final int JAGUAR_LINK = 33110014;
    public static final int FELINE_BERSERK = 33111007; //Buff
    public static final int BACKSTEP = 33111011; //Special Buff (ON/OFF)
    public static final int HUNTING_ASSISTANT_UNIT = 33111013; //Area of Effect
    public static final int SONIC_ROAR = 33111015; //Special Attack (Bite Debuff)
    public static final int FLURRY = 33110008; //Dodge

    public static final int CROSSBOW_EXPERT = 33120000;
    public static final int JAGUAR_SOUL = 33121017; //Special Attack (Stun Debuff) + (Bite Debuff) + (Magic Crash Debuff)
    public static final int DRILL_SALVO = 33121016; //Summon
    public static final int SHARP_EYES = 33121004; //Buff
    public static final int MAPLE_WARRIOR_WH = 33121007; //Buff
    public static final int HEROS_WILL_WH = 33121008;

    //Final Attack
    public static final int FINAL_ATTACK_WH = 33100009;
    public static final int ADVANCED_FINAL_ATTACK_WH = 33120011;

    public static final int FELINE_BERSERK_REINFORCE = 33120043;
    public static final int FELINE_BERSERK_VITALITY = 33120044;
    public static final int FELINE_BERSERK_RAPID = 33120045;
    public static final int FOR_LIBERTY_WH = 33121053;
    public static final int SILENT_RAMPAGE = 33121054;
    public static final int EXPLODING_ARROWS = 33121155;
    public static final int JAGUAR_RAMPAGE = 33121255;


    public WildHunter(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            if (chr.getWildHunterInfo() == null) {
                chr.setWildHunterInfo(new WildHunterInfo());
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isWildHunter(id);
    }

    public void handleJaguarWings(boolean toggle) {
        QuestManager qm = chr.getQuestManager();
        Quest wingQuest = qm.getQuestById(QuestConstants.WILD_HUNTER_JAGUAR_WING_OFF);
        if (wingQuest == null) {
            wingQuest = new Quest(QuestConstants.WILD_HUNTER_JAGUAR_WING_OFF, QuestStatus.Started);
            qm.addQuest(wingQuest);
        }
        if (toggle) {
            if (wingQuest.getQRValue().isEmpty()) {
                wingQuest.setQrValue("1");
            } else {
                wingQuest.setQrValue("");
            }
        }
        chr.write(WvsContext.questRecordExMessage(wingQuest));
        chr.getField().broadcastPacket(UserPacket.beastFormWingOnOff(chr.getId(), wingQuest.getQRValue().isEmpty()));
    }

    private Summon getJaguar() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        for (Option option : tsm.getIndieOptions(IndieEmpty)) {
            if (Arrays.stream(SUMMONS).anyMatch(summonSkillId -> summonSkillId == option.nReason)) {
                return option.summon;
            }
        }
        return null;
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
            handleAnotherBiteFA(attackInfo);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case SUMMON_JAGUAR_GREY:
            case SUMMON_JAGUAR_YELLOW:
            case SUMMON_JAGUAR_RED:
            case SUMMON_JAGUAR_PURPLE:
            case SUMMON_JAGUAR_BLUE:
            case SUMMON_JAGUAR_JAIRA:
            case SUMMON_JAGUAR_SNOW_WHITE:
            case SUMMON_JAGUAR_ONYX:
            case SUMMON_JAGUAR_CRIMSON:
                skillID = attackInfo.jaguarSkillId != 0 ? attackInfo.jaguarSkillId : SUMMON_JAGUAR_GREY;
                si = SkillData.getSkillInfoById(skillID);
                slv = 1;
                if (skillID == JAGUAR_RAMPAGE) {
                    chr.setSkillCooldown(EXPLODING_ARROWS, slv);
                }
                switch (skillID) {
                    case SWIPE: // heal
                        if (chr.getHP() > 0) {
                            int healRate = si.getValue(q, slv);
                            chr.heal((int) (chr.getMaxHP() * ((double) healRate / 100D)), false);
                        }
                        // Fallthrough intended
                    case DASH_N_SLASH_JAGUAR_SUMMONED: // stun
                    case SONIC_ROAR:
                    case JAGUAR_SOUL: // bind
                    case JAGUAR_RAMPAGE:
                    case SUMMON_JAGUAR_GREY: // basic attack
                        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                            if (mob == null) {
                                continue;
                            }
                            MobTemporaryStat mts = mob.getTemporaryStat();
                            // apply another bite debuff
                            if (Util.succeedProp(si.getValue(prop, slv))) {
                                int stacks = 0;
                                if (mts.hasStat(MobStat.JaguarBleeding)) {
                                    stacks = mts.getOption(MobStat.JaguarBleeding).nOption;
                                }
                                o1.nOption = Math.min(stacks + 1, 3); // chr.getSkillStatValue(x, SUMMON_JAGUAR_GREY);
                                o1.rOption = ANOTHER_BITE;
                                o1.tOption = chr.getSkillStatValue(time, SUMMON_JAGUAR_GREY);
                                mts.addStatOptions(MobStat.JaguarBleeding, o1);
                            }
                            // handle skill-specific effects
                            if (skillID == DASH_N_SLASH_JAGUAR_SUMMONED) {
                                if (!mob.isBoss() && Util.succeedProp(si.getValue(subProp, slv))) {
                                    o2.nOption = 1;
                                    o2.rOption = skillID;
                                    o2.tOption = si.getValue(time, slv);
                                    mts.addStatOptions(MobStat.Stun, o2);
                                }
                            } else if (skillID == JAGUAR_SOUL) {
                                mts.removeBuffs();
                                if (Util.succeedProp(si.getValue(subProp, slv))) {
                                    o2.nOption = 1;
                                    o2.rOption = skillID;
                                    o2.tOption = si.getValue(time, slv);
                                    mts.addStatOptions(MobStat.Stun, o2);
                                }
                            }
                            // broadcast
                            mts.sendSetStatPacket();
                        }
                        break;
                }
                break;
            case DASH_N_SLASH_JAGUAR_ON:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void handleAnotherBiteFA(AttackInfo attackInfo) {
        if (!SkillConstants.isWildHunterShotSkill(attackInfo.skillId)) {
            return;
        }
        List<Integer> targetList = new ArrayList<>();
        int attackCount = 1;
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            if (mts.hasStat(MobStat.JaguarBleeding)) {
                int stacks = mts.getOption(MobStat.JaguarBleeding).nOption;
                if (stacks > 0) {
                    targetList.add(mob.getObjectId());
                    if (stacks > attackCount) {
                        attackCount = stacks;
                    }
                }
            }
        }
        if (targetList.size() > 0) {
            chr.write(UserLocal.userBonusAttackRequest(ANOTHER_BITE, targetList, attackCount));
        }
    }

    @Override
    public int getFinalAttackSkill() {
        int skillId = 0;
        if (chr.hasSkill(ADVANCED_FINAL_ATTACK_WH)) {
            skillId = ADVANCED_FINAL_ATTACK_WH;
        } else if (chr.hasSkill(FINAL_ATTACK_WH)) {
            skillId = FINAL_ATTACK_WH;
        }
        if (skillId > 0) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            SkillInfo si = SkillData.getSkillInfoById(skillId);
            int slv = chr.getSkillLevel(skillId);
            if (tsm.hasStatBySkillId(SILENT_RAMPAGE) || Util.succeedProp(si.getValue(prop, slv))) {
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
        Option o4 = new Option();
        AffectedArea aa;
        Rect rect;
        Summon summon;
        Field field;
        boolean isLeft;
        switch (skillID) {
            case WILD_LURE:
                Position pos = inPacket.decodePosition();
                isLeft = inPacket.decodeByte() != 0;
                rect = pos.getRectAround(si.getFirstRect());
                if (!isLeft) {
                    rect = rect.horizontalFlipAround(pos.getX());
                }
                Summon jaguar = getJaguar();
                if (jaguar == null) {
                    return;
                }
                // boss effect
                o1.nOption = jaguar.getObjectId();
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) / 2;
                // normal effect
                o2.nOption = jaguar.getObjectId();
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                int count = si.getValue(mobCount, slv);
                for (Life life : chr.getField().getLifesInRect(rect)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.JaguarProvoke, mob.isBoss() ? o1 : o2);
                        count--;
                        if (count <= 0) {
                            break;
                        }
                    }
                }
                // Fallthrough intended
            case SWIPE:
            case DASH_N_SLASH_JAGUAR_SUMMONED:
            case SONIC_ROAR:
            case JAGUAR_SOUL:
            case JAGUAR_RAMPAGE:
                chr.write(UserLocal.jaguarSkill(skillID));
                break;
            case CAPTURE:
                int mobID = inPacket.decodeInt();
                Life life = chr.getField().getLifeByObjectID(mobID);
                if (life instanceof Mob) {
                    Mob mob = (Mob) life;
                    if (mob.getMaxHp() * 0.90 <= mob.getHp()) {
                        chr.write(UserPacket.effect(Effect.showCaptureEffect(skillID, slv, 0, 1)));
                        return;
                    }
                    Quest quest = chr.getQuestManager().getQuestById(QuestConstants.WILD_HUNTER_JAGUAR_STORAGE_ID);
                    if (quest == null) {
                        quest = new Quest(QuestConstants.WILD_HUNTER_JAGUAR_STORAGE_ID, QuestStatus.Started);
                        chr.getQuestManager().addQuest(quest);
                    }
                    String key = QuestConstants.getWhStorageQuestValByTemplateID(mob.getTemplateId());
                    if (key != null) {
                        quest.setProperty(key, "1");
                        chr.write(WvsContext.questRecordExMessage(quest));
                        chr.write(UserPacket.effect(Effect.showCaptureEffect(skillID, slv, 0, 0)));
                        mob.die(false);
                        handleJaguarLink();
                    } else {
                        chr.write(UserPacket.effect(Effect.showCaptureEffect(skillID, slv, 0, 2)));
                    }
                }
                break;
            case SUMMON_JAGUAR_GREY:
            case SUMMON_JAGUAR_YELLOW:
            case SUMMON_JAGUAR_RED:
            case SUMMON_JAGUAR_PURPLE:
            case SUMMON_JAGUAR_BLUE:
            case SUMMON_JAGUAR_JAIRA:
            case SUMMON_JAGUAR_SNOW_WHITE:
            case SUMMON_JAGUAR_ONYX:
            case SUMMON_JAGUAR_CRIMSON:
                if (chr.getWildHunterInfo() == null
                        || chr.getWildHunterInfo().getIdx() < 0
                        || chr.getWildHunterInfo().getIdx() >= MOUNTS.length) {
                    chr.chatMessage("You haven't selected a jaguar.");
                    return;
                }
                if (tsm.hasStatBySkillId(RIDE_JAGUAR)) {
                    tsm.removeStatsBySkill(RIDE_JAGUAR);
                    tsm.sendResetStatPacket();
                }

                summon = new Summon(skillID);
                summon.setChr(chr);
                summon.setSkillID(skillID);
                summon.setSlv((byte) slv);
                summon.setCharLevel((byte) chr.getStat(Stat.level));
                summon.setPosition(chr.getPosition().deepCopy());
                summon.setMoveAction((byte) 1);
                summon.setCurFoothold((short) chr.getField().findFootholdBelow(summon.getPosition()).getId());
                summon.setEnterType(EnterType.Animation);
                summon.setFlyMob(false);
                summon.setSummonTerm(0);
                summon.setMoveAbility(MoveAbility.Jaguar);
                summon.setAssistType(AssistType.Jaguar);
                summon.setAttackActive(true);
                summon.setBeforeFirstAttack(true);
                summon.setJaguarActive(true);
                chr.getField().spawnSummon(summon);

                o1.nValue = 1;
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = 0;
                o1.summon = summon;
                tsm.putCharacterStatValue(IndieEmpty, o1);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = 0;
                tsm.putCharacterStatValue(JaguarSummoned, o2);
                break;
            case RIDE_JAGUAR:
                if (chr.getWildHunterInfo() == null
                        || chr.getWildHunterInfo().getIdx() < 0
                        || chr.getWildHunterInfo().getIdx() >= MOUNTS.length) {
                    chr.chatMessage("You haven't selected a jaguar.");
                    return;
                }
                if (tsm.hasStat(JaguarSummoned)) {
                    for (int summonSkillId : SUMMONS) {
                        tsm.removeStatsBySkill(summonSkillId);
                    }
                    tsm.sendResetStatPacket();
                }
                TemporaryStatBase tsb = tsm.getTSBByTSIndex(TSIndex.RideVehicle);
                if (tsm.hasStat(RideVehicle)) {
                    tsm.removeStat(RideVehicle, false);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = MOUNTS[chr.getWildHunterInfo().getIdx()];
                    o1.rOption = skillID;
                    tsb.setOption(o1);
                    tsm.putCharacterStatValue(RideVehicle, tsb.getOption());
                    tsm.sendSetStatPacket();
                }
                break;
            case SOUL_ARROW_CROSSBOW:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SoulArrow, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indiePad, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o2);
                break;
            case CROSSBOW_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case CALL_OF_THE_WILD:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(HowlingDefence, o1);
                tsm.putCharacterStatValue(HowlingEvasion, o1);
                tsm.putCharacterStatValue(HowlingMaxMP, o1);
                o2.nOption = si.getValue(z, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(HowlingAttackDamage, o2);
                break;
            case FELINE_BERSERK:
                o1.nValue = si.getValue(indieBooster, slv) - this.chr.getSkillStatValue(w, FELINE_BERSERK_RAPID);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o1);
                o2.nOption = si.getValue(z, slv) + this.chr.getSkillStatValue(z, FELINE_BERSERK_REINFORCE);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BeastFormDamageUp, o2);
                o3.nOption = si.getValue(x, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Speed, o3);
                if (this.chr.hasSkill(FELINE_BERSERK_VITALITY)) {
                    o4.nOption = chr.getSkillStatValue(mhpR, FELINE_BERSERK_VITALITY);
                    o4.rOption = skillID;
                    o4.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(BeastFormMaxHP, o4);
                }
                break;
            case BACKSTEP:
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(DrawBack, o1);
                break;
            case SHARP_EYES:
                o1.nOption = (si.getValue(x, slv) << 8) + si.getValue(y, slv); // (cr << 8) + crDmg;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(SharpEyes, o1);
                break;
            case SILENT_RAMPAGE:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o1);
                break;
            case HUNTING_ASSISTANT_UNIT:
            case DRILL_SALVO:
                aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(inPacket.decodePosition());
                rect = aa.getPosition().getRectAround(si.getFirstRect());
                inPacket.decodeShort(); // unk
                inPacket.decodeByte(); // unk
                isLeft = inPacket.decodeByte() != 0;
                if (!isLeft) {
                    rect = rect.horizontalFlipAround(aa.getPosition().getX());
                }
                aa.setRect(rect);
                aa.setFlip(!isLeft);
                aa.setDelay((short) 4);
                chr.getField().spawnAffectedAreaAndRemoveOld(aa);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void handleJaguarLink() {
        if (!chr.hasSkill(JAGUAR_LINK)) {
            return;
        }
        Quest quest = chr.getQuestManager().getQuestById(QuestConstants.WILD_HUNTER_JAGUAR_STORAGE_ID);
        if (quest == null) {
            return;
        }
        quest.convertQRValueToProperties();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        o1.nOption = Math.min(quest.getProperties().size(), 10); // maxLevel
        o1.rOption = JAGUAR_LINK;
        tsm.putCharacterStatValue(JaguarCount, o1);
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleWarp() {
        handleJaguarLink();
        handleJaguarWings(false);
        if (chr.getTemporaryStatManager().hasStat(JaguarSummoned)) {
            chr.write(UserLocal.jaguarActive(true));
        }
        super.handleWarp();
    }

    @Override
    public void handleRemoveBuff(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (Arrays.stream(SUMMONS).anyMatch((summonSkillId) -> summonSkillId == skillId)) {
            for (int summonSkillId : SUMMONS) {
                tsm.removeStatsBySkill(summonSkillId);
            }
            tsm.sendResetStatPacket();
        }
        super.handleRemoveBuff(skillId);
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        if (hitInfo.hpDamage == 0 && hitInfo.mpDamage == 0) {
            // Dodged
            if (chr.hasSkill(FLURRY)) {
                TemporaryStatManager tsm = chr.getTemporaryStatManager();
                SkillInfo si = SkillData.getSkillInfoById(FLURRY);
                int slv = chr.getSkillLevel(FLURRY);
                Option o = new Option();
                o.nOption = 100;
                o.rOption = FLURRY;
                o.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CriticalBuff, o);
                tsm.sendSetStatPacket();
            }
        }
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleSetJob(short jobId) {
        if (JobConstants.isWildHunter(jobId)) {
            chr.addSkill(CAPTURE, 1, 1);
            chr.addSkill(CALL_OF_THE_HUNTER, 1, 1);
        }
        super.handleSetJob(jobId);
    }
}
