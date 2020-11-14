package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import org.slf4j.LoggerFactory;

public class AirborneVelocity extends ExtendedSquitter {
    private static final double HEADING_RESOLUTION = 360.0 / 1024.0;

    public AirborneVelocity(short[] data) {
        super(data);
    }
//
//    @Override
//    public void decode(Track track, int typeCode, short[] data) {
//        int subType = data[4] & 0x7;
//
//        boolean intentChange = (data[5] >>> 7) == 1;
//        track.setNACv(data[5] >>> 3);
//
//        int altitudeDifferenceBaroDirection = (data[10] >>> 7) == 0 ? 1 : -1;
//        int geometricHeightDiffBaro = ((data[10] & 0x7F) - 1) * altitudeDifferenceBaroDirection;
//        track.setGeometricHeightOffset(geometricHeightDiffBaro);
//
//        int vsiDirection = ((data[8] >>> 3) & 0x1) == 0 ? 1 : -1;
//        boolean verticalRateBaro = ((data[8] >>> 4) & 0x1) == 1;
//        int rocd = (((data[8] & 0x7) << 6) | (data[9] >>> 2) - 1) * 64 * vsiDirection;
//        track.setRocdAvailable(true);
//        track.setRocdSourceBaro(verticalRateBaro);
//        if (verticalRateBaro) {
//            track.setBaroRocd(rocd);
//        } else {
//            track.setRocd(rocd);
//        }
//
//        if (subType == 1 || subType == 2) {
//            decodeGroundSpeed(track, typeCode, subType, data);
//            return;
//        }
//
//        if (subType == 3 || subType == 4) {
//            decodeAirspeedGroundSpeed(track, typeCode, subType, data);
//            return;
//        }
//
//        LoggerFactory.getLogger(getClass()).warn("{}: subtype {} is reserved.", getClass().getSimpleName(), subType);
//    }
//
//    private void decodeGroundSpeed(Track track, int typeCode, int subType, short[] data) {
//        int xDirection = (data[5] >>> 2) == 1 ? 1 : -1;
//        int yDirection = (data[7] >>> 7) == 1 ? 1 : -1;
//
//        int x = ((((data[5] & 0x3) << 8) | (data[6])) - 1) * (subType == 1 ? 1 : 4) * xDirection;
//        int y = ((((data[7] & 0x7F) << 3) | (data[8] >>> 5)) - 1) * (subType == 1 ? 1 : 4) * yDirection;
//
//        track.setVx(x);
//        track.setVy(y);
//        track.setGs(Math.sqrt(x * x + y * y));
//    }
//
//    private void decodeAirspeedGroundSpeed(Track track, int typeCode, int subType, short[] data) {
//        boolean headingAvailable = data[5] >>> 2 == 1;
//        boolean ias = data[7] >>> 7 == 0;
//
//        double heading = (((data[5] & 0x3) << 8) | data[6]) * HEADING_RESOLUTION;
//        int airspeed = ((((data[7] & 0x7F) << 1) | (data[8] >>> 5)) - 1) * (subType == 1 ? 1 : 4);
//
//        if (track.isMagneticHeading()) {
//            track.setMagneticHeading(headingAvailable ? heading : 0);
//        } else {
//            track.setTrueHeading(headingAvailable ? heading : 0);
//        }
//
//        track.setIasAvailable(ias);
//
//        if (ias) {
//            track.setIas(airspeed);
//        } else {
//            track.setTas(airspeed);
//        }
//    }

    @Override
    public AirborneVelocity decode() {
        int subType = data[4] & 0x7;

        switch (subType) {
            case 1:
            case 2:
                return new AirborneVelocityVersion0(data).decode();
            case 3:
            case 4:
                return new AirborneVelocityVersion2(data).decode();
            default:
                throw new NotImplementedException("BDS0,9 sub type " + subType + " is not implemented");
        }
    }

    @Override
    public void apply(Track track) {
        // Nothing
    }
}
