package aero.t2s.modes.constants;

public enum Version {
    VERSION0,
    VERSION1,
    VERSION2,
    ;

    public static Version from(int version) {
        if (version > values().length || version < 0) {
            return VERSION0;
        }

        return values()[version];
    }
}
