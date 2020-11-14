package aero.t2s.modes.constants;

public class ResolutionAdvisory {
    private boolean requiresCorrectionUpwards;
    private boolean requiresPositiveClimb;
    private boolean requiresCorrectionDownwards;
    private boolean requiresPositiveDescend;
    private boolean requiresCrossing;
    private boolean senseReversal;
    private boolean active;

    public ResolutionAdvisory() {

    }

    public ResolutionAdvisory(int activeResolutionAdvisory) {
        update(activeResolutionAdvisory);
    }

    public void update(ResolutionAdvisory advisory) {
        requiresCorrectionUpwards = advisory.isRequiresCorrectionUpwards();
        requiresPositiveClimb = advisory.isRequiresPositiveClimb();
        requiresCorrectionDownwards = advisory.isRequiresCorrectionDownwards();
        requiresPositiveDescend = advisory.isRequiresPositiveDescend();
        requiresCrossing = advisory.isRequiresCrossing();
        senseReversal = advisory.isSenseReversal();
        active = advisory.isActive();
    }

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

    public boolean isRequiresCorrectionUpwards() {
        return requiresCorrectionUpwards;
    }

    public boolean isRequiresPositiveClimb() {
        return requiresPositiveClimb;
    }

    public boolean isRequiresCorrectionDownwards() {
        return requiresCorrectionDownwards;
    }

    public boolean isRequiresPositiveDescend() {
        return requiresPositiveDescend;
    }

    public boolean isRequiresCrossing() {
        return requiresCrossing;
    }

    public boolean isSenseReversal() {
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
