package net.swordie.ms.client.jobs.adventurer.pirate;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.world.field.Foothold;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Cannoneer extends Pirate {
    public static final int BLAST_BACK = 5011002; //Special Attack

    public static final int MONKEY_MAGIC = 5301003; //Buff
    public static final int CANNON_BOOSTER = 5301002; //Buff

    public static final int MONKEY_WAVE = 5311002; //Special Attack
    public static final int MONKEY_WAVE_IMMEDIATE = 5310008;
    public static final int BARREL_ROULETTE = 5311004; //Buff
    public static final int LUCK_OF_THE_DIE = 5311005; //Buff

    public static final int LUCK_OF_THE_DIE_DD = 5320007;
    public static final int ANCHOR_AWEIGH = 5321003; //Summon
    public static final int MONKEY_MILITIA = 5321004; //Summon
    public static final int NAUTILUS_STRIKE_CANNON = 5321001; //Special Attack / Buff
    public static final int PIRATE_SPIRIT = 5321010; //Buff
    public static final int MAPLE_WARRIOR_CANNON = 5321005; //Buff
    public static final int HEROS_WILL_CANNON = 5321006;
    public static final int MEGA_MONKEY_MAGIC = 5320008;

    public static final int EPIC_ADVENTURER_CANNON = 5321053;
    public static final int BUCKSHOT = 5321054;
    public static final int ROLLING_RAINBOW = 5321052;


    public static final int[] MONKEY_MILITIA_SUMMONS = {
            5320011,
            5321004
    };


    public Cannoneer(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isCannonShooter(id);
    }

    private void giveBarrelRouletteBuff(int roulette) {   //TODO
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        Skill skill = chr.getSkill(BARREL_ROULETTE);
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        chr.write(UserPacket.effect(Effect.skillAffectedSelect(BARREL_ROULETTE, slv, roulette, false)));
        switch (roulette) {
            case 1: // Extra Attack (Final Attack)
                //Handled, See Final Attack Handler
                break;
            case 2: // Max CritDmg
                o.nOption = si.getValue(s, slv);
                o.rOption = 0;
                o.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IncCriticalDam, o);
                tsm.sendSetStatPacket();
                break;
            case 3: // Slow Debuff
                //Handled, See Attack Handler
                break;
            case 4: // DoT
                //Handled, See Attack Handler
                break;
        }
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
        if (hasHitMobs) {
            handleMonkeyWaveIgnoreKeyDown(attackInfo);
            applyBarrelRouletteDebuffOnMob(attackInfo);
        }
        switch (attackInfo.skillId) {
            case BLAST_BACK:
                o1.nOption = si.getValue(z, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Speed, o1);
                    }
                }
                break;
            case MONKEY_WAVE:
                o1.nOption = si.getValue(z, slv);
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
                if (tsm.hasStatBySkillId(MONKEY_WAVE_IMMEDIATE)) {
                    tsm.removeStatsBySkill(MONKEY_WAVE_IMMEDIATE);
                    tsm.sendResetStatPacket();
                }
                if (attackInfo.keyDown >= 1000) {
                    o2.nOption = si.getValue(x, slv);
                    o2.rOption = skillID;
                    o2.tOption = si.getValue(subTime, slv);
                    tsm.putCharacterStatValue(IncCriticalDam, o2);
                    tsm.sendSetStatPacket();
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void handleMonkeyWaveIgnoreKeyDown(AttackInfo attackInfo) {
        if (!chr.hasSkill(MONKEY_WAVE)) {
            return;
        }
        if (!attackInfo.didCrit(chr)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(MONKEY_WAVE_IMMEDIATE);
        int slv = chr.getSkillLevel(MONKEY_WAVE);
        if (!Util.succeedProp(si.getValue(prop, slv))) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        o1.nOption = 1;
        o1.rOption = MONKEY_WAVE_IMMEDIATE;
        o1.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(KeyDownTimeIgnore, o1);
        tsm.sendSetStatPacket();
    }

    private void applyBarrelRouletteDebuffOnMob(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(Roulette)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(BARREL_ROULETTE);
        int slv = chr.getSkillLevel(BARREL_ROULETTE);
        int rouletteType = tsm.getOption(Roulette).nOption;
        int slowProc = si.getValue(w, slv);
        Option slowOpt = new Option();
        slowOpt.nOption = si.getValue(u, slv); // hopefully correct skillstat
        slowOpt.rOption = BARREL_ROULETTE;
        slowOpt.tOption = si.getValue(v, slv);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            if (rouletteType == 3) { // Slow
                if (Util.succeedProp(slowProc)) {
                    mts.addStatOptionsAndBroadcast(MobStat.Speed, slowOpt);
                }
            } else if (rouletteType == 4) { // Dot
                mts.createAndAddBurnedInfo(chr, BARREL_ROULETTE, slv);
            }
        }
    }

    @Override
    public int getFinalAttackSkill() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(Roulette) && tsm.getOption(Roulette).nOption == 1) {
            if (Util.succeedProp(chr.getSkillStatValue(z, BARREL_ROULETTE))) {
                return 5310004;
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
        Option o5 = new Option();
        Option o6 = new Option();
        Option o7 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case MONKEY_MAGIC:
            case MEGA_MONKEY_MAGIC:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieAcc, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieACC, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieAllStat, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAllStat, o2);
                o3.nReason = skillID;
                o3.nValue = si.getValue(indieEva, slv);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieEVA, o3);
                o4.nReason = skillID;
                o4.nValue = si.getValue(indieJump, slv);
                o4.tStart = Util.getCurrentTime();
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieJump, o4);
                o5.nReason = skillID;
                o5.nValue = si.getValue(indieMhp, slv);
                o5.tStart = Util.getCurrentTime();
                o5.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHP, o5);
                o6.nReason = skillID;
                o6.nValue = si.getValue(indieMmp, slv);
                o6.tStart = Util.getCurrentTime();
                o6.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMMP, o6);
                o7.nReason = skillID;
                o7.nValue = si.getValue(indieSpeed, slv);
                o7.tStart = Util.getCurrentTime();
                o7.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieSpeed, o7);
                break;
            case BARREL_ROULETTE:
                int roulette = Util.getRandom(1, 4);
                o1.nOption = roulette;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Roulette, o1);
                giveBarrelRouletteBuff(roulette);
                // Fallthrough intended
            case LUCK_OF_THE_DIE:
            case LUCK_OF_THE_DIE_DD:
                chr.reduceSkillCoolTime(NAUTILUS_STRIKE_CANNON, (long) (chr.getRemainingCoolTime(NAUTILUS_STRIKE_CANNON) * 0.5D));
                break;
            case PIRATE_SPIRIT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stance, o1);
                break;
            case BUCKSHOT:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BuckShot, o1);
                break;
            case ROLLING_RAINBOW: //Stationary, Attacks
                summon = Summon.getSummonBy(chr, skillID, slv);
                summon.setFlyMob(false);
                summon.setMoveAction((byte) 0);
                summon.setMoveAbility(MoveAbility.Stop);
                chr.getField().spawnSummon(summon);
                break;
            case MONKEY_MILITIA: //Stationary, Attacks
                for (int summonId : MONKEY_MILITIA_SUMMONS) {
                    summon = Summon.getSummonBy(chr, summonId, slv);
                    summon.setFlyMob(false);
                    summon.setMoveAction((byte) 0);
                    summon.setMoveAbility(MoveAbility.Stop);
                    chr.getField().spawnSummon(summon);
                }
                break;
            case ANCHOR_AWEIGH: //Stationary, Pulls mobs
                if (tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                }
                summon = Summon.getSummonBy(chr, skillID, slv);
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                Position position = inPacket.decodePosition();
                Foothold foothold = chr.getField().findFootHoldBelow(position);
                summon.setCurFoothold((short) foothold.getId());
                summon.setPosition(position);
                chr.getField().spawnSummon(summon);
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
