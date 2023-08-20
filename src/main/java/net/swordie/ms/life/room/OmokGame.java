package net.swordie.ms.life.room;

import net.swordie.ms.util.container.Triple;

import java.util.ArrayList;
import java.util.List;

public class OmokGame {
    private static final int BOARD_SIZE = 15;
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private List<Triple<Integer, Integer, Integer>> history = new ArrayList<>(); // x, y, type

    public OmokGame() {
    }

    public OmokCheckResult tryPutStone(int x, int y, int type) {
        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE || board[x][y] != 0) {
            return OmokCheckResult.ILLEGAL;
        }
        // double three is not allowed unless it blocks a threat
        if (checkDoubleThree(x, y, type) && !checkWin(x, y, type == 1 ? 2 : 1)) {
            return OmokCheckResult.DOUBLE_THREE;
        }
        // place stone
        board[x][y] = type;
        history.add(new Triple<>(x, y, type));
        if (checkWin(x, y, type)) {
            return OmokCheckResult.WIN;
        }
        return OmokCheckResult.OK;
    }

    public int tryRetreat() {
        int count;
        for (count = 0; count < 2; count++) {
            if (history.isEmpty()) {
                break;
            }
            var lastMove = history.remove(history.size() - 1);
            board[lastMove.getLeft()][lastMove.getMiddle()] = 0;
        }
        return count;
    }

    private boolean checkWin(int x, int y, int type) {
        return checkHorizontal(x, y, type, false) == 5 || checkVertical(x, y, type, false) == 5 ||
                checkDiagonalTLBR(x, y, type, false) == 5 || checkDiagonalBLTR(x, y, type, false) == 5;
    }

    private boolean checkDoubleThree(int x, int y, int type) {
        int length;
        int threeCount = 0;

        length = checkHorizontal(x, y, type, true);
        if (length > 3) {
            return false;
        } else if (length == 3) {
            threeCount++;
        }

        length = checkVertical(x, y, type, true);
        if (length > 3) {
            return false;
        } else if (length == 3) {
            threeCount++;
        }

        length = checkDiagonalTLBR(x, y, type, true);
        if (length > 3) {
            return false;
        } else if (length == 3) {
            threeCount++;
        }

        length = checkDiagonalBLTR(x, y, type, true);
        if (length > 3) {
            return false;
        } else if (length == 3) {
            threeCount++;
        }

        return threeCount >= 2;
    }

    private int checkHorizontal(int x, int y, int type, boolean checkOpen) {
        int i;
        int left = 0;
        for (i = x - 1; i >= 0; i--) {
            if (board[i][y] == type) {
                left++;
            } else {
                break;
            }
        }
        if (checkOpen && (i < 0 || board[i][y] != 0)) {
            return -1;
        }
        int right = 0;
        for (i = x + 1; i < BOARD_SIZE; i++) {
            if (board[i][y] == type) {
                right++;
            } else {
                break;
            }
        }
        if (checkOpen && (i >= BOARD_SIZE || board[i][y] != 0)) {
            return -1;
        }
        return left + right + 1;
    }

    private int checkVertical(int x, int y, int type, boolean checkOpen) {
        int j;
        int top = 0;
        for (j = y - 1; j >= 0; j--) {
            if (board[x][j] == type) {
                top++;
            } else {
                break;
            }
        }
        if (checkOpen && (j < 0 || board[x][j] != 0)) {
            return -1;
        }
        int bottom = 0;
        for (j = y + 1; j < BOARD_SIZE; j++) {
            if (board[x][j] == type) {
                top++;
            } else {
                break;
            }
        }
        if (checkOpen && (j >= BOARD_SIZE || board[x][j] != 0)) {
            return -1;
        }
        return top + bottom + 1;
    }

    private int checkDiagonalTLBR(int x, int y, int type, boolean checkOpen) {
        int k;
        int topLeft = 0;
        for (k = 1; (x - k >= 0 && y - k >= 0); k++) {
            if (board[x - k][y - k] == type) {
                topLeft++;
            } else {
                break;
            }
        }
        if (checkOpen && (x - k < 0 || y - k < 0 || board[x - k][y - k] != 0)) {
            return -1;
        }
        int bottomRight = 0;
        for (k = 1; (x + k < BOARD_SIZE && y + k < BOARD_SIZE); k++) {
            if (board[x + k][y + k] == type) {
                bottomRight++;
            } else {
                break;
            }
        }
        if (checkOpen && (x + k >= BOARD_SIZE || y + k >= BOARD_SIZE || board[x + k][y + k] != 0)) {
            return -1;
        }
        return topLeft + bottomRight + 1;
    }

    private int checkDiagonalBLTR(int x, int y, int type, boolean checkOpen) {
        int k;
        int bottomLeft = 0;
        for (k = 1; (x - k >= 0 && y + k < BOARD_SIZE); k++) {
            if (board[x - k][y + k] == type) {
                bottomLeft++;
            } else {
                break;
            }
        }
        if (checkOpen && (x - k < 0 || y + k >= BOARD_SIZE || board[x - k][y + k] != 0)) {
            return -1;
        }
        int topRight = 0;
        for (k = 1; (x + k < BOARD_SIZE && y - k >= 0); k++) {
            if (board[x + k][y - k] == type) {
                topRight++;
            } else {
                break;
            }
        }
        if (checkOpen && (x + k >= BOARD_SIZE || y - k < 0 || board[x + k][y - k] != 0)) {
            return -1;
        }
        return bottomLeft + topRight + 1;
    }


    public static enum OmokCheckResult {
        OK(0),
        WIN(1),
        DOUBLE_THREE(104), // this will print a special message
        ILLEGAL(-1);

        private final int val;

        OmokCheckResult(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }
}
