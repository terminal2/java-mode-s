package aero.t2s.modes.constants;

public enum HorizontalProtectionLimit {
    RC_7_5(7.5),
    RC_25(25),
    RC_75(75),
    RC_185(185.2),
    RC_370(370.4),
    RC_555(555.6),
    RC_926(926),
    RC_1111(1111.2),
    RC_1852(1852),
    RC_3704(3704),
    RC_7408(7408),
    RC_14816(14816),
    RC_37040(37040),
    RC_UNKNOWN(-1),
    ;

    private final double minAccuracyInMetres;

    HorizontalProtectionLimit(double minAccuracyInMetres) {
        this.minAccuracyInMetres = minAccuracyInMetres;
    }
}
