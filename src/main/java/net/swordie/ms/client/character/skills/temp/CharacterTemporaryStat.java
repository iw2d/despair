package net.swordie.ms.client.character.skills.temp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 1/2/2018.
 */
public enum CharacterTemporaryStat implements Comparator<CharacterTemporaryStat> {
    IndiePAD(0),
    IndieMAD(1),
    IndieDEF(2),
    IndieMHP(3),
    IndieMHPR(4),
    IndieMMP(5),
    IndieMMPR(6),
    IndieACC(7),
    IndieEVA(8),
    IndieJump(9),
    IndieSpeed(10),
    IndieAllStat(11),
    IndieAllStatR(12),
    IndieDodgeCriticalTime(13),
    IndieEXP(14),
    IndieBooster(15),
    IndieFixedDamageR(16),
    PyramidStunBuff(17),
    PyramidFrozenBuff(18),
    PyramidFireBuff(19),
    PyramidBonusDamageBuff(20),
    IndieRelaxEXP(21), // idk
    IndieDEX(22),
    IndieINT(23),
    IndieLUK(24),
    IndieDamR(25),
    IndieAsrR(26), // idk
    IndieScriptBuff(27),
    IndieMDF(28),
    IndieIDK(29),
    IndieTerR(30),
    IndieCr(31),
    IndieDEFR(32),
    IndieCrDmg(33),
    IndieBDR(34),
    IndieStatR(35),
    IndieStance(36),
    IndieIgnoreMobpdpR(37),
    IndieEmpty(38),
    IndiePADR(39),
    IndieMADR(40),
    IndieEVAR(41),
    IndieDrainHP(42),
    IndiePMdR(43),
    IndieForceJump(44),
    IndieForceSpeed(45),
    IndieQrPointTerm(46),
    IndieStatCount(54),


    PAD(55), // from 57
    DEF(56),
    MAD(57),
    ACC(58),
    EVA(59),
    Craft(60),
    Speed(61),
    Jump(62), // from 65
    MagicGuard(63),
    DarkSight(64),
    Booster(65),
    PowerGuard(66), // from 69
    MaxHP(67),
    MaxMP(68),
    Invincible(69),
    SoulArrow(70),
    Stun(71),
    Poison(72),
    Seal(73),
    Darkness(74),
    ComboCounter(75),
    WeaponCharge(76),
    HolySymbol(77), // from 80
    MesoUp(78),
    ShadowPartner(79),
    PickPocket(80),
    MesoGuard(81),
    Thaw(82),
    Weakness(83), // from 86
    Curse(84),
    Slow(85), // from 88
    Morph(86), // from 89
    Regen(87), // from 90
    BasicStatUp(88),
    Stance(89),
    SharpEyes(90), // from 93
    ManaReflection(91),
    Attract(92), // from 95
    NoBulletConsume(93), // from 96
    Infinity(94), // from 97
    AdvancedBless(95), // from 98
    IllusionStep(96),
    Blind(97),
    Concentration(98),
    BanMap(99),
    MaxLevelBuff(100), // from 103 - is this echo of the hero?!

    MesoUpByItem(104),
    Ghost(102), // from 105
    Barrier(103), // from 106
    ReverseInput(104),
    ItemUpByItem(105),
    RespectPImmune(106),
    RespectMImmune(117),
    DefenseAtt(108),

    DefenseState(109),
    DojangBerserk(110),
    DojangInvincible(111),
    DojangShield(112), // from 115
    SoulMasterFinal(113),
    WindBreakerFinal(114),
    ElementalReset(115),
    HideAttack(116),
    EventRate(117),
    ComboAbilityBuff(118),
    ComboDrain(119),
    ComboBarrier(120), // from 123
    BodyPressure(121), // from 124
    RepeatEffect(122),
    ExpBuffRate(123),
    StopPortion(124),
    StopMotion(125),
    Fear(126), // from 129
    HiddenPieceOn(127),
    MagicShield(128), // from 131
    MagicResistance(129),
    SoulStone(130),
    Flying(131), // from 134
    Frozen(132), // from 135
    AssistCharge(133),
    Enrage(134), // from 137
    DrawBack(135),
    NotDamaged(136),
    FinalCut(137),
    HowlingAttackDamage(138),
    BeastFormDamageUp(139),
    Dance(140), // from 143

    EMHP(141), // from 144
    EMMP(142),
    EPAD(143),
    EMAD(144),
    EDEF(145), // from 145 (EPDD)
    Guard(146), // from 150
    Cyclone(147), // from 151
    HowlingCritical(148), // from 152
    HowlingMaxMP(149),
    HowlingDefence(150),
    HowlingEvasion(151),
    Conversion(152),
    Revive(153),
    PinkbeanMinibeenMove(154),
    Sneak(155),

