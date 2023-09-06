package net.swordie.ms.world.auction;

import net.swordie.ms.connection.Encodable;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.enums.AuctionState;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sjonnie
 * Created on 11/21/2018.
 */
public class AuctionResult implements Encodable {
    private AuctionType type;
    private byte code = 0;
    private byte byte1;
    private byte byte2;
    private AuctionItem item;
    private Set<AuctionItem> items = new HashSet<>();

    public AuctionResult(AuctionType type) {
        this.type = type;
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(type.getVal());
        switch (type) {
            case Enter:
            case ListItem:
            case CancelListing:
            case PurchaseSingle:
            case Collect:
            case Exit:
                outPacket.encodeByte(code);
                break;
            case SearchItemList:
                outPacket.encodeByte(code);
                outPacket.encodeByte(byte1); // invType
                outPacket.encodeByte(byte2); // subType
                outPacket.encodeInt(items.size());
                for (AuctionItem ai : items) {
                    outPacket.encodeByte(true); // do decode?
                    ai.encode(outPacket);
                }
                break;
            case MyItemList:
                outPacket.encodeInt(items.size());
                for (AuctionItem ai : items) {
                    ai.encode(outPacket);
                }
                break;
            case MyHistory:
                outPacket.encodeInt(items.size());
                for (AuctionItem ai : items) {
                    ai.encodeHistory(outPacket);
                    boolean encodeItem = true;
                    outPacket.encodeByte(encodeItem);
                    if (encodeItem) {
                        ai.encode(outPacket);
                    }
                }
                break;
            case AveragePrice:
                int size = 0;
                outPacket.encodeInt(size);
                for (int i = 0; i < size; i++) {
                    outPacket.encodeInt(0);  // nItemID
                    outPacket.encodeLong(0); // nDailyAvg
                    outPacket.encodeInt(0);  // ignored
                    outPacket.encodeLong(0); // n30MinAvg
                    outPacket.encodeInt(0);  // ignored
                }
                break;
            case PurchaseMultiple:
                outPacket.encodeByte(code);
                if (code == 0) {
                    outPacket.encodeInt(item.getId());
                    boolean encodeItem = item.getState() != AuctionState.Sold;
                    outPacket.encodeByte(encodeItem);
                    if (encodeItem) {
                        item.encode(outPacket);
                    }
                }
                break;
        }
    }

    public static AuctionResult of(AuctionType at, int code) {
        AuctionResult ar = new AuctionResult(at);
        ar.code = (byte) code;
        return ar;
    }

    public static AuctionResult searchResult(Set<AuctionItem> items) {
        AuctionResult ar = new AuctionResult(AuctionType.SearchItemList);
        ar.code = (byte) 0;
        ar.byte1 = (byte) 1;
        ar.items = items;
        return ar;
    }

    public static AuctionResult purchaseMultiple(AuctionItem item) {
        AuctionResult ar = new AuctionResult(AuctionType.SearchItemList);
        ar.code = (byte) 0;
        ar.item = item;
        return ar;
    }

    public static AuctionResult items(AuctionType at, Set<AuctionItem> items) {
        AuctionResult ar = new AuctionResult(at);
        ar.items = items;
        return ar;
    }
}
