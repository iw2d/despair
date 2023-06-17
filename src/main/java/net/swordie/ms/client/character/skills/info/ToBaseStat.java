package net.swordie.ms.client.character.skills.info;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.jobs.adventurer.warrior.Hero;
import net.swordie.ms.enums.BaseStat;

import java.util.Map;

/**
 * Created on 4-7-2019.
 *
 * @author Asura
 */
public class ToBaseStat {

    public static Map<BaseStat, Integer> comboCounter(Char chr, Option o, Map<BaseStat, Integer> stats) {
        int orbAmount = o.nOption - 1;
        int totalFdBonus = 0;
        Skill skill = ((Hero) chr.getJobHandler()).getComboAttackSkill();
        switch (skill.getSkillId()) {
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
        return stats;
    }
}

