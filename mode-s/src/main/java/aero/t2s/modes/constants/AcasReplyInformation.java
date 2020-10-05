package aero.t2s.modes.constants;

public enum  AcasReplyInformation {
    /**
     * 0 - signifies a reply to an air-air interrogation UF = 0 with AQ = 0, no operating ACAS
     */
    NO_OPERATING_ACAS,
    /**
     * 1 - reserved for ACAS
     */
    RESERVED1,
    /**
     * 2 - reserved for ACAS
     */
    RESERVED2,
    /**
     * 3 - reserved for ACAS
     */
    RESERVED3,
    /**
     * 4 - reserved for ACAS
     */
    RESERVED4,
    /**
     * 5 - reserved for ACAS
     */
    RESERVED5,
    /**
     * 6 - reserved for ACAS
     */
    RESERVED6,
    /**
     * 7 - reserved for ACAS
     */
    RESERVED7,
    /**
     * 8 - no maximum airspeed data available
     */
    NO_MAX_SPEED,
    /**
     * 9 - maximum airspeed is .LE. 140 km/h (75 kt)
     */
    LESS_THAN_75KT(Integer.MIN_VALUE, 75),
    /**
     * 10 - maximum airspeed is .GT. 140 and .LE. 280 km/h (75 and 150 kt)
     */
    GREATER_THAN_75_LESS_THAN_150(75, 150),
    /**
     * 11 - maximum airspeed is .GT. 280 and .LE. 560 km/h (150 and 300 kt)
     */
    GREATER_THAN_150_LESS_THAN_300(150, 300),
    /**
     * 12 - maximum airspeed is .GT. 560 and .LE. 1 110 km/h (300 and 600 kt)
     */
    GREATER_THAN_300_LESS_THAN_600(300, 600),
    /**
     * 13 - maximum airspeed is .GT. 1 110 and .LE. 2 220 km/h (600 and 1 200 kt)
     */
    GREATER_THAN_600_LESS_THAN_1200(600, 1200),
    /**
     * 14 - maximum airspeed is more than 2 220 km/h (1 200 kt)
     */
    GREATER_THAN_1200(1200, Integer.MAX_VALUE),
    /**
     * 15 - not assigned
     */
    NOT_ASSIGNED,
    ;

    private final int lower;
    private final int upper;

    AcasReplyInformation(int lower, int upper) {
        if (lower > upper) {
            upper = lower;
            lower = 0;
        }

        this.lower = lower;
        this.upper = upper;
    }

    AcasReplyInformation() {
        this.lower = 0;
        this.upper = 0;
    }

    public static AcasReplyInformation from(int replyInformation) {
        if (replyInformation > values().length || replyInformation < 0) {
            return NO_OPERATING_ACAS;
        }

        return values()[replyInformation];
    }

    public boolean isMaxAirspeedAvailable() {
        return lower != 0 || upper != 0;
    }

    public int getLessThan() {
        return upper;
    }

    public int getGreaterThan() {
        return lower;
    }
}
