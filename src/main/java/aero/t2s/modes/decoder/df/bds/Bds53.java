package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds53 extends Bds {
    private static final double HEADING_ACCURACY = 90d / 512d;
    private static final double MACH_ACCURACY = 0.008d;
    private static final double TAS_ACCURACY = 0.5d;
    private static final int VS_ACCURACY = 64;

    private boolean statusRocd;
    private boolean statusTas;
    private boolean statusMach;
    private boolean statusIas;
    private boolean statusMagneticHeading;

    private double magneticHeading;
    private int rocd;
    private double tas;
    private double mach;
    private int ias;

    public Bds53(short[] data) {
        super(data);

        statusMagneticHeading = (data[4] & 0b10000000) == 0b10000000;
        statusIas = (data[5] & 0b00001000) == 0b00001000;
        statusMach = (data[6] & 0b00000001) == 0b00000001;
        statusTas = (data[8] & 0b01000000) == 0b01000000;
        statusRocd = (data[9] & 0b00000010) == 0b00000010;

        boolean isMagHdgNegative = (data[4] & 0b01000000) == 0b01000000;
        magneticHeading = ((((data[4] & 0b00111111) << 4) | ((data[5] & 0b11110000))) * HEADING_ACCURACY + (isMagHdgNegative ? 180 : 0));
        if ((!statusMagneticHeading && magneticHeading != 0) || magneticHeading > 360) {
            invalidate();
            return;
        }

        ias = ((data[5] & 0b00000111) << 7) | ((data[6] & 0b11111110) >>> 1);
        if (!statusIas && ias != 0) {
            invalidate();
            return;
        }
        if (statusIas && ias > 500) {
            invalidate();
            return;
        }

        mach = ((data[7] << 1) | (data[8] >>> 7)) * MACH_ACCURACY;
        if (!statusMach && mach != 0) {
            invalidate();
            return;
        }

        tas = (((data[8] & 0b00111111) << 6) | ((data[9] & 0b11111100) >>> 2)) * TAS_ACCURACY;
        if (!statusTas && tas != 0) {
            invalidate();
            return;
        }
        if (statusTas && tas > 500) {
            invalidate();
            return;
        }

        boolean isRocdNegative = (data[9] & 0b00000001) == 0b00000001;
        rocd = data[10] * VS_ACCURACY * (isRocdNegative ? -1 : 0);
        if (!statusRocd && rocd != 0) {
            invalidate();
            return;
        }
    }

    @Override
    public void apply(Track track) {
        if (statusMagneticHeading)
            track.setMagneticHeading(magneticHeading);
        if (statusIas)
            track.setIas(ias);
        if (statusMach)
            track.setMach(mach);
        if (statusTas)
            track.setTas(tas);
        if (statusRocd)
            track.setRocd(rocd);
    }

    @Override
    protected void reset() {
        statusRocd = false;
        statusTas = false;
        statusMach = false;
        statusIas = false;
        statusMagneticHeading = false;

        magneticHeading = 0;
        rocd = 0;
        tas = 0;
        mach = 0;
        ias = 0;
    }

    public double getMagneticHeading() {
        return magneticHeading;
    }

    public boolean isStatusRocd() {
        return statusRocd;
    }

    public boolean isStatusTas() {
        return statusTas;
    }

    public boolean isStatusMach() {
        return statusMach;
    }

    public boolean isStatusIas() {
        return statusIas;
    }

    public boolean isStatusMagneticHeading() {
        return statusMagneticHeading;
    }

    public int getRocd() {
        return rocd;
    }

    public double getTas() {
        return tas;
    }

    public double getMach() {
        return mach;
    }

    public int getIas() {
        return ias;
    }
}
