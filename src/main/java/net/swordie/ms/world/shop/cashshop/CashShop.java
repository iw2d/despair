package net.swordie.ms.world.shop.cashshop;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.util.Util;

import java.util.*;

/**
 * Created on 4/21/2018.
 */
public class CashShop {
    private Map<CashShopCategory, List<CashShopItem>> items;
    private List<CashShopCategory> categories;
    private List<Integer> saleItems;
    private boolean eventOn;
    private boolean lockerTransfer;
    private boolean refundAvailable;
    private boolean usingOTP;
    private boolean usingNewOTP;
    private boolean betaTest;
    private final String BANNER_URL = "";

    public CashShop() {
        items = new TreeMap<>(Comparator.comparingInt(CashShopCategory::getIdx));
        saleItems = new ArrayList<>();
    }

    public Map<CashShopCategory, List<CashShopItem>> getItems() {
        return items;
    }

    public List<Integer> getSaleItems() {
        return saleItems;
    }

    public void encodeSaleInfo(OutPacket outPacket) {
        // unk - decodeBuffer(4 * int);
        outPacket.encodeInt(0);

        // ModifiedData
        // short * (int, CS_COMMODITY::DecodeModifiedData)
        outPacket.encodeShort(0);

        // CashRandomItem
        // int * (int, int * (int))
        outPacket.encodeInt(0);
    }

    // CCashShop::CCashShop
    public void encode(OutPacket outPacket) {
        // CCashShop::LoadData
        outPacket.encodeByte(!isBetaTest());

        // CWvsContext::SetSaleInfo
        encodeSaleInfo(outPacket);

        outPacket.encodeArr(new byte[1080]);

        // CS_STOCK
        // short * (int, int) [ nSN, nStockState ]
        outPacket.encodeShort(0);

        // CS_LIMITGOODS
        // short * (116)
        outPacket.encodeShort(0);

        // sub_9E8330
        // short * (68)
        outPacket.encodeShort(0);

        // ~CCashShop::LoadData


        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeInt(0);
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);

        // ignored
        outPacket.encodeByte(0);
        outPacket.encodeByte(0);
        outPacket.encodeLong(0);

        outPacket.encodeByte(0);
        // byte -> (string)
        outPacket.encodeByte(0);

        outPacket.encodeInt(0);
        outPacket.encodeByte(0);
        outPacket.encodeLong(0);
        outPacket.encodeByte(0);
    }

    public boolean isEventOn() {
        return eventOn;
    }

    public void setEventOn(boolean eventOn) {
        this.eventOn = eventOn;
    }

    public boolean isLockerTransfer() {
        return lockerTransfer;
    }

    public void setLockerTransfer(boolean lockerTransfer) {
        this.lockerTransfer = lockerTransfer;
    }

    public boolean isRefundAvailable() {
        return refundAvailable;
    }

    public void setRefundAvailable(boolean refundAvailable) {
        this.refundAvailable = refundAvailable;
    }

    public boolean isUsingOTP() {
        return usingOTP;
    }

    public void setUsingOTP(boolean usingOTP) {
        this.usingOTP = usingOTP;
    }

    public boolean isUsingNewOTP() {
        return usingNewOTP;
    }

    public void setUsingNewOTP(boolean usingNewOTP) {
        this.usingNewOTP = usingNewOTP;
    }

    public boolean isBetaTest() {
        return betaTest;
    }

    public void setBetaTest(boolean betaTest) {
        this.betaTest = betaTest;
    }

    public String getBannerUrl() {
        return BANNER_URL;
    }

    public List<CashShopCategory> getCategories() {
        return categories;
    }

    public CashShopCategory getCategoryByIdx(int idx) {
        return Util.findWithPred(getCategories(), csi -> csi.getIdx() == idx);
    }

    public void setCategories(List<CashShopCategory> categories) {
        this.categories = categories;
    }

    public void addItem(CashShopItem csi) {
        CashShopCategory csc = Util.findWithPred(getCategories(), cat -> cat.getName().equalsIgnoreCase(csi.getCategory()));
        csi.setCashShopCategory(csc);

        if (!getItems().containsKey(csc)) {
            getItems().put(csc, new ArrayList<>());
        }
        int newSize = getItems().size() + 1;
        int page = newSize / GameConstants.MAX_CS_ITEMS_PER_PAGE;
        csi.setSubCategory(4010000 + 10000 * page);
        csi.setParent(1000000 + 70000 + page * 100 + newSize % GameConstants.MAX_CS_ITEMS_PER_PAGE);
        getItems().get(csc).add(csi);
    }

    public List<CashShopItem> getItemsByCategoryIdx(int categoryIdx) {
        CashShopCategory csc = getCategoryByIdx(categoryIdx);
        return getItems().getOrDefault(csc, null);
    }

    public CashShopItem getItemByPosition(int itemPos) {
        for (Map.Entry<CashShopCategory, List<CashShopItem>> entry : getItems().entrySet()) {
            List<CashShopItem> items = entry.getValue();
            if (items.size() <= itemPos) {
                itemPos -= items.size();
            } else {
                return items.get(itemPos);
            }
        }
        return null;
    }
}
