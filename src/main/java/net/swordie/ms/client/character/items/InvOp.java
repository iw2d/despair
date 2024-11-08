package net.swordie.ms.client.character.items;

import net.swordie.ms.enums.InvType;
import net.swordie.ms.enums.InventoryOperation;

import static net.swordie.ms.enums.InvType.EQUIPPED;

public class InvOp {
    private InventoryOperation op;
    private InvType invType;
    private Item item;
    private short oldPos;
    private short newPos;
    private int bagPos;


    public InvOp(InventoryOperation op, InvType invType, short oldPos, short newPos, int bagPos) {
        this.op = op;
        this.invType = invType;
        this.oldPos = oldPos;
        this.newPos = newPos;
        this.bagPos = bagPos;
    }

    public InvOp(InventoryOperation op, Item item, short oldPos, short newPos, int bagPos) {
        this(op, item.getInvType(), oldPos, newPos, bagPos);
        this.item = item;
    }

    public InvType getInvType() {
        return invType;
    }

    public InventoryOperation getOp() {
        return op;
    }

    public Item getItem() {
        return item;
    }

    public short getOldPos() {
        return oldPos;
    }

    public short getNewPos() {
        return newPos;
    }

    public int getBagPos() {
        return bagPos;
    }

}
