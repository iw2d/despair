package net.swordie.ms.world.auction;

import net.swordie.ms.util.Util;

/**
 * @author Sjonnie
 * Created on 9/27/2018.
 */
public enum AuctionType {
    // Refer to Etc.wz/AuctionData.img
    Enter(0),
    ListItem(1),
    CancelListing(2),
    PurchaseSingle(3),
    Bid(4),
    AveragePriceAutoBuy(5),
    Complete(6),
    SearchItemList(7),
    MyItemList(8),
    MyHistory(9),
    AveragePrice(10),
    Haggle(11),
    PurchaseMultiple(12),
    Unk13(13),
    Exit(14),
    ;

    private int val;

    AuctionType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static AuctionType getType(int val) {
        return Util.findWithPred(values(), type -> type.getVal() == val);
    }
}
