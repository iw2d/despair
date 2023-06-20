package net.swordie.ms.client.jobs.adventurer.magician;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
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

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Bishop extends Magician {
    public static final int MP_EATER_BISH = 2300000;
    public static final int SPELL_MASTERY_BISH = 2300006;
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

    public Bishop(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isBishop(id);
    }



    // Buff related methods --------------------------------------------------------------------------------------------

    private void changeBlessedCount() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if(getBlessedSkill() == null) {
            return;
        }
        Skill skill = getBlessedSkill();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();
        Option o1 = new Option();
        Option o2 = new Option();
        int amount = 0;

        if(chr.getParty() != null) { // Should be increasing by Given Party Buffs
            for(PartyMember pm : chr.getParty().getOnlineMembers()) {
                if(chr.getFieldID() == pm.getChr().getFieldID()) {
                    amount++;
                }
            }
        }

        if(amount > 1) { // amount = 2  ->  1 count on Icon
            o1.nOption = amount;
            o1.rOption = BLESSED_ENSEMBLE;
            tsm.putCharacterStatValue(BlessEnsenble, o1);

            o2.nValue = si.getValue(x, slv) * amount;
            o2.nReason = BLESSED_ENSEMBLE;
            o2.tStart = (int) System.currentTimeMillis();
            tsm.putCharacterStatValue(IndieDamR, o2);
            tsm.sendSetStatPacket();
        } else {
            tsm.removeStatsBySkill(BLESSED_ENSEMBLE);
            tsm.sendResetStatPacket();
        }
    }

    private Skill getBlessedSkill() {
        Skill skill = null;
        if(chr.hasSkill(BLESSED_ENSEMBLE)) {
            skill = chr.getSkill(BLESSED_ENSEMBLE);
        }
        if(chr.hasSkill(BLESSED_HARMONY)) {
            skill = chr.getSkill(BLESSED_HARMONY);
        }
        return skill;
    }

    private int changeBishopHealingBuffs(int skillID) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(skillID);
        byte slv = (byte) skill.getCurrentLevel();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        int rate = 0;
        int maxHP = chr.getMaxHP();
        int healrate = 0;
        switch (skillID) {
            case HEAL:
                rate = si.getValue(hp, slv);
                healrate = (int) (maxHP / ((double)100 / rate));
                break;
            case HOLY_MAGIC_SHELL:
                rate = si.getValue(z, slv);
                healrate = (int) (maxHP / ((double)100 / rate));
                break;
            case ANGEL_RAY:
                rate = si.getValue(hp, slv);
                healrate = (int) (maxHP / ((double)100 / rate));
                break;
            case INFINITY_BISH:
                break;
        }
        if(tsm.hasStat(VengeanceOfAngel)) {
            SkillInfo hsi = SkillData.getSkillInfoById(RIGHTEOUSLY_INDIGNANT);
            healrate = (int) (healrate / ((double) 100 / (hsi.getValue(hp, 1))));
        }
        return healrate;
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

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case SHINING_RAY:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                        if (mob == null) {
                            continue;
                        }
                        if(!mob.isBoss()) {
                            mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                        }
                    }
                }
                break;
            case BAHAMUT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(subTime, slv);
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
                Party party = chr.getParty();
                if (party != null) {
                    for (PartyMember partyMember : party.getOnlineMembers()) {
                        Char partyChr = partyMember.getChr();
                        TemporaryStatManager partyTSM = partyChr.getTemporaryStatManager();
                        partyTSM.putCharacterStatValue(ReviveOnce, o1);
                        partyTSM.sendSetStatPacket();
                        if(partyChr != chr) {
                            chr.getField().broadcastPacket(UserRemote.effect(partyChr.getId(), Effect.skillAffected(skillID, slv, 0)), partyChr);
                            partyChr.write(UserPacket.effect(Effect.skillAffected(skillID, slv, 0)));
                        }
                    }
                } else {
                    tsm.putCharacterStatValue(ReviveOnce, o1);
                    tsm.sendSetStatPacket();
                }
                break;
            case ANGEL_RAY:
                chr.heal(changeBishopHealingBuffs(ANGEL_RAY));
                break;
            case GENESIS:
                o1.nOption = 1;
                o1.rOption = BIG_BANG;
                o1.tOption = si.getValue(cooltime, slv);
                tsm.putCharacterStatValue(KeyDownTimeIgnore, o1);
                tsm.sendSetStatPacket();
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
        changeBlessedCount();

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        Option o5 = new Option();
        Option o6 = new Option();
        Option o7 = new Option();
        Rect rect;
        Field field;
        Party party;
        switch (skillID) {
            case HOLY_FOUNTAIN:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                aa.setDelay((short) 4);
                aa.setForce(si.getValue(y, slv));
                chr.getField().spawnAffectedArea(aa);
                break;
            case HEAL:
                // debuff mobs
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                rect = chr.getRectAround(si.getFirstRect());
                if (!chr.isLeft()) {
                    rect.horizontalFlipAround(chr.getPosition().getX());
                }
                for (Life life : chr.getField().getLifesInRect(rect)) {
                    if (life instanceof Mob && ((Mob) life).getHp() > 0) {
                        Mob mob = (Mob) life;
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.AddDamParty, o1);
                    }
                }
                // heal party
                int healRate = changeBishopHealingBuffs(HEAL);
                if (chr.getParty() == null) {
                    chr.heal(!tsm.hasStat(CharacterTemporaryStat.Undead) ? healRate : -healRate, true);
                } else {
                    field = chr.getField();
                    List<PartyMember> eligblePartyMemberList = field.getPartyMembersInRect(chr, rect).stream()
                            .filter(pml -> pml.getChr().getHP() > 0)
                            .toList();
                    for (PartyMember partyMember : eligblePartyMemberList) {
                        Char partyChr = partyMember.getChr();
                        partyChr.heal(!partyChr.getTemporaryStatManager().hasStat(CharacterTemporaryStat.Undead)
                                ? healRate / eligblePartyMemberList.size() : -healRate / eligblePartyMemberList.size(), true);
                    }
                    chr.reduceSkillCoolTime(skillID, si.getValue(y, slv) * 1000L);
                }
                break;
            case DISPEL:
                party = chr.getParty();
                if (party == null) {
                    tsm.removeAllDebuffs();
                } else {
                    rect = chr.getRectAround(si.getFirstRect());
                    if (!chr.isLeft()) {
                        rect.horizontalFlipAround(chr.getPosition().getX());
                    }
                    field = chr.getField();
                    List<PartyMember> eligblePartyMemberList = field.getPartyMembersInRect(chr, rect).stream()
                            .filter(pml -> pml.getChr().getHP() > 0 &&
                                    pml.getChr().getTemporaryStatManager().hasDebuffs()
                            )
                            .toList();
                    for (PartyMember partyMember : eligblePartyMemberList) {
                        Char partyChr = partyMember.getChr();
                        partyChr.getTemporaryStatManager().removeAllDebuffs();
                    }
                    int numCured = Math.max(eligblePartyMemberList.size() - 1, 0);
                    chr.reduceSkillCoolTime(skillID, numCured * si.getValue(y, slv) * 1000L);
                    chr.reduceSkillCoolTime(DIVINE_PROTECTION, numCured * si.getValue(time, slv) * 1000L);
                }
                break;
            case MYSTIC_DOOR:
                Field townField = FieldData.getFieldById(chr.getField().getReturnMap());
                int portalX = townField.getPortalByName("tp").getX();
                int portalY = townField.getPortalByName("tp").getY();
                Position townPosition = new Position(portalX, portalY); // Grabs the Portal Co-ordinates for the TownPortalPoint
                int duration = si.getValue(time, slv);
                if(chr.getTownPortal() != null) {
                    TownPortal townPortal = chr.getTownPortal();
                    townPortal.despawnTownPortal();
                }
                TownPortal townPortal = new TownPortal(chr, townPosition, chr.getPosition(), chr.getField().getReturnMap(), chr.getFieldID(), skillID, duration);
                townPortal.spawnTownPortal();
                chr.dispose();
                break;
            case BLESS:
                o1.nOption = si.getValue(u, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PDD, o1);
                tsm.putCharacterStatValue(MDD, o1);
                o2.nOption = si.getValue(v, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EVA, o2);
                tsm.putCharacterStatValue(ACC, o2);
                o3.nOption = si.getValue(x, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PAD, o3);
                tsm.putCharacterStatValue(MAD, o3);
                break;
            case ADV_BLESSING:
                o1.nOption = si.getValue(u, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PDD, o1);
                tsm.putCharacterStatValue(MDD, o1);
                o2.nOption = si.getValue(v, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EVA, o2);
                tsm.putCharacterStatValue(ACC, o2);
                o3.nOption = si.getValue(x, slv) + this.chr.getSkillStatValue(x, ADV_BLESSING_FEROCITY);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PAD, o3);
                tsm.putCharacterStatValue(MAD, o3);
                o4.nValue = si.getValue(indieMhp, slv) + this.chr.getSkillStatValue(indieMhp, ADV_BLESSING_EXTRA_POINT);
                o4.nReason = skillID;
                o4.tStart = Util.getCurrentTime();
                o4.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHP, o4);
                tsm.putCharacterStatValue(IndieMMP, o4);
                if (this.chr.hasSkill(ADV_BLESSING_BOSS_RUSH)) {
                    o5.nValue = chr.getSkillStatValue(bdR, ADV_BLESSING_BOSS_RUSH);
                    o5.nReason = skillID;
                    o5.tTerm = si.getValue(time, slv);
                    tsm.putCharacterStatValue(IndieBDR, o5);
                }
                break;
            case HOLY_SYMBOL: // fix for party buff is inside the wz, gotta add  <int name="massSpell" value="1"/>
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
                chr.heal(changeBishopHealingBuffs(HOLY_MAGIC_SHELL));
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv) + this.chr.getSkillStatValue(time, HOLY_MAGIC_SHELL_PERSIST);
                o1.xOption = si.getValue(x, slv) + this.chr.getSkillStatValue(x, HOLY_MAGIC_SHELL_EXTRA_GUARD);
                tsm.putCharacterStatValue(HolyMagicShell, o1);
                break;
            case RESURRECTION:
                party = chr.getParty();
                if (party != null) {
                    field = chr.getField();
                    rect = chr.getPosition().getRectAround(si.getFirstRect());
                    if(!chr.isLeft()) {
                        rect = rect.moveRight();
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
                tsm.putCharacterStatValue(VengeanceOfAngel, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieMad, slv);
                o2.tStart = (int) System.currentTimeMillis();
                o2.tTerm = 0;
                tsm.putCharacterStatValue(IndieMAD, o2);
                o3.nReason = skillID;
                o3.nValue = si.getValue(indiePMdR, slv);
                o3.tStart = (int) System.currentTimeMillis();
                o3.tTerm = 0;
                tsm.putCharacterStatValue(IndiePMdR, o3);
                o4.nReason = skillID;
                o4.nValue = si.getValue(indieMaxDamageOver, slv);
                o4.tStart = (int) System.currentTimeMillis();
                o4.tTerm = 0;
                tsm.putCharacterStatValue(IndieMaxDamageOver, o4);
                o5.nReason = skillID;
                o5.nValue = si.getValue(indieBooster, slv);
                o5.tStart = (int) System.currentTimeMillis();
                o5.tTerm = 0;
                tsm.putCharacterStatValue(IndieBooster, o5);
                o6.nOption = si.getValue(ignoreMobpdpR, slv);
                o6.rOption = skillID;
                o6.tOption = 0;
                tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o6);
                o7.nOption = si.getValue(w, slv);
                o7.rOption = skillID;
                o7.tOption = 0;
                tsm.putCharacterStatValue(ElementalReset, o7);
                break;

        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    public void handleMobDebuffSkill(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(DIVINE_PROTECTION) && tsm.getOptByCTSAndSkill(AntiMagicShell, DIVINE_PROTECTION) != null) {
            tsm.removeStatsBySkill(DIVINE_PROTECTION);
            tsm.sendResetStatPacket();
            tsm.removeAllDebuffs();
        }
        super.handleMobDebuffSkill(chr);
    }

    public static void reviveByHeavensDoor(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        chr.healHPMP();
        tsm.removeStatsBySkill(HEAVENS_DOOR);
        tsm.sendResetStatPacket();
        chr.chatMessage("You have been revived by Heaven's Door.");
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}

