package net.swordie.ms.client.character;

import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.enums.ChairType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 26-5-2019.
 *
 * @author Asura
 */
public class PortableChair {
    private int itemID;
    private String msg = "";
    private List<String> displayChrs = new ArrayList<>();
    private int meso;
    private int displayedNumber;
    private int groupChairOID;
    private ChairType type;

    public PortableChair(int itemID, ChairType type) {
        this.itemID = itemID;
        this.type = type;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public int getMeso() {
        return meso;
    }
    public void setMeso(int meso) {
        this.meso = meso;
    }

    public int getDisplayedNumber() {
        return displayedNumber;
    }

    public void setDisplayedNumber(int displayedNumber) {
        this.displayedNumber = displayedNumber;
    }

    public int getGroupChairOID() {
        return groupChairOID;
    }

    public void setGroupChairOID(int groupChairOID) {
        this.groupChairOID = groupChairOID;
    }

    public ChairType getType() {
        return type;
    }

    public void setType(ChairType type) {
        this.type = type;
    }

}
