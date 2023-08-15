package net.swordie.ms.life.mob;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.BattleRecordMan;
import net.swordie.ms.life.mob.skill.BurnedInfo;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.connection.packet.MobPool;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.util.Util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static net.swordie.ms.life.mob.MobStat.*;


public class MobTemporaryStat {
	private final Comparator<MobStat> mobStatComper = (o1, o2) -> {
		int res = 0;
		if (o1.getPos() < o2.getPos()) {
			res = -1;
		} else if (o1.getPos() > o2.getPos()) {
			res = 1;
		} else {
			if (o1.getVal() < o2.getVal()) {
				res = -1;
			} else if (o1.getVal() > o2.getVal()) {
				res = 1;
			}
		}
		return res;
	};
	private final Map<MobStat, Option> currentStatVals = new ConcurrentHashMap<>();
	private final Map<MobStat, Option> newStatVals = new ConcurrentHashMap<>();
	private final Map<MobStat, Option> removedStatVals = new ConcurrentHashMap<>();
	private final Map<MobStat, ScheduledFuture> schedules = new ConcurrentHashMap<>();

	private final Map<Integer, Map<Integer, List<BurnedInfo>>> burnedInfos = new ConcurrentHashMap<>(); // chrId -> skillId -> bis
	private final Map<BurnedInfo, ScheduledFuture> burnCancelSchedules = new ConcurrentHashMap<>();
	private final Map<BurnedInfo, ScheduledFuture> burnSchedules = new ConcurrentHashMap<>();

	private final Lock setStatLock = new ReentrantLock();
	private final Lock removeStatLock = new ReentrantLock();
	private final ReadWriteLock burnedInfoLock = new ReentrantReadWriteLock();

	private Mob mob;
	private String linkTeam;

	public MobTemporaryStat(Mob mob) {
		this.mob = mob;
	}

	public MobTemporaryStat deepCopy() {
		MobTemporaryStat copy = new MobTemporaryStat(getMob());
		for (BurnedInfo bi : getAllBurnedInfos()) {
			copy.addBurnedInfo(bi);
		}
		copy.setLinkTeam(getLinkTeam());
		for (MobStat ms : getCurrentStatVals().keySet()) {
			copy.addStatOptions(ms, getCurrentStatVals().get(ms).deepCopy());
		}
		return copy;
	}

	public Option getNewOptionsByMobStat(MobStat mobStat) {
		return getNewStatVals().getOrDefault(mobStat, new Option());
	}

	public Option getCurrentOptionsByMobStat(MobStat mobStat) {
		return getCurrentStatVals().getOrDefault(mobStat, new Option());
	}

	public Option getRemovedOptionsByMobStat(MobStat mobStat) {
		return getRemovedStatVals().getOrDefault(mobStat, new Option());
	}

