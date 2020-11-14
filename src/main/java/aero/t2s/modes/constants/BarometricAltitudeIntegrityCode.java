package aero.t2s.modes.constants;

public enum BarometricAltitudeIntegrityCode {
    NOT_CROSS_CHECKED,
    CROSS_CHECKED,
    ;

    public static BarometricAltitudeIntegrityCode from(int code) {
        switch (code) {
            case 0:
                return NOT_CROSS_CHECKED;
            case 1:
                return CROSS_CHECKED;
            default:
                throw new IllegalArgumentException("NICbaro " + code + " is not valid");
        }
    }
}
