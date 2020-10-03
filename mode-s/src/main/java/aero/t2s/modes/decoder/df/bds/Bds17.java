package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds17 extends Bds {
    @Override
    public boolean attemptDecode(Track track, short[] data) {
        return ((data[8] & 0xF) | (data[9]) | data[10] | data[11]) == 0;
    }
}
