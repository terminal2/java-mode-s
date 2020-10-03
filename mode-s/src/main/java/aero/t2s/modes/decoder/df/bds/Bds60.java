package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds60 extends Bds {
    private static final double MAG_ACCURACY = 90.0 / 512.0;
    private static final double MACH_ACCURACY = 2048.0 / 512.0;
    private static final double ROCD_ACCURCY = 8192.0 / 256.0;

    @Override
    public boolean attemptDecode(Track track, short[] data) {
        boolean statusMagHdg = ((data[4] >>> 7) & 0x1) == 1;
        boolean statusIas = ((data[5] >>> 4) & 0x1) == 1;
        boolean statusMach = (data[6] & 0x1) == 1;
        boolean statusBaroRocd = ((data[8] >>> 5) & 0x1) == 1;
        boolean statusIrsRocd = ((data[9] >>> 2) & 0x1) == 1;

        double hdgSign = ((data[4] >>> 6) & 0x1) == 1 ? -1 : 1.0;
        double magHdg = (360.0 + (((((data[4] & 0x3F) << 4) | (data[5] >>> 4))) * MAG_ACCURACY * hdgSign) % 360.0);
        if (statusMagHdg && (magHdg <= 0 || magHdg > 360.0)) {
            return false;
        }

        int ias = ((data[5] & 0x7) << 7) | data[6] >>> 1;
        if (statusIas && (ias <= 0 || ias > 512)) {
            return false;
        }

        double mach = ((data[7] << 2) | data[8] >> 6) * MACH_ACCURACY;
        mach /= 1000.0;
        if (statusMach && (mach <= 0 || mach > 1)) {
            return false;
        }

        double baroSign = ((data[8] >>> 4) & 0x1) == 1 ? -1.0 : 1.0;
        double baroRocd = ((data[8] & 0xF) << 5 | data[9] >>> 3) * ROCD_ACCURCY * baroSign;
        if (statusBaroRocd && (baroRocd < -8000 || baroRocd > 8000)) {
            return false;
        }

        double irsSign = ((data[9] >> 1) & 0x1) == 1 ? -1.0 : 1.0;
        double irsRocd = ((data[9] & 0x1) << 8 | data[10]) * ROCD_ACCURCY * irsSign;
        if (statusIrsRocd && (irsRocd < -8000 || irsRocd > 6000)) {
            return false;
        }

        track.setMagneticHeading(magHdg);
        track.setIas(ias);
        track.setMach(mach);
        track.setBaroRocd(baroRocd);
        track.setRocd((int) irsRocd);
        track.setRocdAvailable(true);
        track.setRocdSourceBaro(!statusIrsRocd);

        return true;
    }
}