    Mechanic(156), // from 160
    BeastFormMaxHP(157), // from 161
    Dice(158),
    BlessingArmor(159), // from 163
    DamR(160), // from 164
    TeleportMasteryOn(161),
    CombatOrders(162),
    Beholder(163),

    DispelItemOption(164), // from 168
    Inflation(165),
    OnixDivineProtection(166),
    Web(167), // from 171
    Bless(168),
    TimeBomb(169), // from 173
    Disorder(170),
    Thread(171),

    Team(172),
    Explosion(173),
    BuffLimit(174), // from 178
    STR(175),
    INT(176),
    DEX(177),
    LUK(178), // from 182
    DispelItemOptionByField(179), // from 183

    DarkTornado(180), // Cygnus Attack
    PVPDamage(181),
    PvPScoreBonus(182),
    PvPInvincible(183),
    PvPRaceEffect(184),
    WeaknessMdamage(185), // from 185
    Frozen2(186), // from 186
    PVPDamageSkill(187),
    AmplifyDamage(188),
    IceKnight(189),
    Shock(190), // from 194
    InfinityForce(191),
    IncMaxHP(192), // from 196
    IncMaxMP(193), // from 197
    HolyMagicShell(194), // from 198
    KeyDownTimeIgnore(195), // from 199
    ArcaneAim(196),
    MasterMagicOn(197), // from 201
    AsrR(198), // from 202
    TerR(200),
    DamAbsorbShield(200), // from 204
    DevilishPower(201),
    Roulette(202), // from 206
    SpiritLink(203),

    AsrRByItem(204), // from 208
    Event(205),
    CriticalBuff(206), // from 210
    DropRate(207),
    PlusExpRate(208),
    ItemInvincible(209),
    Awake(210),
    ItemCritical(211),
    // ItemEvade Removed
    Event2(212), // from 217
    VampiricTouch(213), // from 218
    DDR(214),
    IncCriticalDam(215), // TODO check
    IncTerR(216),
    IncAsrR(217), // from 223
    DeathMark(218),
    UsefulAdvancedBless(219), // from 225
    Lapidification(220),
    VenomSnake(221), // from 227
    CarnivalAttack(222),
    CarnivalDefence(223),
    CarnivalExp(224),
    SlowAttack(225),
    PyramidEffect(226),
    KillingPoint(227),
    HollowPointBullet(228),
    KeyDownMoving(229), // from 235
    IgnoreTargetDEF(230), // from 236
    ReviveOnce(231),
    Invisible(232), // from 238
    EnrageCr(233),
    EnrageCrDam(234), // from 240
    Judgement(235),
    DojangLuckyBonus(236),
    PainMark(237),
    Magnet(238), // from 244
    MagnetArea(239), // from 245
    Unk240(240), // new v178
    Unk241(241), // new v178
    VampDeath(242),
    BlessingArmorIncPAD(243),

    KeyDownAreaMoving(244),
    Larkness(245),
    StackBuff(246), // from 250
    BlessOfDarkness(247),
    AntiMagicShell(248),
    LifeTidal(249),
    HitCriDamR(250),
    SmashStack(251),
    PartyBarrier(252), // from 256
    ReshuffleSwitch(253),
    SpecialAction(254),
    VampDeathSummon(255), // from 259
    StopForceAtomInfo(256),
    SoulGazeCriDamR(257),
    SoulRageCount(258),
    PowerTransferGauge(259),
    AffinitySlug(260),
    Trinity(261), // from 265
    IncMaxDamage(262),
    BossShield(263),
    MobZoneState(264),
    GiveMeHeal(265),
    TouchMe(266),
    Contagion(267),
    ComboUnlimited(268), // from 272
    SoulExalt(269), // from 273
    IgnorePCounter(270),
    IgnoreAllCounter(271),
    IgnorePImmune(272),
    IgnoreAllImmune(273),
    FinalJudgement(274), // from 278
    Unk275(275), // TODO check from BossShield ~ Unk275
    IceAura(276), // from 279
    FireAura(277),
    VengeanceOfAngel(278),
    HeavensDoor(279),
    Preparation(280),
    BullsEye(281),
    IncEffectHPPotion(282),
    IncEffectMPPotion(283), // from 286
    BleedingToxin(284), // from 287

