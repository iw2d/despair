package net.swordie.ms.client.jobs.adventurer.magician;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class FirePoison extends Magician {
    public static final int MP_EATER_FP = 2100000;
    public static final int SPELL_MASTERY_FP = 2100006;
    public static final int POISON_BREATH = 2101005;
    public static final int MAGIC_BOOSTER_FP = 2101008;
    public static final int MEDITATION_FP = 2101001;
    public static final int IGNITE = 2101010;
    public static final int IGNITE_AA = 2100010;
    public static final int BURNING_MAGIC = 2110000;
    public static final int POISON_MIST = 2111003;
    public static final int TELEPORT_MASTERY_FP = 2111007;
    public static final int ELEMENTAL_DECREASE_FP = 2111008;
    public static final int ELEMENTAL_ADAPTATION_FP = 2111011;
    public static final int VIRAL_SLIME = 2111010;
    public static final int PARALYZE = 2121006;
    public static final int MIST_ERUPTION = 2121003;
    public static final int FLAME_HAZE = 2121011;
    public static final int INFINITY_FP = 2121004;
    public static final int IFRIT = 2121005;
    public static final int MAPLE_WARRIOR_FP = 2121000;
    public static final int ELEMENTAL_DRAIN = 2100009;
    public static final int FERVENT_DRAIN = 2120014;
    public static final int METEOR_SHOWER = 2121007;
    public static final int METEOR_SHOWER_FA = 2120013;
    public static final int ARCANE_AIM_FP = 2120010;
    public static final int HEROS_WILL_FP = 2121008;

    public static final int EPIC_ADVENTURE_FP = 2121053;
    public static final int INFERNO_AURA = 2221054;
    public static final int MEGIDDO_FLAME = 2121052;
    public static final int MEGIDDO_FLAME_ATOM = 2121055;
    public static final int POISON_MIST_AFTERMATH = 2120044;
    public static final int POISON_MIST_CRIPPLE = 2120045;
    public static final int PARALYZE_CRIPPLE = 2120047;

    private ScheduledFuture elementalDrainTimer;
    private AtomicInteger viralSlimeCount = new AtomicInteger(0);

    public FirePoison(Char chr) {
        super(chr);
        elementalDrainTimer = EventManager.addFixedRateEvent(this::elementalDrain, 2000, 2000);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isFirePoison(id);
    }



    // Buff related methods --------------------------------------------------------------------------------------------

    public void summonViralSlime(Position position, boolean fromSkillCast) {
        if (fromSkillCast) {
            viralSlimeCount.set(0);
        } else if (viralSlimeCount.incrementAndGet() > chr.getSkillStatValue(x, VIRAL_SLIME)) {
            return;
        }

        Summon viralSlime = Summon.getSummonBy(chr, VIRAL_SLIME, chr.getSkillLevel(VIRAL_SLIME));
        Field field = chr.getField();
        viralSlime.setFlyMob(false);
        viralSlime.setPosition(position);
        viralSlime.setCurFoothold((short) chr.getField().findFootHoldBelow(position).getId());
        viralSlime.setMoveAbility(MoveAbility.WalkRandom);
        viralSlime.setAssistType(AssistType.Attack);
        field.spawnAddSummon(viralSlime);
    }

    @Override
    public void handleMobDeath(Mob mob) {
        MobTemporaryStat mts = mob.getTemporaryStat();
        if (mts.hasBurnFromSkillAndOwner(VIRAL_SLIME, chr.getId())) {
            summonViralSlime(mob.getPosition(), false);
            summonViralSlime(mob.getPosition(), false);
        }
        super.handleMobDeath(mob);
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        byte slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }

        // Ignite
        applyIgniteOnMob(attackInfo);
        if (hasHitMobs) {
            // Megiddo Flame Recreation
            if (attackInfo.skillId == MEGIDDO_FLAME_ATOM) {
                recreateMegiddoFlameForceAtom(skillID, slv, attackInfo);
            }
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        AffectedArea aa;
        switch (attackInfo.skillId) {
            case POISON_BREATH:
                applyDot(attackInfo, skillID, slv, si);
                break;
            case POISON_MIST:
                aa = AffectedArea.getAffectedArea(chr, attackInfo);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                aa.setDelay((short) 9);
                chr.getField().spawnAffectedArea(aa);
                break;
            case TELEPORT_MASTERY_FP:
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (!mob.isBoss()) {
                            mts.addStatOptions(MobStat.Stun, o1);
                        }
                        mts.createAndAddBurnedInfo(chr, skillID, slv, si.getValue(dot, slv), si.getValue(dotInterval, slv), getExtendedDoTTime(si.getValue(dotTime, slv)), 1);
                    }
                }
                break;
            case FLAME_HAZE:
                SkillInfo pmSi = SkillData.getSkillInfoById(POISON_MIST);
                byte pmSlv = (byte) chr.getSkillLevel(POISON_MIST);
                aa = AffectedArea.getPassiveAA(chr, POISON_MIST, pmSlv > 0 ? pmSlv : 1);
                aa.setPosition(chr.getPosition());
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                o2.nOption = si.getValue(x, slv); // already negative in si
                o2.rOption = skill.getSkillId();
                o2.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (!mob.isBoss()) {
                            mts.addStatOptionsAndBroadcast(MobStat.DodgeBodyAttack, o1); //Untouchable (physical dmg) Mob Stat
                        }
                        mts.addStatOptionsAndBroadcast(MobStat.Speed, o2);
                        mts.createAndAddBurnedInfo(chr, skillID, slv, si.getValue(dot, slv), si.getValue(dotInterval, slv), getExtendedDoTTime(si.getValue(dotTime, slv)), 1);
                    }
                    aa.setPosition(mob.getPosition());
                }
                aa.setRect(aa.getPosition().getRectAround(pmSi.getFirstRect()));
                chr.getField().spawnAffectedArea(aa);
                break;
            case PARALYZE:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (!mob.isBoss()) {
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                    int dotDmg = si.getValue(dot, slv);
                    if (chr.hasSkill(PARALYZE_CRIPPLE)) {
                        dotDmg += chr.getSkillStatValue(dot, PARALYZE_CRIPPLE);
                    }
                    mts.createAndAddBurnedInfo(chr, skillID, slv, dotDmg, si.getValue(dotInterval, slv), getExtendedDoTTime(si.getValue(dotTime, slv)), 1);
                }
                break;
            case MIST_ERUPTION:
                for (int id : attackInfo.mists) {
                    Field field = chr.getField();
                    field.removeLife(id);
                }
                break;
            case VIRAL_SLIME:
                Summon viralSlime = attackInfo.summon;
                chr.getField().removeLife(viralSlime.getObjectId(), true);
                tsm.removeStatsBySkill(VIRAL_SLIME);
                applyDot(attackInfo, skillID, slv, si);
                break;
            case IFRIT:
                applyDot(attackInfo, skillID, slv, si);
                break;
            case MEGIDDO_FLAME_ATOM:
                applyDot(attackInfo, MEGIDDO_FLAME, chr.getSkillLevel(MEGIDDO_FLAME), SkillData.getSkillInfoById(MEGIDDO_FLAME));
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    public int getExtendedDoTTime(int dotTime) {
        if (chr.hasSkill(BURNING_MAGIC)) {
            dotTime += (chr.getSkillStatValue(x, BURNING_MAGIC) * dotTime) / 100D;
        }
        return dotTime;
    }

    private void createMegiddoFlameForceAtom() {
        Field field = chr.getField();
        SkillInfo si = SkillData.getSkillInfoById(MEGIDDO_FLAME);
        Rect rect = chr.getPosition().getRectAround(si.getFirstRect());
        if (!chr.isLeft()) {
            rect = rect.horizontalFlipAround(chr.getPosition().getX());
        }
        if (field.getMobsInRect(rect).size() > 0) {
            Mob life = Util.getRandomFromCollection(field.getMobsInRect(rect));
            int mobID2 = (life).getObjectId();
            int inc = ForceAtomEnum.DA_ORB.getInc();
            int type = ForceAtomEnum.DA_ORB.getForceAtomType();
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, 20, 40,
                    0, 500, (int) System.currentTimeMillis(), 1, 0,
                    new Position(0, -100));
            chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), type,
                    true, mobID2, MEGIDDO_FLAME_ATOM, forceAtomInfo, new Rect(), 0, 300,
                    life.getPosition(), MEGIDDO_FLAME_ATOM, life.getPosition()));
        }
    }

    private void recreateMegiddoFlameForceAtom(int skillID, byte slv, AttackInfo attackInfo) {
        SkillInfo si = SkillData.getSkillInfoById(MEGIDDO_FLAME);
        int anglenum = new Random().nextInt(360);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            int TW1prop = 85;//
            if (Util.succeedProp(TW1prop)) {
                int mobID = mai.mobId;

                int inc = ForceAtomEnum.DA_ORB_RECREATION.getInc();
                int type = ForceAtomEnum.DA_ORB_RECREATION.getForceAtomType();
                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, 30, 5,
                        anglenum, 0, (int) System.currentTimeMillis(), 1, 0,
                        new Position(0, 0));
                chr.getField().broadcastPacket(FieldPacket.createForceAtom(true, chr.getId(), mobID, type,
                        true, mobID, MEGIDDO_FLAME_ATOM, forceAtomInfo, new Rect(), 0, 300,
                        mob.getPosition(), MEGIDDO_FLAME_ATOM, mob.getPosition()));
            }
        }
    }

    private void applyIgniteOnMob(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        if (si == null || !si.getElemAttr().contains("f") || attackInfo.skillId == IGNITE || attackInfo.skillId == IGNITE_AA) {
            return;
        }
        if (tsm.hasStat(WizardIgnite)) {
            SkillInfo igniteInfo = SkillData.getSkillInfoById(IGNITE);
            Skill skill = chr.getSkill(IGNITE);
            byte slv = (byte) skill.getCurrentLevel();
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                if (Util.succeedProp(igniteInfo.getValue(prop, slv))) {
                    AffectedArea aa = AffectedArea.getPassiveAA(chr, IGNITE_AA, slv);
                    aa.setPosition(mob.deepCopy().getPosition());
                    aa.setRect(aa.getPosition().getRectAround(igniteInfo.getFirstRect()));
                    aa.setDelay((short) 3);
                    chr.getField().spawnAffectedArea(aa);
                }
            }
        }
    }

    private void applyDot(AttackInfo attackInfo, int skillId, int slv, SkillInfo si) {
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            mts.createAndAddBurnedInfo(chr, skillId, slv, si.getValue(dot, slv), si.getValue(dotInterval, slv), getExtendedDoTTime(si.getValue(dotTime, slv)), 1);
        }
    }

    private void elementalDrain() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int dotStacks = (int) chr.getField().getMobs().stream().filter(m -> m.getTemporaryStat().hasBurnFromOwner(chr.getId())).count();
        if (dotStacks == 0) {
            tsm.removeStat(DotBasedBuff, true);
            tsm.sendResetStatPacket();
            return;
        }

        int skillId = getElementalDrainSkill();
        if (skillId == 0) {
            return;
        }

        Option o1 = new Option();

        o1.nOption = Math.min(dotStacks, 5);
        o1.rOption = skillId;
        tsm.putCharacterStatValue(DotBasedBuff, o1);
        tsm.sendSetStatPacket();
    }

    private int getElementalDrainSkill() {
        int res = 0;
        if (chr.hasSkill(FERVENT_DRAIN)) {
            res = FERVENT_DRAIN;
        } else if (chr.hasSkill(ELEMENTAL_DRAIN)) {
            res = ELEMENTAL_DRAIN;
        }
        return res;
    }

    @Override
    public int getFinalAttackSkill() {
        SkillInfo si = SkillData.getSkillInfoById(METEOR_SHOWER_FA);
        if (chr.hasSkill(METEOR_SHOWER)) {
            int slv = chr.getSkillLevel(METEOR_SHOWER);
            if (Util.succeedProp(si.getValue(prop, slv))) {
                return METEOR_SHOWER_FA;
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
        switch (skillID) {
            case MEGIDDO_FLAME:
                createMegiddoFlameForceAtom();
                break;
            case IGNITE:
                if (tsm.hasStat(WizardIgnite)) {
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = 0;
                    tsm.putCharacterStatValue(WizardIgnite, o1);
                }
                break;
            case VIRAL_SLIME:
                summonViralSlime(chr.getPosition(), true);
                break;
            case INFERNO_AURA:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(IceAura, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleMobDebuffSkill(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(ELEMENTAL_ADAPTATION_FP) && tsm.getOptByCTSAndSkill(AntiMagicShell, ELEMENTAL_ADAPTATION_FP) != null) {
            deductEleAdaptationFP();
            tsm.removeAllDebuffs();
        }
        super.handleMobDebuffSkill(chr);
    }

    @Override
    public void handleCancelTimer(Char chr) {
        if (elementalDrainTimer != null && !elementalDrainTimer.isDone()) {
            elementalDrainTimer.cancel(true);
        }
        super.handleCancelTimer(chr);
    }

    // Elemental Adaptation - FP
    private void deductEleAdaptationFP() {
        if(!chr.hasSkill(ELEMENTAL_ADAPTATION_FP)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(ELEMENTAL_ADAPTATION_FP);
        int slv = chr.getSkillLevel(ELEMENTAL_ADAPTATION_FP);
        int proc = si.getValue(prop, slv);

        Option o = new Option();
        int stack = tsm.getOptByCTSAndSkill(AntiMagicShell, ELEMENTAL_ADAPTATION_FP).nOption;
        if (stack > 0) {
            if(Util.succeedProp(proc)) {
                stack--;

                o.nOption = stack;
                o.rOption = ELEMENTAL_ADAPTATION_FP;
                tsm.putCharacterStatValue(AntiMagicShell, o);
                tsm.sendSetStatPacket();
            } else {
                tsm.removeStatsBySkill(ELEMENTAL_ADAPTATION_FP);
                tsm.sendResetStatPacket();
            }
        } else {
            tsm.removeStatsBySkill(ELEMENTAL_ADAPTATION_FP);
            tsm.sendResetStatPacket();
        }
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

