package net.swordie.ms.client.jobs;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.BodyPart;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Util;

import java.util.Arrays;

import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.client.character.skills.SkillStat.*;

/**
 * Created on 12/14/2017.
 */
public class Kinesis extends Job {
    public static final int RETURN_KINESIS = 140001290;

    public static final int PSYCHIC_FORCE = 142001000;
    public static final int MENTAL_SHIELD = 142001007;
    public static final int ESP_BOOSTER = 142001003;
    public static final int ULTIMATE_METAL_PRESS = 142001002;
    public static final int PSYCHIC_BLAST_FWD = 142100000;
    public static final int PSYCHIC_BLAST_DOWN = 142100001;
    public static final int PSYCHIC_ASSAULT_FWD = 142110000;
    public static final int PSYCHIC_ASSAULT_DOWN = 142110001;
    public static final int PSYCHIC_DRAIN = 142101009; // TODO, AffectedArea?
    public static final int PSYCHIC_ARMOR = 142101004;
    public static final int ULTIMATE_DEEP_IMPACT = 142101003;
    public static final int PSYCHIC_BULWARK = 142110009;
    public static final int PURE_POWER = 142101005;
    public static final int PSYCHIC_REINFORCEMENT = 142111008;
    public static final int KINETIC_JAUNT = 142111010;
    public static final int ULTIMATE_TRAINWRECK = 142111007;
    public static final int KINETIC_COMBO = 142110011;
    public static final int MIND_BREAK = 142121004;
    public static final int CLEAR_MIND = 142121007;
    public static final int ULTIMATE_PSYCHIC_SHOT = 142120002;
    public static final int ULTIMATE_BPM = 142121005;
    public static final int PRESIDENTS_ORDERS = 142121016;
    public static final int PSYCHIC_CHARGER = 142121008;
    public static final int TELEPATH_TACTICS = 142121006;
    public static final int MENTAL_TEMPEST = 142121030;
    public static final int MENTAL_SHOCK = 142121031;
    public static final int MENTAL_OVERDRIVE = 142121032;


    private static final int MAX_PP = 30;

    private final int[] nonOrbSkills = new int[] {
            ULTIMATE_METAL_PRESS,
            ULTIMATE_BPM,
            ULTIMATE_DEEP_IMPACT,
            ULTIMATE_PSYCHIC_SHOT,
            ULTIMATE_TRAINWRECK,
            PSYCHIC_FORCE,
            PSYCHIC_DRAIN,
            MENTAL_TEMPEST,
            KINETIC_COMBO,
    };

    public Kinesis(Char chr) {
        super(chr);
        pp = 0;
    }

    private int pp;

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isKinesis(id);
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public void addPP(int amount) {
        pp = pp + amount > MAX_PP ? MAX_PP : pp + amount;
        sendPPPacket();
    }

    public void substractPP(int amount) {
        pp = pp - amount < 0 ? 0 : pp - amount;
        sendPPPacket();
    }

    private void sendPPPacket() {
        Option o = new Option();
        o.nOption = pp;
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.putCharacterStatValue(KinesisPsychicPoint, o);
        tsm.sendSetStatPacket();
    }



    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;

