package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds60 extends Bds {
    private static final double MAG_ACCURACY = 90.0 / 512.0;
    private static final double MACH_ACCURACY = 2048.0 / 512.0;
    private static final double ROCD_ACCURCY = 8192.0 / 256.0;

    private final boolean statusMagneticHeading;
    private final boolean statusIas;
    private final boolean statusMach;
    private final boolean statusBaroRocd;
    private final boolean statusIrsRocd;

    private double mach;
    private double magneticHeading;
    private int ias;
    private double baroRocd;
    private double irsRocd;

    public Bds60(short[] data) {
        super(data);

        statusMagneticHeading = ((data[4] >>> 7) & 0x1) == 1;
        statusIas = ((data[5] >>> 4) & 0x1) == 1;
        statusMach = (data[6] & 0x1) == 1;
        statusBaroRocd = ((data[8] >>> 5) & 0x1) == 1;
        statusIrsRocd = ((data[9] >>> 2) & 0x1) == 1;

        double hdgSign = ((data[4] >>> 6) & 0x1) == 1 ? -1024d : 0d;
        magneticHeading = ((((data[4] & 0b00111111) << 4) | (data[5] >>> 4)) + hdgSign) * MAG_ACCURACY;
        if (magneticHeading < 0) {
            magneticHeading += 360d;
        }
        if (statusMagneticHeading && (magneticHeading <= 0 || magneticHeading > 360.0)) {
            invalidate();
            return;
        }

        ias = ((data[5] & 0x7) << 7) | data[6] >>> 1;
        if (statusIas && (ias <= 0 || ias > 512)) {
            invalidate();
            return;
        }

        mach = ((data[7] << 2) | data[8] >> 6) * MACH_ACCURACY;
        mach /= 1000.0;
        if (statusMach && (mach <= 0 || mach > 1)) {
            invalidate();
            return;
        }

        double baroSign = ((data[8] >>> 4) & 0x1) == 1 ? -512.0 : 0.0;
        baroRocd = ((((data[8] & 0b00001111) << 5) | (data[9] >>> 3)) + baroSign) * ROCD_ACCURCY;
        if (statusBaroRocd && (baroRocd < -8000 || baroRocd > 8000)) {
            invalidate();
            return;
        }

        double irsSign = ((data[9] >> 1) & 0x1) == 1 ? -512.0 : 0.0;
        irsRocd = (((data[9] & 0x1) << 8 | data[10]) + irsSign) * ROCD_ACCURCY;
        if (statusIrsRocd && (irsRocd < -8000 || irsRocd > 6000)) {
            invalidate();
            return;
        }
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
