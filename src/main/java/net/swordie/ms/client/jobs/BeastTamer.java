package net.swordie.ms.client.jobs;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.CharacterStat;
import net.swordie.ms.client.character.info.HitInfo;
import net.swordie.ms.client.character.skills.Option;
import net.swordie.ms.client.character.skills.Skill;
import net.swordie.ms.client.character.skills.SkillStat;
import net.swordie.ms.client.character.skills.TownPortal;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.jobs.adventurer.magician.Bishop;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.connection.packet.Effect;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.connection.packet.UserRemote;
import net.swordie.ms.constants.JobConstants;
import net.swordie.ms.constants.SkillConstants;
import net.swordie.ms.enums.MoveAbility;
import net.swordie.ms.life.AffectedArea;
import net.swordie.ms.life.Summon;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.loaders.FieldData;
import net.swordie.ms.loaders.SkillData;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.field.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.SkillStat.*;
import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;

/**
 * Created on 12/14/2017.
 */
public class BeastTamer extends Job {
    //Common
    public static final int HOMEWARD_BOUND = 110001514;
    public static final int MAPLE_GUARDIAN = 110001511;
    public static final int BEAR_MODE = 110001501;
    public static final int SNOW_LEOPARD_MODE = 110001502;
    public static final int HAWK_MODE = 110001503;
    public static final int CAT_MODE = 110001504;

    //Bear Mode
    public static final int LIL_FORT = 112001007;
    public static final int FORT_FOLLOW_UP = 112000015;
    public static final int MAJESTIC_TRUMPET = 112001006;
    public static final int BEAR_REBORN = 112000016;
    public static final int BEAR_ASSAULT = 112001009;
    public static final int FISHY_SLAP = 112001008;


    //Snow Leopard Mode
    public static final int BRO_ATTACK = 112101016;
    public static final int THUNDER_DASH = 112101007;
    public static final int ADV_THUNDER_DASH = 112100012;
    public static final int THUNDER_TRAIL = 112100008; //tile

    //Hawk Mode
    public static final int EKA_EXPRESS = 112111010;    //Door skill
    public static final int FLY = 112111000;
    public static final int HAWK_FLOCK = 112111007;
    public static final int RAPTOR_TALONS = 112111006;
    public static final int BIRDS_EYE_VIEW = 112111009;
    public static final int RAZOR_BEAK = 112111008;
    public static final int REGROUP = 112111011;    //Warp Party to player
    public static final int DEFENSIVE_FORMATION = 112110005;
    public static final int TORNADO_FLIGHT = 112001016;


    //Cat Mode
    public static final int MEOW_HEAL = 112121013;
    public static final int PURR_ZONE = 112121005; // Special Skill
    public static final int MEOW_CARD = 112121006; // Meow Card
    public static final int MEOW_CARD_RED = 112121007; //Red
    public static final int MEOW_CARD_BLUE = 112121008; //Blue
    public static final int MEOW_CARD_GREEN = 112121009; //Green
    public static final int MEOW_CARD_GOLD = 112121020; //112120009;    //Gold
    public static final int MEOW_CARD_GOLD_SKILL = 112120019; // If chr has the Gold Card Skill
    public static final int FIRE_KITTY = 112121004;
    public static final int CATS_CRADLE_BLITZKRIEG = 112121057; // Special Skill (like PURR_ZONE)
    public static final int KITTY_BATTLE_SQUAD = 112120021;
    public static final int KITTY_TREATS = 112120023;
    public static final int STICKY_PAWS = 112120017;
    public static final int CAT_CLAWS = 112120018;
    public static final int MOUSERS_INSIGHT = 112120022;
    public static final int FRIENDS_OF_ARBY = 112120016;
    public static final int MEOW_CURE = 112121010;
    public static final int MEOW_REVIVE = 112121011;

    //Hyper
    public static final int TEAM_ROAR = 112121056;

    private static int[] bearBuffs = new int[]{
            BEAR_ASSAULT,
    };

    private static int[] leopardBuffs = new int[]{
            BRO_ATTACK,

    };

    private static int[] hawkBuffs = new int[]{
            HAWK_FLOCK,
            RAPTOR_TALONS,
            BIRDS_EYE_VIEW,
            RAZOR_BEAK,
    };

