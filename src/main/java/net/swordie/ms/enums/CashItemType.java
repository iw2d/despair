package net.swordie.ms.enums;

import java.util.*;

/**
 * @author Sjonnie
 * Created on 7/4/2018.
 */
public enum CashItemType {
    Req_WebShopOrderGetList(0),
    Req_LoadLocker(1),
//    Req_LoadWish(2),
    Req_Buy(2),
    Req_Gift(3),
    Req_SetWish(4),
    Req_IncSlotCount(5),
    Req_MaxSlotCount(5), // 9116000
    Req_IncTrunkCount(7),
    Req_IncCharSlotCount(8),
    Req_IncBuyCharCount(9),
    Req_EnableEquipSlotExt(10),
    Req_CancelPurchase(11),
    Req_ConfirmPurchase(12),
    Req_Destroy(13),
    Req_MoveLtoS(15),
    Req_MoveStoL(16),
    Req_Expire(17),
    Req_Use(18),
    Req_StatChange(19),
    Req_SkillChange(20),
    Req_SkillReset(21),
    Req_DestroyPetItem(22),
    Req_SetPetName(23),
    Req_SetPetLife(24),
    Req_SetPetSkill(25),
    Req_SetItemName(26),
    Req_SetAndroidName(27),
    Req_SendMemo(28),
    Req_GetAdditionalCashShopInfo(29),
    Req_GetMaplePoint(30),
    Req_UseMaplePointFromGameSvr(31),
    Req_Rebate(32),
    Req_UseCoupon(33),
    Req_GiftCoupon(34),
    Req_Couple(35),
    Req_BuyPackage(36),
    Req_GiftPackage(37),
    Req_BuyNormal(38),
    Req_ApplyWishListEvent(39),
    Req_MovePetStat(40),
    Req_FriendShip(41),
    Req_ShopScan(42),
    Req_ShopOptionScan(43),
    Req_ShopScanSell(44),
    Req_LoadPetExceptionList(45),
    Req_UpdatePetExceptionList(46),
    Req_DestroyScript(47),
    Req_CashItemCreate(48),
    Req_PurchaseRecord(49),
    Req_DeletePurchaseRecord(50),
    Req_TradeDone(51),
    Req_BuyDone(52),
    Req_TradeSave(53),
    Req_TradeLog(54),
    Req_CharacterSale(55),
    Req_SellCashItemBundleToShop(56),
    Req_Refund(57),
    Req_ConfirmRefund(58),
    Req_CancelRefund(59),
    Req_SetItemNonRefundable(60),
    Req_WebShopOrderBuyItems(61),
    Req_UseCashRandomItem(62),
    Req_UseMaplePointGiftToken(63),
    Req_BuyByToken(64),
    Req_Buy_ByMeso(65),
    Req_UpgradeValuePack(66),
    Req_BuyFarmGift(67),
    Req_CashItemGachapon(68),
    Req_GiftScript(69),
    Req_MoveToAuctionStore(70),
    Req_ClearCashOption(71),
    Req_MasterPiece(72),
    Req_DestroyCoupleRings(73),
    Req_DestroyFriendshipRings(74),
    Req_LockerTransfer(75),
    Req_TradeLogForAuction(76),
    Req_MoveToLockerFromAuction(77),
    Req_NexonStarCouponUse(78),