    IgnoreMobDamR(285), // reserve for RuneStone LIBERATE_THE_RECOVERY_RUNE
    Asura(286),
    OmegaBlaster(287), // new v178?
    FlipTheCoin(288),
    UnityOfPower(289),
    Stimulate(290),
    ReturnTeleport(291),
    DropRIncrease(292), // from 294
    IgnoreMobpdpR(293),
    BdR(294),
    CapDebuff(295), // from 297
    Exceed(296),
    DiabolikRecovery(297),
    FinalAttackProp(298),
    ExceedOverload(299),
    OverloadCount(300), // from 302
    BuckShot(301),
    FireBomb(302),
    HalfstatByDebuff(303),
    SurplusSupply(304),
    SetBaseDamage(305),
    EVAR(306), // from 308
    NewFlying(307), // from 309
    AmaranthGenerator(308),
    OnCapsule(309), // from 311
    CygnusElementSkill(310), // from 312
    StrikerHyperElectric(311),
    EventPointAbsorb(312),
    EventAssemble(313),
    StormBringer(314),
    ACCR(315),
    DEXR(316),
    Albatross(317),
    Translucence(318),
    PoseType(319),
    LightOfSpirit(320),
    ElementSoul(321),
    GlimmeringTime(322),
    TrueSight(323),
    SoulExplosion(324),
    SoulMP(325),
    FullSoulMP(326),
    SoulSkillDamageUp(327),
    ElementalCharge(328), // from 330
    Restoration(329),
    CrossOverChain(330),
    ChargeBuff(331),
    Reincarnation(332),
    KnightsAura(333), // from 335
    ChillingStep(334),
    DotBasedBuff(335),
    BlessEnsenble(336),
    ComboCostInc(337),
    ExtremeArchery(338),
    NaviFlying(339), // from 341
    QuiverCatridge(340),
    AdvancedQuiver(341),
    UserControlMob(342),
    ImmuneBarrier(343),
    ArmorPiercing(344),
    ZeroAuraStr(345),
    ZeroAuraSpd(346),
    CriticalGrowing(347),
    QuickDraw(348),
    BowMasterConcentration(349),
    TimeFastABuff(350),
    TimeFastBBuff(351),
    GatherDropR(352),
    AimBox2D(353), // from 355
    IncMonsterBattleCaptureRate(354),
    CursorSniping(355), // from 357
    DebuffTolerance(356),
    Unk357(357), // new v178?
    DotHealHPPerSecond(358),
    SpiritGuard(359),
    PreReviveOnce(360),
    SetBaseDamageByBuff(361),
    LimitMP(362),
    ReflectDamR(363),
    ComboTempest(364),
    MHPCutR(365),
    MMPCutR(366),
    SelfWeakness(367),
    ElementDarkness(368),
    FlareTrick(369),
    Ember(370),
    Dominion(371), // from 372
    SiphonVitality(372),
    DarknessAscension(373),
    BossWaitingLinesBuff(374),
    DamageReduce(375),
    ShadowServant(376),
    ShadowIllusion(377),
    KnockBack(378),
    AddAttackCount(379), // from 380
    ComplusionSlant(380),
    JaguarSummoned(381),
    JaguarCount(382),
    SSFShootingAttack(383), // from 384
    DevilCry(384), // TODO: check the CTS onwards
    ShieldAttack(386),
    BMageAura(387),
    DarkLighting(388),
    AttackCountX(389),
    BMageDeath(390),
    BombTime(391),
    NoDebuff(392),
    BattlePvPMikeShield(393),
    BattlePvPMikeBugle(394),
    XenonAegisSystem(395),
    AngelicBursterSoulSeeker(396),
    HiddenPossession(397),
    NightWalkerBat(398),
    NightLordMark(399),

    WizardIgnite(400),
    FireBarrier(401),
    ChangeFoxMan(402),
    BattlePvPHelenaMark(403),
    BattlePvPHelenaWindSpirit(404),
    BattlePvPLangEProtection(405),
    BattlePvPLeeMalNyunScaleUp(406),
    BattlePvPRevive(407),

    PinkbeanAttackBuff(408),
    PinkbeanRelax(409),
    PinkbeanRollingGrade(410),
    PinkbeanYoYoStack(411),
    RandAreaAttack(412),
    NextAttackEnhance(413),
    AranBeyonderDamAbsorb(414),
    AranCombotempastOption(415),

    NautilusFinalAttack(416),
    ViperTimeLeap(417),
    RoyalGuardState(418),
    RoyalGuardPrepare(419),
    MichaelSoulLink(420),
    MichaelStanceLink(421),
    TriflingWhimOnOff(422),
    AddRangeOnOff(423),

    KinesisPsychicPoint(424),
    KinesisPsychicOver(425),
    KinesisPsychicShield(426),
    KinesisIncMastery(427),
    KinesisPsychicEnergeShield(428),
    BladeStance(429),
    DebuffActiveSkillHPCon(430),
    DebuffIncHP(431),

