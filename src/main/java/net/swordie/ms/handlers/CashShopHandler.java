package net.swordie.ms.handlers;

import net.swordie.ms.Server;
import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.Client;
import net.swordie.ms.client.User;
import net.swordie.ms.client.anticheat.Offense;
import net.swordie.ms.client.character.BroadcastMsg;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.Equip;
import net.swordie.ms.client.character.items.Inventory;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.trunk.Trunk;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.packet.WvsContext;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.enums.CashItemType;
import net.swordie.ms.connection.packet.CCashShop;
import net.swordie.ms.enums.CashShopActionType;
import net.swordie.ms.enums.CashShopFailReason;
import net.swordie.ms.enums.InvType;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.util.Util;
import net.swordie.ms.world.shop.cashshop.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created on 4/23/2018.
 */
public class CashShopHandler {

    private static final Logger log = LogManager.getLogger(CashShopHandler.class);

    @Handler(op = InHeader.CASH_SHOP_QUERY_CASH_REQUEST)
    public static void handleCashShopQueryCashRequest(Char chr, InPacket inPacket) {
        chr.write(CCashShop.queryCashResult(chr));
    }

    /**
     * Helper function for deducting NX
     * Returns true if enough currency to cover cost and cost has been deducted
     */
    private static boolean handlePayment(Char chr, int paymentMethod, int cost) {
        Account account = chr.getAccount();
        User user = chr.getUser();
        boolean paymentSuccess = false;
        switch (paymentMethod) {
            case 1: // Credit
                if (account.getNxCredit() >= cost) {
                    account.deductNXCredit(cost);
                    paymentSuccess = true;
                }
                break;
            case 2: // Maple points
                if (user.getMaplePoints() >= cost) {
                    user.deductMaplePoints(cost);
                    paymentSuccess = true;
                }
                break;
            case 4: // Prepaid
                if (user.getNxPrepaid() >= cost) {
                    user.deductNXPrepaid(cost);
                    paymentSuccess = true;
                }
                break;
        }
        return paymentSuccess;
    }