    Res_CharacterSaleSuccess(0),
    Res_CharacterSaleFail(1),
    Res_LimitGoodsCount_Changed(-1),
    Res_WebShopOrderGetList_Done(-1),
    Res_WebShopOrderGetList_Failed(-1),
    Res_WebShopReceive_Done(5),
    Res_LoadLocker_Done(2), // correct
    Res_LoadLocker_Failed(3), // correct
    Res_LoadGift_Done(8),
    Res_LoadGift_Failed(9),
    Res_LoadWish_Done(10),
    Res_LoadWish_Failed(11),// ^----- maybe incorrect
    Res_SetWish_Done(11),
    Res_SetWish_Failed(12),
    Res_Buy_Done(13),
    Res_Buy_Failed(14),
    Res_UseCoupon_Done(15),
    Res_GiftCoupon_Done(19),
    Res_UseCoupon_Failed(20),
    Res_Gift_Done(22),
    Res_Gift_Failed(23),
    Res_IncSlotCount_Done(24),
    Res_IncSlotCount_Failed(25),
    Res_Unk26(26),
    Res_Unk27(27),
    Res_IncTrunkCount_Done(28),
    Res_IncTrunkCount_Failed(29),
    Res_IncCharSlotCount_Done(30),
    Res_IncCharSlotCount_Failed(31),
    Res_IncBuyCharCount_Done(32), // Character Card?
    Res_IncBuyCharCount_Failed(33),
    Res_EnableEquipSlotExt_Done(36),
    Res_EnableEquipSlotExt_Failed(37),
    Res_MoveLtoS_Done(38),
    Res_MoveLtoS_Failed(39),
    Res_MoveStoL_Done(40),
    Res_MoveStoL_Failed(41),
    Res_Destroy_Done(42),
    Res_Destroy_Failed(43),
    Res_Expire_Done(44),
    Res_Expire_Failed(45),
    Res_Use_Done(46),
    Res_Use_Failed(47),
    Res_StatChange_Done(48),
    Res_StatChange_Failed(49),
    Res_SkillChange_Done(50),
    Res_SkillChange_Failed(51),
    Res_SkillReset_Done(52),
    Res_SkillReset_Failed(53),
    Res_DestroyPetItem_Done(54),
    Res_DestroyPetItem_Failed(55),
    Res_SetPetName_Done(56),
    Res_SetPetName_Failed(57),
    Res_SetPetLife_Done(58),
    Res_SetPetLife_Failed(59),
    Res_MovePetStat_Failed(60),
    Res_MovePetStat_Done(61),
    Res_SetPetSkill_Failed(62),
    Res_SetPetSkill_Done(63),
    Res_SendMemo_Done(64),
    Res_SendMemo_Warning(65),
    Res_SendMemo_Failed(66),
    Res_GetMaplePoint_Done(67),
    Res_GetMaplePoint_Failed(68),
    Res_UseMaplePointFromGameSvr_Done(69),
    Res_UseMaplePointFromGameSvr_Failed(70),
    Res_CashItemGachapon_Done(71),
    Res_CashItemGachapon_Failed(72),
    Res_Rebate_Done(73),
    Res_Rebate_Failed(74),
    Res_Couple_Done(75),
    Res_Couple_Failed(76),
    Res_BuyPackage_Done(77),
    Res_BuyPackage_Failed(78),
    Res_GiftPackage_Done(79),
    Res_GiftPackage_Failed(80),
    Res_BuyNormal_Done(81),
    Res_BuyNormal_Failed(82), // should be 83
    Res_ApplyWishListEvent_Done(83),
    Res_ApplyWishListEvent_Failed(84),
    Res_Friendship_Done(85),
    Res_Friendship_Failed(86),
    Res_LoadExceptionList_Done(87),
    Res_LoadExceptionList_Failed(88),
    Res_UpdateExceptionList_Done(89),
    Res_UpdateExceptionList_Failed(90),
    Res_DestroyScript_Done(91),
    Res_DestroyScript_Failed(92),
    Res_CashItemCreate_Done(93),
    Res_CashItemCreate_Failed(94),
    Res_ClearOptionScript_Done(95),
    Res_ClearOptionScript_Failed(96),
    Res_Bridge_Failed(97),
    Res_PurchaseRecord_Done(98),
    Res_PurchaseRecord_Failed(99),
    Res_DeletePurchaseRecord_Done(100),
    Res_DeletePurchaseRecord_Failed(101),
    Res_Refund_OK(102),
    Res_Refund_Done(103),
    Res_Refund_Failed(104),
    Res_UseRandomCashItem_Done(105),
    Res_UseRandomCashItem_Failed(106),
    Res_SetAndroidName_Done(107),
    Res_SetAndroidName_Failed(108),
    Res_UseMaplePointGiftToken_Done(109),
    Res_UseMaplePointGiftToken_Failed(110),
    Res_BuyByToken_Done(111),
    Res_BuyByToken_Failed(112),
    Res_UpgradeValuePack_Done(113),
    Res_UpgradeValuePack_Failed(114),
    Res_EventCashItem_Buy_Result(115),
    Res_BuyFarmGift_Done(116),
    Res_BuyFarmGift_Failed(117),
    Res_GiftScript_Done(118),
    Res_GiftScript_Failed(119),
    Res_AvatarMegaphone_Queue_Full(120),
    Res_AvatarMegaphone_Level_Limit(121),
    Res_MovoCashItemToAuction_Done(122),
    Res_MovoCashItemToAuction_Failed(123),
    Res_MasterPiece_Done(124),
    Res_MasterPiece_Failed(125),
    Res_DestroyCoupleRings_Done(126),
    Res_DestroyCoupleRings_Failed(127),
    Res_DestroyFriendShipRings_Done(128),
    Res_DestroyFriendShipRings_Failed(129),
    Res_LockerTransfer_Done(130),
    Res_LockerTransfer_Failed(131),
    Res_MovoCashItemToLockerFromAuction_Done(132),
    Res_MovoCashItemToLockerFromAuction_Failed(133),
    Res_PremiumStyle(160),



