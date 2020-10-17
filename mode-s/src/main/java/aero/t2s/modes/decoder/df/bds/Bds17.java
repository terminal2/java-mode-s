package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds17 extends Bds {
    @Override
    public boolean attemptDecode(Track track, short[] data) {
        // 2,0 Aircraft identification shall always be present
        if ((data[4] & 0b00000010) == 0) {
            return false;
        }

        // F,1 Mil reserved use
        if (data[7] != 0 || data[8] != 0 || data[9] != 0 || data[10] != 0) {
            return false;
        }

        track.getCapabilityReport().update(data);
        return true;
    }
}
