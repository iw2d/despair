package net.swordie.ms.life.room;

import net.swordie.ms.enums.MiniRoomType;

public class OmokGameRoom extends MiniRoom {
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

}
