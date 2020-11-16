package aero.t2s.modes.constants;

public enum VerticalDataAvailable {
    NOT_VALID,
    AUTOPILOT,
    HOLDING_ALTITUDE,
    FMS_RNAV,
    ;

    public static VerticalDataAvailable from(int code) {
        switch (code) {
            case 0: return NOT_VALID;
            case 1: return AUTOPILOT;
            case 2: return HOLDING_ALTITUDE;
            case 3: return FMS_RNAV;
            default:
                throw new IllegalArgumentException("Vertical Data Available " + code + " is not valid");
        }
    }
}
