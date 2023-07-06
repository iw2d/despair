package net.swordie.ms.client.character.skills.info;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.jobs.adventurer.pirate.Buccaneer;
import net.swordie.ms.client.jobs.adventurer.warrior.Hero;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.loaders.SkillData;

import java.util.Map;

import static net.swordie.ms.client.character.skills.SkillStat.*;

/**
 * Created on 4-7-2019.
 *
 * @author Asura
 */
public class ToBaseStat {

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
}

