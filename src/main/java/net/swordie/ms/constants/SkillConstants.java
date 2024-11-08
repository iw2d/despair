package net.swordie.ms.constants;

import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.jobs.*;
import net.swordie.ms.client.jobs.adventurer.*;
import net.swordie.ms.client.jobs.adventurer.archer.*;
import net.swordie.ms.client.jobs.adventurer.magician.*;
import net.swordie.ms.client.jobs.adventurer.pirate.*;
import net.swordie.ms.client.jobs.adventurer.thief.*;
import net.swordie.ms.client.jobs.adventurer.warrior.*;
import net.swordie.ms.client.jobs.cygnus.*;
import net.swordie.ms.client.jobs.legend.*;
import net.swordie.ms.client.jobs.nova.AngelicBuster;
import net.swordie.ms.client.jobs.resistance.*;
import net.swordie.ms.client.jobs.resistance.demon.*;
import net.swordie.ms.enums.BaseStat;
import net.swordie.ms.enums.BeastTamerBeasts;
import net.swordie.ms.loaders.SkillData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static net.swordie.ms.client.jobs.nova.Kaiser.*;

/**
 * Created on 12/18/2017.
 */
public class SkillConstants {
    public static final short PASSIVE_HYPER_MIN_LEVEL = 140;
    public static final List<Short> ACTIVE_HYPER_LEVELS = Arrays.asList((short) 150, (short) 170, (short) 200);

    private static final Logger log = LogManager.getLogger(SkillConstants.class);

    public static final short LINK_SKILL_1_LEVEL = 70;
    public static final short LINK_SKILL_2_LEVEL = 120;
    public static final short LINK_SKILL_3_LEVEL = 210;

    public static final byte PASSIVE_HYPER_JOB_LEVEL = 6;
    public static final byte ACTIVE_HYPER_JOB_LEVEL = 7;

    public static final int MAKING_SKILL_EXPERT_LEVEL = 10;
    public static final int MAKING_SKILL_MASTER_LEVEL = 11;
    public static final int MAKING_SKILL_MEISTER_LEVEL = 12;

    public static final int MINING_SKILL = 92010000;
    public static final int HERBALISM_SKILL = 92000000;

    public static final HashSet<Integer> KEYDOWN_SKILLS = new HashSet<>(Arrays.asList(
            Shade.SPIRIT_INCARNATION,
            Shade.SPIRIT_FRENZY,
            BeastTamer.FISHY_SLAP,
            BeastTamer.TORNADO_FLIGHT,
            PinkBean.LETS_ROLL,
            DemonAvenger.VITALITY_VEIL,
            AngelicBuster.SOUL_RESONANCE,
            BlazeWizard.DRAGON_BLAZE,
            Phantom.TEMPEST,
            IceLightning.LIGHTNING_ORB,
            Jett.FALLING_STARS,
            Jett.BACKUP_BEATDOWN,
            AngelicBuster.SUPREME_SUPERNOVA
    ));

    public static final List<BaseStat>[] DICE_STAT_TYPE = new List[]{
            List.of(),
            List.of(),
            List.of(BaseStat.pddR, BaseStat.mddR),
            List.of(BaseStat.mhpR, BaseStat.mmpR),
            List.of(BaseStat.cr),
            List.of(BaseStat.damR),
            List.of(BaseStat.expR),
            List.of(BaseStat.ied)
    };
    public static final int[] DICE_STAT_VALUE = { 0, 0, 30, 20, 15, 20, 30, 20 };
    public static final int[] DICE_STAT_VALUE_DD = { 0, 0, 40, 30, 25, 30, 40, 30 };

    public static boolean isSkillNeedMasterLevel(int skillId) {
        // bool __cdecl is_skill_need_master_level(int)
        if (isIgnoreMasterLevel(skillId)
                || (skillId / 1000000 == 92 && (skillId % 10000 == 0))
                || isMakingSkillRecipe(skillId)
                || isCommonSkill(skillId)
                || isNoviceSkill(skillId)
                || isFieldAttackObjSkill(skillId)) {
            return false;
        }
        int job = getSkillRootFromSkill(skillId);
        return skillId != 42120024 && !JobConstants.isBeastTamer((short) job)
                && (isAddedSpDualAndZeroSkill(skillId) || (JobConstants.getJobLevel((short) job) == 4 && !JobConstants.isZero((short) job)));
    }

    public static boolean isAddedSpDualAndZeroSkill(int skillId) {
        // bool __cdecl is_added_sp_dual_and_zero_skill(int)
        switch (skillId) {
            case 4311003:
            case 4321006:
            case 4330009:
            case 4331002:
            case 4340007:
            case 4341004:
            case 101000101:
            case 101100101:
            case 101100201:
            case 101110102:
            case 101110200:
            case 101110203:
            case 101120104:
            case 101120204:
                return true;
            default:
                return false;
        }
    }

    public static boolean isWildHunterShotSkill(int skillId) {
        // bool __cdecl is_wildhunter_shot_skill
        if (skillId > 33111212) {
            if (skillId == 33121214) {
                return true;
            }
        } else if (skillId == 33111212 || skillId == 33001205 || skillId == 33101213) {
            return true;
        }
        if (skillId > 33111112) {
            return skillId == 33121114;
        }
        if (skillId == 33111112 || skillId == 33001105) {
            return true;
        }
        return skillId == 33101113;
    }

    public static int getSkillRootFromSkill(int skillId) {
        int prefix = skillId / 10000;
        if (prefix == 8000) {
            prefix = skillId / 100;
        }
        return prefix;
    }

    public static boolean isFieldAttackObjSkill(int skillId) {
        if (skillId <= 0) {
            return false;
        }
        int prefix = skillId / 10000;
        if (skillId / 10000 == 8000) {
            prefix = skillId / 100;
        }
        return prefix == 9500;
    }

    private static boolean isNoviceSkill(int skillId) {
        int prefix  = skillId / 10000;
        if (skillId / 10000 == 8000) {
            prefix = skillId / 100;
        }
        return JobConstants.isBeginnerJob((short) prefix);
    }

    private static boolean isCommonSkill(int skillId) {
        int prefix = skillId / 10000;
        if (skillId / 10000 == 8000) {
            prefix = skillId / 100;
        }
        return (prefix >= 800000 && prefix <= 800099) || prefix == 8001;
    }

    public static boolean isMakingSkillRecipe(int recipeId) {
        boolean result = false;
        if (recipeId / 1000000 != 92 || recipeId % 10000 == 1) {
            int v1 = 10000 * (recipeId / 10000);
            if (v1 / 1000000 == 92 && (v1 % 10000 == 0))
                result = true;
        }
        return result;
    }

    public static boolean isIgnoreMasterLevel(int skillId) {
        // bool __cdecl is_ignore_master_level(int)
        switch (skillId) {
            case 1120012:
            case 1320011:
            case 2121009:
            case 2221009:
            case 2321010:
            case 3210015:
            case 4110012:
            case 4210012:
            case 4340010:
            case 4340012:
            case 5120011:
            case 5120012:
            case 5220012:
            case 5220014:
            case 5320007:
            case 5321004:
            case 5321006:
            case 21120011:
            case 21120014:
            case 21120020:
            case 21120021:
            case 21121008:
            case 22171069:
            case 23120011:
            case 23120013:
            case 23121008:
            case 33120010:
            case 35120014:
            case 51120000:
            case 80001913:
                return true;
            default:
                return false;
        }
    }

