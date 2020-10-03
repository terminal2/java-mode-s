package aero.t2s.modes.constants;

public enum  AcasSensitivity {
    INOP,
    LEVEL1,
    LEVEL2,
    LEVEL3,
    LEVEL4,
    LEVEL5,
    LEVEL6,
    LEVEL7,
    ;

    public static AcasSensitivity from(int sensitivity) {
        if (sensitivity > values().length || sensitivity < 0) {
            return INOP;
        }

        return values()[sensitivity];
    }
}
