package net.swordie.ms.life.mob;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.BattleRecordMan;
import net.swordie.ms.life.mob.skill.BurnedInfo;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.connection.packet.MobPool;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static net.swordie.ms.life.mob.MobStat.*;


public class MobTemporaryStat {
	private static final Logger log = LogManager.getLogger(MobTemporaryStat.class);
	private final Map<MobStat, Option> stats = new ConcurrentHashMap<>();
	private final Map<Integer, Map<Integer, List<BurnedInfo>>> burnedInfos = new ConcurrentHashMap<>(); // chrId -> skillId -> bis
	private final Map<MobStat, ScheduledFuture> statSchedules = new ConcurrentHashMap<>();
	private final Map<BurnedInfo, ScheduledFuture> burnSchedules = new ConcurrentHashMap<>();
	private final Map<BurnedInfo, ScheduledFuture> burnResetSchedules = new ConcurrentHashMap<>();
	private final MobStatMask setStatMask = new MobStatMask();
	private final MobStatMask resetStatMask = new MobStatMask();
	private final Lock lock = new ReentrantLock();
	private Mob mob;

	public MobTemporaryStat(Mob mob) {
		this.mob = mob;
	}

	public MobTemporaryStat deepCopy() {
		MobTemporaryStat copy = new MobTemporaryStat(getMob());
		for (BurnedInfo bi : getAllBurnedInfos()) {
			copy.addBurnedInfo(bi);
		}
		for (MobStat ms : stats.keySet()) {
			copy.addStatOptions(ms, stats.get(ms).deepCopy());
		}
		return copy;
	}

	public Mob getMob() {
		return mob;
	}

	public void setMob(Mob mob) {
		this.mob = mob;
	}



	// MOB STAT INTERFACE ----------------------------------------------------------------------------------------------

	public Map<MobStat, Option> getCurrentStats() {
		return stats;
	}

	public boolean hasStat(MobStat ms) {
		return stats.containsKey(ms);
	}

	public Option getOption(MobStat ms) {
		return stats.getOrDefault(ms, new Option());
	}

	public boolean hasBurnFromOwner(int chrId) {
		return hasStat(BurnedInfo) && burnedInfos.containsKey(chrId);
	}

	public boolean hasBurnFromSkillAndOwner(int skiillId, int chrId) {
		return hasStat(BurnedInfo) && burnedInfos.containsKey(chrId) && burnedInfos.get(chrId).containsKey(skiillId);
	}

	public List<BurnedInfo> getBurnsBySkillAndOwner(int skillId, int chrId) {
		if (!hasStat(BurnedInfo) || !burnedInfos.containsKey(chrId)) {
			return List.of();
		}
		List<BurnedInfo> burns = new ArrayList<>();
		for (Map.Entry<Integer, List<BurnedInfo>> entry : burnedInfos.get(chrId).entrySet()) {
			if (entry.getKey() == skillId) {
				burns.addAll(entry.getValue());
			}
		}
		return burns;
	}

	public List<BurnedInfo> getAllBurnedInfos() {
		if (!hasStat(BurnedInfo)) {
			return List.of();
		}
		List<BurnedInfo> burns = new ArrayList<>();
		for (var chrBis : burnedInfos.values()) {
			for (List<BurnedInfo> bis : chrBis.values()) {
				burns.addAll(bis);
			}
		}
		return burns;
	}

	public void addStatOptions(MobStat ms, Option option) {
		assert ms != BurnedInfo;
		int tAct = option.tOption > 0 ? option.tOption : option.tTerm;
		if (!option.isInMillis()) {
			tAct *= 1000;
		}
		lock.lock();
		try {
			setStatMask.put(ms);
			stats.put(ms, option);
			if (statSchedules.containsKey(ms)) {
				statSchedules.remove(ms).cancel(false);
			}
			if (tAct > 0) {
				statSchedules.put(ms, EventManager.addEvent(() -> removeMobStat(ms, true), tAct));
			}
		} finally {
			lock.unlock();
		}
	}

	public void addStatOptionsAndBroadcast(MobStat ms, Option option) {
		addStatOptions(ms, option);
		sendSetStatPacket();
	}

	public void addMobSkillOptionsAndBroadCast(MobStat ms, Option option) {
		option.rOption |= option.slv << 16; // mob skills are encoded differently: not an int, but short (skill ID), then short (slv).
		addStatOptionsAndBroadcast(ms, option);
	}

	private void addBurnedInfo(BurnedInfo bi) {
		int chrId = bi.getCharacterId();
		int skillId = bi.getSkillId();
		lock.lock();
		try {
			if (!burnedInfos.containsKey(chrId)) {
				burnedInfos.put(chrId, new HashMap<>());
			}
			var chrBis = burnedInfos.get(chrId);
			if (!chrBis.containsKey(skillId)) {
				chrBis.put(skillId, new ArrayList<>());
			}
			List<BurnedInfo> bis = chrBis.get(skillId);
			bis.add(bi);
			for (int i = 0; i < bis.size(); i++) {
				bis.get(i).setSuperPos(i);
			}
		} finally {
			lock.unlock();
		}
	}

