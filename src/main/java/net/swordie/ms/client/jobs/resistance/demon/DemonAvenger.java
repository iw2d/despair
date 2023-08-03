package net.swordie.ms.client.jobs.resistance.demon;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.jobs.resistance.Citizen;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class DemonAvenger extends Job {
    public static final int BLOOD_PACT = 30010242;
    public static final int EXCEED = 30010230;
    public static final int HYPER_POTION_MASTERY = 30010231;
    public static final int STAR_FORCE_CONVERSION = 30010232;

    public static final int EXCEED_DOUBLE_SLASH_1 = 31011000; //Special Attack  //TODO (EXCEED System)
    public static final int EXCEED_DOUBLE_SLASH_2 = 31011004; //Special Attack  //TODO (EXCEED System)
    public static final int EXCEED_DOUBLE_SLASH_3 = 31011005; //Special Attack  //TODO (EXCEED System)
    public static final int EXCEED_DOUBLE_SLASH_4 = 31011006; //Special Attack  //TODO (EXCEED System)
    public static final int EXCEED_DOUBLE_SLASH_PURPLE = 31011007; //Special Attack //TODO (EXCEED System)
    public static final int OVERLOAD_RELEASE = 31011001; // Special Buff        //TODO TempStat: ExceedOverload
    public static final int LIFE_SAP = 31010002; //Passive Life Drain

    public static final int EXCEED_DEMON_STRIKE_1 = 31201000; //Special Attack  //TODO (EXCEED System)
    public static final int EXCEED_DEMON_STRIKE_2 = 31201007; //Special Attack  //TODO (EXCEED System)
    public static final int EXCEED_DEMON_STRIKE_3 = 31201008; //Special Attack  //TODO (EXCEED System)
    public static final int EXCEED_DEMON_STRIKE_4 = 31201009; //Special Attack  //TODO (EXCEED System)
    public static final int EXCEED_DEMON_STRIKE_PURPLE = 31201010; //Special Attack //TODO (EXCEED System)
    public static final int BATTLE_PACT_DA = 31201002; //Buff
    public static final int BAT_SWARM = 31201001;

    public static final int EXCEED_LUNAR_SLASH_1 = 31211000; //Special Attack   //TODO (EXCEED System)
    public static final int EXCEED_LUNAR_SLASH_2 = 31211007; //Special Attack   //TODO (EXCEED System)
    public static final int EXCEED_LUNAR_SLASH_3 = 31211008; //Special Attack   //TODO (EXCEED System)
    public static final int EXCEED_LUNAR_SLASH_4 = 31211009; //Special Attack   //TODO (EXCEED System)
    public static final int EXCEED_LUNAR_SLASH_PURPLE = 31211010; //Special Attack //TODO (EXCEED System)
    public static final int VITALITY_VEIL = 31211001;
    public static final int SHIELD_CHARGE_RUSH = 31211002;
    public static final int SHIELD_CHARGE = 31211011; //Special Attack (Stun Debuff)
    public static final int DIABOLIC_RECOVERY = 31211004; //Buff
    public static final int WARD_EVIL = 31211003; //Buff
    public static final int ADVANCED_LIFE_SAP = 31210006; //Passive Life Drain
    public static final int PAIN_DAMPENER = 31210005;

    public static final int EXCEED_EXECUTION_1 = 31221000; //Special Attack     //TODO (EXCEED System)
    public static final int EXCEED_EXECUTION_2 = 31221009; //Special Attack     //TODO (EXCEED System)
    public static final int EXCEED_EXECUTION_3 = 31221010; //Special Attack     //TODO (EXCEED System)
    public static final int EXCEED_EXECUTION_4 = 31221011; //Special Attack     //TODO (EXCEED System)
    public static final int EXCEED_EXECUTION_PURPLE = 31221012; //Special Attack//TODO (EXCEED System)
    public static final int NETHER_SHIELD = 31221001; //Special Attack          //TODO
    public static final int NETHER_SHIELD_ATOM = 31221014; //atom
    public static final int NETHER_SLICE = 31221002; // Special Attack (DefDown Debuff)
    public static final int BLOOD_PRISON = 31221003; // Special Attack (Stun Debuff)
    public static final int MAPLE_WARRIOR_DA = 31221008; //Buff
    public static final int INFERNAL_EXCEED = 31220007;

    public static final int DEMONIC_FORTITUDE_DA = 31221053;
    public static final int FORBIDDEN_CONTRACT = 31221054;
    public static final int THOUSAND_SWORDS = 31221052;

    private int[] addedSkills = new int[] {
            EXCEED,
            BLOOD_PACT,
            HYPER_POTION_MASTERY,
            STAR_FORCE_CONVERSION,
    };

    private int lastExceedSkill;
    private ScheduledFuture diabolicRecoveryTimer;

    public DemonAvenger(Char chr) {
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
        return JobConstants.isDemonAvenger(id);
    }


    public void diabolicRecoveryHPRecovery() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
		if(tsm.hasStat(DiabolikRecovery)) {
            Skill skill = chr.getSkill(DIABOLIC_RECOVERY);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int recovery = si.getValue(x, slv);
            int duration = si.getValue(w, slv);
            chr.heal((int) (chr.getMaxHP() / ((double) 100 / recovery)));
            diabolicRecoveryTimer = EventManager.addEvent(() -> diabolicRecoveryHPRecovery(), duration, TimeUnit.SECONDS);
        }
    }

    public void regenDFInterval() {
        chr.healMP(10);
        EventManager.addEvent(() -> regenDFInterval(), 4, TimeUnit.SECONDS);
    }



    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        //DA HP Cost System
        applyHpCostForDA(SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId));

        if(hasHitMobs) {
            //Nether Shield Recreation
            if (attackInfo.skillId == NETHER_SHIELD_ATOM) {
                recreateNetherShieldForceAtom(attackInfo);
            }

            //Life Sap & Advanced Life Sap
            lifeSapHealing();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case BLOOD_PRISON:
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
            case SHIELD_CHARGE:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if(!mob.isBoss()) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = SkillConstants.getActualSkillIDfromSkillID(skillID);
                        o1.tOption = 5;
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case NETHER_SLICE:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = si.getValue(x, slv);
                    o1.rOption = skillID;
                    o1.tOption = 30;
                    mts.addStatOptions(MobStat.PDR, o1);
                    mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                }
                break;
            case THOUSAND_SWORDS:
                for(int i = 0; i<4; i++) {
                    incrementOverloadCount(skillID, tsm);
                }
                break;
            case VITALITY_VEIL:
                int amounthealed = si.getValue(y, slv);
                int healamount = (int) ((chr.getMaxHP()) / ((double) 100 / amounthealed));
                chr.heal(healamount);
                break;

            case EXCEED_DOUBLE_SLASH_1:
            case EXCEED_DOUBLE_SLASH_2:
            case EXCEED_DOUBLE_SLASH_3:
            case EXCEED_DOUBLE_SLASH_4:
            case EXCEED_DOUBLE_SLASH_PURPLE:

            case EXCEED_DEMON_STRIKE_1:
            case EXCEED_DEMON_STRIKE_2:
            case EXCEED_DEMON_STRIKE_3:
            case EXCEED_DEMON_STRIKE_4:
            case EXCEED_DEMON_STRIKE_PURPLE:

            case EXCEED_LUNAR_SLASH_1:
            case EXCEED_LUNAR_SLASH_2:
            case EXCEED_LUNAR_SLASH_3:
            case EXCEED_LUNAR_SLASH_4:
            case EXCEED_LUNAR_SLASH_PURPLE:

            case EXCEED_EXECUTION_1:
            case EXCEED_EXECUTION_2:
            case EXCEED_EXECUTION_3:
            case EXCEED_EXECUTION_4:
            case EXCEED_EXECUTION_PURPLE:
                giveExceedOverload(SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId));
                incrementOverloadCount(SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId), tsm);
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    public void sendHpUpdate() {
        // Used for client side damage calculation for DAs
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        o.nOption = 3; // Hp -> damage conversion
        o.mOption = chr.getTotalStat(BaseStat.mhp);
        tsm.putCharacterStatValue(LifeTidal, o);
        tsm.sendSetStatPacket();
    }

    private void createNetherShieldForceAtom() {
        Field field = chr.getField();
        SkillInfo si = SkillData.getSkillInfoById(NETHER_SHIELD);
        Rect rect = chr.getPosition().getRectAround(si.getRects().get(0));
        if(!chr.isLeft()) {
            rect = rect.moveRight();
        }
        List<Mob> mobs = field.getMobsInRect(rect);
        if(mobs.size() <= 0) {
            return;
        }
        Mob mob = Util.getRandomFromCollection(mobs);
        int mobID = mob.getObjectId(); //
        int inc = ForceAtomEnum.NETHER_SHIELD.getInc();
        int type = ForceAtomEnum.NETHER_SHIELD.getForceAtomType();
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, 20, 40,
                    0, 500, Util.getCurrentTime(), 1, 0,
                    new Position(0, -100));
            chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), type,
                    true, mobID, NETHER_SHIELD_ATOM, forceAtomInfo, new Rect(), 0, 300,
                    mob.getPosition(), NETHER_SHIELD_ATOM, mob.getPosition()));
    }

    private void recreateNetherShieldForceAtom(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(NETHER_SHIELD);
        int anglenum = new Random().nextInt(360);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            int TW1prop = 80;
            if (Util.succeedProp(TW1prop)) {
                int mobID = mai.mobId;

                int inc = ForceAtomEnum.NETHER_SHIELD_RECREATION.getInc();
                int type = ForceAtomEnum.NETHER_SHIELD_RECREATION.getForceAtomType();
                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, 35, 4,
                        anglenum, 0, Util.getCurrentTime(), 1, 0,
                        new Position());
                chr.getField().broadcastPacket(FieldPacket.createForceAtom(true, chr.getId(), mobID, type,
                        true, mobID, NETHER_SHIELD_ATOM, forceAtomInfo, new Rect(), 0, 300,
                        mob.getPosition(), NETHER_SHIELD_ATOM, mob.getPosition()));
            }
        }
    }

    public void giveExceedOverload(int skillid) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        o.nOption = skillid;
        o.rOption = skillid;
        o.tOption = 8;
        tsm.putCharacterStatValue(ExceedOverload, o);
        tsm.sendSetStatPacket();
    }

    public void incrementOverloadCount(int skillid, TemporaryStatManager tsm) {
        Option o = new Option();
        int amount = 1;
        if (tsm.hasStat(OverloadCount)) {
            amount = tsm.getOption(OverloadCount).nOption;
            if (amount < getMaxExceed()) {
                if(skillid != lastExceedSkill && lastExceedSkill != 0) {
                    amount++;
                }
                amount++;
            }
        }
        amount = amount > getMaxExceed() ? getMaxExceed() : amount;
        lastExceedSkill = skillid;
        o.nOption = amount;
        o.rOption = EXCEED;
        o.tOption = 0;
        tsm.putCharacterStatValue(OverloadCount, o);
        tsm.sendSetStatPacket();
    }

    private void resetExceed(TemporaryStatManager tsm) {
        tsm.removeStatsBySkill(EXCEED);
        tsm.sendResetStatPacket();
    }

    private int getMaxExceed() {
        int num = 20;
        if(chr.hasSkill(31220044)) { //Hyper Skill Boost [ Reduce Overload ]
            num = 18;
        }
        return num;
    }

    public void lifeSapHealing() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if(chr.hasSkill(LIFE_SAP)) {
            Skill skill = chr.getSkill(LIFE_SAP);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int proc = si.getValue(prop, slv);
            int amounthealed = si.getValue(x, slv);
            if(chr.hasSkill(ADVANCED_LIFE_SAP)) {
                amounthealed = SkillData.getSkillInfoById(ADVANCED_LIFE_SAP).getValue(x, chr.getSkill(ADVANCED_LIFE_SAP).getCurrentLevel());
            }
            if(chr.hasSkill(PAIN_DAMPENER)) {
                amounthealed -= SkillData.getSkillInfoById(PAIN_DAMPENER).getValue(x, chr.getSkill(PAIN_DAMPENER).getCurrentLevel());
            }
            int exceedamount = tsm.getOption(OverloadCount).nOption;
            int exceedpenalty = (int) Math.floor(exceedamount / 5);
            amounthealed -= exceedpenalty;
            if(Util.succeedProp(proc)) {
                int healamount = (int) ((chr.getMaxHP()) / ((double)100 / amounthealed));
                chr.heal(healamount);
            }
        }
    }

    @Override
    public int getFinalAttackSkill() {
        if(chr.hasSkill(INFERNAL_EXCEED)) {
            Skill skill = chr.getSkill(INFERNAL_EXCEED);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int proc = si.getValue(prop, slv);
            if(Util.succeedProp(proc)) {
                return INFERNAL_EXCEED;
            }
        }
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
        switch (skillID) {
            case NETHER_SHIELD:
                createNetherShieldForceAtom();
                createNetherShieldForceAtom();
                break;
            case OVERLOAD_RELEASE:
                int overloadCount = tsm.getOption(OverloadCount).nOption;
                double overloadRate = (double) overloadCount / getMaxExceed();

                o2.nOption = (int) (overloadRate * si.getValue(indiePMdR, slv));
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePMdR, o2);

                o3.nOption = 1;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Exceed, o3);

                resetExceed(tsm);
                chr.heal((int) (overloadRate * chr.getMaxHP()));
                break;
            case WARD_EVIL:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamageReduce, o1);
                o2.nOption = si.getValue(z, slv);
                o2.nReason = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o2);
                tsm.putCharacterStatValue(TerR, o2);
                break;
            case DIABOLIC_RECOVERY: // x = HP restored at interval
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieMhpR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o1);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DiabolikRecovery, o2);
                if(diabolicRecoveryTimer != null && !diabolicRecoveryTimer.isDone()) {
                    diabolicRecoveryTimer.cancel(true);
                }
                diabolicRecoveryHPRecovery();
                break;
            case FORBIDDEN_CONTRACT:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                //HP consumption from Skills = 0;
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void applyHpCostForDA(int skillID) {
        if (skillID == NETHER_SHIELD_ATOM || skillID == 0 || !chr.hasSkill(skillID)) {
            return;
        }
        Skill skill = chr.getSkill(SkillConstants.getActualSkillIDfromSkillID(skillID));
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        int hpRCost = si.getValue(hpRCon, slv);
        if(hpRCost > 0) {
            int skillcost = (int) (chr.getMaxHP() / ((double) 100 / hpRCost));
            if(chr.getHP() < skillcost) {
                return;
            }
            chr.heal(-skillcost);
        }
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

}
