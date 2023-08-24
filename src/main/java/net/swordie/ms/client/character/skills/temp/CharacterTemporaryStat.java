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
    PyramidStunBuff(17), // Osiris' Eye: Stuns monsters for 1 second. Includes Pharaoh Yetis and Pharaoh Mummies at a 30% chance. Costs 500 points.
    PyramidFrozenBuff(18), // Horus' Eye: Slows down all monsters for 15 seconds. Includes Pharaoh Yetis and Pharaoh Mummies at a 30% chance. Costs 700 points.
    PyramidFireBuff(19), // Isis' Eye: Does Damage over time to all monsters for 15 seconds every second. Costs 500 points.
    PyramidBonusDamageBuff(20), // Anubis' Eye: Increases 40 attack for 15 seconds. Costs 500 points.
    IndieRelaxEXP(21),
    IndieSTR(22),
    IndieDEX(23),

    IndieINT(24),
    IndieLUK(25),
    IndieDamR(26),
    IndieScriptBuff(27),
    IndieMDF(28),
    IndieAsrR(29),
    IndieTerR(30),
    IndieCr(31),

    IndiePDDR(32),
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
    IndieForceJump(44), // Max Jump (?)
    IndieForceSpeed(45), // Max Speed (?)
    IndieQrPointTerm(46),

    IndieSummon(47), // Seems to be used by GMS for almost all their summons
    IndieCooltimeReduce(48),
    IndieNotDamaged(49), // Given by Soul Eclipse DW 3rd V & Omega Blaster (Attack mode)  |  Invincibility
    IndieKeyDownTime(50), // DemonicBlast | Twin Blades of Time
    IndieDamReduceR(51),
    IndieCrystalCharge(52),

    INDIE_STAT_COUNT(53),// alot of buffstats went -3
    PAD(54),
    PDD(55),
    MAD(56),
    MDD(57),
    ACC(58),
    EVA(59),
    Craft(60),
    Speed(61),
    Jump(62),
    MagicGuard(63),
    DarkSight(64),
    Booster(65),
    PowerGuard(66),
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
    HolySymbol(77),
    MesoUp(78),
    ShadowPartner(79),
    PickPocket(80),
    MesoGuard(81),
    Thaw(82),
    Weakness(83),
    Curse(84),
    Slow(85),
    Morph(86),
    Regen(87),
    BasicStatUp(88),
    Stance(89),
    SharpEyes(90),
    ManaReflection(91),
    Attract(92),
    NoBulletConsume(93),
    Infinity(94),
    AdvancedBless(95),
    IllusionStep(96),
    Blind(97),
    Concentration(98),
    BanMap(99),
    MaxLevelBuff(100),
    MesoUpByItem(101),
    Ghost(102),
    Barrier(103),
    ReverseInput(104),
    ItemUpByItem(105),
    RespectPImmune(106),
    RespectMImmune(107),
    DefenseAtt(108),
    DefenseState(109),
    DojangBerserk(110),
    DojangInvincible(111),
    DojangShield(112),
    SoulMasterFinal(113),
    WindBreakerFinal(114),
    BladeClone(114),
    ElementalReset(115),
    HideAttack(116),
    EventRate(117),
    ComboAbilityBuff(118),
    ComboDrain(119),
    ComboBarrier(120),
    BodyPressure(121),
    RepeatEffect(122),
    ExpBuffRate(123),
    StopPortion(124),
    StopMotion(125),
    Fear(126),
    HiddenPieceOn(127),
    MagicShield(128),
    MagicResistance(129),
    SoulStone(130),
    Flying(131),
    Frozen(132),
    AssistCharge(133),
    Enrage(134),
    DrawBack(135),
    NotDamaged(136),
    FinalCut(137),
    HowlingAttackDamage(138),
    BeastFormDamageUp(139),
    Dance(140),
    EMHP(141),
    EMMP(142),
    EPAD(143),
    EMAD(144),
    EPDD(145),
    // EMDD(146),
    Guard(146),
    Cyclone(147),
    HowlingCritical(148),
    HowlingMaxMP(149),
    HowlingDefence(150),
    HowlingEvasion(151),
    Conversion(152),
    Revive(153),
    PinkbeanMinibeenMove(154),
    Sneak(155),
    Mechanic(156),
    BeastFormMaxHP(157),
    Dice(158),
    BlessingArmor(159),
    DamR(160),
    TeleportMasteryOn(161),
    CombatOrders(162),
    Beholder(163),
    DispelItemOption(164),
    Inflation(165),
    OnixDivineProtection(166),
    Web(167),
    Bless(168),
    TimeBomb(169),
    Disorder(170),
    Thread(171),
    Team(172),
    Explosion(173),
    BuffLimit(174),
    STR(175),
    INT(176),
    DEX(177),
    LUK(178),
    DispelItemOptionByField(179),
    DarkTornado(180),
    PVPDamage(181),
    PvPScoreBonus(182),
    PvPInvincible(183),
    PvPRaceEffect(184),
    WeaknessMdamage(185),
    Frozen2(186),
    PVPDamageSkill(187),
    AmplifyDamage(188),
    IceKnight(189),
    Shock(190),
    InfinityForce(191),
    IncMaxHP(192),
    IncMaxMP(193),
    HolyMagicShell(194),
    KeyDownTimeIgnore(195),
    ArcaneAim(196),
    MasterMagicOn(197),
    AsrR(198),
    TerR(199),
    DamAbsorbShield(200),
    DevilishPower(201),
    Roulette(202),
    SpiritLink(203),
    AsrRByItem(204),
    Event(209), // not updated
    CriticalBuff(206),
    DropRate(211),
    PlusExpRate(212),
    ItemInvincible(213),
    Awake(214),
    ItemCritical(215),

    ItemEvade(216),
    Event2(217),
    VampiricTouch(218),
    DDR(219),
    IncCriticalDamMin(220),
    IncCriticalDamMax(221),
    IncTerR(222),
    IncAsrR(223),

    DeathMark(224),
    UsefulAdvancedBless(225),
    Lapidification(226),
    VenomSnake(227),
    CarnivalAttack(228),
    CarnivalDefence(229),
    CarnivalExp(230),
    SlowAttack(231),

    PyramidEffect(232),
    KillingPoint(233),
    HollowPointBullet(234),
    KeyDownMoving(235),
    IgnoreTargetDEF(236),
    ReviveOnce(237),
    Invisible(238),
    EnrageCr(239),

    EnrageCrDamMin(240),
    Judgement(241),
    DojangLuckyBonus(242),
    PainMark(243),
    Magnet(244),
    MagnetArea(245),
    VampDeath(246),
    BlessingArmorIncPAD(247),

    KeyDownAreaMoving(248),
    Larkness(249),
    StackBuff(250),
    BlessOfDarkness(251),
    AntiMagicShell(252),
    AntiMagicShellBool(252),
    LifeTidal(253),
    HitCriDamR(254),
    SmashStack(255),

    PartyBarrier(256),
    ReshuffleSwitch(257),
    SpecialAction(258),
    VampDeathSummon(259),
    StopForceAtomInfo(260),
    SoulGazeCriDamR(261),
    SoulRageCount(262),
    PowerTransferGauge(263),

    AffinitySlug(264),
    Trinity(265),
    IncMaxDamage(266),
    BossShield(267),
    MobZoneState(268),
    GiveMeHeal(269),
    TouchMe(270),
    Contagion(271),

    ComboUnlimited(272),
    SoulExalt(273),
    IgnorePCounter(274),
    IgnoreAllCounter(275),
    IgnorePImmune(276),
    IgnoreAllImmune(277),
    FinalJudgement(278),
    IceAura(279),

    FireAura(280),
    VengeanceOfAngel(281),
    HeavensDoor(282),
    Preparation(283),
    BullsEye(284),
    IncEffectHPPotion(285),
    IncEffectMPPotion(286),
    BleedingToxin(287),

    IgnoreMobDamR(288), // reserve for RuneStone LIBERATE_THE_RECOVERY_RUNE
    Asura(289),
    FlipTheCoin(290),
    UnityOfPower(291),
    Stimulate(292),
    ReturnTeleport(293),
    DropRIncrease(294),
    IgnoreMobpdpR(295),

    BdR(296),
    CapDebuff(297),
    Exceed(298),
    DiabolikRecovery(299),
    FinalAttackProp(300),
    ExceedOverload(301),
    OverloadCount(302),
    BuckShot(303),

    FireBomb(304),
    HalfstatByDebuff(305),
    SurplusSupply(306),
    SetBaseDamage(307),
    EVAR(308),
    NewFlying(309),
    AmaranthGenerator(310),
    OnCapsule(311),

    CygnusElementSkill(312),
    StrikerHyperElectric(313),
    EventPointAbsorb(314),
    EventAssemble(315),
    StormBringer(316),
    ACCR(317),
    DEXR(318),
    Albatross(319),

    Translucence(320),
    PoseType(321),
    LightOfSpirit(322),
    ElementSoul(323),
    GlimmeringTime(324),
    TrueSight(325),
    SoulExplosion(326),
    SoulMP(327),

    FullSoulMP(328),
    SoulSkillDamageUp(329),
    ElementalCharge(330),
    Restoration(331),
    CrossOverChain(332),
    ChargeBuff(333),
    Reincarnation(334),
    KnightsAura(335),

    ChillingStep(336),
    DotBasedBuff(337),
    BlessEnsenble(338),
    ComboCostInc(339),
    ExtremeArchery(340),
    NaviFlying(341),
    QuiverCatridge(342),
    AdvancedQuiver(343),

    UserControlMob(344),
    ImmuneBarrier(345),
    ArmorPiercing(346),
    ZeroAuraStr(347),
    ZeroAuraSpd(348),
    CriticalGrowing(349),
    QuickDraw(350),
    BowMasterConcentration(351),

    TimeFastABuff(352),
    TimeFastBBuff(353),
    GatherDropR(354),
    AimBox2D(355),
    IncMonsterBattleCaptureRate(356),
    CursorSniping(357),
    DebuffTolerance(358),
    DotHealHPPerSecond(359),

    SpiritGuard(360),
    PreReviveOnce(361),
    SetBaseDamageByBuff(362),
    LimitMP(363),
    ReflectDamR(364),
    ComboTempest(365),
    MHPCutR(366),
    MMPCutR(367),

    SelfWeakness(368),
    ElementDarkness(369),
    FlareTrick(370),
    Ember(371),
    Dominion(372),
    SiphonVitality(373),
    DarknessAscension(374),
    BossWaitingLinesBuff(375),

    DamageReduce(376),
    ShadowServant(377),
    ShadowIllusion(378),
    KnockBack(379),
    AddAttackCount(380),
    ComplusionSlant(381),
    JaguarSummoned(382),
    JaguarCount(383),

    SSFShootingAttack(384),
    DevilCry(385),
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

    public static final int length = 17;
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
    private static final Logger log = LogManager.getRootLogger();
    private static final List<CharacterTemporaryStat> ORDER = Arrays.asList(
            STR, INT, DEX, LUK, PAD, PDD, MAD, MDD, ACC, EVA, EVAR, Craft, Speed, Jump, EMHP, EMMP, EPAD, EMAD, EPDD,
            MagicGuard, DarkSight, Booster, PowerGuard, Guard, MaxHP, MaxMP, Invincible, SoulArrow, Stun, Shock,
            Poison, Seal, Darkness, ComboCounter, WeaponCharge, ElementalCharge, HolySymbol, MesoUp, ShadowPartner,
            PickPocket, MesoGuard, Thaw, Weakness, WeaknessMdamage, Curse, Slow, TimeBomb, BuffLimit, Team, Disorder,
            Thread, Morph, Ghost, Regen, BasicStatUp, Stance, SharpEyes, ManaReflection, Attract, Magnet, MagnetArea,
            NoBulletConsume, StackBuff, Trinity, Infinity, AdvancedBless, IllusionStep, Blind, Concentration, BanMap,
            MaxLevelBuff, Barrier, DojangShield, ReverseInput, MesoUpByItem, ItemUpByItem, RespectPImmune,
            RespectMImmune, DefenseAtt, DefenseState, DojangBerserk, DojangInvincible, SoulMasterFinal,
            WindBreakerFinal, ElementalReset, HideAttack, EventRate, ComboAbilityBuff, ComboDrain, ComboBarrier,
            PartyBarrier, BodyPressure, RepeatEffect, ExpBuffRate, StopPortion, StopMotion, Fear, MagicShield,
            MagicResistance, SoulStone, Flying, NewFlying, NaviFlying, Frozen, Frozen2, Web, Enrage, NotDamaged,
            FinalCut, HowlingAttackDamage, BeastFormDamageUp, Dance, Cyclone, OnCapsule, HowlingCritical,
            HowlingMaxMP, HowlingDefence, HowlingEvasion, Conversion, Revive, PinkbeanMinibeenMove, Sneak, Mechanic,
            DrawBack, BeastFormMaxHP, Dice, BlessingArmor, BlessingArmorIncPAD, DamR, TeleportMasteryOn, CombatOrders,
            Beholder, DispelItemOption, DispelItemOptionByField, Inflation, OnixDivineProtection, Bless, Explosion,
            DarkTornado, IncMaxHP, IncMaxMP, PVPDamage, PVPDamageSkill, PvPScoreBonus, PvPInvincible, PvPRaceEffect,
            IceKnight, HolyMagicShell, InfinityForce, AmplifyDamage, KeyDownTimeIgnore, MasterMagicOn, AsrR,
            AsrRByItem, TerR, DamAbsorbShield, Roulette, Event, SpiritLink, CriticalBuff, DropRate, PlusExpRate,
            ItemInvincible, ItemCritical, ItemEvade, Event2, VampiricTouch, DDR, IncCriticalDamMin, IncCriticalDamMax,
            IncTerR, IncAsrR, DeathMark, PainMark, UsefulAdvancedBless, Lapidification, VampDeath, VampDeathSummon,
            VenomSnake, CarnivalAttack, CarnivalDefence, CarnivalExp, SlowAttack, PyramidEffect, HollowPointBullet,
            KeyDownMoving, KeyDownAreaMoving, CygnusElementSkill, IgnoreTargetDEF, Invisible, ReviveOnce,
            AntiMagicShell, EnrageCr, EnrageCrDamMin, BlessOfDarkness, LifeTidal, Judgement, DojangLuckyBonus,
            HitCriDamR, Larkness, SmashStack, ReshuffleSwitch, SpecialAction, ArcaneAim, StopForceAtomInfo,
            SoulGazeCriDamR, SoulRageCount, PowerTransferGauge, AffinitySlug, SoulExalt, HiddenPieceOn,
            BossShield, MobZoneState, GiveMeHeal, TouchMe, Contagion, ComboUnlimited, IgnorePCounter,
            IgnoreAllCounter, IgnorePImmune, IgnoreAllImmune, FinalJudgement, KnightsAura, IceAura, FireAura,
            VengeanceOfAngel, HeavensDoor, Preparation, BullsEye, IncEffectHPPotion, IncEffectMPPotion, SoulMP,
            FullSoulMP, SoulSkillDamageUp, BleedingToxin, IgnoreMobDamR, Asura, FlipTheCoin, UnityOfPower,
            Stimulate, ReturnTeleport, CapDebuff, DropRIncrease, IgnoreMobpdpR, BdR, Exceed, DiabolikRecovery,
            FinalAttackProp, ExceedOverload, DevilishPower, OverloadCount, BuckShot, FireBomb, HalfstatByDebuff,
            SurplusSupply, SetBaseDamage, AmaranthGenerator, StrikerHyperElectric, EventPointAbsorb, EventAssemble,
            StormBringer, ACCR, DEXR, Albatross, Translucence, PoseType, LightOfSpirit, ElementSoul, GlimmeringTime,
            Restoration, ComboCostInc, ChargeBuff, TrueSight, CrossOverChain, ChillingStep, Reincarnation, DotBasedBuff,
            BlessEnsenble, ExtremeArchery, QuiverCatridge, AdvancedQuiver, UserControlMob, ImmuneBarrier, ArmorPiercing,
            ZeroAuraStr, ZeroAuraSpd, CriticalGrowing, QuickDraw, BowMasterConcentration, TimeFastABuff, TimeFastBBuff,
            GatherDropR, AimBox2D, CursorSniping, IncMonsterBattleCaptureRate, DebuffTolerance, DotHealHPPerSecond,
            SpiritGuard, PreReviveOnce, SetBaseDamageByBuff, LimitMP, ReflectDamR, ComboTempest, MHPCutR, MMPCutR,
            SelfWeakness, ElementDarkness, FlareTrick, Ember, Dominion, SiphonVitality, DarknessAscension,
            BossWaitingLinesBuff, DamageReduce, ShadowServant, ShadowIllusion, AddAttackCount, ComplusionSlant,
            JaguarSummoned, JaguarCount, SSFShootingAttack, DevilCry, ShieldAttack, BMageAura, DarkLighting,
            AttackCountX, BMageDeath, BombTime, NoDebuff, XenonAegisSystem, AngelicBursterSoulSeeker, HiddenPossession,
            NightWalkerBat, NightLordMark, WizardIgnite, BattlePvPHelenaMark, BattlePvPHelenaWindSpirit,
            BattlePvPLangEProtection, BattlePvPLeeMalNyunScaleUp, BattlePvPRevive, PinkbeanAttackBuff, RandAreaAttack,
            BattlePvPMikeShield, BattlePvPMikeBugle, PinkbeanRelax, PinkbeanYoYoStack, NextAttackEnhance,
            AranBeyonderDamAbsorb, AranCombotempastOption, NautilusFinalAttack, ViperTimeLeap, RoyalGuardState,
            RoyalGuardPrepare, MichaelSoulLink, MichaelStanceLink, TriflingWhimOnOff, AddRangeOnOff,
            KinesisPsychicPoint, KinesisPsychicOver, KinesisPsychicShield, KinesisIncMastery,
            KinesisPsychicEnergeShield, BladeStance, DebuffActiveSkillHPCon, DebuffIncHP, BowMasterMortalBlow,
            AngelicBursterSoulResonance, Fever, IgnisRore, RpSiksin, TeleportMasteryRange, FireBarrier, ChangeFoxMan,
            FixCoolTime, IncMobRateDummy, AdrenalinBoost, AranSmashSwing, AranDrain, AranBoostEndHunt,
            HiddenHyperLinkMaximization, RWCylinder, RWCombination, RWMagnumBlow, RWBarrier, RWBarrierHeal,
            RWMaximizeCannon, RWOverHeat, RWMovingEvar, Stigma, Unk455, IncMaxDamage, Unk456, Unk457, Unk458, Unk459,
            Unk460, PyramidFireBuff /*not sure*/, HayatoStance, HayatoBooster, HayatoStanceBonus, WillowDodge, Unk465,
            HayatoPAD, HayatoHPR, HayatoMPR, Jinsoku, HayatoCr, HakuBlessing, HayatoBoss, BattoujutsuAdvance, Unk477,
            Unk478, BlackHeartedCurse, EyeForEye, BeastMode, TeamRoar, Unk482, Unk483, Unk487, Unk488, Unk489, Unk490,
            Unk491
    );
    private static final List<CharacterTemporaryStat> REMOTE_ORDER = Arrays.asList(
            Speed, ComboCounter, WeaponCharge, ElementalCharge, Stun, Shock, Darkness, Seal, Weakness, WeaknessMdamage,
            Curse, Slow, PvPRaceEffect, IceKnight, TimeBomb, Team, Disorder, Thread, Poison, ShadowPartner, Morph,
            Ghost, Attract, Magnet, MagnetArea, NoBulletConsume, BanMap, Barrier, DojangShield, ReverseInput,
            RespectPImmune, RespectMImmune, DefenseAtt, DefenseState, DojangBerserk, RepeatEffect, StopPortion,
            StopMotion, Fear, MagicShield, Frozen, Frozen2, Web, DrawBack, FinalCut, Cyclone, OnCapsule, Mechanic,
            Inflation, Explosion, DarkTornado, AmplifyDamage, HideAttack, DevilishPower, SpiritGuard, Event, Event2,
            DeathMark, PainMark, Lapidification, VampDeath, VampDeathSummon, VenomSnake, PyramidEffect, KillingPoint,
            PinkbeanRollingGrade, IgnoreTargetDEF, Invisible, Judgement, KeyDownAreaMoving, StackBuff, BlessOfDarkness,
            Larkness, ReshuffleSwitch, SpecialAction, StopForceAtomInfo, SoulGazeCriDamR, PowerTransferGauge,
            AffinitySlug, SoulExalt, HiddenPieceOn, SmashStack, MobZoneState, GiveMeHeal, TouchMe, Contagion,
            ComboUnlimited, IgnorePCounter, IgnoreAllCounter, IgnorePImmune, IgnoreAllImmune, FinalJudgement,
            KnightsAura, IceAura, FireAura, HeavensDoor, DamAbsorbShield, AntiMagicShell, NotDamaged, BleedingToxin,
            WindBreakerFinal, IgnoreMobDamR, Asura, UnityOfPower, Stimulate, ReturnTeleport, CapDebuff, OverloadCount,
            FireBomb, SurplusSupply, NewFlying, NaviFlying, AmaranthGenerator, CygnusElementSkill, StrikerHyperElectric,
            EventPointAbsorb, EventAssemble, Albatross, Translucence, PoseType, LightOfSpirit, ElementSoul,
            GlimmeringTime, Reincarnation, Beholder, QuiverCatridge, ArmorPiercing, ZeroAuraStr, ZeroAuraSpd,
            ImmuneBarrier, FullSoulMP, AntiMagicShellBool, Dance, SpiritGuard, ComboTempest, HalfstatByDebuff,
            ComplusionSlant, JaguarSummoned, BMageAura, DarkLighting, AttackCountX, FireBarrier, KeyDownMoving,
            MichaelSoulLink, KinesisPsychicEnergeShield, BladeStance, Fever, AdrenalinBoost, RWBarrier, RWMagnumBlow,
            Stigma, Unk456, BeastMode, TeamRoar, HayatoStance, HayatoBooster, HayatoStanceBonus, HayatoPAD, HayatoHPR,
            HayatoMPR, HayatoCr, HayatoBoss, Stance, BattoujutsuAdvance, Unk478, BlackHeartedCurse, EyeForEye, Unk458,
            Unk483, Unk487, Unk488, Unk489, Unk491, Unk460
    );
    private static final List<CharacterTemporaryStat> INDIE_ORDER = Arrays.asList(
            IndiePAD, IndieMAD, IndieDEF, IndieMHP, IndieMHPR, IndieMMP, IndieMMPR, IndieACC, IndieEVA, IndieJump,
            IndieSpeed, IndieAllStat, IndieAllStatR, IndieDodgeCriticalTime, IndieEXP, IndieBooster, IndieFixedDamageR,
            PyramidStunBuff, PyramidFrozenBuff, PyramidFireBuff, PyramidBonusDamageBuff, IndieRelaxEXP, IndieSTR,
            IndieDEX, IndieINT, IndieLUK, IndieDamR, IndieScriptBuff, IndieMDF, IndieAsrR, IndieTerR, IndieCr,
            IndiePDDR, IndieCrDmg, IndieBDR, IndieStatR, IndieStance, IndieIgnoreMobpdpR, IndieEmpty, IndiePADR,
            IndieMADR, IndieEVAR, IndieDrainHP, IndiePMdR, IndieForceJump, IndieForceSpeed, IndieQrPointTerm,
            IndieSummon, IndieCooltimeReduce, IndieNotDamaged, IndieKeyDownTime, IndieDamReduceR, IndieCrystalCharge
    );

    private static final List<CharacterTemporaryStat> ENCODE_INT = Arrays.asList(
            RideVehicle, RideVehicleExpire, CarnivalDefence, SpiritLink, DojangLuckyBonus, SoulGazeCriDamR,
            PowerTransferGauge, ReturnTeleport, ShadowPartner, AranSmashSwing, IncMaxDamage, Unk487, SetBaseDamage,
            QuiverCatridge, ImmuneBarrier, NaviFlying, Dance, SetBaseDamageByBuff, DotHealHPPerSecond, Magnet,
            MagnetArea, VampDeath, VampDeathSummon, Cyclone, RWBarrier
    );
    private int bitPos;
    private int val;
    private int pos;

    CharacterTemporaryStat(int val, int pos) {
        this.val = val;
        this.pos = pos;
    }

    CharacterTemporaryStat(int bitPos) {
        this.bitPos = bitPos;
        this.val = 1 << (31 - bitPos % 32);
        this.pos = bitPos / 32;
    }

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

    public static CharacterTemporaryStat getByBitPos(int parseInt) {
        return
                Arrays.asList(values()).stream()
                        .filter(characterTemporaryStat -> characterTemporaryStat.getBitPos() == parseInt)
                        .collect(Collectors.toList()).get(0);
    }

    public static void main(String[] args) {
        int a = Stigma.bitPos;
//        int val = 1 << (31 - (a & 0x1f));
//        int pos = a >> 5;
        int val = 0x1000;
        int pos = 0;
        log.debug(String.format("value 0x%04x, pos %d", val, pos));
        for (CharacterTemporaryStat cts : values()) {
            if (cts.getVal() == val && cts.getPos() == pos) {
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
            case AntiMagicShellBool:
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
