package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.NavigationUncertaintyCategory;
import aero.t2s.modes.constants.RocdSource;

public class AirborneVelocity extends ExtendedSquitter {
    protected NavigationUncertaintyCategory NACv;
    private int subType;
    private boolean supersonic;
    private boolean intentChange;
    private boolean ifrCapability;
    private boolean rocdAvailable;
    private RocdSource rocdSource;
    private int rocd;
    private boolean gnssAltitudeDifferenceFromBaroAvailable;
    private int gnssAltitudeDifferenceFromBaro;

    public AirborneVelocity(short[] data) {
        super(data);
    }

    @Override
    public AirborneVelocity decode() {
        int subType = data[4] & 0x7;

        switch (subType) {
            case 1:
            case 2:
                return new AirborneVelocityGroundspeed(data).decode();
            case 3:
            case 4:
                return new AirborneVelocityAirspeedHeading(data).decode();
            default:
                throw new NotImplementedException("BDS0,9 sub type " + subType + " is not implemented");
        }
    }

    @Override
    public void apply(Track track) {
        // Nothing
    }

    protected void decodeCommonInformation() {
        subType = data[4] & 0b00000111;
        supersonic = subType == 2;
        intentChange = (data[5] & 0b10000000) != 0;
        ifrCapability = (data[5] & 0b01000000) != 0;

        NACv = NavigationUncertaintyCategory.from((data[5] & 0b00111000) >>> 3);

        rocdSource = (data[8] & 0b00010000) != 0 ? RocdSource.BARO : RocdSource.GNSS;
        rocd = ((data[8] & 0b00000111) << 6) | (data[9] & 0b11111100) >>> 2;
        rocdAvailable = rocd != 0;
        rocd = (rocd - 1) * 64;
        if ((data[8] & 0b00001000) != 0) {
            rocd *= -1;
        }

        gnssAltitudeDifferenceFromBaro = data[10] * 0b01111111;
        gnssAltitudeDifferenceFromBaroAvailable = gnssAltitudeDifferenceFromBaro != 0;
        gnssAltitudeDifferenceFromBaro = (gnssAltitudeDifferenceFromBaro - 1) * 25;
        if ((data[10] & 0b10000000) != 0) {
            gnssAltitudeDifferenceFromBaro *= -1;
        }
    }

    public NavigationUncertaintyCategory getNACv() {
        return NACv;
    }

    public int getSubType() {
        return subType;
    }

    public boolean isSupersonic() {
        return supersonic;
    }

    public boolean isIntentChange() {
        return intentChange;
    }

    public boolean isIfrCapability() {
        return ifrCapability;
    }

    public boolean isRocdAvailable() {
        return rocdAvailable;
    }

    public RocdSource getRocdSource() {
        return rocdSource;
    }

    public int getRocd() {
        return rocd;
    }

    public boolean isGnssAltitudeDifferenceFromBaroAvailable() {
        return gnssAltitudeDifferenceFromBaroAvailable;
    }

    public int getGnssAltitudeDifferenceFromBaro() {
        return gnssAltitudeDifferenceFromBaro;
    }
}