	public void encode(OutPacket outPacket, Map<MobStat, Option> stats, boolean clear) {
		// DecodeBuffer(12) + MobStat::DecodeTemporary
		getSetStatLock().lock();
		try {
			List<MobStat> orderedMsList = stats.keySet().stream()
					.sorted(mobStatComper)
					.toList();
			int[] mask = getMaskByCollection(stats);
			for (int i = 0; i < mask.length; i++) {
				outPacket.encodeInt(mask[i]);
			}
			for (MobStat mobStat : orderedMsList) {
				switch (mobStat) {
					case PAD:
					case PDR:
					case MAD:
					case MDR:
					case ACC:
					case EVA:
					case Speed:
					case Stun:
					case Freeze:
					case Poison:
					case Seal:
					case Darkness:
					case PowerUp:
					case MagicUp:
					case PGuardUp:
					case MGuardUp:
					case PImmune:
					case MImmune:
					case Web:
					case HardSkin:
					case Ambush:
					case Venom:
					case Blind:
					case SealSkill:
					case Dazzle:
					case PCounter:
					case MCounter:
					case RiseByToss:
					case BodyPressure:
					case Weakness:
					case Showdown:
					case MagicCrash:
					case DamagedElemAttr:
					case Dark:
					case Mystery:
					case AddDamParty:
					case HitCriDamR:
					case Fatality:
					case Lifting:
					case DeadlyCharge:
					case Smite:
					case AddDamSkill:
					case Incizing:
					case DodgeBodyAttack:
					case DebuffHealing:
					case AddDamSkill2:
					case BodyAttack:
					case TempMoveAbility:
					case FixDamRBuff:
					case ElementDarkness:
					case AreaInstallByHit:
					case BMageDebuff:
					case JaguarProvoke:
					case JaguarBleeding:
					case DarkLightning:
					case PinkBeanFlowerPot:
					case BattlePvPHelenaMark:
					case PsychicLock:
					case PsychicLockCoolTime:
					case PsychicGroundMark:
					case PowerImmune:
					case PsychicForce:
					case MultiPMDR:
					case ElementResetBySummon:
					case BahamutLightElemAddDam:
					case BossPropPlus:
					case MultiDamSkill:
					case RWLiftPress:
					case RWChoppingHammer:
					case TimeBomb:
					case Treasure:
					case AddEffect:
					case Invincible:
					case Explosion:
					case HangOver:
						outPacket.encodeInt(stats.get(mobStat).nOption);
						outPacket.encodeInt(stats.get(mobStat).rOption);
						outPacket.encodeShort(stats.get(mobStat).tOption / 500);
				}
			}
			Set<MobStat> msSet = stats.keySet();
			if (msSet.contains(PDR)) {
				outPacket.encodeInt(stats.get(PDR).cOption);
			}
			if (msSet.contains(MDR)) {
				outPacket.encodeInt(stats.get(MDR).cOption);
			}
			if (msSet.contains(PCounter)) {
				outPacket.encodeInt(stats.get(PCounter).wOption);
			}
			if (msSet.contains(MCounter)) {
				outPacket.encodeInt(stats.get(MCounter).wOption);
			}
			if (msSet.contains(PCounter)) {
				outPacket.encodeInt(stats.get(PCounter).mOption); // nCounterProb
				outPacket.encodeByte(stats.get(PCounter).bOption); // bCounterDelay
				outPacket.encodeInt(stats.get(PCounter).nReason); // nAggroRank
			} else if (msSet.contains(MCounter)) {
				outPacket.encodeInt(stats.get(MCounter).mOption); // nCounterProb
				outPacket.encodeByte(stats.get(MCounter).bOption); // bCounterDelay
				outPacket.encodeInt(stats.get(MCounter).nReason); // nAggroRank
			}
			if (msSet.contains(Fatality)) {
				outPacket.encodeInt(stats.get(Fatality).wOption);
				outPacket.encodeInt(stats.get(Fatality).uOption);
				outPacket.encodeInt(stats.get(Fatality).pOption);
				outPacket.encodeInt(stats.get(Fatality).yOption);
				outPacket.encodeInt(stats.get(Fatality).mOption);
			}
			if (msSet.contains(Explosion)) {
				outPacket.encodeInt(stats.get(Explosion).wOption);
			}
			if (msSet.contains(ExtraBuffStat)) {
				List<Option> values = stats.get(ExtraBuffStat).extraOpts;
				outPacket.encodeByte(values.size() > 0);
				if (values.size() > 0) {
					outPacket.encodeInt(stats.get(ExtraBuffStat).extraOpts.get(0).nOption); // nPAD
					outPacket.encodeInt(stats.get(ExtraBuffStat).extraOpts.get(0).mOption); // nMAD
					outPacket.encodeInt(stats.get(ExtraBuffStat).extraOpts.get(0).xOption); // nPDR
					outPacket.encodeInt(stats.get(ExtraBuffStat).extraOpts.get(0).yOption); // nMDR
				}
			}
			if (msSet.contains(DeadlyCharge)) {
				outPacket.encodeInt(stats.get(DeadlyCharge).pOption);
				outPacket.encodeInt(stats.get(DeadlyCharge).wOption);
			}
			if (msSet.contains(Incizing)) {
				outPacket.encodeInt(stats.get(Incizing).wOption);
				outPacket.encodeInt(stats.get(Incizing).uOption);
				outPacket.encodeInt(stats.get(Incizing).pOption);
			}
			if (msSet.contains(Speed)) {
				outPacket.encodeByte(stats.get(Speed).mOption);
			}
			if (msSet.contains(BMageDebuff)) {
				outPacket.encodeInt(stats.get(BMageDebuff).cOption);
			}
			if (msSet.contains(DarkLightning)) {
				outPacket.encodeInt(stats.get(DarkLightning).cOption);
			}
			if (msSet.contains(BattlePvPHelenaMark)) {
				outPacket.encodeInt(stats.get(BattlePvPHelenaMark).cOption);
			}
			if (msSet.contains(MultiPMDR)) {
				outPacket.encodeInt(stats.get(MultiPMDR).cOption);
			}
			if (msSet.contains(Freeze)) {
				outPacket.encodeInt(stats.get(Freeze).cOption);
			}
			if (msSet.contains(BurnedInfo)) {
				Set<BurnedInfo> bis = getAllBurnedInfos();
				outPacket.encodeByte(bis.size());
				for (BurnedInfo bi : bis) {
					bi.encode(outPacket);
				}
			}
			if (msSet.contains(InvincibleBalog)) {
				outPacket.encodeByte(stats.get(InvincibleBalog).nOption);
				outPacket.encodeByte(stats.get(InvincibleBalog).bOption);
			}
			if (msSet.contains(ExchangeAttack)) {
				outPacket.encodeByte(stats.get(ExchangeAttack).bOption);
			}
			if (msSet.contains(AddDamParty)) {
				outPacket.encodeInt(stats.get(AddDamParty).wOption);
				outPacket.encodeInt(stats.get(AddDamParty).pOption);
				outPacket.encodeInt(stats.get(AddDamParty).cOption);
			}
			if (msSet.contains(LinkTeam)) {
				outPacket.encodeString(getLinkTeam());
			}
			if (msSet.contains(SoulExplosion)) {
				outPacket.encodeInt(stats.get(SoulExplosion).nOption);
				outPacket.encodeInt(stats.get(SoulExplosion).rOption);
				outPacket.encodeInt(stats.get(SoulExplosion).wOption);
			}
			if (msSet.contains(SeperateSoulP)) {
				outPacket.encodeInt(stats.get(SeperateSoulP).nOption);
				outPacket.encodeInt(stats.get(SeperateSoulP).rOption);
				outPacket.encodeShort(stats.get(SeperateSoulP).tOption / 500);
				outPacket.encodeInt(stats.get(SeperateSoulP).wOption);
				outPacket.encodeInt(stats.get(SeperateSoulP).uOption);
			}
			if (msSet.contains(SeperateSoulC)) {
				outPacket.encodeInt(stats.get(SeperateSoulC).nOption);
				outPacket.encodeInt(stats.get(SeperateSoulC).rOption);
				outPacket.encodeShort(stats.get(SeperateSoulC).tOption / 500);
				outPacket.encodeInt(stats.get(SeperateSoulC).wOption);
			}
			if (msSet.contains(Ember)) {
				outPacket.encodeInt(stats.get(Ember).nOption);
				outPacket.encodeInt(stats.get(Ember).rOption);
				outPacket.encodeInt(stats.get(Ember).wOption);
				outPacket.encodeInt(stats.get(Ember).tOption / 500);
				outPacket.encodeInt(stats.get(Ember).uOption);
			}
			if (msSet.contains(TrueSight)) {
				outPacket.encodeInt(stats.get(TrueSight).nOption);
				outPacket.encodeInt(stats.get(TrueSight).rOption);
				outPacket.encodeInt(stats.get(TrueSight).tOption / 500);
				outPacket.encodeInt(stats.get(TrueSight).cOption);
				outPacket.encodeInt(stats.get(TrueSight).pOption);
				outPacket.encodeInt(stats.get(TrueSight).uOption);
				outPacket.encodeInt(stats.get(TrueSight).wOption);
			}
			if (msSet.contains(MultiDamSkill)) {
				outPacket.encodeInt(stats.get(MultiDamSkill).cOption);
			}
			if (msSet.contains(Laser)) {
				outPacket.encodeInt(stats.get(Laser).nOption);
				outPacket.encodeInt(stats.get(Laser).rOption);
				outPacket.encodeInt(stats.get(Laser).tOption / 500);
				outPacket.encodeInt(stats.get(Laser).wOption);
				outPacket.encodeInt(stats.get(Laser).uOption);
			}
			if (msSet.contains(ElementResetBySummon)) {
				outPacket.encodeInt(stats.get(ElementResetBySummon).cOption);
				outPacket.encodeInt(stats.get(ElementResetBySummon).pOption);
				outPacket.encodeInt(stats.get(ElementResetBySummon).uOption);
				outPacket.encodeInt(stats.get(ElementResetBySummon).wOption);
			}
			if (msSet.contains(BahamutLightElemAddDam)) {
				outPacket.encodeInt(stats.get(BahamutLightElemAddDam).pOption);
				outPacket.encodeInt(stats.get(BahamutLightElemAddDam).cOption);
			}

			if (clear) {
				stats.clear();
			}
		} finally {
			getSetStatLock().unlock();
		}
	}

