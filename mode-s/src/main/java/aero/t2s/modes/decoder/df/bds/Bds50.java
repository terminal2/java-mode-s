package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

/**
 * 56-bit MB Field is structured in the following format
 * <pre>
 * Bit |
 * 01  | Status
 * 02  | Sign (Left wing down)
 * 03  | MSB Roll angle (45 degrees)
 * 04  | Roll angle cont.
 * 05  | Roll angle cont.
 * 06  | Roll angle cont.
 * 07  | Roll angle cont.
 * 08  | Roll angle cont.
 * ---------------------------------------
 * 09  | Roll angle cont.
 * 10  | Roll angle cont.
 * 11  | LSB roll angle (45/256 degrees)
 * 12  | Status
 * 13  | Sign (West)
 * 14  | MSB True track angle (90 degrees)
 * 15  | True Track Angle cont.
 * 16  | True Track Angle cont.
 * ---------------------------------------
 * 17  | True Track Angle cont.
 * 18  | True Track Angle cont.
 * 19  | True Track Angle cont.
 * 20  | True Track Angle cont.
 * 21  | True Track Angle cont.
 * 22  | True Track Angle cont.
 * 23  | LSB True Track Angle (90/512 degrees)
 * 24  | Status
 * ---------------------------------------
 * 25  | MSB Ground Speed (1024kt)
 * 26  | Ground Speed cont.
 * 27  | Ground Speed cont.
 * 28  | Ground Speed cont.
 * 29  | Ground Speed cont.
 * 30  | Ground Speed cont.
 * 31  | Ground Speed cont.
 * 32  | Ground Speed cont.
 * ---------------------------------------
 * 33  | Ground Speed cont.
 * 34  | LSB Ground Speed (2kt)
 * 35  | Status
 * 36  | Sign (Minus)
 * 37  | MSB Track Angle Rate (8 degrees / second)
 * 38  | Track Angle Rate cont.
 * 39  | Track Angle Rate cont.
 * 40  | Track Angle Rate cont.
 * ---------------------------------------
 * 41  | Track Angle Rate cont.
 * 42  | Track Angle Rate cont.
 * 43  | Track Angle Rate cont.
 * 44  | Track Angle Rate cont.
 * 45  | LSB Track Angle Rate (8/256 degrees / second)
 * 46  | Status
 * 47  | MSB True Airspeed (1024kt)
 * 48  | True Airspeed cont.
 * ---------------------------------------
 * 49  | True Airspeed cont.
 * 50  | True Airspeed cont.
 * 51  | True Airspeed cont.
 * 52  | True Airspeed cont.
 * 53  | True Airspeed cont.
 * 54  | True Airspeed cont.
 * 55  | True Airspeed cont.
 * 56  | LSB True Airspeed (2kt)
 * </pre>
 *
 * <b>Status flags</b>
 * <ul>
 *     <li>Roll Angle (bit 1) - Indicates if Roll Angle is available/valid</li>
 *     <li>True Track Angle (bit 12) - Indicates if true track angle is available/valid</li>
 *     <li>Ground Speed (bit 24) - Indicates if ground speed available/valid</li>
 *     <li>Track Angle Rate (bit 35) - Indicates if ground speed available/valid</li>
 *     <li>True Airspeed (bit 46) - Indicates if true airspeed is available/valid</li>
 * </ul>
 *
 * <h2>Roll Angle</h2>
 *
 * <p>Roll angle or bank angle tells you if the aircraft is banking to the left or to the right and how many degrees.</p>
 *
 * <p>
 *     Sign (bit 2) determines if the aircraft is banking left (1) or right (0).
 *     When the aircraft is flying straight sign may either set to 1 or 0 with zero angle.
 * </p>
 *
 * <i>
 *     NOTE: If the status flag (bit 1) is not enabled ALL bits (2 through 11) need to be actively set to zero.
 * </i>
 *
 * <i>
 *     NOTE2: LSB shows resolution of 45/256degrees, the total range of the field roll angle is 0-90.
 * </i>
 *
 * <h2>True Track Angle</h2>
 *
 * <p>Is the direction the aircraft is flying relative to the ground and true north.</p>
 *
 * <i>
 *     NOTE: Sign flag (bit 13) determines West (1) and East (0). Sign is 1 then 315 degrees is transmitted as 45degrees.
 *     Or to get the direction 0-360 range you need to add 180 degrees when west is set to 1.
 * </i>
 *
 * <i>
 *     NOTE2: If the status flag (bit 1) is not enabled ALL bits (2 through 11) need to be actively set to zero.
 * </i>
 *
 * <i>
 *     NOTE3: LSB shows resolution of 45/256degrees, the total range of the field roll angle is 0-90.
 * </i>
 * <h2>Ground Speed</h2>
 * <h2>Track Angle Rate</h2>
 * <h2>True Airspeed</h2>
 */
