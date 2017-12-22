package enums;

import java.util.Arrays;

/**
 * Created on 11/23/2017.
 */
public enum InvType {
    EQUIPPED(-1),
    EQUIP(1),
    USE(2),
    ETC(3),
    SETUP(4),
    CASH(5)
    ;

    private byte val;

    InvType(int val) {
        this((byte) val);
    }

    InvType(byte val) {
        this.val = val;
    }

    public byte getVal() {
        return val;
    }

    public static InvType getInvTypeByVal(int val) {
        return Arrays.stream(InvType.values()).filter(t -> t.getVal() == val).findFirst().orElse(null);
    }
}
