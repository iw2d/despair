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
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
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

    private int[] addedSkills = new int[]{
            MAPLE_RETURN,
    };

    private long lastDivineShieldHit = Long.MIN_VALUE;
    private int lastCharge = 0;
    private int divShieldAmount = 0;
    private ScheduledFuture parashockGuardTimer;

    public Paladin(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isPaladin(id);
    }

    private void giveParashockGuardBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(PARASHOCK_GUARD) && tsm.getOptByCTSAndSkill(EVA, PARASHOCK_GUARD) != null) {
            Skill skill = chr.getSkill(PARASHOCK_GUARD);
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            byte slv = (byte) skill.getCurrentLevel();
            Rect rect = chr.getPosition().getRectAround(si.getRects().get(0));
            if (!chr.isLeft()) {
                rect = rect.moveRight();
            }
            Party party = chr.getParty();

            if (party != null) {
                for (PartyMember partyMember : party.getOnlineMembers()) {
                    Char partyChr = partyMember.getChr();
                    TemporaryStatManager partyTSM = partyChr.getTemporaryStatManager();
                    int partyChrX = partyChr.getPosition().getX();
                    int partyChrY = partyChr.getPosition().getY();

                    if (partyChr.getId() == chr.getId()) {
                        continue;
                    }

                    if (partyChrX >= rect.getLeft() && partyChrY >= rect.getTop()       // if  Party Members in Range
                            && partyChrX <= rect.getRight() && partyChrY <= rect.getBottom()) {

                        Option o4 = new Option();
                        Option o5 = new Option();
                        o4.nOption = si.getValue(x, slv);
                        o4.rOption = skill.getSkillId();
                        o4.tOption = 2;
                        partyTSM.putCharacterStatValue(Guard, o4);
                        o5.nOption = chr.getId();
                        o5.rOption = skill.getSkillId();
                        o5.tOption = 2;
                        o5.bOption = 1;
                        partyTSM.putCharacterStatValue(KnightsAura, o5);
                        partyTSM.sendSetStatPacket();
                    } else {
                        partyTSM.removeStatsBySkill(skill.getSkillId());
                        partyTSM.sendResetStatPacket();
                    }
                }
            }
            Option o = new Option();
            Option o1 = new Option();
            Option o2 = new Option();
            Option o3 = new Option();
            o.nOption = chr.getId();
            o.rOption = skill.getSkillId();
            o.bOption = 1;
            tsm.putCharacterStatValue(KnightsAura, o);
            o1.nReason = skill.getSkillId();
            o1.nValue = si.getValue(indiePad, slv);
            o1.tStart = (int) System.currentTimeMillis();
            tsm.putCharacterStatValue(IndiePAD, o1);
            o2.nReason = skill.getSkillId();
            o2.nValue = si.getValue(z, slv);
            o2.tStart = (int) System.currentTimeMillis();
            tsm.putCharacterStatValue(IndiePDDR, o2);
            o3.nOption = -si.getValue(x, slv);
            o3.rOption = skill.getSkillId();
            tsm.putCharacterStatValue(Guard, o3);

            parashockGuardTimer = EventManager.addEvent(this::giveParashockGuardBuff, 1, TimeUnit.SECONDS);
        } else {
            tsm.removeStatsBySkill(PARASHOCK_GUARD);
            tsm.sendResetStatPacket();
        }
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
        Option o = new Option();
        SkillInfo chargeInfo = SkillData.getSkillInfoById(1200014);
        int amount = 1;
        if (tsm.hasStat(ElementalCharge)) {
            amount = tsm.getOption(ElementalCharge).mOption;
            if (lastCharge == skillId) {
                return;
            }
            if (amount < chargeInfo.getValue(z, 1)) {
                amount++;
            }
        }
        lastCharge = skillId;
        o.nOption = 1;
        o.rOption = 1200014;
        o.tOption = (10 * chargeInfo.getValue(time, 1)); // elemental charge  // 10x actual duration
        o.mOption = amount;
        o.wOption = amount * chargeInfo.getValue(w, 1); // elemental charge
        o.uOption = amount * chargeInfo.getValue(u, 1);
        o.zOption = amount * chargeInfo.getValue(z, 1);
        tsm.putCharacterStatValue(ElementalCharge, o);
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
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = null;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skillID);
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (skillID) {
            case HP_RECOVERY:
                hpRecovery();
                break;
            case THREATEN:
                Rect rect = chr.getPosition().getRectAround(si.getRects().get(0));
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
                chr.heal(chr.getMaxHP());

                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NotDamaged, o1);
                tsm.sendSetStatPacket();

                Party party = chr.getParty();
                if (party != null) {
                    Field field = chr.getField();
                    rect = chr.getPosition().getRectAround(si.getRects().get(0));
                    if (!chr.isLeft()) {
                        rect = rect.moveRight();
                    }
                    List<PartyMember> eligblePartyMemberList = field.getPartyMembersInRect(chr, rect).stream().
                            filter(pml -> pml.getChr().getId() != chr.getId() &&
                                    pml.getChr().getHP() <= 0).
                            collect(Collectors.toList());

                    if (eligblePartyMemberList.size() > 0) {
                        Char randomPartyChr = Util.getRandomFromCollection(eligblePartyMemberList).getChr();
                        TemporaryStatManager partyTSM = randomPartyChr.getTemporaryStatManager();
                        randomPartyChr.heal(randomPartyChr.getMaxHP());
                        partyTSM.putCharacterStatValue(NotDamaged, o1);
                        partyTSM.sendSetStatPacket();
                        randomPartyChr.write(UserPacket.effect(Effect.skillAffected(skillID, (byte) 1, 0)));
                        randomPartyChr.getField().broadcastPacket(UserRemote.effect(randomPartyChr.getId(), Effect.skillAffected(skillID, (byte) 1, 0)));
                    }
                }

                break;
            case COMBAT_ORDERS:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CombatOrders, o1);
                break;
            case PARASHOCK_GUARD:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(EVA, o1); // Check for the main Buff method

                if (parashockGuardTimer != null && !parashockGuardTimer.isDone()) {
                    parashockGuardTimer.cancel(true);
                }
                giveParashockGuardBuff();
                break;
            case ELEMENTAL_FORCE:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = (int) System.currentTimeMillis();
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
            int amount = 10;

            if (tsm.hasStat(Restoration)) {
                amount = tsm.getOption(Restoration).nOption;
                if (amount < 300) {
                    amount = amount + 10;
                }
            }

            o.nOption = amount;
            o.rOption = skill.getSkillId();
            o.tOption = si.getValue(time, slv);
            int heal = (recovery + 10) - amount > 10 ? (recovery + 10) - amount : 10;
            chr.heal((int) (chr.getMaxHP() / ((double) 100 / heal)));
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
            int shieldprop = si.getValue(prop, slv);
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
                if (lastDivineShieldHit + (divShieldCoolDown * 1000) < System.currentTimeMillis()) {
                    if (Util.succeedProp(shieldprop)) {
                        lastDivineShieldHit = System.currentTimeMillis();
                        o1.nOption = 1;
                        o1.rOption = DIVINE_SHIELD;
                        o1.tOption = si.getValue(time, slv);
                        tsm.putCharacterStatValue(BlessingArmor, o1);
                        o2.nOption = si.getValue(epad, slv);
                        o2.rOption = DIVINE_SHIELD;
                        o2.tOption = si.getValue(time, slv);
                        tsm.putCharacterStatValue(PAD, o2);
                        tsm.sendSetStatPacket();
                        divShieldAmount = 0;
                    }
                }
            }
        }

        //Paladin - Shield Mastery
        Item shield = chr.getEquippedItemByBodyPart(BodyPart.Shield);
        if (chr.hasSkill(SHIELD_MASTERY) && shield != null && ItemConstants.isShield(shield.getItemId())) {
            if (hitInfo.hpDamage == 0 && hitInfo.mpDamage == 0) {
                // Guarded
                int mobID = hitInfo.mobID;
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mobID);
                if (mob != null) {
                    Option o = new Option();
                    Skill skill = chr.getSkill(SHIELD_MASTERY);
                    byte slv = (byte) skill.getCurrentLevel();
                    SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
                    int proc = si.getValue(subProp, slv);
                    if (Util.succeedProp(proc) && !mob.isBoss()) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        o.nOption = 1;
                        o.rOption = skill.getSkillId();
                        o.tOption = 3;  // Value isn't given
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o);
                    }
                }
            }
        }

        super.handleHit(chr, inPacket, hitInfo);
    }

    private void resetDivineShield() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.removeStat(BlessingArmor, false);
        tsm.removeStat(PAD, false);
        tsm.sendResetStatPacket();
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

