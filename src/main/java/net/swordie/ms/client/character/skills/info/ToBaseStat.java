package net.swordie.ms.client.character.skills.info;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.jobs.adventurer.pirate.Buccaneer;
import net.swordie.ms.client.jobs.adventurer.warrior.Hero;
import net.swordie.ms.client.jobs.legend.Aran;
import net.swordie.ms.client.jobs.legend.Luminous;
import net.swordie.ms.client.jobs.resistance.BattleMage;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.loaders.SkillData;

import java.util.List;
import java.util.Map;

import static net.swordie.ms.client.character.skills.SkillStat.*;

/**
 * Created on 4-7-2019.
 *
 * @author Asura
 */
public class ToBaseStat {

    private static final List<BaseStat>[] DICE_STAT_TYPE = new List[]{
            List.of(),
            List.of(),
            List.of(BaseStat.pddR, BaseStat.mddR),
            List.of(BaseStat.mhpR, BaseStat.mmpR),
            List.of(BaseStat.cr),
            List.of(BaseStat.damR),
            List.of(BaseStat.expR),
            List.of(BaseStat.ied)
    };
    private static final int[] DICE_STAT_VALUE = { 0, 0, 30, 20, 15, 20, 30, 20 };
    private static final int[] DICE_STAT_VALUE_DD = { 0, 0, 40, 30, 25, 30, 40, 30 };

    public static void comboCounter(Char chr, Option o, Map<BaseStat, Integer> stats) {
        int orbAmount = o.nOption - 1;
        int totalFdBonus = 0;
        int skillId = ((Hero) chr.getJobHandler()).getComboAttackSkill();
        switch (skillId) {
            case Hero.COMBO_SYNERGY:
                totalFdBonus = chr.getSkillStatValue(SkillStat.damR, Hero.COMBO_SYNERGY);
            case Hero.COMBO_ATTACK:
                stats.put(BaseStat.pad, orbAmount * chr.getSkillStatValue(SkillStat.y, Hero.COMBO_ATTACK));
                break;
            case Hero.ADVANCED_COMBO:
                stats.put(BaseStat.pad, orbAmount * chr.getSkillStatValue(SkillStat.y, Hero.COMBO_ATTACK));
                totalFdBonus = chr.getSkillStatValue(SkillStat.v, Hero.ADVANCED_COMBO);
                break;
        }
        if (chr.hasSkill(Hero.ADVANCED_COMBO_REINFORCE)) {
            totalFdBonus += chr.getSkillStatValue(SkillStat.damR, Hero.ADVANCED_COMBO_REINFORCE);
        }
        stats.put(BaseStat.fd, orbAmount * totalFdBonus);

        if (chr.hasSkill(Hero.ADVANCED_COMBO_BOSS_RUSH)) {
            stats.put(BaseStat.bd, orbAmount * chr.getSkillStatValue(SkillStat.w, Hero.ADVANCED_COMBO_BOSS_RUSH));
        }
    }

    public static void energyCharged(Char chr, Option o, Map<BaseStat, Integer> stats) {
        // stats are halved when energy not fully charged, rOption set when fully charged
        int divideBy = o.rOption == 0 ? 2 : 1;
        int skillId = ((Buccaneer) chr.getJobHandler()).getViperEnergySkill();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        stats.put(BaseStat.pad, si.getValue(pad, slv) / divideBy);
        stats.put(BaseStat.acc, si.getValue(acc, slv) / divideBy);
        stats.put(BaseStat.speed, si.getValue(speed, slv) / divideBy);
        stats.put(BaseStat.pdd, si.getValue(pdd, slv) / divideBy);
        stats.put(BaseStat.mdd, si.getValue(mdd, slv) / divideBy);
    }

    public static void dice(Char chr, Option o, Map<BaseStat, Integer> stats) {
        int roll1 = o.nOption % 10;
        int roll2 = o.nOption / 10;
        if (roll1 == roll2) {
            for (BaseStat bs : DICE_STAT_TYPE[roll1]) {
                stats.put(bs, DICE_STAT_VALUE_DD[roll1]);
            }
        } else {
            for (BaseStat bs : DICE_STAT_TYPE[roll1]) {
                stats.put(bs, DICE_STAT_VALUE[roll1]);
            }
            for (BaseStat bs : DICE_STAT_TYPE[roll2]) {
                stats.put(bs, DICE_STAT_VALUE[roll2]);
            }
        }
    }

    public static void comboAbilityBuff(Char chr, Option o, Map<BaseStat, Integer> stats) {
        int stacks = o.nOption / 50;
        if (chr.hasSkill(Aran.COMBO_ABILITY)) {
            SkillInfo si = SkillData.getSkillInfoById(Aran.COMBO_ABILITY);
            int slv = chr.getSkillLevel(Aran.COMBO_ABILITY);
            int maxStacks = si.getValue(x, slv);
            int actualStacks = Math.min(stacks, maxStacks);
            stats.put(BaseStat.pad, actualStacks * si.getValue(y, slv));
            stats.put(BaseStat.pdd, actualStacks * si.getValue(z, slv));
            stats.put(BaseStat.mdd, actualStacks * si.getValue(z, slv));
            stats.put(BaseStat.speed, actualStacks * si.getValue(w, slv));
        }
        if (chr.hasSkill(Aran.ADVANCED_COMBO_ABILITY)) {
            SkillInfo si = SkillData.getSkillInfoById(Aran.ADVANCED_COMBO_ABILITY);
            int slv = chr.getSkillLevel(Aran.ADVANCED_COMBO_ABILITY);
            int maxStacks = si.getValue(x, slv);
            int actualStacks = Math.min(stacks, maxStacks);
            stats.put(BaseStat.pad, stats.getOrDefault(BaseStat.pad, 0) + actualStacks * si.getValue(z, slv));
            stats.put(BaseStat.cr, actualStacks * si.getValue(y, slv));
        }
    }

    public static void judgement(Char chr, Option o, Map<BaseStat, Integer> stats) {
        switch (o.nOption) {
            case 1:
                stats.put(BaseStat.cr, o.xOption);
                break;
            case 2:
                stats.put(BaseStat.dropR, o.xOption);
                break;
            case 3:
                stats.put(BaseStat.asr, o.xOption);
                stats.put(BaseStat.ter, o.xOption);
                break;
            case 4:
                stats.put(BaseStat.pddR, o.xOption); // removed
                break;
            case 5:
                // drain handled in Phantom.handleAttack
                break;
        }
    }

    public static void blessOfDarkness(Char chr, Option o, Map<BaseStat, Integer> stats) {
        if (o.rOption != Luminous.BLACK_BLESSING) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(Luminous.BLACK_BLESSING);
        int slv = chr.getSkillLevel(Luminous.BLACK_BLESSING);
        if (o.nOption == 1) {
            stats.put(BaseStat.mad, si.getValue(u, slv));
        } else if (o.nOption == 2) {
            stats.put(BaseStat.mad, si.getValue(y, slv));
        } else if (o.nOption == 3) {
            stats.put(BaseStat.mad, si.getValue(y, slv));
        }
    }
}

