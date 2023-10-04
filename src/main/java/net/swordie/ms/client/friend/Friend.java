package net.swordie.ms.client.friend;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.OutPacket;

import javax.persistence.*;

/**
 * Created on 3/31/2018.
 */
@Entity
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int ownerID;
    private int ownerAccID;
    private int friendID;
    private String name;
    private byte flag; // 5 through 8 = account friend
    @Column(name = "groupName")
    private String group;
    private byte mobile;
    private int friendAccountID;
    private String nickname;
    private String memo;
    @Transient
    private Char chr;

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getFriendID());
        outPacket.encodeString(isOnline() ? getChr().getName() : getName(), 13); // currently online char for account friend
        outPacket.encodeByte(getFlag());
        outPacket.encodeInt(isOnline() ? getChr().getClient().getChannel() - 1 : -1);
        outPacket.encodeString(getGroup(), 17);
        outPacket.encodeByte(getMobile());
        outPacket.encodeInt(isAccount() ? getFriendAccountID() : 0); // client uses CTabFriend::FRIENDITEM.dwFriendAccountID to check if account friend
        outPacket.encodeString(getNickname(), 13);
        outPacket.encodeString(getMemo(), 256);
        outPacket.encodeInt(0); // inShop?
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getOwnerAccID() {
        return ownerAccID;
    }

    public void setOwnerAccID(int ownerAccID) {
        this.ownerAccID = ownerAccID;
    }

    public int getFriendID() {
        return friendID;
    }

    public void setFriendID(int friendID) {
        this.friendID = friendID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public void setFlag(FriendFlag flag) {
        setFlag((byte) flag.getVal());
    }

    public boolean isFlag(FriendFlag flag) {
        return getFlag() == flag.getVal();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public byte getMobile() {
        return mobile;
    }

    public void setMobile(byte mobile) {
        this.mobile = mobile;
    }

    public int getFriendAccountID() {
        return friendAccountID;
    }

    public void setFriendAccountID(int friendAccountID) {
        this.friendAccountID = friendAccountID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAccount() {
        return getFlag() >= FriendFlag.AccountFriendRequest.getVal();
    }

    public Char getChr() {
        return chr;
    }

    public void setChr(Char chr) {
        this.chr = chr;
    }

    public void write(OutPacket outPacket) {
        if (isOnline()) {
            chr.write(outPacket);
        }
    }

    public boolean isOnline() {
        return chr != null && chr.isOnline();
    }

}