	private int[] getMaskByCollection(Map<MobStat, Option> map) {
		int[] res = new int[3];
		for (MobStat mobStat : map.keySet()) {
			res[mobStat.getPos()] |= mobStat.getVal();
		}
		return res;
	}

	public int[] getNewMask() {
		return getMaskByCollection(getNewStatVals());
	}

	public int[] getCurrentMask() {
		return getMaskByCollection(getCurrentStatVals());
	}

	public int[] getRemovedMask() {
		return getMaskByCollection(getRemovedStatVals());
	}

	public boolean hasNewMobStat(MobStat mobStat) {
		return getNewStatVals().keySet().contains(mobStat);
	}

	public boolean hasCurrentMobStat(MobStat mobStat) {
		return getCurrentStatVals().keySet().contains(mobStat);
	}

	public boolean hasCurrentMobStatBySkillId(int skillId) {
		return getCurrentStatVals().entrySet().stream().anyMatch(map -> map.getValue().rOption == skillId);
	}

	public boolean hasBurnFromSkillAndOwner(int skillID, int ownerCID) {
		return getBurnBySkillAndOwner(skillID, ownerCID) != null;
	}

	/**
	 * Checks if this MTS has a burn skill by a given skillID and owner character id. If the given owner's id is 0,
	 * ignores the given owner id.
	 * @param skillID the skill id of which the burn should exist
	 * @param ownerCID the burn skill's owner's id that should match (or 0 if ignored)
	 * @return the BurnedInfo of the burn on this MTS, or null if there is none
	 */
	public BurnedInfo getBurnBySkillAndOwner(int skillID, int ownerCID) {
		List<BurnedInfo> bis = getBurnsBySkillAndOwner(skillID, ownerCID);
		return bis.size() == 0 ? null : bis.get(0);
	}