    @Handler(op = InHeader.CASH_SHOP_CASH_ITEM_REQUEST)
    public static void handleCashShopCashItemRequest(Char chr, InPacket inPacket) {
        User user = chr.getUser();
        Account account = chr.getAccount();
        Trunk trunk = account.getTrunk();
        byte type = inPacket.decodeByte();
        CashItemType cit = CashItemType.getRequestTypeByVal(type);
        CashShop cs = Server.getInstance().getCashShop();
        if (cit == null) {
            log.error("Unhandled cash shop cash item request " + type);
            chr.write(CCashShop.error());
            return;
        }
        switch (cit) {
            case Req_Buy: {
                inPacket.decodeByte();
                byte paymentMethod = inPacket.decodeByte();
                inPacket.decodeInt();
                int sn = inPacket.decodeInt();
                int cost = inPacket.decodeInt();
                CashShopItem csi = cs.getItem(sn);
                if (csi == null || csi.getNewPrice() != cost) {
                    chr.write(CCashShop.error());
                    return;
                }
                if (account.getTrunk().isFull()) {
                    chr.write(CCashShop.error(CashShopFailReason.TooManyCashItems));
                    return;
                }
                if (!handlePayment(chr, paymentMethod, cost)) {
                    chr.write(CCashShop.error(CashShopFailReason.NotEnoughCash));
                    return;
                }
                // handle buy
                CashItemInfo cii = csi.toCashItemInfo(account);
                DatabaseManager.saveToDB(cii); // ensures the item has a unique ID
                account.getTrunk().addCashItem(cii);
                // write result
                chr.write(CCashShop.cashItemResBuyDone(cii, null, null));
                chr.write(CCashShop.queryCashResult(chr));
                break;
            }
            case Req_Gift: {
                String picInput = inPacket.decodeString();
                if (!BCrypt.checkpw(picInput, chr.getUser().getPic())) {
                    chr.write(CCashShop.error(CashShopFailReason.CheckPICPassword));
                    return;
                }
                inPacket.decodeByte();
                int sn = inPacket.decodeInt();
                int cost = inPacket.decodeInt();
                String receiverName = inPacket.decodeString();
                String giftMessage = inPacket.decodeString();
                int receiverId = chr.getWorld().lookupCharIdByName(receiverName);
                if (receiverId < 0) {
                    chr.write(CCashShop.error(CashShopFailReason.CannotFindCharacter));
                    return;
                }
                Account receiverAccount = chr.getWorld().lookupAccountByCharId(receiverId);
                if (receiverAccount == null) {
                    chr.write(CCashShop.error());
                    return;
                }
                if (receiverAccount.getId() == account.getId()) {
                    chr.write(CCashShop.error(CashShopFailReason.CannotGiftOwnAccount));
                    return;
                }
                if (receiverAccount.getTrunk().isFull()) {
                    chr.write(CCashShop.error(CashShopFailReason.RecipientInventoryFull));
                    return;
                }
                CashShopItem csi = cs.getItem(sn);
                if (csi == null || csi.getNewPrice() != cost) {
                    chr.write(CCashShop.error());
                    return;
                }
                if (!handlePayment(chr, 4, cost)) { // paymentMethod always prepaid for gifts
                    chr.write(CCashShop.error(CashShopFailReason.NotEnoughCash));
                    return;
                }
                // handle gift, save user to DB here since there is a transaction
                CashItemInfo giftCii = csi.toCashItemInfo(receiverAccount);
                giftCii.setBuyCharacterID(chr.getName());
                DatabaseManager.saveToDB(giftCii); // ensures the item has a unique ID
                receiverAccount.getTrunk().addCashItem(giftCii);
                DatabaseManager.saveToDB(receiverAccount);
                CashShopGift csg = new CashShopGift(giftCii, receiverId, chr.getName(), giftMessage);
                DatabaseManager.saveToDB(csg);
                DatabaseManager.saveToDB(user);
                // notify client
                chr.write(CCashShop.cashItemResGiftDone(receiverName, csi.getItemID(), csi.getBundleQuantity(), cost));
                chr.write(CCashShop.queryCashResult(chr));
                break;
            }
            case Req_Couple: {
                String picInput = inPacket.decodeString();
                if (!BCrypt.checkpw(picInput, chr.getUser().getPic())) {
                    chr.write(CCashShop.error(CashShopFailReason.CheckPICPassword));
                    return;
                }
                byte paymentMethod = inPacket.decodeByte();
                inPacket.decodeInt();
                int sn = inPacket.decodeInt();
                int cost = inPacket.decodeInt();
                String receiverName = inPacket.decodeString();
                String giftMessage = inPacket.decodeString();
                int receiverId = chr.getWorld().lookupCharIdByName(receiverName);
                if (receiverId < 0) {
                    chr.write(CCashShop.error(CashShopFailReason.CannotFindCharacter));
                    return;
                }
                Account receiverAccount = chr.getWorld().lookupAccountByCharId(receiverId);
                if (receiverAccount == null) {
                    chr.write(CCashShop.error());
                    return;
                }
                if (receiverAccount.getId() == account.getId()) {
                    chr.write(CCashShop.error(CashShopFailReason.CannotGiftOwnAccount));
                    return;
                }
                if (receiverAccount.getTrunk().isFull()) {
                    chr.write(CCashShop.error(CashShopFailReason.TooManyCashItems));
                    return;
                }
                CashShopItem csi = cs.getItem(sn);
                if (csi == null || csi.getNewPrice() != cost) {
                    chr.write(CCashShop.error());
                    return;
                }
                if (account.getTrunk().isFull()) {
                    chr.write(CCashShop.error(CashShopFailReason.TooManyCashItems));
                    return;
                }
                if (!handlePayment(chr, paymentMethod, cost)) {
                    chr.write(CCashShop.error(CashShopFailReason.NotEnoughCash));
                    return;
                }
                // handle gift
                CashItemInfo giftCii = csi.toCashItemInfo(receiverAccount);
                giftCii.setBuyCharacterID(chr.getName());
                DatabaseManager.saveToDB(giftCii); // ensures the item has a unique ID
                receiverAccount.getTrunk().addCashItem(giftCii);
                DatabaseManager.saveToDB(receiverAccount);
                CashShopGift csg = new CashShopGift(giftCii, receiverId, chr.getName(), giftMessage);
                DatabaseManager.saveToDB(csg);
                // handle buy, save user to DB here since there is a transaction
                CashItemInfo cii = csi.toCashItemInfo(account);
                DatabaseManager.saveToDB(cii); // ensures the item has a unique ID
                account.getTrunk().addCashItem(cii);
                DatabaseManager.saveToDB(user);
                // notify client
                chr.write(CCashShop.cashItemResCoupleDone(cii, receiverName));
                chr.write(CCashShop.queryCashResult(chr));
                break;
            }
            case Req_IncSlotCount: {
                inPacket.decodeByte();
                byte paymentMethod = inPacket.decodeByte();
                inPacket.decodeInt();
                boolean isAdd4Slots = inPacket.decodeByte() == 0;
                InvType invType = null;
                int incSlots;
                int cost;
                if (isAdd4Slots) {
                    incSlots = 4;
                    cost = 2500;
                    invType = InvType.getInvTypeByVal(inPacket.decodeByte());
                } else {
                    int sn = inPacket.decodeInt();
                    incSlots = 8;
                    cost = inPacket.decodeInt();
                    inPacket.decodeLong();
                    CashShopItem csi = cs.getItem(sn);
                    if (csi == null || csi.getNewPrice() != cost) {
                        chr.write(CCashShop.error());
                        log.error("Requested item's cost did not match client's cost");
                        return;
                    }
                    switch (csi.getItemID()) {
                        case 9111000 -> invType = InvType.EQUIP;
                        case 9112000 -> invType = InvType.CONSUME;
                        case 9113000 -> invType = InvType.INSTALL;
                        case 9114000 -> invType = InvType.ETC;
                    }
                }
                if (invType == null) {
                    chr.write(CCashShop.error());
                    return;
                }
                if (!handlePayment(chr, paymentMethod, cost)) {
                    chr.write(CCashShop.error(CashShopFailReason.NotEnoughCash));
                    return;
                }
                chr.getInventoryByType(invType).addSlots(incSlots);
                chr.write(CCashShop.cashItemResIncSlotCountDone(invType.getVal(), chr.getInventoryByType(invType).getSlots()));
                chr.write(CCashShop.queryCashResult(chr));
                break;
            }
            case Req_IncTrunkCount: {
                inPacket.decodeByte();
                byte paymentMethod = inPacket.decodeByte();
                inPacket.decodeInt();
                boolean isAdd4Slots = inPacket.decodeByte() == 0;
                int incSlots;
                int cost;
                if (isAdd4Slots) {
                    incSlots = 4;
                    cost = 2500;
                } else {
                    int sn = inPacket.decodeInt();
                    incSlots = 8;
                    cost = inPacket.decodeInt();
                    inPacket.decodeLong();
                    CashShopItem csi = cs.getItem(sn);
                    if (csi == null || csi.getNewPrice() != cost) {
                        chr.write(CCashShop.error());
                        log.error("Requested item's cost did not match client's cost");
                        return;
                    }
                    if (csi.getItemID() != 9110000) {
                        chr.write(CCashShop.error());
                        return;
                    }
                }
                if (!handlePayment(chr, paymentMethod, cost)) {
                    chr.write(CCashShop.error(CashShopFailReason.NotEnoughCash));
                    return;
                }
                chr.getAccount().getTrunk().addSlots(incSlots);
                chr.write(CCashShop.cashItemResIncTrunkCountDone(chr.getAccount().getTrunk().getSlotCount()));
                chr.write(CCashShop.queryCashResult(chr));
                break;
            }
            case Req_MoveLtoS: {
                long itemSn = inPacket.decodeLong();
                CashItemInfo cii = trunk.getLockerItemBySn(itemSn);
                if (cii == null) {
                    chr.write(CCashShop.fullInventoryMsg());
                    return;
                }
                Item item = cii.getItem();
                Inventory inventory;
                if (ItemConstants.isEquip(item.getItemId())) {
                    inventory = chr.getEquipInventory();
                } else {
                    inventory = chr.getCashInventory();
                }
                if (!inventory.canPickUp(item)) {
                    chr.write(CCashShop.fullInventoryMsg());
                    return;
                }
                trunk.getLocker().remove(cii);
                cii.setItem(null);
                chr.addItemToInventory(item);
                chr.write(CCashShop.resMoveLtoSDone(item));
                break;
            }
            case Req_MoveStoL: {
                long itemSn = inPacket.decodeLong();
                Inventory inv = chr.getInventoryByType(InvType.getInvTypeByVal(inPacket.decodeByte()));
                Item item = inv == null ? null : inv.getItemBySN(itemSn);
                if (item == null) {
                    chr.write(CCashShop.error());
                    chr.dispose();
                    return;
                }
                if (trunk.isFull()) {
                    chr.write(CCashShop.fullInventoryMsg());
                    return;
                }
                int quant = item.getQuantity();
                CashItemInfo cii = CashItemInfo.fromItem(chr, item);
                chr.write(CCashShop.resMoveStoLDone(cii));
                chr.consumeItem(item);
                item.setQuantity(quant);
                DatabaseManager.saveToDB(cii);
                trunk.addCashItem(cii);
                chr.write(CCashShop.queryCashResult(chr));
                break;
            }
            default:
                chr.write(CCashShop.error());
                log.error("Unhandled cash shop cash item request " + cit);
                chr.dispose();
                break;
        }
    }

