package net.swordie.ms.world.auction;

import net.swordie.ms.ServerConfig;
import net.swordie.ms.client.character.items.Item;
import net.swordie.ms.connection.Encodable;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.db.FileTimeConverter;
import net.swordie.ms.constants.ItemConstants;
import net.swordie.ms.enums.AuctionState;
import net.swordie.ms.loaders.StringData;
import net.swordie.ms.util.FileTime;

import javax.persistence.*;


@Entity
@Table(name = "auctionitems")
public class AuctionItem implements Encodable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int type;
    private int accountID;
    private int charID;
    private AuctionState state;
    private int itemType;
    private String charName;
    private long price;
    @Convert(converter = FileTimeConverter.class)
    private FileTime endDate;
    private int bidUserID;
    private String bidUsername;
    private int bidWorld;
    private int oid;
    @Convert(converter = FileTimeConverter.class)
    private FileTime regDate;
    private long deposit;
    private int ssType;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item")
    private Item item;
    private String itemName;
    private int soldQuantity;

    public AuctionItem() {
        state = AuctionState.Init;
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getId());

        // AC_AuctionItem::Decode -> decodeBuffer(122) [AC_AuctionItem]
        outPacket.encodeInt(getId()); // dwAuctionID
        outPacket.encodeInt(getType()); // nAuctionType
        outPacket.encodeInt(getCharID()); // dwCharacterID
        outPacket.encodeInt(getAccountID()); // dwAccountID
        outPacket.encodeInt(getState().getVal()); // nState
        outPacket.encodeInt(getItemType()); // nItemType
        outPacket.encodeInt(ServerConfig.WORLD_ID); // nWorldID
        outPacket.encodeString(getCharName(), 13); // sCharName
        outPacket.encodeLong(getPrice()); // nPrice
        outPacket.encodeLong(getSecondPrice()); // nSecondPrice
        outPacket.encodeLong(getDirectPrice()); // nDirectPrice
        outPacket.encodeFT(getEndDate()); // ftEndDate
        outPacket.encodeInt(getBidUserID()); // dwBidUserID
        outPacket.encodeString(getBidUsername(), 13); // sBidUserName
        outPacket.encodeInt(getBidWorld()); // nBidWorld
        outPacket.encodeLong(getOid()); // nNexonOID
        outPacket.encodeFT(getRegDate()); // ftRegDate
        outPacket.encodeLong(0); // nDeposit
        outPacket.encodeInt(getSsType()); // nSSType

        // GW_ItemSlotBase::Decode
        outPacket.encode(getItem());
    }

    public void encodeHistory(OutPacket outPacket) {
        // AC_AuctionHistory::Decode -> decodeBuffer(60) [AC_AuctionHistory]
        outPacket.encodeLong(getId()); // idk
        outPacket.encodeInt(getId());
        outPacket.encodeInt(getAccountID());
        outPacket.encodeInt(getCharID());
        outPacket.encodeInt(getItem().getItemId());
        outPacket.encodeInt(getState().getVal());
        outPacket.encodeLong(getDirectPrice());
        outPacket.encodeFT(getEndDate());
        outPacket.encodeLong(0); // nDeposit
        outPacket.encodeInt(getItem().getQuantity());
        outPacket.encodeInt(ServerConfig.WORLD_ID); // nWorldID
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getCharID() {
        return charID;
    }

    public void setCharID(int charID) {
        this.charID = charID;
    }

    public AuctionState getState() {
        return state;
    }

    public void setState(AuctionState state) {
        this.state = state;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getSecondPrice() {
        return price;
    }

    public long getDirectPrice() {
        if (item.getQuantity() > 1 && !ItemConstants.isThrowingItem(item.getItemId())) {
            return price * getQuantity();
        }
        return price;
    }

    public FileTime getEndDate() {
        return endDate;
    }

    public void setEndDate(FileTime endDate) {
        this.endDate = endDate;
    }

    public int getBidUserID() {
        return bidUserID;
    }

    public void setBidUserID(int bidUserID) {
        this.bidUserID = bidUserID;
    }

    public String getBidUsername() {
        return bidUsername;
    }

    public void setBidUsername(String bidUsername) {
        this.bidUsername = bidUsername;
    }

    public int getBidWorld() {
        return bidWorld;
    }

    public void setBidWorld(int bidWorld) {
        this.bidWorld = bidWorld;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public FileTime getRegDate() {
        return regDate;
    }

    public void setRegDate(FileTime regDate) {
        this.regDate = regDate;
    }

    public long getDeposit() {
        return deposit;
    }

    public void setDeposit(long deposit) {
        this.deposit = deposit;
    }

    public int getSsType() {
        return ssType;
    }

    public void setSsType(int ssType) {
        this.ssType = ssType;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        this.itemType = item.getType().getVal();
        this.itemName = StringData.getItemStringById(item.getItemId());
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public AuctionItem deepCopy() {
        AuctionItem ai = new AuctionItem();

        ai.type = type;
        ai.accountID = accountID;
        ai.charID = charID;
        ai.state = state;
        ai.itemType = itemType;
        ai.charName = charName;
        ai.price = price;
        ai.endDate = endDate;
        ai.bidUserID = bidUserID;
        ai.bidUsername = bidUsername;
        ai.bidWorld = bidWorld;
        ai.oid = oid;
        ai.regDate = regDate;
        ai.deposit = deposit;
        ai.ssType = ssType;
        ai.item = item;
        ai.itemName = itemName;

        return ai;
    }

    public int getQuantity() {
        return getItem().getQuantity();
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
