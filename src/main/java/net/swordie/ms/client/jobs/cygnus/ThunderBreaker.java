package net.swordie.ms.client.jobs.cygnus;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.PartyBooster;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatBase;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.TSIndex;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class ThunderBreaker extends Noblesse {
    public static final int LIGHTNING_ELEMENTAL = 15001022; //Buff (Charge) //Stackable Charge
    public static final int ELECTRIFIED = 15000023;

    public static final int KNUCKLE_MASTERY = 15100023;
    public static final int KNUCKLE_BOOSTER = 15101022; //Buff
    public static final int LIGHTNING_BOOST = 15100025;

    public static final int GALE = 15111022; //Special Attack (Charge)
    public static final int LINK_MASTERY = 15110025; //Special Passive
    public static final int LIGHTNING_LORD = 15110026;

    public static final int KNUCKLE_EXPERT = 15120006;
    public static final int ARC_CHARGER = 15121004; //Buff
    public static final int SPEED_INFUSION = 15121005; //Buff
    public static final int CALL_OF_CYGNUS_TB = 15121000; //Buff
    public static final int TYPHOON = 15120003;
    public static final int THUNDER_GOD = 15120008;

    public static final int GLORY_OF_THE_GUARDIANS_TB = 15121053;
    public static final int PRIMAL_BOLT = 15121054;

    private static final int[] LIGHTNING_BUFF_PASSIVES = {
            ELECTRIFIED,
            LIGHTNING_BOOST,
            LIGHTNING_LORD,
            THUNDER_GOD
    };

    public ThunderBreaker(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isThunderBreaker(id);
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
            if (attackInfo.skillId != GALE && attackInfo.skillId != TYPHOON) {
                handleLightningBuff();
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case GALE:
            case TYPHOON:
                int stacks = tsm.getOption(IgnoreTargetDEF).mOption;
                o1.nOption = stacks * si.getValue(y, slv);
                o1.rOption = GALE;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o1); //Indie
                tsm.sendSetStatPacket();
                // reset lightning buff if no primal bolt buff
                if (!tsm.hasStat(StrikerHyperElectric)) {
                    tsm.removeStat(IgnoreTargetDEF, false);
                    tsm.sendResetStatPacket();
                }
                break;
        }
        super.handleAttack(chr, attackInfo);
    }

    private void handleLightningBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(CygnusElementSkill)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(LIGHTNING_ELEMENTAL);
        int slv = chr.getSkillLevel(LIGHTNING_ELEMENTAL);
        int proc = si.getValue(prop, slv);
        int maxStacks = si.getValue(v, slv);
        int iedPerStack = tsm.hasStat(StrikerHyperElectric) ? chr.getSkillStatValue(x, PRIMAL_BOLT) : si.getValue(x, slv);
        // add passive values
        for (int skillId : LIGHTNING_BUFF_PASSIVES) {
            SkillInfo psi = SkillData.getSkillInfoById(skillId);
            int pslv = chr.getSkillLevel(skillId);
            proc += psi.getValue(prop, pslv);
            maxStacks += psi.getValue(v, pslv);
        }
        if (!Util.succeedProp(proc)) {
            return;
        }
        // increment stack
        int stacks = 0;
        Option oldO = tsm.getOptByCTSAndSkill(IgnoreTargetDEF, LIGHTNING_ELEMENTAL);
        if (oldO != null && oldO.mOption > 0) {
            stacks = oldO.mOption;
        }
        if (stacks < maxStacks) {
            stacks++;
        }
        Option o1 = new Option();
        o1.nOption = stacks * iedPerStack;
        o1.rOption = LIGHTNING_ELEMENTAL;
        o1.tOption = si.getValue(y, slv);
        o1.mOption = stacks;
        tsm.putCharacterStatValue(IgnoreTargetDEF, o1);
        tsm.sendSetStatPacket();
        // reduce arc charger cooldown
        if (chr.hasSkillOnCooldown(ARC_CHARGER)) {
            chr.reduceSkillCoolTime(ARC_CHARGER, 1000L * chr.getSkillStatValue(y, ARC_CHARGER));
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
        switch (skillID) {
            case LIGHTNING_ELEMENTAL:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(CygnusElementSkill, o1);
                break;
            case KNUCKLE_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case ARC_CHARGER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ShadowPartner, o1);
                break;
            case SPEED_INFUSION:
                PartyBooster pb = (PartyBooster) tsm.getTSBByTSIndex(TSIndex.PartyBooster);
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                pb.setOption(o1);
                pb.setCurrentTime(Util.getCurrentTime());
                pb.setExpireTerm(si.getValue(time, slv));
                tsm.putCharacterStatValue(PartyBooster, pb.getOption());
                break;
            case PRIMAL_BOLT:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(StrikerHyperElectric, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indieDamR, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o2);
                // update lightning elemental buff
                if (tsm.hasStat(IgnoreTargetDEF)) {
                    int stacks = tsm.getOption(IgnoreTargetDEF).mOption;
                    int duration = chr.getSkillStatValue(y, LIGHTNING_ELEMENTAL);
                    o3.nOption = stacks * si.getValue(x, slv);
                    o3.rOption = LIGHTNING_ELEMENTAL;
                    o3.tOption = duration;
                    o3.mOption = stacks;
                    tsm.putCharacterStatValue(IgnoreTargetDEF, o3);
                }
                chr.resetSkillCoolTime(TYPHOON);
                chr.resetSkillCoolTime(GALE);
                break;

        }
        tsm.sendSetStatPacket();
    }

    @Override
    public int alterCooldownSkill(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        switch (skillId) {
            case GALE:
            case TYPHOON:
                if (tsm.hasStat(StrikerHyperElectric)) {
                    return 0;
                }
        }
        return super.alterCooldownSkill(skillId);
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }
}
