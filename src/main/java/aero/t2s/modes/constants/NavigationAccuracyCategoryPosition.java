package aero.t2s.modes.constants;

public enum NavigationAccuracyCategoryPosition {
    UNKNOWN("EPU >= 18.52 km (10 NM) - Unknown accuracy"),
    RNP10("EPU < 18.52 km (10 NM) - RNP-10 accuracy"),
    RNP4("EPU < 7.408 km (4 NM) - RNP-4 accuracy"),
    RNP2("EPU < 3.704 km (2 NM) - RNP-2 accuracy"),
    RNP1("EPU < 1852 m (1NM) - RNP-1 accuracy"),
    RNP05("EPU < 926 m (0.5 NM) - RNP-0.5 accuracy"),
    RNP03("EPU < 555.6 m ( 0.3 NM) - RNP-0.3 accuracy"),
    RNP01("EPU < 185.2 m (0.1 NM) - RNP-0.1 accuracy"),
    GPS_SA("EPU < 92.6 m (0.05 NM) - e.g., GPS (with SA)"),
    GPS_NO_SA("EPU < 30m-e.g.,GPS(SA off)"),
    WAAS("EPU < 10m-e.g.,WAAS"),
    LAAS("EPU < 3m-e.g.,LAAS"),
    RESERVED("Reserved"),
    ;

    private String accuracy;

    NavigationAccuracyCategoryPosition(String accuracy) {
        this.accuracy = accuracy;
    }

    public static NavigationAccuracyCategoryPosition find(int accuracy) {
        switch (accuracy) {
            case 0: return UNKNOWN;
            case 1: return RNP10;
            case 2: return RNP4;
            case 3: return RNP2;
            case 4: return RNP1;
            case 5: return RNP05;
            case 6: return RNP03;
            case 7: return RNP01;
            case 8: return GPS_SA;
            case 9: return GPS_NO_SA;
            case 10: return WAAS;
            case 11: return LAAS;
            case 12:
            case 13:
            case 14:
            case 15:
                return RESERVED;
        }

        throw new IllegalArgumentException("Invalid accuracy received " + accuracy);
    }

    @Override
    public String toString() {
        return accuracy;
    }
}
