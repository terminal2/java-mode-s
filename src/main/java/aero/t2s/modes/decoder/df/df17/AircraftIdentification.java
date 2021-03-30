package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;

public class AircraftIdentification extends ExtendedSquitter {
    private String acid;

    public AircraftIdentification(short[] data) {
        super(data);
    }

    @Override
    public AircraftIdentification decode() {
        acid = Common.charToString(data[5] >>> 2) +
            Common.charToString(((data[5] & 0x3) << 4) | (data[6] >>> 4)) +
            Common.charToString(((data[6] & 0xF) << 2) | (data[7] >>> 6)) +
            Common.charToString(data[7] & 0x3F) +
            Common.charToString(data[8] >>> 2) +
            Common.charToString(((data[8] & 0x3) << 4) | (data[9] >>> 4)) +
            Common.charToString(((data[9] & 0xF) << 2) | (data[10] >>> 6)) +
            Common.charToString(data[10] & 0x3F);
        acid = acid.replace("_", "");

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setCallsign(acid);
    }

    public String getAcid() {
        return acid;
    }

    @Override
    public String toString() {
        return String.format(
            "AircraftIdentification\n" +
            "ACID: %s",
            acid
        );
    }
}
