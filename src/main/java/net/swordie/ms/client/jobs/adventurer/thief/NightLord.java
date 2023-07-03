package net.swordie.ms.client.jobs.adventurer.thief;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.life.mob.skill.BurnedInfo;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.*;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class NightLord extends Thief {
    public static final int CLAW_MASTERY = 4100000;
    public static final int CRITICAL_THROW = 4100001;
    public static final int ASSASSINS_MARK = 4101011; //Buff (ON/OFF)
    public static final int ASSASSINS_MARK_ATOM = 4100012;
    public static final int CLAW_BOOSTER = 4101003; //Buff
    public static final int SHADOW_PARTNER_NL = 4111002; //Buff
    public static final int ENVELOPING_DARKNESS = 4110008;
    public static final int EXPERT_THROWING_STAR_HANDLING = 4110012;
    public static final int SHADOW_STARS = 4111009; //Buff
    public static final int DARK_FLARE_NL = 4111007; //Summon
    public static final int SHADOW_WEB = 4111003; //Special Attack (Dot + Bind)
    public static final int VENOM_NL = 4110011; //Passive DoT
    public static final int CLAW_EXPERT = 4120012;
    public static final int MAPLE_WARRIOR_NL = 4121000; //Buff
    public static final int SHOWDOWN = 4121017; //Special Attack
    public static final int SUDDEN_RAID_NL = 4121016; //Special Attack
    public static final int FRAILTY_CURSE = 4121015; //AoE
    public static final int NIGHT_LORDS_MARK = 4120018;
    public static final int NIGHT_LORDS_MARK_ATOM = 4120019;
    public static final int TOXIC_VENOM_NL = 4120011; //Passive DoT
    public static final int HEROS_WILL_NL = 4121009;

    public static final int EPIC_ADVENTURE_NL = 4121053;
    public static final int BLEED_DART = 4121054;
    public static final int SHOWDOWN_ENHANCE = 4120045;
    public static final int FRAILTY_CURSE_SLOW = 4120047;
    public static final int FRAILTY_CURSE_ENHANCE = 4120046;
    public static final int FRAILTY_CURSE_BOSS_RUSH = 4120048;

    public NightLord(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isNightLord(id);
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
            handleExpertThrowingStar(attackInfo);
            handleMark(attackInfo);
            applyBleedDart(attackInfo);
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case SHADOW_WEB:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                        mts.createAndAddBurnedInfo(chr, skillID, slv);
                    }
                }
                break;
            case SHOWDOWN:
                // boss effect
                int bonus = si.getValue(x, slv) + this.chr.getSkillStatValue(x, SHOWDOWN_ENHANCE);
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o1.xOption = bonus / 2; // exp %
                o1.yOption = bonus / 2; // drop %
                // non-boss effect
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                o3.nOption = 1;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                o3.xOption = bonus;
                o3.yOption = bonus;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (mob.isBoss()) {
                        mts.addStatOptionsAndBroadcast(MobStat.Treasure, o1);
                    } else {
                        mts.addStatOptionsAndBroadcast(MobStat.Showdown, o2);
                        mts.addStatOptionsAndBroadcast(MobStat.Treasure, o3);
                    }
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }


    private void handleExpertThrowingStar(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();

        if (!chr.hasSkill(EXPERT_THROWING_STAR_HANDLING)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(EXPERT_THROWING_STAR_HANDLING);
        int slv = chr.getSkillLevel(EXPERT_THROWING_STAR_HANDLING);

        if (Util.succeedProp(si.getValue(prop, slv))) {
            chr.write(UserLocal.setNextShootExJablin());
            // no mp consume handled in AttackHandler.handleAttack
            // throwing star recharge handled in chr.applyBulletCon
        }
    }

    private int getMarkSkill() {
        int skillId = 0;
        if (chr.hasSkill(NIGHT_LORDS_MARK)) {
            skillId = NIGHT_LORDS_MARK;
        } else if (chr.hasSkill(ASSASSINS_MARK)) {
            skillId = ASSASSINS_MARK;
        }
        return skillId;
    }

    private void handleMark(AttackInfo attackInfo) {
        if (attackInfo.skillId == NIGHT_LORDS_MARK_ATOM || attackInfo.skillId == ASSASSINS_MARK_ATOM) {
            return;
        }
        int skillId = getMarkSkill();
        if (skillId == 0) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        boolean hasMarkStat = tsm.hasStat(NightLordMark);

        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        int proc = si.getValue(SkillStat.prop, slv);

        int atomSkillId = skillId == NIGHT_LORDS_MARK ? NIGHT_LORDS_MARK_ATOM : ASSASSINS_MARK_ATOM;
        ForceAtomEnum atomEnum = skillId == NIGHT_LORDS_MARK ? ForceAtomEnum.NIGHTLORD_MARK : ForceAtomEnum.ASSASSIN_MARK;
        int starCount = si.getValue(bulletCount, slv);

        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            /*
             * mark gets triggered when:
             *     - mob has the BurnedInfo
             *     - mob dies and hasMarkStat && succeedProc(proc)
             * apply BurnedInfo if hasMarkStat && succeedProc(proc)
             */
            MobTemporaryStat mts = mob.getTemporaryStat();
            BurnedInfo bi = mob.getTemporaryStat().getBurnBySkillAndOwner(skillId, chr.getId());
            if (bi != null) {
                mts.removeBurnedInfo(bi, true);
                procMark(mob, atomSkillId, atomEnum, starCount);
            } else if (hasMarkStat && Util.succeedProp(proc)) {
                if (Arrays.stream(mai.damages).sum() >= mob.getHp()) {
                    procMark(mob, atomSkillId, atomEnum, starCount);
                } else {
                    mob.getTemporaryStat().createAndAddBurnedInfo(chr, skillId, slv, 0, si.getValue(dotInterval, slv), si.getValue(dotTime, slv), 1);
                }
            }
        }
    }

    private void procMark(Mob mob, int skillId, ForceAtomEnum atomEnum, int starCount) {
        Rect rect = new Rect(
                new Position(
                        mob.getPosition().getX() - 800,
                        mob.getPosition().getY() - 800),
                new Position(
                        mob.getPosition().getX() + 800,
                        mob.getPosition().getY() + 800)
        );
        List<Mob> targets = chr.getField().getMobsInRect(rect);
        if (targets.size() == 0) {
            return;
        }
        int angleStart = Util.getRandom((360 / starCount)-1);
        List<ForceAtomInfo> faiList = new ArrayList<>();
        List<Integer> targetList = new ArrayList<>();
        for (int i = 0; i < starCount; i++) {
            Mob target = Util.getRandomFromCollection(targets);
            if (target == null) {
                continue;
            }
            int angle = (360 / starCount) * i;
            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), atomEnum.getInc(), 45, 4,
                    angleStart + angle, 170, Util.getCurrentTime(), 1, 0, new Position());
            faiList.add(forceAtomInfo);
            targetList.add(target.getObjectId());

        }
        chr.getField().broadcastPacket(FieldPacket.createForceAtom(true, chr.getId(), mob.getObjectId(), atomEnum.getForceAtomType(),
                true, targetList, skillId, faiList, rect, 0, 300,
                null, chr.getBulletIDForAttack(), null));
    }

    private void applyBleedDart(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(BleedingToxin)) {
            int slv = chr.getSkillLevel(BLEED_DART);
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                mob.getTemporaryStat().createAndAddBurnedInfo(chr, BLEED_DART, slv);
            }
        }
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
        switch (skillID) {
            case FRAILTY_CURSE:
                AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa.setMobOrigin((byte) 0);
                aa.setPosition(chr.getPosition());
                aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                aa.setFlip(!chr.isLeft());
                aa.setDelay((short) 9);
                chr.getField().spawnAffectedArea(aa);
                break;
            case ASSASSINS_MARK:
                if (tsm.hasStat(NightLordMark)) {
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = 1;
                    o1.rOption = skillID;
                    o1.tOption = 0;
                    tsm.putCharacterStatValue(NightLordMark, o1);
                }
                break;
            case SHADOW_STARS:
                o1.nOption = chr.getBulletIDForAttack() - 2069999;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NoBulletConsume, o1);
                break;
            case BLEED_DART:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(BleedingToxin, o1);
                o2.nValue = si.getValue(indiePad, slv);
                o2.nReason = skillID;
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o2);
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {
        super.handleHit(chr, hitInfo);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}