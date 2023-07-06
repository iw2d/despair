package net.swordie.ms.client.jobs.adventurer.pirate;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.PartyBooster;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatBase;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.TSIndex;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
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
public class Buccaneer extends Pirate {
    public static final int KNUCKLE_BOOSTER = 5101006; //Buff
    public static final int PERSERVERANCE = 5100013;
    public static final int TORNADO_UPPERCUT = 5101012; //Special Attack
    public static final int ENERGY_CHARGE = 5100015; //Energy Gauge

    public static final int ROLL_OF_THE_DICE_BUCC = 5111007; //Buff
    public static final int ENERGY_BURST = 5111002; //Special Attack
    public static final int STATIC_THUMPER = 5111012; //Special Attack
    public static final int STUN_MASTERY = 5110000;
    public static final int SUPERCHARGE = 5110014;

    public static final int OCTOPUNCH = 5121007; //Special Attack
    public static final int NAUTILUS_STRIKE_BUCC = 5121013; //Special Attack
    public static final int NAUTILUS_STRIKE_BUCC_FA = 5120021; // Final Attack
    public static final int DRAGON_STRIKE = 5121001; //Special Attack
    public static final int BUCCANEER_BLAST = 5121016; //Special Attack
    public static final int CROSSBONES = 5121015; //Buff
    public static final int SPEED_INFUSION = 5121009; //Buff
    public static final int TIME_LEAP = 5121010; //Special Move / Buff
    public static final int MAPLE_WARRIOR_BUCC = 5121000; //Buff
    public static final int PIRATE_REVENGE_BUCC = 5120011;
    public static final int ULTRA_CHARGE = 5120018;
    public static final int ROLL_OF_THE_DICE_BUCC_DD = 5120012;
    public static final int HEROS_WILL_BUCC = 5121008;

    public static final int EPIC_ADVENTURER_BUCC = 5121053;
    public static final int STIMULATING_CONVERSATION = 5121054;
    public static final int POWER_UNITY = 5121052;
    public static final int ROLL_OF_THE_DICE_BUCC_ADDITION = 5120044;
    public static final int ROLL_OF_THE_DICE_BUCC_SAVING_GRACE = 5120043;
    public static final int ROLL_OF_THE_DICE_BUCC_ENHANCE = 5120045;


    private ScheduledFuture perseveranceTimer;
    private ScheduledFuture stimulatingConversationTimer;


    public Buccaneer(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isBuccaneer(id);
    }