	private List<BurnedInfo> getBurnsBySkillAndOwner(int skillID, int ownerCID) {
		return getBurnedInfos().getOrDefault(ownerCID, new HashMap<>()).getOrDefault(skillID, new ArrayList<>());
	}

	public Set<BurnedInfo> getBurnsFromOwner(int chrId) {
		Set<BurnedInfo> bis = new HashSet<>();
		Map<Integer, List<BurnedInfo>> chrBis = getBurnedInfos().get(chrId);
		if (chrBis != null) {
			for (List<BurnedInfo> biList : chrBis.values()) {
				bis.addAll(biList);
			}
		}
		return bis;
	}

	public boolean hasBurnFromOwner(int ownerCID) {
		return getBurnsFromOwner(ownerCID).size() > 0;
	}

	public boolean hasRemovedMobStat(MobStat mobStat) {
		return getRemovedStatVals().containsKey(mobStat);
	}

	public Map<MobStat, Option> getCurrentStatVals() {
		return currentStatVals;
	}

	public Map<MobStat, Option> getNewStatVals() {
		return newStatVals;
	}

	public Map<MobStat, Option> getRemovedStatVals() {
		return removedStatVals;
	}

	public Lock getSetStatLock() {
		return setStatLock;
	}

	public Lock getRemoveStatLock() {
		return removeStatLock;
	}

