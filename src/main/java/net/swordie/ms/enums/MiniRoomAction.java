package net.swordie.ms.enums;

import java.util.Arrays;

/**
 * Created by Asura on 10-8-2018.
 */
public enum MiniRoomAction {
    PlaceItem(0),
    PlaceItem_2(1),
    PlaceItem_3(2),
    PlaceItem_4(3),

    SetMesos(4),
    SetMesos_2(5),
    SetMesos_3(6),
    SetMesos_4(7),
    Trade(8),
    TradeConfirm(9),
    TradeConfirm2(10),
    TradeConfirm3(11), // 3...?
    TradeConfirmRemoteResponse(14), // what is this even used for

    TradeRestraintItem(15),

    Create(16),

    EnterBase(19),
    EnterResultStatic(20),
    InviteStatic(21),
    InviteResultStatic(22),

    Chat(24),
    Open1(25),
    Open2(26),

    Avatar(27),
    Leave(28),

    CheckSSN2(30),

    OwnerEnterMerchant(31),
    AddItem1(33),
    AddItem2(34),
    AddItem3(35),
    AddItem4(36),
    BuyItem(37),
    BuyItem1(38),
    BuyItem2(39),
    BuyItem3(40),
    RemoveItem(49),
    OwnerLeaveMerchant(50),
    TidyMerchant(51),
    CloseMerchant(52),
    Update(77),
    Open3(80),
    ;

    private byte val;

    MiniRoomAction(int val) {this.val = (byte) val;}
    public byte getVal() {return val;}

    public static MiniRoomAction getByVal(byte val) {
        return Arrays.stream(values()).filter(mra -> mra.getVal() == val).findAny().orElse(null);
    }
}
