package net.swordie.ms.life;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.packet.DragonPool;

public class Dragon extends Life {

    private Char owner;

    public Dragon(int templateId) {
        super(templateId);
    }

    public Char getOwner() {
        return owner;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public void resetToPlayer() {
        setPosition(owner.getPosition().deepCopy());
        setMoveAction((byte) 4); // default
    }

    @Override
    public void broadcastSpawnPacket(Char onlyChar) {
        OutPacket outPacket = DragonPool.createDragon(this);
        if (onlyChar == null) {
            getField().broadcastPacket(outPacket);
        } else {
            onlyChar.write(outPacket);
        }
    }

    @Override
    public void broadcastLeavePacket() {
        getField().broadcastPacket(DragonPool.removeDragon(this));
    }
}
