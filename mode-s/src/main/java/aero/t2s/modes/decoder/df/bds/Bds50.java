package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds50 extends Bds {
    private static final double ROLE_ACCURACY = 45.0 / 256.0;
    private static final double TRUE_TRACK_ANGLE_ACCURACY = 90.0 / 512.0;
    private static final double GS_ACCURACY = 1024.0 / 512.0;
    private static final double TRUE_TRACK_RATE_ACCURACY = 8.0 / 256.0;

    @Override
    public boolean attemptDecode(Track track, short[] data) {
        boolean statusRollAngle = ((data[4] >>> 7) & 0x1) == 1;
        boolean statusTrackAngle = ((data[5] >>> 4) & 0x1) == 1;
        boolean statusGroundSpeed = (data[6] & 0x1) == 1;
        boolean statusTrueAngleRate = ((data[8] >>> 5) & 0x1) == 1;
        boolean statusTrueAirspeed = ((data[9] >>> 2) & 0x1) == 1;

        if (! (statusRollAngle && statusTrackAngle && statusGroundSpeed && statusTrueAngleRate && statusTrueAirspeed)) {
            return false;
        }

        double leftWingDown = ((data[4] >>> 6) & 0x1) == 1 ? -1.0 : 1.0;
        double rollAngle = (((data[4] & 0x3F) << 3) | (data[6] >>> 5)) * ROLE_ACCURACY * leftWingDown;
        if (rollAngle < -45 || rollAngle > 45) {
            return false;
        }

        double west = ((data[4] >>> 3) & 0x1) == 1 ? -1.0 : 1.0;
        double trueTrackAngle = 360 + ((((((data[5] & 0x7) << 7) | (data[6] >>> 1)) + west) * TRUE_TRACK_ANGLE_ACCURACY) * west);
        if (trueTrackAngle < 0 || trueTrackAngle > 360) {
            return false;
        }

        double gs = ((data[7] << 2) | ((data[8] >>> 6) & 0x3)) * GS_ACCURACY;
        if (gs < 0 || gs > 1100) {
            return false;
        }

        double trueTrackSign = ((data[8] >>> 4) & 0x1) == 1 ? -1.0 : 1.0;
        double trueTrackRate = ((((data[8] & 0xF) << 5) | data[9] >>> 3)) * TRUE_TRACK_RATE_ACCURACY * trueTrackSign;
        if (trueTrackRate < -16 || trueTrackRate > 16) {
            return false;
        }

        int trueAirspeed = (((data[9] & 0x3) << 8) | data[10]) * 2;
        if (trueAirspeed < 0 || trueAirspeed > 1000) {
            return false;
        }

        track.setRollAngle(rollAngle);
        track.setTrueHeading(trueTrackAngle);
        track.setGs(gs);
        track.setTrackAngleRate(trueTrackRate);
        track.setTas(trueAirspeed);

        return true;
    }
}
