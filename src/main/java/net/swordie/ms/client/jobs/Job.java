package net.swordie.ms.client.jobs;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.runestones.RuneStone;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.*;
import net.swordie.ms.client.character.skills.temp.*;
import net.swordie.ms.client.jobs.adventurer.*;
import net.swordie.ms.client.jobs.adventurer.archer.*;
import net.swordie.ms.client.jobs.adventurer.magician.*;
import net.swordie.ms.client.jobs.adventurer.pirate.*;
import net.swordie.ms.client.jobs.adventurer.thief.*;
import net.swordie.ms.client.jobs.adventurer.warrior.*;
import net.swordie.ms.client.jobs.cygnus.*;
import net.swordie.ms.client.jobs.legend.*;
import net.swordie.ms.client.jobs.nova.*;
import net.swordie.ms.client.jobs.resistance.*;
import net.swordie.ms.client.jobs.sengoku.*;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.*;
import net.swordie.ms.enums.*;
import net.swordie.ms.life.*;
import net.swordie.ms.life.mob.*;
import net.swordie.ms.life.mob.skill.BurnedInfo;
import net.swordie.ms.loaders.*;
import net.swordie.ms.util.*;
import net.swordie.ms.world.field.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.client.jobs.resistance.Mechanic.*;


/**
 * Created on 1/2/2018.
 */
public abstract class Job {
	private static final Logger log = LogManager.getRootLogger();

    protected Char chr;

	public static final int MONOLITH = 80011261;
	public static final int ELEMENTAL_SYLPH = 80001518;
	public static final int FLAME_SYLPH = 80001519;
	public static final int THUNDER_SYLPH = 80001520;
	public static final int ICE_SYLPH = 80001521;
	public static final int EARTH_SYLPH = 80001522;
	public static final int DARK_SYLPH = 80001523;
	public static final int HOLY_SYLPH = 80001524;
	public static final int SALAMANDER_SYLPH = 80001525;
	public static final int ELECTRON_SYLPH = 80001526;
	public static final int UNDINE_SYLPH = 80001527;
	public static final int GNOME_SYLPH = 80001528;
	public static final int DEVIL_SYLPH = 80001529;
	public static final int ANGEL_SYLPH = 80001530;

	public static final int ELEMENTAL_SYLPH_2 = 80001715;
	public static final int FLAME_SYLPH_2 = 80001716;
	public static final int THUNDER_SYLPH_2 = 80001717;
	public static final int ICE_SYLPH_2 = 80001718;
	public static final int EARTH_SYLPH_2 = 80001719;
	public static final int DARK_SYLPH_2 = 80001720;
	public static final int HOLY_SYLPH_2 = 80001721;
	public static final int SALAMANDER_SYLPH_2 = 80001722;
	public static final int ELECTRON_SYLPH_2 = 80001723;
	public static final int UNDINE_SYLPH_2 = 80001724;
	public static final int GNOME_SYLPH_2 = 80001725;
	public static final int DEVIL_SYLPH_2 = 80001726;
	public static final int ANGEL_SYLPH_2 = 80001727;

	public static final int WHITE_ANGELIC_BLESSING = 80000155;
	public static final int WHITE_ANGELIC_BLESSING_2 = 80001154;
	public static final int LIGHTNING_GOD_RING = 80001262;
	public static final int LIGHTNING_GOD_RING_2 = 80011178;
	public static final int GUARD_RING = 80011149;
	public static final int SUN_RING = 80010067;
	public static final int RAIN_RING = 80010068;
	public static final int RAINBOW_RING = 80010069;
	public static final int SNOW_RING = 80010070;
	public static final int LIGHTNING_RING = 80010071;
	public static final int WIND_RING = 80010072;

	public static final int BOSS_SLAYERS = 91001022;
	public static final int UNDETERRED = 91001023;
	public static final int FOR_THE_GUILD = 91001024;

	public static final int REBOOT = 80000186;
	public static final int REBOOT2 = 80000187;

	public static final int MAPLERUNNER_DASH = 80001965;

	public static final int[] REMOVE_ON_STOP = new int[] {
			MAPLERUNNER_DASH
	};

	public static final int[] REMOVE_ON_WARP = new int[] {
			MAPLERUNNER_DASH
	};

	private static final int[] JOBLESS_BUFFS = new int[] {
			BOSS_SLAYERS,
			UNDETERRED,
			FOR_THE_GUILD,
			MAPLERUNNER_DASH
	};

	public Job(Char chr) {
		this.chr = chr;
		if (chr.getId() != 0 && chr.getWorld().isReboot()) {
			if (!chr.hasSkill(REBOOT)) {
				Skill skill = SkillData.getSkillDeepCopyById(REBOOT);
				skill.setCurrentLevel(1);
				chr.addSkill(skill);
			}
		}
	}

	public abstract boolean isHandlerOfJob(short id);

