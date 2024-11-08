package net.swordie.ms.client.jobs.resistance;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Blaster extends Citizen {
    public static final int HAMMER_SMASH = 37111000;
    public static final int HAMMER_SMASH_CHARGE = 37110001;
    public static final int ARM_CANNON_BOOST = 37101003;
    public static final int MAPLE_WARRIOR_BLASTER = 37121006;
    public static final int HEROS_WILL_BLASTER = 37121007;

    public static final int FOR_LIBERTY_BLASTER = 37121053;
    public static final int CANNON_OVERDRIVE = 37121054;
    public static final int HYPER_MAGNUM_PUNCH = 37121052;

    public static final int DETONATE = 37001004;
    public static final int DETONATE_UP = 37000005;

    //Revolving Cannon
    public static final int REVOLVING_CANNON_RELOAD = 37000010;
    public static final int REVOLVING_CANNON = 37001001;
    public static final int REVOLVING_CANNON_2 = 37100008;
    public static final int REVOLVING_CANNON_3 = 37000009;

    public static final int REVOLVING_CANNON_PLUS = 37100007;
    public static final int REVOLVING_CANNON_PLUS_II = 37110007;
    public static final int REVOLVING_CANNON_PLUS_III = 37120008;


    public static final int BUNKER_BUSTER_EXPLOSION_0 = 37110010;
    public static final int BUNKER_BUSTER_EXPLOSION_1 = 37100009;
    public static final int BUNKER_BUSTER_EXPLOSION_2 = 37000008;
    public static final int BUNKER_BUSTER_EXPLOSION_3 = 37001002;
    public static final int BUNKER_BUSTER_EXPLOSION_4 = 37000011;
    public static final int BUNKER_BUSTER_EXPLOSION_5 = 37000012;
    public static final int BUNKER_BUSTER_EXPLOSION_6 = 37000013;

    //Blast Shield
    public static final int BLAST_SHIELD = 37000006;
    public static final int SHIELD_TRAINING = 37110008;
    public static final int SHIELD_TRAINING_II = 37120009;
    public static final int VITALITY_SHIELD = 37121005;

    //Combo Training
    public static final int COMBO_TRAINING = 37110009;
    public static final int COMBO_TRAINING_II = 37120012;

    // Charged Skills
    public static final int CHARGE_MASTERY = 37100006;
    public static final int ADVANCED_CHARGE_MASTERY = 37120011;
    public static final int BOBBING_CHARGED = 37100002;
    public static final int WEAVING_CHARGED = 37110004;

    public static final int BALLISTIC_HURRICANE = 37121003;
    public static final int BALLISTIC_HURRICANE_1 = 37120024;
    public static final int WEAVING = 37111003;
    public static final int BOBBING = 37101001;

    private int gauge = 0;
    private int ammo = getMaxAmmo();
    private int lastAttack = 0;

    public Blaster(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isBlaster(id);
    }

    public void resetBlastShield() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.removeStat(RWBarrier, false);
        tsm.sendResetStatPacket();
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
        switch (attackInfo.skillId) {
            case DETONATE:
            case DETONATE_UP:
                removeAmmo();
                break;
            case BOBBING_CHARGED:
            case WEAVING_CHARGED:
                if (chr.hasSkill(CHARGE_MASTERY) && getAmmo() > 0 && getAmmo() < getMaxAmmo()) {
                    addAmmo(chr.hasSkill(ADVANCED_CHARGE_MASTERY) ? 2 : 1);
                }
                int realSkillId = skillID == BOBBING_CHARGED ? BOBBING : WEAVING;
                si = SkillData.getSkillInfoById(realSkillId);
                slv = chr.getSkillLevel(realSkillId);
                o1.nOption = si.getValue(w, slv);
                o1.rOption = realSkillId;
                o1.tOption = si.getValue(subTime, slv);
                o1.setInMillis(true);
                tsm.putCharacterStatValue(RWMovingEvar, o1);
                tsm.sendSetStatPacket();
                break;
            case HAMMER_SMASH_CHARGE:
                if (chr.hasSkill(CHARGE_MASTERY) && getAmmo() > 0 && getAmmo() < getMaxAmmo())
                    addAmmo(chr.hasSkill(ADVANCED_CHARGE_MASTERY) ? 2 : 1);
                SkillInfo hmc = SkillData.getSkillInfoById(HAMMER_SMASH);
                AffectedArea hmci = AffectedArea.getPassiveAA(chr, HAMMER_SMASH, (byte) slv);
                hmci.setMobOrigin((byte) 0);
                hmci.setPosition(chr.getPosition());
                hmci.setRect(hmci.getPosition().getRectAround(hmc.getRects().get(0)));
                hmci.setDelay((short) 5);
                chr.getField().spawnAffectedArea(hmci);
                //mobs deBuff
                o1.nOption = hmc.getValue(x, chr.getSkillLevel(HAMMER_SMASH));
                o1.rOption = HAMMER_SMASH;
                o1.tOption = 10;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptionsAndBroadcast(MobStat.AddDamSkill2, o1);
                }
                break;
            case BUNKER_BUSTER_EXPLOSION_2:
                if (getGauge() < 3 || tsm.hasStat(RWOverHeat)) {
                    return;
                }
                si = SkillData.getSkillInfoById(BUNKER_BUSTER_EXPLOSION_4);
                o1.nOption = si.getValue(v2, 1);
                o1.rOption = BUNKER_BUSTER_EXPLOSION_4;
                o1.tOption = si.getValue(time, 1);
                o1.nReason = si.getValue(v2, 1);
                tsm.putCharacterStatValue(RWOverHeat, o1);
                gaugeChange(getMaxAmmo() - getGauge());
                break;
            case HYPER_MAGNUM_PUNCH:
                o1.nOption = 5;
                o1.rOption = skillID;
                o1.tOption = 10;
                tsm.putCharacterStatValue(RWMagnumBlow, o1);
                tsm.sendSetStatPacket();
                break;
            case REVOLVING_CANNON_3:
            case REVOLVING_CANNON_2:
            case REVOLVING_CANNON:
                if(getAmmo() > 0) {
                    removeAmmo();
                }
                if(getGauge() < getMaxAmmo()) {
                    addGauge();
                }
                lastAttack = skillID;
                //chr.write(UserLocal.rwMultiChargeCancelRequest((byte)1, skillID));
                break;
        }
        incrementComboTraining(skillID, tsm);
        super.handleAttack(chr, attackInfo);
    }

    public int getGauge() {
        return gauge;
    }

    public void setGauge(int gauge) {
        this.gauge = gauge;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void addAmmo() {
        addAmmo(1);
    }

    public void addAmmo(int amount) {
        ammoChange(amount);
    }

    public void removeAmmo() {
        removeAmmo(1);
    }

    public void removeAmmo(int amount) {
        ammoChange(-amount);
    }

    public void ammoChange(int amount) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        if (getAmmo() > 0 && getAmmo() + amount <= getMaxAmmo()) {
            setAmmo(getAmmo() + amount);
            o.nOption = 1;
            o.bOption = getAmmo();
            o.cOption = getGauge();
            tsm.putCharacterStatValue(RWCylinder, o);
            tsm.sendSetStatPacket();
        }
    }

    public void addGauge() {
        addGauge(1);
    }

    public void addGauge(int amount) {
        gaugeChange(amount);
    }

    public void removeGauge() {
        removeGauge(1);
    }

    public void removeGauge(int amount) {
        gaugeChange(-amount);
    }

    public void gaugeChange(int amount) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        if (getGauge() <= getMaxAmmo() || (amount < 0 && getGauge() + amount > 0)) {
            setGauge(getGauge() + amount);
            o.nOption = 1;
            o.bOption = getAmmo();
            o.cOption = getGauge();
            tsm.putCharacterStatValue(RWCylinder, o);
            tsm.sendSetStatPacket();
        }
    }

    public int getMaxAmmo() {
        int maxAmmo = 3;
        if(chr.hasSkill(REVOLVING_CANNON_PLUS)) {
            maxAmmo = 4;
        }
        if(chr.hasSkill(REVOLVING_CANNON_PLUS_II)) {
            maxAmmo = 5;
        }
        if(chr.hasSkill(REVOLVING_CANNON_PLUS_III)) {
            maxAmmo = 6;
        }
        return maxAmmo;
    }

    public void reloadCylinder() {
        setAmmo(getMaxAmmo());
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        o.nOption = 1;
        o.bOption = getMaxAmmo(); //ammo
        o.cOption = getGauge(); //gauge
        tsm.putCharacterStatValue(RWCylinder, o);
        tsm.sendSetStatPacket();
    }

    private void incrementComboTraining(int skillId, TemporaryStatManager tsm) {
        if (getAmmo() == 0) {
            Option o = new Option();
            o.nOption = 1;
            o.rOption = Blaster.REVOLVING_CANNON_RELOAD;
            o.bOption = getMaxAmmo();
            o.cOption = getGauge();
            tsm.putCharacterStatValue(RWCylinder, o);
            tsm.sendSetStatPacket();
            int time = tsm.hasStat(RWMaximizeCannon) ? 500 : 1500;
            EventManager.addEvent(() -> reloadCylinder(), time);
        }
        if (!chr.hasSkill(COMBO_TRAINING)) {
            return;
        }
        SkillInfo chargeInfo = SkillData.getSkillInfoById(COMBO_TRAINING);
        int amount = 1;
        if (tsm.hasStat(RWCombination)) {
            amount = tsm.getOption(RWCombination).nOption;
            if (lastAttack == skillId) {
                return;
            }
            if (amount < chargeInfo.getValue(z, 1)) {
                amount++;
            }
        }
        lastAttack = skillId;
        Option o = new Option();
        o.nOption = amount;
        o.rOption = chr.hasSkill(COMBO_TRAINING_II) ? COMBO_TRAINING_II : COMBO_TRAINING;
        o.tOption = 10;
        tsm.putCharacterStatValue(RWCombination, o);
        if (amount >= chargeInfo.getValue(w, 1)) { //if combo higher than w value give attack speed, for both passives
            Option o1 = new Option();
            o1.nValue = -1;
            o1.rOption = COMBO_TRAINING;
            o1.tStart = Util.getCurrentTime();
            o1.tTerm = 10;
            tsm.putCharacterStatValue(IndieBooster, o1);
        }
        chargeInfo = SkillData.getSkillInfoById(COMBO_TRAINING_II);
        Option o2 = new Option();
        o2.nOption = chr.hasSkill(COMBO_TRAINING_II) ? (3 + (chr.getSkillLevel(COMBO_TRAINING_II) / 10)) * amount : chr.getSkillLevel(COMBO_TRAINING) / 3 * amount; //diff calculation depends if player has combo training 2
        o2.rOption = chargeInfo.getCurrentLevel();
        o2.tOption = 10;
        tsm.putCharacterStatValue(DamR, o2);

        if (!chr.hasSkill(COMBO_TRAINING_II)) {
            return;
        }
        Option o3 = new Option();
        o3.nOption = chr.getSkillLevel(COMBO_TRAINING_II) / 10 * amount;
        o3.rOption = chargeInfo.getCurrentLevel();
        o3.tOption = 10;
        tsm.putCharacterStatValue(CriticalBuff, o3);
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
        switch(skillID) {
            case REVOLVING_CANNON_RELOAD:
                reloadCylinder();
                break;
            case VITALITY_SHIELD:
                if (!tsm.hasStat(RWBarrier)) {
                    return;
                }
                int healAmount = (int) (0.5 * chr.getMaxHP() + tsm.getOption(RWBarrier).nOption);
                chr.heal(healAmount);
                resetBlastShield();
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(RWBarrierHeal, o1);
                break;
            case ARM_CANNON_BOOST:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case CANNON_OVERDRIVE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(RWMaximizeCannon, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(BLAST_SHIELD) && hitInfo.hpDamage > 0 && !tsm.hasStat(RWBarrier)) {
            Skill shieldSkill = getBlastShieldSkill();
            SkillInfo shieldInfo = SkillData.getSkillInfoById(shieldSkill.getSkillId());
            int amount = Math.min(hitInfo.hpDamage * shieldInfo.getValue(x, shieldSkill.getCurrentLevel()) / 100 + 1, chr.getMaxHP());
            putOnShield(amount);
        }
        super.handleHit(chr, hitInfo);
    }

    public Skill getBlastShieldSkill() {
        Skill skill = null;
        if (chr.hasSkill(BLAST_SHIELD)) {
            skill = chr.getSkill(BLAST_SHIELD);
        }
        if (chr.hasSkill(SHIELD_TRAINING)) {
            skill = chr.getSkill(SHIELD_TRAINING);
        }
        if (chr.hasSkill(SHIELD_TRAINING_II)) {
            skill = chr.getSkill(SHIELD_TRAINING_II);
        }
        return skill;
    }

    public void decreaseShield() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(RWBarrier)) { //function stops on vitality shield or shield value <=0
            return;
        }
        Skill shieldSkill = getBlastShieldSkill();
        SkillInfo si = SkillData.getSkillInfoById(shieldSkill.getSkillId());
        int oldShield = tsm.getOption(RWBarrier).nOption;
        int newShield = (oldShield * si.getValue(y, shieldSkill.getCurrentLevel()) / 100) - si.getValue(z, shieldSkill.getCurrentLevel());
        if (newShield <= 0) {
            tsm.removeStat(RWBarrier, false);
            tsm.sendResetStatPacket();
        } else {
            putOnShield(newShield);
        }
        tsm.sendSetStatPacket();
    }

    public void putOnShield(int amount) {
        Option o = new Option();
        Option o1 = new Option();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        o.nOption = amount;
        o.rOption = BLAST_SHIELD;
        tsm.putCharacterStatValue(RWBarrier, o);
        o1.nReason = BLAST_SHIELD;
        o1.nValue = 100;
        tsm.putCharacterStatValue(IndieStance, o1);
        tsm.sendSetStatPacket();
        EventManager.addEvent(this::decreaseShield, 3000);
    }
}