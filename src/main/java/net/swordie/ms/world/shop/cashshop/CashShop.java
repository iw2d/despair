package net.swordie.ms.world.shop.cashshop;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.constants.GameConstants;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created on 4/21/2018.
 */
public class CashShop {
    private static final Logger log = LogManager.getLogger(CashShop.class);
    private final List<CashShopCategory> categories = new ArrayList<>();
    private final Map<Integer, CashShopItem> items = new HashMap<>();
    private final Map<CashShopCategory, List<CashShopItem>> categoryInfo = new TreeMap<>(Comparator.comparingInt(CashShopCategory::getIdx));
    private final Map<String, CashShopItem> searchInfo = new HashMap<>();                                               // itemName -> CashShopItem
    private final Map<Integer, Tuple<List<Integer>, List<Integer>>> beautyPreview = new HashMap<>();                    // beautyItemId -> Tuple<List<maleStyleId>, List<femaleStyleId>>
    private final Map<Integer, List<Integer>> surpriseBoxInfo = new HashMap<>();                                        // boxItemId -> List<rewardItemId>
    private final Map<Integer, Set<CashShopFavorite>> favoriteInfo = new ConcurrentHashMap<>();                         // accountId -> Set<CashShopFavorite>
    private final List<Integer> saleItems = new ArrayList<>();;
    private boolean eventOn;
    private boolean lockerTransfer;
    private boolean refundAvailable;
    private boolean usingOTP;
    private boolean usingNewOTP;
    private boolean betaTest;
    private final String BANNER_URL = "";

    public CashShop() {}

    public Map<Integer, CashShopItem> getItems() {
        return items;
    }

    public Map<CashShopCategory, List<CashShopItem>> getCategoryInfo() {
        return categoryInfo;
    }

    public Map<String, CashShopItem> getSearchInfo() {
        return searchInfo;
    }

    public Map<Integer, Tuple<List<Integer>, List<Integer>>> getBeautyPreview() {
        return beautyPreview;
    }

    public Map<Integer, List<Integer>> getSurpiseBoxInfo() {
        return surpriseBoxInfo;
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
    public void encode(Char chr, OutPacket outPacket) {
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
        outPacket.encodeInt(chr.getLevel());
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
        for (Integer itemId : getBeautyPreview().keySet()) {
            outPacket.encodeInt(itemId);
            List<Integer> maleStyles = getBeautyPreview().get(itemId).getLeft();
            outPacket.encodeInt(maleStyles.size());
            maleStyles.forEach(outPacket::encodeInt);
            List<Integer> femaleStyles = getBeautyPreview().get(itemId).getRight();
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
        return getCategoryInfo().getOrDefault(csc, List.of());
    }

    public CashShopItem getItem(int sn) {
        return getItems().getOrDefault(sn, null);
    }

    public Set<CashShopItem> getFavorites(int accountId) {
        return favoriteInfo.getOrDefault(accountId, Set.of()).stream()
                .map(csf -> getItem(csf.getItemSn()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void addFavorite(CashShopFavorite csf, boolean saveToDb) {
        if (!favoriteInfo.containsKey(csf.getAccId())) {
            favoriteInfo.put(csf.getAccId(), new HashSet<>());
        }
        favoriteInfo.get(csf.getAccId()).add(csf);
        if (saveToDb) {
            DatabaseManager.saveToDB(csf);
        }
    }

    public void removeFavorite(int accId, int itemSn) {
        if (favoriteInfo.containsKey(accId)) {
            var iter = favoriteInfo.getOrDefault(accId, Set.of()).iterator();
            while (iter.hasNext()) {
                CashShopFavorite csf = iter.next();
                if (csf.getItemSn() != itemSn) {
                    continue;
                }
                iter.remove();
                DatabaseManager.deleteFromDB(csf);
            }
        }
    }

    public List<CashShopGift> claimGifts(int charId) {
        // fetch and delete
        List<CashShopGift> gifts = (List<CashShopGift>) DatabaseManager.getObjListFromDB(CashShopGift.class, "receiverId", charId);
        DatabaseManager.deleteFromDB(CashShopGift.class, "receiverId", charId);
        return gifts;
    }

    public void loadItems() {
        // load categories
        categories.clear();
        categories.addAll(CashShopCategory.BASE_CATEGORIES);
        // load items and search map
        items.clear();
        categoryInfo.clear();
        searchInfo.clear();
        beautyPreview.clear();
        surpriseBoxInfo.clear();
        for (CashShopItem csi : (List<CashShopItem>) DatabaseManager.getObjListFromDB(CashShopItem.class)) {
            items.put(csi.getId(), csi);
            CashShopCategory csc = Util.findWithPred(getCategories(), cat -> cat.getParentIdx() != 0 && cat.getName().equalsIgnoreCase(csi.getCategory()));
            if (!categoryInfo.containsKey(csc)) {
                categoryInfo.put(csc, new ArrayList<>());
            }
            categoryInfo.get(csc).add(csi);
            // get name for search map
            String name = StringData.getItemStringById(csi.getItemID());
            if (name == null) {
                continue;
            }
            name = name.toLowerCase().replaceAll(" ", "");
            searchInfo.put(name, csi);
            // cash shop random
            if (csi.getRandom() != null && csi.getRandom().size() > 0) {
                // extract preview info
                if (csi.getItemID() / 10000 == 515) {
                    beautyPreview.put(csi.getItemID(), new Tuple<>(
                            csi.getRandom().stream().filter(r -> r.getGender() == 0 || r.getGender() == 2).map(CashShopRandom::getReward).toList(),
                            csi.getRandom().stream().filter(r -> r.getGender() == 1 || r.getGender() == 2).map(CashShopRandom::getReward).toList()
                    ));
                }
                // extract surprise box info
                if (csi.getItemID() / 1000 == 5222) {
                    surpriseBoxInfo.put(csi.getItemID(), csi.getRandom().stream().map(CashShopRandom::getReward).toList());
                }
            }
        }
        // load favorites
        favoriteInfo.clear();
        for (CashShopFavorite csf : (List<CashShopFavorite>) DatabaseManager.getObjListFromDB(CashShopFavorite.class)) {
            addFavorite(csf, false);
        }
    }
}
