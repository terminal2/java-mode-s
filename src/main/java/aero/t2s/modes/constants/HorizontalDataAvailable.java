package aero.t2s.modes.constants;

public enum HorizontalDataAvailable {
    NOT_VALID,
    AUTOPILOT,
    HOLD_CURRENT_HEADING_TRACK,
    FMS_RNAV,
    ;

    public static HorizontalDataAvailable from(int code) {
        switch (code) {
            case 0: return NOT_VALID;
            case 1: return AUTOPILOT;
            case 2: return HOLD_CURRENT_HEADING_TRACK;
            case 3: return FMS_RNAV;
            default:
                throw new IllegalArgumentException("Horizon Data Available " + code + " is not valid");
        }
    }
}
