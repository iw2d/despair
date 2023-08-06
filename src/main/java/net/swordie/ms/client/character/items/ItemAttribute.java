package net.swordie.ms.client.character.items;

/**
 * Item flags shared with Equip flags
 * Created on 1/25/2018.
 */
public enum ItemAttribute {
    Locked(0x1),
    PreventSlipping(0x2),
    KarmaUse(0x2),
    PreventColdness(0x4),
    UseBag(0x4),
    Untradable(0x8),
    UntradableAfterTransaction(0x10),
    CraftedUse(0x10),
    NoNonCombatStatGain(0x20),

    Crafted(0x80),
    ProtectionScroll(0x100),
    LuckyDay(0x200),

    TradedOnceWithinAccount(0x1000),
    UpgradeCountProtection(0x2000),
    ScrollProtection(0x4000),
    ReturnScroll(0x8000);

    private int val;

    ItemAttribute(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

}
