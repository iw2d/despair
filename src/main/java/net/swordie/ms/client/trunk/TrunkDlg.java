package net.swordie.ms.client.trunk;

import net.swordie.ms.connection.Encodable;
import net.swordie.ms.connection.OutPacket;

public class TrunkDlg implements Encodable {
    private TrunkType type;
    private Trunk trunk;
    private int npcTemplateId;

    public TrunkDlg(TrunkType type) {
        this.type = type;
    }

    public TrunkType getType() {
        return type;
    }

    public Trunk getTrunk() {
        return trunk;
    }

    public void setTrunk(Trunk trunk) {
        this.trunk = trunk;
    }

    public int getNpcTemplateId() {
        return npcTemplateId;
    }

    public void setNpcTemplateId(int npcTemplateId) {
        this.npcTemplateId = npcTemplateId;
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(type.getVal());
        switch (type) {
            case TrunkRes_OpenTrunkDlg:
                outPacket.encodeInt(npcTemplateId);
                trunk.encodeItems(outPacket);
                break;
            case TrunkRes_GetSuccess:
            case TrunkRes_PutSuccess:
            case TrunkRes_SortItem:
            case TrunkRes_MoneySuccess:
                trunk.encodeItems(outPacket);
                break;
            default:
                break;
        }
    }

    public static TrunkDlg open(int npcTemplateId, Trunk trunk) {
        TrunkDlg td = new TrunkDlg(TrunkType.TrunkRes_OpenTrunkDlg);
        td.setNpcTemplateId(npcTemplateId);
        td.setTrunk(trunk);
        return td;
    }

    public static TrunkDlg success(TrunkType type, Trunk trunk) {
        TrunkDlg td = new TrunkDlg(type);
        td.setTrunk(trunk);
        return td;
    }

    public static TrunkDlg msg(TrunkType type) {
        return new TrunkDlg(type);
    }
}