	public ReadWriteLock getBurnedInfoLock() {
		return burnedInfoLock;
	}

	public void removeMobStat(MobStat mobStat, boolean fromSchedule) {
		if (!hasCurrentMobStat(mobStat)) {
			return;
		}
		getRemoveStatLock().lock();
		try {
			getRemovedStatVals().put(mobStat, getCurrentStatVals().get(mobStat));
		} finally {
			getRemoveStatLock().unlock();
		}
		getSetStatLock().lock();
		try {
			getCurrentStatVals().remove(mobStat);
		} finally {
			getSetStatLock().unlock();
		}
		getMob().getField().broadcastPacket(MobPool.statReset(getMob(), (byte) 1, false));
		getSchedules().remove(mobStat);
		if (!fromSchedule && getSchedules().containsKey(mobStat)) {
			getSchedules().get(mobStat).cancel(true);
			getSchedules().remove(mobStat);
		} else {
			getSchedules().remove(mobStat);
		}
	}

	/**
	 * Removes a BurnedInfo from this MTS, removes its schedules, notifies the Char of its damage, and broadcasts the
	 * changes to the Field.
	 *
	 * @param burnedInfo the BurnedInfo to delete
	 * @param fromSchedule whether or not this BurnedInfo was deleted according to its schedule
	 */
	public void removeBurnedInfo(BurnedInfo burnedInfo, boolean fromSchedule) {
		Char chr = burnedInfo.getChr();
		removeBurnedInfo(burnedInfo); // removes it properly from the inner list (thread safe)
		Option burnedOption = getCurrentOptionsByMobStat(BurnedInfo);
		if (burnedOption == null) {
			return;
		}
		getRemoveStatLock().lock();
		try {
			getRemovedStatVals().put(BurnedInfo, burnedOption);
		} finally {
			getRemoveStatLock().unlock();
		}
		getSetStatLock().lock();
		try {
			if (getBurnedInfos().size() == 0) {
				getCurrentStatVals().remove(BurnedInfo);
			}
		} finally {
			getSetStatLock().unlock();
		}
		Set<BurnedInfo> bis = getAllBurnedInfos();
		getMob().getField().broadcastPacket(MobPool.statReset(getMob(), (byte) 1, false, bis));
		if (chr.isBattleRecordOn()) {
			int count = Math.min(
					burnedInfo.getDotCount(),
					(Util.getCurrentTime() - burnedInfo.getStartTime()) / burnedInfo.getInterval()
			);
			chr.write(BattleRecordMan.dotDamageInfo(burnedInfo, count));
		}
		if (!fromSchedule) {
			getBurnCancelSchedules().get(burnedInfo).cancel(true);
			getBurnSchedules().get(burnedInfo).cancel(true);
		}
		getBurnCancelSchedules().remove(burnedInfo);
		getBurnSchedules().remove(burnedInfo);
	}

