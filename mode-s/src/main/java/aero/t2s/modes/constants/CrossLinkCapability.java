package aero.t2s.modes.constants;

public enum CrossLinkCapability {
    /**
     * 0 - the transponder cannot support the cross-link capability
     */
    UNSUPPORTED,
    /**
     * 1 -  the transponder supports the cross-link capability.
     */
    SUPPORTED,
    ;

    public static CrossLinkCapability from(int coding)
    {
        if (coding == 0) {
            return UNSUPPORTED;
        }

        if (coding == 1) {
            return SUPPORTED;
        }

        return null;
    }
}
