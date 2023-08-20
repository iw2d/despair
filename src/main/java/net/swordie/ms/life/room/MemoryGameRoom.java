package net.swordie.ms.life.room;

import net.swordie.ms.enums.MiniRoomType;

public class MemoryGameRoom extends MiniRoom {
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

}