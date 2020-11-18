package aero.t2s.modes.constants;

public enum VerticalStatus {
    /**
     * 0 - The aircraft is airborne
     */
    AIRBORNE,
    /**
     * 1 - The aircraft is on the ground
     */
    GROUND,
    ;

    public static VerticalStatus from(int status) {
        if (status > values().length || status < 0) {
            return null;
        }

        return values()[status];
    }
}
