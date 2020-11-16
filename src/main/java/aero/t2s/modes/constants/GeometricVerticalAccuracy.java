package aero.t2s.modes.constants;

public enum GeometricVerticalAccuracy {
    UNKNOWN("Unknown or > 150m"),
    LESS_THAN_150M("<=1 150M"),
    LESS_THAN_45M("<= 45M"),
    RESERVED("RESERVED (V3 will have a value < 45m)"),
    ;

    private String probability;

    GeometricVerticalAccuracy(String probability) {
        this.probability = probability;
    }

    public static GeometricVerticalAccuracy from(int code) {
        switch (code) {
            case 0: return UNKNOWN;
            case 1: return LESS_THAN_150M;
            case 2: return LESS_THAN_45M;
            case 3: return RESERVED;
            default: throw new IllegalArgumentException("GVA " + code + " is not valid");
        }
    }

    @Override
    public String toString() {
        return probability;
    }
}
