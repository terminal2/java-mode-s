package aero.t2s.modes.constants;

public enum TargetAltitudeCapability {
    ALTITUDE_HOLD,
    ALTITUDE_HOLD_OR_MCP,
    ALTITUDE_HOLD_OR_MCP_OR_FMS_RNAV,
    RESERVED,
    ;

    public static TargetAltitudeCapability from(int code) {
        switch (code) {
            case 0: return ALTITUDE_HOLD;
            case 1: return ALTITUDE_HOLD_OR_MCP;
            case 2: return ALTITUDE_HOLD_OR_MCP_OR_FMS_RNAV;
            case 3: return RESERVED;
            default:
                throw new IllegalArgumentException("Target Altitude Capability " + code + " is not valid");
        }
    }
}
