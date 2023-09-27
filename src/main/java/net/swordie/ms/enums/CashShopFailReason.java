package net.swordie.ms.enums;

public enum CashShopFailReason {
    // CCashShop::NoticeFailReason
    TimedOut(1), // Request timed out.\r\nPlease try again.
    NotEnoughCash(3), // You don't have enough cash.
    CannotGiftOwnAccount(6), // You cannot send a gift to your own account.\r\nPlease purchase it after logging\r\nin with the related character.
    CannotFindCharacter(7), // That character could not be found in this world.\r\nGifts can only be sent to character\r\nin the same world.
    RecipientInventoryFull(9), // The gift cannot be sent because\r\nthe recipient's Inventory is full.
    TooManyCashItems(10), // You have too many Cash Items.\r\nPlease clear Cash slot and try again.
    CheckPICPassword(36), // Check your PIC password and\r\nplease try again.
    UnknownError(-1), // default: Due to an unknown error,\r\nthe request for Cash Shop has failed.
    ;

    private int val;

    CashShopFailReason(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