	/**
	 * Removes a BurnedInfo from a Char's burnedinfo list. Ensures the containing map/lists are removed if their size
	 * reaches 0.
	 *
	 * @param burnedInfo the BurnedInfo to remove
	 */
	private void removeBurnedInfo(BurnedInfo burnedInfo) {
		int chrId = burnedInfo.getCharacterId();
		int skillId = burnedInfo.getSkillId();
		getBurnedInfoLock().writeLock().lock();
		try {
			Map<Integer, List<BurnedInfo>> chrBis = getBurnedInfos().get(chrId);
			int size = -1;
			if (chrBis != null && chrBis.containsKey(skillId)) {
				List<BurnedInfo> bis = chrBis.get(skillId);
				bis.remove(burnedInfo);
				size = bis.size();
				if (bis.size() == 0) {
					chrBis.remove(skillId);
				}
			}
			if (chrBis != null && chrBis.size() == 0) {
				getBurnedInfos().remove(chrId);
			}
		} finally {
			getBurnedInfoLock().writeLock().unlock();
		}
	}

	private void addBurnedInfo(BurnedInfo bi) {
		int chrId = bi.getCharacterId();
		int skillId = bi.getSkillId();
		getBurnedInfoLock().writeLock().lock();
		try {
			if (!getBurnedInfos().containsKey(chrId)) {
				getBurnedInfos().put(chrId, new HashMap<>());
			}
			Map<Integer, List<BurnedInfo>> chrBis = getBurnedInfos().get(chrId);
			if (!chrBis.containsKey(skillId)) {
				chrBis.put(skillId, new ArrayList<>());
			}
			List<BurnedInfo> bis = chrBis.get(skillId);
			bis.add(bi);
			for (int i = 0; i < bis.size(); i++) {
				bis.get(i).setSuperPos(i);
			}
		} finally {
			getBurnedInfoLock().writeLock().unlock();
		}
	}

	/**
	 * Adds a new MobStat to this MobTemporaryStat. Will immediately broadcast the reaction to all
	 * clients.
	 * Only works for user skills, not mob skills. For the latter, use {@link
	 * #addMobSkillOptionsAndBroadCast(MobStat, Option)}.
	 *
	 * @param mobStat
	 * 		The MobStat to add.
	 * @param option
	 * 		The Option that contains the values of the stat.
	 */
	public void addStatOptionsAndBroadcast(MobStat mobStat, Option option) {
		addStatOptions(mobStat, option);
		getMob().getField().broadcastPacket(MobPool.statSet(getMob(), (short) 0));
	}

	/**
	 * Adds a new MobStat to this MobTemporary stat. Will immediately broadcast the reaction to all
	 * clients.
	 * Only works for mob skills, not user skills. For the latter, use {@link
	 * #addStatOptionsAndBroadcast(MobStat, Option)}.
	 *
	 * @param mobStat
	 * 		The MobStat to add.
	 * @param o
	 * 		The option that contains the values of the stat.
	 */
	public void addMobSkillOptionsAndBroadCast(MobStat mobStat, Option o) {
		o.rOption |= o.slv << 16; // mob skills are encoded differently: not an int, but short (skill ID), then short (slv).
		addStatOptionsAndBroadcast(mobStat, o);
	}

	public void addStatOptions(MobStat mobStat, Option option) {
		int tAct = option.tOption > 0 ? option.tOption : option.tTerm;
		if (!option.isInMillis()) {
			tAct *= 1000;
		}
		getSetStatLock().lock();
		try {
			getNewStatVals().put(mobStat, option);
			getCurrentStatVals().put(mobStat, option);
		} finally {
			getSetStatLock().unlock();
		}
		if (tAct > 0 && mobStat != BurnedInfo) {
			if (getSchedules().containsKey(mobStat)) {
				getSchedules().get(mobStat).cancel(true);
			}
			ScheduledFuture sf = EventManager.addEvent(() -> removeMobStat(mobStat, true), tAct);
			getSchedules().put(mobStat, sf);
		}
	}