    public static boolean isKeyDownSkill(int skillId) {
        return skillId == 2321001 || skillId == 80001836 || skillId == 37121052 || skillId == 36121000 ||
                skillId == 37121003 || skillId == 36101001 || skillId == 33121114 || skillId == 33121214 ||
                skillId == 35121015 || skillId == 33121009 || skillId == 32121003 || skillId == 31211001 ||
                skillId == 31111005 || skillId == 30021238 || skillId == 31001000 || skillId == 31101000 ||
                skillId == 80001887 || skillId == 80001880 || skillId == 80001629 || skillId == 20041226 ||
                skillId == 60011216 || skillId == 65121003 || skillId == 80001587 || skillId == 131001008 ||
                skillId == 142111010 || skillId == 131001004 || skillId == 95001001 || skillId == 101110100 ||
                skillId == 101110101 || skillId == 101110102 || skillId == 27111100 || skillId == 12121054 ||
                skillId == 11121052 || skillId == 11121055 || skillId == 5311002 || skillId == 4341002 ||
                skillId == 5221004 || skillId == 5221022 || skillId == 3121020 || skillId == 3101008 ||
                skillId == 3111013 || skillId == 1311011 || skillId == 2221011 || skillId == 2221052 ||
                skillId == 25121030 || skillId == 27101202 || skillId == 25111005 || skillId == 23121000 ||
                skillId == 22171083 || skillId == 14121004 || skillId == 13111020 || skillId == 13121001 ||
                skillId == 14111006 || (skillId >= 80001389 && skillId <= 80001392) || skillId == 42121000 ||
                skillId == 42120003 || skillId == 5700010 || skillId == 5711021 || skillId == 5721001 ||
                skillId == 5721061 || skillId == 21120018 || skillId == 21120019 || skillId == 24121000 || skillId == 24121005;
    }

    public static boolean isEvanForceSkill(int skillId) {
        switch (skillId) {
            case 22140022:
            case 22111011:
            case 22111012:
            case 22110022:
            case 22110023:
            case 22111017:
            case 80001894:
            case 22171062:
            case 22171063:
            case 22141011:
            case 22141012:
                return true;
        }
        return false;
    }

    public static boolean isSuperNovaSkill(int skillID) {
        return skillID == 4221052 || skillID == 65121052;
    }

    public static boolean isRushBombSkill(int skillID) {
        switch (skillID) {
            case 101120205:
            case 101120203:
            case 80011386:
            case 101120200:
            case 80011380:
            case 61111113:
            case 61111218:
            case 61111111:
            case 61111100:
            case 40021186:
            case 31201001:
            case 27121201:
            case 22140015:
            case 22140024:
            case 14111022:
            case 5101014:
            case 5301001:
            case 12121001:
            case 2221012:
            case 5101012:
                return true;

        }
        return false;
    }

    public static boolean isZeroSkill(int skillID) {
        int prefix = skillID / 10000;
        if(prefix == 8000) {
            prefix = skillID / 100;
        }
        return prefix == 10000 || prefix == 10100 || prefix == 10110 || prefix == 10111 || prefix == 10112;
    }

    public static boolean isUsercloneSummonedAbleSkill(int skillID) {
        switch (skillID) {
            case 11101120:        // Flicker
            case 11101121:        // Trace Cut
            case 11101220:        // Bluster
            case 11101221:        // Shadow Tackle
            case 11111120:        // Moon Shadow
            case 11111121:        // Moon Cross
            case 11111220:        // Light Merger
            case 11111221:        // Sun Cross
            case 11121101:        // Moon Dancer
            case 11121102:        // Moon Dancer
            case 11121103:        // Crescent Divide
            case 11121201:        // Speeding Sunset
            case 11121202:        // Speeding Sunset
            case 11121203:        // Solar Pierce

            case 14001020:        // Lucky Seven
            case 14101020:        // Triple Throw
            case 14101021:        // Triple Throw
            case 14111020:        // Quad Star
            case 14111021:        // Quad Star
            case 14111022:        // Shadow Spark
            case 14111023:        // Shadow Spark
            case 14121001:        // Quintuple Star
            case 14121002:        // Quintuple Star

            case 23001000:        // Swift Dual Shot
            case 23100004:        // Parting Shot
            case 23101000:        // Piercing Storm
            case 23101001:        // Rising Rush
            case 23101007:        // Rising Rush
            case 23111000:        // Stunning Strikes
            case 23111001:        // Leap Tornado
            case 23111003:        // Gust Dive
            case 23110006:        // Aerial Barrage
            case 23120013:        // Staggering Strikes
            case 23121052:        // Wrath of Enlil
            case 23121000:        // Ishtar's Ring
            case 400031024:       // Irkalla's Wrath

            case 131001000:       // Pink Powerhouse
            case 131001001:       // Pink Powerhouse
            case 131001002:       // Pink Powerhouse
            case 131001003:       // Pink Powerhouse
            case 131001004:       // Let's Roll!
            case 131001005:       // Umbrella
            case 131001008:       // Sky Jump
            case 131001010:       // Blazing Yo-yo
            case 131001011:       // Blazing Yo-yo
            case 131001012:       // Pink Pulverizer
            case 131001013:       // Let's Rock!
            case 131001101:       // Pink Powerhouse
            case 131001102:       // Pink Powerhouse
            case 131001103:       // Pink Powerhouse
            case 131001104:       // Let's Roll!
            case 131001108:       // Mid-air Sky Jump
            case 131001113:       // Electric Guitar
            case 131001208:       // Sky Jump Grounder
            case 131001213:       // Whistle
            case 131001313:       // Megaphone
            case 131002010:       // Blazing Yo-yo
                return true;
        }
        return false;
    }

    public static boolean isNoconsumeUsebulletMeleeAttack(int skillID) {
        return skillID == 14121052 || skillID == 14121003 || skillID == 14000028 || skillID == 14000029;
    }

    public static boolean isScreenCenterAttackSkill(int skillID) {
        return skillID == 80001431 || skillID == 100001283 || skillID == 21121057 || skillID == 13121052 ||
                skillID == 14121052 || skillID == 15121052 || skillID == 80001429;
    }

    public static boolean isAranFallingStopSkill(int skillID) {
        switch(skillID) {
            case 21110028:
            case 21120025:
            case 21110026:
            case 21001010:
            case 21000006:
            case 21000007:
            case 21110022:
            case 21110023:
            case 80001925:
            case 80001926:
            case 80001927:
            case 80001936:
            case 80001937:
            case 80001938:
                return true;
            default:
                return false;
        }
    }

    public static boolean isFlipAffectAreaSkill(int skillID) {
        return skillID == 33111013 || skillID == 33121016 || skillID == 33121012 || skillID == 131001207 ||
                skillID == 131001107 || skillID == 4121015 || skillID == 51120057 || skillID == Mechanic.DISTORTION_BOMB;
    }

    public static boolean isShootSkillNotConsumingBullets(int skillID) {
        int job = skillID / 10000;
        if (skillID / 10000 == 8000) {
            job = skillID / 100;
        }
        switch (skillID) {
            case 80001279:
            case 80001914:
            case 80001915:
            case 80001880:
            case 80001629:
            case 33121052:
            case 33101002:
            case 14101006:
            case 13101020:
            case 1078:
                return true;
            default:
                return getDummyBulletItemIDForJob(job, 0, 0) > 0
                        || isShootSkillNotUsingShootingWeapon(skillID, false)
                        || isFieldAttackObjSkill(skillID);

        }
    }

    private static boolean isShootSkillNotUsingShootingWeapon(int skillID, boolean bySteal) {
        if(bySteal || (skillID >= 80001848 && skillID <= 80001850)) {
            return true;
        }
        switch (skillID) {
            case 80001863:
            case 80001880:
            case 80001914:
            case 80001915:
            case 80001939:
            case 101110204:
            case 101110201:
            case 101000202:
            case 101100202:
            case 80001858:
            case 80001629:
            case 80001829:
            case 80001838:
            case 80001856:
            case 80001587:
            case 80001418:
            case 80001387:
            case 61111215:
            case 80001279:
            case 61001101:
            case 51121008:
            case 51111007:
            case 51001004:
            case 36111010:
            case 36101009:
            case 31111005:
            case 31111006: // ? was 26803624, guessing it's just a +1
            case 31101000:
            case 22110024:
            case 22110014:
            case 21120006:
            case 21100007:
            case 21110027:
            case 21001009:
            case 21000004:
            case 5121013:
            case 1078:
            case 1079:
                return true;
            default:
                return false;

        }
    }

