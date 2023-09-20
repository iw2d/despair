package net.swordie.ms.world.shop.cashshop;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created on 4/21/2018.
 */
public class CashShop {
    private static final Logger log = LogManager.getLogger(CashShop.class);
    private final List<CashShopCategory> categories = new ArrayList<>();
    private final Map<CashShopCategory, List<CashShopItem>> items = new TreeMap<>(Comparator.comparingInt(CashShopCategory::getIdx));
    private final Map<String, CashShopItem> searchInfo = new HashMap<>();
    private final Map<CashShopItem, Tuple<List<Integer>, List<Integer>>> beautyPreview = new HashMap<>();
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

    public Map<String, CashShopItem> getSearchInfo() {
        return searchInfo;
    }

    public Map<CashShopItem, Tuple<List<Integer>, List<Integer>>> getBeautyPreview() {
        return beautyPreview;
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

    public void encodePreviewInfo(OutPacket outPacket) {
        // byte * (byte -> int, byte, short * (int, int))
        outPacket.encodeByte(0);

        // byte * (byte -> int, short * (byte -> short * (int, int)))
        outPacket.encodeByte(0);

        // BEAUTY_DATA::Decode
        outPacket.encodeInt(getBeautyPreview().size());
        for (CashShopItem csi : getBeautyPreview().keySet()) {
            outPacket.encodeInt(csi.getItemID());
            List<Integer> maleStyles = getBeautyPreview().get(csi).getLeft();
            outPacket.encodeInt(maleStyles.size());
            maleStyles.forEach(outPacket::encodeInt);
            List<Integer> femaleStyles = getBeautyPreview().get(csi).getRight();
            outPacket.encodeInt(femaleStyles.size());
            femaleStyles.forEach(outPacket::encodeInt);
        }
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
        searchInfo.clear();
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
            searchInfo.put(name, csi);
            // extract preview info
            if (csi.getItemID() / 10000 == 515 && csi.getRandom() != null && csi.getRandom().size() > 0) {
                beautyPreview.put(csi, new Tuple<>(
                        csi.getRandom().stream().filter(r -> r.getGender() == 0 || r.getGender() == 2).map(CashShopRandom::getReward).toList(),
                        csi.getRandom().stream().filter(r -> r.getGender() == 1 || r.getGender() == 2).map(CashShopRandom::getReward).toList()
                ));
            }
        }
        for (CashShopCategory csc : items.keySet()) {
            if (items.get(csc).size() >= GameConstants.MAX_CS_ITEMS_PER_CATEGORY) {
                log.warn(String.format("Cash Shop item count for category %s exceeds the maximum %d / %d.", csc.getName(), items.get(csc).size(), GameConstants.MAX_CS_ITEMS_PER_CATEGORY));
            }
        }
    }
}
