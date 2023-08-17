package net.swordie.ms.client.jobs.legend;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.BodyPart;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.enums.Stat;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Mercedes extends Job {
    //Link Skill = return skill

    public static final int ELVEN_GRACE = 20020112;
    public static final int UPDRAFT = 20020111;
    public static final int ELVEN_HEALING = 20020109;

    public static final int DUAL_BOWGUNS_MASTERY = 23100005;
    public static final int DUAL_BOWGUNS_BOOST = 23101002; //Buff

    public static final int RISING_RUSH = 23101001;
    public static final int RISING_RUSH_LAUNCH = 23101007;
    public static final int STUNNING_STRIKES = 23111000; //Special Attack
    public static final int UNICORN_SPIKE = 23111002; //Special Attack
    public static final int IGNIS_ROAR = 23111004; //Buff
    public static final int WATER_SHIELD = 23111005; //Buff
    public static final int ELEMENTAL_KNIGHTS_BLUE = 23111008; //Summon
    public static final int ELEMENTAL_KNIGHTS_RED = 23111009; //Summon
    public static final int ELEMENTAL_KNIGHTS_PURPLE = 23111010; //Summon

    public static final int DUAL_BOWGUNS_EXPERT = 23120009;
    public static final int ISHTARS_RING = 23121000;
    public static final int SPIRIT_NIMBLE_FLIGHT = 23121014;
    public static final int SPIRIT_NIMBLE_FLIGHT_VERTICAL = 23121015;
    public static final int SPIKES_ROYALE = 23121002;  //Special Attack
    public static final int STAGGERING_STRIKES = 23120013; //Special Attack
    public static final int ANCIENT_WARDING = 23121004; //Buff
    public static final int MAPLE_WARRIOR_MERC = 23121005; //Buff
    public static final int LIGHTNING_EDGE = 23121003; //Debuff mobs
    public static final int HEROS_WILL_MERC = 23121008;

    public static final int WATER_SHIELD_REINFORCE = 23120046;
    public static final int WATER_SHIELD_TRUE_1 = 23120047;
    public static final int WATER_SHIELD_TRUE_2 = 23120048;
    public static final int SPIKES_ROYALE_ARMORBREAK = 23120050;
    public static final int HEROIC_MEMORIES_MERC = 23121053;
    public static final int ELVISH_BLESSING = 23121054;
    public static final int WRATH_OF_ENLIL = 23121052;

    //Final Attack
    public static final int FINAL_ATTACK_DBG = 23100006;
    public static final int ADVANCED_FINAL_ATTACK = 23120012;

    private static final List<Integer> ELEMENTAL_KNIGHT_SKILLS = List.of(
            ELEMENTAL_KNIGHTS_BLUE,
            ELEMENTAL_KNIGHTS_RED,
            ELEMENTAL_KNIGHTS_PURPLE
    );

    private ScheduledFuture elvenHealingTimer;

    public Mercedes(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            elvenHealingTimer = EventManager.addFixedRateEvent(this::elvenHealing, 4000, 4000);
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isMercedes(id);
    }

    private void elvenHealing() {
        if (chr.hasSkill(ELVEN_HEALING) && chr.getHP() > 0) {
            int healRate = chr.getSkillStatValue(x, ELVEN_HEALING);
            chr.heal((int) (chr.getMaxHP() * (healRate / 100D)));
            chr.healMP((int) (chr.getMaxMP() * (healRate / 100D)));
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
        if (hasHitMobs) {
        }
        if (attackInfo.addAttackProc != 0) {
            // link skill used
            handleIgnisRoar();
            handleCooldownReduce();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case RISING_RUSH_LAUNCH:
                o1.nOption = chr.getSkillStatValue(x, RISING_RUSH);
                o1.rOption = skillID;
                o1.tOption = 1;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.RiseByToss, o1);
                }
                break;
            case STUNNING_STRIKES:
            case STAGGERING_STRIKES:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                int proc = si.getValue(prop, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if(mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(proc)) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case UNICORN_SPIKE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.pOption = chr.getPartyID();
                o1.wOption = chr.getId();
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.AddDamParty, o1);
                    }
                }
                break;
            case SPIKES_ROYALE:
                o1.nOption = -si.getValue(y, slv) + this.chr.getSkillStatValue(x, SPIKES_ROYALE_ARMORBREAK); // x already negative
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptions(MobStat.PDR, o1);
                    mts.addStatOptionsAndBroadcast(MobStat.MDR, o1);
                }
                break;
            case LIGHTNING_EDGE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = ISHTARS_RING;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (mob.isBoss()) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.AddDamSkill, o1);
                    }
                }
                break;
            case ELEMENTAL_KNIGHTS_BLUE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(subTime, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                    }
                }
                break;
            case ELEMENTAL_KNIGHTS_RED:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void handleIgnisRoar() {
        if (!chr.hasSkill(IGNIS_ROAR)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(IgnisRore)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(IGNIS_ROAR);
        int slv = chr.getSkillLevel(IGNIS_ROAR);
        int stacks = 0;
        if (tsm.hasStat(AddAttackCount)) {
            stacks = tsm.getOption(AddAttackCount).nOption;
        }
        if (stacks < si.getValue(y, slv)) {
            stacks++;
        }
        Option o1 = new Option();
        o1.nOption = stacks;
        o1.rOption = IGNIS_ROAR;
        o1.tOption = si.getValue(subTime, slv);
        tsm.putCharacterStatValue(AddAttackCount, o1);
        tsm.sendSetStatPacket();
    }

    private void handleCooldownReduce() {
        if (chr.hasSkillOnCooldown(UNICORN_SPIKE)) {
            chr.reduceSkillCoolTime(UNICORN_SPIKE, 1000);
        }
        if (chr.hasSkillOnCooldown(SPIKES_ROYALE)) {
            chr.reduceSkillCoolTime(SPIKES_ROYALE, 1000);
        }
        if (chr.hasSkillOnCooldown(WRATH_OF_ENLIL)) {
            chr.reduceSkillCoolTime(WRATH_OF_ENLIL, 1000);
        }
    }

    @Override
    public int getFinalAttackSkill() {
        int skillId = 0;
        if (chr.hasSkill(ADVANCED_FINAL_ATTACK)) {
            skillId = ADVANCED_FINAL_ATTACK;
        } else if (chr.hasSkill(FINAL_ATTACK_DBG)) {
            skillId = FINAL_ATTACK_DBG;
        }
        if (skillId > 0) {
            SkillInfo si = SkillData.getSkillInfoById(skillId);
            int slv = chr.getSkillLevel(skillId);
            int proc = si.getValue(prop, slv);
            if (Util.succeedProp(proc)) {
                return 0;
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
        Summon summon;
        Field field;
        switch(skillID) {
            case DUAL_BOWGUNS_BOOST:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case IGNIS_ROAR:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(IgnisRore, o1);
                o2.nValue = si.getValue(indiePad, slv);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o2);
                break;
            case WATER_SHIELD:
                o1.nOption = si.getValue(asrR, slv) + this.chr.getSkillStatValue(x, WATER_SHIELD_TRUE_1);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o1);
                o2.nOption = si.getValue(terR, slv) + this.chr.getSkillStatValue(x, WATER_SHIELD_TRUE_2);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(TerR, o2);
                o3.nOption = si.getValue(x, slv) + this.chr.getSkillStatValue(x, WATER_SHIELD_REINFORCE);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamageReduce, o3);
                break;
            case ANCIENT_WARDING:
                o1.nOption = si.getValue(emhp, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EMHP, o1);
                o2.nValue = si.getValue(indiePadR, slv);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePADR, o2);
                break;
            case ELVISH_BLESSING:
                o1.nValue = si.getValue(indiePad, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stance, o2);
                break;
            case ELEMENTAL_KNIGHTS_BLUE:
                List<Integer> existingSummons = tsm.getOptions(IndieEmpty).stream()
                        .map(o -> o.nReason)
                        .filter(ELEMENTAL_KNIGHT_SKILLS::contains)
                        .toList();
                List<Integer> summonIds = new ArrayList<>();
                summonIds.addAll(ELEMENTAL_KNIGHT_SKILLS);
                Collections.shuffle(summonIds);
                for (int summonId : summonIds) {
                    if (!existingSummons.contains(summonId)) {
                        summon = Summon.getSummonBy(chr, summonId, slv);
                        summon.setMoveAbility(MoveAbility.Fly);
                        chr.getField().spawnSummon(summon);
                        break;
                    }
                }
                if (existingSummons.size() >= 2) {
                    tsm.removeStatsBySkill(IndieEmpty, existingSummons.get(0));
                    tsm.sendResetStatPacket();
                }
                break;
            case SPIRIT_NIMBLE_FLIGHT_VERTICAL:
                chr.setSkillCooldown(SPIRIT_NIMBLE_FLIGHT, slv);
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
    public void handleCancelTimer() {
        if (elvenHealingTimer != null && !elvenHealingTimer.isDone()) {
            elvenHealingTimer.cancel(true);
        }
        super.handleCancelTimer();
    }

    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        chr.getAvatarData().getAvatarLook().setDrawElfEar(true);
        Item item = ItemData.getItemDeepCopy(1352000); // Secondary
        item.setBagIndex(BodyPart.Shield.getVal());
        chr.getEquippedInventory().addItem(item);
        chr.getAvatarData().getCharacterStat().setPosMap(910150000);

        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        cs.setLevel(10);
        cs.setStr(4);
        cs.setDex(58);
        cs.setMaxHp(300);
        cs.setMaxMp(200);
        Map<Stat, Object> stats = new HashMap<>();
        stats.put(Stat.mhp, chr.getStat(Stat.mhp));
        stats.put(Stat.mmp, chr.getStat(Stat.mmp));
        chr.write(WvsContext.statChanged(stats));
    }

    @Override
    public void handleSetJob(short jobId) {
        if (JobConstants.isMercedes(jobId)) {
            chr.addSkill(ELVEN_GRACE, 1, 1);
            chr.addSkill(UPDRAFT, 1, 1);
            chr.addSkill(ELVEN_HEALING, 1, 1);
        }
        super.handleSetJob(jobId);
    }
}
