package net.swordie.ms.client.character;

import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.client.character.items.ItemAttribute;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.util.container.Tuple;

import java.util.*;

/**
 * @author Sjonnie
 * Created on 8/10/2018.
 */
public class TradeRoom {
    private Map<Char, List<Tuple<Integer, Item>>> offeredItems = new HashMap<>(); // wow
    private Map<Char, Long> money = new HashMap<>();
    private Set<Char> confirmedPlayers = new HashSet<>();
    private Char other;
    private Char chr;
    private boolean cash;

    public TradeRoom(Char chr, Char other) {
        this.chr = chr;
        offeredItems.put(chr, new ArrayList<>());
        this.other = other;
        offeredItems.put(other, new ArrayList<>());
    }

    public boolean isCash() {
        return cash;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public Char getOther() {
        return other;
    }

    public boolean canAddItem(Char chr) {
        return getOfferedItems().get(chr).size() < GameConstants.MAX_TRADE_ITEMS;
    }

    public void addItem(Char chr, int pos, Item item) {
        List<Tuple<Integer, Item>> items = getOfferedItems().get(chr);
        Tuple<Integer, Item> entry = new Tuple<>(pos, item);
        items.add(entry);
    }

    public Char getChr() {
        return chr;
    }

    private Map<Char, Long> getMoney() {
        return money;
    }

    public long getMoney(Char chr) {
        return getMoney().getOrDefault(chr, 0L);
    }

    public void putMoney(Char chr, long money) {
        getMoney().put(chr, money);
    }

    public Map<Char, List<Tuple<Integer, Item>>> getOfferedItems() {
        return offeredItems;
    }

    public void restoreItems() {
        Char[] chars = new Char[]{getChr(), getOther()};
        for (Char chr : chars) {
            for (Tuple<Integer, Item> entry : getOfferedItems().get(chr)) {
                chr.addItemToInventory(entry.getRight());
            }
            chr.addMoney(getMoney(chr));
        }
    }

    public Set<Char> getConfirmedPlayers() {
        return confirmedPlayers;
    }

    public void addConfirmedPlayer(Char chr) {
        getConfirmedPlayers().add(chr);
    }

    public boolean hasConfirmed(Char other) {
        return getConfirmedPlayers().contains(other);
    }

    public boolean completeTrade() {
        Char chr = getChr();
        Char other = getOther();
        // Check if the characters have enough space for all the items
        List<Item> itemsForChr = getOfferedItems().get(other).stream().map(Tuple::getRight).toList();
        if (!chr.canHold(itemsForChr)) {
            chr.chatMessage("You do not have enough inventory space.");
            other.chatMessage(chr.getName() + " does not have enough inventory space.");
            return false;
        }
        List<Item> itemsForOther = getOfferedItems().get(chr).stream().map(Tuple::getRight).toList();
        if (!other.canHold(itemsForOther)) {
            other.chatMessage("You do not have enough inventory space.");
            chr.chatMessage(chr.getName() + " does not have enough inventory space.");
            return false;
        }
        // Add all the items + money
        for (Item item : itemsForChr) {
            ItemAttribute.handleTradeAttribute(item);
            chr.addItemToInventory(item);
        }
        chr.addMoney(GameConstants.tradeTax(getMoney(other)));
        for (Item item : itemsForOther) {
            ItemAttribute.handleTradeAttribute(item);
            other.addItemToInventory(item);
        }
        other.addMoney(GameConstants.tradeTax(getMoney(chr)));
        DatabaseManager.saveToDB(chr);
        DatabaseManager.saveToDB(other);
        return true;
    }

    public void cancelTrade() {
        chr.setTradeRoom(null);
        other.setTradeRoom(null);
        restoreItems();
    }

    public Char getOtherChar(Char chr) {
        return chr == getChr() ? getOther() : getChr();
    }
}