public class Bds50 extends Bds {
    private static final double ROLL_ACCURACY = 45.0 / 256.0;
    private static final double TRUE_TRACK_ANGLE_ACCURACY = 90.0 / 512.0;
    private static final double SPEED_ACCURACY = 2;
    private static final double TRUE_TRACK_RATE_ACCURACY = 8.0 / 256.0;

    @Override
    public boolean attemptDecode(Track track, short[] data) {
        boolean statusRollAngle = ((data[4] >>> 7) & 0x1) == 1;
        boolean statusTrackAngle = ((data[5] >>> 4) & 0x1) == 1;
        boolean statusGroundSpeed = (data[6] & 0x1) == 1;
        boolean statusTrueAngleRate = ((data[8] >>> 5) & 0x1) == 1;
        boolean statusTrueAirspeed = ((data[9] >>> 2) & 0x1) == 1;

        boolean isLeftWingDown = ((data[4] >>> 6) & 0x1) == 1;
        int rollAngle = (((data[4] & 0x3F) << 3) | (data[6] >>> 5));
        if (!statusRollAngle && (isLeftWingDown || rollAngle != 0)) {
            return false;
        }

        boolean isWest = (data[5] & 0b00001000) != 0;
        int trueTrack = (((data[5] & 0x7) << 7) | (data[6] >>> 1));
        if (!statusTrackAngle && (isWest || trueTrack != 0)) {
            return false;
        }

        int gs = (data[7] << 2) | ((data[8] >>> 6) & 0x3);
        if (!statusGroundSpeed && gs != 0) {
            return false;
        }

        boolean isTrackAngleRateNegative = (data[8] & 0b00010000) != 0;
        int trackAngleRate = ((data[8] & 0xF) << 5) | data[9] >>> 3;
        if (!statusTrueAngleRate && (isTrackAngleRateNegative || trackAngleRate != 0)) {
            return false;
        }

        int trueAirspeed = ((data[9] & 0x3) << 8) | data[10];
        if (!statusTrueAirspeed && trueAirspeed != 0) {
            return false;
        }

        // If both are available check if they are within reasonable distance
        if (statusGroundSpeed && statusTrueAirspeed) {
            // diff > 400 kts is probably an error
            if (Math.abs(gs - trueAirspeed) * 2 > 400) {
                return false;
            }

            if (trueAirspeed < 50 && gs > 200) {
                return false;
            }
        }

        if (statusRollAngle && (rollAngle * ROLL_ACCURACY > 60)) {
            return false;
        }

        if (statusTrackAngle && trackAngleRate > 8) {
            return false;
        }

        if (statusTrackAngle && statusRollAngle) {
            if (rollAngle * ROLL_ACCURACY > 30 && trackAngleRate * TRUE_TRACK_RATE_ACCURACY < 2) {
                return false;
            }
        }

        if (statusRollAngle)
            track.setRollAngle(rollAngle * ROLL_ACCURACY * (isLeftWingDown ? -1d : 1d));

        if (statusTrackAngle)
            track.setTrueHeading(trueTrack * TRUE_TRACK_ANGLE_ACCURACY + (isWest ? 180d : 0d));

        if (statusGroundSpeed)
            track.setGs(gs * SPEED_ACCURACY);

        if (statusTrueAngleRate)
            track.setTrackAngleRate(trackAngleRate * TRUE_TRACK_RATE_ACCURACY * (isTrackAngleRateNegative ? -1d : 1d));

        if (statusTrueAirspeed) {
            track.setTas(trueAirspeed * SPEED_ACCURACY);
        }

        return true;
    }
}