    private static int[] catBuffs = new int[]{
            MEOW_CARD,
            MEOW_CARD_RED,
            MEOW_CARD_BLUE,
            MEOW_CARD_GREEN,
            MEOW_CARD_GOLD,
            MEOW_CARD_GOLD_SKILL,
            KITTY_BATTLE_SQUAD,
            KITTY_TREATS,
            STICKY_PAWS,
            CAT_CLAWS,
            MOUSERS_INSIGHT,
            FRIENDS_OF_ARBY,
    };

    private int[] cards = new int[]{
            MEOW_CARD_RED,
            MEOW_CARD_GREEN,
            MEOW_CARD_BLUE,
            MEOW_CARD_GOLD
    };

    private static final HashMap<Integer, int[]> buffsByMode;

    static {
        buffsByMode = new HashMap<>();
        buffsByMode.put(BEAR_MODE, bearBuffs);
        buffsByMode.put(SNOW_LEOPARD_MODE, leopardBuffs);
        buffsByMode.put(HAWK_MODE, hawkBuffs);
        buffsByMode.put(CAT_MODE, catBuffs);
    }

    private int fortFollowUpAddAttack = 0;
    private Summon defensiveFormation;

    public BeastTamer(Char chr) {
        super(chr);
    }

    @Override
    public boolean isHandlerOfJob(short id) {
        return JobConstants.isBeastTamer(id);
    }

