package net.swordie.ms.life.mob.boss.demian.stigma;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.DemianFieldPacket;
import net.swordie.ms.life.Life;
import net.swordie.ms.util.Position;
import net.swordie.ms.world.field.Field;

import java.util.Random;

/**
 * Created on 18-8-2019.
 *
 * @author Asura
 */
public class DemianStigmaIncinerateObject extends Life {

    public DemianStigmaIncinerateObject(int templateId) {
        super(templateId);
    }

    @Override
    public void broadcastSpawnPacket(Char onlyChar) {
        // randomise position before spawning
        setPosition(new Position(new Random().nextInt(1400) + 100, 16));
        OutPacket outPacket = DemianFieldPacket.stigmaIncinerateObjectPacket(this, true);
        if (onlyChar == null) {
            getField().broadcastPacket(outPacket);
        } else {
            onlyChar.write(outPacket);
        }
    }

    @Override
    public void broadcastLeavePacket() {
        getField().broadcastPacket(DemianFieldPacket.stigmaIncinerateObjectPacket(null, true)); // null -> remove
    }
}
