package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.*;

public class TargetStatusMessageType0 extends TargetStatusMessage {
    private int subType = 0;
    private VerticalDataAvailable verticalDataAvailable;
    private TargetAltitudeType targetAltitudeType;
    private TargetAltitudeCapability targetAltitudeCapability;
    private VerticalModeIndicator verticalModeIndicator;
    private int targetAltitude;

    private HorizontalDataAvailable horizontalDataAvailable;
    private int targetHeadingTrack;
    private Angle targetAngle;
    private HorizontalModeIndicator horizontalModeIndicator;

    private NavigationAccuracyCategoryPosition NACp;
    private BarometricAltitudeIntegrityCode NICbaro;
    private SourceIntegrityLevel sil;

    private AcasCapability acasCapability;
    private EmergencyState emergency;

    public TargetStatusMessageType0(short[] data) {
        super(data);
    }

    @Override
    public TargetStatusMessageType0 decode() {
        verticalDataAvailable = VerticalDataAvailable.from(((data[4] & 0b00000001) << 1) | (data[5] >>> 7));
        targetAltitudeType = TargetAltitudeType.from((data[5] & 0b01000000) >>> 6);
        targetAltitudeCapability = TargetAltitudeCapability.from((data[5] & 0b00011000) >>> 3);
        verticalModeIndicator = VerticalModeIndicator.from((data[5] & 0b00000110) >>> 1);

        int targetAltitudeValue = ((data[5] & 0b00000001) << 9) | (data[6] << 1) | ((data[7] & 0b10000000) >>> 7);
        if (targetAltitudeValue < 1011) {
            if (targetAltitudeValue >= 11) {
                targetAltitudeValue -= 1; // 11 = 0ft
            }

            targetAltitude = (-10 + targetAltitudeValue) * 1000;
        }

        horizontalDataAvailable = HorizontalDataAvailable.from((data[7] & 0b01100000) >>> 5);
        targetHeadingTrack = ((data[7] & 0b00011111) << 4) | data[8] & 0b11110000;
        targetAngle = (data[8] & 0b00001000) != 0 ? Angle.TRACK : Angle.HEADING;
        horizontalModeIndicator = HorizontalModeIndicator.from((data[8] & 0b00000110) >>> 1);

        NACp = NavigationAccuracyCategoryPosition.find(((data[8] & 0b00000001) << 3) | (data[9] & 0b11100000) >>> 5);
        NICbaro = BarometricAltitudeIntegrityCode.from((data[9] & 0b00010000) >>> 4);
        sil = SourceIntegrityLevel.from((data[9] & 0b000001100) >>> 2);

        acasCapability = AcasCapability.from((data[10] & 0b00011000) >>> 3);
        emergency = EmergencyState.from((data[10] & 0b00000111));

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setNACp(NACp);

        if (horizontalDataAvailable != HorizontalDataAvailable.NOT_VALID)
            track.setSelectedHeading(targetHeadingTrack);

        if (verticalDataAvailable != VerticalDataAvailable.NOT_VALID)
            track.setSelectedAltitude(targetAltitude);

        track.setEmergencyState(emergency);
        track.setSil(sil.ordinal());
    }

    public int getSubType() {
        return subType;
    }

    public boolean isTargetHeadingTrackValid() {
        return targetHeadingTrack >= 360;
    }

    public VerticalDataAvailable getVerticalDataAvailable() {
        return verticalDataAvailable;
    }

    public TargetAltitudeType getTargetAltitudeType() {
        return targetAltitudeType;
    }

    public TargetAltitudeCapability getTargetAltitudeCapability() {
        return targetAltitudeCapability;
    }

    public VerticalModeIndicator getVerticalModeIndicator() {
        return verticalModeIndicator;
    }

    public int getTargetAltitude() {
        return targetAltitude;
    }

    public HorizontalDataAvailable getHorizontalDataAvailable() {
        return horizontalDataAvailable;
    }

    public int getTargetHeadingTrack() {
        return targetHeadingTrack;
    }

    public Angle getTargetAngle() {
        return targetAngle;
    }

    public HorizontalModeIndicator getHorizontalModeIndicator() {
        return horizontalModeIndicator;
    }

    public NavigationAccuracyCategoryPosition getNACp() {
        return NACp;
    }

    public BarometricAltitudeIntegrityCode getNICbaro() {
        return NICbaro;
    }

    public SourceIntegrityLevel getSil() {
        return sil;
    }

    public AcasCapability getAcasCapability() {
        return acasCapability;
    }

    public EmergencyState getEmergency() {
        return emergency;
    }
}