    private static int getDummyBulletItemIDForJob(int job, int subJob, int skillID) {
        if ( job / 100 == 35 )
            return 2333000;
        if ( job / 10 == 53 || job == 501 || (job / 1000) == 0 && subJob == 2 )
            return 2333001;
        if ( JobConstants.isMercedes((short) job) )
            return 2061010;
        if ( JobConstants.isAngelicBuster((short) job) )
            return 2333001;
        // TODO:
//        if ( !JobConstants.isPhantom((short) job)
//                || !is_useable_stealedskill(skillID)
//                || (result = get_vari_dummy_bullet_by_cane(skillID), result <= 0) )
//        {
//            result = 0;
//        }
        return 0;
    }

    public static boolean isKeydownSkillRectMoveXY(int skillID) {
        return skillID == WindArcher.SENTIENT_ARROW;
    }

    public static int getOriginalOfLinkedSkill(int skillID) {
        int result = 0;
        switch(skillID) {
            case 80010006:
                result = 110000800;
                break;
            case 80001040:
                result = 20021110;
                break;
            case 80001140:
                result = 50001214;
                break;
            case 80001155:
                result = 60011219;
                break;
            case 80000378:
                result = 30000077;
                break;
            case 80000334:
                result = 30000075;
                break;
            case 80000335:
                result = 30000076;
                break;
            case 80000369:
                result = 20010294;
                break;
            case 80000370:
                result = 20000297;
                break;
            case 80000333:
                result = 30000074;
                break;
            case 80000000:
                result = 110;
                break;
            case 80000001:
                result = 30010112;
                break;
            case 80000002:
                result = 20030204;
                break;
            case 80000005:
                result = 20040218;
                break;
            case 80000006:
                result = 60000222;
                break;
            case 80000047:
                result = 30020233;
                break;
            case 80000050:
                result = 30010241;
                break;
            case 80000066:
                result = 10000255;
                break;
            case 80000067:
                result = 10000256;
                break;
            case 80000068:
                result = 10000257;
                break;
            case 80000069:
                result = 10000258;
                break;
            case 80000070:
                result = 10000259;
                break;
            case 80000110:
                result = 100000271;
                break;
            case 80000169:
                result = 20050286;
                break;
            case 80000188:
                result = 140000292;
                break;
            case 80000004:
                result = 40020002;
                break;
            case 0:
                result = 0;
                break;
            default:
                log.error("Unknown corresponding link skill for link skill id " + skillID);
        }
        return result;
    }

    public static boolean isZeroAlphaSkill(int skillID) {
        return isZeroSkill(skillID) && skillID % 1000 / 100 == 2;
    }

    public static boolean isZeroBetaSkill(int skillID) {
        return isZeroSkill(skillID) && skillID % 1000 / 100 == 1;
    }

    public static boolean isLightmageSkill(int skillID) {
        int prefix = skillID / 10000;
        if(prefix == 8000) {
            prefix = skillID / 100;
        }
        return prefix / 100 == 27 || prefix == 2004;
    }

    public static boolean isLarknessDarkSkill(int skillID) {
        return skillID != 20041222 && isLightmageSkill(skillID) && skillID / 100 % 10 == 2;
    }

    public static boolean isLarknessLightSkill(int skillID) {
        return skillID != 20041222 && isLightmageSkill(skillID) && skillID / 100 % 10 == 1;
    }

    public static boolean isEquilibriumSkill(int skillID) {
        return skillID >= 20040219 && skillID <= 20040220;
    }

    public static int getAdvancedCountHyperSkill(int skillId) {
        switch(skillId) {
            case 4121013:
                return 4120051;
            case 5321012:
                return 5320051;
            default:
                return 0;
        }
    }

    public static int getAdvancedAttackCountHyperSkill(int skillId) {
        switch(skillId) {
            case 25121005:
                return 25120148;
            case 31121001:
                return 31120050;
            case 31111005:
                return 31120044;
            case 22140023:
                return 22170086;
            case 21120022:
            case 21121015:
            case 21121016:
            case 21121017:
                return 21120066;
            case 21120006:
                return 21120049;
            case 21110020:
            case 21111021:
                return 21120047;
            case 15121002:
                return 15120048;
            case 14121002:
                return 14120045;
            case 15111022:
            case 15120003:
                return 15120045;
            case 51121008:
                return 51120048;
            case 32111003:
                return 32120047;
            case 35121016:
                return 35120051;
            case 37110002:
                return 37120045;
            case 51120057:
                return 51120058;
            case 51121007:
                return 51120051;
            case 65121007:
            case 65121008:
            case 65121101:
                return 65120051;
            case 61121201:
            case 61121100:
                return 61120045;
            case 51121009:
                return 51120058;
            case 13121002:
                return 13120048;
            case 5121016:
            case 5121017:
                return 5120051;
            case 3121015:
                return 3120048;
            case 2121006:
                return 2120048;
            case 2221006:
                return 2220048;
            case 1221011:
                return 1220050;
            case 1120017:
            case 1121008:
                return 1120051;
            case 1221009:
                return 1220048;
            case 4331000:
                return 4340045;
            case 3121020:
                return 3120051;
            case 3221017:
                return 3220048;
            case 4221007:
                return 4220048;
            case 4341009:
                return 4340048;
            case 5121007:
                return 5120048;
            case 5321004:
                return 5320043;
            // if ( nSkillID != &loc_A9B1CF ) nothing done with line 172?
            case 12110028:
            case 12000026:
            case 12100028:
                return 12120045;
            case 12120010:
                return 12120045;
            case 12120011:
                return 12120046;
            default:
                return 0;
        }
    }

    public static boolean isKinesisPsychicLockSkill(int skillId) {
        switch(skillId) {
            case 142120000:
            case 142120001:
            case 142120002:
            case 142120014:
            case 142111002:
            case 142100010:
            case 142110003:
            case 142110015:
                return true;
            default:
                return false;
        }
    }

