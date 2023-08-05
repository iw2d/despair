package net.swordie.ms.enums;

import java.util.Arrays;

public enum MiniRoomType {
    // CMiniRoomBaseDlg::MiniRoomFactory(int nType) (0x00BB02A0)
    OMOK(1),
    MEMORY_GAME(2),
    BATTLE_RPS(3),
    TRADING_ROOM(4),
    PERSONAL_SHOP(5),
    ENTRUSTED_SHOP(6),
    CASH_TRADING_ROOM(7),
    WEDDING_EX(8),
    CANDY_TRADING_ROOM(9),
    MULTI_YUT_GAME_NOUSESKILL(10),
    SIGN_ROOM(11),
    TENTH_ANNI_BOARD_GAME(12),
    BINGO_GAME_MULTI(13),
    OMOK_RENEWAL(14),
    MEMORY_GAME_2013(15),
    ONE_CARD_GAME_ROOM(16),
    MULTI_YUT_GAME_USESKILL(17),
    RUNNER_MINI_GAME(18);

    private byte val;

    MiniRoomType(int val) {this.val = (byte) val;}
    public byte getVal() {return val;}

    public static MiniRoomType getByVal(byte val) {
        return Arrays.stream(values()).filter(mrt -> mrt.getVal() == val).findAny().orElse(null);
    }
}
