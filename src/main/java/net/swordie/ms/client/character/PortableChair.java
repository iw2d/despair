package net.swordie.ms.client.character;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.util.Position;
import net.swordie.ms.util.Rect;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;

import java.util.*;

public class PortableChair {
    public static final PortableChair EMPTY_CHAIR = new PortableChair(0);
    private int itemId;
    private int meso;
    private String text;
    private List<Integer> towerChairIdList = new ArrayList<>();

    // group chair fields
    private int objectId;
    private int capacity;
    private Rect rect;
    private Position position;
    private List<Integer> emotions = List.of(0);
    private Char owner;
    private Map<Char, Integer> groupChairUsers = new HashMap<>(); // chr, emotionId

    // unk fields
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

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Integer> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Integer> emotions) {
        this.emotions = emotions;
    }

    public boolean isOwner(Char chr) {
        return owner == chr;
    }

    public Char getOwner() {
        return owner;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public Set<Char> getGroupChairUsers() {
        return groupChairUsers.keySet();
    }

    public void addGroupChairUser(Char chr) {
        groupChairUsers.put(chr, Util.getRandomFromCollection(emotions));
    }

    public void removeGroupChairUser(Char chr) {
        groupChairUsers.remove(chr);
    }

    public boolean isCustomChair() {
        // TODO: timeChairInfo, popChairInfo, starForceChairInfo
        return false;
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

    public void encodeGroupChairInfo(OutPacket outPacket) {
        // sub_DDA560
        outPacket.encodeInt(objectId);
        outPacket.encodeByte(true);
        // sub_DD9130
        outPacket.encodeInt(itemId);
        outPacket.encodeInt(capacity);
        outPacket.encodeRectInt(rect);
        outPacket.encodePositionInt(position);
        outPacket.encodeInt(groupChairUsers.size());
        groupChairUsers.forEach((chr, emotion) -> {
            outPacket.encodeInt(chr.getId());
            outPacket.encodeByte(true);
            outPacket.encodeInt(emotion);
        });
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