    BowMasterMortalBlow(432),
    AngelicBursterSoulResonance(433),
    Fever(434),
    IgnisRore(435),
    RpSiksin(436),
    TeleportMasteryRange(437),
    FixCoolTime(438),
    IncMobRateDummy(439),

    AdrenalinBoost(440),
    AranSmashSwing(441),
    AranDrain(442),
    AranBoostEndHunt(443),
    HiddenHyperLinkMaximization(444),
    RWCylinder(445),
    RWCombination(446),
    RWMagnumBlow(447),

    RWBarrier(448),
    RWBarrierHeal(449),
    RWMaximizeCannon(450),
    RWOverHeat(451),
    UsingScouter(452),
    RWMovingEvar(453),
    Stigma(454),
    Unk455(455),

    Unk456(456),
    Unk457(457),
    Unk458(458),
    Unk459(459),
    Unk460(460),
    HayatoStance(461),
    HayatoStanceBonus(462),
    EyeForEye(463),

    WillowDodge(464),
    Unk465(465),
    HayatoPAD(466),
    HayatoHPR(467),
    HayatoMPR(468),
    HayatoBooster(469),
    Unk470(470),
    Unk471(471),

    Jinsoku(472),
    HayatoCr(473),
    HakuBlessing(474),
    HayatoBoss(475),
    BattoujutsuAdvance(476),
    Unk477(477),
    Unk478(478),
    BlackHeartedCurse(479),

    BeastMode(480),
    TeamRoar(481),
    Unk482(482),
    Unk483(483),
    Unk484(484),
    Unk485(485),
    Unk486(486),
    Unk487(487),

    Unk488(488),
    Unk489(489),
    Unk490(490),
    Unk491(491),

    EnergyCharged(492),
    DashSpeed(493),
    DashJump(494),
    RideVehicle(495),
    PartyBooster(496),
    GuidedBullet(497),
    Undead(498),
    RideVehicleExpire(499),
    ;

    private int bitPos;
    private int val;
    private int pos;
    public static final int length = 17;
    private static final Logger log = LogManager.getRootLogger();

