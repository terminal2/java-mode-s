package aero.t2s.modes.constants;

public enum Hazard {
    NIL,
    LIGHT,
    MODERATE,
    SEVERE,
    ;

    public static Hazard find(int source) {
        switch (source) {
            case 0: return NIL;
            case 1: return LIGHT;
            case 2: return MODERATE;
            case 3: return SEVERE;
            default: throw new IllegalArgumentException("Invalid Hazard source " + source);
        }
    }
}
