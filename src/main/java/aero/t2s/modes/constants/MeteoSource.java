package aero.t2s.modes.constants;

public enum MeteoSource {
    INVALID,
    INS,
    GNSS,
    DME_DME,
    VOR_DME,
    RESERVED,
    ;

    public static MeteoSource find(int source) {
        switch (source) {
            case 0:
                return INVALID;
            case 1:
                return INS;
            case 2:
                return GNSS;
            case 3:
                return DME_DME;
            case 4:
                return VOR_DME;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return RESERVED;
            default:
                throw new IllegalArgumentException("Invalid source " + source);
        }
    }
}