	public void handleAttack(Char chr, AttackInfo attackInfo) {
		TemporaryStatManager tsm = chr.getTemporaryStatManager();
		int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
		SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
		int slv = chr.getSkillLevel(skillID);
		boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;

		// Recovery Rune  HP Recovery
		if (tsm.getOptByCTSAndSkill(IgnoreMobDamR, RuneStone.LIBERATE_THE_RECOVERY_RUNE) != null) {
			SkillInfo recoveryRuneInfo = SkillData.getSkillInfoById(RuneStone.LIBERATE_THE_RECOVERY_RUNE);
			byte recoveryRuneSLV = 1; //Hardcode Skill Level to 1
			int healrate = recoveryRuneInfo.getValue(dotHealHPPerSecondR, recoveryRuneSLV);
			int healing = chr.getMaxHP() / (100 / healrate);
			chr.heal(healing);
		}

		for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
			Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
			if (mob == null) {
				continue;
			}
			MobTemporaryStat mts = mob.getTemporaryStat();
			if (attackInfo.skillId != Bishop.BAHAMUT && mts.hasCurrentMobStat(MobStat.BahamutLightElemAddDam)) {
				mts.removeMobStat(MobStat.BahamutLightElemAddDam, true);
			}
		}

		Option o1 = new Option();
		Option o2 = new Option();
		Option o3 = new Option();
		Option o4 = new Option();
		switch (skillID) {
			case RuneStone.LIBERATE_THE_DESTRUCTIVE_RUNE:
				// Attack of the Rune
				AffectedArea aa = AffectedArea.getAffectedArea(chr, attackInfo);
				aa.setMobOrigin((byte) 0);
				aa.setPosition(chr.getPosition());
				aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
				chr.getField().spawnAffectedArea(aa);
				for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
					Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
					MobTemporaryStat mts = mob.getTemporaryStat();
					mts.createAndAddBurnedInfo(chr, skillID, 1);
				}

				// Buff of the Rune
				si = SkillData.getSkillInfoById(RuneStone.LIBERATE_THE_DESTRUCTIVE_RUNE_BUFF); //Buff Info
				o1.nReason = RuneStone.LIBERATE_THE_DESTRUCTIVE_RUNE_BUFF;
				o1.nValue = si.getValue(indieDamR, 1); //50% DamR
				o1.tStart = Util.getCurrentTime();
				o1.tTerm = si.getValue(time, 1);
				tsm.putCharacterStatValue(IndieDamR, o1);

