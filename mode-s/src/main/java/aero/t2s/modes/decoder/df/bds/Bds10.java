package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds10 extends Bds {
    @Override
    public boolean attemptDecode(Track track, short[] data) {
        if (data[4] >>> 4 != 1 || (data[4] & 0xF) != 0) {
            return false;
        }

        if (((data[5] >>> 2) & 0x1F) == 0) {
            return true;
        }

        return false;
    }
}
