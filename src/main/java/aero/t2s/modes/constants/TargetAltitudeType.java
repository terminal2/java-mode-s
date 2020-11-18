package aero.t2s.modes.constants;

public enum TargetAltitudeType {
    FL,
    MSL,
    ;

    public static TargetAltitudeType from(int code) {
        switch (code) {
            case 0: return FL;
            case 1: return MSL;
            default:
                throw new IllegalArgumentException("Target Altitude Type " + code + " is not valid");
        }
    }
}