    public static int getActualSkillIDfromSkillID(int skillID) {
        switch (skillID) {
            case 101120206: //Zero - Severe Storm Break (Tile)
                return 101120204; //Zero - Adv Storm Break

            case 4221016: //Shadower - Assassinate 2
                return 4221014; //Shadower - Assassinate 1

            case 41121020: //Hayato - Tornado Blade-Battoujutsu Link
                return 41121017; //Tornado Blade

            case 41121021: //Hayato - Sudden Strike-Battoujutsu Link
                return 41121018; //Sudden Strike

            case 5121017: //Bucc - Double Blast
                return 5121016; //Bucc - Buccaneer Blast

            case 5101014: //Bucc - Energy Vortex
                return 5101012; //Bucc - Tornado Uppercut

            case 5121020: //Bucc - Octopunch (Max Charge)
                return 5121007; //Bucc - Octopunch

            case 5111013: //Bucc - Hedgehog Buster
                return 5111002; //Bucc - Energy Burst

            case 5111015: //Bucc - Static Thumper
                return 5111012; //Bucc - Static Thumper

            case 31011004: //DA - Exceed Double Slash 2
            case 31011005: //DA - Exceed Double Slash 3
            case 31011006: //DA - Exceed Double Slash 4
            case 31011007: //DA - Exceed Double Slash Purple
                return 31011000; //DA - Exceed Double Slash 1

            case 31201007: //DA - Exceed Demon Strike 2
            case 31201008: //DA - Exceed Demon Strike 3
            case 31201009: //DA - Exceed Demon Strike 4
            case 31201010: //DA - Exceed Demon Strike Purple
                return 31201000; //DA - Exceed Demon Strike 1

            case 31211007: //DA - Exceed Lunar Slash 2
            case 31211008: //DA - Exceed Lunar Slash 3
            case 31211009: //DA - Exceed Lunar Slash 4
            case 31211010: //DA - Exceed Lunar Slash Purple
                return 31211000; //DA - Exceed Lunar Slash 1

            case 31221009: //DA - Exceed Execution 2
            case 31221010: //DA - Exceed Execution 3
            case 31221011: //DA - Exceed Execution 4
            case 31221012: //DA - Exceed Execution Purple
                return 31221000; //DA - Exceed Execution 1

            case 31211002: //DA - Shield Charge (Spikes)
                return 31211011; //DA - Shield Charge (Rush)

            case 61120219: //Kaiser - Dragon Slash (Final Form)
                return 61001000; //Kaiser - Dragon Slash 1

            case 61111215: //Kaiser - Flame Surge (Final Form)
                return 61001101; //Kaiser - Flame Surge

            case 61111216: //Kaiser - Impact Wave (Final Form)
                return 61101100; //Kaiser - Impact Wave

            case 61111217: //Kaiser - Piercing Blaze (Final Form)
                return 61101101; //Kaiser - Piercing Blaze

            case 61111111: //Kaiser - Wing Beat (Final Form)
                return 61111100; //Kaiser - Wing Beat

            case 61111219: //Kaiser - Pressure Chain (Final Form)
                return 61111101; //Kaiser - Pressure Chain

            case 61121201: //Kaiser - Gigas Wave (Final Form)
                return 61121100; //Kaiser - Gigas Wave

            case 61121222: //Kaiser - Inferno Breath (Final Form)
                return 61121105; //Kaiser - Inferno Breath

            case 61121203: //Kaiser - Dragon Barrage (Final Form)
                return 61121102; //Kaiser - Dragon Barrage

            case 61121221: //Kaiser - Blade Burst (Final Form)
                return 61121104; //Kaiser - Blade Burst

            case 14101021: //NW - Quint. Throw Finisher
                return 14101020; //NW - Quint. Throw

            case 14111021: //NW - Quad Throw Finisher
                return 14111020; //NW - Quad Throw

            case 14121002: //NW - Triple Throw Finisher
                return 14121001; //NW - Triple Throw

            case Mercedes.STAGGERING_STRIKES:
                return Mercedes.STUNNING_STRIKES;

            case Aran.SMASH_WAVE_COMBO:
                return Aran.SMASH_WAVE;

            case Aran.FINAL_BLOW_COMBO:
            case Aran.FINAL_BLOW_SMASH_SWING_COMBO:
                return Aran.FINAL_BLOW;

            case AngelicBuster.SOUL_SEEKER_ATOM:
                return AngelicBuster.SOUL_SEEKER;

            case 65101006: //AB - Lovely Sting Explosion
                return AngelicBuster.LOVELY_STING;

            case 65121007:
            case 65121008:
                return AngelicBuster.TRINITY;

            case Blaster.REVOLVING_CANNON_2:
            case Blaster.REVOLVING_CANNON_3:
                return Blaster.REVOLVING_CANNON;

            case Kinesis.PSYCHIC_ASSAULT_DOWN:
                return Kinesis.PSYCHIC_ASSAULT_FWD;
            case Kinesis.PSYCHIC_BLAST_DOWN:
                return Kinesis.PSYCHIC_BLAST_FWD;

            case Shade.GROUND_POUND_SECOND:
                return Shade.GROUND_POUND_FIRST;
            case 25120001:
            case 25120002:
            case Shade.BOMB_PUNCH_FINAL:
                return Shade.BOMB_PUNCH;
            default:
                return skillID;
        }
    }

    public static int getKaiserGaugeIncrementBySkill(int skillID) {
        HashMap<Integer, Integer> hashMapIncrement = new HashMap<>();
        hashMapIncrement.put(DRAGON_SLASH_1, 1);
        hashMapIncrement.put(DRAGON_SLASH_2, 3);
        hashMapIncrement.put(DRAGON_SLASH_3, 4);
        hashMapIncrement.put(DRAGON_SLASH_1_FINAL_FORM, 1);

        hashMapIncrement.put(FLAME_SURGE, 2);
        hashMapIncrement.put(FLAME_SURGE_FINAL_FORM, 2);

        hashMapIncrement.put(IMPACT_WAVE, 5);
        hashMapIncrement.put(IMPACT_WAVE_FINAL_FORM, 0);

        hashMapIncrement.put(PIERCING_BLAZE, 5);
        hashMapIncrement.put(PIERCING_BLAZE_FINAL_FORM, 0);

        hashMapIncrement.put(WING_BEAT, 2);
        hashMapIncrement.put(WING_BEAT_FINAL_FORM, 1);

        hashMapIncrement.put(PRESSURE_CHAIN, 8);
        hashMapIncrement.put(PRESSURE_CHAIN_FINAL_FORM, 0);

        hashMapIncrement.put(GIGA_WAVE, 8);
        hashMapIncrement.put(GIGA_WAVE_FINAL_FORM, 0);

        hashMapIncrement.put(INFERNO_BREATH, 14);
        hashMapIncrement.put(INFERNO_BREATH_FINAL_FORM, 0);

        hashMapIncrement.put(DRAGON_BARRAGE, 6);
        hashMapIncrement.put(DRAGON_BARRAGE_FINAL_FORM, 0);

        hashMapIncrement.put(BLADE_BURST, 6);
        hashMapIncrement.put(BLADE_BURST_FINAL_FORM, 0);

        hashMapIncrement.put(TEMPEST_BLADES_FIVE, 15);
        hashMapIncrement.put(TEMPEST_BLADES_FIVE_FF, 0);

        hashMapIncrement.put(TEMPEST_BLADES_THREE, 15);
        hashMapIncrement.put(TEMPEST_BLADES_THREE_FF, 0);

        return hashMapIncrement.getOrDefault(skillID, 0);
    }

    public static boolean isEvanFusionSkill(int skillID) {
        switch (skillID) {
            case 22110014:
            case 22110025:
            case 22140014:
            case 22140015:
            case 22140024:
            case 22140023:
            case 22170065:
            case 22170066:
            case 22170067:
            case 22170094:
                return true;
            default:
                return false;
        }
    }

    public static boolean isShikigamiHauntingSkill(int skillID) {
        switch(skillID) {
            case 80001850:
            case 42001000:
            case 42001005:
            case 42001006:
            case 40021185:
            case 80011067:
                return true;
            default:
                return false;
        }
    }

    public static boolean isStealableSkill(int skillID) {
        // TODO
        return false;
    }

    public static int getStealSkillManagerTabFromSkill(int skillID) {
        int smJobID;

        //Hyper Skills
        if(skillID % 100 == 54) {
            return 5;
        }
        switch (skillID / 10000) {

            // 1st Job Tab
            case 100:
            case 200:
            case 300:
            case 400:
            case 430:
            case 500:
            case 501:
                return 1;

            // 2nd Job Tab
            case 110:
            case 120:
            case 130:

            case 210:
            case 220:
            case 230:


            case 310:
            case 320:

            case 410:
            case 420:
            case 431:
            case 432:

            case 510:
            case 520:
            case 530:
                return 2;

            // 3rd Job Tab
            case 111:
            case 121:
            case 131:

            case 211:
            case 221:
            case 231:

            case 311:
            case 321:

            case 411:
            case 421:
            case 433:

            case 511:
            case 521:
            case 531:
                return 3;

            // 4th job Tab
            case 112:
            case 122:
            case 132:

            case 212:
            case 222:
            case 232:

            case 312:
            case 322:

            case 412:
            case 422:
            case 434:

            case 512:
            case 522:
            case 532:
                return 4;
        }
        return -1;
    }

    public static int getMaxPosBysmJobID(int smJobID) {
        int maxPos = 0;
        switch (smJobID) {
            case 1:
            case 2:
                maxPos = 3;
                break;
            case 3:
                maxPos = 2;
                break;
            case 4:
            case 5:
                maxPos = 1;
                break;
        }
        return maxPos;
    }

    public static int getStartPosBysmJobID(int smJobID) {
        int startPos = 0;
        switch (smJobID) {
            case 1:
                startPos = 0;
                break;
            case 2:
                startPos = 4;
                break;
            case 3:
                startPos = 8;
                break;
            case 4:
                startPos = 11;
                break;
            case 5:
                startPos = 13;
                break;
        }
        return startPos;
    }

