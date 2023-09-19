package net.swordie.ms.world.shop.cashshop;

import net.swordie.ms.connection.OutPacket;

import java.util.List;
import java.util.Objects;

public class CashShopCategory {
    public static final List<CashShopCategory> BASE_CATEGORIES = List.of(
            new CashShopCategory(2000000, "Favorites", 0, 100),

            new CashShopCategory(1010000, "Event", 0, 100),
            new CashShopCategory(1010100, "SSB", 1010000, 100),
            new CashShopCategory(1010200, "Special", 1010000, 100),
            new CashShopCategory(1010300, "Chance Sale", 1010000, 100),

            new CashShopCategory(1020000, "Enhance", 0, 100),
            new CashShopCategory(1020100, "Enhance", 1020000, 100),
            new CashShopCategory(1020200, "Scroll", 1020000, 100),

            new CashShopCategory(1030000, "Game", 0, 100),
            new CashShopCategory(1030100, "Convenience", 1030000, 100),
            new CashShopCategory(1030200, "Social", 1030000, 100),
            new CashShopCategory(1030300, "Atmospheric Effects", 1030000, 100),
            new CashShopCategory(1030400, "Add Slot", 1030000, 100),

            new CashShopCategory(1040000, "Outfits", 0, 100),
            new CashShopCategory(1040100, "Weapons", 1040000, 100),
            new CashShopCategory(1040200, "Hats", 1040000, 100),
            new CashShopCategory(1040300, "Capes", 1040000, 100),
            new CashShopCategory(1040400, "Outfits", 1040000, 100),
            new CashShopCategory(1040500, "Tops", 1040000, 100),
            new CashShopCategory(1040600, "Bottoms", 1040000, 100),
            new CashShopCategory(1040700, "Shoes", 1040000, 100),
            new CashShopCategory(1040800, "Gloves", 1040000, 100),
            new CashShopCategory(1040900, "Accessories", 1040000, 100),
            new CashShopCategory(1041000, "Eye Accessories", 1040000, 100),
            new CashShopCategory(1041100, "Rings", 1040000, 100),
            new CashShopCategory(1041200, "Effects", 1040000, 100),
            new CashShopCategory(1041300, "Transparent", 1040000, 100),

            new CashShopCategory(1050000, "Beauty", 0, 100),
            new CashShopCategory(1050100, "Hair", 1050000, 100),
            new CashShopCategory(1050200, "Face", 1050000, 100),
            new CashShopCategory(1050300, "Other", 1050000, 100),
            new CashShopCategory(1050400, "Expressions", 1050000, 100),

            new CashShopCategory(1060000, "Pets", 0, 100),
            new CashShopCategory(1060100, "Pets", 1060000, 100),
            new CashShopCategory(1060200, "Pet Equipment", 1060000, 100),
            new CashShopCategory(1060300, "Pet Use", 1060000, 100),
            new CashShopCategory(1060400, "Pet Skills", 1060000, 100),

            new CashShopCategory(1070000, "Packages", 0, 100),
            new CashShopCategory(1070100, "Packages", 1070000, 100)
    );

    private int id; //idx: base = 1000000; favorite = +1000000; category = +10000; subcategory = +100 subsubcategory = +1
    private int idx;
    private String name;
    private int parentIdx;
    private Flag flag = Flag.None;
    private int stock;

    public CashShopCategory(int idx, String name, int parentIdx, int stock) {
        this.idx = idx;
        this.name = name;
        this.parentIdx = parentIdx;
        this.stock = stock;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getIdx());
        outPacket.encodeString(getName());
        outPacket.encodeInt(getParentIdx());
        outPacket.encodeInt(getFlag() == null ? 0 : getFlag().ordinal());
        outPacket.encodeInt(getStock());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentIdx() {
        return parentIdx;
    }

    public void setParentIdx(int parentIdx) {
        this.parentIdx = parentIdx;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashShopCategory that = (CashShopCategory) o;
        return idx == that.idx &&
                parentIdx == that.parentIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idx, parentIdx);
    }

    @Override
    public String toString() {
        return "CashShopCategory{" +
                "idx=" + idx +
                ", name='" + name + '\'' +
                ", parentIdx=" + parentIdx +
                '}';
    }

    public enum Flag {
        None,
        New,
        Hot
    }



}
