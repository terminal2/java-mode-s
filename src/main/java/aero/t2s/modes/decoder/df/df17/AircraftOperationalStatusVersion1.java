package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;

public class AircraftOperationalStatusVersion1 extends AircraftOperationalStatusMessage {
    public AircraftOperationalStatusVersion1(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion1 decode() {
        int subType = data[4] & 0x7;

        switch (subType) {
            case 0: return new AircraftOperationalStatusVersion1Airborne(data).decode();
            case 1: return new AircraftOperationalStatusVersion1Surface(data).decode();
            default: throw new NotImplementedException("Aircraft operation status version 1 sub type " + subType + " is not known");
        }
    }

    @Override
    public void apply(Track track) {
        // Nothing
    }
}
