package aero.t2s.modes.constants;

public enum SurveillanceStatus {
    NO_CONDITION,
    PERMANENT_ALERT,
    TEMPORARY_ALERT,
    SPI,
    ;

    public static SurveillanceStatus from(int code) {
        switch (code) {
            case 0: return NO_CONDITION;
            case 1: return PERMANENT_ALERT;
            case 2: return TEMPORARY_ALERT;
            case 3: return SPI;
            default: throw new IllegalArgumentException("Surveillance Status " + code + " is not valid");
        }
    }
}
