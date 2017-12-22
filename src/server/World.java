package server;

import enums.ServerStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 11/2/2017.
 */
public class World {
    //WORLDITEM struct

    private int worldId, worldState, worldEventEXP_WSE, worldEventDrop_WSE, boomUpEventNotice;
    private boolean starplanet;
    private String name, worldEventDescription;
    private List<Channel> channels;

    public World(int worldId, String name, int worldState, String worldEventDescription, int worldEventEXP_WSE,
                 int worldEventDrop_WSE, int boomUpEventNotice, int amountOfChannels, boolean starplanet) {
        this.worldId = worldId;
        this.name = name;
        this.worldState = worldState;
        this.worldEventDescription = worldEventDescription;
        this.worldEventEXP_WSE = worldEventEXP_WSE;
        this.worldEventDrop_WSE = worldEventDrop_WSE;
        this.boomUpEventNotice = boomUpEventNotice;
        List<Channel> channelList = new ArrayList<>();
        for(int i = 1; i <= amountOfChannels; i++){
            channelList.add(new Channel(name, worldId, i));
        }
        this.channels = channelList;
        this.starplanet = starplanet;
    }

    public World(int worldId, String name, int amountOfChannels) {
        this(worldId, name, 0, "", 100, 100,
                0, amountOfChannels, false);
    }

    public int getWorldId() {
        return worldId;
    }

    public String getName() {
        return name;
    }

    public int getWorldState() {
        return worldState;
    }

    public int getWorldEventEXP_WSE() {
        return worldEventEXP_WSE;
    }

    public int getWorldEventDrop_WSE() {
        return worldEventDrop_WSE;
    }

    public int getBoomUpEventNotice() {
        return boomUpEventNotice;
    }

    public boolean isStarplanet() {
        return starplanet;
    }

    public String getWorldEventDescription() {
        return worldEventDescription;
    }

    public void setWorldEventDescription(String worldEventDescription) {
        this.worldEventDescription = worldEventDescription;
    }

    public Channel getChannelById(byte id) {
        return getChannels().stream().filter(c -> c.getChannelId() == id).findFirst().orElse(null);
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public ServerStatus getStatus() {
        return ServerStatus.NORMAL;
    }
}
