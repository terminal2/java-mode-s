package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds40 extends Bds {
    @Override
    public boolean attemptDecode(Track track, short[] data) {
        boolean mcpStatus = data[4] >>> 7 == 1;
        boolean fmsStatus = ((data[5] >>> 2) & 0x1) == 1;
        boolean baroStatus = ((data[7] >>> 5) & 0x1) == 1;
        boolean targetAlt = ((data[10] >>> 2) & 0x1) == 1;
        boolean reservedZeroA = ((data[8] & 0x1) | (data[9] >>> 1)) == 0;
        boolean reservedZeroB = ((data[10] >>> 3) & 0x3) == 0;

        if (!reservedZeroA && !reservedZeroB) {
            return false;
        }

        int mcpFcuAltitude = (((data[4] & 0x7F) << 5) | (data[6] >> 3)) * 16;
        if (mcpStatus && (mcpFcuAltitude <= 0 || mcpFcuAltitude > 46000)) {
            return false;
        }

        int fmsAltitude = (((data[5] & 0x3) << 10) | (data[6] << 2) | ((data[7] >>> 6) & 0x3)) * 16;
        if (fmsStatus && (fmsAltitude <= 0 || fmsAltitude > 46000)) {
            return false;
        }

        double baro = ((((data[7] & 0x1F) << 7) | (data[8] >>> 1)) * 0.1) + 800.0;
        if (baroStatus && (baro == 800 || baro < 850 || baro > 1100)) {
            return false;
        }

        if (targetAlt && (data[10] & 0x3) != 0) {
            return false;
        }

        if ((data[9] & 0x1) == 1) {
            track.setVnav((data[10] >>> 7) == 1);
            track.setAltitudeHold(((data[10] >>> 6) & 0x1) == 1);
            track.setApproachMode(((data[10] >>> 5) & 0x1) == 1);
        }

        track.setSelectedAltitude(mcpFcuAltitude);
        track.setFmsSelectedAltitude(fmsAltitude);
        track.setBaroSetting(baro);

        return true;
    }
}
