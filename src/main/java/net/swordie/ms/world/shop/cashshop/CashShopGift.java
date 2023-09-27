package net.swordie.ms.world.shop.cashshop;

import net.swordie.ms.connection.Encodable;
import net.swordie.ms.connection.OutPacket;

import javax.persistence.*;

@Entity
@Table(name = "cs_gifts")
public class CashShopGift implements Encodable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cashitemid")
    private CashItemInfo item;
    private int receiverId;
    private String senderName;
    private String giftMessage;

    public CashShopGift() {}

    public CashShopGift(CashItemInfo item, int receiverId, String senderName, String giftMessage) {
        this.item = item;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.giftMessage = giftMessage;
    }

    @Override
    public void encode(OutPacket outPacket) {
        // 70 bytes
        outPacket.encodeLong(item.getId());
        outPacket.encodeInt(item.getItemID());
        outPacket.encodeString(senderName, 13);
        outPacket.encodeString(giftMessage, 45);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CashItemInfo getItem() {
        return item;
    }

    public void setItem(CashItemInfo item) {
        this.item = item;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }
}