    public static int getImpecSkillIDBysmJobID(int smJobID) {
        int impecSkillID = 0;
        switch (smJobID) {
            case 1:
                impecSkillID = 24001001;
                break;
            case 2:
                impecSkillID = 24101001;
                break;
            case 3:
                impecSkillID = 24111001;
                break;
            case 4:
                impecSkillID = 24121001;
                break;
            case 5:
                impecSkillID = 24121054;
                break;
        }
        return impecSkillID;
    }

    public static int getSMJobIdByImpecSkillId(int impecSkillId) {
        switch (impecSkillId) {
            case 24001001:  // 1st Job
                return 1;
            case 24101001:  // 2nd Job
                return 2;
            case 24111001:  // 3rd job
                return 3;
            case 24121001:  // 4th Job
                return 4;
            case 24121054:  // Hyper Skill
                return 5;
        }
        return -1;
    }

    public static boolean isIceSkill(int skillID) {
        switch (skillID) {
            case IceLightning.CHILLING_STEP:
            case IceLightning.COLD_BEAM:
            case IceLightning.ICE_STRIKE:
            case IceLightning.GLACIER_CHAIN:
            case IceLightning.FREEZING_BREATH:
            case IceLightning.BLIZZARD:
            case IceLightning.FROZEN_ORB:
            case IceLightning.ELQUINES:
                return true;

            default:
                return false;
        }
    }

    public static int getLinkSkillByJob(short job) {
        if (JobConstants.isCannonShooter(job)) { // Pirate Blessing
            return 80000000;
        } else if (JobConstants.isCygnusKnight(job)) { // Cygnus Blessing
            return 80000070;
        }  else if (JobConstants.isAran(job)) { // Combo Kill Blessing
            return 80000370;
        } else if (JobConstants.isEvan(job)) { // Rune Persistence
            return 80000369;
        } else if (JobConstants.isMercedes(job)) { // Elven Blessing
            return 80001040;
        } else if (JobConstants.isDemonSlayer(job)) { // Fury Unleashed
            return 80000001;
        } else if (JobConstants.isDemonAvenger(job)) { // Wild Rage
            return 80000050;
        } else if (JobConstants.isJett(job)) { // Core Aura
            return 80001151;
        } else if (JobConstants.isPhantom(job)) { // Phantom Instinct
            return 80000002;
        } else if (JobConstants.isMihile(job)) { // Knight's Watch
            return 80001140;
        } else if (JobConstants.isLuminous(job)) { // Light Wash
            return 80000005;
        } else if (JobConstants.isAngelicBuster(job)) { // Terms and Conditions
            return 80001155;
        } else if (JobConstants.isHayato(job)) { // Keen Edge
            return 80000003;
        } else if (JobConstants.isKanna(job)) { // Elementalism
            return 80000004;
        } else if (JobConstants.isKaiser(job)) { // Iron Will
            return 80000006;
        } else if (JobConstants.isXenon(job)) { // Hybrid Logic
            return 80000047;
        } else if (JobConstants.isZero(job)) { // Rhinne's Blessing
            return 80000110;
        } else if (JobConstants.isKinesis(job)) { // Judgment
            return 80000188;
        } else if (JobConstants.isBeastTamer(job)) { // Focus Spirit
            return 80010006;
        }
        return 0;
    }

    public static int getLinkSkillLevelByCharLevel(short level) {
        int res = 0;
        if (level >= LINK_SKILL_3_LEVEL) {
            res = 3;
        } else if (level >= LINK_SKILL_2_LEVEL) {
            res = 2;
        } else if (level >= LINK_SKILL_1_LEVEL) {
            res = 1;
        }
        return res;
    }

    public static int getLinkedSkill(int skillID) {
        switch(skillID) {
            case Zero.STORM_BREAK_INIT:
                return Zero.STORM_BREAK;
            case Zero.ADV_STORM_BREAK_SHOCK_INIT:
        return Zero.ADV_STORM_BREAK;
    }
        return skillID;
}

    public static boolean isPassiveSkill(int skillId) {
        if (isSkipPassiveSkill(skillId)) {
            return false;
        }
        if (isPassiveStatSkill(skillId)) {
            return true;
        }
        if (SkillConstants.isBlessingSkill(skillId)) {
            // special handling for blessing skills in Char::initBlessingSkills
            return false;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        return si != null && si.isPsd() && (si.getPsdSkills().isEmpty() || si.getSkillStatInfo().containsKey(SkillStat.coolTimeR));
    }

    private static boolean isPassiveStatSkill(int skillId) {
        // overrides
        switch (skillId) {
            case Hero.ADVANCED_COMBO:
            case Paladin.WEAPON_MASTERY_PAGE:
            case Paladin.ACHILLES:
            case NightLord.CLAW_MASTERY:
            case FirePoison.SPELL_MASTERY_FP:
            case FirePoison.IFRIT:
            case IceLightning.SPELL_MASTERY_IL:
            case IceLightning.ELQUINES:
            case Bishop.SPELL_MASTERY_BISH:
            case Bishop.HOLY_SYMBOL_PREPARATION:
            case Bishop.RIGHTEOUSLY_INDIGNANT:
            case Archer.CRITICAL_SHOT:
            case Bowmaster.BOW_MASTERY:
            case Marksman.CROSSBOW_MASTERY:
            case Shadower.DAGGER_MASTERY:
            case DualBlade.KATARA_MASTERY:
            case Buccaneer.KNUCKLE_MASTERY:
            case Corsair.GUN_MASTERY:
            case Jett.GUN_MASTERY:
            case DawnWarrior.SWORD_MASTERY:
            case WindArcher.BOW_MASTERY:
            case Mihile.SOUL_ASYLUM:
            case Evan.MAGIC_AMPLIFICATION:
            case Evan.DRAGON_FURY:
            case Luminous.SPELL_MASTERY:
            case DemonSlayer.OBSIDIAN_SKIN:
            case DemonAvenger.DEFENSE_EXPERTISE:
            case Mechanic.EXTREME_MECH:
            case Mechanic.ROBOT_MASTERY:
                return true;
            default:
                return false;
        }
    }

    private static boolean isSkipPassiveSkill(int skillId) {
        // for passive skills handled elsewhere
        switch (skillId) {
            case Paladin.SHIELD_MASTERY:
            case Shadower.SHIELD_MASTERY:
            case DualBlade.SHADOW_MELD:
            case ThunderBreaker.PRIMAL_BOLT:
            case WildHunter.FELINE_BERSERK_VITALITY:
            case WildHunter.SILENT_RAMPAGE:
            case Mechanic.MECHANIZED_DEFENSE_SYSTEM:
            case Mechanic.ENHANCED_SUPPORT_UNIT:
            case Xenon.SUPPLY_SURPLUS:
                return true;
        }
        return false;
    }

    public static boolean isPsdWTSkill(int skillId) {
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        return (si != null && !si.getPsdWT().isEmpty());
    }

    public static boolean isConditionalPassiveSkill(int skillId) {
        switch (skillId) {
            case Paladin.SHIELD_MASTERY:    // shield mastery, mastery stat is additive
            case Shadower.SHIELD_MASTERY:
            case Xenon.MULTILATERAL_I:      // passive stat depending on base stat (allocated by sp)
            case Xenon.MULTILATERAL_II:
            case Xenon.MULTILATERAL_III:
            case Xenon.MULTILATERAL_IV:
            case Xenon.MULTILATERAL_V:
            case Xenon.MULTILATERAL_VI:
                return true;
            default:
                return false;
        }
    }

    public static boolean isHyperstatSkill(int skillID) {
        return skillID >= 80000400 && skillID <= 80000418;
    }

    public static int getTotalHyperStatSpByLevel(short currentlevel) {
        int sp = 0;
        for (short level = 140; level <= currentlevel; level++) {
            sp += getHyperStatSpByLv(level);
        }
        return sp;
    }

    public static int getHyperStatSpByLv(short level) {
        return 3 + ((level - 140) / 10);
    }

    public static int getNeededSpForHyperStatSkill(int lv) {
        switch (lv) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 8;
            case 5:
                return 10;
            case 6:
                return 15;
            case 7:
                return 20;
            case 8:
                return 25;
            case 9:
                return 30;
            case 10:
                return 35;
            default:
                return 0;
        }
    }

