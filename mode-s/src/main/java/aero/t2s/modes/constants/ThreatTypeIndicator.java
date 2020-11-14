package aero.t2s.modes.constants;

public enum ThreatTypeIndicator {
    NO_ID,
    MODES,
    ALT_BRG_DIST,
    NOT_ASSIGNED,
    ;

    public static ThreatTypeIndicator from(int code) {
        switch (code) {
            case 0: return NO_ID;
            case 1: return MODES;
            case 2: return ALT_BRG_DIST;
            case 3: return NOT_ASSIGNED;
            default: throw new IllegalArgumentException("Threat Type Indicator " + code + " is not valid");
        }
    }
}
