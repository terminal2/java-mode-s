package aero.t2s.modes.constants;

public enum VerticalStatus {
    AIRBORNE,
    GROUND,
    ;

    public static VerticalStatus from(int status) {
        if (status > values().length || status < 0) {
            return null;
        }

        return values()[status];
    }
}
