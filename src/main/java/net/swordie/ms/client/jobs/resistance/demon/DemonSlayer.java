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
public class DemonSlayer extends Job {
    public static final int CURSE_OF_FURY = 30010111;

    public static final int GRIM_SCYTHE = 31001000; //Special Attack            //TODO (Demon Force)
    public static final int BATTLE_PACT_DS = 31001001; //Buff

    public static final int SOUL_EATER = 31101000; //Special Attack             //TODO (Demon Force)
    public static final int DARK_THRUST = 31101001; //Special Attack            //TODO (Demon Force)
    public static final int CHAOS_LOCK = 31101002; //Special Attack  -Stun-     //TODO (Demon Force)
    public static final int VENGEANCE = 31101003; //Buff (Stun Debuff)

    public static final int JUDGEMENT = 31111000; //Special Attack              //TODO (Demon Force)
    public static final int VORTEX_OF_DOOM = 31111001; //Special Attack  -Stun- //TODO (Demon Force)
    public static final int RAVEN_STORM = 31111003; //Special Attack -GainHP-   //TODO (Demon Force)
    public static final int CARRION_BREATH = 31111005; //Special Attack  -DoT-  //TODO (Demon Force)
    public static final int POSSESSED_AEGIS = 31110008;
    public static final int MAX_FURY = 31110009;

    public static final int INFERNAL_CONCUSSION = 31121000; //Special Attack    //TODO (Demon Force)
    public static final int DEMON_IMPACT = 31121001; //Special Attack  -Slow-   //TODO (Demon Force)
    public static final int DEMON_CRY = 31121003; //Special Attack -DemonCry-   //TODO (Demon Force)
    public static final int BINDING_DARKNESS = 31121006; //Special Attack -Bind-//TODO (Demon Force)
    public static final int DARK_METAMORPHOSIS = 31121005; //Buff               //TODO (Demon Force)
    public static final int BOUNDLESS_RAGE = 31121007; //Buff                   //TODO (Demon Force)
    public static final int LEECH_AURA = 31121002; //Buff                       //TODO (Demon Force)
    public static final int MAPLE_WARRIOR_DS = 31121004; //Buff

    public static final int BLUE_BLOOD = 31121054;
    public static final int DEMONIC_FORTITUDE_DS = 31121053;
    public static final int CERBERUS_CHOMP = 31121052;

    public static final int DEMON_LASH = 31000004;
    public static final int DEMON_LASH_2 = 31001006;
    public static final int DEMON_LASH_3 = 31001007;
    public static final int DEMON_LASH_4 = 31001008;


    private int[] addedSkills = new int[] {
            CURSE_OF_FURY,
    };

    private long leechAuraCD = Long.MIN_VALUE;


