package net.swordie.ms.life.room;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OmokGameTest {

    @Test
    void checkOk() {
        OmokGame omokGame = new OmokGame();
        assertEquals(omokGame.tryPutStone(0, 0, 1), OmokGame.OmokCheckResult.OK);
        assertEquals(omokGame.tryPutStone(14, 14, 1), OmokGame.OmokCheckResult.OK);
    }


    @Test
    void checkIllegal() {
        OmokGame omokGame = new OmokGame();
        // out of bounds
        assertEquals(omokGame.tryPutStone(-1, -1, 1), OmokGame.OmokCheckResult.ILLEGAL);
        assertEquals(omokGame.tryPutStone(15, 15, 1), OmokGame.OmokCheckResult.ILLEGAL);
        // already placed
        omokGame.tryPutStone(0, 0, 1);
        assertEquals(omokGame.tryPutStone(0, 0, 1), OmokGame.OmokCheckResult.ILLEGAL);
    }

    @Test
    void checkDoubleThree() {
        OmokGame omokGame = new OmokGame();
        omokGame.tryPutStone(4, 5, 1);
        omokGame.tryPutStone(6, 5, 1);
        omokGame.tryPutStone(5, 4, 1);
        omokGame.tryPutStone(5, 6, 1);
        assertEquals(omokGame.tryPutStone(5, 5, 1), OmokGame.OmokCheckResult.DOUBLE_THREE);

        omokGame = new OmokGame();
        // same as before, but 1 is forced to block 5, 5
        omokGame.tryPutStone(4, 5, 1);
        omokGame.tryPutStone(6, 5, 1);
        omokGame.tryPutStone(5, 4, 1);
        omokGame.tryPutStone(5, 6, 1);

        omokGame.tryPutStone(3, 5, 2);
        omokGame.tryPutStone(4, 5, 2);
        omokGame.tryPutStone(6, 5, 2);
        omokGame.tryPutStone(7, 5, 2);
        assertEquals(omokGame.tryPutStone(5, 5, 1), OmokGame.OmokCheckResult.OK);
    }

    @Test
    void checkSix() {
        OmokGame omokGame = new OmokGame();
        omokGame.tryPutStone(5, 5, 1);
        omokGame.tryPutStone(6, 5, 1);
        // 7, 5 for 6 in a row, which does not count as a win
        omokGame.tryPutStone(8, 5, 1);
        omokGame.tryPutStone(9, 5, 1);
        omokGame.tryPutStone(10, 5, 1);
        assertEquals(omokGame.tryPutStone(7, 5, 1), OmokGame.OmokCheckResult.OK);
    }

    @Test
    void checkWin() {
        OmokGame omokGame = new OmokGame();
        omokGame.tryPutStone(5, 5, 1);
        omokGame.tryPutStone(6, 5, 1);
        // 7, 5 for 5 in a row
        omokGame.tryPutStone(8, 5, 1);
        omokGame.tryPutStone(9, 5, 1);
        assertEquals(omokGame.tryPutStone(7, 5, 1), OmokGame.OmokCheckResult.WIN);

        omokGame = new OmokGame();
        omokGame.tryPutStone(1, 1, 1);
        omokGame.tryPutStone(2, 2, 1);
        // 3, 3 for 5 in a row
        omokGame.tryPutStone(4, 4, 1);
        omokGame.tryPutStone(5, 5, 1);
        assertEquals(omokGame.tryPutStone(3, 3, 1), OmokGame.OmokCheckResult.WIN);
    }
}