package net.swordie.ms.enums;

import java.util.Arrays;

/**
 * Created on 6/7/2018.
 */
public enum UserEffectType {
    LevelUp(0),
    SkillUse(1),
    SkillUseBySummoned(2),
    Unk3(3),
    SkillAffected(4),
    SkillAffected_Ex(5),
    SkillAffected_Select(6),
    SkillSpecialAffected(7),
    Quest(8),
    Pet(9),
    SkillSpecial(10),
    Resist(11),
    ProtectOnDieItemUse(12),
    PlayPortalSE(13),
    JobChanged(14),
    QuestComplete(15),
    IncDecHPEffect(16),
    BuffItemEffect(17),
    SquibEffect(18),
    MonsterBookCardGet(19),
    LotteryUse(20),
    ItemLevelUp(21),
    ItemMaker(22), // EnchantSuccess
    Unk23(23),
    ExpItemConsumed(24),
    FieldExpItemConsumed(25),
    ReservedEffect(26),
    WheelOfDestiny(28),
    PremiumWheelOfDestiny(29),
    BattlefieldItemUse(30),
    AvatarOriented(31),
    AvatarOrientedRepeat(32),
    AvatarOrientedMultipleRepeat(33),
    IncubatorUse(34),
    PlaySoundWithMuteBGM(35),
    PlayExclSoundWithDownBGM(36),
    SoulStoneUse(37),
    IncDecHPEffect_EX(38),
    IncDecHPRegenEffect(39),
    EffectUOL(40),
    PvPRage(41),
    PvPChampion(42),
    PvPGradeUp(43),
    PvPRevive(44),
    PvPJobEffect(45),
    FadeInOut(46),
    MobSkillHit(47),
    AswanSiegeAttack(48),
    BlindEffect(49),
    BossShieldEffect(50),
    ResetOnStateForOnOffSkill(51),
    JewelCraft(52),
    ConsumeEffect(53),
    PetBuff(54),
    LotteryUIResult(55),
    LeftMonsterNumber(56),
    ReservedEffectRepeat(57),
    RobbinsBomb(58),
    SkillMode(59),
    ActQuestComplete(60),
    Point(61),
    SpeechBalloon(62),
    TextEffect(63),
    SkillPreLoopEnd(64),
    Aiming(65),
    PickUpItem(66),
    BattlePvP_IncDecHp(67),
    BiteAttack_ReceiveSuccess(68),
    BiteAttack_ReceiveFail(69),
    IncDecHPEffect_Delayed(70),
    Lightness(71),
    FoxManActionSetUsed(72),
    Unk73(73), // int, int, int
    SkillMoveEffect(74), // Skill/%03d.img/skill/%07d/moveEffect
    Unk75(75), // same as 76 but with int -> play_item_sound
    Unk76(76),
    UpgradePotionMsg(77),
    MonsterBookSetComplete(78),
    FamiliarEscape(79),
    Unk80(80), // int, int Win Lose Draw?
    ;

    private byte val;

    UserEffectType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public static UserEffectType getTypeByVal(int val) {
        return Arrays.stream(values()).filter(uet -> uet.getVal() == val).findAny().orElse(null);
    }
}