    private static final List<CharacterTemporaryStat> ORDER = Arrays.asList(
            STR, INT, DEX, LUK, PAD, DEF, MAD, ACC, EVA, EVAR, Craft, Speed, Jump, EMHP, EMMP, EPAD, EMAD, EDEF,
            MagicGuard, DarkSight, Booster, PowerGuard, Guard, MaxHP, MaxMP, Invincible, SoulArrow, Stun, Shock,
            Poison, Seal, Darkness, ComboCounter, WeaponCharge, ElementalCharge, HolySymbol, MesoUp, ShadowPartner,
            PickPocket, MesoGuard, Thaw, Weakness, WeaknessMdamage, Curse, Slow, TimeBomb, BuffLimit, Team, Disorder,
            Thread, Morph, Ghost, BasicStatUp, Stance, SharpEyes, ManaReflection, Attract, Magnet, MagnetArea,
            Unk240, Unk241, NoBulletConsume, StackBuff, Trinity, Infinity, AdvancedBless, IllusionStep, Blind,
            Concentration, BanMap, MaxLevelBuff, Barrier, DojangShield, ReverseInput, MesoUpByItem, ItemUpByItem,
            RespectPImmune, RespectMImmune, DefenseAtt, DefenseState, DojangBerserk, DojangInvincible, SoulMasterFinal,
            WindBreakerFinal, ElementalReset, HideAttack, EventRate, ComboAbilityBuff, ComboDrain, ComboBarrier,
            PartyBarrier, BodyPressure, RepeatEffect, ExpBuffRate, StopPortion, StopMotion, Fear, MagicShield,
            MagicResistance, SoulStone, Flying, NewFlying, NaviFlying, Frozen, Frozen2, Web, Enrage, NotDamaged,
            FinalCut, HowlingAttackDamage, BeastFormDamageUp, Dance, Cyclone, OnCapsule, HowlingCritical, HowlingMaxMP,
            HowlingDefence, HowlingEvasion, Conversion, Revive, PinkbeanMinibeenMove, Sneak, Mechanic, DrawBack,
            BeastFormMaxHP, Dice, BlessingArmor, BlessingArmorIncPAD, DamR, TeleportMasteryOn, CombatOrders,
            Beholder, DispelItemOption, DispelItemOptionByField, Inflation, OnixDivineProtection, Bless, Explosion,
            DarkTornado, IncMaxHP, IncMaxMP, PVPDamage, PVPDamageSkill, PvPScoreBonus, PvPInvincible, PvPRaceEffect,
            IceKnight, HolyMagicShell, InfinityForce, AmplifyDamage, KeyDownTimeIgnore, MasterMagicOn, AsrR, AsrRByItem,
            TerR, DamAbsorbShield, Roulette, Event, SpiritLink, CriticalBuff, DropRate, PlusExpRate, ItemInvincible,
            ItemCritical, Event2, VampiricTouch, DDR, IncCriticalDam, IncTerR, IncAsrR, DeathMark, PainMark,
            UsefulAdvancedBless, Lapidification, VampDeath, VampDeathSummon, VenomSnake, CarnivalAttack,
            CarnivalDefence, CarnivalExp, SlowAttack, PyramidEffect, HollowPointBullet, KeyDownMoving,
            KeyDownAreaMoving, CygnusElementSkill, IgnoreTargetDEF, Invisible, ReviveOnce, AntiMagicShell, EnrageCr,
            EnrageCrDam, BlessOfDarkness, LifeTidal, Judgement, DojangLuckyBonus, HitCriDamR, Larkness, SmashStack,
            ReshuffleSwitch, SpecialAction, ArcaneAim, StopForceAtomInfo, SoulGazeCriDamR, SoulRageCount,
            PowerTransferGauge, AffinitySlug, SoulExalt, HiddenPieceOn, BossShield, MobZoneState, GiveMeHeal, TouchMe,
            Contagion, ComboUnlimited, IgnorePCounter, IgnoreAllCounter, IgnorePImmune, IgnoreAllImmune, FinalJudgement,
            Unk275, KnightsAura, IceAura, FireAura, VengeanceOfAngel, HeavensDoor, Preparation, BullsEye,
            IncEffectHPPotion, IncEffectMPPotion, SoulMP, SoulSkillDamageUp, BleedingToxin, IgnoreMobDamR, Asura,
            OmegaBlaster, FlipTheCoin, UnityOfPower, Stimulate, ReturnTeleport, CapDebuff, DropRIncrease, IgnoreMobpdpR,
            BdR, Exceed, DiabolikRecovery, FinalAttackProp, ExceedOverload, DevilishPower, OverloadCount, BuckShot,
            FireBomb, HalfstatByDebuff, SurplusSupply, SetBaseDamage, AmaranthGenerator, StrikerHyperElectric,
            EventPointAbsorb, EventAssemble, StormBringer, ACCR, DEXR, Albatross, Translucence, PoseType, LightOfSpirit,
            ElementSoul, GlimmeringTime, Restoration, ComboCostInc, ChargeBuff, TrueSight, CrossOverChain, ChillingStep,
            Reincarnation, DotBasedBuff, BlessEnsenble, ExtremeArchery, QuiverCatridge, AdvancedQuiver, UserControlMob,
            ImmuneBarrier, ArmorPiercing, ZeroAuraStr, ZeroAuraSpd, CriticalGrowing, QuickDraw, BowMasterConcentration,
            TimeFastABuff, TimeFastBBuff, GatherDropR, AimBox2D, CursorSniping,  IncMonsterBattleCaptureRate,
            DebuffTolerance, Unk275, DotHealHPPerSecond, SpiritGuard, PreReviveOnce, SetBaseDamageByBuff, LimitMP,
            ReflectDamR, ComboTempest, MHPCutR, MMPCutR, SelfWeakness, ElementDarkness, FlareTrick, Ember, Dominion,
            SiphonVitality, DarknessAscension, BossWaitingLinesBuff, DamageReduce, ShadowServant, ShadowIllusion,
            AddAttackCount, ComplusionSlant, JaguarSummoned, JaguarCount, SSFShootingAttack, DevilCry, ShieldAttack,
            BMageAura, DarkLighting, AttackCountX, BMageDeath, BombTime, NoDebuff, XenonAegisSystem,
            AngelicBursterSoulSeeker, HiddenPossession, NightWalkerBat, NightLordMark, WizardIgnite,
            BattlePvPHelenaMark, BattlePvPHelenaWindSpirit, BattlePvPLangEProtection, BattlePvPLeeMalNyunScaleUp,
            BattlePvPRevive, PinkbeanAttackBuff, RandAreaAttack, BattlePvPMikeShield, BattlePvPMikeBugle, PinkbeanRelax,
            PinkbeanYoYoStack, NextAttackEnhance, AranBeyonderDamAbsorb, AranCombotempastOption, NautilusFinalAttack,
            ViperTimeLeap, RoyalGuardState, RoyalGuardPrepare, MichaelSoulLink, MichaelStanceLink, TriflingWhimOnOff,
            AddRangeOnOff, KinesisPsychicPoint, KinesisPsychicOver, KinesisPsychicShield, KinesisIncMastery,
            KinesisPsychicEnergeShield, BladeStance, DebuffActiveSkillHPCon, DebuffIncHP, BowMasterMortalBlow,
            AngelicBursterSoulResonance, Fever, IgnisRore, RpSiksin, TeleportMasteryRange, FireBarrier, ChangeFoxMan,
            FixCoolTime, IncMobRateDummy, AdrenalinBoost, AranSmashSwing, AranDrain, AranBoostEndHunt,
            HiddenHyperLinkMaximization, RWCylinder, RWCombination, RWMagnumBlow, RWBarrier, RWBarrierHeal,
            RWMaximizeCannon, RWOverHeat, RWMovingEvar, Stigma, Unk455, IncMaxDamage, Unk456, Unk457, Unk458, Unk459,
            Unk460, PyramidFireBuff, HayatoStance, HayatoBooster, HayatoStanceBonus, WillowDodge, Unk465,
            HayatoPAD, HayatoHPR, HayatoMPR, Jinsoku, HayatoCr, HakuBlessing, HayatoBoss, BattoujutsuAdvance, Unk477,
            Unk478, BlackHeartedCurse, EyeForEye, BeastMode, TeamRoar, Unk482, Unk483, Unk487, Unk488, Unk489, Unk490,
            Unk491
        );

