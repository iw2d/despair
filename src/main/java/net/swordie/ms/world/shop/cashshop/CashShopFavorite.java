package net.swordie.ms.world.shop.cashshop;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cs_favorites")
public class CashShopFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int accId;
    private int itemSn;

    public CashShopFavorite() {}

    public CashShopFavorite(int accId, int itemSn) {
        this.accId = accId;
        this.itemSn = itemSn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public int getItemSn() {
        return itemSn;
    }

    public void setItemSn(int itemSn) {
        this.itemSn = itemSn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accId, itemSn);
    }
}
