package aero.t2s.modes.constants;

public class ResolutionAdvisory {
    private boolean requiresCorrectionUpwards;
    private boolean requiresPositiveClimb;
    private boolean requiresCorrectionDownwards;
    private boolean requiresPositiveDescend;
    private boolean requiresCrossing;
    private boolean senseReversal;
    private boolean active;

    public void update(int ara) {
        requiresCorrectionUpwards = ara >>> 12 == 1;
        requiresPositiveClimb = ara >>> 11 == 1;
        requiresCorrectionDownwards = ara >>> 10 == 1;
        requiresPositiveDescend = ara >>> 9 == 1;
        requiresCrossing = ara >>> 8 == 1;
        senseReversal = ara >>> 7 == 1;

        if (!active && ara >>> 13 == 1) {
           active = true;
        }
    }

    public void clear() {
        requiresCorrectionUpwards = false;
        requiresPositiveClimb = false;
        requiresCorrectionDownwards = false;
        requiresPositiveDescend = false;
        requiresCrossing = false;
        senseReversal = false;
        active = false;
    }

    public boolean requiresCorrectionUpwards() {
        return requiresCorrectionUpwards;
    }

    public boolean ruquiresPositiveClimb() {
        return requiresPositiveClimb;
    }

    public boolean requiresCorrectionDownwards() {
        return requiresCorrectionDownwards;
    }

    public boolean requiresPositiveDescend() {
        return requiresPositiveDescend;
    }

    public boolean requiresCrossing() {
        return requiresCrossing;
    }

    public boolean senseReversal() {
        return senseReversal;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        String out = "\n";

        if (requiresCorrectionUpwards) {
            out += " - RA requires a correction in the upward sense";
        }

        if (requiresPositiveClimb) {
            out += " - RA requires a positive climb";
        }

        if (requiresCorrectionDownwards) {
            out += " - RA requires a correction in the downward sense";
        }

        if (requiresPositiveDescend) {
            out += " - RA requires a positive descend";
        }

        if (requiresCrossing) {
            out += " - RA requires a crossing";
        }

        if (senseReversal) {
            out += " - RA is a sense reversal";
        }

        return out;
    }

}
