package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.AltitudeEncoding;

public class Bds60 extends Bds {
    private static final double MAG_ACCURACY = 90.0 / 512.0;
    private static final double MACH_ACCURACY = 2048.0 / 512.0;
    private static final double ROCD_ACCURCY = 8192.0 / 256.0;

    private boolean statusMagneticHeading;
    private boolean statusIas;
    private boolean statusMach;
    private boolean statusBaroRocd;
    private boolean statusIrsRocd;

    private double mach;
    private double magneticHeading;
    private int ias;
    private double baroRocd;
    private double irsRocd;

    public Bds60(short[] data) {
        super(data);

        statusMagneticHeading = (data[4] & 0b10000000) != 0;
        statusIas = (data[5] & 0b00001000) != 0;
        statusMach = (data[6] & 0b00000001) != 0;
        statusBaroRocd = (data[8] & 0b00100000) != 0;
        statusIrsRocd = (data[9] & 0b00000100) != 0;

        if (!(statusMagneticHeading || statusIas || statusMach || statusBaroRocd || statusIrsRocd)) {
            invalidate();
            return;
        }

        double hdgSign = ((data[4] >>> 6) & 0x1) == 1 ? -1024d : 0d;
        magneticHeading = ((((data[4] & 0b00111111) << 4) | (data[5] >>> 4)) + hdgSign) * MAG_ACCURACY;
        if (magneticHeading < 0) {
            magneticHeading += 360d;
        }
        if (statusMagneticHeading) {
            if (magneticHeading < 0 || magneticHeading > 360.0) {
                invalidate();
                return;
            }
        } else {
            if (magneticHeading != 0) {
                invalidate();
                return;
            }
        }

        ias = ((data[5] & 0x7) << 7) | data[6] >>> 1;
        if (statusIas) {
            if (ias <= 0 || ias > 512) {
                invalidate();
                return;
            }
        } else {
            if (ias != 0) {
                invalidate();
                return;
            }
        }

        mach = ((data[7] << 2) | data[8] >> 6) * MACH_ACCURACY;
        mach /= 1000.0;
        if (statusMach) {
            if (mach <= 0 || mach > 1) {
                invalidate();
                return;
            }
        } else {
            if (mach != 0) {
                invalidate();
                return;
            }
        }

        double baroSign = ((data[8] >>> 4) & 0x1) == 1 ? -512.0 : 0.0;
        baroRocd = (((((data[8] & 0b00001111) << 5) | (data[9] >>> 3)) + baroSign) * ROCD_ACCURCY) % 16384;
        if (statusBaroRocd) {
            if (baroRocd < -8000 || baroRocd > 8000) {
                invalidate();
                return;
            }
        } else {
            if (baroRocd != 0) {
                invalidate();
                return;
            }
        }

        double irsSign = ((data[9] >> 1) & 0x1) == 1 ? -512.0 : 0.0;
        irsRocd = ((((data[9] & 0x1) << 8 | data[10]) + irsSign) * ROCD_ACCURCY) % 16384;
        if (statusIrsRocd) {
            if (irsRocd < -8000 || irsRocd > 6000) {
                invalidate();
                return;
            }
        } else {
            if (irsRocd != 0) {
                invalidate();
                return;
            }
        }

        if (statusMach && statusIas && data[0] >>> 3 == 20) {
            double altitude = AltitudeEncoding.decode((data[2] & 0x1F) << 8 | data[3]).getAltitude();
            if (altitude > 0) {
                double cas = machToCas(altitude * 0.3048) / 0.514444;
                if (Math.abs(cas - ias) > 20) {
                    invalidate();
                    return;
                }
            }
        }
    }

    private double machToCas(double altitude) {
        return tasToCas(machToTas(altitude), altitude);
    }

    private double tasToCas(double tas, double altitude) {
        double rho0 = 1.225;
        double T = Math.max(288.15 - 0.0065 * altitude, 216.65);
        double rhotrop = 1.225 * Math.pow((T / 288.15), 4.256848030018761);
        double dhstrat = Math.max(0.0, altitude - 11000.0);
        double rho = rhotrop * Math.exp(-dhstrat / 6341.552161);
        double R = 287.05287; // m2/(s2 x K), gas constant, sea level ISA
        double p = rho * R * T;
        double p0 = 101325.0; // Pa, air pressure, sea level ISA

        double qdyn = p * (Math.pow((1.0 + rho * tas * tas / (7.0 * p)), 3.5) - 1.0);
        return Math.sqrt(7.0 * p0 / rho0 * (Math.pow((qdyn / p0 + 1.0), (2.0 / 7.0)) - 1.0));
    }

    private double machToTas(double altitude) {
        double T = Math.max(288.15 - 0.0065 * altitude, 216.65);
        double R = 287.05287; // m2/(s2 x K), gas constant, sea level ISA
        double gamma = 1.40; // cp/cv for air
        double a = Math.sqrt(gamma * R * T);

        return mach * a;
    }


    @Override
    public void apply(Track track) {
        track.setMagneticHeading(magneticHeading);
        track.setIas(ias);
        track.setMach(mach);
        track.setBaroRocd(baroRocd);
        track.setRocd((int) irsRocd);
        track.setRocdAvailable(true);
        track.setRocdSourceBaro(!statusIrsRocd);
    }

    @Override
    protected void reset() {
        statusMagneticHeading = false;
        statusIas = false;
        statusMach = false;
        statusBaroRocd = false;
        statusIrsRocd = false;

        mach = 0;
        magneticHeading = 0;
        ias = 0;
        baroRocd = 0;
        irsRocd = 0;
    }

    public boolean isStatusMagneticHeading() {
        return statusMagneticHeading;
    }

    public boolean isStatusIas() {
        return statusIas;
    }

    public boolean isStatusMach() {
        return statusMach;
    }

    public boolean isStatusBaroRocd() {
        return statusBaroRocd;
    }

    public boolean isStatusIrsRocd() {
        return statusIrsRocd;
    }

    public double getMach() {
        return mach;
    }

    public double getMagneticHeading() {
        return magneticHeading;
    }

    public int getIas() {
        return ias;
    }

    public double getBaroRocd() {
        return baroRocd;
    }

    public double getIrsRocd() {
        return irsRocd;
    }
}
