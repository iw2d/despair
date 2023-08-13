package net.swordie.ms.client.jobs.legend;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.LarknessManager;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;

import java.util.List;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Luminous extends Job {
    public static final int SUNFIRE = 20040216;
    public static final int ECLIPSE = 20040217;
    public static final int EQUILIBRIUM_LIGHT = 20040219;
    public static final int EQUILIBRIUM_DARK = 20040220; // ignore for now, i can't figure out how the flashing works
    public static final int INNER_LIGHT = 20040221;
    public static final int FLASH_BLINK = 20041222;
    public static final int CHANGE_LIGHT_DARK = 20041239;

    public static final int STANDARD_MAGIC_GUARD = 27000003;
    public static final int MAGIC_BOOSTER = 27101004; //Buff
    public static final int BLACK_BLESSING = 27100003;
    public static final int SPELL_MASTERY = 27100005;
    public static final int FLASH_SHOWER = 27001100;
    public static final int ABYSSALL_DROP = 27001201;
    public static final int LIGHT_AFFINITY = 27000106;
    public static final int DARK_AFFINITY = 27000207;
    public static final int PRESSURE_VOID = 27101202;

    public static final int SHADOW_SHELL = 27111004; //Buff
    public static final int RAY_OF_REDEMPTION = 27111101; // Attack + heals party members
    public static final int DUSK_GUARD = 27111005; //Buff
    public static final int PHOTIC_MEDITATION = 27111006; //Buff
    public static final int LUNAR_TIDE = 27110007;
    public static final int DEATH_SCYTHE = 27111303;

    public static final int MAGIC_MASTERY = 27120007;
    public static final int DARK_CRESCENDO = 27121005; //Buff
    public static final int ARCANE_PITCH = 27121006; //Buff
    public static final int MAPLE_WARRIOR_LUMI = 27121009; //Buff
    public static final int APOCALYPSE = 27121202;
    public static final int ENDER = 27121303;
    public static final int DARKNESS_MASTERY = 27120008;
    public static final int HEROS_WILL_LUMI = 27121010;

    public static final int APOCALYPSE_RECHARGE = 27120047;
    public static final int EQUALIZE = 27121054;
    public static final int HEROIC_MEMORIES_LUMI = 27121053;
    public static final int ARMAGEDDON = 27121052; //Stun debuff

    public Luminous(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            if (chr.getTemporaryStatManager().getLarknessManager() == null) {
                chr.getTemporaryStatManager().setLarknessManager(new LarknessManager(chr));
                chr.getTemporaryStatManager().getLarknessManager().updateInfo();
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isLuminous(id);
    }

    private void setLarknessState(boolean dark) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        tsm.getLarknessManager().setDark(dark);
        Option o1 = new Option();
        o1.nOption = 1;
        o1.rOption = dark ? ECLIPSE : SUNFIRE;
        tsm.putCharacterStatValue(Larkness, o1);
        tsm.sendSetStatPacket();
    }

    private void setEquilibrium(boolean dark) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        o1.nOption = 1;
        o1.rOption = dark ? EQUILIBRIUM_DARK : EQUILIBRIUM_LIGHT;
        o1.tOption = chr.getSkillStatValue(time, EQUILIBRIUM_LIGHT) + chr.getSkillStatValue(time, DARKNESS_MASTERY);
        tsm.putCharacterStatValue(Larkness, o1);
        tsm.sendSetStatPacket();

        chr.resetSkillCoolTime(ENDER);
        chr.resetSkillCoolTime(DEATH_SCYTHE);
    }

    public void handleRemoveLarkness(int larknessSkillId) {
        if (larknessSkillId == EQUILIBRIUM_LIGHT || larknessSkillId == EQUILIBRIUM_DARK) {
            setLarknessState(chr.getTemporaryStatManager().getLarknessManager().isDark());
        }
    }

    public void changeBlackBlessingCount(boolean increment) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int count = 1;
        if (tsm.hasStat(BlessOfDarkness)) {
            count = tsm.getOptByCTSAndSkill(BlessOfDarkness, BLACK_BLESSING).nOption;
            if (increment) {
                count = Math.min(count + 1, 3);
            } else {
                count = count - 1;
                if (count <= 0) {
                    tsm.removeStat(BlessOfDarkness, false);
                    tsm.sendResetStatPacket();
                    return;
                }
            }
        }
        Option o1 = new Option();
        o1.nOption = count;
        o1.rOption = BLACK_BLESSING;
        tsm.putCharacterStatValue(BlessOfDarkness, o1);
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
        if (hasHitMobs) {
            handleDarkCresendo();
            handleLarknessHeal(attackInfo.skillId);
        }
        handleLunarTide(); // TODO: create and use callback for HP/MP update
        handleLarknessGauge(attackInfo.skillId);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case ARMAGEDDON:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Freeze, o1);
                }
                break;
            case RAY_OF_REDEMPTION:
                // NOTE: this also goes through SkillHandler, but doesn't get executed because the cooldown is set by AttackHandler
                Rect rect = attackInfo.getForcedPos().getRectAround(si.getFirstRect());
                if (!attackInfo.left) {
                    rect = rect.horizontalFlipAround(attackInfo.getForcedPos().getX());
                }
                int healRate = (int) (chr.getDamageCalc().getMaxBaseDamage() * (si.getValue(hp, slv)) / 100D);
                if (chr.getParty() == null) {
                    chr.heal(!tsm.hasStat(CharacterTemporaryStat.Undead) ? healRate : -healRate, true);
                    chr.write(UserPacket.effect(Effect.skillAffected(skillID, slv, 0)));
                    chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffected(skillID, slv, 0)), chr);
                } else {
                    List<PartyMember> partyMembers = chr.getField().getPartyMembersInRect(chr, rect).stream()
                            .filter(pml -> pml.getChr().getHP() > 0)
                            .toList();
                    for (PartyMember partyMember : partyMembers) {
                        Char partyChr = partyMember.getChr();
                        partyChr.heal(!partyChr.getTemporaryStatManager().hasStat(CharacterTemporaryStat.Undead)
                                ? healRate / partyMembers.size() : -healRate / partyMembers.size(), true);
                        partyChr.write(UserPacket.effect(Effect.skillAffected(skillID, slv, 0)));
                        partyChr.getField().broadcastPacket(UserRemote.effect(partyChr.getId(), Effect.skillAffected(skillID, slv, 0)), partyChr);
                    }
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void handleLunarTide() {
        if (!chr.hasSkill(LUNAR_TIDE)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(LUNAR_TIDE);
        int slv = chr.getSkillLevel(LUNAR_TIDE);
        double hpRatio = (double) chr.getHP() / chr.getMaxHP();
        double mpRatio = (double) chr.getMP() / chr.getMaxMP();
        Option o1 = new Option();
        o1.rOption = LUNAR_TIDE;
        if (hpRatio > mpRatio) {
            o1.nOption = 1; // damR
            o1.mOption = si.getValue(x, slv);
        } else {
            o1.nOption = 2; // cr
            o1.mOption = si.getValue(prop, slv);
        }
        if (o1.nOption != tsm.getOption(LifeTidal).nOption) {
            tsm.putCharacterStatValue(LifeTidal, o1);
            tsm.sendSetStatPacket();
        }
    }

    private void handleDarkCresendo() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(StackBuff)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(DARK_CRESCENDO);
        int slv = chr.getSkillLevel(DARK_CRESCENDO);
        if (!Util.succeedProp(si.getValue(prop, slv))) {
            return;
        }
        int stacks = tsm.getOption(StackBuff).nOption;
        if (stacks < si.getValue(x, slv)) {
            stacks++;
        } else {
            // no need to update, already at max stacks
            return;
        }
        Option o1 = new Option();
        o1.nOption = stacks * si.getValue(damR, slv);
        o1.mOption = stacks;
        o1.rOption = DARK_CRESCENDO;
        o1.tOption = tsm.getRemainingTime(StackBuff, DARK_CRESCENDO);
        o1.setInMillis(true);
        tsm.putCharacterStatValue(StackBuff, o1);
        tsm.sendSetStatPacket();
    }

    private void handleLarknessGauge(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int inc = chr.getSkillStatValue(gauge, skillId);
        if (skillId == APOCALYPSE) {
            inc += chr.getSkillStatValue(gauge, APOCALYPSE_RECHARGE);
        }
        if (SkillConstants.isLarknessLightSkill(skillId)) {
            tsm.getLarknessManager().addGauge(inc, false);
            if (!tsm.hasStat(Larkness)) {
                setLarknessState(false);
            }
        } else if (SkillConstants.isLarknessDarkSkill(skillId)) {
            tsm.getLarknessManager().addGauge(inc, true);
            if (!tsm.hasStat(Larkness)) {
                setLarknessState(true);
            }
        }
    }

    private void handleLarknessHeal(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(Larkness)) {
            int larknessSkillId = tsm.getOption(Larkness).rOption;
            if ((larknessSkillId == SUNFIRE && SkillConstants.isLarknessLightSkill(skillId)) ||
                    (larknessSkillId == ECLIPSE && SkillConstants.isLarknessDarkSkill(skillId)) ||
                    (larknessSkillId == EQUILIBRIUM_LIGHT && SkillConstants.isLarknessLightSkill(skillId))) {
                if (chr.getHP() > 0) {
                    chr.heal((int) (chr.getMaxHP() * (1D / 100D)));
                }
            }
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
        LarknessManager lm = tsm.getLarknessManager();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch(skillID) {
            case SUNFIRE:
            case ECLIPSE:
                if (chr.hasSkill(EQUILIBRIUM_LIGHT)) {
                    setEquilibrium(skillID == ECLIPSE);
                } else {
                    setLarknessState(skillID == ECLIPSE);
                }
                lm.changeMode(skillID == ECLIPSE);
                break;
            case EQUALIZE:
                setEquilibrium(!lm.isDark());
                lm.setDark(!lm.isDark());
                break;
            case MAGIC_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case SHADOW_SHELL:
                o1.nOption = 3; // no SkillStat
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AntiMagicShell, o1);
                chr.resetSkillCoolTime(SHADOW_SHELL);
                break;
            case DUSK_GUARD:
                o1.nValue = si.getValue(indieMdd, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMDD, o1);
                o2.nValue = si.getValue(indiePdd, slv);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePDD, o2);
                break;
            case PHOTIC_MEDITATION:
                o1.nOption = si.getValue(emad, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EMAD, o1);
                break;
            case DARK_CRESCENDO:
                o1.nOption = si.getValue(damR, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.mOption = 1;
                tsm.putCharacterStatValue(StackBuff, o1);
                break;
            case ARCANE_PITCH:
                o1.nOption = si.getValue(y, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ElementalReset, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleSkillPrepare(int prepareSkillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(prepareSkillId);
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        int slv = chr.getSkillLevel(skillID);
        Option o1 = new Option();
        switch (prepareSkillId) {
            case PRESSURE_VOID:
                o1.nOption = 16;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(KeyDownAreaMoving, o1);
                tsm.sendSetStatPacket();
                break;
        }
        super.handleSkillPrepare(prepareSkillId);
    }

    @Override
    public int getMpCon(int skillId, int slv) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int mpCon = super.getMpCon(skillId, slv);
        if (tsm.hasStat(Larkness)) {
            int larknessSkillId = tsm.getOption(Larkness).rOption;
            if ((larknessSkillId == SUNFIRE && SkillConstants.isLarknessLightSkill(skillId)) ||
                    (larknessSkillId == ECLIPSE && SkillConstants.isLarknessDarkSkill(skillId))) {
                mpCon -= (int) (mpCon * (50D / 100D));
            } else if ((larknessSkillId == EQUILIBRIUM_LIGHT || larknessSkillId == EQUILIBRIUM_DARK) && SkillConstants.isLarknessDarkSkill(skillId)) {
                mpCon = 0;
            }
        }
        return mpCon;
    }

    @Override
    public int alterCooldownSkill(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        switch (skillId) {
            case ENDER:
            case DEATH_SCYTHE:
                int larknessSkillId = tsm.getOption(Larkness).rOption;
                if (larknessSkillId == EQUILIBRIUM_LIGHT || larknessSkillId == EQUILIBRIUM_DARK) {
                    return 0;
                }
                break;
        }
        return super.alterCooldownSkill(skillId);
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(BlessOfDarkness) && chr.hasSkill(BLACK_BLESSING) && hitInfo.hpDamage > 0) {
            SkillInfo si = SkillData.getSkillInfoById(BLACK_BLESSING);
            int slv = chr.getSkillLevel(BLACK_BLESSING);
            changeBlackBlessingCount(false);
            int dmgReduceR = si.getValue(x, slv);
            hitInfo.hpDamage -= (int) (hitInfo.hpDamage * ((double) dmgReduceR / 100D));
        }
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleMobDebuffSkill(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStatBySkillId(SHADOW_SHELL)) {
            if (tsm.getOption(AntiMagicShell).nOption > 0) {
                Option o1 = new Option();
                o1.nOption = tsm.getOption(AntiMagicShell).nOption - 1;
                o1.rOption = SHADOW_SHELL;
                tsm.putCharacterStatValue(AntiMagicShell, o1);
                tsm.sendSetStatPacket();
            } else {
                tsm.removeStatsBySkill(SHADOW_SHELL);
                tsm.sendResetStatPacket();
                chr.setSkillCooldown(SHADOW_SHELL, chr.getSkillLevel(SHADOW_SHELL));
            }
            tsm.removeAllDebuffs();
            chr.write(UserPacket.effect(Effect.skillSpecial(SHADOW_SHELL)));
            chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillSpecial(SHADOW_SHELL)), chr);
            return;
        }
        super.handleMobDebuffSkill(chr);
    }


    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        chr.getAvatarData().getCharacterStat().setPosMap(927020080);
    }

    @Override
    public void handleSetJob(short jobId) {
        if (JobConstants.isLuminous(jobId)) {
            // TODO: distribute these skills for the different job levels
            chr.addSkill(EQUILIBRIUM_LIGHT, 1, 1);
            chr.addSkill(CHANGE_LIGHT_DARK, 1, 1);
            chr.addSkill(SUNFIRE, 1, 1);
            chr.addSkill(ECLIPSE, 1, 1);
            chr.addSkill(INNER_LIGHT, 1, 1);
        }
        super.handleSetJob(jobId);
    }
}