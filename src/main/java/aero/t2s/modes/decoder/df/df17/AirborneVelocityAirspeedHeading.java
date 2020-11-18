package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.RocdSource;
import aero.t2s.modes.constants.Speed;

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
        track.setNACv(NACv.ordinal());

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
