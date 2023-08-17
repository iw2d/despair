package net.swordie.ms.client.jobs.resistance;

import net.swordie.ms.client.character.Char;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.connection.packet.FieldPacket;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.util.Position;
import net.swordie.ms.world.field.Field;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Asura on 27-7-2018.
 */
public class OpenGate {
    private Char chr;
    private Position position;
    private Party party;
    private int gateId;

    public OpenGate(Char chr, Position position, Party party, int gateId) {
        this.chr = chr;
        this.position = position;
        this.party = party;
        this.gateId = gateId;
    }


    public Char getChr() {
        return chr;
    }

    public void setChr(Char chr) {
        this.chr = chr;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public int getGateId() {
        return gateId;
    }

    public void setGateId(int gateId) {
        this.gateId = gateId;
    }

    public void spawnOpenGate(Field field) {
        field.broadcastPacket(FieldPacket.openGateCreated(this));
        field.addOpenGate(this);
    }

    public void despawnOpenGate(Field field) {
        field.broadcastPacket(FieldPacket.openGateRemoved(this));
        field.removeOpenGate(this);
    }

    public void showOpenGate(Field field) {
        field.broadcastPacket(FieldPacket.openGateCreated(this));
    }

}
