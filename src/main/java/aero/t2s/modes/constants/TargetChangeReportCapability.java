package aero.t2s.modes.constants;

public enum TargetChangeReportCapability {
    NO_CAPABILITY,
    SUPPORT_TC_PLUS_ZERO,
    SUPPORT_MULTIPLE_TC,
    RESERVED
    ;

    public static TargetChangeReportCapability from(int code) {
        switch (code) {
            case 0: return NO_CAPABILITY;
            case 1: return SUPPORT_TC_PLUS_ZERO;
            case 2: return SUPPORT_MULTIPLE_TC;
            case 3: return RESERVED;
            default:
                throw new IllegalArgumentException("Target Change Report Capability " + code + " is not valid");
        }
    }
}
