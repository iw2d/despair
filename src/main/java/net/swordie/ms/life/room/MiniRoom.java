package net.swordie.ms.life.room;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.enums.MiniRoomType;
import net.swordie.ms.life.Life;

import java.util.ArrayList;
import java.util.List;

public abstract class MiniRoom extends Life {
    private String title;
    private boolean isPrivate;
    private String password;
    private int kind;
    private List<Char> chars = new ArrayList<>();
    private Char owner;
    private int maxSize;
    private boolean open;

    public MiniRoom(int templateId) {
        super(templateId);
    }

    public abstract MiniRoomType getMiniRoomType();

    @Override
    public int getType() {
        return getMiniRoomType().getVal();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public Char getOwner() {
        return owner;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public List<Char> getChars() {
        return chars;
    }

    public void addChar(Char chr) {
        chars.add(chr);
    }

    public void removeChar(Char chr) {
        chars.remove(chr);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getObjectId());
        outPacket.encodeString(getTitle());
        outPacket.encodeByte(isPrivate());
        outPacket.encodeByte(getKind()); // piece type for omok, size for match card
        outPacket.encodeByte(getChars().size());
        outPacket.encodeByte(getMaxSize());
        outPacket.encodeByte(isOpen() ? 0 : 1);
    }

    @Override
    public void broadcastSpawnPacket(Char onlyChar) {
        if (onlyChar == null) {
            getField().broadcastPacket(UserPacket.makeMiniRoomBalloon(this));
        } else {
            // handled by USER_ENTER_FIELD
        }
    }

    @Override
    public void broadcastLeavePacket() {
        getField().broadcastPacket(UserPacket.destroyMiniRoomBalloon(this));
    }


    public static MiniRoom createMiniGameRoom(MiniRoomType type, String title, boolean isPrivate, String password, int kind) {
        MiniRoom miniRoom;
        switch (type) {
            case OMOK:
                miniRoom = new OmokGameRoom(0);
                break;
            case MEMORY_GAME:
                miniRoom = new MemoryGameRoom(0);
                break;
            default:
                return null;
        }
        miniRoom.setTitle(title);
        miniRoom.setPrivate(isPrivate);
        miniRoom.setPassword(password);
        miniRoom.setKind(kind);
        miniRoom.setOpen(true);
        return miniRoom;
    }
}
