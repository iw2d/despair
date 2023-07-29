package net.swordie.ms.client.jobs.legend;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatBase;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Dragon;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.life.Life;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobTemporaryStat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.client.character.skills.SkillStat.*;

/**
 * Created on 12/14/2017.
 */
public class Evan extends Job {

    public static final int INHERITED_WILL = 20010194;
    public static final int BACK_TO_NATURE = 20011293;
    public static final int RECOVERY = 20011001; // Buff
    public static final int NIMBLE_FEET = 20011002; // Buff

    public static final int MAGIC_GUARD = 22001012; // Buff

    public static final int MAGIC_BOOSTER = 22111020; // Buff
    public static final int ELEMENTAL_DECREASE = 22141016; // Buff
    public static final int PARTNERS = 22110016;
    public static final int SPELL_MASTERY = 22110018;
    public static final int CRITICAL_MAGIC = 22140018;
    public static final int MAGIC_AMPLIFICATION = 22140020;

    public static final int BLESSING_OF_THE_ONYX = 22171073; // Buff
    public static final int MAPLE_WARRIOR_EVAN = 22171000; // Buff
    public static final int MAGIC_DEBRIS = 22141017;

    public static final int DRAGON_MASTER = 22171080; // Mount
    public static final int DRAGON_MASTER_2 = 22171083; // Add-on
    public static final int SUMMON_ONYX_DRAGON = 22171081; // Summon
    public static final int HEROIC_MEMORIES_EVAN = 22171082;
    public static final int ENHANCED_MAGIC_DEBRIS = 22170070;
    public static final int HEROS_WILL_EVAN = 22171004;
    public static final int DRAGON_FURY = 22170074;
    public static final int MAGIC_MASTERY = 22170071;

    // Returns
    public static final int RETURN_FLASH = 22110013; // Return after Wind Skills (Mob Debuff)
    public static final int RETURN_DIVE = 22140013; // Return Dive (Buff)
    public static final int RETURN_FLAME = 22170064; // Return Flame (Flame  AoE)
    public static final int RETURN_FLAME_TILE = 22170093; // Return Flames Tile

    // Dragon Attacks
    public static final int DRAGON_FLASH = 22111012;
    public static final int DRAGON_FLASH_3 = 22110022;
    public static final int DRAGON_FLASH_4 = 22110023;
    public static final int DRAGON_DIVE = 22141012;
    public static final int DRAGON_DIVE_4 = 22140022;
    public static final int DRAGON_BREATH = 22171063;


    // Evan Attacks
    public static final int MANA_BURST_I = 22001010;
    public static final int MANA_BURST_II = 22110010;
    public static final int MANA_BURST_III = 22140010;
    public static final int MANA_BURST_IV_1 = 22170060;
    public static final int MANA_BURST_IV_2 = 22170061;
    public static final int WIND_CIRCLE = 22111011;
    public static final int THUNDER_CIRCLE = 22141011;
    public static final int EARTH_CIRCLE = 22171062;
    public static final int DARK_FOG = 22171095;

    //Final Attack
    public static final int DRAGON_SPARK = 22000015;
    public static final int ADV_DRAGON_SPARK = 22110021;

    private final Map<Integer, Position> debrisPos = new ConcurrentHashMap<>(); // ForceAtomKey : Position
    private final Lock debrisLock = new ReentrantLock();
    private Dragon dragon;

    private int[] addedSkills = new int[] {
            BACK_TO_NATURE,
    };

    public Evan(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            for (int id : addedSkills) {
                if (!chr.hasSkill(id)) {
                    Skill skill = SkillData.getSkillDeepCopyById(id);
                    skill.setCurrentLevel(skill.getMasterLevel());
                    chr.addSkill(skill);
                }
            }
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isEvan(id);
    }


