package net.swordie.ms.life.room;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.MiniRoomPacket;
import net.swordie.ms.connection.packet.UserPacket;
import net.swordie.ms.enums.LeaveType;
import net.swordie.ms.enums.MiniRoomAction;
import net.swordie.ms.enums.MiniRoomType;
import net.swordie.ms.enums.RoomLeaveType;

public class OmokGameRoom extends MiniRoom {
    private OmokGame omokGame;
    private int nextTurn = 0;

    public OmokGameRoom(int templateId) {
        super(templateId);
    }

    @Override
    public MiniRoomType getMiniRoomType() {
        return MiniRoomType.OMOK;
    }

    @Override
    public int getMaxSize() {
        return 2;
    }

    public int getNextTurn() {
        return nextTurn;
    }

    public void setNextTurn(int nextTurn) {
        this.nextTurn = nextTurn;
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
                    broadcastPacket(MiniRoomPacket.MiniGameRoom.gameResult(this, 2, chr));
                    setNextTurn(getPosition(chr)); // get next move for accepting tie request
                }
                break;
            case ClaimGiveUp:
                broadcastPacket(MiniRoomPacket.MiniGameRoom.gameResult(this, 0, getOtherChar(chr)));
                setNextTurn(getPosition(chr));
                break;
            case RetreatRequest:
                getOtherChar(chr).write(MiniRoomPacket.MiniGameRoom.retreatRequest());
                break;
            case RetreatResult:
                if (inPacket.decodeByte() == 0) {
                    getOtherChar(chr).write(MiniRoomPacket.MiniGameRoom.retreatResult(false, -1, -1));
                } else {
                    int count = omokGame.tryRetreat();
                    if (count % 2 != 0) {
                        setNextTurn(getNextTurn() ^ 1);
                    }
                    broadcastPacket(MiniRoomPacket.MiniGameRoom.retreatResult(true, count, getNextTurn()));
                }
                break;
            case UserLeaveBooked:
            case UserCancelLeaveBooked:
                // TODO: handle leave booking
                break;
            case UserReady:
            case UserCancelReady:
                broadcastPacket(MiniRoomPacket.MiniGameRoom.userReady(mra == MiniRoomAction.UserReady));
                break;
            case UserClickBan:
                Char expelChar = getOtherChar(chr);
                broadcastPacket(MiniRoomPacket.MiniGameRoom.leave(getPosition(expelChar), RoomLeaveType.MRLeave_Kicked));
                removeChar(expelChar);
                expelChar.setMiniRoom(null);
                getField().broadcastPacket(UserPacket.makeMiniRoomBalloon(this)); // update balloon
                break;
            case UserStart:
                omokGame = new OmokGame();
                broadcastPacket(MiniRoomPacket.MiniGameRoom.userStart(getNextTurn() == 0 ? 1 : 0)); // idk
                break;
            case TimeOver:
                setNextTurn(getPosition(getOtherChar(chr)));
                broadcastPacket(MiniRoomPacket.MiniGameRoom.timeOver(getNextTurn()));
                break;
            case PutStoneChecker:
                int x = inPacket.decodeInt();
                int y = inPacket.decodeInt();
                int type = inPacket.decodeByte();
                OmokGame.OmokCheckResult result = omokGame.tryPutStone(x, y, type);
                if (result == OmokGame.OmokCheckResult.OK) {
                    broadcastPacket(MiniRoomPacket.MiniGameRoom.putStoneChecker(x, y, type));
                    setNextTurn(getPosition(getOtherChar(chr)));
                } else if (result == OmokGame.OmokCheckResult.WIN) {
                    broadcastPacket(MiniRoomPacket.MiniGameRoom.putStoneChecker(x, y, type));
                    broadcastPacket(MiniRoomPacket.MiniGameRoom.gameResult(this, 2, chr));
                    setNextTurn(getPosition(getOtherChar(chr)));
                } else {
                    chr.write(MiniRoomPacket.MiniGameRoom.putStoneCheckerErr(result.getVal()));
                }
                break;
            default:
                super.handlePacket(chr, mra, inPacket);
                break;
        }
    }

}