				tsm.sendSetStatPacket();
				break;
		}
	}

	public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
		this.handleJoblessbuff(chr, skillID, slv);
		TemporaryStatManager tsm = chr.getTemporaryStatManager();
		SkillInfo si = SkillData.getSkillInfoById(skillID);

		Option o1 = new Option();
		Option o2 = new Option();
		Summon summon;
		Field field;
		switch (skillID) {
			// BEGINNER SKILLS -----------------------------------------------------------------------------------------
			case Beginner.NIMBLE_FEET:
			case Evan.NIMBLE_FEET:
				o1.nOption = 5 + 5 * slv;
				o1.rOption = skillID;
				o1.tOption = 4 * slv;
				tsm.putCharacterStatValue(Speed, o1);
				chr.addSkillCooldown(skillID, 60 * 1000);
				break;
			case Beginner.RECOVERY:
			case Evan.RECOVERY:
				o1.nOption = 24 * slv;
				o1.rOption = skillID;
				o1.tOption = 30;
				tsm.putCharacterStatValue(Regen, o1);
				chr.addSkillCooldown(skillID, 10 * 60 * 1000);
				break;
			// LINK SKILLS ---------------------------------------------------------------------------------------------
			case Shade.CLOSE_CALL:
			case Shade.CLOSE_CALL_LINK:
				o1.nOption = 1;
				o1.rOption = skillID;
				tsm.putCharacterStatValue(PreReviveOnce, o1);
				break;
			// RETURN SKILLS -------------------------------------------------------------------------------------------
			case Beginner.MAPLE_RETURN:
			case Jett.RETURN_TO_SPACESHIP:
			case Noblesse.IMPERIAL_RECALL:
			case Mihile.IMPERIAL_RECALL:
			case Aran.RETURN_TO_RIEN:
			case Evan.BACK_TO_NATURE:
			// case Mercedes
			case Phantom.TO_THE_SKIES:
			// case Shade
			// case Luminous
			case Citizen.SECRET_ASSEMBLY:
			case Xenon.PROMESSA_ESCAPE:
			// case Kaiser
			case AngelicBuster.DAY_DREAMER:
			case Zero.TEMPLE_RECALL:
			case BeastTamer.HOMEWARD_BOUND:
			case Kinesis.RETURN_KINESIS:
				o1.nValue = si.getValue(x, slv);
				Field toField = chr.getOrCreateFieldByCurrentInstanceType(o1.nValue);
				chr.warp(toField);
				break;
			// HERO'S WILL ---------------------------------------------------------------------------------------------
			// Explorers
			case Hero.HEROS_WILL_HERO:
			case Paladin.HEROS_WILL_PALADIN:
			case DarkKnight.HEROS_WILL_DRK:
			case Bowmaster.HEROS_WILL_BM:
			case Marksman.HEROS_WILL_MM:
			case FirePoison.HEROS_WILL_FP:
			case IceLightning.HEROS_WILL_IL:
			case Bishop.HEROS_WILL_BISH:
			case NightLord.HEROS_WILL_NL:
			case Shadower.HEROS_WILL_SHAD:
			case DualBlade.HEROS_WILL_DB:
			case Buccaneer.HEROS_WILL_BUCC:
			case Corsair.HEROS_WILL_SAIR:
			case Cannoneer.HEROS_WILL_CANNON:
			case Jett.HEROS_WILL_JETT:
			// Cygnus do not have Hero's Will
			// Heroes
			case Aran.HEROS_WILL_ARAN:
			case Evan.HEROS_WILL_EVAN:
			case Mercedes.HEROS_WILL_MERC:
			case Phantom.HEROS_WILL_PH:
			case Shade.HEROS_WILL_SH:
			case Luminous.HEROS_WILL_LUMI:
			// Resistance
			case BattleMage.HEROS_WILL_BAM:
			case WildHunter.HEROS_WILL_WH:
			case HEROS_WILL_MECH:
			case Xenon.HEROS_WILL_XENON:
			case Blaster.HEROS_WILL_BLASTER:
			// Demons do not have Hero's Will
			// Nova
			case Kaiser.NOVA_TEMPERANCE_KAISER:
			case AngelicBuster.NOVA_TEMPERANCE_AB:
			// Sengoku
			case Hayato.AKATSUKI_BLOSSOMS:
			case Kanna.BLOSSOMING_DAWN:
			// Others
			// Zero does not have Hero's Will
			// BeastTamer does not have Hero's Will
			case Kinesis.CLEAR_MIND:
				tsm.removeAllDebuffs();
				break;
			// MAPLE WARRIOR -------------------------------------------------------------------------------------------
			// Explorers
			case Hero.MAPLE_WARRIOR_HERO:
			case Paladin.MAPLE_WARRIOR_PALADIN:
			case DarkKnight.MAPLE_WARRIOR_DARK_KNIGHT:
			case Bowmaster.MAPLE_WARRIOR_BOW:
			case Marksman.MAPLE_WARRIOR_XBOW:
			case FirePoison.MAPLE_WARRIOR_FP:
			case IceLightning.MAPLE_WARRIOR_IL:
			case Bishop.MAPLE_WARRIOR_BISH:
			case NightLord.MAPLE_WARRIOR_NL:
			case Shadower.MAPLE_WARRIOR_SHAD:
			case DualBlade.MAPLE_WARRIOR_DB:
			case Buccaneer.MAPLE_WARRIOR_BUCC:
			case Corsair.MAPLE_WARRIOR_SAIR:
			case Cannoneer.MAPLE_WARRIOR_CANNON:
			case Jett.MAPLE_WARRIOR_JETT:
			// Cygnus
			case DawnWarrior.CALL_OF_CYGNUS_DW:
			case BlazeWizard.CALL_OF_CYGNUS_BW:
			case WindArcher.CALL_OF_CYGNUS_WA:
			case NightWalker.CALL_OF_CYGNUS_NW:
			case ThunderBreaker.CALL_OF_CYGNUS_TB:
			case Mihile.CALL_OF_CYGNUS_MIHILE:
			// Heroes
			case Aran.MAPLE_WARRIOR_ARAN:
			case Evan.MAPLE_WARRIOR_EVAN:
			case Mercedes.MAPLE_WARRIOR_MERC:
			case Phantom.MAPLE_WARRIOR_PH:
			case Shade.MAPLE_WARRIOR_SH:
			case Luminous.MAPLE_WARRIOR_LUMI:
			// Resistance
			case BattleMage.MAPLE_WARRIOR_BAM:
			case WildHunter.MAPLE_WARRIOR_WH:
			case MAPLE_WARRIOR_MECH:
			case Xenon.MAPLE_WARRIOR_XENON:
			case Blaster.MAPLE_WARRIOR_BLASTER:
			case Demon.MAPLE_WARRIOR_DA:
			case Demon.MAPLE_WARRIOR_DS:
			// Nova
			case Kaiser.NOVA_WARRIOR_KAISER:
			case AngelicBuster.NOVA_WARRIOR_AB:
			// Sengoku
			case Hayato.AKATSUKI_HERO_HAYATO:
			case Kanna.AKATUSKI_HERO_KANNA:
			// Others
			case Zero.RHINNES_PROTECTION:
			case BeastTamer.MAPLE_GUARDIAN:
			case Kinesis.PRESIDENTS_ORDERS:
				o1.nOption = si.getValue(x, slv);
				o1.rOption = skillID;
				o1.tOption = si.getValue(time, slv);
				tsm.putCharacterStatValue(BasicStatUp, o1);
				break;
			// EPIC ADVENTURE ------------------------------------------------------------------------------------------
			// Explorers
			case Hero.EPIC_ADVENTURE_HERO:
			case Paladin.EPIC_ADVENTURE_PALA:
			case DarkKnight.EPIC_ADVENTURE_DRK:
			case Bowmaster.EPIC_ADVENTURE_BOW:
			case Marksman.EPIC_ADVENTURE_XBOW:
			case FirePoison.EPIC_ADVENTURE_FP:
			case IceLightning.EPIC_ADVENTURE_IL:
			case Bishop.EPIC_ADVENTURE_BISH:
			case NightLord.EPIC_ADVENTURE_NL:
			case Shadower.EPIC_ADVENTURE_SHAD:
			case DualBlade.EPIC_ADVENTURE_DB:
			case Buccaneer.EPIC_ADVENTURER_BUCC:
			case Corsair.EPIC_ADVENTURER_SAIR:
			case Cannoneer.EPIC_ADVENTURER_CANNON:
			case Jett.EPIC_ADVENTURER_JETT:
				handleEpicAdventure(chr, skillID, slv, inPacket,
						((Predicate<Short>) JobConstants::isAdventurer).or(JobConstants::isJett));
				break;
			// Cygnus
			case DawnWarrior.GLORY_OF_THE_GUARDIANS_DW:
			case BlazeWizard.GLORY_OF_THE_GUARDIANS_BW:
			case WindArcher.GLORY_OF_THE_GUARDIANS_WA:
			case NightWalker.GLORY_OF_THE_GUARDIANS_NW:
			case ThunderBreaker.GLORY_OF_THE_GUARDIANS_TB:
			case Mihile.QUEEN_OF_TOMORROW:
				handleEpicAdventure(chr, skillID, slv, inPacket,
						((Predicate<Short>) JobConstants::isCygnusKnight).or(JobConstants::isMihile));
				break;
			// Heroes
			case Aran.HEROIC_MEMORIES_ARAN:
			case Evan.HEROIC_MEMORIES_EVAN:
			case Mercedes.HEROIC_MEMORIES_MERC:
			case Phantom.HEROIC_MEMORIES_PH:
			case Shade.HEROIC_MEMORIES_SH:
			case Luminous.HEROIC_MEMORIES_LUMI:
				handleEpicAdventure(chr, skillID, slv, inPacket, JobConstants::isLegend);
				break;
			// Resistance
			case BattleMage.FOR_LIBERTY_BAM:
			case WildHunter.FOR_LIBERTY_WH:
			case FOR_LIBERTY_MECH:
			// case Xenon.ORBITAL_CATACLYSM: (self)
			case Blaster.FOR_LIBERTY_BLASTER:
			case Demon.DEMONIC_FORTITUDE_DS:
			case Demon.DEMONIC_FORTITUDE_DA:
				handleEpicAdventure(chr, skillID, slv, inPacket, JobConstants::isResistance);
				break;
			// Nova : Kaiser, AngelicBuster do not have Epic Adventure
			// Sengoku
			case Hayato.PRINCESS_VOW_HAYATO:
			case Kanna.PRINCESS_VOW_KANNA:
				handleEpicAdventure(chr, skillID, slv, inPacket, JobConstants::isSengoku);
				break;
			// Others : Zero, BeastTamer, Kinesis do not have Epic Adventure
		}
		tsm.sendSetStatPacket();
	}

	private void handleEpicAdventure(Char chr, int skillID, int slv, InPacket inPacket, Predicate<Short> pred) {
		TemporaryStatManager tsm = chr.getTemporaryStatManager();
		SkillInfo si = SkillData.getSkillInfoById(skillID);
		Option o1 = new Option();
		Option o2 = new Option();
		o1.nReason = skillID;
		o1.nValue = si.getValue(indieDamR, slv);
		o1.tStart = Util.getCurrentTime();
		o1.tTerm = si.getValue(time, slv);
		o2.nReason = skillID;
		o2.nValue = si.getValue(indieMaxDamageOverR, slv);
		o2.tStart = Util.getCurrentTime();
		o2.tTerm = si.getValue(time, slv);
		if (chr.getParty() == null) {
			tsm.putCharacterStatValue(IndieDamR, o1);
			tsm.putCharacterStatValue(IndieMaxDamageOverR, o2);
			tsm.sendResetStatPacket();
		} else {
			Position pos = inPacket.decodePosition();
			Rect rect = pos.getRectAround(si.getFirstRect());
			List<PartyMember> partyMembers = chr.getField().getPartyMembersInRect(chr, rect).stream()
					.filter(pml -> pml.getChr().getHP() > 0 &&
							pred.test(pml.getChr().getJob()) // check job
					)
					.toList();
			for (PartyMember partyMember : partyMembers) {
				TemporaryStatManager partyTsm = partyMember.getChr().getTemporaryStatManager();
				partyTsm.putCharacterStatValue(IndieDamR, o1);
				partyTsm.putCharacterStatValue(IndieMaxDamageOverR, o2);
				partyTsm.sendSetStatPacket();
			}
		}
	}

	public void handleJoblessbuff(Char chr, int skillID, int slv) {
		TemporaryStatManager tsm = chr.getTemporaryStatManager();
		SkillInfo si = SkillData.getSkillInfoById(skillID);

		int noviceSkill = SkillConstants.getNoviceSkillFromRace(skillID);
		// archangel, darkangel
		if (noviceSkill == 1085 || noviceSkill == 1087 || noviceSkill == 1090 || noviceSkill == 1179) {
			Summon summon = Summon.getSummonBy(chr, skillID, (byte) slv);
			summon.setMoveAction((byte) 4);
			summon.setAssistType(AssistType.Heal);
			summon.setFlyMob(true);
			chr.getField().spawnSummon(summon);
			return;
		}
		// soaring
		if (noviceSkill == 1026) {
			if (chr.getField().isFly()) {
				Option option = new Option();
				option.nOption = 1;
				option.rOption = skillID;
				option.tOption = 0;
				tsm.putCharacterStatValue(Flying, option);
				tsm.sendSetStatPacket();
			}
			return;
		}

		Option o1 = new Option();
		Option o2 = new Option();
		Summon summon;
		// TOOD: make sure user owns skill
		switch (skillID) {
			case MONOLITH:
				summon = Summon.getSummonBy(chr, skillID, slv);
				summon.setMoveAbility(MoveAbility.Stop);
				chr.getField().spawnSummon(summon);
				chr.getField().setKishin(true);
				break;
			case WHITE_ANGELIC_BLESSING:
			case WHITE_ANGELIC_BLESSING_2:
			case LIGHTNING_GOD_RING:
			case LIGHTNING_GOD_RING_2:
			case GUARD_RING:
			case SUN_RING:
			case RAIN_RING:
			case RAINBOW_RING:
			case SNOW_RING:
			case LIGHTNING_RING:
			case WIND_RING:
				summon = Summon.getSummonBy(chr, skillID, (byte) slv);
				summon.setMoveAction((byte) 4);
				summon.setAssistType(AssistType.Heal);
				summon.setFlyMob(true);
				chr.getField().spawnSummon(summon);
				break;
			case ELEMENTAL_SYLPH:
			case FLAME_SYLPH:
			case THUNDER_SYLPH:
			case ICE_SYLPH:
			case EARTH_SYLPH:
			case DARK_SYLPH:
			case HOLY_SYLPH:
			case SALAMANDER_SYLPH:
			case ELECTRON_SYLPH:
			case UNDINE_SYLPH:
			case GNOME_SYLPH:
			case DEVIL_SYLPH:
			case ANGEL_SYLPH:
			case ELEMENTAL_SYLPH_2:
			case FLAME_SYLPH_2:
			case THUNDER_SYLPH_2:
			case ICE_SYLPH_2:
			case EARTH_SYLPH_2:
			case DARK_SYLPH_2:
			case HOLY_SYLPH_2:
			case SALAMANDER_SYLPH_2:
			case ELECTRON_SYLPH_2:
			case UNDINE_SYLPH_2:
			case GNOME_SYLPH_2:
			case DEVIL_SYLPH_2:
			case ANGEL_SYLPH_2:
				summon = Summon.getSummonBy(chr, skillID, (byte) slv);
				chr.getField().spawnSummon(summon);
				break;
		}
		tsm.sendSetStatPacket();
	}

	public void handleGuildBuff(Char chr, int skillID, int slv) {
		SkillInfo si = SkillData.getSkillInfoById(skillID);
		TemporaryStatManager tsm = chr.getTemporaryStatManager();
		Option o1 = new Option();
		Option o2 = new Option();
		Option o3 = new Option();
		Option o4 = new Option();
		Option o5 = new Option();
		Summon summon;
		Field field;
		int curTime = Util.getCurrentTime();
		boolean sendStat = true;
		switch (skillID) {
			case BOSS_SLAYERS:
				o1.nReason = skillID;
				o1.nValue = si.getValue(indieBDR, slv);
				o1.tStart = curTime;
				o1.tTerm = si.getValue(time, slv);
				tsm.putCharacterStatValue(IndieBDR, o1);
				break;
			case UNDETERRED:
				o1.nReason = skillID;
				o1.nValue = si.getValue(indieIgnoreMobpdpR, slv);
				o1.tStart = curTime;
				o1.tTerm = si.getValue(time, slv);
				tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o1);
				break;
			case FOR_THE_GUILD:
				o1.nReason = skillID;
				o1.nValue = si.getValue(indieDamR, slv);
				o1.tStart = curTime;
				o1.tTerm = si.getValue(time, slv);
				tsm.putCharacterStatValue(IndieDamR, o1);
				break;
			case MAPLERUNNER_DASH:
				o1.nReason = o2.nReason = skillID;
				o1.tStart = o2.tStart = curTime;
				o1.tTerm = o2.tTerm = si.getValue(time, slv);
				o1.nValue = si.getValue(indieForceJump, slv);
				tsm.putCharacterStatValue(IndieForceJump, o1);
				o2.nValue = si.getValue(indieForceSpeed, slv);
				tsm.putCharacterStatValue(IndieForceSpeed, o2);
				break;
			default:
				sendStat = false;
		}
		if (sendStat) {
			tsm.sendSetStatPacket();
		}
	}

	/**
	 * Handles the 'middle' part of hit processing, namely the job-specific stuff like Magic Guard,
	 * and puts this info in <code>hitInfo</code>.
	 *
	 * @param chr     The character
	 * @param hitInfo The hit info that should be altered if necessary
	 */
	public void handleHit(Char chr, HitInfo hitInfo) {
		TemporaryStatManager tsm = chr.getTemporaryStatManager();
		Field field = chr.getField();

		// General - Damage Reduce
		if (hitInfo.hpDamage > 0) {
			int totalDmgReduceR = chr.getTotalStat(BaseStat.dmgReduce);
			if (totalDmgReduceR > 0) {
				int dmgReduce = hitInfo.hpDamage * totalDmgReduceR / 100;
				hitInfo.hpDamage = dmgReduce < hitInfo.hpDamage ? hitInfo.hpDamage - dmgReduce : 0;
			}
		}

		// Bishop - Holy Magic Shell
		Bishop.handleHolyMagicShell(chr, hitInfo);

		// Mihile - Soul Link
		Mihile.handleSoulLink(chr, hitInfo);


		// Power Guard Damage Reflection
		if (tsm.hasStat(PowerGuard)) {
			if (hitInfo.blockSkillDamage > 0 && hitInfo.reflectMobID > 0) {
				Life reflectMob = chr.getField().getLifeByObjectID(hitInfo.reflectMobID);
				if (reflectMob != null) {
					((Mob) reflectMob).damage(chr, hitInfo.blockSkillDamage);
				}
			}
		}

		// Magic Guard
		if (chr.getTotalStat(BaseStat.magicGuard) > 0) {
			int dmgPerc = chr.getTotalStat(BaseStat.magicGuard);
			int dmg = hitInfo.hpDamage;
			int mpDmg = (int) (dmg * (dmgPerc / 100D));
			mpDmg = chr.getStat(Stat.mp) - mpDmg < 0 ? chr.getStat(Stat.mp) : mpDmg;
			hitInfo.hpDamage = dmg - mpDmg;
			hitInfo.mpDamage = mpDmg;
		}

		// Miss or Evade
		if (hitInfo.hpDamage <= 0) {
			// Hypogram Field Support
			if (chr.getParty() != null) {
				for (AffectedArea aa : field.getAffectedAreas().stream().filter(aa -> aa.getSkillID() == Xenon.HYPOGRAM_FIELD_SUPPORT).collect(Collectors.toList())) {
					boolean isInsideAA = aa.getRect().hasPositionInside(chr.getPosition());
					if (!isInsideAA) {
						continue;
					}
					Option supportOption = tsm.getOptByCTSAndSkill(IndieMHPR, Xenon.HYPOGRAM_FIELD_SUPPORT);
					if (supportOption != null) {
						Char xenonChr = chr.getParty().getPartyMemberByID(supportOption.wOption).getChr();
						if (xenonChr != null && xenonChr.getField() == chr.getField() && xenonChr != chr) {
							((Xenon) xenonChr.getJobHandler()).incrementSupply(1);
						}
					}
				}
			}
		}
	}

	/**
	 * The final part of the hit process. Assumes the correct info (wrt buffs for example) is
	 * already in <code>hitInfo</code>.
	 *
	 * @param chr
	 * 		The character
	 * @param hitInfo
	 * 		The completed hitInfo
	 */
	public void processHit(Char chr, HitInfo hitInfo) {
		hitInfo.hpDamage = Math.max(0, hitInfo.hpDamage); // to prevent -1 (dodges) healing the player.

		if (chr.getStat(Stat.hp) <= hitInfo.hpDamage) {
			TemporaryStatManager tsm = chr.getTemporaryStatManager();

			// Global Revives ---------------------------------------

			// Global - Heaven's Door (Bishop)
			if (tsm.hasStat(HeavensDoor)) {
				Bishop.reviveByHeavensDoor(chr);
				return;
			}

			// Global - Shade Link Skill (Shade)
			else if (tsm.hasStat(PreReviveOnce)) {
				if (Shade.tryReviveByCloseCall(chr)) {
					return;
				}
			}


			// Class Revives ----------------------------------------

			// Dark Knight - Final Pact
			else if (JobConstants.isDarkKnight(chr.getJob()) && chr.hasSkill(DarkKnight.FINAL_PACT_INFO) && !chr.hasSkillOnCooldown(DarkKnight.FINAL_PACT_INFO)) {
				((DarkKnight) chr.getJobHandler()).reviveByFinalPact();
				return;
			}

			// Night Walker - Darkness Ascending
			else if (tsm.getOptByCTSAndSkill(ReviveOnce, NightWalker.DARKNESS_ASCENDING) != null ) {
				((NightWalker) chr.getJobHandler()).reviveByDarknessAscending();
				return;
			}

			// Blaze Wizard - Phoenix Run
			else if (tsm.getOptByCTSAndSkill(ReviveOnce, BlazeWizard.PHOENIX_RUN) != null) {
				((BlazeWizard) chr.getJobHandler()).reviveByPhoenixRun();
				return;
			}

			// Shade - Summon Other Spirit
			else if (tsm.getOptByCTSAndSkill(ReviveOnce, Shade.SUMMON_OTHER_SPIRIT) != null) {
				((Shade) chr.getJobHandler()).reviveBySummonOtherSpirit();
				return;
			}

			// Beast Tamer - Bear Reborn		TODO
			else if (tsm.getOptByCTSAndSkill(ReviveOnce, BeastTamer.BEAR_REBORN) != null) {
				((BeastTamer) chr.getJobHandler()).reviveByBearReborn();
				return;
			}

			// Zero - Rewind
			else if (tsm.getOptByCTSAndSkill(ReviveOnce, Zero.REWIND) != null) {
				((Zero) chr.getJobHandler()).reviveByRewind();
				return;
			}

			// Phantom - Final Feint
			else if (tsm.getOptByCTSAndSkill(ReviveOnce, Phantom.FINAL_FEINT) != null) {
				((Phantom) chr.getJobHandler()).reviveByFinalFeint();
				return;
			}


		}
		int curHP = chr.getStat(Stat.hp);
		int newHP = curHP - hitInfo.hpDamage;
		if (newHP <= 0) {
			curHP = 0;
		} else {
			curHP = newHP;
		}
		Map<Stat, Object> stats = new HashMap<>();
		chr.setStat(Stat.hp, curHP);
		stats.put(Stat.hp, curHP);

		int curMP = chr.getStat(Stat.mp);
		int newMP = curMP - hitInfo.mpDamage;
		if (newMP < 0) {
			// should not happen
			curMP = 0;
		} else {
			curMP = newMP;
		}
		chr.setStat(Stat.mp, curMP);
		stats.put(Stat.mp, curMP);
		chr.write(WvsContext.statChanged(stats));
		chr.getField().broadcastPacket(UserRemote.hit(chr, hitInfo), chr);
		if (chr.getParty() != null) {
			chr.getParty().broadcast(UserRemote.receiveHP(chr), chr);
		}
		if (curHP <= 0) {
			// TODO Add more items for protecting exp and whatnot
			chr.write(UserLocal.openUIOnDead(true, chr.getBuffProtectorItem() != null,
					false, false, false,
					ReviveType.NORMAL.getVal(),0));
		}
	}

	public void handleSkillPrepare(int prepareSkillId) {
		TemporaryStatManager tsm = chr.getTemporaryStatManager();
		int skillID = SkillConstants.getActualSkillIDfromSkillID(prepareSkillId);
		SkillInfo si = SkillData.getSkillInfoById(skillID);
		int slv = chr.getSkillLevel(skillID);
		switch (prepareSkillId) {
		}
	}

	public int alterCooldownSkill(int skillId) {
		if (!chr.hasSkill(skillId)) {
			return -1;
		}
		SkillInfo si = SkillData.getSkillInfoById(skillId);
		int slv = chr.getSkillLevel(skillId);
		int cdInSec = si.getValue(SkillStat.cooltime, slv);
		int cdInMillis = cdInSec > 0 ? cdInSec * 1000 : si.getValue(SkillStat.cooltimeMS, slv);
		// apply cooltimeR BaseStat
		cdInMillis = getBuffedSkillCooldown(cdInMillis);
		// apply cooltimeR from hypers
		int cooldownReductionR = chr.getHyperPsdSkillsCooltimeR().getOrDefault(skillId, 0);
		if (cooldownReductionR > 0) {
			cdInMillis = (int) (cdInMillis - ((double) (cdInMillis * cooldownReductionR) / 100));
		}
		return cdInMillis;
	}


	/**
	 * Gets called when Character receives a debuff from a Mob Skill
	 *
	 * @param chr
	 * 		The Character
	 */

	public void handleMobDebuffSkill(Char chr) {

	}

	/**
	 * Gets called when Character resists a debuff from a Mob Skill with their asr stat
	 *
	 * @param chr
	 * 		The Character
	 */

	public void handleMobDebuffResist(Char chr) {

	}

	/**
	 * Used for Classes that have timers, to cancel the timer on logging out
	 *
	 * @param chr
	 * 		The Character
	 */
	public void handleCancelTimer(Char chr) {

	}


	public int getFinalAttackSkill() {
		return 0;
	}

	/**
	 * Handles when specific CTSs are removed.
	 *
	 * @param cts The Character Temporary Stat
	 */
	public void handleRemoveCTS(CharacterTemporaryStat cts) {

	}

	/**
	 * Called when a player is right-clicking a buff, requesting for it to be disabled.
	 *
	 * @param skillID The skill that the player right-clicked
	 */
	public void handleRemoveBuff(int skillID) {

	}

	/**
	 * Called when a player removes all points in a skill
	 * @param skillID
	 */
	public void handleRemoveSkill(int skillID) {

	}

	/**
	 * Called every time a mob should take burn damage
	 *
	 * @param mob The mob taking burn damage
	 * @param bi The BurnedInfo object containing info about damage, skill etc.
	 */
	public void handleMobBurn(Mob mob, BurnedInfo bi) {

	}

	/**
	 * Called when a mob dies
	 *
	 * @param mob The Mob that has died
	 */
	public void handleMobDeath(Mob mob) {

	}

	/**
	 * Called when client sends a request to recalculate the damage stat
	 */
	public void handleCalcDamageStatSet() {

	}

	/**
	 * Called when client sends a USER_FORCE_ATOM_COLLISION packet
	 */
	public void handleForceAtomCollision(int forceAtomKey, int mobId) {

	}

	/**
	 * Called when Character warps to another map
	 */
	public void handleWarp() {

	}

	public void handleLevelUp() {
		short level = chr.getLevel();
		Map<Stat, Object> stats = new HashMap<>();
		if (level > 10) {
			chr.addStat(Stat.ap, 5);
			stats.put(Stat.ap, (short) chr.getStat(Stat.ap));
		} else {
			if (level >= 6) {
				chr.addStat(Stat.str, 4);
				chr.addStat(Stat.dex, 1);
			} else {
				chr.addStat(Stat.str, 5);
			}
			stats.put(Stat.str, (short) chr.getStat(Stat.str));
			stats.put(Stat.dex, (short) chr.getStat(Stat.dex));
		}
		if (level >= 50) {
			chr.addHonorExp(700 + ((chr.getLevel() - 50) / 10) * 100);
		}
		int sp = SkillConstants.getBaseSpByLevel(level);
		if ((level % 10) % 3 == 0 && level > 100) {
			sp *= 2; // double sp on levels ending in 3/6/9
		}
		chr.addSpToJobByCurrentLevel(sp);
		stats.put(Stat.sp, chr.getAvatarData().getCharacterStat().getExtendSP());
		byte linkSkillLevel = (byte) SkillConstants.getLinkSkillLevelByCharLevel(level);
		int linkSkillID = SkillConstants.getOriginalOfLinkedSkill(SkillConstants.getLinkSkillByJob(chr.getJob()));
		if (linkSkillID != 0 && linkSkillLevel > 0) {
			Skill skill = chr.getSkill(linkSkillID, true);
			if (skill.getCurrentLevel() != linkSkillLevel) {
				chr.addSkill(linkSkillID, linkSkillLevel, 3);
			}
		}

		int[][] incVal = GameConstants.getIncValArray(chr.getJob());
		if (incVal != null) {
			chr.addStat(Stat.mhp, incVal[0][1]);
			stats.put(Stat.mhp, chr.getStat(Stat.mhp));
			if (!JobConstants.isNoManaJob(chr.getJob())) {
				chr.addStat(Stat.mmp, incVal[3][0]);
				stats.put(Stat.mmp, chr.getStat(Stat.mmp));
			}
		} else {
			chr.chatMessage("Unhandled HP/MP job " + chr.getJob());
		}
		chr.write(WvsContext.statChanged(stats));
		chr.heal(chr.getMaxHP());
		chr.healMP(chr.getMaxMP());

		if (chr.getWorld().isReboot()) {
			Skill skill = SkillData.getSkillDeepCopyById(REBOOT2);
			skill.setCurrentLevel(level);
			chr.addSkill(skill);
		}
		switch (level) {
			case 10: {
				String message = "#b[Guide] 1st Job Advancement#k\r\n\r\n";
				message += "You've reached level 10, and are ready for your #b[1st Job Advancement]#k!\r\n\r\n";
				message += "Complete the #r[Job Advancement]#k quest and unlock your 1st job advancement!\r\n";
				chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));
				break;
			}
			case 20: {
				String message;
				if (chr.getJob() == JobConstants.JobEnum.THIEF.getJobId() && chr.getSubJob() == 1) {
					message = "#b[Guide] 1.5th Job Advancement#k\r\n\r\n";
					message += "You've reached level 20 and are ready for your #b[1.5th Job Advancement]#k!\r\n\r\n";
					message += "Complete the #r[Job Advancement]#k quest to unlock your 1.5th job advancement!\r\n";
					chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));
				}
				message = "#b[Guide] Upgrade#k\r\n\r\n";
				message += "You've reached level 20, and can now use #b[Scroll Enhancement]#k!\r\n\r\n";
				message += "Accept the quest #bDo You Know About Scroll Enhancements?#k from the Quest Notifier!\r\n";
				chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));
				break;
			}
			case 30: {
				String message = "#b[Guide] 2nd Job Advancement#k\r\n\r\n";
				message += "You've reached level 30, and are ready for your #b[2nd Job Advancement]#k!\r\n\r\n";
				message += "Complete the #r[Job Advancement]#k quest to unlock your 2nd job advancement!\r\n";
				chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));

				message = "#b[Guide] Ability#k\r\n\r\n";
				message += "You've reached level 30 and can now unlock #b[Abilities]#k!\r\n\r\n";
				message += "Accept the quest #bFirst Ability - The Eye Opener#k from the Quest Notifier!\r\n";
				chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));
				break;
			}
			case 31: {
				String message = "#b[Guide] Traits#k\r\n\r\n";
				message += "From level 30 and can now unlock #b[Traits]#k!\r\n\r\n";
				message += "Open your #bProfession UI (Default Hotkey: B)#k and check your #b[Traits]#k!\r\n";
				chr.write(UserLocal.addPopupSay(9010000, 6000, message, "FarmSE.img/boxResult"));
				break;
			}
		}
	}

	private boolean isJoblessBuff(int skillID) {
		return Arrays.stream(JOBLESS_BUFFS).anyMatch(b -> b == skillID);
	}

	public int getMpCon(int skillId, int slv) {
		SkillInfo si = SkillData.getSkillInfoById(skillId);
		if (si == null) {
			return 0;
		}
		int mpCon = si.getValue(SkillStat.mpCon, slv);
		mpCon += (int) (mpCon * (chr.getTotalStat(BaseStat.costMpR) / 100D));
		mpCon -= (int) (mpCon * (chr.getTotalStat(BaseStat.mpConReduce) / 100D));
		return mpCon;
	}

	public final int getBuffedSkillDuration(int duration) {
		return (int) ((double) duration * (chr.getTotalStat(BaseStat.buffTimeR) / 100D));
	}

	public final int getBuffedSkillCooldown(int cooldown) {
		int cooltimeR = Math.max(100 - chr.getTotalStat(BaseStat.reduceCooltime), 0);
		return (int) ((double) cooldown * (cooltimeR / 100D));
	}

	public void setCharCreationStats(Char chr) {
		CharacterStat characterStat = chr.getAvatarData().getCharacterStat();
		characterStat.setLevel(1);
		characterStat.setStr(12);
		characterStat.setDex(5);
		characterStat.setInt(4);
		characterStat.setLuk(4);
		characterStat.setHp(50);
		characterStat.setMaxHp(50);
		characterStat.setMp(5);
		characterStat.setMaxMp(5);

		characterStat.setPosMap(100000000);// should be handled for each job not here
		Item whitePot = ItemData.getItemDeepCopy(2000002);
		whitePot.setQuantity(100);
		chr.addItemToInventory(whitePot);
		Item manaPot = ItemData.getItemDeepCopy(2000006);
		manaPot.setQuantity(100);
		chr.addItemToInventory(manaPot);
		Item hyperTp = ItemData.getItemDeepCopy(5040004);
		chr.addItemToInventory(hyperTp);
	}
}
