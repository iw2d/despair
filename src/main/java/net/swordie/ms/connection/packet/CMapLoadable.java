package net.swordie.ms.connection.packet;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.handlers.header.OutHeader;

public class CMapLoadable {

    public static OutPacket setBackEffect(int effect, int fieldID, int pageID, int duration) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_BACK_EFFECT);
        outPacket.encodeByte(effect);
        outPacket.encodeInt(fieldID);
        outPacket.encodeByte(pageID);
        outPacket.encodeInt(duration);
        return outPacket;
    }
    public static OutPacket setMapObjectMove(String keyName, int x, int y, int time) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_MAP_OBJECT_MOVE);
        outPacket.encodeString(keyName);
        outPacket.encodeInt(x);
        outPacket.encodeInt(y);
        outPacket.encodeInt(time);
        return outPacket;
    }
}
