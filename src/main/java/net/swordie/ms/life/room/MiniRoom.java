package net.swordie.ms.life.room;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.MiniRoomPacket;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.enums.MiniRoomAction;
import net.swordie.ms.enums.MiniRoomType;
import net.swordie.ms.enums.RoomLeaveType;
import net.swordie.ms.life.Life;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class MiniRoom extends Life {
    private static final Logger log = LogManager.getLogger(MiniRoom.class);
    private String title;
    private boolean isPrivate;
    private String password;
    private int kind;
    private List<Char> chars = new ArrayList<>();
    private Char owner;
    private boolean open;
    private Set<Char> leaveBook = new HashSet<>();

    public MiniRoom(int templateId) {
        super(templateId);
    }

    public abstract int getMaxSize();

    public abstract int getNextTurn();

    public abstract void setNextTurn(int nextTurn);

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
        updateBalloon();
    }

    public void removeChar(Char chr) {
        chars.remove(chr);
        updateBalloon();
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
        updateBalloon();
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

    public int getPosition(Char chr) {
        return getChars().indexOf(chr);
    }

    public Char getOtherChar(Char self) {
        for (Char chr : getChars()) {
            if (chr.getId() != self.getId()) {
                return chr;
            }
        }
        return null;
    }

    public void updateBalloon() {
        if (getOwner() != null && getField() != null) {
            getField().broadcastPacket(UserPacket.makeMiniRoomBalloon(this));
        }
    }

    public void end(int type, Char winner) {
        broadcastPacket(MiniRoomPacket.MiniGameRoom.gameResult(this, type, winner));
        setOpen(true);
        if (leaveBook.contains(getOwner())) {
            leave(getOwner());
        } else {
            for (Char chr : leaveBook) {
                leave(chr);
            }
        }
        leaveBook.clear();
    }

    public void leave(Char chr) {
        // handle game
        if (!isOpen()) {
            end(0, getOtherChar(chr));
        }
        if (getOwner() == chr) {
            // kick others
            for (Char visitor : getChars()) {
                if (chr != visitor) {
                    visitor.write(MiniRoomPacket.MiniGameRoom.leave(getPosition(visitor), RoomLeaveType.MRLeave_HostOut));
                    visitor.setMiniRoom(null);
                }
            }
            broadcastPacket(MiniRoomPacket.MiniGameRoom.leave(getPosition(chr), RoomLeaveType.MRLeave_UserRequest));
            getField().removeLife(this);
            chr.setMiniRoom(null);
        } else {
            // notify user leaving
            broadcastPacket(MiniRoomPacket.MiniGameRoom.leave(getPosition(chr), RoomLeaveType.MRLeave_UserRequest));
            chr.setMiniRoom(null);
            removeChar(chr);
        }
    }

    public void broadcastPacket(OutPacket outPacket) {
        for (Char chr : getChars()) {
            chr.write(outPacket);
        }
    }

    @Override
    public void broadcastSpawnPacket(Char onlyChar) {
        if (onlyChar == null) {
            updateBalloon();
        } else {
            // handled by USER_ENTER_FIELD
        }
    }

    @Override
    public void broadcastLeavePacket() {
        getField().broadcastPacket(UserPacket.destroyMiniRoomBalloon(getOwner()));
    }


    public void handlePacket(Char chr, MiniRoomAction mra, InPacket inPacket) {
        switch (mra) {
            case TieRequest:
                getOtherChar(chr).write(MiniRoomPacket.MiniGameRoom.tieRequest());
                break;
            case TieResult:
                if (inPacket.decodeByte() == 0) {
                    getOtherChar(chr).write(MiniRoomPacket.MiniGameRoom.tieResult());
                } else {
                    end(1, null);
                }
                break;
            case ClaimGiveUp:
                setNextTurn(getPosition(chr));
                end(0, getOtherChar(chr));
                break;
            case UserLeaveBooked:
                broadcastPacket(MiniRoomPacket.MiniGameRoom.infoMessage(5, chr.getName()));
                leaveBook.add(chr);
                break;
            case UserCancelLeaveBooked:
                broadcastPacket(MiniRoomPacket.MiniGameRoom.infoMessage(6, chr.getName()));
                leaveBook.remove(chr);
                break;
            case UserReady:
            case UserCancelReady:
                broadcastPacket(MiniRoomPacket.MiniGameRoom.userReady(mra == MiniRoomAction.UserReady));
                break;
            case UserClickBan:
                Char expelChar = getOtherChar(chr);
                broadcastPacket(MiniRoomPacket.MiniGameRoom.leave(getPosition(expelChar), RoomLeaveType.MRLeave_Kicked));
                expelChar.setMiniRoom(null);
                removeChar(expelChar);
                break;
            case TimeOver:
                setNextTurn(getPosition(getOtherChar(chr)));
                broadcastPacket(MiniRoomPacket.MiniGameRoom.timeOver(getNextTurn()));
                break;
            default:
                log.error(String.format("Unhandled MiniRoomAction %s(%d)", mra.name(), mra.getVal()));
                break;
        }
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
