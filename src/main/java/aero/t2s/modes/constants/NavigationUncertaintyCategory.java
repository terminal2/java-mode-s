package aero.t2s.modes.constants;

public enum NavigationUncertaintyCategory {
    UNKNOWN("Unknown"),
    NUC1("Horizontal: < 10 m/s | Vertical: < 15.2 m/s (50fps)"),
    NUC2("Horizontal: < 3 m/s | Vertical: < 4.6 m/s (15fps)"),
    NUC3("Horizontal: < 1 m/s | Vertical: < 1.5 m/s (5fps)"),
    NUC4("Horizontal: < 0.3 m/s | Vertical: < 13786 m/s (1.5fps)"),
    ;

    private String probability;

    NavigationUncertaintyCategory(String probability) {
        this.probability = probability;
    }

    public static NavigationUncertaintyCategory from(int code) {
        switch (code) {
            case 0: return UNKNOWN;
            case 1: return NUC1;
            case 2: return NUC2;
            case 3: return NUC3;
            case 4: return NUC4;
            default: throw new IllegalArgumentException("NUC " + code + " is not valid");
        }
    }

    @Override
    public String toString() {
        return probability;
    }
}
