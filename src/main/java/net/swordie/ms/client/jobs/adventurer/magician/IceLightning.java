package net.swordie.ms.client.jobs.adventurer.magician;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class IceLightning extends Magician {
    public static final int MP_EATER_IL = 2200000;
    public static final int SPELL_MASTERY_IL = 2200006;
    public static final int CHILLING_STEP = 2201009;
    public static final int COLD_BEAM = 2201008;
    public static final int FREEZING_CRUSH = 2200011;
    public static final int FROST_CLUTCH = 2220015;
    public static final int MAGIC_BOOSTER_IL = 2201010;
    public static final int MEDITATION_IL = 2201001;
    public static final int ICE_STRIKE = 2211002;
    public static final int GLACIER_CHAIN = 2211010;
    public static final int THUNDER_STORM = 2211011;
    public static final int TELEPORT_MASTERY_IL = 2211007;
    public static final int TELEPORT_MASTERY_RANGE_IL = 2221045;
    public static final int ELEMENTAL_DECREASE_IL = 2211008;
    public static final int ELEMENTAL_ADAPTATION_IL = 2211012;
    public static final int CHAIN_LIGHTNING = 2221006;
    public static final int FREEZING_BREATH = 2221011;
    public static final int BLIZZARD = 2221007;
    public static final int BLIZZARD_FA = 2220014;
    public static final int FROZEN_ORB = 2221012;
    public static final int INFINITY_IL = 2221004;
    public static final int ELQUINES = 2221005;
    public static final int ARCANE_AIM_IL = 2220010;
    public static final int MAPLE_WARRIOR_IL = 2221000;
    public static final int HEROS_WILL_IL = 2221008;
    public static final int LIGHTNING_ORB = 2221052;

    public static final int EPIC_ADVENTURE_IL = 2221053;
    public static final int ABSOLUTE_ZERO_AURA = 2221054;

    private ScheduledFuture absoluteZeroAuraTimer;

    public IceLightning(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isIceLightning(id);
    }

    private void giveAbsoluteZeroAuraBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!chr.hasSkill(ABSOLUTE_ZERO_AURA) || !tsm.hasStatBySkillId(ABSOLUTE_ZERO_AURA)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(ABSOLUTE_ZERO_AURA);
        Option o1 = new Option();

        Party party = chr.getParty();
        if (party != null) {
            Rect rect = chr.getRectAround(si.getFirstRect());
            List<Char> pChrList = chr.getParty().getPartyMembersInSameField(chr).stream().filter(pc -> rect.hasPositionInside(pc.getPosition())).toList();
            for (Char pChr : pChrList) {
                if (pChr.getHP() > 0) {
                    o1.nOption = 1;
                    o1.rOption = ABSOLUTE_ZERO_AURA;
                    o1.tOption = 2;
                    o1.bOption = 0;
                    pChr.getTemporaryStatManager().putCharacterStatValue(IceAura, o1);
                    pChr.getTemporaryStatManager().sendSetStatPacket();
                }
            }
        }
        absoluteZeroAuraTimer = EventManager.addEvent(this::giveAbsoluteZeroAuraBuff, 1, TimeUnit.SECONDS);
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
            applyFreezingCrushOnMob(attackInfo, skillID);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case COLD_BEAM:
            case ICE_STRIKE:
            case GLACIER_CHAIN:
                o1.nOption = 5;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                }
                break;
            case TELEPORT_MASTERY_IL:
            case CHAIN_LIGHTNING:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null || mob.isBoss()) {
                            continue;
                        }
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void applyFreezingCrushOnMob(AttackInfo attackInfo, int skillID) {
        if (!SkillConstants.isIceSkill(skillID)){
            return;
        }
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            int counter = 1;
            if(mts.hasStat(MobStat.Speed)) {
                counter = mts.getOption(MobStat.Speed).mOption;
                if (counter < 5) {
                    counter++;
                }
            }
            Option o1 = new Option();
            o1.nOption = 20;
            o1.rOption = skillID;
            o1.tOption = 15; //No Duration given
            o1.mOption = counter;
            mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
        }
    }

    @Override
    public int getFinalAttackSkill() {
        if (chr.hasSkillOnCooldown(BLIZZARD)) {
            SkillInfo si = SkillData.getSkillInfoById(BLIZZARD_FA);
            int slv = chr.getSkillLevel(BLIZZARD);
            if (Util.succeedProp(si.getValue(prop, slv))) {
                return BLIZZARD_FA;
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
        Rect rect;
        Summon summon;
        Field field;
        switch (skillID) {
            case FREEZING_BREATH:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 1;
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = 1;
                o3.nOption = si.getValue(y, slv);
                o3.rOption = skillID;
                o3.tOption = 1;
                rect = chr.getPosition().getRectAround(si.getFirstRect()); // position not encoded
                if (!chr.isLeft()) {
                    rect = rect.horizontalFlipAround(chr.getPosition().getX());
                }
                System.out.println(rect);
                for (Life life : chr.getField().getLifesInRect(rect)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                        mts.addStatOptionsAndBroadcast(MobStat.PDR, o2);
                        mts.addStatOptionsAndBroadcast(MobStat.MDR, o3);
                    }
                }
                if (!tsm.hasStatBySkillId(skillID)) {
                    o4.nOption = 1;
                    o4.rOption = skillID;
                    o4.tOption = 25;
                    tsm.putCharacterStatValue(NotDamaged, o4);
                }
                break;
            case Magician.TELEPORT:
                if (chr.hasSkill(CHILLING_STEP)) {
                    createChillStepAA();
                }
                break;
            case ELEMENTAL_ADAPTATION_IL:
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(AntiMagicShell, o1);
                break;
            case THUNDER_STORM:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(true);
                field.spawnSummon(summon);
                break;
            case CHILLING_STEP:
                // Skill cancel handled by client
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(ChillingStep, o1);
                break;
            case ABSOLUTE_ZERO_AURA:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                o1.bOption = 1;
                tsm.putCharacterStatValue(IceAura, o1);
                if (absoluteZeroAuraTimer != null && !absoluteZeroAuraTimer.isDone()) {
                    absoluteZeroAuraTimer.cancel(true);
                }
                giveAbsoluteZeroAuraBuff();
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void createChillStepAA() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo chillingStepInfo = SkillData.getSkillInfoById(CHILLING_STEP);
        int slv = chr.getSkillLevel(CHILLING_STEP);
        if (tsm.hasStat(ChillingStep) && Util.succeedProp(chillingStepInfo.getValue(prop, slv))) {
            for (int i = 0; i < 168; i += 56) {
                AffectedArea aa = AffectedArea.getPassiveAA(chr, CHILLING_STEP, slv);
                aa.setMobOrigin((byte) 0);
                int x = chr.isLeft() ? chr.getPosition().getX() - i : chr.getPosition().getX() + i;
                int y = chr.getPosition().getY();
                aa.setPosition(new Position(x, y));
                aa.setRect(aa.getPosition().getRectAround(chillingStepInfo.getFirstRect()));
                aa.setCurFoothold();
                aa.setDelay((short) 4);
                aa.setSkillID(CHILLING_STEP);
                aa.setRemoveSkill(false);
                chr.getField().spawnAffectedArea(aa);
            }
        }
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleMobDebuffSkill(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(ELEMENTAL_ADAPTATION_IL) && tsm.getOptByCTSAndSkill(AntiMagicShell, ELEMENTAL_ADAPTATION_IL) != null) {
            if (tsm.getOption(AntiMagicShell).bOption == 0) {
                SkillInfo si = SkillData.getSkillInfoById(ELEMENTAL_ADAPTATION_IL);
                int slv = chr.getSkillLevel(ELEMENTAL_ADAPTATION_IL);

                tsm.removeStatsBySkill(ELEMENTAL_ADAPTATION_IL);
                tsm.sendResetStatPacket();

                Option o1 = new Option();
                o1.nOption = 1;
                o1.rOption = ELEMENTAL_ADAPTATION_IL;
                o1.tOption = si.getValue(time, slv);
                o1.bOption = 1;
                tsm.putCharacterStatValue(AntiMagicShell, o1);
                tsm.sendSetStatPacket();
            }
            tsm.removeAllDebuffs();
            return;
        }
        super.handleMobDebuffSkill(chr);
    }

    @Override
    public void handleRemoveSkill(int skillID) {
        if (skillID == ABSOLUTE_ZERO_AURA && absoluteZeroAuraTimer != null && !absoluteZeroAuraTimer.isDone()) {
            absoluteZeroAuraTimer.cancel(true);
        }
        super.handleRemoveSkill(skillID);
    }

    @Override
    public void handleCancelTimer() {
        if (absoluteZeroAuraTimer != null && !absoluteZeroAuraTimer.isDone()) {
            absoluteZeroAuraTimer.cancel(true);
        }
        super.handleCancelTimer();
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

