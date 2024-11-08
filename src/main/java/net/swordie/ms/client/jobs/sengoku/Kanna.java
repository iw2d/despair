package net.swordie.ms.client.jobs.sengoku;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.connection.packet.DropPool;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.Stat;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.drop.Drop;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */

public class Kanna extends Job {

    public static final int HAKU = 40020109;

    public static final int RADIANT_PEACOCK = 42101003;
    public static final int NIMBUS_CURSE = 42101005;
    public static final int HAKU_REBORN = 42101002;

    public static final int KISHIN_SHOUKAN = 42111003; //summon
    public static final int BLOSSOM_BARRIER = 42111004; //AoE
    public static final int SOUL_SHEAR = 42111002; //Reactive Skill [4033270 - soul shear balls]
    public static final int SOUL_SHEAR_BOMB_ITEM_ID = 4033270;

    public static final int MONKEY_SPIRITS = 42120003; //Passive activation summon
    public static final int BELLFLOWER_BARRIER = 42121005; //AoE
    public static final int AKATUSKI_HERO_KANNA = 42121006;
    public static final int NINE_TAILED_FURY = 42121024; //Attacking Skill + Buff
    public static final int BINDING_TEMPEST = 42121004;
    public static final int BLOSSOMING_DAWN = 42121007;

    public static final int VERITABLE_PANDEMONIUM = 42121052; //Immobility Debuff
    public static final int PRINCESS_VOW_KANNA = 42121053;
    public static final int BLACKHEARTED_CURSE = 42121054;

    //Haku Buffs
    public static final int HAKUS_GIFT = 42121020;
    public static final int FOXFIRE = 42121021;
    public static final int HAKUS_BLESSING = 42121022;
    public static final int BREATH_UNSEEN = 42121023;

    public Kanna(Char chr) {
        super(chr);

    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isKanna(id);
    }


    public static void hakuFoxFire(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(FOXFIRE);
        int slv = si.getCurrentLevel();
        Option o1 = new Option();

        o1.nOption = 6;
        o1.rOption = FOXFIRE;
        o1.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(FireBarrier, o1);
        tsm.sendSetStatPacket();
    }

    public static void hakuHakuBlessing(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(HAKUS_BLESSING);
        int slv = si.getCurrentLevel();
        Option o1 = new Option();

        o1.nReason = HAKUS_BLESSING;
        o1.nValue = si.getValue(indiePdd, slv);
        o1.tStart = Util.getCurrentTime();
        o1.tTerm = si.getValue(time, slv);
        tsm.putCharacterStatValue(IndieDEF, o1);
        tsm.sendSetStatPacket();
    }

    public static void hakuBreathUnseen(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(BREATH_UNSEEN);
        int slv = si.getCurrentLevel();
        Option o1 = new Option();
        Option o2 = new Option();

        o1.nOption = si.getValue(prop, slv);
        o1.rOption = BREATH_UNSEEN;
        o1.tOption = si.getValue(time, slv);
        tsm.putCharacterStatValue(Stance, o1);
        o2.nOption = si.getValue(x, slv);
        o2.rOption = BREATH_UNSEEN;
        o2.tOption = si.getValue(time, slv);
        o2.bOption = 1;
        tsm.putCharacterStatValue(IgnoreMobpdpR, o2);
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
        dropSoulShearBomb(attackInfo);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case BINDING_TEMPEST:
            case VERITABLE_PANDEMONIUM:
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
            case NIMBUS_CURSE:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, (byte) slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                aa.setDelay((short) 5);
                chr.getField().spawnAffectedArea(aa);
                break;
            case SOUL_SHEAR:
                explodeSoulShearBomb();
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void dropSoulShearBomb(AttackInfo attackInfo) {
        if (!chr.hasSkill(SOUL_SHEAR)) {
            return;
        }
        Field field = chr.getField();
        Skill skill = chr.getSkill(SOUL_SHEAR);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();
        int proc = si.getValue(prop, slv);
        for(MobAttackInfo mai : attackInfo.mobAttackInfo) {
            if (Util.succeedProp(proc)) {
                Mob mob = (Mob) field.getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                Item item = ItemData.getItemDeepCopy(SOUL_SHEAR_BOMB_ITEM_ID);
                Drop drop = new Drop(item.getItemId(), item);
                field.drop(drop, mob.getPosition());
            }
        }
    }

    private void explodeSoulShearBomb() {
        Set<Drop> soulShearBombSet = chr.getField().getDrops().stream().filter(d -> !d.isMoney() && d.getItem().getItemId() == SOUL_SHEAR_BOMB_ITEM_ID).collect(Collectors.toSet());
        for(Drop soulShearBomb : soulShearBombSet) {
            chr.getField().broadcastPacket(DropPool.dropExplodeField(soulShearBomb.getObjectId()));
            soulShearBomb.broadcastLeavePacket();
        }
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
        Option o4 = new Option();
        Option o5 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case BLOSSOM_BARRIER:
            case BELLFLOWER_BARRIER:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                aa.setDelay((short) 3);
                chr.getField().spawnAffectedArea(aa);
                break;
            case NINE_TAILED_FURY:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o1); //Indie
                tsm.sendSetStatPacket();
                break;
            case HAKU_REBORN:
                o1.nOption = 0;
                o1.rOption = skillID;
                o1.tOption = 30;
                tsm.putCharacterStatValue(ChangeFoxMan, o1);
                break;
            case RADIANT_PEACOCK:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case KISHIN_SHOUKAN: //TODO
                chr.getField().setKishin(true);

                Summon.summonKishin(chr, slv);
                break;
            case BLACKHEARTED_CURSE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BlackHeartedCurse, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        int foxfires = 6;
        if (tsm.hasStat(FireBarrier)) {
            if(foxfires > 1) {
                foxfires = foxfires - 1;
                }
            if(foxfires == 4 || foxfires == 3) {
                o.nOption = 2;
                tsm.putCharacterStatValue(FireBarrier, o);
                tsm.sendSetStatPacket();
            } else if(foxfires == 2) {
                o.nOption = 1;
                tsm.putCharacterStatValue(FireBarrier, o);
                tsm.sendSetStatPacket();
            } else if (foxfires == 1) {
                resetFireBarrier();
                o.nOption = 0;
                tsm.putCharacterStatValue(FireBarrier, o);
                tsm.sendSetStatPacket();
            }
        }
        super.handleHit(chr, hitInfo);
    }

    public void resetFireBarrier() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.removeStat(FireBarrier, false);
        tsm.sendResetStatPacket();
    }

    @Override
    public void handleWarp() {
        // spawn haku
        chr.write(FieldPacket.enterFieldFoxMan(chr));
        super.handleWarp();
    }

    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        cs.setPosMap(807040000);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
        if (chr.getLevel() == 60) {
            chr.setJob(JobConstants.JobEnum.KANNA3.getJobId());
            chr.setSpToCurrentJob(3);
            Map<Stat, Object> stats = new HashMap<>();
            stats.put(Stat.sp, chr.getAvatarData().getCharacterStat().getExtendSP());
            chr.getClient().write(WvsContext.statChanged(stats));
        }
        if (chr.getLevel() == 100) {
            chr.setJob(JobConstants.JobEnum.KANNA4.getJobId());
            chr.setSpToCurrentJob(3);
            Map<Stat, Object> stats = new HashMap<>();
            stats.put(Stat.sp, chr.getAvatarData().getCharacterStat().getExtendSP());
            chr.getClient().write(WvsContext.statChanged(stats));
        }
    }

}