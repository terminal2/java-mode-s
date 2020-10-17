package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;

public class Bds20 extends Bds {
    @Override
    public boolean attemptDecode(Track track, short[] data) {
        if (!track.getCapabilityReport().isBds20()) {
            return false;
        }

        if (data[4] >>> 4 != 2 || (data[4] & 0xF) != 0) {
            return false;
        }

        String acid = Common.charToString(data[5] >>> 2) +
                Common.charToString(((data[5] & 0x3) << 4) | (data[6] >>> 4)) +
                Common.charToString(((data[6] & 0xF) << 2) | (data[7] >>> 6)) +
                Common.charToString(data[7] & 0x3F) +
                Common.charToString(data[8] >>> 2) +
                Common.charToString(((data[8] & 0x3) << 4) | (data[9] >>> 4)) +
                Common.charToString(((data[9] & 0xF) << 2) | (data[10] >>> 6)) +
                Common.charToString(data[10] & 0x3F);

        acid = acid.replace("_", "");

        track.setCallsign(acid);

        return true;
    }
}
