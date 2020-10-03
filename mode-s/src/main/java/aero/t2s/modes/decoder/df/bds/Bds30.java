package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds30 extends Bds {
    @Override
    public boolean attemptDecode(Track track, short[] data) {
        if (data[4] >>> 4 != 3 || (data[4] & 0xF) != 0) {
            return false;
        }


        return true;
    }
}