        if (hasHitMobs && chr.hasSkill(KINETIC_COMBO)) {
            createKineticOrbForceAtom(skillID, slv, attackInfo);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        kinesisPPAttack(skillID, slv, si);
        switch (attackInfo.skillId) {
            case PSYCHIC_FORCE:
            case PSYCHIC_BLAST_FWD:
            case PSYCHIC_BLAST_DOWN:
            case PSYCHIC_ASSAULT_FWD:
            case PSYCHIC_ASSAULT_DOWN:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
            case MIND_BREAK:
                int count = 0;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if(mob.isBoss()) {
                        count += si.getValue(x, slv);
                    } else {
                        count++;
                    }
                }
                count = count > si.getValue(w, slv) ? si.getValue(w, slv) : count;
                o1.nValue = count * si.getValue(indiePMdR, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePMdR, o1);
                tsm.sendSetStatPacket();
                break;
            case MENTAL_SHOCK:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if(!mob.isBoss()) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void createKineticOrbForceAtom(int skillID, int slv, AttackInfo attackInfo) {
        if(Arrays.asList(nonOrbSkills).contains(skillID)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(KINETIC_COMBO);
        for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
            if (Util.succeedProp(si.getValue(prop, slv))) {
                int mobID = mai.mobId;
                ForceAtomEnum fae = ForceAtomEnum.KINESIS_ORB_REAL;
                int curTime = Util.getCurrentTime();
                ForceAtomInfo fai = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc(), 15, 15,
                        0, 0, curTime, 0, skillID, new Position(0, 0));
                chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(), true,
                        mobID, KINETIC_COMBO, fai, null, 0, 0, null, 0, null));
            }
        }
    }

    private void kinesisPPAttack(int skillID, int slv, SkillInfo si) {
        if(si == null) {
            return;
        }
        int ppRec = si.getValue(ppRecovery, slv);
        addPP(ppRec);
        int ppCons = si.getValue(ppCon, slv);
        substractPP(ppCons);
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
        int curTime = Util.getCurrentTime();
        switch(skillID) {
            case PSYCHIC_CHARGER:
                int add = (MAX_PP - getPp()) / 2;
                addPP(add);
                break;
            case ESP_BOOSTER:
                o1.nValue = si.getValue(indieBooster, slv);
                o1.nReason = skillID;
                o1.tStart = curTime;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o1);
                break;
            case MENTAL_SHIELD:
                o1.nOption = si.getValue(x, slv);
                o1.nReason = skillID;
                tsm.putCharacterStatValue(KinesisPsychicEnergeShield, o1);
                break;
            case PSYCHIC_ARMOR:
            case PSYCHIC_BULWARK:
                int t = SkillData.getSkillInfoById(PSYCHIC_ARMOR).getValue(time, slv);
                o1.nValue = si.getValue(indiePdd, slv);
                o1.nReason = skillID;
                o1.tStart = curTime;
                o2.tTerm = t;
                tsm.putCharacterStatValue(IndieDEF, o1);
                o2.nOption = si.getValue(stanceProp, slv);
                o2.rOption = skillID;
                o2.tOption = t;
                tsm.putCharacterStatValue(IndieStance, o2);
                break;
            case PURE_POWER:
                o1.nValue = si.getValue(indieDamR, slv);
                o1.nReason = skillID;
                o1.tStart = curTime;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o1);
                break;
            case PSYCHIC_REINFORCEMENT:
                o1.nValue = si.getValue(indieMadR, slv);
                o1.nReason = skillID;
                o1.tStart = curTime;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMADR, o1);
                break;
            case TELEPATH_TACTICS:
                o1.nValue = si.getValue(indieMad, slv);
                o1.nReason = skillID;
                o1.tStart = curTime;
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMADR, o1);
                o2.nValue = si.getValue(indieDamR, slv);
                o2.nReason = skillID;
                o2.tStart = curTime;
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o2);
                break;
            case KINETIC_JAUNT:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NewFlying, o1); //38s
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if(tsm.hasStat(KinesisPsychicShield)) {
            hitInfo.hpDamage = (int) (hitInfo.hpDamage * (tsm.getOption(KinesisPsychicEnergeShield).nOption / 100D));
            substractPP(1);
        }
        if(getPp() <= 0) {
            tsm.removeStat(KinesisPsychicShield, false);
            tsm.sendResetStatPacket();
        }
        super.handleHit(chr, hitInfo);
    }

    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        cs.setLevel(10);
        cs.setJob(JobConstants.JobEnum.KINESIS_1.getJobId());
        cs.setInt(49);
        cs.setHp(574);
        cs.setMaxHp(574);
        Item item = ItemData.getItemDeepCopy(1353200); // Pawn Chess Piece
        item.setBagIndex(BodyPart.Shield.getVal());
        chr.getEquippedInventory().addItem(item);
        chr.setSpToCurrentJob(5);
    }
}