    @Handler(op = InHeader.CASH_SHOP_ACTION)
    public static void handleCashShopAction(Char chr, InPacket inPacket) {
        CashShop cashShop = Server.getInstance().getCashShop();
        byte type = inPacket.decodeByte();
        CashShopActionType csat = CashShopActionType.getByVal(type);
        if (csat == null) {
            log.error("Unhandled cash shop cash action request " + type);
            chr.write(CCashShop.error());
            return;
        }
        switch (csat) {
            case Req_ShowCategory: {
                int categoryIdx = inPacket.decodeInt();
                if (categoryIdx != 4000000) { // Home
                    chr.write(CCashShop.openCategoryResult(cashShop, chr, categoryIdx));
                }
                break;
            }
            case Req_Favorite: {
                inPacket.decodeByte(); // 0
                int sn = inPacket.decodeInt();
                CashShopItem item = cashShop.getItem(sn);
                if (item == null) {
                    chr.write(CCashShop.error());
                    chr.dispose();
                    return;
                }
                CashShopFavorite favorite = new CashShopFavorite(chr.getAccount().getId(), sn);
                cashShop.addFavorite(favorite, true);
                chr.write(CCashShop.favorite(true, sn));
                break;
            }
            case Req_UnFavorite: {
                inPacket.decodeByte(); // 1
                int sn = inPacket.decodeInt();
                cashShop.removeFavorite(chr.getAccount().getId(), sn);
                chr.write(CCashShop.favorite(false, sn));
                break;
            }
            case Req_Like:
            case Req_UnLike:
                // TODO
                break;
            case Req_ShowFavorites:
                chr.write(CCashShop.showFavorites(chr, cashShop));
                break;
            case Req_Leave:
                break;
            case Req_ShowSearchResult:
                if (ServerConstants.CASH_SHOP_SEARCH_STRING_HOOK) {
                    String query = inPacket.decodeString().toLowerCase().replaceAll(" ", "");
                    List<CashShopItem> result = new ArrayList<>();
                    for (String name : cashShop.getSearchInfo().keySet()) {
                        if (name.contains(query)) {
                            result.add(cashShop.getSearchInfo().get(name));
                        }
                    }
                    chr.write(CCashShop.listItems(cashShop, chr, CashShopActionType.ShowSearchResult, result));
                    break;
                }
                // Fallthrough intended
            default:
                chr.write(CCashShop.error());
                log.error("Unhandled cash shop cash action request " + csat);
                chr.dispose();
                break;
        }
    }

