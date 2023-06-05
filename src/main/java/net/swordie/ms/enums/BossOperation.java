package net.swordie.ms.enums;

public enum BossOperation {

    BALROG(1), // Requires Quest: 2241
    ZAKUM_EASY(2),
    ZAKUM(3),
    ZAKUM_CHAOS(4),
    URSUS(999), // Requires Quest: 33565 // Not handled here apparently.

    MAGNUS_EASY(21),
    MAGNUS(22), // Requires Quest: 31833
    MAGNUS_HARD(23),
    HILLA(7),
    HILLA_HARD(8),
    PIERRE(9), // Requires Quest: 30007
    PIERRE_CHAOS(10),
    VONBON(11),
    VONBON_CHAOS(12),
    CRIMSONQUEEN(13),
    CRIMSONQUEEN_CHAOS(14),
    VELLUM(15),
    VELLUM_CHAOS(16),
    VONLEON_EASY(999), // Requires Quest: 3157
    VONLEON(999),
    HORNTAIL_EASY(27), // Requires Quest : 7313
    HORNTAIL(5),
    HORNTAIL_CHAOS(6),
    ARKARIUM_EASY(19), // Requires Quest : 31179
    ARKARIUM(20),
    PINKBEAN(24), // Requires Quest: 3521
    PINKBEAN_CHAOS(25),
    CYGNUS_EASY(999),// Not handled here apparently.

    CYGNUS(26), // Requires Quest: 31152
    LOTUS(29), // Requires Quest: 33294
    LOTUS_HARD(28),
    DAMIEN(32), // Requires Quest: 34015
    DAMIEN_HARD(33),
    GOLLUX(103), // Requires Quest: 17523
    RANMARU(104),
    RANMARU_HARD(109),
    PRINCESSNO(105), // Requires Quest: 58955
    LUCID(34), // Requires Quest: 34330

    NOT_FOUND(-2);

    private final int val;

    private BossOperation(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static BossOperation getByVal(int val) {
        for (BossOperation nIndex : values()) {
            if (nIndex.getVal() == val) {
                return nIndex;
            }
        }
        return NOT_FOUND;
    }
}
