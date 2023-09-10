package net.swordie.ms.client.party;

import java.util.Arrays;

/**
 * Created on 3/19/2018.
 */
public enum PartyType {
    PartyReq_LoadParty(0),
    PartyReq_CreateNewParty(1),
    PartyReq_WithdrawParty(2),
    PartyReq_JoinParty(3),
    PartyReq_InviteParty(4),
    PartyReq_InviteIntrusion(5), // member -> party request
    PartyReq_KickParty(6), // party -> member request
    PartyReq_ChangePartyBoss(7),
    PartyReq_ApplyParty(8),
    PartyReq_SetAppliable(9),
    PartyReq_ClearIntrusion(10),
    PartyReq_CreateNewParty_Group(11),
    PartyReq_JoinParty_Group(12),
    PartyReq_PartySetting(13),
    PartyReq_LoadStarPlanetPoint(14),

    PartyRes_LoadParty_Done(16),
    PartyRes_CreateNewParty_Done(17),
    PartyRes_CreateNewParty_AlreayJoined(18),
    PartyRes_CreateNewParty_Beginner(19),
    PartyRes_CreateNewParty_Unknown(20),
    PartyRes_CreateNewParty_byNonBoss(21),

    PartyRes_WithdrawParty_Done(22),
    PartyRes_WithdrawParty_NotJoined(23),
    PartyRes_WithdrawParty_Unknown(24),

    PartyRes_JoinParty_Done(25),
    PartyRes_JoinParty_Done2(26), // join msg

    PartyRes_JoinParty_AlreadyJoined(27),
    PartyRes_JoinParty_AlreadyFull(28),
    PartyRes_JoinParty_OverDesiredSize(29),
    PartyRes_JoinParty_UnknownUser(30),
    PartyRes_JoinParty_Unknown(31),

    PartyRes_JoinIntrusion_Done(32),
    PartyRes_JoinIntrusion_UnknownParty(33),

    PartyRes_InviteParty_Sent(34),
    PartyRes_InviteParty_BlockedUser(35),
    PartyRes_InviteParty_AlreadyInvited(36),
    PartyRes_InviteParty_AlreadyInvitedByInviter(37),
    PartyRes_InviteParty_Rejected(38),
    PartyRes_InviteParty_Accepted(39),

    PartyRes_InviteIntrusion_Sent(40),
    PartyRes_InviteIntrusion_BlockedUser(41),
    PartyRes_InviteIntrusion_AlreadyInvited(42),
    PartyRes_InviteIntrusion_AlreadyInvitedByInviter(43),
    PartyRes_InviteIntrusion_Rejected(44),
    PartyRes_InviteIntrusion_Accepted(45),

    PartyRes_KickParty_Done(46),
    PartyRes_KickParty_FieldLimit(47),
    PartyRes_KickParty_Unknown(48),
    PartyRes_KickParty_Unavailable(49),

    PartyRes_ChangePartyBoss_Done(50),
    PartyRes_ChangePartyBoss_NotSameField(51),
    PartyRes_ChangePartyBoss_NoMemberInSameField(52),
    PartyRes_ChangePartyBoss_NotSameChannel(53),
    PartyRes_ChangePartyBoss_Unknown(54),

    PartyRes_AdminCannotCreate(55),
    PartyRes_AdminCannotInvite(56),

    PartyRes_InAnotherWorld(57),
    PartyRes_InAnotherChanelBlockedUser(58),

    PartyRes_UserMigration(59),
    PartyRes_ChangeLevelOrJob(60),
    PartyRes_UpdateShutdownStatus(61),
    PartyRes_SetAppliable(63),
    PartyRes_SetAppliableFailed(64),
    PartyRes_SuccessToSelectPQReward(65),
    PartyRes_FailToSelectPQReward(66),
    PartyRes_ReceivePQReward(67),
    PartyRes_FailToRequestPQReward(68),
    PartyRes_CanNotInThisField(69),

    PartyRes_ApplyParty_Sent(70),
    PartyRes_ApplyParty_UnknownParty(71),
    PartyRes_ApplyParty_BlockedUser(72),
    PartyRes_ApplyParty_AlreadyApplied(73),
    PartyRes_ApplyParty_AlreadyAppliedByApplier(74),
    PartyRes_ApplyParty_AlreadyFull(75),
    PartyRes_ApplyParty_Rejected(76),
    PartyRes_ApplyParty_Accepted(77),

    PartyRes_FoundPossibleMember(78),
    PartyRes_FoundPossibleParty(79),

    PartyRes_PartySettingDone(80),
    PartyRes_Load_StarGrade_Result(81),
    PartyRes_Load_StarGrade_Result2(82),
    PartyRes_Member_Rename(83),
    PartyRes_Unk85(85),

    PartyInfo_TownPortalChanged(86),
    PartyInfo_OpenGate(87),

    // below enums unchecked
    ExpeditionReq_Load(88),
    ExpeditionReq_CreateNew(89),
    ExpeditionReq_Invite(90),
    ExpeditionReq_ResponseInvite(91),
    ExpeditionReq_Withdraw(92),
    ExpeditionReq_Kick(93),
    ExpeditionReq_ChangeMaster(94),
    ExpeditionReq_ChangePartyBoss(95),
    ExpeditionReq_RelocateMember(96),

    ExpeditionNoti_Load_Done(97),
    ExpeditionNoti_Load_Fail(98),
    ExpeditionNoti_CreateNew_Done(99),
    ExpeditionNoti_Join_Done(100),
    ExpeditionNoti_You_Joined(101),
    ExpeditionNoti_You_Joined2(102),
    ExpeditionNoti_Join_Fail(103),
    ExpeditionNoti_Withdraw_Done(104),
    ExpeditionNoti_You_Withdrew(105),
    ExpeditionNoti_Kick_Done(106),
    ExpeditionNoti_You_Kicked(107),
    ExpeditionNoti_Removed(108),
    ExpeditionNoti_MasterChanged(109),
    ExpeditionNoti_Modified(110),
    ExpeditionNoti_Modified2(111),
    ExpeditionNoti_Invite(112),
    ExpeditionNoti_ResponseInvite(113),
    ExpeditionNoti_Create_Fail_By_Over_Weekly_Counter(114),
    ExpeditionNoti_Invite_Fail_By_Over_Weekly_Counter(115),
    ExpeditionNoti_Apply_Fail_By_Over_Weekly_Counter(116),
    ExpeditionNoti_Invite_Fail_By_Blocked_Behavior(117),

    AdverNoti_LoadDone(118),
    AdverNoti_Change(119),
    AdverNoti_Remove(120),
    AdverNoti_GetAll(121),
    AdverNoti_Apply(122),
    AdverNoti_ResultApply(123),
    AdverNoti_AddFail(124),
    AdverReq_Add(125),
    AdverReq_Remove(126),
    AdverReq_GetAll(127),
    AdverReq_RemoveUserFromNotiList(128),
    AdverReq_Apply(129),
    AdverReq_ResultApply(130),

    ;

    private byte val;

    PartyType(int val) {
        this.val = (byte) val;
    }

    public static PartyType getByVal(byte val) {
        return Arrays.stream(values()).filter(pt -> pt.getVal() == val).findAny().orElse(null);
    }

    public byte getVal() {
        return val;
    }
}
