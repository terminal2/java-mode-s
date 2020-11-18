package aero.t2s.modes.constants;

public enum SourceIntegrityLevel {
    UNKNOWN("Unknown or >1 x 10e-3"),
    LESS_THEN_ONE_PER_THOUSAND("<=1 x 10e-3"),
    LESS_THEN_ONE_PER_HUNDRED_THOUSAND("<=1 x 10e-5"),
    LESS_THEN_ONE_PER_TEN_MILLION("<=1 x 10e-7"),
    ;

    private String probability;

    SourceIntegrityLevel(String probability) {
        this.probability = probability;
    }

    public static SourceIntegrityLevel from(int code) {
        switch (code) {
            case 0: return UNKNOWN;
            case 1: return LESS_THEN_ONE_PER_THOUSAND;
            case 2: return LESS_THEN_ONE_PER_HUNDRED_THOUSAND;
            case 3: return LESS_THEN_ONE_PER_TEN_MILLION;
            default: throw new IllegalArgumentException("Sil supplement " + code + " is not valid");
        }
    }

    @Override
    public String toString() {
        return probability;
    }
}
