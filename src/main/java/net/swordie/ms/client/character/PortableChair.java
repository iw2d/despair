package net.swordie.ms.client.character;

import net.swordie.ms.connection.OutPacket;

import java.util.ArrayList;
import java.util.List;

public class PortableChair {
    public static final PortableChair EMPTY_CHAIR = new PortableChair(0);
    private int itemId;
    private int meso;
    private String text;
    private List<Integer> towerChairIdList = new ArrayList<>();
    private int unk1;
    private int unk2;

    public PortableChair(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getMeso() {
        return meso;
    }

    public void setMeso(int meso) {
        this.meso = meso;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Integer> getTowerChairIdList() {
        return towerChairIdList;
    }

    public boolean isCustomChair() {
        return false; // TODO: timeChairInfo, popChairInfo, starForceChairInfo
    }

    public void encodeTextChairInfo(OutPacket outPacket) {
        outPacket.encodeInt(text != null ? 1 : 0);
        if (text != null) {
            outPacket.encodeString(text);
        }
    }

    public void encodeTowerChairInfo(OutPacket outPacket) {
        outPacket.encodeInt(towerChairIdList.size());
        for (Integer towerChairId : towerChairIdList) {
            outPacket.encodeInt(towerChairId);
        }
    }

    public void encodeForSetActivePortableChair(OutPacket outPacket) {
        outPacket.encodeInt(itemId);
        encodeTextChairInfo(outPacket);
        outPacket.encodeInt(unk1); // this + 93552
        outPacket.encodeInt(unk2); // this + 93556
        encodeTowerChairInfo(outPacket);
        outPacket.encodeByte(false); // sub_130B7E0
        outPacket.encodeInt(meso);
        outPacket.encodeByte(isCustomChair());
    }

    public void encodeForEnterField(OutPacket outPacket) {
        outPacket.encodeInt(itemId);
        encodeTextChairInfo(outPacket);
        encodeTowerChairInfo(outPacket);
        outPacket.encodeInt(unk1); // this + 93552
        outPacket.encodeInt(unk2); // this + 93556
        outPacket.encodeByte(false); // sub_130B7E0: int, int -> (int, str, (byte -> AvatarLook), (byte -> AvatarLook))
    }
}
