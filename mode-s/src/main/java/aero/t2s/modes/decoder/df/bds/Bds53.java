package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds53 extends Bds {
    private static final double HEADING_ACCURACY = 90d / 512d;
    private static final double MACH_ACCURACY = 0.008d;
    private static final double TAS_ACCURACY = 0.5d;
    private static final int VS_ACCURACY = 64;

    @Override
    public boolean attemptDecode(Track track, short[] data) {
        if (!track.getCapabilityReport().isBds53()) {
            return false;
        }

        boolean magHdgStatus = (data[4] & 0b10000000) == 0b10000000;
        boolean iasStatus = (data[5] & 0b00001000) == 0b00001000;
        boolean machStatus = (data[6] & 0b00000001) == 0b00000001;
        boolean tasStatus = (data[8] & 0b01000000) == 0b01000000;
        boolean vsStatus = (data[9] & 0b00000010) == 0b00000010;

        boolean isMagHdgNegative = (data[4] & 0b01000000) == 0b01000000;
        double magHdg = ((((data[4] & 0b00111111) << 4) | ((data[5] & 0b11110000))) * HEADING_ACCURACY + (isMagHdgNegative ? 180 : 0));
        if ((!magHdgStatus && magHdg != 0) || magHdg > 360) {
            return false;
        }

        int ias = ((data[5] & 0b00000111) << 7) | ((data[6] & 0b11111110) >>> 1);
        if (!iasStatus && ias != 0) {
            return false;
        }
        if (iasStatus && ias > 500) {
            return false;
        }

        double mach = ((data[7] << 1) | (data[8] >>> 7)) * MACH_ACCURACY;
        if (!machStatus && mach != 0) {
            return false;
        }

        double tas = (((data[8] & 0b00111111) << 6) | ((data[9] & 0b11111100) >>> 2)) * TAS_ACCURACY;
        if (!tasStatus && tas != 0) {
            return false;
        }
        if (tasStatus && tas > 500) {
            return false;
        }

        boolean isVsNegative = (data[9] & 0b00000001) == 0b00000001;
        int vs = data[10] * VS_ACCURACY * (isVsNegative ? -1 : 0);
        if (!vsStatus && vs != 0) {
            return false;
        }

        if (magHdgStatus)
            track.setMagneticHeading(magHdg);
        if (iasStatus)
            track.setIas(ias);
        if (machStatus)
            track.setMach(mach);
        if (tasStatus)
            track.setTas(tas);
        if (vsStatus)
            track.setRocd(vs);

        return true;
    }
}
