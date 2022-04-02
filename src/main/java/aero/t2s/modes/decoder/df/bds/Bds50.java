package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Altitude;
import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.AltitudeEncoding;

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

    private boolean statusRollAngle;
    private boolean statusTrackAngle;
    private boolean statusGs;
    private boolean statusTrueAngleRate;
    private boolean statusTas;

    private double gs;
    private double trackAngleRate;
    private double trueTrack;
    private double tas;
    private double rollAngle;

    public Bds50(short[] data) {
        super(data);

        statusRollAngle = ((data[4] >>> 7) & 0x1) == 1;
        statusTrackAngle = ((data[5] >>> 4) & 0x1) == 1;
        statusGs = (data[6] & 0x1) == 1;
        statusTrueAngleRate = ((data[8] >>> 5) & 0x1) == 1;
        statusTas = ((data[9] >>> 2) & 0x1) == 1;

        boolean isLeftWingDown = ((data[4] >>> 6) & 0x1) == 1;
        rollAngle = ((((data[4] & 0b00111111) << 3) | (data[5] >>> 5)) - (isLeftWingDown ? 512 : 0)) * ROLL_ACCURACY;
        if (!statusRollAngle && (isLeftWingDown || rollAngle != 0)) {
            invalidate();
            return;
        }
        if (statusRollAngle) {
            if (Math.abs(rollAngle) > 32) {
                invalidate();
                rollAngle = 0;
                return;
            }
        } else {
            rollAngle = 0;
        }

        boolean isWest = (data[5] & 0b00001000) != 0;
        trueTrack = (((data[5] & 0x7) << 7) | (data[6] >>> 1));
        if (!statusTrackAngle && (isWest || trueTrack != 0)) {
            invalidate();
            return;
        }
        trueTrack = trueTrack * TRUE_TRACK_ANGLE_ACCURACY + (isWest ? 180d : 0d);

        gs = ((data[7] << 2) | ((data[8] >>> 6) & 0x3)) * SPEED_ACCURACY;
        if (statusGs) {
            if (gs > 600) {
                invalidate();
                return;
            }
        } else {
            if (gs != 0) {
                invalidate();
                return;
            }
        }

        boolean isTrackAngleRateNegative = (data[8] & 0b00010000) != 0;
        trackAngleRate = ((data[8] & 0xF) << 5) | data[9] >>> 3;
        if (!statusTrueAngleRate && (isTrackAngleRateNegative || trackAngleRate != 0)) {
            invalidate();
            return;
        }
        if (statusTrueAngleRate && trackAngleRate == 0b11111111) {
            invalidate();
            return;
        }
        trackAngleRate = (trackAngleRate + (isTrackAngleRateNegative ? -512d : 0)) * TRUE_TRACK_RATE_ACCURACY;

        tas = (((data[9] & 0x3) << 8) | data[10]) * SPEED_ACCURACY;
        if (!statusTas && tas != 0) {
            invalidate();
            return;
        }
        if (statusGs && statusTas && Math.abs((tas - gs)) > 200) {
            invalidate();
            return;
        }
    }

    public Bds compareWithBds60(Bds60 bds60) {
        // average speed of sound between 0 - 45000ft
        double speedOfSound = 617d;

        short[] data = getData();
        if (data[0] >>> 3 == 20) {
            Altitude altitude = AltitudeEncoding.decode((data[2] & 0x1F) << 8 | data[3]);
            // Estimated TAS
            double bds60Tas = bds60.getIas() * (1 + (altitude.getAltitude() / 1000) * 0.02);
            // Estimated Mach
            double bds60Mach = bds60Tas / speedOfSound;

            if (Math.abs(bds60Mach - bds60.getMach()) < 0.1) {
                return bds60;
            }

            return this;
        } else {
            // No altitude information assume check in 2000ft increments
            for (int altitude = 0; altitude < 45; altitude += 2) {
                double bds60Tas = bds60.getIas() * (1 + altitude * 0.02);
                double bds60Mach = bds60Tas / speedOfSound;

                if (Math.abs(bds60Mach - bds60.getMach()) < 0.01) {
                    return bds60;
                }
            }

            return this;
        }
    }

    @Override
    public void apply(Track track) {
        if (statusRollAngle)
            track.setRollAngle(rollAngle);

        if (statusTrackAngle)
            track.setTrueHeading(trueTrack);

        if (statusGs)
            track.setGs(gs);

        if (statusTrueAngleRate)
            track.setTrackAngleRate(trackAngleRate);

        if (statusTas) {
            track.setTas(tas);
        }
    }

    @Override
    protected void reset() {
        statusRollAngle = false;
        statusTrackAngle = false;
        statusGs = false;
        statusTrueAngleRate = false;
        statusTas = false;

        gs = 0;
        trackAngleRate = 0;
        trueTrack = 0;
        tas = 0;
        rollAngle = 0;
    }

    public boolean isStatusRollAngle() {
        return statusRollAngle;
    }

    public boolean isStatusTrackAngle() {
        return statusTrackAngle;
    }

    public boolean isStatusGs() {
        return statusGs;
    }

    public boolean isStatusTrueAngleRate() {
        return statusTrueAngleRate;
    }

    public boolean isStatusTas() {
        return statusTas;
    }

    public double getGs() {
        return gs;
    }

    public double getTrackAngleRate() {
        return trackAngleRate;
    }

    public double getTrueTrack() {
        return trueTrack;
    }

    public double getTas() {
        return tas;
    }

    public double getRollAngle() {
        return rollAngle;
    }
}