    private void powerUnity() {
        if(!chr.hasSkill(POWER_UNITY)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Skill skill = chr.getSkill(POWER_UNITY);
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        int amount = 1;
        if(tsm.hasStat(UnityOfPower)) {
            amount = tsm.getOption(UnityOfPower).nOption;
            if(amount < 4) {
                amount++;
            }
        }
        o1.nOption = amount;
        o1.rOption = skill.getSkillId();
        o1.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(UnityOfPower, o1);
        tsm.sendSetStatPacket();
    }

    private void incrementStimulatingConversation() {
        if(!chr.hasSkill(STIMULATING_CONVERSATION)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(Stimulate)) {
            Skill skill = chr.getSkill(STIMULATING_CONVERSATION);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            updateViperEnergy(tsm.getOption(EnergyCharged).nOption + si.getValue(x, slv));
            stimulatingConversationTimer = EventManager.addEvent(this::incrementStimulatingConversation, 4, TimeUnit.SECONDS);
        }
    }

    private void handlePerserverance() {
        if (!chr.hasSkill(PERSERVERANCE)) {
            return;
        }
        Skill skill = chr.getSkill(PERSERVERANCE);
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());

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
        if (hasHitMobs && attackInfo.skillId != 0) {
            // Stun Mastery
            applyStunMasteryOnMob(attackInfo);

            // Viper Energy
            boolean hasHitBoss = attackInfo.mobAttackInfo.stream().anyMatch(mai ->
                    chr.getField().getLifeByObjectID(mai.mobId) != null &&
                            ((Mob) chr.getField().getLifeByObjectID(mai.mobId)).isBoss()
            );
            changeViperEnergy(attackInfo.skillId, hasHitBoss);
        }
        switch (attackInfo.skillId) {
            case POWER_UNITY:
                powerUnity();
                break;
            case DRAGON_STRIKE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptionsAndBroadcast(MobStat.AddDamSkill2, o1);
                }
                break;
            case NAUTILUS_STRIKE_BUCC:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(VenomSnake, o1);
                tsm.sendSetStatPacket();
                break;
        }
        super.handleAttack(chr, attackInfo);
    }


    private void applyStunMasteryOnMob(AttackInfo attackInfo) {
        if (!chr.hasSkill(STUN_MASTERY)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(STUN_MASTERY);
        int slv = si.getCurrentLevel();
        Option o1 = new Option();
        o1.nOption = 1;
        o1.rOption = STUN_MASTERY;
        o1.tOption = 3;
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            if (Util.succeedProp(si.getValue(subProp, slv))) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null || mob.isBoss()) {
                    continue;
                }
                MobTemporaryStat mts = mob.getTemporaryStat();
                mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
            }
        }
    }

    private void changeViperEnergy(int skillId, boolean hasHitBoss) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int energy = getEnergyIncrement(hasHitBoss);
        if(tsm.getViperEnergyCharge() == 0) {
            if (tsm.hasStat(EnergyCharged)) {
                energy = tsm.getOption(EnergyCharged).nOption;
                if (energy < getMaximumEnergy()) {
                    energy += getEnergyIncrement(hasHitBoss);
                }
                if (energy > getMaximumEnergy()) {
                    energy = getMaximumEnergy();
                }
            }
            chr.write(UserPacket.effect(Effect.skillAffected(getViperEnergySkill().getSkillId(), (byte) 1, 0)));
            chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffected(getViperEnergySkill().getSkillId(), (byte) 1, 0)));
        } else {
            energy = deductViperEnergyCost(skillId);
        }
        updateViperEnergy(energy);
    }

    private int deductViperEnergyCost(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int energy = tsm.getOption(EnergyCharged).nOption;
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        energy = energy - si.getValue(forceCon, 1);

        return energy;
    }

    private void updateViperEnergy(int energy) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        TemporaryStatBase tsb = tsm.getTSBByTSIndex(TSIndex.EnergyCharged);
        tsb.setNOption(energy < 0 ? 0 : (energy > getMaximumEnergy() ? getMaximumEnergy() : energy));
        tsb.setROption(getViperEnergySkill().getSkillId());
        tsm.putCharacterStatValue(EnergyCharged, tsb.getOption());
        if(energy >= getMaximumEnergy()) {
            tsm.setViperEnergyCharge(getViperEnergySkill().getSkillId());
        } else if (energy <= 0) {
            tsm.setViperEnergyCharge(0);
        }
        tsm.sendSetStatPacket();
    }

    private int getEnergyIncrement(boolean hasHitBoss) {
        Skill skill = getViperEnergySkill();
        int skillId = skill.getSkillId();
        int increment = chr.getSkillStatValue(x, skillId);
        if (hasHitBoss && (skillId == SUPERCHARGE || skillId == ULTRA_CHARGE)) {
            increment *= 2;
        }
        return increment;
    }

    private int getMaximumEnergy() {
        Skill skill = getViperEnergySkill();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();

        return si.getValue(z, slv);
    }

    private Skill getViperEnergySkill() {
        Skill skill = null;
        if(chr.hasSkill(ENERGY_CHARGE)) {
            skill = chr.getSkill(ENERGY_CHARGE);
        }
        if(chr.hasSkill(SUPERCHARGE)) {
            skill = chr.getSkill(SUPERCHARGE);
        }
        if(chr.hasSkill(ULTRA_CHARGE)) {
            skill = chr.getSkill(ULTRA_CHARGE);
        }
        return skill;
    }

    @Override
    public int getFinalAttackSkill() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(NAUTILUS_STRIKE_BUCC) && tsm.hasStatBySkillId(NAUTILUS_STRIKE_BUCC)) {
            return NAUTILUS_STRIKE_BUCC_FA;
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
        Option o5 = new Option();
        Option o6 = new Option();
        Option o7 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case TIME_LEAP:
                long nextAvailableTime = Util.getCurrentTimeLong() + (si.getValue(time, slv)*1000);
                chr.getScriptManager().createQuestWithQRValue(chr, GameConstants.TIME_LEAP_QR_KEY, String.valueOf(nextAvailableTime), false);
                if (chr.getQuestManager().getQuestById(GameConstants.TIME_LEAP_QR_KEY).getQRValue() == null
                        || Long.parseLong(chr.getQuestManager().getQuestById(GameConstants.TIME_LEAP_QR_KEY).getQRValue()) < Util.getCurrentTimeLong()) {
                    for (int skillId : chr.getSkillCoolTimes().keySet()) {
                        if (!SkillData.getSkillInfoById(skillId).isNotCooltimeReset() && SkillData.getSkillInfoById(skillId).getHyper() == 0) {
                            chr.resetSkillCoolTime(skillId);
                        }
                    }
                }
                break;
            case SPEED_INFUSION:
                PartyBooster pb = (PartyBooster) tsm.getTSBByTSIndex(TSIndex.PartyBooster);
                pb.setNOption(-1);
                pb.setROption(skillID);
                pb.setCurrentTime(Util.getCurrentTime());
                pb.setExpireTerm(1);
                tsm.putCharacterStatValue(PartyBooster, pb.getOption());
                break;
            case CROSSBONES:
                o2.nReason = skillID;
                o2.nValue = si.getValue(indiePadR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePADR, o2);
                break;
            case STIMULATING_CONVERSATION:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stimulate, o1);
                o2.nOption = si.getValue(indieDamR, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o2);
                if(stimulatingConversationTimer != null && !stimulatingConversationTimer.isDone()) {
                    stimulatingConversationTimer.cancel(true);
                }
                incrementStimulatingConversation();
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
