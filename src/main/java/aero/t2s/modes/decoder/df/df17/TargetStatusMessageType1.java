package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.*;

public class TargetStatusMessageType1 extends TargetStatusMessage {
    private static final double HEADING_RESOLUTION = 180.0 / 256.0;

    private int subType = 1;
    private SelectedAltitudeSource selectedAltitudeType;
    private int selectedAltitude;
    private double baroSetting;

    private boolean selectedHeadingAvailable;
    private double selectedHeading;

    private SourceIntegrityLevelSupplement SILsupp;
    private SourceIntegrityLevel SIL;
    private NavigationAccuracyCategoryPosition NACp;
    private BarometricAltitudeIntegrityCode NICbaro;

    private boolean autopilot;
    private boolean autopilotLnav;
    private boolean autopilotVnav;
    private boolean autopilotAltitudeHold;
    private boolean autopilotApproach;

    private boolean acasOperation;

    public TargetStatusMessageType1(short[] data) {
        super(data);
    }

    @Override
    public TargetStatusMessageType1 decode() {
        SILsupp = SourceIntegrityLevelSupplement.from(data[4] & 0x1);

        if ((data[9] & 0b00000010) == 0) {
            selectedAltitudeType = SelectedAltitudeSource.UNKNOWN;
        } else if (data[5] >>> 7 == 1) {
            selectedAltitudeType = SelectedAltitudeSource.FMS;
        } else {
            selectedAltitudeType = SelectedAltitudeSource.MCP;
        }

        if (selectedAltitudeType != SelectedAltitudeSource.UNKNOWN) {
            selectedAltitude = (((data[5] & 0x7F) << 4) | (data[6] >>> 4) - 2) * 32;
        }

        baroSetting = ((((data[6] & 0b00001111) << 5) | (data[7] >>> 3)));
        if (baroSetting != 0) {
            baroSetting = (baroSetting - 1) * 0.8 + 800;
        }

        selectedHeadingAvailable = ((data[7] >>> 2) & 0x1) == 1;
        if (selectedHeadingAvailable) {
            boolean isNegativeHeading = ((data[7] >>> 1) & 0x1) == 1;
            selectedHeading = (((data[7] & 0x1) << 7) | (data[8] >>> 1)) * HEADING_RESOLUTION;
            if (isNegativeHeading)
                selectedHeading += 180;
        }

        NACp = NavigationAccuracyCategoryPosition.find(((data[8] & 0b00000001) << 3) | data[9] >>> 5);
        NICbaro = BarometricAltitudeIntegrityCode.from((data[9] >>> 4) & 0x1);
        SIL = SourceIntegrityLevel.from((data[9] >>> 2) & 0x3);

        autopilot = (data[9] & 0x1) == 1;
        autopilotVnav = (data[10] >>> 7) == 1;
        autopilotAltitudeHold = ((data[10] >>> 6) & 0x1) == 1;
        autopilotApproach = ((data[10] >>> 4) & 0x1) == 1;
        acasOperation = ((data[10] >>> 3) & 0x1) == 1;
        autopilotLnav = ((data[10] >>> 2) & 0x1) == 1;


        return this;
    }

    @Override
    public void apply(Track track) {
        track.setSelectedAltitudeManagedFms(selectedAltitudeType == SelectedAltitudeSource.FMS);
        track.setSelectedAltitudeManagedMcp(selectedAltitudeType == SelectedAltitudeSource.MCP);
        track.setSelectedAltitude(selectedAltitudeType != SelectedAltitudeSource.UNKNOWN ? selectedAltitude : 0);

        if (selectedHeadingAvailable) {
            track.setSelectedHeading(selectedHeading);
        }

        track.setSil(SIL.ordinal());
        track.setAutopilot(autopilot);
        track.setVnav(autopilotVnav);
        track.setAltitudeHold(autopilotAltitudeHold);
        track.setApproachMode(autopilotApproach);
        track.getAcas().setActive(acasOperation);
        track.setLnav(autopilotLnav);
    }

    public int getSubType() {
        return subType;
    }

    public SourceIntegrityLevelSupplement getSILsupp() {
        return SILsupp;
    }

    public SelectedAltitudeSource getSelectedAltitudeType() {
        return selectedAltitudeType;
    }

    public int getSelectedAltitude() {
        return selectedAltitude;
    }

    public double getBaroSetting() {
        return baroSetting;
    }

    public boolean isBaroAvailable() {
        return baroSetting != 0;
    }

    public boolean isSelectedHeadingAvailable() {
        return selectedHeadingAvailable;
    }

    public double getSelectedHeading() {
        return selectedHeading;
    }

    public SourceIntegrityLevel getSIL() {
        return SIL;
    }

    public NavigationAccuracyCategoryPosition getNACp() {
        return NACp;
    }

    public BarometricAltitudeIntegrityCode getNICbaro() {
        return NICbaro;
    }

    public boolean isAutopilot() {
        return autopilot;
    }

    public boolean isAutopilotLnav() {
        return autopilotLnav;
    }

    public boolean isAutopilotVnav() {
        return autopilotVnav;
    }

    public boolean isAutopilotAltitudeHold() {
        return autopilotAltitudeHold;
    }

    public boolean isAutopilotApproach() {
        return autopilotApproach;
    }

    public boolean isAcasOperation() {
        return acasOperation;
    }
}