    public Dragon getDragon() {
        if (dragon == null && chr.getJob() != JobConstants.JobEnum.EVAN.getJobId()) {
            dragon = new Dragon(0);
            dragon.setOwner(chr);
        }
        return dragon;
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
            if (SkillConstants.isEvanFusionSkill(attackInfo.skillId)) {
                handleDebris(attackInfo);
                handlePartnerBuff();
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case DRAGON_FLASH:
            case DRAGON_FLASH_3:
            case DRAGON_FLASH_4:
                if (!chr.hasSkillOnCooldown(DRAGON_FLASH)) {
                    chr.setSkillCooldown(DRAGON_FLASH, slv);
                }
                break;
            case DRAGON_DIVE:
            case DRAGON_DIVE_4:
                if (!chr.hasSkillOnCooldown(DRAGON_DIVE)) {
                    chr.setSkillCooldown(DRAGON_DIVE, slv);
                }
                break;
            case DRAGON_BREATH:
                if (!chr.hasSkillOnCooldown(DRAGON_BREATH)) {
                    chr.setSkillCooldown(DRAGON_BREATH, slv);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void handleDebris(AttackInfo attackInfo) {
        int skillId = 0;
        if (chr.hasSkill(ENHANCED_MAGIC_DEBRIS)) {
            skillId = ENHANCED_MAGIC_DEBRIS;
        } else if (chr.hasSkill(MAGIC_DEBRIS)) {
            skillId = MAGIC_DEBRIS;
        }
        if (skillId == 0) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        int duration = si.getValue(time, slv);
        int maxDebris = si.getValue(x, slv);
        debrisLock.lock();
        try {
            List<Integer> addedList = new ArrayList<>();
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                int curDebris = debrisPos.size();
                if (curDebris >= maxDebris) {
                    break;
                }
                int debrisId = chr.getNewForceAtomKey();
                addedList.add(debrisId);
                debrisPos.put(debrisId, mob.getPosition());
                chr.write(FieldPacket.addWreckage(chr, mob, skillId, duration, debrisId, curDebris));
            }
            EventManager.addEvent(() -> handleRemoveDebris(addedList, false), duration, TimeUnit.MILLISECONDS);
        } finally {
            debrisLock.unlock();
        }
    }

    private void handleRemoveDebris(List<Integer> debrisList, boolean sendPacket) {
        debrisLock.lock();
        try {
            List<Integer> removeList = new ArrayList<>();
            for (int debrisId : debrisList) {
                if (debrisPos.remove(debrisId) == null) {
                    continue;
                }
                removeList.add(debrisId);
            }
            if (sendPacket && removeList.size() > 0) {
                chr.write(FieldPacket.delWreckage(chr, removeList));
            }
        } finally {
            debrisLock.unlock();
        }
    }

    private void createMagicDebrisForceAtom(int skillId) {
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        Rect rect = chr.getPosition().getRectAround(si.getFirstRect());
        List<Mob> targets =  chr.getField().getMobsInRect(rect);
        if (targets.size() == 0) {
            return;
        }
        if (debrisPos.size() == 0) {
            return;
        }
        debrisLock.lock();
        try {
            ForceAtomEnum atomEnum = skillId == ENHANCED_MAGIC_DEBRIS ? ForceAtomEnum.ADV_WRECKAGE : ForceAtomEnum.WRECKAGE;
            List<ForceAtomInfo> faiList = new ArrayList<>();
            List<Integer> targetList = new ArrayList<>();
            List<Integer> removeList = new ArrayList<>();
            for (int debrisId : debrisPos.keySet()) {
                Position source = debrisPos.get(debrisId);
                Mob target = Util.getRandomFromCollection(targets);
                if (target == null) {
                    continue;
                }
                ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), atomEnum.getInc(), 15, 10,
                        0, 200, Util.getCurrentTime(), 1, 0, chr.getPosition().delta(source));
                faiList.add(forceAtomInfo);
                targetList.add(target.getObjectId());
                removeList.add(debrisId);
                debrisPos.remove(debrisId);
            }
            chr.write(FieldPacket.delWreckage(chr, removeList));
            chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), atomEnum.getForceAtomType(),
                    true, targetList, skillId, faiList, null, 0, 300,
                    null, 0, null));
        } finally {
            debrisLock.unlock();
        }
    }

    private void handlePartnerBuff() {
        if (!chr.hasSkill(PARTNERS)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(PARTNERS);
        int slv = chr.getSkillLevel(PARTNERS);
        Option o1 = new Option();
        o1.nValue = si.getValue(indieDamR, slv);
        o1.nReason = PARTNERS;
        o1.tStart = Util.getCurrentTime();
        o1.tTerm = si.getValue(time, slv);
        tsm.putCharacterStatValue(IndieDamR, o1);
    }

    @Override
    public int getFinalAttackSkill() {
        int skillId = 0;
        if (chr.hasSkill(ADV_DRAGON_SPARK)) {
            skillId = ADV_DRAGON_SPARK;
        } else if (chr.hasSkill(DRAGON_SPARK)) {
            skillId = DRAGON_SPARK;
        }
        if (skillId > 0) {
            SkillInfo si = SkillData.getSkillInfoById(skillId);
            int slv = chr.getSkillLevel(skillId);
            int proc = si.getValue(prop, slv);
            if (Util.succeedProp(proc)) {
                return skillId;
            }
        }
        return super.getFinalAttackSkill();
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
        Summon summon;
        Field field;
        Rect rect;
        switch (skillID) {
            case RETURN_FLASH:
                rect = chr.getRectAround(si.getFirstRect());
                if (!chr.isLeft()) {
                    rect.horizontalFlipAround(chr.getPosition().getX());
                }
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (Mob mob : chr.getField().getMobsInRect(rect)) {
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.AddDamParty, o1);
                }
                break;
            case RETURN_DIVE:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieBooster, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                if (chr.getParty() == null) {
                    tsm.putCharacterStatValue(IndieBooster, o1);
                } else {
                    rect = chr.getRectAround(si.getFirstRect());
                    List<PartyMember> partyMembers = chr.getField().getPartyMembersInRect(chr, rect).stream()
                            .filter(pml -> pml.getChr().getHP() > 0)
                            .toList();
                    for (PartyMember partyMember : partyMembers) {
                        Char partyChr = partyMember.getChr();
                        TemporaryStatManager partyTsm = partyChr.getTemporaryStatManager();
                        partyTsm.putCharacterStatValue(IndieBooster, o1);
                        partyTsm.sendSetStatPacket();
                    }
                }
                break;
            case RETURN_FLAME:
                SkillInfo rft = SkillData.getSkillInfoById(RETURN_FLAME_TILE);
                AffectedArea aa = AffectedArea.getPassiveAA(chr, RETURN_FLAME_TILE, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(rft.getFirstRect()));
                chr.getField().spawnAffectedArea(aa);
                break;
            case MAGIC_DEBRIS:
            case ENHANCED_MAGIC_DEBRIS:
                createMagicDebrisForceAtom(skillID);
                break;
            case MAGIC_GUARD:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(MagicGuard, o1);
                break;
            case MAGIC_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case ELEMENTAL_DECREASE:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ElementalReset, o1);
                break;
            case BLESSING_OF_THE_ONYX:
                o1.nOption = si.getValue(emad, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EMAD, o1);
                o2.nOption = si.getValue(emdd, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EMDD, o2);
                o3.nOption = si.getValue(epdd, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EPDD, o3);
                break;
            case SUMMON_ONYX_DRAGON:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(true);
                summon.setMoveAbility(MoveAbility.Stop);
                field.spawnSummon(summon);
                break;
            case DRAGON_MASTER:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NotDamaged, o1);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NewFlying, o2);
                TemporaryStatBase tsb = tsm.getTSBByTSIndex(TSIndex.RideVehicleExpire);
                o3.nOption = 1939007;
                o3.rOption = skillID;
                tsb.setOption(o3);
                tsb.setDynamicTermSet(true);
                tsb.setExpireTerm(si.getValue(time, slv));
                tsm.putCharacterStatValue(RideVehicleExpire, tsb.getOption());
                break;
        }
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        super.handleRemoveCTS(cts);
    }

    @Override
    public void handleWarp() {
        // spawn mir
        if (chr.getJob() != JobConstants.JobEnum.EVAN.getJobId()) {
            getDragon().resetToPlayer();
        }
        // clear debris
        handleRemoveDebris(debrisPos.keySet().stream().toList(), true);
        super.handleWarp();
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        cs.setPosMap(900010000);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}
