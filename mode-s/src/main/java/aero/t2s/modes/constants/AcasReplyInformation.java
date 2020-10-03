package aero.t2s.modes.constants;

public enum  AcasReplyInformation {
    NO_OPERATING_ACAS,
    RESERVED1,
    RESERVED2,
    RESERVED3,
    RESERVED4,
    RESERVED5,
    RESERVED6,
    RESERVED7,
    NO_MAX_SPEED,
    LESS_THAN_75KT(Integer.MIN_VALUE, 75),
    GREATER_THAN_75_LESS_THAN_150(75, 150),
    GREATER_THAN_150_LESS_THAN_300(150, 300),
    GREATER_THAN_300_LESS_THAN_600(300, 600),
    GREATER_THAN_600_LESS_THAN_1200(600, 1200),
    GREATER_THAN_1200(1200, Integer.MAX_VALUE),
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
