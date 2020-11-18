package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AircraftOperationalStatusVersion0 extends AircraftOperationalStatusMessage {
    public AircraftOperationalStatusVersion0(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion0 decode() {
        throw new NotImplementedException("AircraftOperationalStatusVersion0 not implemented");
    }

    @Override
    public void apply(Track track) {
        track.setVersion(Version.VERSION0);
    }
}
