package net.swordie.ms.client.jobs.adventurer.thief;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.Beginner;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.*;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.drop.Drop;
import net.swordie.ms.life.drop.DropInfo;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class NightLord extends Thief {
    public static final int ASSASSINS_MARK = 4101011; //Buff (ON/OFF)
    public static final int ASSASSIN_MARK_ATOM = 4100012;
    public static final int NIGHTLORD_MARK_ATOM = 4120019;
    public static final int CLAW_BOOSTER = 4101003; //Buff
    public static final int SHADOW_PARTNER_NL = 4111002; //Buff
    public static final int EXPERT_THROWING_STAR_HANDLING = 4110012;
    public static final int SHADOW_STARS = 4111009; //Buff
    public static final int DARK_FLARE_NL = 4111007; //Summon
    public static final int SHADOW_WEB = 4111003; //Special Attack (Dot + Bind)
    public static final int VENOM_NL = 4110011; //Passive DoT
    public static final int MAPLE_WARRIOR_NL = 4121000; //Buff
    public static final int SHOWDOWN = 4121017; //Special Attack
    public static final int SUDDEN_RAID_NL = 4121016; //Special Attack
    public static final int FRAILTY_CURSE = 4121015; //AoE
    public static final int NIGHT_LORD_MARK = 4120018;
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
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        byte slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = (byte) skill.getCurrentLevel();
            skillID = SkillConstants.getActualSkillIDfromSkillID(skill.getSkillId());
        }
        if (hasHitMobs) {
            applyBleedDartOnMob(attackInfo);
            if (chr.hasSkill(ASSASSINS_MARK)) {
                handleMark(attackInfo);
                setMarkonMob(attackInfo);
            }
            if (attackInfo.skillId != NIGHTLORD_MARK_ATOM && attackInfo.skillId != ASSASSIN_MARK_ATOM) {
                procExpertThrowingStar(skillID);
            }
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();
        switch (attackInfo.skillId) {
            case SHADOW_WEB:
                o1.nOption = 1;
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        if (mob == null || mob.isBoss()) {
                            continue;
                        }
                        MobTemporaryStat mts = mob.getTemporaryStat();
                        mts.addStatOptionsAndBroadcast(MobStat.Stun, o1);
                        mts.createAndAddBurnedInfo(chr, skill);
                    }
                }
                break;
            case SHOWDOWN:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skill.getSkillId();
                o1.tOption = si.getValue(time, slv);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    if (!mob.isBoss()) {
                        mts.addStatOptionsAndBroadcast(MobStat.Showdown, o1);
                    }

                    int bonus = si.getValue(x, slv) + chr.getSkillStatValue(x, SHOWDOWN_ENHANCE);
                    o2.xOption = mob.isBoss() ? bonus / 2 : bonus; // Exp
                    o2.yOption = mob.isBoss() ? bonus / 2 : bonus; // Item Drop
                    mts.addStatOptionsAndBroadcast(MobStat.Treasure, o2);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }


    private void procExpertThrowingStar(int skillId) {
        if (!chr.hasSkill(EXPERT_THROWING_STAR_HANDLING)) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o = new Option();

        Skill skill = chr.getSkill(EXPERT_THROWING_STAR_HANDLING);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();
        int hideIconSkillId = skill.getSkillId() + 100; // there's no Buff Icon

        if (tsm.getOptByCTSAndSkill(IndieDamR, hideIconSkillId) == null) {
            if (tsm.hasStatBySkillId(hideIconSkillId)) {
                tsm.removeStatsBySkill(hideIconSkillId);
            }
            if (Util.succeedProp(si.getValue(prop, slv))) {
                o.nReason = hideIconSkillId;
                o.nValue = si.getValue(pdR, slv);
                o.tStart = (int) System.currentTimeMillis();
                o.tTerm = 5;
                tsm.putCharacterStatValue(IndieDamR, o);
                tsm.sendSetStatPacket();
            }
        } else {
            tsm.removeStatsBySkill(hideIconSkillId);
            o.nOption = 100;
            o.rOption = hideIconSkillId;
            o.tOption = 5;
            tsm.putCharacterStatValue(CriticalGrowing, o);
            tsm.sendSetStatPacket();

            if (SkillData.getSkillInfoById(skillId) != null) {
                chr.healMP(SkillData.getSkillInfoById(skillId).getValue(mpCon, slv));
            }

            chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffected(skill.getSkillId(), slv, 0)));
            chr.write(UserPacket.effect(Effect.skillAffected(skill.getSkillId(), slv, 0)));
        }
    }

    private void handleMark(AttackInfo attackInfo) {
        if (getMarkSkill() == null) {
            return;
        }
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = getMarkSkill();
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();

        if (tsm.hasStat(NightLordMark)) {
            if (attackInfo.skillId != NIGHTLORD_MARK_ATOM && attackInfo.skillId != ASSASSIN_MARK_ATOM) {
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    int randomInt = new Random().nextInt((360/getAssassinsMarkStarCount())-1);
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    Rect rect = new Rect(
                            new Position(
                                    mob.getPosition().getX() - 800,
                                    mob.getPosition().getY() - 800),
                            new Position(
                                    mob.getPosition().getX() + 800,
                                    mob.getPosition().getY() + 800)
                    );
                    MobTemporaryStat mts = mob.getTemporaryStat();

                    List<Mob> lifes = chr.getField().getMobsInRect(rect);
                    if (lifes.size() <= 0) {
                        return;
                    }
                    List<Mob> bossLifes = chr.getField().getBossMobsInRect(rect);
                    if (mts.hasBurnFromSkillAndOwner(getCurMarkLv(), chr.getId())) {
                        for (int i = 0; i < getAssassinsMarkStarCount(); i++) {

                            Mob life = Util.getRandomFromCollection(lifes);
                            if (bossLifes.size() > 0 && Util.succeedProp(65)) {
                                life = Util.getRandomFromCollection(bossLifes);
                            }

                            int anglez = (360 / getAssassinsMarkStarCount()) * i;

                            int inc = ForceAtomEnum.ASSASSIN_MARK.getInc();
                            int type = ForceAtomEnum.ASSASSIN_MARK.getForceAtomType();
                            int atom = ASSASSIN_MARK_ATOM;

                            if (chr.hasSkill(NIGHT_LORD_MARK)) {
                                inc = ForceAtomEnum.NIGHTLORD_MARK.getInc();
                                type = ForceAtomEnum.NIGHTLORD_MARK.getForceAtomType();
                                atom = NIGHTLORD_MARK_ATOM;
                            }
                            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, 45, 4,
                                    randomInt+anglez, 170, (int) System.currentTimeMillis(), 1, 0,
                                    new Position());
                            chr.getField().broadcastPacket(FieldPacket.createForceAtom(true, chr.getId(), life.getObjectId(), type,
                                    true, life.getObjectId(), atom, forceAtomInfo, rect, 0, 300,
                                    life.getPosition(), chr.getBulletIDForAttack(), life.getPosition()));
                        }
                    }
                }
            }
        }
    }

    private int getAssassinsMarkStarCount() {
        if (getMarkSkill() != null) {
            Skill skill = getMarkSkill();
            SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
            byte slv = (byte) skill.getCurrentLevel();

            return si.getValue(bulletCount, slv);
        }
        return 0;
    }

    private Skill getMarkSkill() {
        Skill skill = null;
        if (chr.hasSkill(ASSASSINS_MARK)) {
            skill = chr.getSkill(ASSASSINS_MARK);
        }
        if (chr.hasSkill(NIGHT_LORD_MARK)) {
            skill = chr.getSkill(NIGHT_LORD_MARK);
        }
        return skill;
    }



    private void setMarkonMob(AttackInfo attackInfo) {
        Skill skill = chr.getSkill(getCurMarkLv());
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = SkillData.getSkillInfoById(getCurMarkLv());
        byte slv = (byte) skill.getCurrentLevel();
        int markprop = si.getValue(prop, slv);
        if (tsm.hasStat(NightLordMark)) {
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                if (Util.succeedProp(markprop)) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.createAndAddBurnedInfo(chr, skill);
                }
            }
        }
    }

    private int getCurMarkLv() {
        int supgrade = 0;
        if (chr.hasSkill(ASSASSINS_MARK)) {
            supgrade = ASSASSINS_MARK;
        }
        if (chr.hasSkill(NIGHT_LORD_MARK)) {
            supgrade = NIGHT_LORD_MARK;
        }
        return supgrade;
    }

    private void applyBleedDartOnMob(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(BleedingToxin)) {
            Skill skill = chr.getSkill(BLEED_DART);
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                MobTemporaryStat mts = mob.getTemporaryStat();
                mts.createAndAddBurnedInfo(chr, skill);
            }
        }
    }



    // Skill related methods -------------------------------------------------------------------------------------------

    @Override
    public void handleSkill(Char chr, int skillID, int slv, InPacket inPacket) {
        super.handleSkill(chr, skillID, slv, inPacket);
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        SkillInfo si = si = SkillData.getSkillInfoById(skillID);

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case FRAILTY_CURSE:
                SkillInfo fci = SkillData.getSkillInfoById(skillID);
                int lt1 = si.getValue(lt, slv);
                int rb1 = si.getValue(rb, slv);
                AffectedArea aa2 = AffectedArea.getPassiveAA(chr, skillID, slv);
                aa2.setMobOrigin((byte) 0);
                aa2.setPosition(chr.getPosition());
                aa2.setRect(aa2.getPosition().getRectAround(fci.getRects().get(0)));
                aa2.setFlip(!chr.isLeft());
                aa2.setDelay((short) 9);
                chr.getField().spawnAffectedArea(aa2);
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
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NoBulletConsume, o1);
                break;

        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, InPacket inPacket, HitInfo hitInfo) {
        super.handleHit(chr, inPacket, hitInfo);
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }
}