package net.swordie.ms.client.jobs.adventurer.warrior;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.BodyPart;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Paladin extends Warrior {
    public static final int WEAPON_MASTERY_PAGE = 1200000;
    public static final int WEAPON_BOOSTER_PAGE = 1201004;
    public static final int FINAL_ATTACK_PAGE = 1200002;
    public static final int CLOSE_COMBAT = 1201013;
    public static final int ELEMENTAL_CHARGE = 1200014;
    public static final int FLAME_CHARGE = 1201011;
    public static final int BLIZZARD_CHARGE = 1201012;
    public static final int SHIELD_MASTERY = 1210001;
    public static final int LIGHTNING_CHARGE = 1211008;
    public static final int HP_RECOVERY = 1211010;
    public static final int COMBAT_ORDERS = 1211011;
    public static final int PARASHOCK_GUARD = 1211014;
    public static final int DIVINE_CHARGE = 1221004;
    public static final int THREATEN = 1211013;
    public static final int ADVANCED_CHARGE = 1220010;
    public static final int HIGH_PALADIN = 1220018;
    public static final int HEAVENS_HAMMER = 1221011;
    public static final int ELEMENTAL_FORCE = 1221015;
    public static final int MAPLE_WARRIOR_PALADIN = 1221000;
    public static final int GUARDIAN = 1221016;
    public static final int BLAST = 1221009;
    public static final int DIVINE_SHIELD = 1210016;
    public static final int MAGIC_CRASH_PALADIN = 1221014;
    public static final int HEROS_WILL_PALADIN = 1221012;

    public static final int EPIC_ADVENTURE_PALA = 1221053; //Lv200
    public static final int SACROSANCTITY = 1221054; //Lv150
    public static final int SMITE_SHIELD = 1221052; //Lv170
    public static final int THREATEN_PERSIST = 1220043;
    public static final int THREATEN_OPPORTUNITY = 1220044;
    public static final int THREATEN_ENHANCE = 1220045;

    private long lastDivineShieldHit = Long.MIN_VALUE;
    private int lastCharge = 0;
    private int divShieldAmount = 0;
    private ScheduledFuture parashockGuardTimer;

    public Paladin(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isPaladin(id);
    }

    private Char getReviveTarget(Rect range) {
        Party party = chr.getParty();
        if (party != null) {
            List<Char> pChrList = chr.getParty().getPartyMembersInSameField(chr).stream().filter(pChr -> range.hasPositionInside(pChr.getPosition())).collect(Collectors.toList());
            Char closestChr = null;
            double closestDst = Double.MIN_VALUE;
            for (Char pChr : pChrList) {
                if (pChr.getHP() <= 0) {
                    double dst = chr.getPosition().distance(pChr.getPosition());
                    if (dst > closestDst) {
                        closestChr = pChr;
                        closestDst = dst;
                    }
                }
            }
            if (closestChr != null) {
                return closestChr;
            }
        }
        return null;
    }


    private void giveParashockGuardBuff() {
        Skill skill = chr.getSkill(PARASHOCK_GUARD);
        SkillInfo si = SkillData.getSkillInfoById(PARASHOCK_GUARD);
        int slv = skill.getCurrentLevel();
        Option o1 = new Option();

        Party party = chr.getParty();
        if (party != null) {
            Rect rect = chr.getRectAround(si.getFirstRect());
            List<Char> pChrList = chr.getParty().getPartyMembersInSameField(chr).stream().filter(pc -> rect.hasPositionInside(pc.getPosition())).collect(Collectors.toList());
            for (Char pChr : pChrList) {
                if (pChr.getHP() > 0) {
                    o1.nOption = slv;
                    o1.rOption = PARASHOCK_GUARD;
                    o1.bOption = 0;
                    o1.tOption = 2;
                    pChr.getTemporaryStatManager().putCharacterStatValue(KnightsAura, o1);
                    pChr.getTemporaryStatManager().sendSetStatPacket();
                }
            }
        }
        parashockGuardTimer = EventManager.addEvent(this::giveParashockGuardBuff, 1, TimeUnit.SECONDS);
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        int slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case CLOSE_COMBAT:
                if (Util.succeedProp(si.getValue(prop, slv))) {
                    for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        if (!mob.isBoss()) {
                            MobTemporaryStat mts = mob.getTemporaryStat();
                            o1.nOption = 1;
                            o1.rOption = skill.getSkillId();
                            o1.tOption = si.getValue(time, slv);
                            mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                        }
                    }
                }
                break;
            case FLAME_CHARGE:
                giveChargeBuff(skill.getSkillId(), tsm);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(chr, skill);
                    }
                }
                break;
            case BLIZZARD_CHARGE:
                giveChargeBuff(skill.getSkillId(), tsm);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.tOption = si.getValue(time, slv);
                        o1.nOption = -20;
                        o1.rOption = skill.getSkillId();
                        mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
                    }
                }
                break;
            case LIGHTNING_CHARGE:
                giveChargeBuff(skill.getSkillId(), tsm);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        if (!mob.isBoss()) {
                            MobTemporaryStat mts = mob.getTemporaryStat();
                            o1.nOption = 1;
                            o1.rOption = skill.getSkillId();
                            o1.tOption = si.getValue(time, slv);
                            mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                        }
                    } else {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.createAndAddBurnedInfo(chr, skill);
                    }
                }
                break;
            case DIVINE_CHARGE:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o1.nOption = 1;
                        o1.rOption = skill.getSkillId();
                        o1.tOption = si.getValue(time, slv);
                        mts.addStatOptionsAndBroadcast(MobStat.Seal, o1);
                    }
                }
                giveChargeBuff(skill.getSkillId(), tsm);
                break;
            case HEAVENS_HAMMER:
                break;
            case BLAST:
                int charges = tsm.getOption(ElementalCharge).mOption;
                if (charges == SkillData.getSkillInfoById(ELEMENTAL_CHARGE).getValue(z, 1)) {
                    if (tsm.getOptByCTSAndSkill(DamR, BLAST) == null) {
                        resetCharges(tsm);
                        int t = si.getValue(time, slv);
                        o1.nOption = si.getValue(cr, slv);
                        o1.rOption = skillID;
                        o1.tOption = t;
                        tsm.putCharacterStatValue(CriticalBuff, o1);
                        o2.nOption = si.getValue(ignoreMobpdpR, slv);
                        o2.rOption = skillID;
                        o2.tOption = t;
                        tsm.putCharacterStatValue(IgnoreTargetDEF, o2);
                        o3.nOption = si.getValue(damR, slv);
                        o3.rOption = skillID;
                        o3.tOption = t;
                        tsm.putCharacterStatValue(DamR, o3);
                        tsm.sendSetStatPacket();
                    }
                }
                break;
            case SMITE_SHIELD:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = 1;
                    o1.rOption = skill.getSkillId();
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobStat.Smite, o1);
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void giveChargeBuff(int skillId, TemporaryStatManager tsm) {
        SkillInfo si = SkillData.getSkillInfoById(ELEMENTAL_CHARGE);
        int count = 1;
        if (tsm.hasStat(ElementalCharge)) {
            count = tsm.getOption(ElementalCharge).mOption;
            if (lastCharge == skillId) {
                return;
            }
            if (count < si.getValue(z, 1)) {
                count++;
            }
        }
        lastCharge = skillId;
        Option o1 = new Option();
        o1.nOption = count * chr.getSkillStatValue(x, ADVANCED_CHARGE); // damR
        o1.rOption = ELEMENTAL_CHARGE;
        o1.tOption = si.getValue(time, 1);
        o1.mOption = count; // count
        o1.uOption = count * si.getValue(u, 1); // asr
        int padMult = chr.hasSkill(ADVANCED_CHARGE) ? chr.getSkillStatValue(y, ADVANCED_CHARGE) : si.getValue(y, 1);
        o1.wOption = count * padMult; // pad
        o1.zOption = si.getValue(w, 1); // dmgReduce
        tsm.putCharacterStatValue(ElementalCharge, o1);
        tsm.sendSetStatPacket();
    }

    private void resetCharges(TemporaryStatManager tsm) {
        tsm.removeStat(ElementalCharge, false);
        tsm.sendResetStatPacket();
    }

    private Skill getFinalAtkSkill() {
        Skill skill = null;
        if (chr.hasSkill(FINAL_ATTACK_PAGE)) {
            skill = chr.getSkill(FINAL_ATTACK_PAGE);

        }
        return skill;
    }

    @Override
    public int getFinalAttackSkill() {
        Skill faSkill = getFinalAtkSkill();
        if (faSkill != null) {
            SkillInfo si = SkillData.getSkillInfoById(faSkill.getSkillId());
            byte slv = (byte) faSkill.getCurrentLevel();
            int proc = si.getValue(prop, slv);

            if (Util.succeedProp(proc)) {
                return faSkill.getSkillId();
            }
        }
        return 0;
    }



    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillID);;

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Rect rect;
        Char ptChr;
        switch (skillID) {
            case HP_RECOVERY:
                hpRecovery();
                break;
            case THREATEN:
                rect = chr.getRectAround(si.getFirstRect());
                if (!chr.isLeft()) {
                    rect = rect.moveRight();
                }
                for (Life life : chr.getField().getLifesInRect(rect)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        if (Util.succeedProp(si.getValue(prop, slv) + chr.getSkillStatValue(prop, THREATEN_OPPORTUNITY))) {
                            o1.nOption = si.getValue(x, slv) + chr.getSkillStatValue(x, THREATEN_ENHANCE);
                            o1.rOption = skillID;
                            o1.tOption = si.getValue(time, slv) + chr.getSkillStatValue(time, THREATEN_PERSIST);
                            mts.addStatOptions(MobStat.PAD, o1.deepCopy());
                            mts.addStatOptions(MobStat.MAD, o1.deepCopy());
                            mts.addStatOptions(MobStat.PDR, o1.deepCopy());
                            mts.addStatOptions(MobStat.MDR, o1.deepCopy());
                            o2.nOption = -si.getValue(z, slv) - chr.getSkillStatValue(y, THREATEN_ENHANCE);
                            o2.rOption = skillID;
                            o2.tOption = si.getValue(subTime, slv);
                            mts.addStatOptionsAndBroadcast(MobStat.Darkness, o2);
                        }
                    }
                }
                break;
            case GUARDIAN:
                // note: skill only triggers if not in a pt or a dead pt member is nearby
                // but the detect radius is bigger than the actual skill range
                ptChr = getReviveTarget(chr.getRectAround(si.getFirstRect()));
                if (ptChr != null) {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);

                    tsm.putCharacterStatValue(NotDamaged, o1);

                    TemporaryStatManager ptTsm = ptChr.getTemporaryStatManager();
                    ptChr.heal(ptChr.getMaxHP());
                    ptTsm.putCharacterStatValue(NotDamaged, o1);
                    ptTsm.sendSetStatPacket();

                    ptChr.write(UserPacket.effect(Effect.skillAffected(skillID, (byte) 1, ptChr.getMaxHP())));
                    ptChr.getField().broadcastPacket(UserRemote.effect(ptChr.getId(), Effect.skillAffected(skillID, (byte) 1, ptChr.getMaxHP())));
                } else {
                    chr.chatMessage("There are no nearby allies to revive.");
                    chr.resetSkillCoolTime(skillID);
                }
                break;
            case COMBAT_ORDERS:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CombatOrders, o1);
                break;
            case PARASHOCK_GUARD:
                o1.nOption = slv;
                o1.rOption = PARASHOCK_GUARD;
                o1.bOption = 1;
                o2.nReason = PARASHOCK_GUARD;
                o2.nValue = si.getValue(indiePad, slv);
                o2.tStart = Util.getCurrentTime();
                o3.nReason = PARASHOCK_GUARD;
                o3.nValue = si.getValue(z, slv);
                o3.tStart = Util.getCurrentTime();

                tsm.putCharacterStatValue(KnightsAura, o1);
                tsm.putCharacterStatValue(IndiePAD, o2);
                tsm.putCharacterStatValue(IndiePDDR, o3);
                // guard chance handled client side with KnightsAura

                if (parashockGuardTimer != null && !parashockGuardTimer.isDone()) {
                    parashockGuardTimer.cancel(true);
                }
                giveParashockGuardBuff();
                break;
            case ELEMENTAL_FORCE:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o1);
                break;
            case SACROSANCTITY:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NotDamaged, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    public void hpRecovery() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();
        if (chr.hasSkill(HP_RECOVERY)) {
            Skill skill = chr.getSkill(HP_RECOVERY);
            byte slv = (byte) skill.getCurrentLevel();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            int recovery = si.getValue(x, slv);
            int amount = 10; // si.getValue(y, slv);

            if (tsm.hasStat(Restoration)) {
                amount = tsm.getOption(Restoration).nOption;
                if (amount < 300) { // only displayed to 30 stacks
                    amount = amount + 10;
                }
            }

            o.nOption = amount;
            o.rOption = skill.getSkillId();
            o.tOption = si.getValue(time, slv);
            int heal = recovery - Math.max(10, amount);
            chr.heal((int) (chr.getMaxHP() / ((double) 100 / heal)), true);
            tsm.putCharacterStatValue(Restoration, o);
            tsm.sendSetStatPacket();
        }
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, InPacket inPacket, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();

        //Paladin - Divine Shield
        if (chr.hasSkill(DIVINE_SHIELD)) {
            Skill skill = chr.getSkill(DIVINE_SHIELD);
            SkillInfo si = SkillData.getSkillInfoById(DIVINE_SHIELD);
            int slv = skill.getCurrentLevel();
            int shieldProp = si.getValue(prop, slv);
            Option o1 = new Option();
            Option o2 = new Option();
            int divShieldCoolDown = si.getValue(cooltime, slv);
            if (tsm.hasStat(BlessingArmor)) {
                if (divShieldAmount < 10) {
                    divShieldAmount++;
                } else {
                    resetDivineShield();
                    divShieldAmount = 0;
                }
            } else {
                if (lastDivineShieldHit + (divShieldCoolDown * 1000L) < Util.getCurrentTimeLong()) {
                    if (Util.succeedProp(shieldProp)) {
                        lastDivineShieldHit = Util.getCurrentTimeLong();
                        o1.nOption = 1;
                        o1.rOption = DIVINE_SHIELD;
                        o1.tOption = si.getValue(time, slv);
                        tsm.putCharacterStatValue(BlessingArmor, o1);
                        o2.nOption = si.getValue(epad, slv);
                        o2.rOption = DIVINE_SHIELD;
                        o2.tOption = si.getValue(time, slv);
                        tsm.putCharacterStatValue(BlessingArmorIncPAD, o2);
                        tsm.sendSetStatPacket();
                        divShieldAmount = 0;
                    }
                }
            }
        }

        //Paladin - Shield Mastery
        Item shield = chr.getEquippedItemByBodyPart(BodyPart.Shield);
        if (chr.hasSkill(SHIELD_MASTERY) && shield != null && ItemConstants.isShield(shield.getItemId())) {
            if (hitInfo.guard == 2) {
                Field field = chr.getField();
                Skill skill = chr.getSkill(SHIELD_MASTERY);
                int slv = skill.getCurrentLevel();
                SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                int mobID = hitInfo.mobID;
                Mob mob = (Mob) field.getLifeByObjectID(mobID);
                if (mob != null) {
                    Option o = new Option();
                    int proc = si.getValue(subProp, slv);
                    if (Util.succeedProp(proc) && !mob.isBoss()) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o.nOption = 1;
                        o.rOption = skill.getSkillId();
                        o.tOption = GameConstants.DEFAULT_STUN_DURATION;
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o);
                    }
                }
                chr.write(UserPacket.effect(Effect.changeHPEffect(0, true)));
            }
        }

        super.handleHit(chr, inPacket, hitInfo);
    }

    private void resetDivineShield() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.removeStat(BlessingArmor, true);
        tsm.removeStat(BlessingArmorIncPAD, true);
        tsm.sendResetStatPacket();
    }

    @Override
    public void handleSkillRemove(Char chr, int skillID) {
        if (skillID == PARASHOCK_GUARD && parashockGuardTimer != null && !parashockGuardTimer.isDone()) {
            parashockGuardTimer.cancel(true);
        }
    }

    @Override
    public void handleCancelTimer(Char chr) {
        if (parashockGuardTimer != null && !parashockGuardTimer.isDone()) {
            parashockGuardTimer.cancel(true);
        }
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

