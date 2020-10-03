package aero.t2s.modes.constants;

public enum  EmergencyState {
    NONE,
    EMERGENCY,
    MEDICAL,
    MINIMAL_FUEL,
    NO_COMM,
    HIJACK,
    DOWNED,
    RESERVED
    ;

    public static EmergencyState from(int state) {
        if (state > values().length || state < 0) {
            return NONE;
        }

        return values()[state];
    }
}
