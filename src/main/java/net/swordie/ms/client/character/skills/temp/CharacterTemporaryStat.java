package net.swordie.ms.client.character.skills.temp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

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
    IndieDodgeCriticalTime(12),
    IndieEXP(13),
    IndieBooster(14),
    IndieFixedDamageR(15),
    PyramidStunBuff(16),
    PyramidFrozenBuff(17),
    PyramidFireBuff(18),
    PyramidBonusDamageBuff(19),
    IndieRelaxEXP(20),
    IndieSTR(21),
    IndieDEX(22),
    IndieINT(23),
    IndieLUK(24),
    IndieDamR(25),
    IndieMDF(26),
    IndieMaxDamageOver(27),
    IndieAsrR(28),
    IndieTerR(29),
    IndieCr(30),
    IndieDEFR(31),
    IndieCrDmg(32),
    IndieBDR(33),
    IndieStatR(34),
    IndieStance(35),
    IndieIgnoreMobpdpR(36),
    IndieEmpty(37),
    IndiePADR(38),
    IndieMADR(39),
    IndieCrMaxR(40),
    IndieEVAR(41),
    IndieMDDR(42),
    IndieDrainHP(43),
    IndiePMdR(44),
    IndieMaxDamageOverR(45),
    IndieForceJump(43), // v178
    IndieForceSpeed(44), // v178
    IndieQrPointTerm(45), // TODO: check unk indies
    IndieUnk46(46),
    IndieUnk47(47),
    IndieUnk48(48),
    IndieHyperStat(49), // new v178
    IndieStatCount(54),


    PAD(55),
    DEF(56),
    MAD(57),
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
    EDEF(145),
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

    DarkTornado(180), // Cygnus Attack
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
    Event(205),
    CriticalBuff(206),
    DropRate(207),
    PlusExpRate(208),
    ItemInvincible(209),
    Awake(210),
    ItemCritical(211),
    // ItemEvade Removed
    Event2(212),
    VampiricTouch(213),
    DDR(214),
    IncCriticalDam(215), // TODO check
    IncTerR(216),
    IncAsrR(217),
    DeathMark(218),
    UsefulAdvancedBless(219),
    Lapidification(220),
    VenomSnake(221),
    CarnivalAttack(222),
    CarnivalDefence(223),
    CarnivalExp(224),
    SlowAttack(225),
    PyramidEffect(226),
    KillingPoint(227),
    HollowPointBullet(228),
    KeyDownMoving(229),
    IgnoreTargetDEF(230),
    ReviveOnce(231),
    Invisible(232),
    EnrageCr(233),
    EnrageCrDam(234),
    Judgement(235),
    DojangLuckyBonus(236),
    PainMark(237),
    Magnet(238),
    MagnetArea(239),
    GuidedArrow(240), // new v178
    ExtraSkillCTS(241), // new v178
    VampDeath(242),
    BlessingArmorIncPAD(243),
    KeyDownAreaMoving(244),
    Larkness(245),
    StackBuff(246),
    BlessOfDarkness(247),
    AntiMagicShell(248),
    LifeTidal(249),
    HitCriDamR(250),
    SmashStack(251),
    PartyBarrier(252),
    ReshuffleSwitch(253),
    SpecialAction(254),
    VampDeathSummon(255),
    StopForceAtomInfo(256),
    SoulGazeCriDamR(257),
    SoulRageCount(258),
    PowerTransferGauge(259),
    AffinitySlug(260),
    Trinity(261),
    IncMaxDamage(262),
    BossShield(263),
    MobZoneState(264),
    GiveMeHeal(265),
    TouchMe(266),
    Contagion(267),
    ComboUnlimited(268),
    SoulExalt(269),
    IgnorePCounter(270),
    IgnoreAllCounter(271),
    IgnorePImmune(272),
    IgnoreAllImmune(273),
    FinalJudgement(274),
    Unk275(275), // TODO check from BossShield ~ Unk275
    IceAura(276),
    FireAura(277),
    VengeanceOfAngel(278),
    HeavensDoor(279),
    Preparation(280),
    BullsEye(281),
    IncEffectHPPotion(282),
    IncEffectMPPotion(283),
    BleedingToxin(284),

    IgnoreMobDamR(285), // reserve for RuneStone LIBERATE_THE_RECOVERY_RUNE
    Asura(286),
    OmegaBlaster(287), // new v178?
    FlipTheCoin(288),
    UnityOfPower(289),
    Stimulate(290),
    ReturnTeleport(291),
    DropRIncrease(292),
    IgnoreMobpdpR(293),
    BdR(294),
    CapDebuff(295),
    Exceed(296),
    DiabolikRecovery(297),
    FinalAttackProp(298),
    ExceedOverload(299),
    OverloadCount(300),
    BuckShot(301),
    FireBomb(302),
    HalfstatByDebuff(303),
    SurplusSupply(304),
    SetBaseDamage(305),
    EVAR(306),
    NewFlying(307),
    AmaranthGenerator(308),
    OnCapsule(309),
    CygnusElementSkill(310),
    StrikerHyperElectric(311),
    EventPointAbsorb(312),
    EventAssemble(313),
    StormBringer(314),
    ACCR(315),
    DEXR(316),
    Albatross(317),
    Translucence(318),
    PoseType(319), // v178
    LightOfSpirit(320),
    ElementSoul(321),
    GlimmeringTime(322),
    TrueSight(323),
    SoulExplosion(324),
    SoulMP(325),
    FullSoulMP(326),
    SoulSkillDamageUp(327),
    ElementalCharge(328),
    Restoration(329),
    CrossOverChain(330),
    ChargeBuff(331),
    Reincarnation(332),
    KnightsAura(333),
    ChillingStep(334),
    DotBasedBuff(335),
    BlessEnsenble(336),
    ComboCostInc(337),
    ExtremeArchery(338),
    NaviFlying(339),
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
    AimBox2D(353),
    IncMonsterBattleCaptureRate(354),
    CursorSniping(355),
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
    Dominion(371), // v178
    SiphonVitality(372),
    DarknessAscension(373),
    BossWaitingLinesBuff(374),
    DamageReduce(375),
    ShadowServant(376),
    ShadowIllusion(377),
    KnockBack(378),
    AddAttackCount(379),
    ComplusionSlant(380),
    JaguarSummoned(381),
    JaguarCount(382),
    SSFShootingAttack(383),
    DevilCry(384),
    ShieldAttack(385),
    BMageAura(386),
    DarkLighting(387),
    AttackCountX(388),
    BMageDeath(389),
    BombTime(390),
    NoDebuff(391),
    BattlePvPMikeShield(392),
    BattlePvPMikeBugle(393),
    XenonAegisSystem(394),
    AngelicBursterSoulSeeker(395),
    HiddenPossession(396),
    NightWalkerBat(397),
    NightLordMark(398),
    WizardIgnite(399),
    FireBarrier(400), // v178
    ChangeFoxMan(401),
    DivineEcho(402), // new v178
    DemonicFrenzy(403), // new v178
    Unk404(404), // new v178
    BattlePvPHelenaMark(405), // v178
    BattlePvPHelenaWindSpirit(406),
    BattlePvPLangEProtection(407),
    BattlePvPLeeMalNyunScaleUp(408),
    BattlePvPRevive(409),
    PinkbeanAttackBuff(410),
    PinkbeanRelax(411),
    PinkbeanYoYoStack(412),
    PinkbeanRollingGrade(413),
    RandAreaAttack(414),
    NextAttackEnhance(415), // v178
    AranBeyonderDamAbsorb(416),
    AranCombotempastOption(417),
    NautilusFinalAttack(418),
    ViperTimeLeap(419),
    RoyalGuardState(420),
    RoyalGuardPrepare(421),
    MichaelSoulLink(422), // v178
    MichaelStanceLink(423),
    TriflingWhimOnOff(424),
    AddRangeOnOff(425),
    KinesisPsychicPoint(426),
    KinesisPsychicOver(427),
    KinesisPsychicShield(428),
    KinesisIncMastery(429),
    KinesisPsychicEnergeShield(430), // v178
    BladeStance(431), // v178
    DebuffActiveSkillHPCon(432),
    DebuffIncHP(433),
    BowMasterMortalBlow(434),
    AngelicBursterSoulResonance(435),
    Fever(436), // v178
    IgnisRore(437),
    RpSiksin(438),
    TeleportMasteryRange(439),
    FixCoolTime(440),
    IncMobRateDummy(441),
    AdrenalinBoost(442), // v178
    AranSmashSwing(443),
    AranDrain(444),
    AranBoostEndHunt(445),
    HiddenHyperLinkMaximization(446),
    RWCylinder(447),
    RWCombination(448),
    RWMagnumBlow(449), // v178
    RWBarrier(450), // v178
    RWBarrierHeal(451),
    RWMaximizeCannon(452),
    RWOverHeat(453),
    UsingScouter(454),
    RWMovingEvar(455), // v178
    Stigma(456),
    MahasFury(457),
    RuneCooltime(458),
    Unk459(459),
    Unk460(460),
    Unk461(461),
    MeltDown(462),
    SparkleBurstStage(463),
    LightningCascade(464),
    BulletParty(465),
    Unk466(466),
    AuraScythe(467),
    Benediction(468),
    VoidStrike(469),
    ReduceHitDmgOnlyHPR(470),
    WeaponAura(471),
    ManaOverload(472),
    RhoAias(473),
    PsychicTornado(474),
    SpreadThrow(475),
    WindEnergy(476),
    MassDestructionRockets(477),
    ShadowAssault(478),
    BlitzShield(479),
    SplitShot(480),
    FreudBlessing(481),
    OverloadMode(482),
    Unk483(483),
    Unk484(484),
    Unk485(485),
    Unk486(486),
    Unk487(487),
    HayatoStance(488), // v178
    HayatoStanceBonus(489),
    EyeForEye(490),
    WillowDodge(491),
    Unk492(492),
    HayatoPAD(493),
    HayatoHPR(494),
    HayatoMPR(495),
    HayatoBooster(496), // v178
    Unk477(477),
    Unk478(478),

    Jinsoku(499), // v178
    HayatoCr(500),
    HakuBlessing(501),
    HayatoBoss(502),
    BattoujutsuAdvance(503),
    Unk504(504),
    Unk505(505),
    BlackHeartedCurse(506),
    BeastMode(507), // v178
    TeamRoar(508), // v178
    Unk509(509),
    Unk510(510),
    Unk511(511),
    Unk512(512),
    Unk513(513),
    Unk514(514),
    Unk515(515),
    Unk516(516),
    Unk517(517),
    Unk518(518),
    Unk519(519),
    Unk520(520),

    // TSB
    EnergyCharged(521),
    DashSpeed(522),
    DashJump(523),
    RideVehicle(524),
    PartyBooster(525),
    GuidedBullet(526),
    Undead(527),
    RideVehicleExpire(528),
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
            Thread, Morph, Ghost, Regen, BasicStatUp, Stance, SharpEyes, ManaReflection, Attract, Magnet, MagnetArea,
            GuidedArrow, ExtraSkillCTS, NoBulletConsume, StackBuff, Trinity, Infinity, AdvancedBless, IllusionStep, Blind,
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
            IncEffectHPPotion, IncEffectMPPotion, SoulMP, FullSoulMP, SoulSkillDamageUp, BleedingToxin, IgnoreMobDamR, Asura,
            OmegaBlaster, FlipTheCoin, UnityOfPower, Stimulate, ReturnTeleport, CapDebuff, DropRIncrease, IgnoreMobpdpR,
            BdR, Exceed, DiabolikRecovery, FinalAttackProp, ExceedOverload, DevilishPower, OverloadCount, BuckShot,
            FireBomb, HalfstatByDebuff, SurplusSupply, SetBaseDamage, AmaranthGenerator, StrikerHyperElectric,
            EventPointAbsorb, EventAssemble, StormBringer, ACCR, DEXR, Albatross, Translucence, PoseType, LightOfSpirit,
            ElementSoul, GlimmeringTime, Restoration, ComboCostInc, ChargeBuff, TrueSight, CrossOverChain, ChillingStep,
            Reincarnation, DotBasedBuff, BlessEnsenble, ExtremeArchery, QuiverCatridge, AdvancedQuiver, UserControlMob,
            ImmuneBarrier, ArmorPiercing, ZeroAuraStr, ZeroAuraSpd, CriticalGrowing, QuickDraw, BowMasterConcentration,
            TimeFastABuff, TimeFastBBuff, GatherDropR, AimBox2D, CursorSniping,  IncMonsterBattleCaptureRate,
            DebuffTolerance, Unk357, DotHealHPPerSecond, SpiritGuard, PreReviveOnce, SetBaseDamageByBuff, LimitMP,
            ReflectDamR, ComboTempest, MHPCutR, MMPCutR, SelfWeakness, ElementDarkness, FlareTrick, Ember, Dominion,
            SiphonVitality, DarknessAscension, BossWaitingLinesBuff, DamageReduce, ShadowServant, ShadowIllusion,
            AddAttackCount, ComplusionSlant, JaguarSummoned, JaguarCount, SSFShootingAttack, DevilCry, ShieldAttack,
            BMageAura, DarkLighting, AttackCountX, BMageDeath, BombTime, NoDebuff, XenonAegisSystem,
            AngelicBursterSoulSeeker, HiddenPossession, NightWalkerBat, NightLordMark, WizardIgnite,
            DemonicFrenzy, Unk404, BattlePvPHelenaMark, BattlePvPHelenaWindSpirit, BattlePvPLangEProtection,
            BattlePvPLeeMalNyunScaleUp, BattlePvPRevive, PinkbeanAttackBuff, RandAreaAttack, BattlePvPMikeShield,
            BattlePvPMikeBugle, PinkbeanRelax, PinkbeanRollingGrade, WindEnergy, NextAttackEnhance, AranBeyonderDamAbsorb,
            AranCombotempastOption, NautilusFinalAttack, ViperTimeLeap, RoyalGuardState, RoyalGuardPrepare,
            MichaelSoulLink, MichaelStanceLink, TriflingWhimOnOff, AddRangeOnOff, KinesisPsychicPoint,
            KinesisPsychicOver, KinesisPsychicShield, KinesisIncMastery, KinesisPsychicEnergeShield, BladeStance,
            DebuffActiveSkillHPCon, DebuffIncHP, BowMasterMortalBlow, AngelicBursterSoulResonance, Fever, IgnisRore,
            RpSiksin, TeleportMasteryRange, FireBarrier, ChangeFoxMan, FixCoolTime, IncMobRateDummy, AdrenalinBoost,
            AranSmashSwing, AranDrain, AranBoostEndHunt, HiddenHyperLinkMaximization, RWCylinder, RWCombination,
            RWMagnumBlow, RWBarrier, RWBarrierHeal, RWMaximizeCannon, RWOverHeat, RWMovingEvar, Stigma, MahasFury,
            RuneCooltime, Unk459, Unk460, Unk461, MeltDown, SparkleBurstStage, LightningCascade, BulletParty, Unk466,
            AuraScythe, Benediction, VoidStrike, ReduceHitDmgOnlyHPR, DivineEcho, WeaponAura, ManaOverload, RhoAias,
            PsychicTornado, SpreadThrow, MassDestructionRockets, ShadowAssault, BlitzShield, SplitShot, FreudBlessing,
            OverloadMode, IncMaxDamage, Unk483, Unk484, Unk485, Unk486, Unk487, IndieHyperStat, HayatoStance,
            HayatoBooster, HayatoStanceBonus, WillowDodge, Unk492, HayatoPAD, HayatoHPR, HayatoMPR, Jinsoku, HayatoCr,
            HakuBlessing, HayatoBoss, BattoujutsuAdvance, Unk504, Unk505, BlackHeartedCurse, EyeForEye, BeastMode,
            TeamRoar, Unk509, Unk510, Unk511, Unk512, Unk513, Unk514, Unk515, Unk516, Unk517, Unk518, Unk519, Unk520
    );

    private static final List<CharacterTemporaryStat> REMOTE_ORDER = Arrays.asList(
            Speed, ComboCounter, WeaponCharge, ElementalCharge, Stun, Shock, Darkness, Seal, Weakness, WeaknessMdamage,
            Curse, Slow, PvPRaceEffect, TimeBomb, Team, Disorder, Thread, Poison, ShadowPartner, DarkSight,
            SoulArrow, Morph, Ghost, Attract, Magnet, MagnetArea, NoBulletConsume, BanMap, Barrier, DojangShield,
            ReverseInput, RespectPImmune, RespectMImmune, DefenseAtt, DefenseState, DojangBerserk, DojangInvincible,
            RepeatEffect, StopPortion, StopMotion, Fear, MagicShield, Flying, Frozen, Frozen2, Web, DrawBack,
            FinalCut, Cyclone, OnCapsule, OnCapsule, Sneak, BeastFormDamageUp, Mechanic, BlessingArmor, BlessingArmorIncPAD,
            Inflation, Explosion, DarkTornado, AmplifyDamage, HideAttack, HolyMagicShell, DevilishPower, SpiritLink,
            Event, VampiricTouch, DeathMark, PainMark, Lapidification, VampDeath, VampDeathSummon, VenomSnake,
            PyramidEffect, KillingPoint, IgnoreTargetDEF, Invisible, Judgement, KeyDownAreaMoving,
            StackBuff, BlessOfDarkness, Larkness, ReshuffleSwitch, SpecialAction, StopForceAtomInfo, SoulGazeCriDamR,
            PowerTransferGauge, AffinitySlug, SoulExalt, HiddenPieceOn, SmashStack, MobZoneState, GiveMeHeal, TouchMe,
            Contagion, ComboUnlimited, IgnorePCounter, IgnoreAllCounter, IgnorePImmune, IgnoreAllImmune,
            FinalJudgement, Unk275, KnightsAura, IceAura, FireAura, VengeanceOfAngel, HeavensDoor, DamAbsorbShield,
            AntiMagicShell, NotDamaged, BleedingToxin, WindBreakerFinal, IgnoreMobDamR, Asura, OmegaBlaster,
            UnityOfPower, Stimulate, ReturnTeleport, CapDebuff, OverloadCount, FireBomb, SurplusSupply, NewFlying,
            NaviFlying, AmaranthGenerator, CygnusElementSkill, StrikerHyperElectric, EventPointAbsorb, EventAssemble,
            Albatross, Translucence, PoseType, LightOfSpirit, ElementSoul, GlimmeringTime, Reincarnation, Beholder,
            QuiverCatridge, ArmorPiercing, UserControlMob, ZeroAuraStr, ZeroAuraSpd, ImmuneBarrier, FullSoulMP,
            AntiMagicShell, Dance, SpiritGuard, ComboTempest, HalfstatByDebuff, ComplusionSlant, JaguarSummoned, BMageAura,
            MeltDown, SparkleBurstStage, LightningCascade, BulletParty, Unk466, AuraScythe, Benediction, DarkLighting,
            AttackCountX, FireBarrier, KeyDownMoving, MichaelSoulLink, KinesisPsychicEnergeShield,  BladeStance,
            Fever, AdrenalinBoost, RWBarrier, RWMagnumBlow, GuidedArrow, ExtraSkillCTS, Stigma, DivineEcho,
            RhoAias, PsychicTornado, MahasFury, ManaOverload, CursorSniping, Unk483, BeastMode, TeamRoar, HayatoStance,
            HayatoBooster, HayatoStanceBonus, HayatoPAD, HayatoHPR, HayatoMPR, HayatoCr, HayatoBoss,
            Stance, BattoujutsuAdvance, Unk505, BlackHeartedCurse, EyeForEye, Unk485, Unk510, Unk514, Unk515, Unk516,
            Unk518, Unk519, Unk520, Unk487
    );

    private static final List<CharacterTemporaryStat> INDIE_ORDER = Arrays.asList(
            IndiePAD, IndieMAD, IndieDEF, IndieMHP, IndieMHPR, IndieMMP, IndieMMPR, IndieACC, IndieEVA, IndieJump,
            IndieSpeed, IndieAllStat, IndieDodgeCriticalTime, IndieEXP, IndieBooster, IndieFixedDamageR,
            PyramidStunBuff, PyramidFrozenBuff, PyramidFireBuff, PyramidBonusDamageBuff, IndieRelaxEXP, IndieSTR,
            IndieDEX, IndieINT, IndieLUK, IndieDamR, IndieMDF, IndieMaxDamageOver, IndieAsrR, IndieTerR, IndieCr,
            IndieDEFR, IndieCrDmg, IndieBDR, IndieStatR, IndieStance, IndieIgnoreMobpdpR, IndieEmpty, IndiePADR,
            IndieMADR, IndieCrMaxR, IndieEVAR, IndieMDDR, IndieDrainHP, IndiePMdR, IndieMaxDamageOverR, IndieForceJump,
            IndieForceSpeed, IndieQrPointTerm, IndieHyperStat, IndieStatCount
    );

    private static final List<CharacterTemporaryStat> ENCODE_INT = Arrays.asList(
            ShadowPartner, Dance, SpiritLink, CarnivalDefence, DojangLuckyBonus, MagnetArea, VampDeath, SoulGazeCriDamR,
            PowerTransferGauge, IncMaxDamage, OmegaBlaster, ReturnTeleport, SetBaseDamage, NaviFlying, QuiverCatridge,
            ImmuneBarrier, Unk357, DotHealHPPerSecond, SetBaseDamageByBuff, DivineEcho, AranSmashSwing, Unk514,
            RideVehicle, RideVehicleExpire
    );

    public static final EnumSet<CharacterTemporaryStat> MOVING_AFFECTING_STAT = EnumSet.of(
            IndieJump, IndieSpeed, IndieForceJump, IndieForceSpeed, Speed, Jump, Stun, Weakness, Slow, Morph,
            BasicStatUp, Attract, Ghost, Flying, Frozen, Dance, Mechanic, DarkTornado, Frozen2, Lapidification,
            KeyDownMoving, Magnet, MagnetArea, VampDeath, VampDeathSummon, GiveMeHeal, TouchMe, NewFlying,
            NaviFlying, UserControlMob, SelfWeakness, BattlePvPHelenaWindSpirit, BattlePvPLeeMalNyunScaleUp,
            EnergyCharged, DashSpeed, DashJump, RideVehicle, RideVehicleExpire
    );

    public static final EnumSet<CharacterTemporaryStat> RESET_BY_TIME_CTS = EnumSet.of(
            Stun, Shock, Poison, Seal, Darkness, Weakness, WeaknessMdamage, Curse, Slow, /*TimeBomb,*/
            Disorder, Thread, Attract, Magnet, MagnetArea, ReverseInput, BanMap, StopPortion, StopMotion,
            Fear, Frozen, Frozen2, Web, NotDamaged, FinalCut, Lapidification, VampDeath, VampDeathSummon,
            GiveMeHeal, TouchMe, Contagion, ComboUnlimited, CrossOverChain, Reincarnation, ComboCostInc,
            DotBasedBuff, ExtremeArchery, QuiverCatridge, AdvancedQuiver, UserControlMob, ArmorPiercing,
            CriticalGrowing, QuickDraw, BowMasterConcentration, ComboTempest, SiphonVitality, KnockBack,
            RWMovingEvar
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
                        .toList().get(0);
    }

    public boolean isIndie() {
        return bitPos < IndieStatCount.bitPos;
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
            case OmegaBlaster:
            case ImmuneBarrier:
            case FullSoulMP:
            case Dance:
            case SpiritGuard:
            case KinesisPsychicEnergeShield:
            case BladeStance:
            case AdrenalinBoost:
            case RWBarrier:
            case RWMagnumBlow:
            case DivineEcho:
            case RhoAias:
            case PsychicTornado:
            case MahasFury:
            case ManaOverload:
            case Unk514:
            case Unk515:
            case Unk516:
            case Unk519:
            case Unk520:
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
            case PinkbeanYoYoStack:
            case ReturnTeleport:
            case FireBomb:
            case SurplusSupply:
            case AntiMagicShell:
            case Unk487:
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
            case PinkbeanYoYoStack:
            case StackBuff:
            case BlessOfDarkness:
            case SurplusSupply:
            case ImmuneBarrier:
            case AdrenalinBoost:
            case RWBarrier:
            case RWMagnumBlow:
            case PsychicTornado:
            case MahasFury:
            case ManaOverload:
            case Unk515:
            case Unk516:
            case Unk519:
            case Unk520:
            case Unk487:
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
            case OnCapsule:
            case Sneak:
            case BeastFormDamageUp:
            case BlessingArmor:
            case BlessingArmorIncPAD:
            case HolyMagicShell:
            case VengeanceOfAngel:
            case UserControlMob:
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
