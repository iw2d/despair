package net.swordie.ms.connection.packet;

import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.User;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.trunk.Trunk;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.enums.CashItemType;
import net.swordie.ms.enums.CashShopActionType;
import net.swordie.ms.enums.CashShopFailReason;
import net.swordie.ms.enums.CashShopInfoType;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.world.shop.cashshop.*;

import java.util.List;
import java.util.Set;

/**
 * Created on 4/23/2018.
 */
public class CCashShop {
    public static OutPacket queryCashResult(Char chr) {
        User user = chr.getUser();
        Account account = chr.getAccount();

        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_QUERY_CASH_RESULT);
        outPacket.encodeInt(account.getNxCredit());
        outPacket.encodeInt(user.getMaplePoints());
        outPacket.encodeInt(chr.getRewardPoints());
        outPacket.encodeInt(user.getNxPrepaid());
        return outPacket;
    }

    public static OutPacket cashItemResBuyDone(CashItemInfo cashItemInfo, FileTime registerDate, CashItemInfo receiveBonus) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_Buy_Done.getVal());
        cashItemInfo.encode(outPacket);
        boolean hasRegisterDate = registerDate != null;
        outPacket.encodeInt(hasRegisterDate ? 1 : 0);
        if (hasRegisterDate) {
            outPacket.encodeFT(registerDate);
        }
        boolean hasReceiveBonus = receiveBonus != null;
        outPacket.encodeByte(hasReceiveBonus);
        if (receiveBonus != null) {
            receiveBonus.encode(outPacket);
        }
        boolean bool = false;
        outPacket.encodeByte(bool);
        if (bool) {
            outPacket.encodeInt(0); // Total spent since %s : %d NX
        }
        return outPacket;
    }

    public static OutPacket cashItemResGiftDone(String receiver, int itemId, int quantity, int cost) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_Gift_Done.getVal());
        outPacket.encodeString(receiver);
        outPacket.encodeInt(itemId);
        outPacket.encodeShort(quantity);
        outPacket.encodeInt(cost);
        boolean bool = false;
        outPacket.encodeByte(bool);
        if (bool) {
            outPacket.encodeInt(0); // Total spent since %s : %d NX
        }
        return outPacket;
    }

    public static OutPacket cashItemResCoupleDone(CashItemInfo cii, String receiver) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_Couple_Done.getVal());
        cii.encode(outPacket);
        outPacket.encodeString(receiver);
        outPacket.encodeInt(cii.getItemID());
        outPacket.encodeShort(cii.getQuantity());
        boolean bool = false;
        outPacket.encodeByte(bool);
        if (bool) {
            outPacket.encodeInt(0); // Total spent since %s : %d NX
        }
        return outPacket;
    }

    public static OutPacket cashItemResIncSlotCountDone(byte invType, int newSlotCount) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_IncSlotCount_Done.getVal());
        outPacket.encodeByte(invType);
        outPacket.encodeShort(newSlotCount);
        boolean bool = false;
        outPacket.encodeByte(bool);
        if (bool) {
            outPacket.encodeInt(0); // Total spent since %s : %d NX
        }
        return outPacket;
    }

    public static OutPacket cashItemResIncTrunkCountDone(int newSlotCount) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_IncTrunkCount_Done.getVal());
        outPacket.encodeShort(newSlotCount);
        boolean bool = false;
        outPacket.encodeByte(bool);
        if (bool) {
            outPacket.encodeInt(0); // Total spent since %s : %d NX
        }
        return outPacket;
    }

    public static OutPacket webShopOrderGetList_Done(List<CashItemInfo> firstList, List<Item> secondList) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_WebShopOrderGetList_Done.getVal());
        outPacket.encodeShort(firstList.size());
        for (CashItemInfo cii : firstList) {
            cii.encode(outPacket);
        }
        outPacket.encodeShort(secondList.size());
        for (Item item : secondList) {
            item.encode(outPacket);
        }
        return outPacket;
    }

    public static OutPacket infoItems(CashShopInfoType csit, List<CashShopItem> items) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_INFO);
        outPacket.encodeByte(csit.getVal());
        outPacket.encodeByte(1); // 0 does not encode anything, 2 does the same as 1 (encoding wise)
        outPacket.encodeByte(items.size()); // size
        for (CashShopItem csi : items) {
            csi.encode(outPacket);
        }
        return outPacket;
    }

    public static OutPacket cartInfo(CashShop cashShop) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_INFO);
        outPacket.encodeByte(CashShopInfoType.Cart.getVal());
        outPacket.encodeByte(1); // 0 does not encode anything, 2 does the same as 1 (encoding wise)
        outPacket.encodeByte(0); // size
        return outPacket;
    }

    public static OutPacket categoryInfo(CashShop cashShop) {
        List<CashShopCategory> categories = cashShop.getCategories();
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_INFO);
        outPacket.encodeByte(CashShopInfoType.Categories.getVal());
        outPacket.encodeByte(1);
        outPacket.encodeByte(categories == null ? 0 : categories.size());
        categories.forEach(cat -> cat.encode(outPacket));
        return outPacket;
    }

    public static OutPacket bannerMsg(CashShop cashShop, List<String> messages) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_INFO);
        outPacket.encodeByte(CashShopInfoType.BannerMsg.getVal());
        outPacket.encodeByte(messages.size());
        messages.forEach(msg -> {
            outPacket.encodeString(msg);
            outPacket.encodeLong(0);
            outPacket.encodeLong(0);
        });
        return outPacket;
    }

    public static OutPacket listItems(CashShop cashShop, Char chr, CashShopActionType cast, List<CashShopItem> items) {
        // the number of items is normally a byte, but changed to a short in CASH_SHOP_ACTION (0x00A43AB0)
        // but it results in error38 when the number of items is equal to or greater than 474 for some reason
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_ACTION);
        outPacket.encodeByte(cast.getVal()); // 11, 13, 18
        outPacket.encodeByte(1);
        List<CashShopItem> trimmedItems = items.subList(0, Math.min(items.size(), GameConstants.MAX_CS_ITEMS_PER_CATEGORY));
        if (ServerConstants.CASH_SHOP_ITEM_COUNT_HOOK) {
            outPacket.encodeShort(trimmedItems.size());
        } else {
            outPacket.encodeByte(trimmedItems.size());
        }
        Set<CashShopItem> favorites = cashShop.getFavorites(chr.getAccount().getId());
        trimmedItems.forEach(item -> item.encode(outPacket, favorites.contains(item), false));
        return outPacket;
    }

    public static OutPacket openCategoryResult(CashShop cashShop, Char chr, int categoryIdx) {
        List<CashShopItem> items = cashShop.getItemsByCategoryIdx(categoryIdx);
        return listItems(cashShop, chr, CashShopActionType.ShowCategory, items);
    }

    public static OutPacket showFavorites(Char chr, CashShop cashShop) {
        List<CashShopItem> items = cashShop.getFavorites(chr.getAccount().getId()).stream().toList();
        return listItems(cashShop, chr, CashShopActionType.ShowFavorites, items);
    }

    public static OutPacket favorite(boolean add, int itemSn) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_ACTION);
        CashShopActionType cast = add ? CashShopActionType.AddFavorite : CashShopActionType.RemoveFavorite;
        outPacket.encodeByte(cast.getVal());
        outPacket.encodeByte(1); // success
        outPacket.encodeInt(itemSn);
        return outPacket;
    }

    public static OutPacket resMoveLtoSDone(Item item) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_MoveLtoS_Done.getVal());
        outPacket.encodeByte(true); // bExclRequestSent
        outPacket.encodeShort(item.getBagIndex());
        item.encode(outPacket);
        outPacket.encodeInt(0); // List of SNs (longs)
        outPacket.encodeByte(0); // Bonus cash item (CashItemInfo::Decode)
        return outPacket;
    }

    public static OutPacket resMoveStoLDone(CashItemInfo cii) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_MoveStoL_Done.getVal());
        cii.encode(outPacket);
        return outPacket;
    }

    public static OutPacket resRebateDone(CashItemInfo cii) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_Rebate_Done.getVal());
        outPacket.encodeByte(0);
        outPacket.encodeLong(cii.getCashItemSN());
        return outPacket;
    }

    public static OutPacket loadLockerDone(Account account) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_LoadLocker_Done.getVal());
        Trunk trunk = account.getTrunk();
        List<CashItemInfo> locker = trunk.getLocker();
        int lockerSize = locker.size();
        boolean isOverMaxSlots = lockerSize > GameConstants.MAX_LOCKER_SIZE;
        outPacket.encodeByte(isOverMaxSlots);
        if (isOverMaxSlots) {
            outPacket.encodeInt(lockerSize - GameConstants.MAX_LOCKER_SIZE);
        }
        outPacket.encodeShort(lockerSize);
        locker.forEach(item -> {
            item.encode(outPacket);
        });
        int size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            outPacket.encode(new Item()); // bonus items
        }
        outPacket.encodeShort(account.getTrunk().getSlotCount());
        outPacket.encodeShort(account.getUser().getCharacterSlots());
        outPacket.encodeShort(0); // nBuyCharacterCount
        outPacket.encodeShort(account.getCharacters().size());
        return outPacket;
    }

    public static OutPacket fullInventoryMsg() {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_MoveLtoS_Failed.getVal());
        outPacket.encodeByte(10);
        return outPacket;
    }

    public static OutPacket loadGift(List<CashShopGift> gifts) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_LoadGift_Done.getVal());
        outPacket.encodeShort(gifts.size());
        gifts.forEach(csg -> csg.encode(outPacket));
        return outPacket;
    }

    public static OutPacket surpriseBox(CashItemInfo reward, CashItemInfo box, boolean Rare) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_GAIN_ITEM);
        outPacket.encodeByte(CashItemType.Res_PremiumStyle.getVal());
        outPacket.encodeLong(box.getCashItemSN());
        outPacket.encodeInt(box.getItemID());
        box.encode(outPacket);
        // sub_1D910F0
        outPacket.encodeInt(reward.getItemID());
        outPacket.encodeByte(1); //Quantity?
        outPacket.encodeByte(Rare);
        return outPacket;
    }

    public static OutPacket error() {
        return error(CashShopFailReason.UnknownError);
    }

    public static OutPacket error(CashShopFailReason failReason) {
        OutPacket outPacket = new OutPacket(OutHeader.CASH_SHOP_CASH_ITEM_RESULT);
        outPacket.encodeByte(CashItemType.Res_Error.getVal());
        outPacket.encodeByte(failReason.getVal());
        return outPacket;
    }
}
