package aero.t2s.modes.constants;

public enum SelectedAltitudeSource {
    /**
     * 00 = Unknown - no information on the source of the information is available.
     */
    UNKNOWN,
    /**
     * 01 = Aircraft Altitude - could indicate the aircraft is not equipped with an autopilot or the flight directors
     * are disabled, manual flying.
     */
    AIRCRAFT,
    /**
     * 10 = MCP/FCU managed altitude - the aircraft is controlling the selected altitude through the MCP/FCU on the autopilot
     */
    MCP,
    /**
     * 11 = FMS managed selected altitude - The FMS is control of the selected altitude
     */
    FMS,
    ;

    public static SelectedAltitudeSource find(int source) {
        switch (source) {
            case 0:
                return UNKNOWN;
            case 1:
                return AIRCRAFT;
            case 2:
                return MCP;
            case 3:
                return FMS;
            default:
                throw new IllegalArgumentException("Unsupported value " + source);
        }
    }
}