    private static final List<CharacterTemporaryStat> REMOTE_ORDER = Arrays.asList(
            Speed, ComboCounter, WeaponCharge, ElementalCharge, Stun, Shock, Darkness, Seal, Weakness, WeaknessMdamage,
            Curse, Slow, PvPRaceEffect, TimeBomb, Team, Disorder, Thread, Poison, Poison, ShadowPartner, DarkSight,
            SoulArrow, Morph, Ghost, Attract, Magnet, MagnetArea, NoBulletConsume, BanMap, Barrier, DojangShield,
            ReverseInput, RespectPImmune, RespectMImmune, DefenseAtt, DefenseState, DojangBerserk, DojangInvincible,
            RepeatEffect, Unk483, StopPortion, StopMotion, Fear, MagicShield, Flying, Frozen, Frozen2, Web, DrawBack,
            FinalCut, OnCapsule, OnCapsule, Sneak, BeastFormDamageUp, Mechanic, BlessingArmor, BlessingArmorIncPAD,
            Inflation, Explosion, DarkTornado, AmplifyDamage, HideAttack, HolyMagicShell, DevilishPower, SpiritLink,
            Event, VampiricTouch, DeathMark, PainMark, Lapidification, VampDeath, VampDeathSummon, VenomSnake,
            PyramidEffect, KillingPoint, PinkbeanRollingGrade, IgnoreTargetDEF, Invisible, Judgement, KeyDownAreaMoving,
            StackBuff, BlessOfDarkness, Larkness, ReshuffleSwitch, SpecialAction, StopForceAtomInfo, SoulGazeCriDamR,
            PowerTransferGauge, AffinitySlug, SoulExalt, HiddenPieceOn, SmashStack, MobZoneState, GiveMeHeal, TouchMe,
            Contagion, Contagion, ComboUnlimited, IgnorePCounter, IgnoreAllCounter, IgnorePImmune, IgnoreAllImmune,
            FinalJudgement, KnightsAura, IceAura, FireAura, VengeanceOfAngel, HeavensDoor, DamAbsorbShield,
            AntiMagicShell, NotDamaged, BleedingToxin, WindBreakerFinal, IgnoreMobDamR, Asura, OmegaBlaster,
            UnityOfPower, Stimulate, ReturnTeleport, CapDebuff, OverloadCount, FireBomb, SurplusSupply, NewFlying,
            NaviFlying, AmaranthGenerator, CygnusElementSkill, StrikerHyperElectric, EventPointAbsorb, EventAssemble,
            Albatross, Translucence, PoseType, LightOfSpirit, ElementSoul, GlimmeringTime, Reincarnation, Beholder,
            QuiverCatridge, ArmorPiercing, UserControlMob, ZeroAuraStr, ZeroAuraSpd, ImmuneBarrier, ImmuneBarrier,
            FullSoulMP, Dance, SpiritGuard, ComboTempest, HalfstatByDebuff, ComplusionSlant, JaguarSummoned, BMageAura,
            BombTime, DarkLighting, AttackCountX, FireBarrier, KeyDownMoving, MichaelSoulLink,
            KinesisPsychicEnergeShield, BladeStance, BladeStance, Fever, AdrenalinBoost, RWBarrier, RWMagnumBlow,
            Stigma, CursorSniping, BeastMode, TeamRoar, HayatoStance, HayatoBooster, HayatoStanceBonus,
            HayatoPAD, HayatoHPR, HayatoMPR, HayatoCr, HayatoBoss, Stance, BattoujutsuAdvance, Unk484,
            BlackHeartedCurse, EyeForEye, Unk458, Unk487, Unk488, Unk489, Unk491, Unk460
    );

