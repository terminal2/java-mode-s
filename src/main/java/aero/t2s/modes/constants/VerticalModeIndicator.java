package aero.t2s.modes.constants;

public enum VerticalModeIndicator {
    UNKNOWN,
    ACQUIRE,
    CAPTURE_MAINTAIN,
    RESERVED,
    ;

    public static VerticalModeIndicator from(int code) {
        switch (code) {
            case 0: return UNKNOWN;
            case 1: return ACQUIRE;
            case 2: return CAPTURE_MAINTAIN;
            case 3: return RESERVED;
            default:
                throw new IllegalArgumentException("Vertical Mode Indicator " + code + " is not valid");
        }
    }
}
