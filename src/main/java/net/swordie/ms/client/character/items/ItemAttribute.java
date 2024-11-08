package net.swordie.ms.client.character.items;

import net.swordie.ms.constants.ItemConstants;

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
    Used(0x40),
    Crafted(0x80),
    ProtectionScroll(0x100),
    LuckyDay(0x200),
    CubeExOptLv(0x400),
    CubeExOptLv2(0x800),
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

    public static void handleTradeAttribute(Item item) {
        if (ItemConstants.isEquip(item.getItemId())) {
            if (item.hasAttribute(ItemAttribute.UntradableAfterTransaction)) {
                item.removeAttribute(ItemAttribute.UntradableAfterTransaction);
                item.addAttribute(ItemAttribute.Untradable);
            }
        } else {
            if (item.hasAttribute(ItemAttribute.KarmaUse)) {
                item.removeAttribute(ItemAttribute.KarmaUse);
                item.addAttribute(ItemAttribute.Untradable);
            }
        }
    }

}