    FailReason_Unknown(0),
    FailReason_Timeout(1),
    FailReason_CashDaemonDBError(2),
    FailReason_NoRemainCash(3),
    FailReason_GiftUnderAge(4),
    FailReason_GiftLimitOver(5),
    FailReason_GiftSameAccount(6),
    FailReason_GiftUnknownRecipient(7),
    FailReason_GiftRecipientGenderMismatch(8),
    FailReason_GiftRecipientLockerFull(9),
    FailReason_BuyStoredProcFailed(10),
    FailReason_GiftStoredProcFailed(11),
    FailReason_GiftNoReceiveCharacter(12),
    FailReason_GiftNoSenderCharacter(13),
    FailReason_InvalidCoupon(14),
    FailReason_ExpiredCoupon(15),
    FailReason_UsedCoupon(16),
    FailReason_CouponForCafeOnly(17),
    FailReason_CouponForCafeOnly_Used(18),
    FailReason_CouponForCafeOnly_Expired(19),
    FailReason_NotAvailableCoupon(20),
    FailReason_GenderMisMatch(21),
    FailReason_GiftNormalItem(22),
    FailReason_GiftMaplePoint(23),
    FailReason_NoEmptyPos(24),
    FailReason_ForPremiumUserOnly(25),
    FailReason_BuyCoupleStoredProcFailed(26),
    FailReason_BuyFriendshipStoredProcFailed(27),
    FailReason_NotAvailableTime(28),
    FailReason_NoStock(29),
    FailReason_PurchaseLimitOver(30),
    FailReason_NoRemainMeso(31),
    FailReason_IncorrectSSN2(32),
    FailReason_IncorrectSPW(33),
    FailReason_ForNoPurchaseExpUsersOnly(34),
    FailReason_AlreadyApplied(35),
    FailReason_WebShopUnknown(36),
    FailReason_WebShopInventoryCount(37),
    FailReason_WebShopBuyStoredProcFailed(38),
    FailReason_WebShopInvalidOrder(39),
    FailReason_GachaponLimitOver(40),
    FailReason_NoUser(41),
    FailReason_WrongCommoditySN(42),
    FailReason_CouponLimitError(43),
    FailReason_CouponLimitError_Hour(44),
    FailReason_CouponLimitError_Day(45),
    FailReason_CouponLimitError_Week(46),
    FailReason_BridgeNotConnected(47),
    FailReason_TooYoungToBuy(48),
    FailReason_GiftTooYoungToRecv(49),
    FailReason_LimitOverTheItem(50),
    FailReason_CashLock(51),
    FailReason_FindSlotPos(52),
    FailReason_GetItem(53),
    FailReason_DestroyCashItem(54),
    FailReason_NotSaleTerm(55),
    FailReason_InvalidCashItem(56),
    FailReason_InvalidRandomCashItem(57),
    FailReason_ReceiveItem(58),
    FailReason_UseRandomCashItem(59),
    FailReason_NotGameSvr(60),
    FailReason_NotShopSvr(61),
    FailReason_ItemLockerIsFull(62),
    FailReason_NoAndroid(63),
    FailReason_DBQueryFailed(64),
    FailReason_UserSaveFailed(65),
    FailReason_CannotBuyMonthlyOnceItem(66),
    FailReason_OnlyCashItem(67),
    FailReason_NotEnoughMaplePoint(68),
    FailReason_TooMuchMaplePointAlready(69),
    FailReason_GiveMaplePointUnknown(70),
    FailReason_OnWorld(71),
    FailReason_NoRemainToken(72),
    FailReason_GiftToken(73),
    FailReason_LimitOverCharacter(74),
    FailReason_CurrentValuePack(75),
    FailReason_NoRemainCashMileage(76),
    FailReason_NotEquipItem(77),
    FailReason_DoNotReceiveCashItemInvenFull(78),
    FailReason_DoNotCheckQuest(79),
    FailReason_SpecialServerUnable(80),
    FailReason_BuyWSLimit(81),
    FailReason_NoNISMS(82),
    FailReason_RefundExpired(83),
    FailReason_NoRefundItem(84),
    FailReason_NoRefundPackage(85),
    FailReason_PurchaseItemLimitOver(86),
    FailReason_OTPStateError(87),
    FailReason_WrongPassword(88),
    FailReason_CountOver(89),
    FailReason_Reissuing(90),
    FailReason_NotExist(91),
    FailReason_NotAvailableLockerTransfer(92),
    FailReason_DormancyAccount(93);

    private int val;

    CashItemType(int val) {
        this.val = val;
    }

    public static CashItemType getRequestTypeByVal(byte type) {
        return Arrays.stream(values()).filter(cit -> cit.toString().startsWith("Req") && cit.getVal() == type).findAny().orElse(null);
    }

    public static CashItemType getResultTypeByVal(byte type) {
        return Arrays.stream(values()).filter(cit -> cit.toString().startsWith("Res") && cit.getVal() == type).findAny().orElse(null);
    }

    public int getVal() {
        return val;
    }
}
