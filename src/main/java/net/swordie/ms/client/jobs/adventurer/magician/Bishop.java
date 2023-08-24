package net.swordie.ms.client.jobs.adventurer.magician;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.TownPortal;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.FieldData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Bishop extends Magician {
    public static final int MP_EATER_BISH = 2300000;
    public static final int SPELL_MASTERY_BISH = 2300006;
    public static final int INVINCIBLE = 2300003;
    public static final int HEAL = 2301002;
    public static final int MAGIC_BOOSTER_BISH = 2301008;
    public static final int BLESSED_ENSEMBLE = 2300009;
    public static final int BLESS = 2301004;
    public static final int DISPEL = 2311001;
    public static final int SHINING_RAY = 2311004;
    public static final int HOLY_MAGIC_SHELL = 2311009;
    public static final int TELEPORT_MASTERY_BISH = 2311007;
    public static final int HOLY_FOUNTAIN = 2311011;
    public static final int DIVINE_PROTECTION = 2311012;
    public static final int MYSTIC_DOOR = 2311002;
    public static final int HOLY_SYMBOL = 2311003;
    public static final int BUFF_MASTERY_BISH = 2320012;
    public static final int ADV_BLESSING = 2321005;
    public static final int BAHAMUT = 2321003;
    public static final int INFINITY_BISH = 2321004;
    public static final int BLESSED_HARMONY = 2320013;
    public static final int MAPLE_WARRIOR_BISH = 2321000;
    public static final int GENESIS = 2321008;
    public static final int BIG_BANG = 2321001;
    public static final int ARCANE_AIM_BISH = 2320011;
    public static final int ANGEL_RAY = 2321007;
    public static final int RESURRECTION = 2321006;
    public static final int HEROS_WILL_BISH = 2321009;

    public static final int EPIC_ADVENTURE_BISH = 2321053;
    public static final int RIGHTEOUSLY_INDIGNANT = 2321054;
    public static final int HEAVENS_DOOR = 2321052;
    public static final int HOLY_MAGIC_SHELL_PERSIST = 2320044;
    public static final int HOLY_MAGIC_SHELL_EXTRA_GUARD = 2320043;
    public static final int HOLY_SYMBOL_EXPERIENCE = 2320046;
    public static final int HOLY_SYMBOL_PREPARATION = 2320047;
    public static final int HOLY_SYMBOL_ITEM_DROP = 2320048;
    public static final int ADV_BLESSING_FEROCITY = 2320049;
    public static final int ADV_BLESSING_BOSS_RUSH = 2320050;
    public static final int ADV_BLESSING_EXTRA_POINT = 2320051;

    private ScheduledFuture blessedEmsembleTimer;

    public Bishop(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            blessedEmsembleTimer = EventManager.addFixedRateEvent(this::changeBlessedCount, 2000, 2000);
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isBishop(id);
    }



    // Buff related methods --------------------------------------------------------------------------------------------

    private void changeBlessedCount() {
        int skillId = 0;
        if (chr.hasSkill(BLESSED_HARMONY)) {
            skillId = BLESSED_HARMONY;
        } else if (chr.hasSkill(BLESSED_ENSEMBLE)) {
            skillId = BLESSED_ENSEMBLE;
        }
        if (skillId == 0 || chr.getParty() == null) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);

        List<PartyMember> partyMembers = chr.getParty().getOnlineMembers().stream().filter(pm -> pm.getCharID() != chr.getId() && pm.isOnline()).toList();
        int count = partyMembers.size();
        int bishCount = (int) partyMembers.stream().filter(pm -> JobConstants.isBishop(pm.getChr().getJob())).count();
        if (count > 0) {
            // compare and send stat packet only if update is required
            Option oldO1 = tsm.getOptByCTSAndSkill(BlessEnsenble, BLESSED_ENSEMBLE);
            Option oldO2 = tsm.getOptByCTSAndSkill(IndieEXP, BLESSED_ENSEMBLE);

            Option o1 = new Option();
            o1.nOption = count * si.getValue(x, slv);
            o1.rOption = BLESSED_ENSEMBLE;
            tsm.putCharacterStatValue(BlessEnsenble, o1);
            if (oldO1 == null || oldO1.nOption != o1.nOption) {
                tsm.sendSetStatPacket();
            }

            if (bishCount > 0) {
                Option o2 = new Option();
                o2.nValue = bishCount * 20;
                o2.nReason = BLESSED_ENSEMBLE;
                o2.tStart = Util.getCurrentTime();
                tsm.putCharacterStatValue(IndieEXP, o2);
                if (oldO2 == null || oldO2.nValue != o2.nValue) {
                    tsm.sendSetStatPacket();
                }
            }
        } else if (tsm.hasStat(BlessEnsenble)) {
            tsm.removeStat(BlessEnsenble, false);
            tsm.sendResetStatPacket();
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
        Rect rect;
        int healRate;
        List<PartyMember> partyMembers;
        switch (attackInfo.skillId) {
            case HEAL:
                // debuff mobs
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.pOption = chr.getPartyID();
                o1.wOption = chr.getId();
                rect = attackInfo.getForcedPos().getRectAround(si.getFirstRect());
                int count = si.getValue(u, slv);
                for (Life life : chr.getField().getLifesInRect(rect)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.AddDamParty, o1);
                        count--;
                        if (count <= 0) {
                            break;
                        }
                    }
                }
                // heal party
                healRate = (int) (chr.getDamageCalc().getMaxBaseDamage() * (si.getValue(hp, slv)) / 100D);
                if (tsm.hasStat(VengeanceOfAngel)) {
                    healRate = (int) (healRate * (chr.getSkillStatValue(hp, RIGHTEOUSLY_INDIGNANT) / 100D));
                }
                if (chr.getParty() == null) {
                    if (chr.getHP() > 0) {
                        chr.heal(!tsm.hasStat(CharacterTemporaryStat.Undead) ? healRate : -healRate, true);
                    }
                } else {
                    partyMembers = chr.getField().getPartyMembersInRect(chr, rect).stream()
                            .filter(pml -> pml.getChr().getHP() > 0)
                            .toList();
                    for (PartyMember partyMember : partyMembers) {
                        Char partyChr = partyMember.getChr();
                        partyChr.heal(!partyChr.getTemporaryStatManager().hasStat(CharacterTemporaryStat.Undead)
                                ? healRate / partyMembers.size() : -healRate / partyMembers.size(), true);
                    }
                    count = partyMembers.size() - 1;
                    if (count > 0) {
                        chr.reduceSkillCoolTime(skillID, count * si.getValue(y, slv) * 1000L);
                    }
                }
                break;
            case TELEPORT_MASTERY_BISH:
            case SHINING_RAY:
                o1.nOption = 1;
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
                break;
            case BAHAMUT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(subTime, slv);
                // fix in client: PatchNop(0x00E9CE55, 2)
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.BahamutLightElemAddDam, o1);
                }
                break;
            case HEAVENS_DOOR:
                o1.nOption = 1;
                o1.rOption = HEAVENS_DOOR;
                o1.tOption = 0;
                if (chr.getParty() == null) {
                    tsm.putCharacterStatValue(HeavensDoor, o1);
                    tsm.sendSetStatPacket();
                } else {
                    rect = attackInfo.getForcedPos().getRectAround(si.getFirstRect());
                    partyMembers = chr.getField().getPartyMembersInRect(chr, rect).stream()
                            .filter(pml -> pml.getChr().getHP() > 0)
                            .toList();
                    for (PartyMember partyMember : partyMembers) {
                        Char partyChr = partyMember.getChr();
                        TemporaryStatManager partyTSM = partyChr.getTemporaryStatManager();
                        partyTSM.putCharacterStatValue(HeavensDoor, o1);
                        partyTSM.sendSetStatPacket();
                        if (partyChr != chr) {
                            chr.getField().broadcastPacket(UserRemote.effect(partyChr.getId(), Effect.skillAffected(skillID, (byte) slv, 0)), partyChr);
                            partyChr.write(UserPacket.effect(Effect.skillAffected(skillID, (byte) slv, 0)));
                        }
                    }
                }
                break;
            case ANGEL_RAY:
                healRate = si.getValue(hp, slv);
                if (tsm.hasStat(VengeanceOfAngel)) {
                    healRate = (int) (healRate * (chr.getSkillStatValue(hp, RIGHTEOUSLY_INDIGNANT) / 100D));
                }
                if (chr.getParty() == null) {
                    if (chr.getHP() > 0) {
                        chr.heal((int) (chr.getMaxHP() * (healRate / 100D)), true);
                    }
                } else {
                    partyMembers = chr.getParty().getOnlineMembers().stream()
                            .filter(pml -> pml != null &&
                                    pml.getChr().getFieldID() == chr.getFieldID() &&
                                    pml.getChr().getHP() > 0
                            )
                            .toList();
                    for (PartyMember partyMember : partyMembers) {
                        Char partyChr = partyMember.getChr();
                        partyChr.heal((int) (partyChr.getMaxHP() * (healRate / 100D)), true);
                    }
                }
                break;
            case GENESIS:
                o1.nOption = 1;
                o1.rOption = BIG_BANG;
                o1.tOption = si.getValue(cooltime, slv);
                tsm.putCharacterStatValue(KeyDownTimeIgnore, o1);
                tsm.sendSetStatPacket();
                break;
            case BIG_BANG:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    int stack = 1;
                    if (mts.hasStat(MobStat.PDR)) {
                        Option oldO = mts.getOption(MobStat.PDR);
                        if (oldO.rOption == skillID) {
                            stack = oldO.cOption;
                            if (stack < si.getValue(y, slv)) {
                                stack++;
                            }
                        }
                    }
                    Option o = new Option();
                    o.nOption = stack * si.getValue(x, slv);
                    o.rOption = skillID;
                    o.tOption = si.getValue(time, slv);
                    o.cOption = stack;
                    mts.addStatOptionsAndBroadcast(MobStat.PDR, o);
                    mts.addStatOptionsAndBroadcast(MobStat.MDR, o);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
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
        int count;
        Position pos;
        Rect rect;
        Field field;
        Party party;
        List<PartyMember> partyMembers;
        switch (skillID) {
            case HOLY_FOUNTAIN:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(inPacket.decodePosition());
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                aa.setDelay((short) 4);
                aa.setForce(si.getValue(y, slv));
                chr.getField().spawnAffectedArea(aa);
                break;
            case DIVINE_PROTECTION:
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(AntiMagicShell, o1);
                break;
            case HEAL: // handle in handleAttack
                break;
            case DISPEL:
                if (!chr.equals(this.chr)) {
                    // massSpell, handle everything as caster
                    break;
                }
                if (chr.getParty() == null) {
                    tsm.removeAllDebuffs();
                } else {
                    pos = inPacket.decodePosition();
                    rect = pos.getRectAround(si.getFirstRect());
                    if (!chr.isLeft()) {
                        rect.horizontalFlipAround(pos.getX());
                    }
                    partyMembers = chr.getField().getPartyMembersInRect(chr, rect).stream()
                            .filter(pml -> pml.getChr().getHP() > 0 &&
                                    pml.getChr().getTemporaryStatManager().hasDebuffs()
                            )
                            .toList();
                    for (PartyMember partyMember : partyMembers) {
                        Char partyChr = partyMember.getChr();
                        partyChr.getTemporaryStatManager().removeAllDebuffs();
                    }
                    int numCured = partyMembers.size();
                    if (numCured > 0) {
                        chr.reduceSkillCoolTime(skillID, numCured * si.getValue(y, slv) * 1000L);
                        chr.reduceSkillCoolTime(DIVINE_PROTECTION, numCured * si.getValue(time, slv) * 1000L);
                    }
                }
                break;
            case MYSTIC_DOOR:
                Field townField = FieldData.getFieldById(chr.getField().getReturnMap());
                int portalX = townField.getPortalByName("tp").getX();
                int portalY = townField.getPortalByName("tp").getY();
                Position townPosition = new Position(portalX, portalY); // Grabs the Portal Co-ordinates for the TownPortalPoint
                int duration = si.getValue(time, slv);
                if (chr.getTownPortal() != null) {
                    TownPortal townPortal = chr.getTownPortal();
                    townPortal.despawnTownPortal();
                }
                TownPortal townPortal = new TownPortal(chr, townPosition, inPacket.decodePosition(), chr.getField().getReturnMap(), chr.getFieldID(), skillID, duration);
                townPortal.spawnTownPortal();
                chr.dispose();
                break;
            case BLESS:
                if (!tsm.hasStat(AdvancedBless)) {
                    o1.nOption = slv;
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(Bless, o1);
                }
                break;
            case ADV_BLESSING:
                if (tsm.hasStat(Bless)) {
                    tsm.removeStat(Bless, false);
                    tsm.sendResetStatPacket();
                }
                o1.nOption = slv;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.xOption = this.chr.getSkillStatValue(bdR, ADV_BLESSING_BOSS_RUSH);
                tsm.putCharacterStatValue(AdvancedBless, o1);
                o2.nValue = si.getValue(indieMhp, slv) + this.chr.getSkillStatValue(indieMhp, ADV_BLESSING_EXTRA_POINT);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHP, o2);
                tsm.putCharacterStatValue(IndieMMP, o2);
                if (this.chr.hasSkill(ADV_BLESSING_FEROCITY)) {
                    o3.nOption = this.chr.getSkillStatValue(x, ADV_BLESSING_FEROCITY);
                    o3.rOption = skillID;
                    o3.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(PAD, o3);
                    tsm.putCharacterStatValue(MAD, o3);
                }
                break;
            case HOLY_SYMBOL:
                o1.nOption = si.getValue(x, slv) + chr.getSkillStatValue(y, HOLY_SYMBOL_EXPERIENCE); // self only
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(HolySymbol, o1);
                if (this.chr.hasSkill(HOLY_SYMBOL_PREPARATION)) {
                    o2.nOption = this.chr.getSkillStatValue(asrR, HOLY_SYMBOL_PREPARATION);
                    o2.rOption = skillID;
                    o2.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(AsrR, o2);
                    tsm.putCharacterStatValue(TerR, o2);
                }
                if (this.chr.hasSkill(HOLY_SYMBOL_ITEM_DROP)) {
                    o3.nOption = this.chr.getSkillStatValue(v, HOLY_SYMBOL_ITEM_DROP);; // Item Drop Rate %
                    o3.nReason = skillID;
                    o2.tOption = si.getValue(time, slv);
                    tsm.putCharacterStatValue(DropRate, o3);
                }
                break;
            case HOLY_MAGIC_SHELL:
                if (!tsm.hasStat(HolyMagicShell)) {
                    int healAmount = chr.getMaxHP();
                    if (tsm.hasStat(VengeanceOfAngel)) {
                        healAmount = (int) (healAmount * (chr.getSkillStatValue(hp, RIGHTEOUSLY_INDIGNANT) / 100D));
                    }
                    if (chr.getHP() > 0) {
                        chr.heal(healAmount, true);
                    }
                    o1.nOption = si.getValue(x, slv) + this.chr.getSkillStatValue(x, HOLY_MAGIC_SHELL_EXTRA_GUARD);
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv) + this.chr.getSkillStatValue(time, HOLY_MAGIC_SHELL_PERSIST);
                    o1.xOption = si.getValue(y, slv);
                    tsm.putCharacterStatValue(HolyMagicShell, o1);
                }
                break;
            case RESURRECTION:
                party = chr.getParty();
                if (party != null) {
                    field = chr.getField();
                    rect = chr.getPosition().getRectAround(si.getFirstRect()); // position not encoded
                    if(!chr.isLeft()) {
                        rect = rect.horizontalFlipAround(chr.getPosition().getX());
                    }
                    List<PartyMember> eligblePartyMemberList = field.getPartyMembersInRect(chr, rect).stream()
                            .filter(pml -> pml.getChr().getId() != chr.getId() &&
                                    pml.getChr().getHP() <= 0
                            )
                            .toList();
                    for (PartyMember partyMember : eligblePartyMemberList) {
                        Char partyChr = partyMember.getChr();
                        partyChr.healHPMP();
                        partyChr.write(UserPacket.effect(Effect.skillAffected(skillID, (byte) 1, 0)));
                        partyChr.getField().broadcastPacket(UserRemote.effect(partyChr.getId(), Effect.skillAffected(skillID, (byte) 1, 0)));
                    }
                }
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NotDamaged, o1);
                break;
            case RIGHTEOUSLY_INDIGNANT:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(VengeanceOfAngel, o1); // reduce -40 damR when n != 0
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMad, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = 0;
                tsm.putCharacterStatValue(IndieMAD, o2);
                o3.nReason = skillID;
                o3.nValue = si.getValue(indiePMdR, slv);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = 0;
                tsm.putCharacterStatValue(IndiePMdR, o3);
                o4.nReason = skillID;
                o4.nValue = si.getValue(ignoreMobpdpR, slv);
                o4.tStart = Util.getCurrentTime();
                o4.tTerm = 0;
                tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o6);
                o6.nReason = skillID;
                o6.nValue = si.getValue(indieBooster, slv);
                o6.tStart = Util.getCurrentTime();
                o6.tTerm = 0;
                tsm.putCharacterStatValue(IndieBooster, o5);
                o7.nReason = skillID;
                o7.nValue = -si.getValue(w, slv);
                o5.tStart = Util.getCurrentTime();
                o7.tTerm = 0;
                tsm.putCharacterStatValue(IndieTerR, o6);
                break;

        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleMobDebuffSkill(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(DIVINE_PROTECTION) && tsm.getOptByCTSAndSkill(AntiMagicShell, DIVINE_PROTECTION) != null) {
            tsm.removeStatsBySkill(DIVINE_PROTECTION);
            tsm.sendResetStatPacket();
            tsm.removeAllDebuffs();
            return;
        }
        super.handleMobDebuffSkill(chr);
    }

    @Override
    public void handleCancelTimer() {
        if (blessedEmsembleTimer != null && !blessedEmsembleTimer.isDone()) {
            blessedEmsembleTimer.cancel(true);
        }
        super.handleCancelTimer();
    }

    public static void reviveByHeavensDoor(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        chr.healHPMP();
        tsm.removeStat(HeavensDoor, false);
        tsm.sendResetStatPacket();
        chr.chatMessage("You have been revived by Heaven's Door.");
    }

    public static void handleHolyMagicShell(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(HolyMagicShell) || tsm.getOption(HolyMagicShell).nOption == 0) {
            return;
        }
        hitInfo.hpDamage = 0;
        hitInfo.mpDamage = 0;
        chr.write(UserPacket.effect(Effect.skillAffected(HOLY_MAGIC_SHELL, 1, 0)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffected(HOLY_MAGIC_SHELL, 1, 0)), chr);
        Option oldOption = tsm.getOption(HolyMagicShell);
        if (oldOption.nOption > 1) {
            Option o1 = oldOption.deepCopy();
            o1.nOption -= 1;
            tsm.putCharacterStatValue(HolyMagicShell, o1, true);
            tsm.sendSetStatPacket();
        } else {
            tsm.removeStat(HolyMagicShell, false);
            tsm.sendResetStatPacket();
        }
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

