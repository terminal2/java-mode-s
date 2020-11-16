package aero.t2s.modes.decoder.df.df17.data;

import aero.t2s.modes.constants.AcasState;
import aero.t2s.modes.constants.SourceIntegrityLevel;

public class SurfaceOperationalMode {
    private AcasState acasRA;
    private boolean acasIdent;
    private boolean singleAntennaFlag;
    private SourceIntegrityLevel systemDesignAssurance;

    private boolean gpsLateralOffsetAvailable;
    /**
     * Negative is left of center in metres
     * Positive is right of center in metres
     */
    private int gpsLateralOffset;

    private boolean gpsLongitudinalOffsetAvailable;
    private boolean gpsLongitudinalOffsetAppliedBySensor;
    /**
     * Aft of aircraft nose in metres
     */
    private int gpsLongitudinalOffset;

    public SurfaceOperationalMode(int data) {
        acasRA = (data & 0b0010000000000000) != 0 ? AcasState.RA_ACTIVE : AcasState.RA_NOT_ACTIVE;
        acasIdent = (data & 0b0001000000000000) != 0;
        singleAntennaFlag = (data & 0b0000010000000000) != 0;
        systemDesignAssurance = SourceIntegrityLevel.from((data & 0b0000001100000000) >>> 7);
        int gpsAntennaOffset = (data & 0b0000000011111111);

        gpsLateralOffset = (gpsAntennaOffset & 0b11100000) >>> 5;
        if ((gpsLateralOffset & 0b100) == 0) {
            gpsLateralOffset = (gpsLateralOffset & 0b011) * 2;
            gpsLateralOffsetAvailable = gpsLateralOffset != 0;
        } else {
            gpsLateralOffset = (gpsLateralOffset & 0b011) * -2;
            gpsLateralOffsetAvailable = true;
        }

        gpsLongitudinalOffset = gpsAntennaOffset & 0b00011111;
        gpsLongitudinalOffsetAvailable = gpsLongitudinalOffset != 0;
        gpsLongitudinalOffsetAppliedBySensor = gpsLongitudinalOffset == 1;
        gpsLongitudinalOffset = (gpsLongitudinalOffset - 1) * 2;
    }

    public AcasState getAcasRA() {
        return acasRA;
    }

    public boolean isAcasIdent() {
        return acasIdent;
    }

    public boolean isSingleAntennaFlag() {
        return singleAntennaFlag;
    }

    public SourceIntegrityLevel getSystemDesignAssurance() {
        return systemDesignAssurance;
    }

    public boolean isGpsLateralOffsetAvailable() {
        return gpsLateralOffsetAvailable;
    }

    public int getGpsLateralOffset() {
        return gpsLateralOffset;
    }

    public boolean isGpsLongitudinalOffsetAvailable() {
        return gpsLongitudinalOffsetAvailable;
    }

    public boolean isGpsLongitudinalOffsetAppliedBySensor() {
        return gpsLongitudinalOffsetAppliedBySensor;
    }

    public int getGpsLongitudinalOffset() {
        return gpsLongitudinalOffset;
    }
}
