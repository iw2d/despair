
package net.swordie.ms.enums;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.info.ToBaseStat;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.jobs.adventurer.archer.Bowmaster;
import net.swordie.ms.client.jobs.adventurer.archer.Marksman;
import net.swordie.ms.client.jobs.resistance.BattleMage;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.loaders.SkillData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 5/4/2018.
 */
public enum BaseStat {
    unk,
    str,
    strR,
    dex,
    dexR,
    inte,
    intR,
    luk,
    lukR,
    pad,
    padR,
    mad,
    madR,
    pdd,
    pddR,
    mdd,
    mddR,
    mhp,
    mhpR,
    mmp,
    mmpR,
    cr, // Crit rate
    minCd, // Min crit damage
    maxCd, // Max crit damage
    damR, // Damage %
    fd, // Final damage (total damage)
    bd, // Boss damage
    ied, // Ignore enemy defense
    asr, // All status resistance
    ter, // Status time minus
    acc,
    accR,
    eva,
    evaR,
    jump,
    speed,
    expR,
    dropR,
    mesoR,
    booster,
    stance,
    mastery,
    damageOver, // max damage
    allStat,
    allStatR,
    hpRecovery,
    mpRecovery,
    incAllSkill,
    strLv,
    dexLv,
    intLv,
    lukLv,
    basicStrR, // % of base stat
    basicDexR,
    basicIntR,
    basicLukR,
    buffTimeR, // Buff Duration multiplier
    recoveryUp, // % increase in heal potion use
    costMpR,
    mpConReduce,
    reduceCooltime,
    padLv,
    madLv,
    mhpLv,
    mmpLv,
    dmgReduce,
    magicGuard, // in %  of HP goes to MP instead.
    invincibleAfterRevive, // in seconds
    shopDiscountR, // % discount on shop items
    pqShopDiscountR, // % discount in pq Shop
    ;


    public static BaseStat getFromStat(Stat s) {
        switch (s) {
            case str:
                return str;
            case dex:
                return dex;
            case inte:
                return inte;
            case luk:
                return luk;
            case mhp:
                return mhp;
            case mmp:
                return mmp;
            default:
                return unk;
        }
    }

    public BaseStat getBasicStatRateVar() {
        switch (this) {
            case str:
                return basicStrR;
            case dex:
                return basicDexR;
            case inte:
                return basicIntR;
            case luk:
                return basicLukR;
            default:
                return null;
        }
    }

    public BaseStat getRateVar() {
        switch (this) {
            case str:
                return strR;
            case dex:
                return dexR;
            case inte:
                return intR;
            case luk:
                return lukR;
            case pad:
                return padR;
            case mad:
                return madR;
            case pdd:
                return pddR;
            case mdd:
                return mddR;
            case mhp:
                return mhpR;
            case mmp:
                return mmpR;
            case acc:
                return accR;
            case eva:
                return evaR;
            default:
                return null;
        }
    }

    public BaseStat getLevelVar() {
        switch(this) {
            case str:
                return strLv;
            case dex:
                return dexLv;
            case inte:
                return intLv;
            case luk:
                return lukLv;
            case pad:
                return padLv;
            case mad:
                return madLv;
            case mhp:
                return mhpLv;
            case mmp:
                return mmpLv;
        }
        return null;
    }

