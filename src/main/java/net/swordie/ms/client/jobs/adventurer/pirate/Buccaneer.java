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
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Buccaneer extends Pirate {
    public static final int KNUCKLE_MASTERY = 5100001;
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
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            perseveranceTimer = EventManager.addEvent(this::handlePerserverance, getPerserveranceDelay(), TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isBuccaneer(id);
    }


    private void handlePerserverance() {
        if (chr.hasSkill(PERSERVERANCE) && chr.getHP() > 0) {
            int healRate = chr.getSkillStatValue(x, PERSERVERANCE);
            chr.heal((int) (chr.getMaxHP() * ((double) healRate / 100D)));
            chr.healMP((int) (chr.getMaxMP() * ((double) healRate / 100D)));
        }
        if (perseveranceTimer != null && !perseveranceTimer.isDone()) {
            perseveranceTimer.cancel(true);
        }
        perseveranceTimer = EventManager.addEvent(this::handlePerserverance, getPerserveranceDelay(), TimeUnit.SECONDS);
    }

    private int getPerserveranceDelay() {
        if (!chr.hasSkill(PERSERVERANCE)) {
            return 5;
        }
        return chr.getSkillStatValue(w, PERSERVERANCE);
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        boolean hasHitBoss = attackInfo.mobAttackInfo.stream().anyMatch(mai -> {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            return mob != null && mob.isBoss();
        });
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        if (hasHitMobs) {
            applyStunMasteryOnMob(attackInfo);
            changeViperEnergy(attackInfo.skillId, slv, hasHitBoss);
        }
        switch (attackInfo.skillId) {
            case ENERGY_BURST:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                int proc = si.getValue(prop, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(proc)) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case POWER_UNITY:
                int stacks = 1;
                if (tsm.hasStat(UnityOfPower)) {
                    stacks = tsm.getOption(UnityOfPower).nOption;
                    if (stacks < 4) {
                        stacks++;
                    }
                }
                o1.nOption = stacks;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(UnityOfPower, o1);
                break;
            case DRAGON_STRIKE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.AddDamSkill2, o1);
                }
                break;
            case NAUTILUS_STRIKE_BUCC:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NautilusFinalAttack, o1);
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
        o1.tOption = GameConstants.DEFAULT_STUN_DURATION;
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            if (Util.succeedProp(si.getValue(subProp, slv))) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null || mob.isBoss()) {
                    continue;
                }
                mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
            }
        }
    }

    private void changeViperEnergy(int attackSkillId, int attackSlv, boolean hasHitBoss) {
        /**
         * when energy charging (tsm.setViperEnergyCharge(0))
         *      when energy hits max, switch to fully charged mode
         *      else, apply half of the buff effects, increment energy
         * when energy fully charged (tsm.setViperEnergyCharge(energySkillId) // shows the effect)
         *      when energy hits 0, switch back to charging mode
         *      else, apply full buff effects, decrement energy
         */
        int energySkillId = getViperEnergySkill();
        if (energySkillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int energy = tsm.getOption(EnergyCharged).nOption;
        if (tsm.getViperEnergyCharge() == 0) {
            int increment = chr.getSkillStatValue(x, energySkillId);
            if (hasHitBoss && (energySkillId == SUPERCHARGE || energySkillId == ULTRA_CHARGE)) {
                increment *= 2;
            }
            energy += increment;
            chr.write(UserPacket.effect(Effect.skillAffected(energySkillId, (byte) 1, 0)));
            chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffected(energySkillId, (byte) 1, 0)));
        } else {
            energy -= SkillData.getSkillInfoById(attackSkillId).getValue(forceCon, attackSlv);
        }
        updateViperEnergy(energy);
    }

    private void updateViperEnergy(int energy) {
        int energySkillId = getViperEnergySkill();
        if (energySkillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(energySkillId);
        int slv = chr.getSkillLevel(energySkillId);
        int energyMax = si.getValue(z, slv);
        if (energy >= energyMax) {
            energy = energyMax;
            tsm.setViperEnergyCharge(energySkillId);
        } else if (energy <= 0) {
            energy = 0;
            tsm.setViperEnergyCharge(0);
        }
        TemporaryStatBase tsb = tsm.getTSBByTSIndex(TSIndex.EnergyCharged);
        Option o1 = new Option();
        o1.nOption = energy;
        o1.rOption = tsm.getViperEnergyCharge() == 0 ? 0 : energySkillId;
        tsb.setOption(o1);
        tsm.putCharacterStatValue(EnergyCharged, tsb.getOption());
        tsm.sendSetStatPacket();
    }

    public int getViperEnergySkill() {
        if (chr.hasSkill(ULTRA_CHARGE)) {
            return ULTRA_CHARGE;
        } else if (chr.hasSkill(SUPERCHARGE)) {
            return SUPERCHARGE;
        } else if (chr.hasSkill(ENERGY_CHARGE)) {
            return ENERGY_CHARGE;
        }
        return 0;
    }

    @Override
    public int getFinalAttackSkill() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(NautilusFinalAttack)) {
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
        switch (skillID) {
            case TIME_LEAP:
                long nextAvailableTime = Util.getCurrentTimeLong() + (si.getValue(time, slv) * 1000L);
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
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                pb.setOption(o1);
                pb.setCurrentTime(Util.getCurrentTime());
                pb.setExpireTerm(si.getValue(time, slv));
                tsm.putCharacterStatValue(PartyBooster, pb.getOption());
                break;
            case CROSSBONES:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indiePadR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1); // DamR according to skill description
                break;
            case STIMULATING_CONVERSATION:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stimulate, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieDamR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o2);
                if (stimulatingConversationTimer != null && !stimulatingConversationTimer.isDone()) {
                    stimulatingConversationTimer.cancel(true);
                }
                EventManager.addEvent(this::incrementStimulatingConversation, 4, TimeUnit.SECONDS); // subTime = 4
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void incrementStimulatingConversation() {
        if (!chr.hasSkill(STIMULATING_CONVERSATION)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(Stimulate)) {
            int energy = tsm.getOption(EnergyCharged).nOption;
            updateViperEnergy(energy + chr.getSkillStatValue(x, STIMULATING_CONVERSATION));
            stimulatingConversationTimer = EventManager.addEvent(this::incrementStimulatingConversation, 4, TimeUnit.SECONDS);
        }
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleRemoveSkill(int skillID) {
        if (skillID == STIMULATING_CONVERSATION && stimulatingConversationTimer != null && !stimulatingConversationTimer.isDone()) {
            stimulatingConversationTimer.cancel(true);
        }
        super.handleRemoveSkill(skillID);
    }

    @Override
    public void handleCancelTimer() {
        if (perseveranceTimer != null && !perseveranceTimer.isDone()) {
            perseveranceTimer.cancel(true);
        }
        if (stimulatingConversationTimer != null && !stimulatingConversationTimer.isDone()) {
            stimulatingConversationTimer.cancel(true);
        }
        super.handleCancelTimer();
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}
