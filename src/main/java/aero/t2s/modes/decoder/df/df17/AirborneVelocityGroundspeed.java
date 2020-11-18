package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.RocdSource;

public class AirborneVelocityGroundspeed extends AirborneVelocity {
    private boolean xVelocityAvailable;
    private int xVelocity;

    private boolean yVelocityAvailable;
    private int yVelocity;

    public AirborneVelocityGroundspeed(short[] data) {
        super(data);
    }

    @Override
    public AirborneVelocityGroundspeed decode() {
        decodeCommonInformation();

        xVelocity = ((data[5] & 0b00000011) << 8) | data[6];
        xVelocityAvailable = xVelocity != 0;
        xVelocity = (xVelocity - 1) * (isSupersonic() ? 4 : 1);
        if ((data[5] & 0b00000100) != 0) {
            xVelocity *= -1;
        }

        yVelocity = ((data[7] & 0b01111111) << 3) | (data[8] & 0b11100000) >>> 5;
        yVelocityAvailable = yVelocity != 0;
        yVelocity = (yVelocity - 1) * (isSupersonic() ? 4 : 1);
        if ((data[7] & 0b10000000) != 0) {
            yVelocity *= -1;
        }


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

        if (xVelocityAvailable) {
            track.setVx(xVelocity);
        }

        if (yVelocityAvailable) {
            track.setVy(yVelocity);
        }

        if (xVelocityAvailable && yVelocityAvailable) {
            track.setGs(Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity));
        }
    }
}
