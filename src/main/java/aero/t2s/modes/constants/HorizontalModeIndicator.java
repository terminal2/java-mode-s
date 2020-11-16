package aero.t2s.modes.constants;

public enum HorizontalModeIndicator {
    UNKNOWN,
    ACQUIRE,
    CAPTURE_MAINTAIN,
    RESERVED,
    ;

    public static HorizontalModeIndicator from(int code) {
        switch (code) {
            case 0: return UNKNOWN;
            case 1: return ACQUIRE;
            case 2: return CAPTURE_MAINTAIN;
            case 3: return RESERVED;
            default:
                throw new IllegalArgumentException("Horizontal Mode Indicator " + code + " is not valid");
        }
    }
}
