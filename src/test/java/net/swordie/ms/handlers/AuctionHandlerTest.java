package net.swordie.ms.handlers;

import net.swordie.ms.ServerConfig;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.Inventory;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.enums.AuctionState;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.world.World;
import net.swordie.ms.world.auction.AuctionInvType;
import net.swordie.ms.world.auction.AuctionItem;
import net.swordie.ms.world.auction.AuctionPotType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


class AuctionHandlerTest {

    private int accountIdCounter = 0;
    private int auctionItemIdCounter = 0;

    Char prepareChar(World world) {
        Account account = Mockito.mock(Account.class);
        Mockito.when(account.getId()).thenReturn(accountIdCounter++);
        Mockito.doCallRealMethod().when(account).createAndAddAuctionByItem(any(), any(), anyLong());
        Char chr = Mockito.mock(Char.class);
        Mockito.when(chr.getWorld()).thenReturn(world);
        Mockito.when(chr.getAccount()).thenReturn(account);
        Mockito.when(chr.getMoney()).thenReturn(1_000_000_000L);
        Mockito.when(chr.canAddMoney(anyLong())).thenReturn(true);
        Mockito.when(chr.getInventoryByType(any())).thenReturn(Mockito.mock(Inventory.class));
        return chr;
    }
    @Test
    void testAuctionInteraction() {
        // required for searching items by their names
        StringData.loadItemStrings();

        // mock DatabaseManager to avoid writing to DB
        try (MockedStatic<DatabaseManager> databaseManager = Mockito.mockStatic(DatabaseManager.class)) {
            databaseManager.when(() -> DatabaseManager.saveToDB(any(AuctionItem.class))).then(mock -> {
                ((AuctionItem) mock.getArgument(0)).setId(auctionItemIdCounter++);
                return null;
            });
            databaseManager.when(() -> DatabaseManager.getObjListFromDB(any())).thenReturn(List.of());

            // test world instance
            World world = new World(ServerConfig.WORLD_ID, "test", 0, "");

            // try listing item
            Char seller = prepareChar(world);
            Item item = ItemData.getItemDeepCopy(2000000); // red potion
            item.setQuantity(50);
            AuctionHandler.handleListItem(seller, item, 50, 500_000L);
                // deposit taken
                Mockito.verify(seller).deductMoney(GameConstants.AUCTION_DEPOSIT_AMOUNT);
                // auction item added
                assertEquals(1, world.getAuctionHouse().size());

            // search and buy item
            Char buyer = prepareChar(world);
            Set<AuctionItem> searchResult = world.getAuctionItemsWithFilter(true, "red", Set.of(), AuctionInvType.All, 0, Long.MAX_VALUE, AuctionPotType.All, 0, Integer.MAX_VALUE);
                // search found
                assertEquals(1, searchResult.size());
            AuctionItem auctionItem = searchResult.iterator().next();
            AuctionHandler.handlePurchaseMultiple(buyer, auctionItem, 10);
                // money taken
                Mockito.verify(buyer).deductMoney(500_000L * 10);
                Mockito.verify(buyer.getAccount()).createAndAddAuctionByItem(any(), any(), anyLong());
                // auction item updated
                assertEquals(40, auctionItem.getQuantity());
                assertEquals(3, world.getAuctionHouse().size()); // existing item, bought item, sold item

            // collect bought item
            AuctionItem boughtItem = world.getAuctionHouse().stream()
                    .filter(ai -> ai.getState() == AuctionState.Reserve && ai.getAccountID() == buyer.getAccount().getId())
                    .findAny().orElseThrow();
            AuctionHandler.handleCollect(buyer, boughtItem);
                // item received
                Mockito.verify(buyer).addItemToInventory(boughtItem.getItem());
                // bought item updated
                assertEquals(AuctionState.BidSuccessDone, boughtItem.getState());

            // collect money from sold item
            AuctionItem soldItem = world.getAuctionHouse().stream()
                    .filter(ai -> ai.getState() == AuctionState.Sold && ai.getAccountID() == seller.getAccount().getId())
                    .findAny().orElseThrow();
            AuctionHandler.handleCollect(seller, soldItem);
                // money received
                long expectedAmount = (long) ((soldItem.getPrice() * soldItem.getSoldQuantity()) * GameConstants.AUCTION_TAX);
                Mockito.verify(seller).addMoney(expectedAmount);
                // sold item updated
                assertEquals(AuctionState.SoldDone, soldItem.getState());

            // cancel listing
            AuctionHandler.handleCancelListing(seller, auctionItem);
                // auction item updated
                assertEquals(AuctionState.Expire, auctionItem.getState());
                // no more available auction items
                assertEquals(0, world.getAuctionRecentListings().size());

            // collect expired items
            AuctionHandler.handleCollect(seller, auctionItem);
                // item added back to inventory
                Mockito.verify(seller).addItemToInventory(auctionItem.getItem());
                // expired item updated
                assertEquals(AuctionState.Done, auctionItem.getState());
        }
    }
}