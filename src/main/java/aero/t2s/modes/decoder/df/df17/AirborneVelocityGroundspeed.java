package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.NavigationAccuracyCategoryVelocity;
import aero.t2s.modes.constants.RocdSource;
import aero.t2s.modes.constants.Version;
import aero.t2s.modes.registers.Register09;
import aero.t2s.modes.registers.Register09V0;

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
            .setVerticalRateSource(isRocdAvailable() ? getRocdSource() : RocdSource.UNKNOWN)
            .setVerticalRate(isRocdAvailable() ? getRocd() : 0)
            .setGnssDifferenceFromBaro(isGnssAltitudeDifferenceFromBaroAvailable() ? getGnssAltitudeDifferenceFromBaro() : 0)
            .setIntentChangeFlag(isIntentChange())
            .setNACv(NavigationAccuracyCategoryVelocity.values()[NACv.ordinal()])
            .validate();

        if (xVelocityAvailable && yVelocityAvailable) {
            track.register09()
                .setVx(xVelocity)
                .setVy(yVelocity);
        }
    }

    public boolean isVxAvailable() {
        return xVelocityAvailable;
    }

    public int getVx() {
        return xVelocity;
    }

    public boolean isVyAvailable() {
        return yVelocityAvailable;
    }

    public int getVy() {
        return yVelocity;
    }
}