    public static Map<BaseStat, Integer> getFromCTS(Char chr, CharacterTemporaryStat ctsArg, Option o) {
        Map<BaseStat, Integer> stats = new HashMap<>();
        // TODO: Left at "Albatross" in CTS
        SkillInfo si;
        int slv;
        switch (ctsArg) {
            case IndiePAD:
                stats.put(pad, o.nValue);
                break;
            case EPAD:
            case PAD:
                stats.put(pad, o.nOption);
                break;
            case IndieMAD:
                stats.put(mad, o.nValue);
                break;
            case MAD:
            case EMAD:
                stats.put(mad, o.nOption);
                break;
            case IndiePDD:
                stats.put(pdd, o.nValue);
                break;
            case PDD:
            case EPDD:
                stats.put(pdd, o.nOption);
                break;
            case IndieMDD:
                stats.put(mdd, o.nValue);
                break;
            case MDD:
            case EMDD:
                stats.put(mdd, o.nOption);
                break;
            case IndiePADR:
                stats.put(padR, o.nValue);
                break;
            case IndieMADR:
                stats.put(madR, o.nValue);
                break;
            case IndiePDDR:
                stats.put(pddR, o.nValue);
                break;
            case IndieMDDR:
                stats.put(mddR, o.nValue);
                break;
            case IndieMHP:
                stats.put(mhp, o.nValue);
                break;
            case MaxHP:
            case IncMaxHP:
                stats.put(mhp, o.nOption);
                break;
            case IndieMHPR:
                stats.put(mhpR, o.nValue);
                break;
            case MaxMP:
            case IncMaxMP:
                stats.put(mmp, o.nOption);
                break;
            case IndieMMP:
                stats.put(mmp, o.nValue);
                break;
            case IndieMMPR:
                stats.put(mmpR, o.nValue);
                break;
            case IndieACC:
                stats.put(acc, o.nValue);
                break;
            case ACC:
                stats.put(acc, o.nOption);
                break;
            case ACCR:
                stats.put(accR, o.nOption);
                break;
            case IndieEVA:
                stats.put(eva, o.nValue);
                break;
            case EVA:
            case ItemEvade:
                stats.put(eva, o.nOption);
                break;
            case IndieEVAR:
                stats.put(evaR, o.nValue);
                break;
            case EVAR:
            case RWMovingEvar:
                stats.put(evaR, o.nOption);
                break;
            case Speed:
                stats.put(speed, o.nOption);
                break;
            case IndieSpeed:
                stats.put(speed, o.nValue);
                break;
            case IndieJump:
                stats.put(jump, o.nValue);
                break;
            case Jump:
                stats.put(jump, o.nOption);
                break;
            case IndieAllStat:
                stats.put(str, o.nValue);
                stats.put(dex, o.nValue);
                stats.put(inte, o.nValue);
                stats.put(luk, o.nValue);
                break;
            case IndieDodgeCriticalTime:
            case IndieCr:
                stats.put(cr, o.nValue);
                break;
            case Enrage:
                if (JobConstants.getJobCategory(chr.getJob()) == 2) {
                    stats.put(damR, o.nOption / 100);
                } else {
                    stats.put(fd, o.nOption / 100);
                }
                break;
            case EnrageCr:
                stats.put(cr, o.nOption);
                break;
            case EnrageCrDamMin:
                stats.put(minCd, o.nOption);
                break;
            case IncCriticalDamMin:
                stats.put(minCd, o.nValue);
                break;
            case IndieCrMax:
            case IndieCrMaxR:
            case IncCriticalDamMax:
                stats.put(maxCd, o.nValue);
                break;
            case IndieEXP:
            case IndieRelaxEXP:
                stats.put(expR, o.nValue);
                break;
            case HolySymbol:
            case ExpBuffRate:
            case CarnivalExp:
            case PlusExpRate:
                stats.put(expR, o.nOption);
                break;
            case IndieBooster:
                stats.put(booster, o.nValue);
                break;
            case Booster:
            case PartyBooster:
            case HayatoBooster:
                stats.put(booster, o.nOption);
                break;
            case STR:
            case ZeroAuraStr:
                stats.put(str, o.nOption);
                break;
            case IndieSTR:
                stats.put(str, o.nValue);
                break;
            case DEX:
                stats.put(dex, o.nOption);
                break;
            case IndieDEX:
                stats.put(dex, o.nValue);
                break;
            case INT:
                stats.put(inte, o.nOption);
                break;
            case IndieINT:
                stats.put(inte, o.nValue);
                break;
            case LUK:
                stats.put(luk, o.nOption);
                break;
            case IndieLUK:
                stats.put(luk, o.nValue);
                break;
            case DEXR:
                stats.put(basicDexR, o.nOption);
                break;
            case IndieStatR:
                stats.put(strR, o.nValue);
                stats.put(dexR, o.nValue);
                stats.put(intR, o.nValue);
                stats.put(lukR, o.nValue);
                break;
            case DamR:
                stats.put(damR, o.nOption);
                break;
            case IndieDamR:
                stats.put(damR, o.nValue);
            case BeastFormDamageUp:
                stats.put(padR, o.nOption);
                break;
            case IndieAsrR:
                stats.put(asr, o.nValue);
                break;
            case AsrR:
            case AsrRByItem:
            case IncAsrR:
                stats.put(asr, o.nOption);
                break;
            case IndieTerR:
                stats.put(ter, o.nValue);
                break;
            case TerR:
            case IncTerR:
                stats.put(ter, o.nOption);
                break;
            case IndieBDR:
                stats.put(bd, o.nValue);
                break;
            case BdR:
                stats.put(bd, o.nOption);
                break;
            case IndieStance:
                stats.put(stance, o.nValue);
                break;
            case IgnoreMobpdpR:
                stats.put(ied, o.nOption * o.bOption);
                break;
            case IgnoreTargetDEF:
                stats.put(ied, o.nOption);
                break;
            case IndieIgnoreMobpdpR:
                stats.put(ied, o.nValue);
                break;
            case MesoUp:
            case MesoUpByItem:
                stats.put(mesoR, o.nOption);
                break;
            case BasicStatUp:
                stats.put(basicStrR, o.nOption);
                stats.put(basicDexR, o.nOption);
                stats.put(basicIntR, o.nOption);
                stats.put(basicLukR, o.nOption);
                break;
            case Stance:
                stats.put(stance, o.nOption);
                break;
            case PowerGuard:
            case DamageReduce:
            case DamAbsorbShield:
                stats.put(dmgReduce, o.nOption);
                break;
            case ComboCounter:
                ToBaseStat.comboCounter(chr, o, stats);
                break;
            case ComboAbilityBuff:
                ToBaseStat.comboAbilityBuff(chr, o, stats);
                break;
            case MagicGuard:
                stats.put(magicGuard, o.nOption);
                break;
            case SharpEyes:
                stats.put(cr, o.nOption >> 8);
                stats.put(maxCd, o.nOption & 0xFF);
                stats.put(ied, o.mOption);
                break;
            case CriticalBuff:
            case ItemCritical:
                stats.put(cr, o.nOption);
                break;
            case Concentration:
                // TODO
                break;
            case DropRIncrease:
            case DropRate:
                stats.put(dropR, o.nOption);
                break;
            case ItemUpByItem:
                stats.put(dropR, o.nOption);
                break;
            case EventRate:
                // TODO
                break;
            case EMHP:
            case BeastFormMaxHP:
                stats.put(mhp, o.nOption);
                break;
            case EMMP:
                stats.put(mmp, o.nOption);
                break;
            case IndieScriptBuff:
                stats.put(buffTimeR, o.nValue);
                break;
            case ArcaneAim:
                stats.put(damR, o.nOption);
                break;
            case BlessingArmorIncPAD:
                stats.put(pad, o.nOption);
                break;
            case ElementalCharge:
                stats.put(damR, o.nOption);
                stats.put(asr, o.uOption);
                stats.put(pad, o.wOption);
                stats.put(dmgReduce, o.zOption);
                break;
            case CrossOverChain:
                stats.put(fd, o.nOption);
                break;
            case DotBasedBuff:
                si = SkillData.getSkillInfoById(o.nReason); // used for tracking fd
                slv = chr.getSkillLevel(o.nReason);
                stats.put(fd, o.nOption * si.getValue(SkillStat.x, slv));
                break;
            case Infinity:
                stats.put(fd, o.nOption - 1);
                break;
            case IceAura:
                si = SkillData.getSkillInfoById(o.rOption);
                slv = chr.getSkillLevel(o.rOption);
                stats.put(stance, o.nOption * si.getValue(SkillStat.x, slv));
                stats.put(dmgReduce, o.nOption * si.getValue(SkillStat.y, slv));
                stats.put(asr, o.nOption * si.getValue(SkillStat.v, slv));
                stats.put(ter, o.nOption * si.getValue(SkillStat.v, slv));
                break;
            case BlessEnsenble:
                stats.put(fd, o.nOption);
                break;
            case Bless:
                si = SkillData.getSkillInfoById(o.rOption);
                stats.put(pad, si.getValue(SkillStat.x, o.nOption));
                stats.put(mad, si.getValue(SkillStat.y, o.nOption));
                stats.put(pdd, si.getValue(SkillStat.z, o.nOption));
                stats.put(mdd, si.getValue(SkillStat.u, o.nOption));
                stats.put(acc, si.getValue(SkillStat.v, o.nOption));
                stats.put(eva, si.getValue(SkillStat.w, o.nOption));
                break;
            case AdvancedBless:
                si = SkillData.getSkillInfoById(o.rOption);
                stats.put(pad, si.getValue(SkillStat.x, o.nOption));
                stats.put(mad, si.getValue(SkillStat.y, o.nOption));
                stats.put(pdd, si.getValue(SkillStat.z, o.nOption));
                stats.put(mdd, si.getValue(SkillStat.u, o.nOption));
                stats.put(acc, si.getValue(SkillStat.v, o.nOption));
                stats.put(mpConReduce, si.getValue(SkillStat.mpConReduce, o.nOption));
                stats.put(bd, o.xOption);
                break;
            case VengeanceOfAngel:
                si = SkillData.getSkillInfoById(o.rOption);
                slv = chr.getSkillLevel(o.rOption);
                stats.put(damR, o.nOption * -si.getValue(SkillStat.z, slv));
                break;
            case IllusionStep:
                stats.put(evaR, o.nOption);
                break;
            case ExtremeArchery:
                if (o.rOption == Bowmaster.RECKLESS_HUNT_BOW) {
                    stats.put(pad, o.nOption);
                    stats.put(pddR, -o.bOption);
                    stats.put(mddR, -o.bOption);
                } else if (o.rOption == Marksman.RECKLESS_HUNT_XBOW) {
                    stats.put(minCd, o.xOption);
                    stats.put(maxCd, o.nOption);
                    stats.put(evaR, -o.bOption);
                }
                break;
            case BowMasterConcentration:
                stats.put(asr, o.xOption);
                break;
            case BullsEye:
                stats.put(cr, o.nOption >> 8);
                stats.put(maxCd, o.nOption & 0xFF);
                break;
            case FinalCut:
                stats.put(fd, o.nOption - 100);
                break;
            case EnergyCharged:
                ToBaseStat.energyCharged(chr, o, stats);
                break;
            case Dice:
                ToBaseStat.dice(chr, o, stats);
                break;
            case BuckShot:
                si = SkillData.getSkillInfoById(o.rOption);
                slv = chr.getSkillLevel(o.rOption);
                stats.put(fd, -si.getValue(SkillStat.y, slv));
                break;
            case AddAttackCount:
                si = SkillData.getSkillInfoById(o.rOption);
                slv = chr.getSkillLevel(o.rOption);
                stats.put(fd, o.nOption * si.getValue(SkillStat.x, slv));
                break;
            case Judgement:
                ToBaseStat.judgement(chr, o, stats);
                break;
            case BlessOfDarkness:
                ToBaseStat.blessOfDarkness(chr, o, stats);
                break;
            case StackBuff:
                stats.put(damR, o.nOption);
                break;
            case LifeTidal:
                if (o.nOption == 1) {
                    stats.put(damR, o.mOption);
                } else if (o.nOption == 2) {
                    stats.put(cr, o.mOption);
                }
                break;
            case BMageAura:
                if (o.rOption == BattleMage.BLUE_AURA) {
                    stats.put(BaseStat.dmgReduce, o.nOption);
                }
                break;
            case JaguarCount:
                ToBaseStat.jaguarCount(chr, o, stats);
                break;
            case HowlingAttackDamage:
                stats.put(BaseStat.padR, o.nOption);
                stats.put(BaseStat.madR, o.nOption);
                break;
            case HowlingCritical:
                stats.put(BaseStat.cr, o.nOption);
                break;
            case HowlingMaxMP:
                stats.put(BaseStat.mmpR, o.nOption);
                break;
            case HowlingDefence:
                stats.put(BaseStat.dmgReduce, o.nOption);
                break;
            case HowlingEvasion:
                // CUserLocal::CheckMissByEtc
                break;
            default:
                stats.put(unk, o.nOption);
                break;
        }
        return stats;
    }

    public Stat toStat() {
        switch(this) {
            case str:
                return Stat.str;
            case dex:
                return Stat.dex;
            case inte:
                return Stat.inte;
            case luk:
                return Stat.luk;
            case mhp:
                return Stat.mhp;
            case mmp:
                return Stat.mmp;
            default:
                return null;
        }
    }

    public boolean isNonAdditiveStat() {
        return this == fd || this == ied;
    }

    public boolean isLevelStat() {
        switch (this) {
            case strLv:
            case dexLv:
            case intLv:
            case lukLv:
            case padLv:
            case madLv:
            case mhpLv:
            case mmpLv:
                return true;
            default:
                return false;
        }
    }
}