	public Set<BurnedInfo> getAllBurnedInfos() {
		Set<BurnedInfo> bis = new HashSet<>();
		getBurnedInfoLock().readLock().lock();
		try {
			for (Map<Integer, List<BurnedInfo>> chrBis : getBurnedInfos().values()) {
				for (List<BurnedInfo> biList : chrBis.values()) {
					bis.addAll(biList);
				}
			}
			return bis;
		} finally {
			getBurnedInfoLock().readLock().unlock();
		}
	}

	public Map<Integer, Map<Integer, List<BurnedInfo>>> getBurnedInfos() {
		return burnedInfos;
	}

	public Comparator getMobStatComper() {
		return mobStatComper;
	}

	public String getLinkTeam() {
		return linkTeam;
	}

	public void setLinkTeam(String linkTeam) {
		this.linkTeam = linkTeam;
	}

	public boolean hasNewMovementAffectingStat() {
		return getNewStatVals().keySet().stream().anyMatch(MobStat::isMovementAffectingStat);
	}

	public boolean hasCurrentMovementAffectingStat() {
		return getCurrentStatVals().keySet().stream().anyMatch(MobStat::isMovementAffectingStat);
	}

	public boolean hasRemovedMovementAffectingStat() {
		return getRemovedStatVals().keySet().stream().anyMatch(MobStat::isMovementAffectingStat);
	}

	public Map<MobStat, ScheduledFuture> getSchedules() {
		return schedules;
	}

	public Mob getMob() {
		return mob;
	}

	public void setMob(Mob mob) {
		this.mob = mob;
	}

	public void clear() {
		for (ScheduledFuture t : getBurnSchedules().values()) {
			t.cancel(true);
		}
		getBurnSchedules().clear();
		for (ScheduledFuture t : getBurnCancelSchedules().values()) {
			t.cancel(true);
		}
		getBurnCancelSchedules().clear();
		for (ScheduledFuture t : getSchedules().values()) {
			t.cancel(true);
		}
		getSchedules().clear();
		getCurrentStatVals().forEach((ms, o) -> removeMobStat(ms, false));
	}

	public void createAndAddBurnedInfo(Char chr, Skill skill) {
		createAndAddBurnedInfo(chr, skill.getSkillId(), skill.getCurrentLevel());
	}

	public void createAndAddBurnedInfo(Char chr, int skillId, int slv) {
		createAndAddBurnedInfo(chr, skillId, slv, 1);
	}

	public void createAndAddBurnedInfo(Char chr, int skillId, int slv, int maxDotStacks) {
		SkillInfo si = SkillData.getSkillInfoById(skillId);
		int dotDmg = si.getValue(SkillStat.dot, slv);
		int dotInterval = si.getValue(SkillStat.dotInterval, slv);
		if (dotInterval <= 0) {
			dotInterval = 1;
		}
		int dotTime = si.getValue(SkillStat.dotTime, slv);
		createAndAddBurnedInfo(chr, skillId, slv, dotDmg, dotInterval, dotTime, maxDotStacks);
	}

