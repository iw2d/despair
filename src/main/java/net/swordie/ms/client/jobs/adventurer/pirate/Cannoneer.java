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

import java.util.Random;

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
    public static final int BARREL_ROULETTE = 5311004; //Buff
    public static final int LUCK_OF_THE_DIE = 5311005; //Buff

    public static final int LUCK_OF_THE_DIE_DD = 5320007;
    public static final int ANCHOR_AWEIGH = 5321003; //Summon
    public static final int MONKEY_MALITIA = 5321004; //Summon
    public static final int NAUTILUS_STRIKE_CANNON = 5321001; //Special Attack / Buff
    public static final int PIRATE_SPIRIT = 5321010; //Buff
    public static final int MAPLE_WARRIOR_CANNON = 5321005; //Buff
    public static final int HEROS_WILL_CANNON = 5321006;
    public static final int MEGA_MONKEY_MAGIC = 5320008;

    public static final int EPIC_ADVENTURER_CANNON = 5321053;
    public static final int BUCKSHOT = 5321054;
    public static final int ROLLING_RAINBOW = 5321052;


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
                tsm.putCharacterStatValue(IncCriticalDamMin, o);
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
            // Barrel Roulette
            applyBarrelRouletteDebuffOnMob(attackInfo);

            // Monkey Wave Ignore KeyDown Time
            if (chr.hasSkill(MONKEY_WAVE)) {
                Skill mwskill = chr.getSkill(MONKEY_WAVE);
                SkillInfo mwsi = SkillData.getSkillInfoById(MONKEY_WAVE);
                byte mwslv = (byte) mwskill.getCurrentLevel();
                if (Util.succeedProp(mwsi.getValue(w, mwslv)) && !(tsm.getOption(KeyDownTimeIgnore).nOption > 0) && attackInfo.skillId != 5310008) {
                    o1.nOption = 1;
                    o1.rOption = 5310008;
                    o1.tOption = 15; // doesn't have an assigned skillStat
                    tsm.putCharacterStatValue(KeyDownTimeIgnore, o1);
                    tsm.sendSetStatPacket();
                }
            }
        }
        switch (attackInfo.skillId) {
            case BLAST_BACK:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = si.getValue(z, slv);
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
                    }
                }
                break;
            case MONKEY_WAVE:
                for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
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
                }
                if(tsm.hasStat(KeyDownTimeIgnore) && tsm.getOption(KeyDownTimeIgnore).nOption > 0) {
                    tsm.removeStatsBySkill(5310008);
                    tsm.removeStat(KeyDownTimeIgnore, true);
                    tsm.sendResetStatPacket();
                }
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(subTime, slv);
                tsm.putCharacterStatValue(IncCriticalDamMax, o2);
                tsm.sendSetStatPacket();
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void applyBarrelRouletteDebuffOnMob(AttackInfo attackInfo) {
        if(chr.hasSkill(BARREL_ROULETTE)) {
            TemporaryStatManager tsm = chr.getTemporaryStatManager();
            Option o = new Option();
            Skill skill = chr.getSkill(BARREL_ROULETTE);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            if(tsm.hasStat(Roulette)) {
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (tsm.hasStat(Roulette) && tsm.getOption(Roulette).nOption == 4) {  //DoT Debuff
                        mts.createAndAddBurnedInfo(chr, skill);
                    } else if (tsm.hasStat(Roulette) && tsm.getOption(Roulette).nOption == 3) {  //Slow Debuff
                        int slowProc = si.getValue(w, slv);
                        if (Util.succeedProp(slowProc)) {
                            o.nOption = -20;
                            o.rOption = skill.getSkillId();
                            o.tOption = si.getValue(v, slv);
                            mts.addStatOptionsAndBroadcast(MobStat.Speed, o);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getFinalAttackSkill() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(Roulette) && tsm.getOption(Roulette).nOption == 1) {
            SkillInfo si = SkillData.getSkillInfoById(BARREL_ROULETTE);
            if (Util.succeedProp(chr.getSkillStatValue(z, BARREL_ROULETTE))) {
                return 5310008;
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
                int roulette = new Random().nextInt(4)+1;
                o1.nOption = roulette;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Roulette, o1);
                giveBarrelRouletteBuff(roulette);
                chr.reduceSkillCoolTime(NAUTILUS_STRIKE_CANNON, (long) (chr.getRemainingCoolTime(NAUTILUS_STRIKE_CANNON) * 0.5F));
                break;
            case PIRATE_SPIRIT:
                o1.nOption = si.getValue(prop, slv);
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
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setMoveAction((byte) 0);
                summon.setMoveAbility(MoveAbility.Stop);
                field.spawnSummon(summon);
                break;
            case MONKEY_MALITIA: //Stationary, Attacks
                int[] summons = new int[] {
                        5320011,
                        5321004
                };
                for(int summonZ : summons) {
                    summon = Summon.getSummonBy(chr, summonZ, slv);
                    field = chr.getField();
                    summon.setFlyMob(false);
                    summon.setMoveAction((byte) 0);
                    summon.setMoveAbility(MoveAbility.Stop);
                    field.spawnSummon(summon);
                }
                break;
            case ANCHOR_AWEIGH: //Stationary, Pulls mobs
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setMoveAbility(MoveAbility.Stop);
                Position position = new Position(chr.isLeft() ? chr.getPosition().getX() - 250 : chr.getPosition().getX() + 250, chr.getPosition().getY());
                summon.setCurFoothold((short) chr.getField().findFootHoldBelow(position).getId());
                summon.setPosition(position);
                summon.setSummonTerm(20);
                field.spawnSummon(summon);
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