    public DemonSlayer(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
            if (chr.hasSkill(MAX_FURY)) {
                //regenDFInterval(); //TODO  WVsCrash
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isDemonSlayer(id);
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
        if (hasHitMobs) {
            //Demon Slayer Fury Atoms
            createDemonFuryForceAtom(attackInfo);

            //Max Fury
            if(chr.hasSkill(MAX_FURY)) {
                if(attackInfo.skillId == DEMON_LASH || attackInfo.skillId == DEMON_LASH_2 || attackInfo.skillId == DEMON_LASH_3 || attackInfo.skillId == DEMON_LASH_4) {
                    Skill maxfuryskill = chr.getSkill(MAX_FURY);
                    SkillInfo mfsi = SkillData.getSkillInfoById(MAX_FURY);
                    byte skillLevel = (byte) maxfuryskill.getCurrentLevel();
                    int propz = mfsi.getValue(prop, skillLevel);
                    if (Util.succeedProp(propz)) {
                        createDemonFuryForceAtom(attackInfo);
                    }
                }
            }

            //Leech Aura
            leechAuraHealing(attackInfo);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case CHAOS_LOCK: //prop Stun/Bind
            case VORTEX_OF_DOOM: //prop
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
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
                break;
            case CARRION_BREATH: //DoT
                for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
            case BINDING_DARKNESS: //stun + DoT
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if(!mob.isBoss()) {
                        o1.nOption = 1;
                        o1.rOption = skillID;
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptions(MobStat.Stun, o1);
                    }
                    if(Util.succeedProp(si.getValue(prop, slv))) {
                        mts.createAndAddBurnedInfo(chr, skillID, slv);
                    }
                }
                break;
            case DEMON_CRY:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = -si.getValue(y, slv);
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptions(MobStat.PAD, o1);
                    mts.addStatOptions(MobStat.PDR, o1);
                    mts.addStatOptions(MobStat.MAD, o1);
                    mts.addStatOptions(MobStat.MDR, o1);
                    o2.nOption = -si.getValue(z, slv);
                    o2.rOption = skillID;
                    o2.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobStat.ACC, o2);
                    o3.nOption = 1;
                    o3.rOption = skillID;
                    o3.tOption = si.getValue(time, slv);
                    o3.xOption = si.getValue(w, slv); // exp
                    o3.yOption = si.getValue(w, slv); // dropRate
                    mts.addStatOptionsAndBroadcast(MobStat.Treasure, o3);
                }
                break;
            case DEMON_IMPACT:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = -20;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
                }
                break;
            case CERBERUS_CHOMP:
                int furyabsorbed = si.getValue(x, slv);
                chr.healMP(furyabsorbed);
                break;
            case RAVEN_STORM:
                int hpheal = (int) (chr.getMaxHP() / ((double) 100 / si.getValue(x, slv)));
                chr.heal(hpheal);
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    public void leechAuraHealing(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if(chr.hasSkill(LEECH_AURA)) {
            if(tsm.getOptByCTSAndSkill(Regen, LEECH_AURA) != null) {
                Skill skill = chr.getSkill(LEECH_AURA);
                byte slv = (byte) skill.getCurrentLevel();
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                int cd = si.getValue(y, slv) * 1000;
                if(cd + leechAuraCD < Util.getCurrentTimeLong()) {
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        int totaldmg = Arrays.stream(mai.damages).sum();
                        int hpheal = (int) (totaldmg * ((double) 100 / si.getValue(x, slv)));
                        if (hpheal >= (chr.getMaxHP() / 4)) {
                            hpheal = (chr.getMaxHP() / 4);
                        }
                        leechAuraCD = Util.getCurrentTimeLong();
                        chr.heal(hpheal);
                    }
                }
            }
        }
    }

    private void createDemonFuryForceAtom(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            int mobID = mai.mobId;
            int angle = new Random().nextInt(40)+30;
            int speed = new Random().nextInt(31)+29;

            //Attacking with Demon Lash
            if(attackInfo.skillId == DEMON_LASH || attackInfo.skillId == DEMON_LASH_2 || attackInfo.skillId == DEMON_LASH_3 || attackInfo.skillId == DEMON_LASH_4) {
                int inc = ForceAtomEnum.DEMON_SLAYER_FURY_1.getInc();
                int type = ForceAtomEnum.DEMON_SLAYER_FURY_1.getForceAtomType();
                if(mob.isBoss()) {
                    inc = ForceAtomEnum.DEMON_SLAYER_FURY_1_BOSS.getInc();
                    type = ForceAtomEnum.DEMON_SLAYER_FURY_1_BOSS.getForceAtomType();
                }
                if (chr.getJob() == JobConstants.JobEnum.DEMON_SLAYER4.getJobId()) {
                    inc = ForceAtomEnum.DEMON_SLAYER_FURY_2.getInc();
                    type = ForceAtomEnum.DEMON_SLAYER_FURY_2.getForceAtomType();
                    if(mob.isBoss()) {
                        inc = ForceAtomEnum.DEMON_SLAYER_FURY_2_BOSS.getInc();
                        type = ForceAtomEnum.DEMON_SLAYER_FURY_2_BOSS.getForceAtomType();
                    }
                }
                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, speed, 5,
                        angle, 50, Util.getCurrentTime(), 1, 0,
                        new Position(0, 0));
                chr.write(FieldPacket.createForceAtom(true, chr.getId(), mobID, type,
                        true, mobID, 0, forceAtomInfo, new Rect(), 0, 300,
                        mob.getPosition(), 0, mob.getPosition()));
            } else {

                //Attacking with another skill
                int totaldmg = Arrays.stream(mai.damages).sum();
                if (totaldmg > mob.getHp()) {
                    int inc = ForceAtomEnum.DEMON_SLAYER_FURY_1.getInc();
                    int type = ForceAtomEnum.DEMON_SLAYER_FURY_1.getForceAtomType();
                    if(mob.isBoss()) {
                        inc = ForceAtomEnum.DEMON_SLAYER_FURY_1_BOSS.getInc();
                        type = ForceAtomEnum.DEMON_SLAYER_FURY_1_BOSS.getForceAtomType();
                    }
                    if (chr.getJob() == JobConstants.JobEnum.DEMON_SLAYER4.getJobId()) {
                        inc = ForceAtomEnum.DEMON_SLAYER_FURY_2.getInc();
                        type = ForceAtomEnum.DEMON_SLAYER_FURY_2.getForceAtomType();
                        if(mob.isBoss()) {
                            inc = ForceAtomEnum.DEMON_SLAYER_FURY_2_BOSS.getInc();
                            type = ForceAtomEnum.DEMON_SLAYER_FURY_2_BOSS.getForceAtomType();
                        }
                    }
                    ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, speed, 5,
                            angle, 50, Util.getCurrentTime(), 1, 0,
                            new Position(0, 0));
                    chr.write(FieldPacket.createForceAtom(true, chr.getId(), mobID, type,
                            true, mobID, 0, forceAtomInfo, new Rect(), 0, 300,
                            mob.getPosition(), 0, mob.getPosition()));
                }
            }
        }
    }

    private void createPossessedAegisFuryForceAtom(int mobID) {
        Field field = chr.getField();
        Life life = field.getLifeByObjectID(mobID);
        if (life instanceof Mob) {
            int angle = new Random().nextInt(40)+30;
            int speed = new Random().nextInt(31)+29;
            int inc = ForceAtomEnum.DEMON_SLAYER_FURY_1.getInc();
            int type = ForceAtomEnum.DEMON_SLAYER_FURY_1.getForceAtomType();
            if (chr.getJob() == JobConstants.JobEnum.DEMON_SLAYER4.getJobId()) {
                inc = ForceAtomEnum.DEMON_SLAYER_FURY_2.getInc();
                type = ForceAtomEnum.DEMON_SLAYER_FURY_2.getForceAtomType();
            }
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, speed, 4,
                    angle, 50, Util.getCurrentTime(), 1, 0,
                    new Position(0, 0));
            chr.getField().broadcastPacket(FieldPacket.createForceAtom(true, chr.getId(), mobID, type,
                    true, mobID, 0, forceAtomInfo, new Rect(), 0, 300,
                    life.getPosition(), 0, life.getPosition()));
        }
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
            case VENGEANCE: //stun chance = prop | stun dur. = subTime
                o1.nOption = si.getValue(y, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PowerGuard, o1);
                break;
            case DARK_METAMORPHOSIS:
                o1.nOption = si.getValue(damR, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMhpR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o2);
                o3.nOption = si.getValue(damage, slv); //?
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PowerGuard, o3);
                o4.nOption = 1;
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DevilishPower, o4);
                break;
            case BOUNDLESS_RAGE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(InfinityForce, o1);
                break;
            case LEECH_AURA:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Regen, o1);
                break;
            case BLUE_BLOOD:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ShadowPartner, o1);
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public int alterCooldownSkill(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(skillId);
        if(skill != null) {
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            byte slv = (byte) skill.getCurrentLevel();

            switch (skillId) {
                case DEMON_CRY:
                    if (tsm.hasStat(InfinityForce)) {
                        return si.getValue(s, slv) * 1000;
                    }
            }
        }
        return super.alterCooldownSkill(skillId);
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();

        //Vengeance
        if(tsm.getOptByCTSAndSkill(PowerGuard, VENGEANCE) != null) {
            if(hitInfo.hpDamage != 0) {
                Skill skill = chr.getSkill(VENGEANCE);
                byte slv = (byte) skill.getCurrentLevel();
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                int mobID = hitInfo.mobID;
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mobID);
                if(mob == null) {
                    return;
                }
                MobTemporaryStat mts = mob.getTemporaryStat();
                if (Util.succeedProp(si.getValue(prop, slv))) {
                    o1.nOption = 1;
                    o1.rOption = skill.getSkillId();
                    o1.tOption = si.getValue(subTime, slv);
                    o1.bOption = 1;
                    mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                }
            }
        }

        //Possessed Aegis
        if(hitInfo.hpDamage == 0 && hitInfo.mpDamage == 0) {
            // Guarded
            if(chr.hasSkill(POSSESSED_AEGIS)) {
                Skill skill = chr.getSkill(POSSESSED_AEGIS);
                byte slv = (byte) skill.getCurrentLevel();
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                int propz = si.getValue(x, slv);
                if(Util.succeedProp(propz)) {
                    int mobID = hitInfo.mobID;
                    createPossessedAegisFuryForceAtom(mobID);
                    chr.heal((int) (chr.getMaxHP() / ((double) 100 / si.getValue(y, slv))));
                }
            }
        }
        super.handleHit(chr, hitInfo);
    }

}