	/**
	 * Creates a BurnedInfo from the given arguments. Will go until a max amount of stacks, and will override the oldest
	 * BurnedInfo if the max is reached.
	 *
	 * @param chr The Char who is the creator of the BurnedInfo
	 * @param skillId the skillId of the used skill
	 * @param slv the slv of the used skill
	 * @param dotDmg the damage % of the skill
	 * @param dotInterval the interval (seconds) of the DoT
	 * @param dotTime the amount of time the DoT takes
	 * @param maxDotStacks the max amount of DoT stacks of one skillId + chr combination
	 */
	public void createAndAddBurnedInfo(Char chr, int skillId, int slv, int dotDmg, int dotInterval, int dotTime, int maxDotStacks) {
		int duration = dotTime * 1000;
		// update existing burned info
		List<BurnedInfo> bis = getBurnsBySkillAndOwner(skillId, chr.getId());
		if (bis != null && bis.size() >= maxDotStacks) {
			BurnedInfo toRemove = bis.get(0);
			removeBurnedInfo(toRemove, false);
		}
		for (BurnedInfo ebi : bis) {
			ScheduledFuture sf = getBurnCancelSchedules().getOrDefault(ebi, null);
			ScheduledFuture burn = getBurnSchedules().getOrDefault(ebi, null);
			boolean toRefresh = false;
			if (sf != null && !sf.isDone()) {
				sf.cancel(true);
				toRefresh = true;
			}
			if (burn != null && !burn.isDone()) {
				burn.cancel(true);
				toRefresh = true;
			}
			if (toRefresh) {
				sf = EventManager.addEvent(() -> removeBurnedInfo(ebi, true), duration);
				burn = EventManager.addFixedRateEvent(
						() -> burnDamage(ebi.getChr(), getMob(), ebi), ebi.getAttackDelay() + ebi.getInterval(), ebi.getInterval(), ebi.getDotCount());
				getBurnCancelSchedules().put(ebi, sf);
				getBurnSchedules().put(ebi, burn);
			}
		}
		// add burned info
		BurnedInfo bi = new BurnedInfo();
		bi.setCharacterId(chr.getId());
		bi.setChr(chr);
		bi.setSkillId(skillId);
		bi.setDamage((int) (chr.getDamageCalc().getMaxBaseDamage() * ((double) dotDmg / 100D)));
		bi.setInterval(dotInterval * 1000);
		bi.setDotCount(duration / bi.getInterval());
		bi.setAttackDelay(0);
		bi.setDotTickIdx(0);
		bi.setDotTickDamR(0); //damage added for every tick
		bi.setDotAnimation(bi.getAttackDelay() + bi.getInterval() + duration);
		bi.setStartTime(Util.getCurrentTime());
		bi.setLastUpdate(Util.getCurrentTime());
		bi.setEnd(Util.getCurrentTime() + duration);
		addBurnedInfo(bi);
		Option o = new Option();
		o.nOption = 0;
		o.rOption = skillId;
		addStatOptionsAndBroadcast(MobStat.BurnedInfo, o);
		ScheduledFuture sf = EventManager.addEvent(() -> removeBurnedInfo(bi, true), duration);
		ScheduledFuture burn = EventManager.addFixedRateEvent(
				() -> burnDamage(chr, getMob(), bi), bi.getAttackDelay() + bi.getInterval(), bi.getInterval(), bi.getDotCount());
		getBurnCancelSchedules().put(bi, sf);
		getBurnSchedules().put(bi, burn);
	}

	public void burnDamage(Char chr, Mob mob, BurnedInfo bi) {
		mob.damage(chr, bi.getDamage());
		chr.getJobHandler().handleMobBurn(mob, bi);
	}

	public Map<BurnedInfo, ScheduledFuture> getBurnCancelSchedules() {
		return burnCancelSchedules;
	}

	public Map<BurnedInfo, ScheduledFuture> getBurnSchedules() {
		return burnSchedules;
	}

	public void removeBuffs() {
		removeMobStat(PowerUp, false);
		removeMobStat(MagicUp, false);
		removeMobStat(PGuardUp, false);
		removeMobStat(MGuardUp, false);
		removeMobStat(PImmune, false);
		removeMobStat(MImmune, false);
		removeMobStat(PCounter, false);
		removeMobStat(MCounter, false);
		if (hasCurrentMobStat(ACC) && getCurrentOptionsByMobStat(ACC).nOption > 0) {
			removeMobStat(ACC, false);
		}
		if (hasCurrentMobStat(EVA) && getCurrentOptionsByMobStat(EVA).nOption > 0) {
			removeMobStat(EVA, false);
		}
		getMob().getField().broadcastPacket(MobPool.statReset(getMob(), (byte) 0, false));
	}
}
