package net.swordie.ms.world.shop.cashshop;

import net.swordie.ms.connection.Encodable;
import net.swordie.ms.connection.OutPacket;

public class Commodity implements Encodable {
    private CommodityMask mask;
    private int itemId;
    private int price;
    private int originalPrice;

    public Commodity(int itemId, int price, int originalPrice) {
        this.mask = CommodityMask.All;
        this.itemId = itemId;
        this.price = price;
        this.originalPrice = originalPrice;
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodeLong(mask.get());
        if (mask.isInMask(CommodityMask.ItemId)) {
            outPacket.encodeInt(itemId); // nItemId
        }
        if (mask.isInMask(CommodityMask.Count)) {
            outPacket.encodeShort(1); // nCount
        }
        if (mask.isInMask(CommodityMask.Priority)) {
            outPacket.encodeByte(0); // nPriority
        }
        if (mask.isInMask(CommodityMask.Price)) {
            outPacket.encodeInt(price); // nPrice
        }
        if (mask.isInMask(CommodityMask.OriginalPrice)) {
            outPacket.encodeInt(originalPrice); // nOriginalPrice
        }
        if (mask.isInMask(CommodityMask.Token)) {
            outPacket.encodeInt(0); // nToken
        }
        if (mask.isInMask(CommodityMask.Bonus)) {
            outPacket.encodeByte(0); // bBonus
        }
        if (mask.isInMask(CommodityMask.Zero)) {
            outPacket.encodeByte(0); // bZero
        }
        if (mask.isInMask(CommodityMask.Period)) {
            outPacket.encodeShort(0);
        }
        if (mask.isInMask(CommodityMask.ReqPOP)) {
            outPacket.encodeShort(0);
        }
        if (mask.isInMask(CommodityMask.ReqLEV)) {
            outPacket.encodeShort(0);
        }
        if (mask.isInMask(CommodityMask.MaplePoint)) {
            outPacket.encodeInt(0); // nMaplePoint
        }
        if (mask.isInMask(CommodityMask.Meso)) {
            outPacket.encodeInt(0); // nMeso
        }
        if (mask.isInMask(CommodityMask.ForPremiumUser)) {
            outPacket.encodeByte(0); // bForPremiumUser
        }
        if (mask.isInMask(CommodityMask.CommodityGender)) {
            outPacket.encodeByte(2); // nCommodityGender
        }
        if (mask.isInMask(CommodityMask.OnSale)) {
            outPacket.encodeByte(true); // bOnSale
        }
        if (mask.isInMask(CommodityMask.Class)) {
            outPacket.encodeByte(0); // nClass
        }
        if (mask.isInMask(CommodityMask.Limit)) {
            outPacket.encodeByte(0); // nLimit
        }
        if (mask.isInMask(CommodityMask.PbCash)) {
            outPacket.encodeShort(0); // pPbCash
        }
        if (mask.isInMask(CommodityMask.PbPoint)) {
            outPacket.encodeShort(0); // nPbPoint
        }
        if (mask.isInMask(CommodityMask.PbGift)) {
            outPacket.encodeShort(0); // nPbGift
        }
        if (mask.isInMask(CommodityMask.PackageSN)) {
            // aPackageSN
            int size = 0;
            outPacket.encodeByte(size);
            for (int i = 0; i < size; i++) {
                outPacket.encodeInt(0);
            }
        }
        if (mask.isInMask(CommodityMask.TermStart)) {
            outPacket.encodeInt(0); // TermStart
        }
        if (mask.isInMask(CommodityMask.TermEnd)) {
            outPacket.encodeInt(0); // TermEnd
        }
        if (mask.isInMask(CommodityMask.Refundable)) {
            outPacket.encodeByte(0); // bRefundable
        }
        if (mask.isInMask(CommodityMask.BombSale)) {
            outPacket.encodeByte(0); // bBombSale
        }
        if (mask.isInMask(CommodityMask.ForcedCategory)) {
            outPacket.encodeByte(0); // nForcedCategory
            outPacket.encodeByte(0); // nForcedSubCategory
        }
        if (mask.isInMask(CommodityMask.WorldLimit)) {
            outPacket.encodeByte(0); // nWorldLimit
        }
        if (mask.isInMask(CommodityMask.LimitMax)) {
            outPacket.encodeByte(0); // nLimitMax
        }
        if (mask.isInMask(CommodityMask.CheckQuestID)) {
            outPacket.encodeInt(0); // nCheckQuestID
        }
        if (mask.isInMask(CommodityMask.Discount)) {
            outPacket.encodeByte(0); // bDiscount
        }
        if (mask.isInMask(CommodityMask.DiscountRate)) {
            outPacket.encodeLong(0); // dDiscountRate (double)
        }
        if (mask.isInMask(CommodityMask.Mileage)) {
            outPacket.encodeByte(0); // nMileageRate
            outPacket.encodeByte(0); // bOnlyMileage
        }
        if (mask.isInMask(CommodityMask.CheckQuestID2)) {
            outPacket.encodeInt(0); // nCheckQuestID
        }
        if (mask.isInMask(CommodityMask.Unk400000000)) {
            outPacket.encodeInt(0);
            outPacket.encodeInt(0);
        }
        if (mask.isInMask(CommodityMask.Unk800000000)) {
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(CommodityMask.Unk1000000000)) {
            outPacket.encodeByte(0);
        }
    }


    public enum CommodityMask {
        ItemId          (0x1),
        Count           (0x2),
        Price           (0x04),
        Bonus           (0x08),
        Priority        (0x10),
        Period          (0x20),
        MaplePoint      (0x40),
        Meso            (0x80),
        ForPremiumUser  (0x100),
        CommodityGender (0x200),
        OnSale          (0x400),
        Class           (0x800),
        Limit           (0x1000),
        PbCash          (0x2000),
        PbPoint         (0x4000),
        PbGift          (0x8000),
        PackageSN       (0x10000),
        ReqPOP          (0x20000),
        ReqLEV          (0x40000),
        TermStart       (0x80000),
        TermEnd         (0x100000),
        Refundable      (0x200000),
        BombSale        (0x400000),
        ForcedCategory  (0x800000),
        WorldLimit      (0x1000000),
        Token           (0x2000000),
        LimitMax        (0x4000000),
        CheckQuestID    (0x8000000L),
        OriginalPrice   (0x10000000),
        Discount        (0x20000000),
        DiscountRate    (0x40000000),
        Mileage         (0x80000000L),
        Zero            (0x100000000L),
        CheckQuestID2   (0x200000000L),
        Unk400000000    (0x400000000L),
        Unk800000000    (0x800000000L),
        Unk1000000000   (0x1000000000L),

        All             (0xFFFFFFFFFFFFFFFFL),
        ;

        public final long uFlag;

        CommodityMask(long uFlag) {
            this.uFlag = uFlag;
        }

        public long get() {
            return uFlag;
        }

        public boolean isInMask(long mask){
            return (mask & get()) != 0;
        }

        public boolean isInMask(CommodityMask mask){
            return (mask.get() & get()) != 0;
        }
    }
}
