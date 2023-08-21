package net.swordie.ms.life.room;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.packet.MiniRoomPacket;
import net.swordie.ms.enums.MiniRoomAction;
import net.swordie.ms.enums.MiniRoomType;

public class MemoryGameRoom extends MiniRoom {
    private MemoryGame memoryGame;
    private int nextTurn = 0;
    private int firstCard;

    public MemoryGameRoom(int templateId) {
        super(templateId);
    }

    @Override
    public MiniRoomType getMiniRoomType() {
        return MiniRoomType.MEMORY_GAME;
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
            case UserStart:
                memoryGame = new MemoryGame(getKind());
                broadcastPacket(MiniRoomPacket.MiniGameRoom.userStartMemoryGame(getNextTurn() == 0 ? 1 : 0, memoryGame.getCards()));
                setOpen(false);
                break;
            case TurnUpCard:
                boolean isFirst = inPacket.decodeByte() != 0;
                int cardIndex = inPacket.decodeByte();
                if (isFirst) {
                    firstCard = cardIndex;
                    getOtherChar(chr).write(MiniRoomPacket.MiniGameRoom.turnUpCard(true, cardIndex, -1, -1));
                } else {
                    MemoryGame.MemoryGameResult result = memoryGame.checkCards(firstCard, cardIndex, chr);
                    if (result == MemoryGame.MemoryGameResult.NO_MATCH) {
                        broadcastPacket(MiniRoomPacket.MiniGameRoom.turnUpCard(false, cardIndex, firstCard, getPosition(chr)));
                        setNextTurn(getPosition(getOtherChar(chr)));
                    } else {
                        broadcastPacket(MiniRoomPacket.MiniGameRoom.turnUpCard(false, cardIndex, firstCard, getPosition(chr) + 2));
                        if (result == MemoryGame.MemoryGameResult.WIN) {
                            end(2, chr);
                        } else if (result == MemoryGame.MemoryGameResult.LOSE) {
                            end(2, getOtherChar(chr));
                        } else if (result == MemoryGame.MemoryGameResult.TIE) {
                            end(1, null);
                        }
                    }
                }
                break;
            default:
                super.handlePacket(chr, mra, inPacket);
                break;
        }
    }

}