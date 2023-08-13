package net.swordie.ms.client.jobs.legend;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.jobs.adventurer.archer.*;
import net.swordie.ms.client.jobs.adventurer.magician.*;
import net.swordie.ms.client.jobs.adventurer.pirate.*;
import net.swordie.ms.client.jobs.adventurer.thief.*;
import net.swordie.ms.client.jobs.adventurer.warrior.*;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.ForceAtomEnum;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;

import java.util.*;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Phantom extends Job {

    public static final int JUDGMENT_DRAW_1 = 20031209;
    public static final int JUDGMENT_DRAW_2 = 20031210;
    public static final int JUDGMENT_DRAW_TOGGLE = 20031260;
    public static final int SHROUD_WALK = 20031205;
    public static final int SKILL_SWIPE = 20031207;
    public static final int LOADOUT = 20031208;
    public static final int TO_THE_SKIES = 20031203;
    public static final int DEXTEROUS_TRAINING = 20030206;
    public static final int GHOSTWALK = 20031211;

    public static final int IMPECCABLE_MEMORY_I = 24001001;

    public static final int IMPECCABLE_MEMORY_II = 24101001;
    public static final int CANE_BOOSTER = 24101005; //Buff
    public static final int CARTE_BLANCHE = 24100003;

    public static final int IMPECCABLE_MEMORY_III = 24111001;
    public static final int FINAL_FEINT = 24111002; //Buff (Unlimited Duration) Gone upon Death
    public static final int BAD_LUCK_WARD = 24111003; //Buff
    public static final int CLAIR_DE_LUNE = 24111005; //Buff

    public static final int IMPECCABLE_MEMORY_IV = 24121001;
    public static final int PRIERE_DARIA = 24121004; //Buff
    public static final int VOL_DAME = 24121007; // Special Buff
    public static final int MAPLE_WARRIOR_PH = 24121008; //Buff
    public static final int CARTE_NOIR = 24120002;              //80001890
    public static final int HEROS_WILL_PH = 24121009;
    public static final int TEMPEST = 24121005;

    public static final int HEROIC_MEMORIES_PH = 24121053;
    public static final int CARTE_ROSE_FINALE = 24121052;

    public static final int CARTE_ATOM = 80001890;

    private static final MobStat[] VOL_DAME_MOBSTATS = new MobStat[] {
            MobStat.PCounter,
            MobStat.MCounter,
            MobStat.PImmune,
            MobStat.MImmune,
            MobStat.PowerUp,
            MobStat.MagicUp,
            MobStat.Invincible
    };

    private int cardCount = 0;
    private Set<Job> stealJobHandlers = new HashSet<>();

    public Phantom(Char chr) {
        super(chr);
        if (chr.getId() != 0 && isHandlerOfJob(chr.getJob())) {
            stealJobHandlers.add(new Warrior(chr));
            stealJobHandlers.add(new Hero(chr));
            stealJobHandlers.add(new Paladin(chr));
            stealJobHandlers.add(new DarkKnight(chr));

            stealJobHandlers.add(new Magician(chr));
            stealJobHandlers.add(new Bishop(chr));
            stealJobHandlers.add(new IceLightning(chr));
            stealJobHandlers.add(new FirePoison(chr));

            stealJobHandlers.add(new Archer(chr));
            stealJobHandlers.add(new Bowmaster(chr));
            stealJobHandlers.add(new Marksman(chr));

            stealJobHandlers.add(new Thief(chr));
            stealJobHandlers.add(new NightLord(chr));
            stealJobHandlers.add(new Shadower(chr));
            stealJobHandlers.add(new DualBlade(chr));

            stealJobHandlers.add(new Pirate(chr));
            stealJobHandlers.add(new Buccaneer(chr));
            stealJobHandlers.add(new Corsair(chr));
            stealJobHandlers.add(new Cannoneer(chr));
        }
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isPhantom(id);
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        for (Job jobHandler : stealJobHandlers) {
            jobHandler.handleAttack(chr, attackInfo);
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        if (hasHitMobs) {
            if (attackInfo.skillId != CARTE_NOIR && attackInfo.skillId != CARTE_BLANCHE) {
                handleCarteSkill(attackInfo);
            }
            handleDrainCard();
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case CARTE_ROSE_FINALE:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    AffectedArea aa = AffectedArea.getAffectedArea(chr, attackInfo);
                    aa.setMobOrigin((byte) 1);
                    aa.setPosition(mob.getPosition());
                    aa.setDelay((short) 13);
                    aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
                    chr.getField().spawnAffectedArea(aa);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void setCardCount(int newCount) {
        int maxCount = 0;
        if (chr.hasSkill(JUDGMENT_DRAW_2)) {
            maxCount = 40;
        } else if (chr.hasSkill(JUDGMENT_DRAW_1)) {
            maxCount = 20;
        }
        cardCount = Math.min(newCount, maxCount);
        chr.write(UserLocal.incJudgementStack(cardCount));
    }

    private void createCarteForceAtom(int count) {
        int skillId = 0;
        if (chr.hasSkill(CARTE_NOIR)) {
            skillId = CARTE_NOIR;
        } else if (chr.hasSkill(CARTE_BLANCHE)) {
            skillId = CARTE_BLANCHE;
        }
        if (skillId == 0) {
            return;
        }
        // find target
        Rect rect = new Rect(
                chr.getPosition().getX() - 450, chr.getPosition().getY() - 450,
                chr.getPosition().getX() + 450, chr.getPosition().getY() + 450
        );
        List<Mob> targets = chr.getField().getMobsInRect(rect);
        if (targets.size() == 0) {
            return;
        }
        // create force atom
        ForceAtomEnum fae = skillId == CARTE_NOIR ? ForceAtomEnum.PHANTOM_CARD_2 : ForceAtomEnum.PHANTOM_CARD_1;
        List<ForceAtomInfo> faiList = new ArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        int angleStart = Util.getRandom(295, 325);
        for (int i = 0; i < count; i++) {
            Mob target = Util.getRandomFromCollection(targets);
            if (target == null) {
                continue;
            }
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), fae.getInc(), 23, 9,
                    angleStart - (2 * i), i * 5, Util.getCurrentTime(), 1, 0, new Position());
            faiList.add(forceAtomInfo);
            targetList.add(target.getObjectId());
        }
        chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(),
                true, targetList, skillId, faiList, null, 0, 0,
                null, 0, null));
    }

    private void handleCarteSkill(AttackInfo attackInfo) {
        int skillId = 0;
        if (chr.hasSkill(CARTE_NOIR)) {
            skillId = CARTE_NOIR;
        } else if (chr.hasSkill(CARTE_BLANCHE)) {
            skillId = CARTE_BLANCHE;
        }
        if (skillId == 0 || !attackInfo.didCrit(chr)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        if (!Util.succeedProp(si.getValue(prop, slv))) {
            return;
        }
        int addCount = si.getValue(x, slv);
        setCardCount(cardCount + addCount);
        createCarteForceAtom(addCount);
    }

    private void handleDrainCard() {
        if (!chr.hasSkill(JUDGMENT_DRAW_2)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!tsm.hasStat(Judgement) || tsm.getOption(Judgement).nOption != 5) {
            return;
        }
        if (chr.getHP() > 0) {
            int healRate = chr.getSkillStatValue(z, JUDGMENT_DRAW_2);
            chr.heal((int) (chr.getMaxHP() * (healRate / 100D)));
        }
    }


    @Override
    public int getFinalAttackSkill() {
        return 0;
    }



    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        for (Job jobHandler : stealJobHandlers) {
            jobHandler.handleSkill(chr, skillID, slv, inPacket);
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (skillID) {
            case SHROUD_WALK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Invisible, o1);
                chr.resetSkillCoolTime(SHROUD_WALK);
                break;
            case VOL_DAME:
                Position pos = inPacket.decodePosition();
                Rect rect = pos.getRectAround(si.getFirstRect());
                List<Mob> mobs = chr.getField().getMobsInRect(rect);
                if (mobs.size() == 0) {
                    return;
                }
                handleVolDame(mobs);
                break;
            case JUDGMENT_DRAW_1:
            case JUDGMENT_DRAW_2:
                handleJudgmentDraw(skillID);
                break;
            case GHOSTWALK:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DarkSight, o1);
                break;
            case CANE_BOOSTER:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Booster, o1);
                break;
            case FINAL_FEINT:
                o1.nOption = 1;
                o1.rOption = skillID;
                tsm.putCharacterStatValue(ReviveOnce, o1);
                break;
            case BAD_LUCK_WARD:
                o1.nValue = si.getValue(indieMhpR, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMHPR, o1);
                o2.nValue = si.getValue(indieMmpR, slv);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMMPR, o2);
                o3.nOption = si.getValue(x, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AsrR, o3);
                o4.nOption = si.getValue(y, slv);
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(TerR, o4);
                break;
            case CLAIR_DE_LUNE:
                o1.nValue = si.getValue(indiePad, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o2.nValue = si.getValue(indieAcc, slv);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieACC, o2);
                break;
            case PRIERE_DARIA:
                o1.nOption = si.getValue(damR, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamR, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                o2.bOption = 1;
                tsm.putCharacterStatValue(IgnoreMobpdpR, o2);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void handleVolDame(List<Mob> mobs) {
        // remove mob buff
        MobStat stolenBuffStat = null;
        for (Mob mob : mobs) {
            MobTemporaryStat mts = mob.getTemporaryStat();
            for (MobStat mobStat : VOL_DAME_MOBSTATS) {
                if (mts.hasCurrentMobStat(mobStat)) {
                    mts.removeMobStat(mobStat, false);
                    stolenBuffStat = mobStat;
                }
            }
        }
        if (stolenBuffStat == null) {
            return;
        }
        // handle buff
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(VOL_DAME);
        int slv = chr.getSkillLevel(VOL_DAME);
        Option o1 = new Option();
        switch (stolenBuffStat) {
            case PowerUp:
            case MagicUp:
                o1.nOption = si.getValue(epad, slv);
                o1.rOption = VOL_DAME;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PAD, o1);
                break;
            case PImmune:
            case MImmune:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = VOL_DAME;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(DamageReduce, o1);
                break;
            case PCounter:
            case MCounter:
                o1.nOption = si.getValue(y, slv);
                o1.rOption = VOL_DAME;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(PowerGuard, o1);
                break;
            case Invincible:
                o1.nOption = 1;
                o1.rOption = VOL_DAME;
                o1.tOption = si.getValue(subTime, slv);
                tsm.putCharacterStatValue(NotDamaged, o1);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private void handleJudgmentDraw(int skillId) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        // handle cards
        setCardCount(0);
        createCarteForceAtom(si.getValue(u, slv));
        // handle buff
        int roll = Util.getRandom(1, skillId == JUDGMENT_DRAW_2 ? 4 : 2);
        System.out.println(roll);
        int xValue = 0;
        switch (roll) {
            case 1: // cr
                xValue = si.getValue(v, slv);
                break;
            case 2: // dropR
                xValue = si.getValue(w, slv);
                break;
            case 3: // AsrR/TerR
                xValue = si.getValue(x, slv); // TerR = y, but they are equal
                break;
            case 4: // Drain
                roll = 5; // original 4 (PDDR) was removed
                xValue = 1;
                break;
        }
        chr.write(UserPacket.effect(Effect.avatarOriented("Skill/2003.img/skill/20031210/affected/"+ (roll-1))));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.avatarOriented("Skill/2003.img/skill/20031210/affected/"+ (roll-1))), chr);
        Option o1 = new Option();
        o1.nOption = roll;
        o1.rOption = skillId;
        o1.tOption = si.getValue(time, slv);
        o1.xOption = xValue;
        tsm.putCharacterStatValue(Judgement, o1);
        tsm.sendSetStatPacket();
    }

    @Override
    public void handleRemoveCTS(CharacterTemporaryStat cts) {
        if (cts == Invisible) {
            Option shroudWalkOpt = chr.getTemporaryStatManager().getOptByCTSAndSkill(Invisible, SHROUD_WALK);
            if (shroudWalkOpt != null) {
                chr.addSkillCooldown(SHROUD_WALK, shroudWalkOpt.nOption * chr.getSkillStatValue(cooltimeMS, SHROUD_WALK));
            }
        }
        super.handleRemoveCTS(cts);
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        for (Job jobHandler : stealJobHandlers) {
            jobHandler.handleHit(chr, hitInfo);
        }
        super.handleHit(chr, hitInfo);
    }

    public void reviveByFinalFeint() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(FINAL_FEINT);
        int slv = chr.getSkillLevel(FINAL_FEINT);

        System.out.println(chr.getMaxHP() * (si.getValue(x, slv) / 100D));
        chr.heal((int) (chr.getMaxHP() * (si.getValue(x, slv) / 100D)));
        tsm.removeStatsBySkill(FINAL_FEINT);
        tsm.sendResetStatPacket();
        chr.chatMessage("You have been revived by Final Feint.");
        chr.write(UserPacket.effect(Effect.skillSpecial(FINAL_FEINT)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillSpecial(FINAL_FEINT)), chr);

        Option o1 = new Option();
        o1.nOption = 1;
        o1.rOption = FINAL_FEINT;
        o1.tOption = si.getValue(y, slv);
        tsm.putCharacterStatValue(NotDamaged, o1);
        tsm.sendSetStatPacket();
    }

    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        chr.setStolenSkills(new HashSet<>());
        chr.setChosenSkills(new HashSet<>());
        chr.getAvatarData().getCharacterStat().setPosMap(915000000);
    }

    @Override
    public void handleSetJob(short jobId) {
        if (jobId == JobConstants.JobEnum.PHANTOM.getJobId()) {
            chr.addSkill(SHROUD_WALK, 1, 1);
            chr.addSkill(SKILL_SWIPE, 1, 1);
            chr.addSkill(LOADOUT, 1, 1);
            chr.addSkill(TO_THE_SKIES, 1, 1);
        } else if (jobId == JobConstants.JobEnum.PHANTOM2.getJobId()) {
            chr.addSkill(JUDGMENT_DRAW_1, 1, 1);
            chr.addSkill(JUDGMENT_DRAW_TOGGLE, 1, 1);
        } else if (jobId == JobConstants.JobEnum.PHANTOM4.getJobId()) {
            chr.removeSkill(JUDGMENT_DRAW_1);
            chr.addSkill(JUDGMENT_DRAW_2, 1, 1);
        }
        super.handleSetJob(jobId);
    }
}