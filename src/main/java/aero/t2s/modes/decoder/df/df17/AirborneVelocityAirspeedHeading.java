package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.registers.Register09;
import aero.t2s.modes.registers.Register09V0;

public class AirborneVelocityAirspeedHeading extends AirborneVelocity {
    private static final double HEADING_RESOLUTION = 360.0 / 1024.0;

    private boolean headingAvailable;
    private double heading;

    private boolean airspeedAvailable;
    private Speed airspeedType;
    private int airspeed;

    public AirborneVelocityAirspeedHeading(short[] data) {
        super(data);
    }

    @Override
    public AirborneVelocityAirspeedHeading decode() {
        decodeCommonInformation();

        headingAvailable = (data[5] & 0b00000100) != 0;
        heading = (((data[5] & 0b00000011) << 8) | data[6]) * HEADING_RESOLUTION;

        airspeed = ((data[7] & 0b01111111) << 3) | (data[8] & 0b11100000) >>> 5;
        airspeedAvailable = airspeed != 0;
        airspeed = (airspeed - 1) * (isSupersonic() ? 4 : 1);
        airspeedType = ((data[7] & 0b10000000) != 0) ? Speed.TAS : Speed.IAS;

        return this;
    }

    @Override
    public void apply(Track track) {
        if (isGnssAltitudeDifferenceFromBaroAvailable()) {
            track.setGeometricHeightOffset(getGnssAltitudeDifferenceFromBaro());
        }

        if (isRocdAvailable()) {
            track.setRocdAvailable(true);
            track.setRocdSourceBaro(getRocdSource() == RocdSource.BARO);
            if (getRocdSource() == RocdSource.BARO) {
                track.setBaroRocd(getRocd());
            } else {
                track.setRocd(getRocd());
            }
        }


        if (track.getVersion().equals(Version.VERSION2) && track.register09() instanceof Register09V0) {
            track.register09(new Register09V0());
        }
        if (!track.getVersion().equals(Version.VERSION2) && !(track.register09() instanceof Register09V0)) {
            track.register09(new Register09());
        }

        if (track.register09() instanceof Register09V0) {
            ((Register09V0) track.register09()).setIfrCapability(isIfrCapability());
        }

        track.register09()
            .setHeadingSource(isHeadingAvailable() ? Angle.HEADING : Angle.UNAVAILABLE)
            .setHeading((int) Math.round(heading))
            .setAirspeedSource(isAirspeedAvailable() ? airspeedType : Speed.UNKNOWN)
            .setAirspeed(airspeed)
            .setVerticalRateSource(isRocdAvailable() ? getRocdSource() : RocdSource.UNKNOWN)
            .setVerticalRate(isRocdAvailable() ? getRocd() : 0)
            .setGnssDifferenceFromBaro(isGnssAltitudeDifferenceFromBaroAvailable() ? getGnssAltitudeDifferenceFromBaro() : 0)
            .setIntentChangeFlag(isIntentChange())
            .setNACv(NavigationAccuracyCategoryVelocity.values()[NACv.ordinal()])
            .validate();
        ;
    }

    public boolean isHeadingAvailable() {
        return headingAvailable;
    }

    public double getHeading() {
        return heading;
    }

    public boolean isAirspeedAvailable() {
        return airspeedAvailable;
    }

    public Speed getAirspeedType() {
        return airspeedType;
    }

    public int getAirspeed() {
        return airspeed;
    }
}