    public static int getTotalNeededSpForHyperStatSkill(int lv) {
        switch (lv) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 7;
            case 4:
                return 15;
            case 5:
                return 25;
            case 6:
                return 40;
            case 7:
                return 60;
            case 8:
                return 85;
            case 9:
                return 115;
            case 10:
                return 150;
            default:
                return 0;
        }
    }

    public static boolean isUnregisteredSkill(int skillID) {
        int prefix = skillID / 10000;
        if (prefix == 8000) {
            prefix = skillID / 100;
        }
        return prefix != 9500 && skillID / 10000000 == 9;
    }

    public static boolean isHomeTeleportSkill(int skillId) {
        switch (skillId) {
            case Beginner.MAPLE_RETURN:
            // case DualBlade.RETURN:
            case Jett.RETURN_TO_SPACESHIP:
            case Noblesse.IMPERIAL_RECALL:
            case Mihile.IMPERIAL_RECALL:
            case Aran.RETURN_TO_RIEN:
            case Evan.BACK_TO_NATURE:
                // Mercedes
            case Phantom.TO_THE_SKIES:
                // Shade
                // Luminous
            case Citizen.SECRET_ASSEMBLY:
            case Xenon.PROMESSA_ESCAPE:
                // Demon
                // Kaiser
            case AngelicBuster.DAY_DREAMER:
                // Hayato
                // Kanna
            case Zero.TEMPLE_RECALL:
            case BeastTamer.HOMEWARD_BOUND:
            case Kinesis.RETURN_KINESIS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isArmorPiercingSkill(int skillId) {
        switch (skillId) {
            case 95001000: // Arrow Blaster
            case Bowmaster.FINAL_ATTACK_BOW:
            case Bowmaster.ADVANCED_FINAL_ATTACK_BOW:
            case Bowmaster.QUIVER_CARTRIDGE_ATOM:
            case Bowmaster.ENCHANTED_QUIVER_ATOM:
                return false;
            default:
                return true;
        }
    }

    public static int getBaseSpByLevel(short level) {
        return level > 140 ? 0
                : level > 130 ? 6
                : level > 120 ? 5
                : level > 110 ? 4
                : 3;
    }

    public static int getTotalPassiveHyperSpByLevel(short level) {
        return level < 140 ? 0 : (level - 130) / 10;
    }

    public static int getTotalActiveHyperSpByLevel(short level) {
        return level < 140 ? 0 : level < 170 ? 1 : level < 200 ? 2 : 3;
    }

    public static boolean isGuildSkill(int skillID) {
        int prefix = skillID / 10000;
        if (prefix == 8000) {
            prefix = skillID / 100;
        }
        return prefix == 9100;
    }

    public static boolean isGuildContentSkill(int skillID) {
        return (skillID >= 91000007 && skillID <= 91000015) || (skillID >= 91001016 && skillID <= 91001021);
    }

    public static boolean isGuildNoblesseSkill(int skillID) {
        return skillID >= 91001022 && skillID <= 91001024;
    }

    public static boolean isMultiAttackCooldownSkill(int skillID) {
        switch (skillID) {
            case 5311010: // Cannoneer.MONKEY_FURY\
            case WildHunter.HUNTING_ASSISTANT_UNIT:
            case DemonSlayer.DARK_METAMORPHOSIS:
            case DemonAvenger.VITALITY_VEIL:
            case DemonAvenger.NETHER_SHIELD:
            case Jett.FALLING_STARS:
            case Evan.DRAGON_BREATH:
            case Evan.DRAGON_DIVE:
            case DualBlade.ASURAS_ANGER:
            case IceLightning.THUNDER_STORM:
            case IceLightning.FROZEN_ORB:
            case Bowmaster.ARROW_PLATTER:
            case Shadower.SHADOW_VEIL:
            case Mechanic.DISTORTION_BOMB:
            case AngelicBuster.SUPREME_SUPERNOVA:
            case Blaster.BALLISTIC_HURRICANE:
            case Blaster.BALLISTIC_HURRICANE_1:
                return true;
        }
        return false;
    }

    public static boolean isUserCloneSummon(int skillID) {
        switch (skillID) {
            case 14111024:
            case 14121054:
            case 14121055:
            case 14121056:
            case 131001017:
            case 131003017:
            case 400011005:
            case 400031007:
            case 400031008:
            case 400031009:
            case 400041028:
                return true;
        }
        return false;
    }

    public static boolean isSummonJaguarSkill(int skillID) {
        return skillID >= 33001007 && skillID <= 33001015;
    }

    public static boolean isShadowAssaultSkill(int skillID) {
        return skillID >= 400041002 && skillID <= 400041005;
    }

    public static boolean isSomeFifthSkillForRemote(int skillId) {
        // sub_12237C0
        switch (skillId) {
            case 400051003:
            case 400051008:
            case 400051016:
            case 400041034:
            case 400041020:
            case 400041016:
            case 400021078:
            case 400021080:
            case 400021009:
            case 400021010:
            case 400021011:
            case 400021028:
            case 400021047:
            case 400021048:
            case 400011004:
            case 152121004:
            case 152001002:
            case 152120003:
                return true;
        }
        return false;
    }

    public static boolean isMatching(int rootId, int job) {
        boolean matchingStart = job / 100 == rootId / 100;
        boolean matching = matchingStart;
        if (matchingStart && rootId % 100 != 0) {
            // job path must match
            matching = (rootId % 100) / 10 == (job % 100) / 10;
        }
        return matching;
    }

    // is_skill_from_item(signed int nSkillID)
    public static boolean isSkillFromItem(int skillID) {
        switch (skillID) {
            case 80011123: // New Destiny
            case 80011247: // Dawn Shield
            case 80011248: // Dawn Shield
            case 80011249: // Divine Guardian
            case 80011250: // Divine Shield
            case 80011251: // Divine Brilliance
            case 80011261: // Monolith
            case 80011295: // Scouter
            case 80011346: // Ribbit Ring
            case 80011347: // Krrr Ring
            case 80011348: // Rawr Ring
            case 80011349: // Pew Pew Ring
            case 80011475: // Elunarium Power (ATT & M. ATT)
            case 80011476: // Elunarium Power (Skill EXP)
            case 80011477: // Elunarium Power (Boss Damage)
            case 80011478: // Elunarium Power (Ignore Enemy DEF)
            case 80011479: // Elunarium Power (Crit Rate)
            case 80011480: // Elunarium Power (Crit Damage)
            case 80011481: // Elunarium Power (Status Resistance)
            case 80011482: // Elunarium Power (All Stats)
            case 80011492: // Firestarter Ring
            case 80001768: // Rope Lift
            case 80001705: // Rope Lift
            case 80001941: // Scouter
            case 80010040: // Altered Fate
                return true;
        }
        // Tower of Oz skill rings
        return (skillID >= 80001455 && skillID <= 80001479);
    }

    public static int getHyperPassiveSkillSpByLv(int level) {
        // 1 sp per 10 levels, starting at 140, ending at 220
        return level >= 140 && level <= 220 && level % 10 == 0 ? 1 : 0;
    }

    public static int getHyperActiveSkillSpByLv(int level) {
        return level == 150 || level == 170 || level == 200 ? 1 : 0;
    }

    public static int getNoviceSkillRoot(short job) {
        if (job / 100 == 22 || job == 2001) {
            return JobConstants.JobEnum.EVAN.getJobId();
        }
        if (job / 100 == 23 || job == 2002) {
            return JobConstants.JobEnum.MERCEDES.getJobId();
        }
        if (job / 100 == 24 || job == 2003) {
            return JobConstants.JobEnum.PHANTOM.getJobId();
        }
        if (JobConstants.isDemon(job)) {
            return JobConstants.JobEnum.DEMON.getJobId();
        }
        if (JobConstants.isMihile(job)) {
            return JobConstants.JobEnum.NAMELESS_WARDEN.getJobId();
        }
        if (JobConstants.isLuminous(job)) {
            return JobConstants.JobEnum.LUMINOUS.getJobId();
        }
        if (JobConstants.isAngelicBuster(job)) {
            return JobConstants.JobEnum.ANGELIC_BUSTER.getJobId();
        }
        if (JobConstants.isXenon(job)) {
            return JobConstants.JobEnum.XENON.getJobId();
        }
        if (JobConstants.isShade(job)) {
            return JobConstants.JobEnum.SHADE.getJobId();
        }
        if (JobConstants.isKinesis(job)) {
            return JobConstants.JobEnum.KINESIS.getJobId();
        }
        if (JobConstants.isBlaster(job)) {
            return JobConstants.JobEnum.CITIZEN.getJobId();
        }
        if (JobConstants.isHayato(job)) {
            return JobConstants.JobEnum.HAYATO.getJobId();
        }
        if (JobConstants.isKanna(job)) {
            return JobConstants.JobEnum.KANNA.getJobId();
        }
        return 1000 * (job / 1000);
    }

    public static int getNoviceSkillFromRace(int skillID) {
        if (skillID == 50001215 || skillID == 10001215) {
            return 1005;
        }
        if (isCommonSkill(skillID) || (skillID >= 110001500 && skillID <= 110001504)) {
            return skillID;
        }
        if (isNoviceSkill(skillID)) {
            return skillID % 10000;
        }
        return 0;
    }

    public static int getBuffSkillItem(int buffSkillID) {
        int novice = getNoviceSkillFromRace(buffSkillID);
        switch (novice) {
            // Angelic Blessing
            case 86:
                return 2022746;
            // Dark Angelic Blessing
            case 88:
                return 2022747;
            // Angelic Blessing
            case 91:
                return 2022764;
            // White Angelic Blessing
            case 180:
                return 2022823;
            // Lightning God's Blessing
            case 80000086:
                return 2023189;
            // White Angelic Blessing
            case 80000155:
                return 2022823;
            // Lightning God's Blessing
            case 80010065:
                return 2023189;
            // Goddess' Guard
            case 80011150:
                return 1112932;
        }
        return 0;
    }

    public static String getMakingSkillName(int skillID) {
        switch (skillID) {
            case 92000000:
                return "Herbalism";
            case 92010000:
                return "Mining";
            case 92020000:
                return "Smithing";
            case 92030000:
                return "Accessory Crafting";
            case 92040000:
                return "Alchemy";
        }
        return null;
    }

    public static int recipeCodeToMakingSkillCode(int skillID) {
        return 10000 * (skillID / 10000);
    }

    public static int getNeededProficiency(int level) {
        if (level <= 0 || level >= MAKING_SKILL_EXPERT_LEVEL) {
            return 0;
        }
        return ((100 * level * level) + (level * 400)) / 2;
    }

    public static boolean isSynthesizeRecipe(int recipeID) {
        return isMakingSkillRecipe(recipeID) && recipeID % 10000 == 9001;
    }

    public static boolean isDecompositionRecipeScroll(int recipeID) {
        return isMakingSkillRecipe(recipeID)
                && recipeCodeToMakingSkillCode(recipeID) == 92040000
                && recipeID - 92040000 >= 9003
                && recipeID - 92040000 <= 9006;
    }

    public static boolean isDecompositionRecipeCube(int recipeID) {
        return isMakingSkillRecipe(recipeID) && recipeCodeToMakingSkillCode(recipeID) == 92040000 && recipeID == 92049002;
    }

    public static boolean isDecompositionRecipe(int recipeID) {
        if (isMakingSkillRecipe(recipeID) && recipeCodeToMakingSkillCode(recipeID) == 92040000 && recipeID == 92049000
         || isDecompositionRecipeScroll(recipeID)
         || isDecompositionRecipeCube(recipeID)) {
            return true;
        }
        return false;
    }

    public static int getFairyBlessingByJob(short job) {
        short beginJob = JobConstants.JobEnum.getJobById(job).getBeginnerJobId();
        // xxxx0012, where xxxx is the "0th" job
        return beginJob * 10000 + 12;
    }

    public static int getEmpressBlessingByJob(short job) {
        short beginJob = JobConstants.JobEnum.getJobById(job).getBeginnerJobId();
        // xxxx0073, where xxxx is the "0th" job
        return beginJob * 10000 + 73;
    }

    public static boolean isBlessingSkill(int skillId) {
        return JobConstants.isBeginnerJob((short) (skillId / 10000)) && skillId % 100 == 12 || skillId % 100 == 73;
    }


    public static int getSoaringByJob(short job) {
        short beginJob = JobConstants.JobEnum.getJobById(job).getBeginnerJobId();
        // xxxx1026, where xxxx is the "0th" job
        return beginJob * 10000 + 1026;
    }

    public static boolean isBeginnerSpAddableSkill(int skillID) {
        return skillID == 1000 || skillID == 1001 || skillID == 1002 || skillID == 140000291 || skillID == 30001000
                || skillID == 30001001 || skillID == 30001002;
    }

    public static boolean isKinesisPsychicAreaSkill(int skillId){
        switch (skillId) {
            case 142001002:
            case 142120003:
            case 142101009:
            case 142111006:
            case 142111007:
            case 142121005:
            case 142121030:
                return true;
        }
        return false;
    }

    public static boolean isNoConsumeBullet(int skillID) {
        switch (skillID) {
            case NightWalker.DARK_OMEN:
            case NightWalker.DOMINION:
            case NightWalker.SHADOW_BAT_ATTACK:
            case NightWalker.SHADOW_BAT_ATTACK_BOUND:
                return true;
            default:
                return false;
        }
    }

    public static boolean isNoConsumeBullet2(int skillID) {
        // 0x017443D0
        return isNoConsumeBullet(skillID) || skillID == NightWalker.SHADOW_STITCH ||
                skillID == NightWalker.SHADOW_SPARK || skillID == NightWalker.SHADOW_SPARK_EXPLOSION;
    }

    public static boolean isThrowingStarSkill(int skillID) {
        // used for Night Walker Shadow Bat Creation
        switch (skillID) {
            case NightWalker.LUCKY_SEVEN:
            case NightWalker.TRIPLE_THROW:
            case NightWalker.TRIPLE_THROW_FINISHER:
            case NightWalker.QUAD_STAR:
            case NightWalker.QUAD_STAR_FINISHER:
            case NightWalker.QUINTUPLE_STAR:
            case NightWalker.QUINTUPLE_STAR_FINISHER:
            case NightWalker.SHADOW_SPARK:
                return true;
            default:
                return false;
        }
    }

    public static BeastTamerBeasts getBeastFromSkill(int skillId) {
        switch (skillId / 10000) {
            case 11200:
                return BeastTamerBeasts.Bear;
            case 11210:
                return BeastTamerBeasts.Leopard;
            case 11211:
                return BeastTamerBeasts.Bird;
            case 11212:
                return BeastTamerBeasts.Cat;
            default:
                return BeastTamerBeasts.None;
        }
    }

    public static boolean isKeydownCDSkill(int skillId) {
        return KEYDOWN_SKILLS.contains(skillId);
    }

    public static boolean isMassSpellEncodePosition(int skillId) {
        // Maple Warrior skills seem to encode position too but i am not going to test each one
        switch (skillId) {
            case Hero.RAGE:
            case DarkKnight.HYPER_BODY:
            case FirePoison.MEDITATION_FP:
            case IceLightning.MEDITATION_IL:
            case Bishop.BLESS:
            case Bishop.ADV_BLESSING:
            case Bishop.HOLY_SYMBOL:
            case Bowmaster.SHARP_EYES_BOW:
            case Marksman.SHARP_EYES_XBOW:
            case WindArcher.SHARP_EYES:
            // WildHunter.SHARP_EYES: -> does not encode position f3
            case Buccaneer.SPEED_INFUSION:
            case ThunderBreaker.SPEED_INFUSION:
            case Aran.MAHA_BLESSING:
            case Mihile.RALLY:
            case Luminous.PHOTIC_MEDITATION:
                return true;
            default:
                return false;
        }
    }

    public static void fixBaseStatsBySkill(Map<BaseStat, Integer> stats, SkillInfo si, int slv) {
        int skillId = si.getSkillId();
        if (SkillConstants.isBlessingSkill(skillId)) {
            stats.put(BaseStat.pad, si.getValue(SkillStat.x, slv));
            stats.put(BaseStat.mad, si.getValue(SkillStat.y, slv));
            stats.put(BaseStat.acc, si.getValue(SkillStat.z, slv));
            stats.put(BaseStat.eva, si.getValue(SkillStat.z, slv));
        }
        switch (skillId) {
            // common
            case Archer.CRITICAL_SHOT:
            case NightLord.CRITICAL_THROW:
            case DualBlade.SHARPNESS:
            case NightWalker.CRITICAL_THROW:
            case Evan.CRITICAL_MAGIC:
                stats.put(BaseStat.cr, si.getValue(SkillStat.prop, slv));
                break;
            case Bowmaster.BOW_EXPERT:
            case Marksman.CROSSBOW_EXPERT:
            case NightLord.CLAW_EXPERT:
            case Shadower.DAGGER_EXPERT:
            case DualBlade.KATARA_EXPERT:
            case DawnWarrior.STUDENT_OF_THE_BLADE:
            case WindArcher.BOW_EXPERT:
            case NightWalker.THROWING_EXPERT:
            case ThunderBreaker.KNUCKLE_EXPERT:
            case Aran.HIGH_MASTERY:
            case Mercedes.DUAL_BOWGUNS_EXPERT:
            case WildHunter.CROSSBOW_EXPERT:
                stats.put(BaseStat.pad, si.getValue(SkillStat.x, slv));
                break;
            case FirePoison.SPELL_MASTERY_FP:
            case IceLightning.SPELL_MASTERY_IL:
            case Bishop.SPELL_MASTERY_BISH:
            case BlazeWizard.SPELL_CONTROL:
            case BlazeWizard.PURE_MAGIC:
            case Evan.SPELL_MASTERY:
            case Evan.MAGIC_MASTERY:
            case Luminous.SPELL_MASTERY:
            case Luminous.MAGIC_MASTERY:
            case BattleMage.STAFF_MASTERY:
            case BattleMage.STAFF_EXPERT:
                stats.put(BaseStat.mad, si.getValue(SkillStat.x, slv));
                break;
            case Corsair.GUN_MASTERY:
            case Jett.GUN_MASTERY:
            case DawnWarrior.SWORD_MASTERY:
            case WindArcher.BOW_MASTERY:
            case ThunderBreaker.KNUCKLE_MASTERY:
            case Aran.POLEARM_MASTERY:
            case Mercedes.DUAL_BOWGUNS_MASTERY:
            case Shade.KNUCKLE_MASTERY:
            case DemonSlayer.WEAPON_MASTERY:
            case Mechanic.MECHANIC_MASTERY:
                stats.put(BaseStat.acc, si.getValue(SkillStat.x, slv));
                break;
            case Mihile.SOUL_ASYLUM:
            case Aran.HIGH_DEFENSE:
            case DemonSlayer.OBSIDIAN_SKIN:
                stats.put(BaseStat.dmgReduce, si.getValue(SkillStat.t, slv));
                break;
            // class specific
            case Hero.ADVANCED_COMBO:
                stats.remove(BaseStat.damR);
                break;
            case Paladin.ACHILLES:
                stats.put(BaseStat.dmgReduce, si.getValue(SkillStat.y, slv));
                break;
            case DarkKnight.FINAL_PACT_INFO:
                stats.remove(BaseStat.damR); // damR handled as a buff
                break;
            case DarkKnight.FINAL_PACT_COOLDOWN:
                stats.remove(BaseStat.crDmg); // ??
                break;
            case DarkKnight.SACRIFICE:
                stats.remove(BaseStat.bd);
                break;
            case Bishop.RIGHTEOUSLY_INDIGNANT:
                stats.clear(); // only want passive damR effect
                stats.put(BaseStat.damR, si.getValue(SkillStat.z, slv));
                break;
            case Bowmaster.ILLUSION_STEP_BOW:
            case Marksman.ILLUSION_STEP_XBOW:
                stats.remove(BaseStat.dex); // active effect
                break;
            case Thief.HASTE:
            case NightWalker.HASTE:
                stats.remove(BaseStat.speed); // active effect
                stats.remove(BaseStat.jump);
                break;
            case Buccaneer.CROSSBONES:
                stats.remove(BaseStat.padR); // active effect
                break;
            case Buccaneer.STIMULATING_CONVERSATION:
                stats.remove(BaseStat.damR); // active effect
                break;
            case Jett.BOUNTY_CHASER:
                stats.clear();
                stats.put(BaseStat.str, si.getValue(SkillStat.strX, slv));
                stats.put(BaseStat.dex, si.getValue(SkillStat.dexX, slv));
                break;
            case Jett.HIGH_GRAVITY:
                stats.clear();
                stats.put(BaseStat.bd, si.getValue(SkillStat.bdR, slv));
                break;
            case DawnWarrior.WILL_OF_STEEL:
                stats.put(BaseStat.dmgReduce, si.getValue(SkillStat.x, slv));
                break;
            case BlazeWizard.FIRE_REPULSION:
                stats.put(BaseStat.magicGuard, si.getValue(SkillStat.x, slv));
                break;
            case WindArcher.FEATHERWEIGHT:
                stats.put(BaseStat.dmgReduce, si.getValue(SkillStat.x, slv));
            case WindArcher.SECOND_WIND:
                stats.remove(BaseStat.pad); // active effect
                break;
            case Mihile.RALLY:
                stats.clear(); // active effect
                stats.put(BaseStat.pad, si.getValue(SkillStat.padX, slv));
                break;
            case Evan.PARTNERS:
                stats.remove(BaseStat.damR);
                break;
            case Evan.MAGIC_AMPLIFICATION:
                stats.put(BaseStat.fd, si.getValue(SkillStat.z, slv));
                break;
            case Evan.DRAGON_FURY:
                stats.put(BaseStat.madR, si.getValue(SkillStat.damage, slv));
                break;
            case Luminous.STANDARD_MAGIC_GUARD:
                stats.put(BaseStat.magicGuard, si.getValue(SkillStat.x, slv));
                break;
            case BattleMage.HASTY_AURA: // overwrite active stats for BaM auras
                stats.put(BaseStat.speed, si.getValue(SkillStat.psdSpeed, slv));
                stats.put(BaseStat.booster, si.getValue(SkillStat.actionSpeed, slv));
                break;
            case BattleMage.BLUE_AURA:
                stats.put(BaseStat.asr, si.getValue(SkillStat.asrR, slv));
                break;
            case BattleMage.DARK_AURA:
                stats.remove(BaseStat.damR);
                break;
            case Mechanic.SUPPORT_UNIT_HEX:
                stats.clear();
                stats.put(BaseStat.ter, si.getValue(SkillStat.terR, slv));
                break;
            case Mechanic.EXTREME_MECH:
                stats.clear(); // other stats handled by Mech skills
                stats.put(BaseStat.mastery, si.getValue(SkillStat.mastery, slv));
                stats.put(BaseStat.mhpR, si.getValue(SkillStat.indieMhpR, slv));
                break;
            case Mechanic.ROBOT_MASTERY:
                stats.clear(); // other stats handled by client
                stats.put(BaseStat.summonTimeR, si.getValue(SkillStat.y, slv));
                break;
        }
    }
}
