package net.swordie.ms.handlers;

import net.swordie.ms.client.Account;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.enums.AuctionState;
import net.swordie.ms.enums.InvType;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.util.FileTime;
import net.swordie.ms.world.World;
import net.swordie.ms.world.auction.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class AuctionHandler {
    private static final Logger log = LogManager.getLogger(AuctionHandler.class);



    @Handler(op = InHeader.AUCTION_REQUEST)
    public static void handleAuctionRequest(Char chr, InPacket inPacket) {
        int type = inPacket.decodeByte();
        AuctionType at = AuctionType.getType(type);
        if (at == null) {
            log.error(String.format("Unknown auction request type %d", type));
            return;
        }
        Account acc = chr.getAccount();
        World world = chr.getWorld();
        int auctionId;
        AuctionItem ai;
        switch (at) {
            case Enter:
                chr.write(FieldPacket.auctionResult(AuctionResult.items(AuctionType.MyItemList, acc.getSellingAuctionItems())));
                chr.write(FieldPacket.auctionResult(AuctionResult.items(AuctionType.MyHistory, acc.getCompletedAuctionItems())));
                chr.write(FieldPacket.auctionResult(AuctionResult.searchResult(world.getAuctionRecentListings())));
                break;
            case ListItem:
                if (acc.getSellingAuctionItems().size() >= GameConstants.AUCTION_LIST_SLOTS) {
                    chr.write(FieldPacket.auctionResult(AuctionResult.of(at, 4)));
                    return;
                }
                inPacket.decodeInt(); // auction Type
                int itemId = inPacket.decodeInt();
                int quant = inPacket.decodeInt();
                long unitPrice = inPacket.decodeLong();
                int listHours = inPacket.decodeInt(); // always 24?
                InvType invType = InvType.getInvTypeByVal(inPacket.decodeByte());
                int pos = inPacket.decodeInt();
                inPacket.decodeByte(); // unk
                Item item = chr.getInventoryByType(invType).getItemBySlot(pos);
                if (item == null || item.getItemId() != itemId) {
                    chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.ListItem, 6))); // failure due to unknown error
                    return;
                }
                handleListItem(chr, item, quant, unitPrice);
                break;
            case CancelListing:
                auctionId = inPacket.decodeInt();
                ai = acc.getAuctionById(auctionId);
                handleCancelListing(chr, ai);
                break;
            case PurchaseSingle:
                auctionId = inPacket.decodeInt();
                ai = world.getAuctionById(auctionId);
                handlePurchaseSingle(chr, ai);
                break;
            case PurchaseMultiple:
                auctionId = inPacket.decodeInt();
                long price = inPacket.decodeLong();
                int buyQuant = inPacket.decodeInt();
                ai = world.getAuctionById(auctionId);
                handlePurchaseMultiple(chr, ai, buyQuant);
                break;
            case Collect:
                if (inPacket.getUnreadAmount() != 60) {
                    chr.write(FieldPacket.auctionResult(AuctionResult.of(at, 8)));
                    return;
                }
                inPacket.decodeLong();
                auctionId = inPacket.decodeInt();
                ai = acc.getAuctionById(auctionId);
                handleCollect(chr, ai);
                break;
            case SearchItemList:
                inPacket.decodeByte(); // unk
                String query = "";
                Set<Integer> itemIdList = new HashSet<>();
                boolean stringQuery = inPacket.decodeByte() != 0;
                if (stringQuery) {
                    query = inPacket.decodeString();
                } else {
                    int count = inPacket.decodeInt();
                    for (int i = 0; i < count; i++) {
                        itemIdList.add(inPacket.decodeInt());
                    }
                }
                int type1 = inPacket.decodeInt();
                int type2 = inPacket.decodeInt();
                AuctionEnum subType = AuctionInvType.getByVal(type1).getSubByVal(type2);
                if (inPacket.decodeByte() != 0) {
                    subType = subType.getSubByVal(inPacket.decodeInt());
                }
                boolean advancedSearchAnd = inPacket.decodeByte() != 0;
                boolean priceRange = inPacket.decodeByte() != 0;
                long priceMin = 0;
                long priceMax = Long.MAX_VALUE;
                if (priceRange) {
                    priceMin = inPacket.decodeLong();
                    priceMax = inPacket.decodeLong();
                }
                AuctionPotType apt = AuctionPotType.All;
                if (inPacket.decodeByte() != 0) {
                    apt = AuctionPotType.getByVal(inPacket.decodeByte());
                }
                boolean levelRange = inPacket.decodeByte() != 0;
                int levelMin = 0;
                int levelMax = Integer.MAX_VALUE;
                if (levelRange) {
                    levelMin = inPacket.decodeInt();
                    levelMax = inPacket.decodeInt();
                }
                handleSearchItemList(chr, stringQuery, query, itemIdList, subType, priceMin, priceMax, apt, levelMin, levelMax);
                break;
            case MyItemList:
                chr.write(FieldPacket.auctionResult(AuctionResult.items(AuctionType.MyItemList, acc.getSellingAuctionItems())));
                break;
            case MyHistory:
                chr.write(FieldPacket.auctionResult(AuctionResult.items(AuctionType.MyHistory, acc.getCompletedAuctionItems())));
                break;
            case Exit:
                chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.Exit, 0)));
                break;
            default:
                log.warn(String.format("Unhandled auction type %s", at));
                break;
        }
    }

    public static void handleListItem(Char chr, Item item, int quant, long unitPrice) {
        long cost = GameConstants.AUCTION_DEPOSIT_AMOUNT;
        if (cost > chr.getMoney()) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.ListItem, 3)));
            return;
        }
        if (unitPrice < GameConstants.AUCTION_MIN_PRICE || unitPrice > GameConstants.AUCTION_MAX_PRICE) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.ListItem, 1)));
            return;
        }
        chr.deductMoney(GameConstants.AUCTION_DEPOSIT_AMOUNT);
        chr.consumeItem(item, quant, true);
        item = item.deepCopy();
        item.setQuantity(quant);
        AuctionItem ai = chr.getAccount().createAndAddAuctionByItem(item, chr, unitPrice);
        DatabaseManager.saveToDB(item);
        DatabaseManager.saveToDB(ai);
        chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.ListItem, 0)));
    }

    public static void handleCancelListing(Char chr, AuctionItem ai) {
        if (ai == null || ai.getState() != AuctionState.Init) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.CancelListing, 5)));
            return;
        }
        ai.setEndDate(FileTime.currentTime());
        ai.setState(AuctionState.Expire);
        DatabaseManager.saveToDB(ai);
        chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.CancelListing, 0)));
    }

    public static void handlePurchaseSingle(Char chr, AuctionItem ai) {
        if (ai == null || ai.getState() != AuctionState.Init || ai.getAccountID() == chr.getAccount().getId()) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.PurchaseSingle, 7)));
            return;
        }
        long cost = ai.getDirectPrice();
        if (cost > chr.getMoney()) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.PurchaseSingle, 4)));
            return;
        }
        chr.deductMoney(cost);
        ai.setBidUserID(chr.getId());
        ai.setBidUsername(chr.getName());
        ai.setState(AuctionState.Sold);
        ai.setEndDate(FileTime.currentTime());
        ai.setSoldQuantity(ai.getSoldQuantity() + 1);
        DatabaseManager.saveToDB(ai);
        // create entry for buyer
        AuctionItem boughtItem = chr.getAccount().createAndAddAuctionByItem(ai.getItem(), chr, ai.getPrice());
        boughtItem.setEndDate(FileTime.currentTime());
        boughtItem.setState(AuctionState.Reserve);
        chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.PurchaseSingle, 0)));
    }

    public static void handlePurchaseMultiple(Char chr, AuctionItem ai, int quant) {
        if (ai == null || ai.getQuantity() <= 0 || ai.getState() != AuctionState.Init
                || ai.getAccountID() == chr.getAccount().getId()) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.PurchaseMultiple, 8)));
            return;
        }
        int buyQuant = Math.min(quant, ai.getQuantity());
        long cost = ai.getPrice() * buyQuant;
        if (cost > chr.getMoney()) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.PurchaseMultiple, 4)));
            return;
        }
        chr.deductMoney(cost);
        if (ai.getQuantity() == buyQuant) {
            ai.setBidUserID(chr.getId());
            ai.setBidUsername(chr.getName());
            ai.setSoldQuantity(buyQuant);
            ai.setState(AuctionState.Sold);
            ai.setEndDate(FileTime.currentTime());
            DatabaseManager.saveToDB(ai);
            // create entry for buyer
            AuctionItem boughtItem = chr.getAccount().createAndAddAuctionByItem(ai.getItem(), chr, ai.getPrice());
            boughtItem.setEndDate(FileTime.currentTime());
            boughtItem.setState(AuctionState.Reserve);
            DatabaseManager.saveToDB(boughtItem);
        } else {
            ai.getItem().setQuantity(ai.getQuantity() - buyQuant);
            DatabaseManager.saveToDB(ai);
            // create entry for buyer
            Item splitItem = ai.getItem().deepCopy();
            splitItem.setQuantity(buyQuant);
            AuctionItem boughtItem = chr.getAccount().createAndAddAuctionByItem(splitItem, chr, ai.getPrice());
            boughtItem.setEndDate(FileTime.currentTime());
            boughtItem.setState(AuctionState.Reserve);
            DatabaseManager.saveToDB(boughtItem);
            // create entry for seller
            AuctionItem sellItem = ai.deepCopy();
            sellItem.setItem(splitItem);
            sellItem.setSoldQuantity(buyQuant);
            sellItem.setEndDate(FileTime.currentTime());
            sellItem.setState(AuctionState.Sold);
            chr.getWorld().addAuction(sellItem, true);
        }
        chr.write(FieldPacket.auctionResult(AuctionResult.purchaseMultiple(ai)));
    }

    public static void handleCollect(Char chr, AuctionItem ai) {
        if (ai == null || (ai.getState() != AuctionState.Sold && ai.getState() != AuctionState.Reserve && ai.getState() != AuctionState.Expire)) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.Collect, 8)));
            return;
        }
        if (ai.getState() == AuctionState.Sold) {
            long collectMoney = (long) ((ai.getPrice() * ai.getSoldQuantity()) * GameConstants.AUCTION_TAX);
            if (!chr.canAddMoney(collectMoney)) {
                chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.Collect, 5)));
                return;
            }
            ai.setState(AuctionState.SoldDone);
            DatabaseManager.saveToDB(ai);
            chr.addMoney(collectMoney);
        } else {
            Item item = ai.getItem();
            InvType invType = item.getInvType();
            if (chr.getInventoryByType(invType).isFull()) {
                chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.Collect, 3)));
                return;
            }
            ai.setState(ai.getState() == AuctionState.Expire ? AuctionState.Done : AuctionState.BidSuccessDone);
            chr.addItemToInventory(item);
            DatabaseManager.saveToDB(ai);
        }
        chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.Collect, 0)));
    }

    public static void handleSearchItemList(Char chr, boolean stringQuery, String query, Set<Integer> itemIdList, AuctionEnum subType,
                                            long priceMin, long priceMax, AuctionPotType apt, int levelMin, int levelMax) {
        // ignoring advanced search options
        Set<AuctionItem> searchResult = chr.getWorld().getAuctionItemsWithFilter(stringQuery, query, itemIdList, subType, priceMin, priceMax, apt, levelMin, levelMax);
        if (searchResult.size() == 0) {
            chr.write(FieldPacket.auctionResult(AuctionResult.of(AuctionType.SearchItemList, 2)));
        } else {
            chr.write(FieldPacket.auctionResult(AuctionResult.searchResult(searchResult)));
        }
    }
}