package aero.t2s.modes.constants;

public enum SourceIntegrityLevelSupplement {
    PER_HOUR,
    PER_SAMPLE,
    ;

    public static SourceIntegrityLevelSupplement from(int code) {
        switch (code) {
            case 0:
                return PER_HOUR;
            case 1:
                return PER_SAMPLE;
            default:
                throw new IllegalArgumentException("Sil supplement " + code + " is not valid");
        }
    }
}
