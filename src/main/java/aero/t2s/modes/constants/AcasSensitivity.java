package aero.t2s.modes.constants;

public enum  AcasSensitivity {
    /**
     * 0 - ACAS inoperative
     */
    INOP,
    /**
     * 1 - ACAS is operating at sensitivity level 1
     */
    LEVEL1,
    /**
     * 2 - ACAS is operating at sensitivity level 2
     */
    LEVEL2,
    /**
     * 3 - ACAS is operating at sensitivity level 3
     */
    LEVEL3,
    /**
     * 4 - ACAS is operating at sensitivity level 4
     */
    LEVEL4,
    /**
     * 5 - ACAS is operating at sensitivity level 5
     */
    LEVEL5,
    /**
     * 6 - ACAS is operating at sensitivity level 6
     */
    LEVEL6,
    /**
     * 7 - ACAS is operating at sensitivity level 7
     */
    LEVEL7,
    ;

    public static AcasSensitivity from(int sensitivity) {
        if (sensitivity > values().length || sensitivity < 0) {
            return INOP;
        }

        return values()[sensitivity];
    }
}
