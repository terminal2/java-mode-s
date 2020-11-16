package aero.t2s.modes.constants;

public enum AcasCapability {
    ACAS_OPERATIONAL_OR_UNKNOWN,
    ACAS_NOT_OPERATIONAL,
    ACAS_NO_RA,
    ACAS_RA,
    ;

    public static AcasCapability from(int code) {
        switch (code) {
            case 0: return ACAS_OPERATIONAL_OR_UNKNOWN;
            case 1: return ACAS_NOT_OPERATIONAL;
            case 2: return ACAS_NO_RA;
            case 3: return ACAS_RA;
            default:
                throw new IllegalArgumentException("ACAS Capability " + code + " is not valid");
        }
    }
}