	public void removeBurnedInfo(BurnedInfo bi, boolean sendPacket) {
		int chrId = bi.getCharacterId();
		int skillId = bi.getSkillId();
		if (!hasStat(BurnedInfo) || !burnedInfos.containsKey(chrId) || !burnedInfos.get(chrId).containsKey(skillId)) {
			return;
		}
		lock.lock();
		try {
			resetStatMask.put(BurnedInfo);
			List<BurnedInfo> bis = burnedInfos.get(chrId).get(skillId);
			bis.removeIf(burnedInfo -> burnedInfo.equals(bi));
			// clean up burnedInfos map
			if (bis.size() == 0) {
				burnedInfos.get(chrId).remove(skillId);
			}
			if (burnedInfos.get(chrId).size() == 0) {
				burnedInfos.remove(chrId);
			}
			if (burnedInfos.size() == 0) {
				stats.remove(BurnedInfo);
			}
			// clean up schedule
			if (burnSchedules.containsKey(bi)) {
				burnSchedules.remove(bi).cancel(false);
			}
			if (burnResetSchedules.containsKey(bi)) {
				burnResetSchedules.remove(bi).cancel(false);
			}
		} finally {
			lock.unlock();
		}
		// send packet
		if (sendPacket) {
			mob.getField().broadcastPacket(MobPool.statReset(mob, (byte) 1, false, getAllBurnedInfos()));
		}
		if (bi.getChr().isBattleRecordOn()) {
			int count = Math.min(
					bi.getDotCount(),
					(Util.getCurrentTime() - bi.getStartTime()) / bi.getInterval()
			);
			bi.getChr().write(BattleRecordMan.dotDamageInfo(bi, count));
		}
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

	public void createAndAddBurnedInfo(Char chr, int skillId, int slv, int dotDmg, int dotInterval, int dotTime, int maxDotStacks) {
		int chrId = chr.getId();
		int duration = dotTime * 1000;
		// remove the oldest existing burned info
		List<BurnedInfo> bis = getBurnsBySkillAndOwner(skillId, chrId);
		if (bis.size() >= maxDotStacks) {
			removeBurnedInfo(bis.get(0), true);
			sendResetStatPacket();
		}
		// create burned info
		BurnedInfo bi = new BurnedInfo();
		bi.setCharacterId(chrId);
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
		// add mobstat
		Option o1 = new Option();
		o1.nOption = 0;
		o1.rOption = skillId;
		lock.lock();
		try {
			setStatMask.put(BurnedInfo);
			stats.put(BurnedInfo, o1);
			burnSchedules.put(bi, EventManager.addFixedRateEvent(() -> burnDamage(chr, mob, bi), bi.getAttackDelay() + bi.getInterval(), bi.getInterval(), bi.getDotCount()));
			burnResetSchedules.put(bi, EventManager.addEvent(() -> removeBurnedInfo(bi, true), duration));
		} finally {
			lock.unlock();
		}
		// send packet
		sendSetStatPacket();
	}

	private static void burnDamage(Char chr, Mob mob, BurnedInfo bi) {
		mob.damage(chr, bi.getDamage());
		chr.getJobHandler().handleMobBurn(mob, bi);
	}

	public void removeMobStat(MobStat ms, boolean sendPacket) {
		if (!hasStat(ms)) {
			return;
		}
		lock.lock();
		try {
			resetStatMask.put(ms);
			stats.remove(ms);
			statSchedules.remove(ms);
		} finally {
			lock.unlock();
		}
		if (sendPacket) {
			sendResetStatPacket();
		}
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
		if (stats.containsKey(ACC) && stats.get(ACC).nOption > 0) {
			removeMobStat(ACC, false);
		}
		if (stats.containsKey(EVA) && stats.get(EVA).nOption > 0) {
			removeMobStat(EVA, false);
		}
		sendResetStatPacket();
	}




	// PACKET SENDING INTERFACE ----------------------------------------------------------------------------------------

	public void sendSetStatPacket() {
		lock.lock();
		try {
			if (!setStatMask.isEmpty()) {
				mob.getField().broadcastPacket(MobPool.statSet(mob, (short) 0));
				setStatMask.clear();
			}
		} finally {
			lock.unlock();
		}
	}

	public void sendResetStatPacket() {
		lock.lock();
		try {
			if (!resetStatMask.isEmpty()) {
				mob.getField().broadcastPacket(MobPool.statReset(mob, (byte) 1, false));
				resetStatMask.clear();
			}
		} finally {
			lock.unlock();
		}
	}

	public MobStatMask getResetStatMask() {
		return resetStatMask;
	}

	public boolean hasNewMovingAffectingStat() {
		return MOVING_AFFECTING_STAT.stream().anyMatch(setStatMask::has);
	}

	public boolean hasRemovedMovingAffectingStat() {
		return MOVING_AFFECTING_STAT.stream().anyMatch(resetStatMask::has);
	}

	public void encode(OutPacket outPacket, boolean enterField) {
		MobStatMask statMask = enterField ? MobStatMask.from(stats.keySet()) : setStatMask;
		statMask.encode(outPacket);
		List<MobStat> mobStatList = statMask.sorted();
		for (MobStat ms : mobStatList) {
			switch (ms) {
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
					outPacket.encodeInt(stats.get(ms).nOption);
					outPacket.encodeInt(stats.get(ms).rOption);
					outPacket.encodeShort(stats.get(ms).tOption / 500);
					break;
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
				List<BurnedInfo> bis = getAllBurnedInfos();
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
				outPacket.encodeString(""); // LinkTeam
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
		}
	}

}
