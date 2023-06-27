package net.swordie.ms.client.jobs.adventurer.archer;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.GameConstants;
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

import java.util.Random;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class Bowmaster extends Archer {
    public static final int FINAL_ATTACK_BOW = 3100001;
    public static final int SOUL_ARROW_BOW = 3101004;
    public static final int ARROW_BOMB = 3101005;
    public static final int BOW_BOOSTER = 3101002;
    public static final int QUIVER_CARTRIDGE = 3101009;
    public static final int QUIVER_CARTRIDGE_ATOM = 3100010;
    public static final int PHOENIX = 3111005;
    public static final int FLAME_SURGE = 3111003;
    public static final int HOOKSHOT = 3111010;
    public static final int RECKLESS_HUNT_BOW = 3111011;
    public static final int FOCUSED_FURY = 3110012;
    public static final int MORTAL_BLOW_BOW = 3110001;
    public static final int ARROW_PLATTER = 3111013;
    public static final int EVASION_BOOST = 3110007;
    public static final int SHARP_EYES_BOW = 3121002;
    public static final int ILLUSION_STEP_BOW = 3121007;
    public static final int ENCHANTED_QUIVER = 3121016;
    public static final int ENCHANTED_QUIVER_ATOM = 3121017;
    public static final int BINDING_SHOT = 3121014;
    public static final int MAPLE_WARRIOR_BOW = 3121000;
    public static final int ARMOR_BREAK = 3120018;
    public static final int HEROS_WILL_BM = 3121009;
    public static final int BOW_EXPERT = 3120005;
    public static final int ADVANCED_FINAL_ATTACK_BOW = 3120008;

    public static final int SHARP_EYES_BOW_PERSIST = 3120043;
    public static final int SHARP_EYES_BOW_IED_H = 3120044;
    public static final int SHARP_EYES_BOW_CR_H = 3120045;
    public static final int GRITTY_DUST = 3121052;
    public static final int EPIC_ADVENTURE_BOW = 3121053;
    public static final int CONCENTRATION = 3121054;

    private QuiverCartridge quiverCartridge;
    private long nextArmorBreak = Long.MIN_VALUE;

    public Bowmaster(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isBowmaster(id);
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Skill skill = chr.getSkill(attackInfo.skillId);
        int skillID = 0;
        SkillInfo si = null;
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;
        int slv = 0;
        if (skill != null) {
            si = SkillData.getSkillInfoById(skill.getSkillId());
            slv = skill.getCurrentLevel();
            skillID = skill.getSkillId();
        }
        if (hasHitMobs && SkillConstants.isArmorPiercingSkill(skillID)) {
            handleQuiverCartridge(tsm, attackInfo);
            incrementMortalBlow();
            incrementFocusedFury();
            procArmorBreak();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case ARROW_BOMB:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case PHOENIX:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = GameConstants.DEFAULT_STUN_DURATION;
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null || mob.isBoss()) {
                        continue;
                    }
                    if (Util.succeedProp(si.getValue(prop, slv))) {
                        mob.getTemporaryStat().addStatOptionsAndBroadcast(MobStat.Stun, o1);
                    }
                }
                break;
            case FLAME_SURGE:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    AffectedArea aa = AffectedArea.getAffectedArea(chr, attackInfo);
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    aa.setMobOrigin((byte) 0);
                    aa.setDuration(si.getValue(dotTime, slv));
                    aa.setPosition(new Position(mob.getX(), mob.getY()));
                    aa.setRect(aa.getPosition().getRectAround(si.getFirstRect()));
                    chr.getField().spawnAffectedArea(aa);
                }
                break;
            case BINDING_SHOT:
                o1.nOption = si.getValue(s, slv); // Already negative in SI
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                o2.nOption = -si.getValue(x, slv); // TODO: check DebuffHealing effect
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    mts.addStatOptionsAndBroadcast(MobStat.Speed, o1);
                    mts.addStatOptionsAndBroadcast(MobStat.DebuffHealing, o2);
                }
                break;
            case GRITTY_DUST:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    mob.getTemporaryStat().createAndAddBurnedInfo(chr, skillID, slv);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void incrementMortalBlow() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!chr.hasSkill(MORTAL_BLOW_BOW)) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(MORTAL_BLOW_BOW);
        int slv = chr.getSkillLevel(MORTAL_BLOW_BOW);
        int count = 1;
        if (tsm.hasStat(BowMasterMortalBlow)) {
            count = tsm.getOption(BowMasterMortalBlow).nOption;
            if (count < si.getValue(x, slv)) {
                count ++;
            } else {
                count = 1;
            }
        }
        Option o1 = new Option();
        o1.nOption = count;
        o1.rOption = MORTAL_BLOW_BOW;
        tsm.putCharacterStatValue(BowMasterMortalBlow, o1);
        tsm.sendSetStatPacket();
    }

    private void incrementFocusedFury() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!chr.hasSkill(FOCUSED_FURY)) {
            return;
        }
        if (tsm.hasStat(BowMasterConcentration) && tsm.getOption(BowMasterConcentration).nOption == 0) {
            // cooldown
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(FOCUSED_FURY);
        int slv = chr.getSkillLevel(FOCUSED_FURY);
        int count = 1;
        if (tsm.hasStat(BowMasterConcentration)) {
            count = tsm.getOption(BowMasterConcentration).nOption;
            if (count < 20) {
                count++;
            }
        }
        Option o1 = new Option();
        o1.nOption = count;
        o1.rOption = FOCUSED_FURY;
        o1.tOption = si.getValue(time, slv);
        o1.xOption = count * si.getValue(x, slv); // not sent, used for tracking BaseStat
        tsm.putCharacterStatValue(BowMasterConcentration, o1);
        tsm.sendSetStatPacket();
        if (count < 20) {
            chr.write(UserPacket.effect(Effect.skillUse(FOCUSED_FURY, (byte) slv, 0)));
            chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(FOCUSED_FURY, (byte) slv, 0)));
        }
    }

    private void procArmorBreak() {
        if (!chr.hasSkill(ARMOR_BREAK) || Util.getCurrentTimeLong() < nextArmorBreak) {
            return;
        }
        SkillInfo si = SkillData.getSkillInfoById(ARMOR_BREAK);
        int slv = chr.getSkillLevel(ARMOR_BREAK);
        int cd = si.getValue(y, slv) * 1000;
        nextArmorBreak = Util.getCurrentTimeLong() + cd;
        chr.write(UserLocal.skillCooltimeSetM(ARMOR_BREAK, cd));
        chr.write(UserPacket.effect(Effect.skillUse(ARMOR_BREAK, (byte) slv, 0)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(ARMOR_BREAK, (byte) slv, 0)));
    }

    @Override
    public int getFinalAttackSkill() {
        int skillId = 0;
        if (chr.hasSkill(ADVANCED_FINAL_ATTACK_BOW)) {
            skillId = ADVANCED_FINAL_ATTACK_BOW;
        } else if (chr.hasSkill(FINAL_ATTACK_BOW)) {
            skillId = FINAL_ATTACK_BOW;
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
        Skill skill = chr.getSkill(skillID);
        SkillInfo si = null;
        if(skill != null) {
            si = SkillData.getSkillInfoById(skillID);
        }

        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch(skillID) {
            case QUIVER_CARTRIDGE:
                if (quiverCartridge == null) {
                    quiverCartridge = new QuiverCartridge(chr);
                } else if (tsm.hasStat(QuiverCatridge)) {
                    quiverCartridge.incType();
                }
                o1 = quiverCartridge.getOption();
                tsm.putCharacterStatValue(QuiverCatridge, o1);
                break;
            case RECKLESS_HUNT_BOW:
                if (tsm.hasStatBySkillId(skillID)) {
                    tsm.removeStatsBySkill(skillID);
                    tsm.sendResetStatPacket();
                } else {
                    o1.nOption = si.getValue(padX, slv); // not used in client, but for BaseStat calc
                    o1.rOption = skillID;
                    o1.bOption = si.getValue(x, slv);
                    tsm.putCharacterStatValue(ExtremeArchery, o1);
                    o2.nValue = si.getValue(indieDamR, slv);
                    o2.nReason = skillID;
                    o2.tStart = Util.getCurrentTime();
                    tsm.putCharacterStatValue(IndieDamR, o2);
                }
                break;
            case ENCHANTED_QUIVER:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(AdvancedQuiver, o1);
                break;
            case CONCENTRATION:
                o1.nValue = si.getValue(indiePad, slv);
                o1.nReason = skillID;
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o1);
                o2.nOption = si.getValue(x, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Stance, o2);
                o3.nValue = si.getValue(y, slv);
                o3.nReason = skillID;
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDamR, o3);
                break;
        }
        tsm.sendSetStatPacket();
    }



    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleMobDebuffResist(Char chr) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (chr.hasSkill(FOCUSED_FURY) && tsm.hasStat(BowMasterConcentration)) {
            chr.write(UserPacket.effect(Effect.skillSpecial(FOCUSED_FURY)));
            chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillSpecial(FOCUSED_FURY)));
            // apply cooltime
            SkillInfo si = SkillData.getSkillInfoById(FOCUSED_FURY);
            int slv = chr.getSkillLevel(FOCUSED_FURY);
            Option o1 = new Option();
            o1.nOption = 0;
            o1.rOption = FOCUSED_FURY;
            o1.tOption = si.getValue(cooltime, slv);
            o1.xOption = 0;
            tsm.putCharacterStatValue(BowMasterConcentration, o1);
            tsm.sendSetStatPacket();
        }
    }

    @Override
    public void handleLevelUp() {
        super.handleLevelUp();
    }


    private void handleQuiverCartridge(TemporaryStatManager tsm, AttackInfo attackInfo) {
        if (quiverCartridge == null) {
            return;
        }
        int skillId, forceAtomSkillId;
        if (chr.hasSkill(ENCHANTED_QUIVER)) {
            skillId = ENCHANTED_QUIVER;
            forceAtomSkillId = ENCHANTED_QUIVER_ATOM;
        } else if (chr.hasSkill(QUIVER_CARTRIDGE)) {
            skillId = QUIVER_CARTRIDGE;
            forceAtomSkillId = QUIVER_CARTRIDGE_ATOM;
        } else {
            if (tsm.hasStat(QuiverCatridge)) {
                tsm.removeStat(QuiverCatridge, true);
                tsm.sendResetStatPacket();
            }
            quiverCartridge = null;
            return;
        }

        SkillInfo si = SkillData.getSkillInfoById(skillId);
        int slv = chr.getSkillLevel(skillId);
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                continue;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            int mobId = mai.mobId;
            switch (quiverCartridge.getType()) {
                case BLOOD:
                    if (Util.succeedProp(si.getValue(w, slv)) && chr.getHP() > 0) {
                        chr.heal((int) (chr.getMaxHP() / (100D / si.getValue(x, slv))));
                    }
                    break;
                case POISON:
                    mts.createAndAddBurnedInfo(chr, skillId, slv, si.getValue(dotSuperpos, slv));
                    break;
                case MAGIC:
                    int num = new Random().nextInt(130) + 50;
                    if (Util.succeedProp(si.getValue(u, slv))) {
                        int inc = ForceAtomEnum.BM_ARROW.getInc();
                        int type = ForceAtomEnum.BM_ARROW.getForceAtomType();
                        ForceAtomInfo forceAtomInfo = new ForceAtomInfo(chr.getNewForceAtomKey(), inc, 13, 12,
                                num, 0, Util.getCurrentTime(), 1, 0,
                                new Position());
                        chr.getField().broadcastPacket(FieldPacket.createForceAtom(false, 0, chr.getId(), type,
                                true, mobId, forceAtomSkillId, forceAtomInfo, new Rect(), 0, 300,
                                mob.getPosition(), 0, mob.getPosition()));
                    }
                    break;
            }
        }

        if (!tsm.hasStat(AdvancedQuiver)) {
            quiverCartridge.decrementAmount();
            tsm.putCharacterStatValue(QuiverCatridge, quiverCartridge.getOption());
            tsm.sendSetStatPacket();
        }
    }

    public class QuiverCartridge{
        public enum ArrowType {
            BLOOD(1),
            POISON(2),
            MAGIC(3);

            private int val;

            ArrowType(int val) {
                this.val = val;
            }

            public int getVal() {
                return val;
            }

            public static ArrowType nextType(ArrowType type) {
                switch (type) {
                    case BLOOD:
                        return POISON;
                    case POISON:
                        return MAGIC;
                    case MAGIC:
                        return BLOOD;
                }
                return BLOOD; // default
            }
        }

        private int blood; // 1
        private int poison; // 2
        private int magic; // 3
        private ArrowType type;
        private final Char chr;

        public QuiverCartridge(Char chr) {
            this.chr = chr;
            blood = getMaxNumberOfArrows(ArrowType.BLOOD);
            poison = getMaxNumberOfArrows(ArrowType.POISON);
            magic = getMaxNumberOfArrows(ArrowType.MAGIC);
            type = ArrowType.BLOOD;
        }

        public void decrementAmount() {
            switch (type) {
                case BLOOD:
                    blood--;
                    if (blood == 0) {
                        blood = getMaxNumberOfArrows(type);
                        incType();
                    }
                    break;
                case POISON:
                    poison--;
                    if (poison == 0) {
                        poison = getMaxNumberOfArrows(type);
                        incType();
                    }
                    break;
                case MAGIC:
                    magic--;
                    if (magic == 0) {
                        magic = getMaxNumberOfArrows(type);
                        incType();
                    }
                    break;
            }
        }

        public void incType() {
            type = ArrowType.nextType(getType());
        }

        public ArrowType getType() {
            return type;
        }

        public Option getOption() {
            Option o = new Option();
            o.nOption = blood * 10000 + poison * 100 + magic;
            o.rOption = QUIVER_CARTRIDGE;
            o.xOption = type.getVal();
            return o;
        }

        private int getMaxNumberOfArrows(ArrowType type) {
            if (chr.hasSkill(ENCHANTED_QUIVER)) {
                if (type == ArrowType.MAGIC) {
                    return chr.getSkillStatValue(z, ENCHANTED_QUIVER);
                } else {
                    return chr.getSkillStatValue(y, ENCHANTED_QUIVER);
                }
            }
            if (chr.hasSkill(QUIVER_CARTRIDGE)) {
                return chr.getSkillStatValue(y, QUIVER_CARTRIDGE);
            }
            return 0;
        }
    }
}
