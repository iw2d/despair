package net.swordie.ms.client.character.social;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.OutPacket;

import javax.persistence.*;

@Entity
@Table(name = "couplerecords")
public class CoupleRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private CoupleRecordType type;
    private int status;
    private int itemId;
    private int charId;
    private int partnerId;
    private String charName;
    private String partnerName;
    private long charSn;
    private long partnerSn;

    public CoupleRecord() {}

    public CoupleRecord(int itemId, int charId, int partnerId, String charName, String partnerName, long charSn, long partnerSn) {
        this.itemId = itemId;
        this.charId = charId;
        this.partnerId = partnerId;
        this.charName = charName;
        this.partnerName = partnerName;
        this.charSn = charSn;
        this.partnerSn = partnerSn;
    }

    public void encodeForLocal(Char chr, OutPacket outPacket) {
        if (isMarriage()) {
            // GW_MarriageRecord::Decode (48)
            outPacket.encodeInt(id);                            // dwMarriageNo
            outPacket.encodeInt(charId);                        // dwGroomID
            outPacket.encodeInt(partnerId);                     // dwBrideID
            outPacket.encodeShort(status);                      // usStatus
            outPacket.encodeInt(itemId);                        // nGroomItemID
            outPacket.encodeInt(itemId);                        // nBrideItemID
            outPacket.encodeString(charName, 13);               // sGroomName
            outPacket.encodeString(partnerName, 13);            // sBrideName
        } else {
            // GW_CoupleRecord::Decode (33) / GW_FriendRecord::Decode (37)
            if (chr.getId() == charId) {
                outPacket.encodeInt(partnerId);                 // dwPairCharacterID
                outPacket.encodeString(partnerName, 13);        // sPairCharacterName
                outPacket.encodeLong(charSn);                   // liSN
                outPacket.encodeLong(partnerSn);                // liPairSN
            } else {
                outPacket.encodeInt(charId);                    // dwPairCharacterID
                outPacket.encodeString(charName, 13);           // sPairCharacterName
                outPacket.encodeLong(partnerSn);                // liSN
                outPacket.encodeLong(charSn);                   // liPairSN
            }
            if (isFriend()) {
                outPacket.encodeInt(itemId);                    // dwFriendItemID
            }
        }
    }

    public void encodeForRemote(Char chr, OutPacket outPacket) {
        // CUserRemote::Init / CUserRemote::OnAvatarModified
        if (isMarriage()) {
            outPacket.encodeInt(charId);                        // dwMarriageCharacterID
            outPacket.encodeInt(partnerId);                     // dwMarriagePairCharacterID
            outPacket.encodeInt(itemId);                        // nWeddingRingID
        } else {
            if (chr.getId() == charId) {
                outPacket.encodeLong(charSn);                // liCoupleItemSN
                outPacket.encodeLong(partnerSn);                   // liPairItemSN
            } else {
                outPacket.encodeLong(partnerSn);                   // liCoupleItemSN
                outPacket.encodeLong(charSn);                // liPairItemSN
            }
            outPacket.encodeInt(itemId);                        // nItemID
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CoupleRecordType getType() {
        return type;
    }

    public void setType(CoupleRecordType type) {
        this.type = type;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public long getCharSn() {
        return charSn;
    }

    public void setCharSn(long charSn) {
        this.charSn = charSn;
    }

    public long getPartnerSn() {
        return partnerSn;
    }

    public void setPartnerSn(long partnerSn) {
        this.partnerSn = partnerSn;
    }

    public boolean isCouple() {
        return type == CoupleRecordType.COUPLE;
    }

    public boolean isFriend() {
        return type == CoupleRecordType.FRIENDSHIP;
    }

    public boolean isMarriage() {
        return type == CoupleRecordType.MARRIAGE;
    }


    public enum CoupleRecordType {
        COUPLE, FRIENDSHIP, MARRIAGE
    }
}
