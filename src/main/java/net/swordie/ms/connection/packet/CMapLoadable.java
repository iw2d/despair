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

    public static OutPacket setMapObjectVisible(String objectName, int index) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_MAP_OBJECT_VISIBLE);
        outPacket.encodeString(objectName);
        outPacket.encodeInt(index);
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

    public static OutPacket setSpineBackEffect(String keyName, int msgType) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_SPINE_BACK_EFFECT);
        outPacket.encodeString(keyName);
        outPacket.encodeInt(msgType);
        return outPacket;
    }

    public static OutPacket reloadBack() {
        OutPacket outPacket = new OutPacket(OutHeader.RELOAD_BACK);
        return outPacket;
    }

    public enum SpineScriptMsgType {
        None(1),
        PKCS7(2),
        Zeros(3),
        ANSIX923(4),
        ISO10126(5),
        ANSIX923_PKCS7(6)
        ;

        private int value;

        SpineScriptMsgType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
