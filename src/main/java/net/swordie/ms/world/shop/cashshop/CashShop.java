package net.swordie.ms.world.shop.cashshop;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.handlers.CashShopHandler;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.loaders.containerclasses.ItemInfo;
import net.swordie.ms.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.*;

/**
 * Created on 4/21/2018.
 */
public class CashShop {
    private static final Logger log = LogManager.getLogger(CashShop.class);
    private final List<CashShopCategory> categories = new ArrayList<>();
    private final Map<CashShopCategory, List<CashShopItem>> items = new TreeMap<>(Comparator.comparingInt(CashShopCategory::getIdx));
    private final Map<String, CashShopItem> searchMap = new HashMap<>();
    private final List<Integer> saleItems = new ArrayList<>();;
    private boolean eventOn;
    private boolean lockerTransfer;
    private boolean refundAvailable;
    private boolean usingOTP;
    private boolean usingNewOTP;
    private boolean betaTest;
    private final String BANNER_URL = "";

    public CashShop() {}

    public Map<CashShopCategory, List<CashShopItem>> getItems() {
        return items;
    }

    public Map<String, CashShopItem> getSearchMap() {
        return searchMap;
    }

    public List<Integer> getSaleItems() {
        return saleItems;
    }

    public void encodeSaleInfo(OutPacket outPacket) {
        // unk
        // int -> decodeBuffer(4 * int);
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

        // CCashShop.m_aBest
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
        return Util.findWithPred(getCategories(), csc -> csc.getIdx() == idx);
    }

    public List<CashShopItem> getItemsByCategoryIdx(int categoryIdx) {
        CashShopCategory csc = getCategoryByIdx(categoryIdx);
        return getItems().getOrDefault(csc, List.of());
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

    public void loadItems() {
        // load categories
        categories.clear();
        categories.addAll(CashShopCategory.BASE_CATEGORIES);
        // load items and search map
        items.clear();
        searchMap.clear();
        for (CashShopItem csi : (List<CashShopItem>) DatabaseManager.getObjListFromDB(CashShopItem.class)) {
            CashShopCategory csc = Util.findWithPred(getCategories(), cat -> cat.getParentIdx() != 0 && cat.getName().equalsIgnoreCase(csi.getCategory()));
            if (!items.containsKey(csc)) {
                items.put(csc, new ArrayList<>());
            }
            items.get(csc).add(csi);
            // get name for search map
            String name = StringData.getItemStringById(csi.getItemID());
            if (name == null) {
                continue;
            }
            name = name.toLowerCase().replaceAll(" ", "");
            searchMap.put(name, csi);
        }
        for (CashShopCategory csc : items.keySet()) {
            if (items.get(csc).size() >= GameConstants.MAX_CS_ITEMS_PER_CATEGORY) {
                log.warn(String.format("Cash Shop item count for category %s exceeds the maximum %d / %d.", csc.getName(), items.get(csc).size(), GameConstants.MAX_CS_ITEMS_PER_CATEGORY));
            }
        }
    }
}
