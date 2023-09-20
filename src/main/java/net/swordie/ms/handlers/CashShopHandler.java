package net.swordie.ms.handlers;

import net.swordie.ms.Server;
import net.swordie.ms.ServerConstants;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.User;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.Inventory;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.trunk.Trunk;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.enums.CashItemType;
import net.swordie.ms.connection.packet.CCashShop;
import net.swordie.ms.enums.CashShopActionType;
import net.swordie.ms.enums.InvType;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.world.shop.cashshop.CashItemInfo;
import net.swordie.ms.world.shop.cashshop.CashShop;
import net.swordie.ms.world.shop.cashshop.CashShopItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 4/23/2018.
 */
public class CashShopHandler {

    private static final Logger log = LogManager.getLogger(CashShopHandler.class);

    @Handler(op = InHeader.CASH_SHOP_QUERY_CASH_REQUEST)
    public static void handleCashShopQueryCashRequest(Char chr, InPacket inPacket) {
        chr.write(CCashShop.queryCashResult(chr));
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
                int itemPos = inPacket.decodeInt();
                int cost = inPacket.decodeInt();
                CashShopItem csi = cs.getItemByPosition(itemPos - 1); // client's pos starts at 1
                if (csi == null || csi.getNewPrice() != cost) {
                    chr.write(CCashShop.error());
                    log.error("Requested item's cost did not match client's cost");
                    return;
                }
                boolean notEnoughMoney = false;
                switch (paymentMethod) {
                    case 1: // Credit
                        if (account.getNxCredit() >= cost) {
                            account.deductNXCredit(cost);
                        } else {
                            notEnoughMoney = true;
                        }
                        break;
                    case 2: // Maple points
                        if (user.getMaplePoints() >= cost) {
                            user.deductMaplePoints(cost);
                        } else {
                            notEnoughMoney = true;
                        }
                        break;
                    case 4: // Prepaid
                        if (user.getNxPrepaid() >= cost) {
                            user.deductNXPrepaid(cost);
                        } else {
                            notEnoughMoney = true;
                        }
                        break;
                }
                if (notEnoughMoney) {
                    chr.write(CCashShop.error());
                    log.error("Character does not have enough to pay for this item (Paying with " + paymentMethod + ")");
                    return;
                }
                CashItemInfo cii = csi.toCashItemInfo(account, chr);
                DatabaseManager.saveToDB(cii); // ensures the item has a unique ID
                account.getTrunk().addCashItem(cii);
                chr.write(CCashShop.cashItemResBuyDone(cii, null, null));
                chr.write(CCashShop.queryCashResult(chr));
                break;
            }
            case Req_IncSlotCount: {
                inPacket.decodeByte();
                inPacket.decodeByte();
                inPacket.decodeInt();
                boolean isAdd4Slots = inPacket.decodeByte() == 0;
                InvType invType = null;
                byte incSlots;
                int cost;
                if (isAdd4Slots) {
                    incSlots = 4;
                    cost = 2500;
                    invType = InvType.getInvTypeByVal(inPacket.decodeByte());
                } else {
                    int itemPos = inPacket.decodeInt();
                    incSlots = 8;
                    cost = inPacket.decodeInt();
                    inPacket.decodeLong();
                    CashShopItem csi = cs.getItemByPosition(itemPos - 1);
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
                    chr.dispose();
                    return;
                }
                if (account.getNxCredit() >= cost) {
                    account.deductNXCredit(cost);
                    chr.getInventoryByType(invType).addSlots(incSlots);
                    chr.write(CCashShop.cashItemResIncSlotCountDone(invType.getVal(), chr.getInventoryByType(invType).getSlots()));
                    chr.write(CCashShop.queryCashResult(chr));
                }
                break;
            }
            case Req_IncTrunkCount: {
                inPacket.decodeByte();
                inPacket.decodeInt();
                inPacket.decodeByte();
                boolean isAdd4Slots = inPacket.decodeByte() == 0;
                byte incSlots;
                int cost;
                if (isAdd4Slots) {
                    incSlots = 4;
                    cost = 2500;
                } else {
                    int itemPos = inPacket.decodeInt();
                    incSlots = 8;
                    cost = inPacket.decodeInt();
                    inPacket.decodeLong();
                    CashShopItem csi = cs.getItemByPosition(itemPos - 1);
                    if (csi == null || csi.getNewPrice() != cost) {
                        chr.write(CCashShop.error());
                        log.error("Requested item's cost did not match client's cost");
                        return;
                    }
                    if (csi.getItemID() != 9110000) {
                        chr.write(CCashShop.error());
                        chr.dispose();
                        return;
                    }
                }
                if (account.getNxCredit() >= cost) {
                    account.deductNXCredit(cost);
                    chr.getAccount().getTrunk().addSlots(incSlots);
                    chr.write(CCashShop.cashItemResIncTrunkCountDone(chr.getAccount().getTrunk().getSlotCount()));
                    chr.write(CCashShop.queryCashResult(chr));
                }
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
            case Req_ShowCategory:
                int categoryIdx = inPacket.decodeInt();
                if (categoryIdx != 4000000) { // Home
                    chr.write(CCashShop.openCategoryResult(cashShop, categoryIdx));
                }
                break;
            case Req_Favorite:
            case Req_UnFavorite:
                // TODO
                break;
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
                    chr.write(CCashShop.listItems(CashShopActionType.ShowSearchResult, result));
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
}
