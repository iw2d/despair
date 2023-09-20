package net.swordie.ms.world.shop.cashshop;

import net.swordie.ms.Server;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.loaders.ItemData;
import net.swordie.ms.util.FileTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created on 4/21/2018.
 */
@Entity
@Table(name = "cs_items")
public class CashShopItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int itemID;
    private int stock;
    @Enumerated(EnumType.ORDINAL)
    private CashShopItemFlag shopItemFlag = CashShopItemFlag.None;
    private int oldPrice;
    private int newPrice;
    private int bundleQuantity;
    private int availableDays;
    private short buyableWithMaplePoints;
    private short buyableWithCredit;
    private short buyableWithPrepaid;
    private short likable;
    private short meso;
    private short favoritable;
    private int gender;
    private int likes;
    private int requiredLevel;
    private String category;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parentid")
    private List<CashShopRandom> random;
    @Transient
    private int subCategory;
    @Transient
    private int parent;
    @Transient
    private CashShopCategory cashShopCategory;

    public CashShopItem() {
        stock = 100;
        buyableWithMaplePoints = 1;
        buyableWithCredit = 1;
        buyableWithPrepaid = 1;
        likable = 1;
        favoritable = 1;
        gender = 2;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getCashShopCategory() == null ? 0 : getCashShopCategory().getIdx());
        outPacket.encodeInt(getSubCategory());
        outPacket.encodeInt(getParent());

        outPacket.encodeString(Server.getInstance().getCashShop().getBannerUrl());
        outPacket.encodeInt(getId());
        outPacket.encodeInt(getItemID());
        outPacket.encodeInt(getStock());
        outPacket.encodeInt(getShopItemFlag().ordinal());
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(getOldPrice() == 0 ? getNewPrice() : getOldPrice());

        outPacket.encodeFT(FileTime.currentTime());
        outPacket.encodeFT(FileTime.fromType(FileTime.Type.MAX_TIME));
        outPacket.encodeFT(FileTime.currentTime());
        outPacket.encodeFT(FileTime.fromType(FileTime.Type.MAX_TIME));

        outPacket.encodeInt(getNewPrice());
        outPacket.encodeInt(1);
        outPacket.encodeInt(getBundleQuantity());
        outPacket.encodeInt(getAvailableDays());

        outPacket.encodeShort(getBuyableWithMaplePoints()); // with maple point
        outPacket.encodeShort(getBuyableWithCredit()); // with credit
        outPacket.encodeShort(getBuyableWithPrepaid()); // with prepaid
        outPacket.encodeShort(getLikable());
        outPacket.encodeShort(getMeso());
        outPacket.encodeShort(getFavoritable());

        outPacket.encodeInt(getGender());
        outPacket.encodeInt(getLikes());
        outPacket.encodeInt(getRequiredLevel());

        outPacket.encodeString("");

        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);

        outPacket.encodeByte(false); // has favorited, maybe implement later
        outPacket.encodeByte(false); // has liked, maybe implement later

        int size = 0;
        outPacket.encodeInt(size);
        for (int i = 0; i < size; i++) {
            // Package stuff, just leave it for now
            outPacket.encodeInt(1); // package item SN
            outPacket.encodeInt(2); // package item ID
            outPacket.encodeInt(3); // 1
            outPacket.encodeInt(4); // package item usual price
            outPacket.encodeInt(5); // package item discounted price
            outPacket.encodeInt(6);
            outPacket.encodeInt(7);
            outPacket.encodeInt(8);
            outPacket.encodeInt(9);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemID() {
        return itemID;
    }

    public CashShopItem setItemID(int itemID) {
        this.itemID = itemID;
        return this;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public CashShopItemFlag getShopItemFlag() {
        return shopItemFlag;
    }

    public void setShopItemFlag(CashShopItemFlag shopItemFlag) {
        this.shopItemFlag = shopItemFlag;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }

    public int getBundleQuantity() {
        return bundleQuantity;
    }

    public void setBundleQuantity(int bundleQuantity) {
        this.bundleQuantity = bundleQuantity;
    }

    public int getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(int availableDays) {
        this.availableDays = availableDays;
    }

    public short getBuyableWithMaplePoints() {
        return buyableWithMaplePoints;
    }

    public void setBuyableWithMaplePoints(short buyableWithMaplePoints) {
        this.buyableWithMaplePoints = buyableWithMaplePoints;
    }

    public short getBuyableWithCredit() {
        return buyableWithCredit;
    }

    public void setBuyableWithCredit(short buyableWithCredit) {
        this.buyableWithCredit = buyableWithCredit;
    }

    public short getBuyableWithPrepaid() {
        return buyableWithPrepaid;
    }

    public void setBuyableWithPrepaid(short buyableWithPrepaid) {
        this.buyableWithPrepaid = buyableWithPrepaid;
    }

    public short getLikable() {
        return likable;
    }

    public void setLikable(short likable) {
        this.likable = likable;
    }

    public short getMeso() {
        return meso;
    }

    public void setMeso(short meso) {
        this.meso = meso;
    }

    public short getFavoritable() {
        return favoritable;
    }

    public void setFavoritable(short favoritable) {
        this.favoritable = favoritable;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public int getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(int subCategory) {
        this.subCategory = subCategory;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<CashShopRandom> getRandom() {
        return random;
    }

    @Override
    public String toString() {
        return "CashShopItem{" +
                "itemID=" + itemID +
                ", newPrice=" + newPrice +
                ", category='" + category + '\'' +
                ", cashShopCategory=" + cashShopCategory +
                '}';
    }
    public void setCashShopCategory(CashShopCategory cashShopCategory) {
        this.cashShopCategory = cashShopCategory;
    }

    public CashShopCategory getCashShopCategory() {
        return cashShopCategory;
    }

    public CashItemInfo toCashItemInfo(Account account, Char chr) {
        CashItemInfo cii = new CashItemInfo();
        cii.setAccountID(account.getId());
        Item item = ItemData.getItemDeepCopy(getItemID());
        item.setQuantity((short) (getBundleQuantity() == 0 ? 1 : getBundleQuantity()));
        cii.setItem(item);
        cii.setCommodityID(getId());
        if (getAvailableDays() > 0) {
            item.setDateExpire(FileTime.fromDate(LocalDateTime.now().plusDays(getAvailableDays())));
        }
        return cii;
    }

    private enum CashShopItemFlag {
        None,
        Event,
        New,
        Sale,
        Hot,
        Limited,
        BlackFriday,
        AccountLimited,
        CharLimited
    }


}