    private static final List<CharacterTemporaryStat> INDIE_ORDER = Arrays.asList(
            IndiePAD, IndieMAD, IndieDEF, IndieMHP, IndieMHPR, IndieMMP, IndieMMPR, IndieACC, IndieEVA,
            IndieJump, IndieSpeed, IndieAllStat, IndieDodgeCriticalTime, IndieEXP, IndieBooster, IndieFixedDamageR,
            PyramidStunBuff, PyramidFrozenBuff, PyramidFireBuff, PyramidBonusDamageBuff, IndieRelaxEXP, IndieDEX,
            IndieINT, IndieLUK, IndieDamR, IndieAsrR, IndieScriptBuff, IndieMDF, IndieIDK, IndieTerR, IndieCr,
            IndieDEFR, IndieBDR, IndieStatR, IndieStance, IndieIgnoreMobpdpR, IndieEmpty, IndiePADR, IndieMADR,
            IndieEVAR, IndieDrainHP, IndiePMdR, IndieForceJump, IndieForceSpeed, IndieQrPointTerm, IndieStatCount
    );

    private static final List<CharacterTemporaryStat> ENCODE_INT = Arrays.asList(
            RideVehicle, RideVehicleExpire, CarnivalDefence, SpiritLink, DojangLuckyBonus, SoulGazeCriDamR,
            PowerTransferGauge, ReturnTeleport, ShadowPartner, AranSmashSwing, IncMaxDamage, Unk487, SetBaseDamage,
            QuiverCatridge, ImmuneBarrier, NaviFlying, Dance, SetBaseDamageByBuff, DotHealHPPerSecond, Magnet,
            MagnetArea, VampDeath, VampDeathSummon, Cyclone, RWBarrier
    );

    public static final EnumSet<CharacterTemporaryStat> MOVING_AFFECTING_STAT = EnumSet.of(
            Speed, Jump, Stun, Weakness, Slow, Morph, Ghost, BasicStatUp, Attract, DashSpeed, DashJump, Flying, Frozen,
            Frozen2, Lapidification, IndieSpeed, IndieJump, KeyDownMoving, EnergyCharged, Mechanic, Magnet, MagnetArea,
            VampDeath, VampDeathSummon, GiveMeHeal, DarkTornado, NewFlying, NaviFlying, UserControlMob, Dance,
            SelfWeakness, BattlePvPHelenaWindSpirit, BattlePvPLeeMalNyunScaleUp, TouchMe, IndieForceSpeed,
            IndieForceJump, RideVehicle, RideVehicleExpire
    );

    public static final EnumSet<CharacterTemporaryStat> RESET_BY_TIME_CTS = EnumSet.of(
            Stun, Shock, Poison, Seal, Darkness, Weakness, WeaknessMdamage, Curse, Slow, /*TimeBomb,*/
            Disorder, Thread, Attract, Magnet, MagnetArea, ReverseInput, BanMap, StopPortion, StopMotion,
            Fear, Frozen, Frozen2, Web, NotDamaged, FinalCut, Lapidification, VampDeath, VampDeathSummon,
            GiveMeHeal, TouchMe, Contagion, ComboUnlimited, CrossOverChain, Reincarnation, ComboCostInc,
            DotBasedBuff, ExtremeArchery, QuiverCatridge, AdvancedQuiver, UserControlMob, ArmorPiercing,
            CriticalGrowing, QuickDraw, BowMasterConcentration, ComboTempest, SiphonVitality, KnockBack, RWMovingEvar
    );

    public static List<CharacterTemporaryStat> getOrderList() {
        return ORDER;
    }

    public static List<CharacterTemporaryStat> getRemoteOrderList() {
        return REMOTE_ORDER;
    }

    public static List<CharacterTemporaryStat> getIndieOrderList() {
        return INDIE_ORDER;
    }

    public static List<CharacterTemporaryStat> getEncodeIntList() {
        return ENCODE_INT;
    }


    CharacterTemporaryStat(int val, int pos) {
        this.val = val;
        this.pos = pos;
    }

    CharacterTemporaryStat(int bitPos) {
        this.bitPos = bitPos;
        this.val = 1 << (31 - bitPos % 32);
        this.pos = bitPos / 32;
    }

    public static CharacterTemporaryStat getByBitPos(int parseInt) {
        return
                Arrays.asList(values()).stream()
                        .filter(characterTemporaryStat -> characterTemporaryStat.getBitPos() == parseInt)
                        .collect(Collectors.toList()).get(0);
    }

    public boolean isIndie() {
        return INDIE_ORDER.contains(this);
    }