    private boolean isBearMode() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        return tsm.getOption(BeastMode).nOption == 1;
    }

    private boolean isLeopardMode() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        return tsm.getOption(BeastMode).nOption == 2;
    }

    private boolean isHawkMode() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        return tsm.getOption(BeastMode).nOption == 3;
    }

    private boolean isCatMode() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        return tsm.getOption(BeastMode).nOption == 4;
    }

    private void giveMeowCard(int slv) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (!chr.hasSkill(MEOW_CARD) && !chr.hasSkill(MEOW_CARD_GOLD_SKILL)) {
            return;
        }
        SkillInfo mc = SkillData.getSkillInfoById(MEOW_CARD);
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        Option o4 = new Option();

        int randomMeowCard = getRandomMeowCard();

        resetPrevMeowCards();
        switch (randomMeowCard) {
            case MEOW_CARD_RED:
                o1.nReason = randomMeowCard;
                o1.nValue = mc.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = mc.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o1);
                break;
            case MEOW_CARD_GREEN:
                o1.nReason = randomMeowCard;
                o1.nValue = mc.getValue(indieBooster, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = mc.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o1);
                o2.nReason = randomMeowCard;
                o2.nValue = mc.getValue(indieSpeed, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = mc.getValue(time, slv);
                tsm.putCharacterStatValue(IndieSpeed, o1);
                break;
            case MEOW_CARD_BLUE:
                o1.nReason = randomMeowCard;
                o1.nValue = mc.getValue(pdd, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = mc.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDEF, o1);
                break;
            case MEOW_CARD_GOLD:
                o1.nReason = randomMeowCard;
                o1.nValue = mc.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = mc.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o1);
                o2.nReason = randomMeowCard;
                o2.nValue = mc.getValue(indieBooster, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = mc.getValue(time, slv);
                tsm.putCharacterStatValue(IndieBooster, o2);
                o3.nReason = randomMeowCard;
                o3.nValue = mc.getValue(indieSpeed, slv);
                o3.tStart = Util.getCurrentTime();
                o3.tTerm = mc.getValue(time, slv);
                tsm.putCharacterStatValue(IndieSpeed, o3);
                o4.nReason = randomMeowCard;
                o4.nValue = mc.getValue(pdd, slv);
                o4.tStart = Util.getCurrentTime();
                o4.tTerm = mc.getValue(time, slv);
                tsm.putCharacterStatValue(IndieDEF, o4);
                break;
        }
        tsm.sendSetStatPacket();
    }

    private int getRandomMeowCard() {
        int rng = new Random().nextInt((chr.hasSkill(MEOW_CARD_GOLD_SKILL) ? cards.length : cards.length - 1));
        return cards[rng];
    }

    private void resetPrevMeowCards() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        for (int cardBuffId : cards) {
            if (tsm.hasStatBySkillId(cardBuffId)) {
                tsm.removeStatsBySkill(cardBuffId);
                tsm.sendResetStatPacket();
            }
        }
    }

    private void giveKittyBattleSquadBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(KITTY_BATTLE_SQUAD);
        int slv = si.getCurrentLevel();
        o1.nReason = KITTY_BATTLE_SQUAD;
        o1.nValue = si.getValue(indiePad, slv);
        o1.tStart = Util.getCurrentTime();
        tsm.putCharacterStatValue(IndiePAD, o1);
        tsm.putCharacterStatValue(IndieMAD, o1);
        tsm.sendSetStatPacket();
    }

    private void giveKittyTreatsBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(KITTY_TREATS);
        int slv = si.getCurrentLevel();
        o1.nReason = KITTY_TREATS;
        o1.nValue = si.getValue(indieMhp, slv);
        o1.tStart = Util.getCurrentTime();
        tsm.putCharacterStatValue(IndieMHP, o1);
        o2.nReason = KITTY_TREATS;
        o2.nValue = si.getValue(indieMmp, slv);
        o2.tStart = Util.getCurrentTime();
        tsm.putCharacterStatValue(IndieMMP, o2);
        tsm.sendSetStatPacket();
    }

    private void giveStickyPawsBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(STICKY_PAWS);
        int slv = si.getCurrentLevel();
        o1.nOption = si.getValue(v, slv);
        o1.rOption = STICKY_PAWS;
        tsm.putCharacterStatValue(DropRate, o1);
        tsm.sendSetStatPacket();
    }

    private void giveCatClawsBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(CAT_CLAWS);
        int slv = si.getCurrentLevel();
        o1.nOption = si.getValue(x, slv);
        o1.rOption = CAT_CLAWS;
        tsm.putCharacterStatValue(CriticalBuff, o1);
        o2.nOption = si.getValue(y, slv);
        o2.rOption = CAT_CLAWS;
        tsm.putCharacterStatValue(IncCriticalDam, o2);
        tsm.sendSetStatPacket();
    }

    private void giveMouserInsightBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(MOUSERS_INSIGHT);
        int slv = si.getCurrentLevel();
        o1.nValue = si.getValue(x, slv);
        o1.nReason = MOUSERS_INSIGHT;
        o1.tStart = Util.getCurrentTime();
        tsm.putCharacterStatValue(IndieIgnoreMobpdpR, o1);
        tsm.sendSetStatPacket();
    }

    private void giveFriendsOfArbyBuff() {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        Option o2 = new Option();
        SkillInfo si = SkillData.getSkillInfoById(FRIENDS_OF_ARBY);
        int slv = si.getCurrentLevel();
        if (tsm.getOptByCTSAndSkill(HolySymbol, Bishop.HOLY_SYMBOL) == null) { // Only apply if player doesn't have Holy Symbol
            o1.nOption = si.getValue(x, slv);
            o1.rOption = FRIENDS_OF_ARBY;
            tsm.putCharacterStatValue(HolySymbol, o1);
            tsm.sendSetStatPacket();
        }
    }

    private Summon defensiveFormationSummon() {
        Skill skill = chr.getSkill(DEFENSIVE_FORMATION);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();
        defensiveFormation = Summon.getSummonBy(chr, DEFENSIVE_FORMATION, slv);
        defensiveFormation.setFlyMob(true);
        defensiveFormation.setSummonTerm(si.getValue(time, slv));
        defensiveFormation.setMoveAbility(MoveAbility.Fly); // Different MoveAbility?
        return defensiveFormation;
    }


    // Attack related methods ------------------------------------------------------------------------------------------

    @Override
    public void handleAttack(Char chr, AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillID = SkillConstants.getActualSkillIDfromSkillID(attackInfo.skillId);
        SkillInfo si = SkillData.getSkillInfoById(attackInfo.skillId);
        int slv = chr.getSkillLevel(skillID);
        boolean hasHitMobs = attackInfo.mobAttackInfo.size() > 0;

        if (isLeopardMode()) { // Leopard
            if (hasHitMobs) {
                if (skillID != BRO_ATTACK) {
                    procBroAttack(attackInfo);
                }
            }
        }

        if (isHawkMode()) { // Hawk
            if (hasHitMobs) {
                applyRaptorTalonsOnMob(attackInfo);
            }
        }

        if (isCatMode()) { // Cat
            giveKittyBattleSquadBuff();
            giveKittyTreatsBuff();
            giveStickyPawsBuff();
            giveCatClawsBuff();
            giveMouserInsightBuff();
            giveFriendsOfArbyBuff();
        }
        Option o1 = new Option();
        Option o2 = new Option();
        Option o3 = new Option();
        switch (attackInfo.skillId) {
            case MAJESTIC_TRUMPET:
                SkillInfo rca = SkillData.getSkillInfoById(skillID);
                AffectedArea aa = AffectedArea.getAffectedArea(chr, attackInfo);
                aa.setMobOrigin((byte) 0);
                aa.setSkillID(skillID);
                int x = chr.getPosition().getX();
                int y = chr.getPosition().getY() + 41;
                aa.setPosition(new Position(x, y));
                aa.setRect(aa.getPosition().getRectAround(rca.getRects().get(0)));
                aa.setDelay((short) 4);
                chr.getField().spawnAffectedArea(aa);
                break;
            case THUNDER_DASH:
            case ADV_THUNDER_DASH:
                SkillInfo tdi = SkillData.getSkillInfoById(THUNDER_TRAIL);
                AffectedArea aa2 = AffectedArea.getAffectedArea(chr, attackInfo);
                aa2.setMobOrigin((byte) 0);
                aa2.setSkillID(THUNDER_TRAIL);
                //int x = chr.getPosition().getX();
                //int y = chr.getPosition().getY() + 41;
                //aa.setPosition(new Position(x, y));
                aa2.setPosition(chr.getPosition());
                Rect rect = tdi.getRects().get(0);
                if (!chr.isLeft()) {
                    rect = rect.moveRight();
                }
                aa2.setRect(aa2.getPosition().getRectAround(rect));
                aa2.setDelay((short) 4);
                chr.getField().spawnAffectedArea(aa2);
                break;
            case PURR_ZONE: //TODO  isn't a AffectedArea, but a 'Special'
                SkillInfo pz = SkillData.getSkillInfoById(PURR_ZONE);
                AffectedArea aa3 = AffectedArea.getAffectedArea(chr, attackInfo);
                aa3.setMobOrigin((byte) 0);
                aa3.setSkillID(skillID);
                aa3.setPosition(chr.getPosition());
                aa3.setRect(aa3.getPosition().getRectAround(pz.getRects().get(0)));
                aa3.setSlv((byte) slv);
                chr.getField().spawnAffectedArea(aa3);
                break;
            case FIRE_KITTY:
                for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                    Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                    if (mob == null) {
                        continue;
                    }
                    MobTemporaryStat mts = mob.getTemporaryStat();
                    o1.nOption = si.getValue(SkillStat.x, slv);
                    o1.rOption = skillID;
                    o1.tOption = si.getValue(time, slv);
                    mts.addStatOptionsAndBroadcast(MobStat.PDR, o1);
                }
                break;
        }

        super.handleAttack(chr, attackInfo);
    }

    private void procBroAttack(AttackInfo attackInfo) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        Option o1 = new Option();
        if (tsm.getOptByCTSAndSkill(ACC, BRO_ATTACK) != null) {
            Summon summon;
            Field field;
            Skill skill = chr.getSkill(BRO_ATTACK);
            if (!chr.hasSkill(BRO_ATTACK)) {
                return;
            }
            SkillInfo si = SkillData.getSkillInfoById(BRO_ATTACK);
            byte slv = (byte) skill.getCurrentLevel();
            int summonProp = si.getValue(prop, slv);
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    continue;
                }
                if (Util.succeedProp(summonProp)) {
                    summon = Summon.getSummonBy(chr, BRO_ATTACK, slv);
                    field = chr.getField();
                    summon.setFlyMob(false);
                    summon.setPosition(mob.getPosition());
                    summon.setSummonTerm(si.getValue(x, slv));
                    summon.setMoveAbility(MoveAbility.WalkRandom);
                    field.spawnAddSummon(summon);
                }
            }

        }
    }

    private void applyRaptorTalonsOnMob(AttackInfo attackInfo) {
        Option o1 = new Option();
        if (!chr.hasSkill(RAPTOR_TALONS)) {
            return;
        }
        Skill skill = chr.getSkill(RAPTOR_TALONS);
        SkillInfo si = SkillData.getSkillInfoById(skill.getSkillId());
        byte slv = (byte) skill.getCurrentLevel();
        for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
            Mob mob = (Mob) chr.getField().getLifeByObjectID(mai.mobId);
            if (mob == null) {
                return;
            }
            MobTemporaryStat mts = mob.getTemporaryStat();
            if (Util.succeedProp(si.getValue(prop, slv))) {
                mts.createAndAddBurnedInfo(chr, skill.getSkillId(), slv);
            }
        }
    }

    @Override
    public int getFinalAttackSkill() {
        fortFollowUpAddAttack++;
        if (isBearMode() && chr.hasSkill(FORT_FOLLOW_UP) && fortFollowUpAddAttack >= 4) {
            fortFollowUpAddAttack = 0;
            return FORT_FOLLOW_UP;
        }

        return 0;
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
        Option o4 = new Option();
        Option o5 = new Option();
        Summon summon;
        Field field;
        switch (skillID) {
            case EKA_EXPRESS: //TODO Eka Express Skill
                Field townField = FieldData.getFieldById(chr.getField().getReturnMap());
                int portalX = townField.getPortalByName("tp").getX();
                int portalY = townField.getPortalByName("tp").getY();
                Position townPosition = new Position(portalX, portalY); // Grabs the Portal Co-ordinates for the TownPortalPoint
                int duration = si.getValue(time, slv);
                if (chr.getTownPortal() != null) {
                    TownPortal townPortal = chr.getTownPortal();
                    townPortal.despawnTownPortal();
                }
                TownPortal townPortal = new TownPortal(chr, townPosition, chr.getPosition(), chr.getField().getReturnMap(), chr.getFieldID(), skillID, duration);
                townPortal.spawnTownPortal();
                chr.dispose();
                break;
            case MEOW_CURE:
                tsm.removeAllDebuffs();
                break;
            case MEOW_HEAL:
                chr.heal((int) (chr.getMaxHP() / ((double) 100 / si.getValue(hp, slv))));
                break;
            case MEOW_REVIVE:
                Party party = chr.getParty();
                if (party != null) {
                    field = chr.getField();
                    Rect rect = chr.getPosition().getRectAround(si.getRects().get(0));
                    if (!chr.isLeft()) {
                        rect = rect.moveRight();
                    }
                    List<PartyMember> eligblePartyMemberList = field.getPartyMembersInRect(chr, rect).stream().
                            filter(pml -> pml.getChr().getId() != chr.getId() &&
                                    pml.getChr().getHP() <= 0).
                            collect(Collectors.toList());

                    if (eligblePartyMemberList.size() > 0) {
                        Char randomPartyChr = Util.getRandomFromCollection(eligblePartyMemberList).getChr();
                        TemporaryStatManager partyTSM = randomPartyChr.getTemporaryStatManager();
                        randomPartyChr.heal(randomPartyChr.getMaxHP());
                        partyTSM.putCharacterStatValue(NotDamaged, o1);
                        partyTSM.sendSetStatPacket();
                        randomPartyChr.write(UserPacket.effect(Effect.skillAffected(skillID, (byte) 1, 0)));
                        randomPartyChr.getField().broadcastPacket(UserRemote.effect(randomPartyChr.getId(), Effect.skillAffected(skillID, (byte) 1, 0)));
                    }
                }
                break;
            case BEAR_MODE:
            case SNOW_LEOPARD_MODE:
            case HAWK_MODE:
            case CAT_MODE:
                o1.nOption = (skillID - 110001500);
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(BeastMode, o1);

                for (int modeId : buffsByMode.keySet()) {
                    if (skillID == modeId) {
                        continue;
                    }
                    for (int buffId : buffsByMode.get(modeId)) {
                        tsm.removeStatsBySkill(buffId);
                        tsm.sendResetStatPacket();
                    }
                }
                break;

            //Bear Mode
            case LIL_FORT:
                summon = Summon.getSummonBy(chr, skillID, slv);
                field = chr.getField();
                summon.setFlyMob(false);
                summon.setSummonTerm(si.getValue(time, slv));
                summon.setMoveAbility(MoveAbility.Stop);
                field.spawnSummon(summon);
                break;
            case BEAR_ASSAULT:
                o1.nOption = si.getValue(x, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(q, slv);
                tsm.putCharacterStatValue(DamR, o1);
                o2.nOption = si.getValue(y, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(q, slv);
                tsm.putCharacterStatValue(IncCriticalDam, o2);
                o3.nOption = si.getValue(z, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(q, slv);
                tsm.putCharacterStatValue(CriticalBuff, o3);
                o4.nOption = si.getValue(mobCount, slv);
                o4.rOption = skillID;
                o4.tOption = si.getValue(q, slv);
                tsm.putCharacterStatValue(Enrage, o4);
                break;

            //Leopard Mode
            case BRO_ATTACK:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ACC, o1);
                break;

            //Hawk Mode
            case FLY:
                o1.nOption = 1;
                o1.rOption = skillID;
                o1.tOption = 0;
                tsm.putCharacterStatValue(NewFlying, o1);

                //TODO  summon Defensive Formation
                break;
            case HAWK_FLOCK:
                o1.nOption = si.getValue(speed, slv);
                o1.rOption = skillID;
                o1.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Speed, o1);
                o2.nOption = si.getValue(jump, slv);
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(Jump, o2);
                break;
            case RAPTOR_TALONS:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieMad, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMAD, o1);
                break;
            case BIRDS_EYE_VIEW:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieCr, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieCr, o1);
                o3.nOption = si.getValue(epdd, slv);
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EDEF, o3);
                o4.nOption = si.getValue(acc, slv);
                o4.rOption = skillID;
                o4.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(ACC, o4);
                o5.nOption = si.getValue(eva, slv);
                o5.rOption = skillID;
                o5.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(EVA, o5);
                break;
            case RAZOR_BEAK:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieMad, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieMAD, o1);
                o2.nReason = skillID;
                o2.nValue = si.getValue(indiePad, slv);
                o2.tStart = Util.getCurrentTime();
                o2.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndiePAD, o2);
                break;

            //Cat Mode
            case MEOW_CARD:
            case MEOW_CARD_GOLD_SKILL:
                giveMeowCard(slv);
                break;

            //Hyper
            case TEAM_ROAR:
                o1.nReason = skillID;
                o1.nValue = si.getValue(indieDamR, slv);
                o1.tStart = Util.getCurrentTime();
                o1.tTerm = si.getValue(time, slv);
                tsm.putCharacterStatValue(IndieAsrR, o1);
                o2.nOption = 1;
                o2.rOption = skillID;
                o2.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(NotDamaged, o2);
                o3.nOption = 1;
                o3.rOption = skillID;
                o3.tOption = si.getValue(time, slv);
                tsm.putCharacterStatValue(TeamRoar, o3);
                break;
        }
        tsm.sendSetStatPacket();
        chr.dispose();
    }

    public static void beastTamerRegroup(Char chr) { //Handled in WorldHandler
        Party party = chr.getParty();
        if (party != null) {
            for (PartyMember pm : party.getOnlineMembers()) {
                Char pmChr = pm.getChr();
                if (pmChr.getId() != chr.getId() && pmChr.getClient().getChannel() == chr.getClient().getChannel() && pmChr.getLevel() > 9) {
                    pmChr.warp(chr.getField());
                    pmChr.write(FieldPacket.teleport(chr.getPosition(), pmChr));
                }
                pmChr.dispose();
            }
        }
    }


    // Hit related methods ---------------------------------------------------------------------------------------------

    @Override
    public void handleHit(Char chr, HitInfo hitInfo) {

        super.handleHit(chr, hitInfo);
    }

    public void reviveByBearReborn() { // TODO
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        chr.heal(chr.getMaxHP());
        tsm.removeStatsBySkill(BEAR_REBORN);
        tsm.sendResetStatPacket();
        chr.chatMessage("You have been revived by Bear Reborn.");
        chr.write(UserPacket.effect(Effect.skillAffected(BEAR_REBORN, (byte) 1, 0)));
        chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillAffected(BEAR_REBORN, (byte) 1, 0)));
    }

    // Character creation related methods ------------------------------------------------------------------------------

    @Override
    public void setCharCreationStats(Char chr) {
        super.setCharCreationStats(chr);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();
        cs.setPosMap(866101000);
        cs.setJob(11212);
    }

    @Override
    public void handleSetJob(short jobId) {
        if (JobConstants.isBeastTamer(jobId)) {
            // TODO: tutorial questline to give these skills
            chr.addSkill(BEAR_MODE, 1, 1);
            chr.addSkill(SNOW_LEOPARD_MODE, 1, 1);
            chr.addSkill(HAWK_MODE, 1, 1);
            chr.addSkill(CAT_MODE, 1, 1);
            chr.addSkill(HOMEWARD_BOUND, 1, 1);
        }
        super.handleSetJob(jobId);
    }
}