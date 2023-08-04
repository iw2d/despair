package net.swordie.ms.client.character.skills.info;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.BodyPart;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.jobs.adventurer.magician.Bishop;
import net.swordie.ms.client.jobs.adventurer.thief.Shadower;
import net.swordie.ms.client.jobs.adventurer.warrior.Paladin;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.enums.Stat;
import net.swordie.ms.enums.WeaponType;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

/**
 * Created on 12/20/2017.
 */
public class SkillInfo {
    private static final Logger log = LogManager.getLogger(SkillInfo.class);

    private int skillId;
    private int rootId;
    private int maxLevel;
    private int currentLevel;
    private String actionName = "";
    private int actionDelay;
    private Map<SkillStat, String> skillStatInfo = new HashMap<>();
    private boolean invisible;
    private int masterLevel;
    private int fixLevel;
    private List<Rect> rects = new ArrayList<>();
    private boolean massSpell;
    private int type;
    private Set<Integer> psdSkills = new HashSet<>();
    private String elemAttr = "";
    private int hyper;
    private int hyperstat;
    private int vehicleId;
    private int reqTierPoint;
    private Map<Integer, Integer> reqSkills = new HashMap<>();
    private boolean notCooltimeReset;
    private boolean notIncBuffDuration;
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    private boolean psd;
    private Set<Integer> addAttackSkills = new HashSet<>();
    private Map<Integer, Integer> extraSkillInfo = new HashMap<>();
    private Map<WeaponType, Map<SkillStat, Integer>> psdWT = new HashMap<>(); // <WeaponType, <SkillStat, Value>>
    private Map<Integer, Map<SkillStat, Integer>> cachedData = new HashMap<>(); // <Level, <SkillStat, Value>>

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getActionDelay() {
        return actionDelay;
    }

    public void setActionDelay(int actionDelay) {
        this.actionDelay = actionDelay;
    }

    public Map<SkillStat, String> getSkillStatInfo() {
        return skillStatInfo;
    }

    public void setSkillStatInfo(Map<SkillStat, String> skillStatInfo) {
        this.skillStatInfo = skillStatInfo;
    }

    public void addSkillStatInfo(SkillStat sc, String value) {
        getSkillStatInfo().put(sc, value);
    }

