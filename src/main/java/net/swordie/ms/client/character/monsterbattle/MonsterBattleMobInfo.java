package net.swordie.ms.client.character.monsterbattle;

import net.swordie.ms.connection.OutPacket; /**
 * Created on 12/20/2017.
 */
public class MonsterBattleMobInfo {
    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
        outPacket.encodeByte(0);
        outPacket.encodeInt(0);
        outPacket.encodeInt(0);
    }
}