    @Handler(op = InHeader.SURPRISE_BOX)
    public static void handleSurprise(Char chr, InPacket inPacket) {
        Account account = chr.getAccount();
        long sn = inPacket.decodeLong();
        Trunk trunk = account.getTrunk();
        if (trunk.isFull()) {
            chr.write(CCashShop.fullInventoryMsg());
            return;
        }
        CashItemInfo box = account.getTrunk().getLockerItemBySn(sn);
        if (box == null) {
            chr.write(CCashShop.error());
            chr.dispose();
            return;
        }
        CashShop cashShop = Server.getInstance().getCashShop();
        List<Integer> pool = cashShop.getSurpiseBoxInfo().get(box.getItem().getItemId());
        if (pool == null) {
            chr.write(CCashShop.error());
            chr.dispose();
            return;
        }
        Equip equip = ItemData.getEquipDeepCopyFromID(Util.getRandomFromCollection(pool), false);
        CashItemInfo cii = CashItemInfo.fromItem(chr, equip);
        if (cii == null) {
            chr.write(CCashShop.error());
            chr.dispose();
            return;
        }
        DatabaseManager.saveToDB(cii); // ensures the item has a unique ID
        trunk.addCashItem(cii);
        chr.write(CCashShop.queryCashResult(chr));
        // consume box
        box.getItem().setQuantity(box.getItem().getQuantity() - 1);
        if (box.getItem().getQuantity() <= 0) {
            trunk.getLocker().remove(box);
        }
        chr.write(CCashShop.surpriseBox(cii, box, false));
        chr.write(CCashShop.loadLockerDone(chr.getAccount()));
    }
}