    public boolean isEncodeInt() {
        return ENCODE_INT.contains(this);
    }

    public boolean isMovingEffectingStat() {
        return MOVING_AFFECTING_STAT.contains(this);
    }

    public int getVal() {
        return val;
    }

    public int getPos() {
        return pos;
    }

    public int getOrder() {
        return ORDER.indexOf(this);
    }

    public int getRemoteOrder() {
        return REMOTE_ORDER.indexOf(this);
    }

    public boolean isRemoteEncode4() {
        switch (this) {
            case NoBulletConsume:
            case RespectPImmune:
            case RespectMImmune:
            case DefenseAtt:
            case DefenseState:
            case MagicShield:
            case PyramidEffect:
            case BlessOfDarkness:
            case ImmuneBarrier:
            case Dance:
            case SpiritGuard:
            case KinesisPsychicEnergeShield:
            case AdrenalinBoost:
            case RWBarrier:
            case RWMagnumBlow:
            case HayatoStance:
            case Unk487:
            case Unk488:
            case Unk489:
                return true;
            default:
                return false;
        }
    }

    public boolean isRemoteEncode1() {
        switch (this) {
            case Speed:
            case ComboCounter:
            case Shock:
            case Team:
            case Cyclone:
            case OnCapsule:
            case KillingPoint:
            case PinkbeanRollingGrade:
            case ReturnTeleport:
            case FireBomb:
            case SurplusSupply:
            case AntiMagicShell:
                return true;
            default:
                return false;
        }
    }

    public boolean isNotEncodeReason() {
        switch (this) {
            case Speed:
            case ComboCounter:
            case ElementalCharge:
            case Shock:
            case Team:
            case Ghost:
            case NoBulletConsume:
            case RespectPImmune:
            case RespectMImmune:
            case DefenseAtt:
            case DefenseState:
            case MagicShield:
            case Cyclone:
            case OnCapsule:
            case PyramidEffect:
            case KillingPoint:
            case PinkbeanRollingGrade:
            case StackBuff:
            case BlessOfDarkness:
            case SurplusSupply:
            case ImmuneBarrier:
            case AdrenalinBoost:
            case RWBarrier:
            case RWMagnumBlow:
            case HayatoStance:
            case Unk488:
            case Unk489:
            case Unk460:
                return true;
            default:
                return false;
        }
    }

    public boolean isNotEncodeAnything() {
        switch (this) {
            case DarkSight:
            case SoulArrow:
            case DojangInvincible:
            case Flying:
            case Sneak:
            case BeastFormDamageUp:
            case BlessingArmor:
            case BlessingArmorIncPAD:
            case HolyMagicShell:
            case VengeanceOfAngel:
            case FullSoulMP:
                return true;
            default:
                return false;
        }
    }

    public boolean isRemoteSkip() {
        // for troublesome CTS that cause error38
        switch (this) {
            case EnergyCharged:
                return true;
            default:
                return false;
        }
    }

    public static void main(String[] args) {
        int a = Stigma.bitPos;
//        int val = 1 << (31 - (a & 0x1f));
//        int pos = a >> 5;
        int val = 0x1000;
        int pos = 0;
        log.debug(String.format("value 0x%04x, pos %d", val, pos));
        for(CharacterTemporaryStat cts : values()) {
            if(cts.getVal() == val && cts.getPos() == pos) {
                log.debug("Corresponds to " + cts);
            }
        }
//        for (CharacterTemporaryStat cts : values()) {
//            val = cts.getVal();
//            for (int i = 0; i < 32; i++) {
//                if (1 << i == val) {
//                    val = 31 - i;
//                }
//            }
//            if (val % 8 == 0) {
//                System.out.println();
//            }
//            System.out.println(String.format("%s(%d),", cts.toString(), (cts.getPos() * 32) + val));
//        }
    }

    @Override
    public int compare(CharacterTemporaryStat o1, CharacterTemporaryStat o2) {
        if (o1.getPos() < o2.getPos()) {
            return -1;
        } else if (o1.getPos() > o2.getPos()) {
            return 1;
        }
        // hacky way to circumvent java not having unsigned ints
        int o1Val = o1.getVal();
        if (o1Val == 0x8000_0000) {
            o1Val = Integer.MAX_VALUE;
        }
        int o2Val = o2.getVal();
        if (o2Val == 0x8000_0000) {
            o2Val = Integer.MAX_VALUE;
        }

        if (o1Val > o2Val) {
            // bigger value = earlier in the int => smaller
            return -1;
        } else if (o1Val < o2Val) {
            return 1;
        }
        return 0;
    }

    public int getBitPos() {
        return bitPos;
    }
}