    public int getValue(SkillStat skillStat, int slv) {
        int result = 0;
        String value = getSkillStatInfo().get(skillStat);
        if(value == null || slv == 0) {
            return 0;
        }
        if (!cachedData.containsKey(slv)) {
            cachedData.put(slv, new HashMap<>());
        }
        Map<SkillStat, Integer> lvCache = cachedData.get(slv);
        if (lvCache.containsKey(skillStat)) {
            return lvCache.get(skillStat);
        }
        // Not in cache: evaluate
        // Sometimes newlines get taken, just remove those
        value = value.replace("\n", "").replace("\r", "");
        value = value.replace("\\n", "").replace("\\r", ""); // unluko
        String original = value;
        if(Util.isNumber(value)) {
            result = Integer.parseInt(value);
        } else {
            try {
                value = value.replace("u", "Math.ceil");
                value = value.replace("d", "Math.floor");
                String toReplace = value.contains("y") ? "y"
                        : value.contains("X") ? "X"
                        : "x";
                Object res = engine.eval(value.replace(toReplace, slv + ""));
                if(res instanceof Integer) {
                    result = (Integer) res;
                } else if(res instanceof Double) {
                    result = ((Double) res).intValue();
                }
            } catch (ScriptException e) {
                log.error(String.format("Error when parsing: skill %d, level %d, skill stat %s, tried to eval %s.",
                        getSkillId(), slv, skillStat, original));
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    public void setMasterLevel(int masterLevel) {
        this.masterLevel = masterLevel;
    }

    public int getFixLevel() {
        return fixLevel;
    }

    public void setFixLevel(int fixLevel) {
        this.fixLevel = fixLevel;
    }

    public void addRect(Rect rect) {
        getRects().add(rect);
    }

    public List<Rect> getRects() {
        return rects;
    }

    public Rect getLastRect() {
        return rects != null && rects.size() > 0 ? rects.get(rects.size() - 1) : null;
    }

    public Rect getFirstRect() {
        return rects != null && rects.size() > 0 ? rects.get(0) : null;
    }

    public boolean isMassSpell() {
        // overrides
        if (skillId == Bishop.HOLY_SYMBOL) {
            return true;
        }
        return massSpell;
    }

    public void setMassSpell(boolean massSpell) {
        this.massSpell = massSpell;
    }

    public boolean hasCooltime() {
        return getValue(SkillStat.cooltime, 1) > 0 || getValue(SkillStat.cooltimeMS, 1) > 0;
    }

    public Map<BaseStat, Integer> getBaseStatValues(Char chr, int slv) {
        Map<BaseStat, Integer> stats = new HashMap<>();
        for (SkillStat ss : getSkillStatInfo().keySet()) {
            Tuple<BaseStat, Integer> bs = getBaseStatValue(ss, chr, slv);
            if (bs.getLeft() != null) {
                stats.put(bs.getLeft(), bs.getRight());
            }
        }
        SkillConstants.fixBaseStatsBySkill(stats, this, slv);
        return stats;
    }

    public Map<BaseStat, Integer> getPsdWTStatValues(Char chr, int slv) {
        Map<BaseStat, Integer> stats = new HashMap<>();
        getSkillStatsByWT(chr.getEquippedWeaponType()).forEach((ss, value) ->
                stats.put(ss.getBaseStat(), value + stats.getOrDefault(ss.getBaseStat(), 0))
        );
        // handle shield mastery
        if (SkillConstants.isShieldMasterySkill(skillId)) {
            Item shield = chr.getEquippedItemByBodyPart(BodyPart.Shield);
            if (shield != null && ItemConstants.isShield(shield.getItemId())) {
                getBaseStatValues(chr, slv).forEach((bs, value) -> {
                    stats.put(bs, value);
                });
                switch (getSkillId()) {
                    case Paladin.SHIELD_MASTERY:
                    case Shadower.SHIELD_MASTERY:
                        stats.put(BaseStat.pddR, getValue(SkillStat.x, slv));
                        stats.put(BaseStat.mddR, getValue(SkillStat.x, slv));
                        stats.put(BaseStat.pad, getValue(SkillStat.y, slv));
                        break;
                }
            }
        }
        return stats;
    }

    public Tuple<BaseStat, Integer> getBaseStatValue(SkillStat ss, Char chr, int slv) {
        BaseStat bs = ss.getBaseStat();
        int value = getValue(ss, slv);
        switch (ss) {
            case lv2damX:
                value *= chr.getLevel();
                break;
            case str2dex:
                value *= chr.getStat(Stat.str);
                break;
            case dex2luk:
            case dex2str:
                value *= chr.getStat(Stat.dex);
                break;
            case int2luk:
                value *= chr.getStat(Stat.inte);
                break;
            case luk2dex:
            case luk2int:
                value *= chr.getStat(Stat.luk);
                break;
            case mhp2damX:
                value *= chr.getStat(Stat.mhp);
                break;
            case mmp2damX:
                value *= chr.getStat(Stat.mmp);
                break;
        }
        return new Tuple<>(bs, value);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setRects(List<Rect> rects) {
        this.rects = rects;
    }

    public void addPsdSkill(int skillID) {
        getPsdSkills().add(skillID);
    }

    public Set<Integer> getPsdSkills() {
        return psdSkills;
    }

    public void setPsdSkills(Set<Integer> psdSkills) {
        this.psdSkills = psdSkills;
    }

    public String getElemAttr() {
        return elemAttr;
    }

    public void setElemAttr(String elemAttr) {
        this.elemAttr = elemAttr;
    }

    public void setHyper(int hyper) {
        this.hyper = hyper;
    }

    public int getHyper() {
        return hyper;
    }

    public void setHyperStat(int hyperstat) {
        this.hyperstat = hyperstat;
    }

    public int getHyperStat() {
        return hyperstat;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setReqTierPoint(int reqTierPoint) {
        this.reqTierPoint = reqTierPoint;
    }

    public int getReqTierPoint() {
        return reqTierPoint;
    }

    public void addReqSkill(int skillID, int slv) {
        getReqSkills().put(skillID, slv);
    }

    public Map<Integer, Integer> getReqSkills() {
        return reqSkills;
    }

    public void setNotCooltimeReset(boolean notCooltimeReset) {
        this.notCooltimeReset = notCooltimeReset;
    }

    public boolean isNotCooltimeReset() {
        return notCooltimeReset;
    }

    public void setNotIncBuffDuration(boolean notIncBuffDuration) {
        this.notIncBuffDuration = notIncBuffDuration;
    }

    public boolean isNotIncBuffDuration() {
        return notIncBuffDuration;
    }

    public void setPsd(boolean psd) {
        this.psd = psd;
    }

    public boolean isPsd() {
        return psd;
    }

    public Set<Integer> getAddAttackSkills() {
        return addAttackSkills;
    }

    public void setAddAttackSkills(Set<Integer> addAttackSkills) {
        this.addAttackSkills = addAttackSkills;
    }

    public void addAddAttackSkills(int skillId) {
        getAddAttackSkills().add(skillId);
    }

    public Map<Integer, Integer> getExtraSkillInfo() {
        return extraSkillInfo;
    }

    public void setExtraSkillInfo(Map<Integer, Integer> extraSkillInfo) {
        this.extraSkillInfo = extraSkillInfo;
    }

    public void addExtraSkillInfo(int skillid, int delay) {
        getExtraSkillInfo().put(skillid, delay);
    }

    public boolean isIgnoreCounter() {
        return getValue(SkillStat.ignoreCounter, 1) != 0;
    }

    public Map<WeaponType, Map<SkillStat, Integer>> getPsdWT() {
        return psdWT;
    }

    public void setPsdWT(Map<WeaponType, Map<SkillStat, Integer>> psdWT) {
        this.psdWT = psdWT;
    }

    public Map<SkillStat, Integer> getSkillStatsByWT(WeaponType weaponType) {
        return getPsdWT().getOrDefault(weaponType, Collections.EMPTY_MAP);
    }

    public void addPsdWT(WeaponType wt, Map<SkillStat, Integer> skillStats) {
        getPsdWT().put(wt, skillStats);
    }

    public boolean isPsdWTSkill() {
        return getPsdWT().size() > 0;
    }
}
