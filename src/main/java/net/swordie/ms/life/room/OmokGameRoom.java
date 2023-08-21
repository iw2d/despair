package net.swordie.ms.life.room;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.MiniRoomPacket;
import net.swordie.ms.enums.MiniRoomAction;
import net.swordie.ms.enums.MiniRoomType;

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

    @Override
    public int getNextTurn() {
        return nextTurn;
    }

    @Override
    public void setNextTurn(int nextTurn) {
        this.nextTurn = nextTurn;
    }

    public void handlePacket(Char chr, MiniRoomAction mra, InPacket inPacket) {
        switch (mra) {
            case RetreatRequest:
                getOtherChar(chr).write(MiniRoomPacket.MiniGameRoom.retreatRequest());
                break;
            case RetreatResult:
                if (inPacket.decodeByte() == 0) {
                    getOtherChar(chr).write(MiniRoomPacket.MiniGameRoom.retreatResult(false, -1, -1));
                } else {
                    int count = omokGame.tryRetreat();
                    if (count % 2 != 0) {
                        setNextTurn(getNextTurn() == 0 ? 1 : 0);
                    }
                    broadcastPacket(MiniRoomPacket.MiniGameRoom.retreatResult(true, count, getNextTurn()));
                }
                break;
            case UserStart:
                omokGame = new OmokGame();
                broadcastPacket(MiniRoomPacket.MiniGameRoom.userStart(getNextTurn() == 0 ? 1 : 0));
                setOpen(false);
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
                    setNextTurn(getPosition(getOtherChar(chr)));
                    end(2, chr);
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
