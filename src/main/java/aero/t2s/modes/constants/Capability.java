package aero.t2s.modes.constants;

public enum  Capability {
    SURVEILLANCE_ONLY,
    RESERVED1,
    RESERVED2,
    RESERVED3,
    LEVEL2_ON_GROUND,
    LEVEL2_AIRBORNE,
    LEVEL2_UNKNOWN,
    CONDITIONS_NOT_MET,
    ;

    public static Capability from(int  capability) {
        switch (capability) {
            case 0: return SURVEILLANCE_ONLY;
            case 1: return RESERVED1;
            case 2: return RESERVED2;
            case 3: return RESERVED3;
            case 4: return LEVEL2_ON_GROUND;
            case 5: return LEVEL2_AIRBORNE;
            case 6: return LEVEL2_UNKNOWN;
            case 7: return CONDITIONS_NOT_MET;
            default:
                throw new IllegalArgumentException("Invalid capability " + capability);
        }
    }
}
