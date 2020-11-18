package aero.t2s.modes.constants;

public enum NavigationIntegrityCategory {
    UNKNOWN("Rc unknown"),
    RC_20_NM("Rc < 20 NM (37.04 km)"),
    RC_8_NM("Rc < 8 NM (14.816 km)"),
    RC_4_NM("Rc < 4 NM (7.408 km)"),
    RC_2_NM("Rc < 2 NM (3.704 km)"),
    RC_1_NM("Rc < 1 NM (1852 m)"),
    RC_0_6_NM("Rc < 0.6 NM (1111.2 m)"),
    RC_0_5_NM("Rc < 0.5 NM (926 m)"),
    RC_0_3_NM("Rc < 0.3 NM (555.6 m)"),
    RC_0_2_NM("Rc < 0.2 NM (370.4 m)"),
    RC_0_1_NM("Rc < 0.1 NM (185.2 m)"),
    RC_75_M("Rc < 75m"),
    RC_25_M("Rc < 25m"),
    RC_7_5_M("Rc < 7.5m"),
    RESERVED_1("Reserved"),
    RESERVED_2("Reserved"),
    RESERVED_3("Reserved"),
    RESERVED_4("Reserved"),
    ;

    private String radiusContainment;

    NavigationIntegrityCategory(String radiusContainment) {
        this.radiusContainment = radiusContainment;
    }

    public static NavigationIntegrityCategory airborne(int nic, int nicA, int nicB) {
        switch (nic) {
            case 0: return UNKNOWN;
            case 1: return RC_20_NM;
            case 2: return RC_8_NM;
            case 3: return RC_4_NM;
            case 4: return RC_2_NM;
            case 5: return RC_1_NM;
            case 6:
                if (nicA == 1) {
                    return RC_0_6_NM;
                } else {
                    return nicB == 1 ? RC_0_3_NM : RC_0_5_NM;
                }
            case 7: return RC_0_2_NM;
            case 8: return RC_0_1_NM;
            case 9: return RC_75_M;
            case 10: return RC_25_M;
            case 11: return RC_7_5_M;
            case 12: return RESERVED_1;
            case 13: return RESERVED_2;
            case 14: return RESERVED_3;
            case 15: return RESERVED_4;
            default: return UNKNOWN;
        }
    }

    public static NavigationIntegrityCategory surface(int nic, int nicA) {
        switch (nic) {
            case 0: return UNKNOWN;
            case 6:
                if (nicA == 1) {
                    return RC_0_3_NM;
                } else {
                    return RC_0_6_NM;
                }
            case 7: return RC_0_2_NM;
            case 8: return RC_0_1_NM;
            case 9: return RC_75_M;
            case 10: return RC_25_M;
            case 11: return RC_7_5_M;
            case 12: return RESERVED_1;
            case 13: return RESERVED_2;
            case 14: return RESERVED_3;
            case 15: return RESERVED_4;
            default: return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return radiusContainment;
    }
}
