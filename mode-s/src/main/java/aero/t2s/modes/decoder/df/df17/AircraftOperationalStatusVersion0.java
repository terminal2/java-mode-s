package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AircraftOperationalStatusVersion0 extends AircraftOperationalStatusMessage {
    public AircraftOperationalStatusVersion0(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion0 decode() {
        return this;
    }

    @Override
    public void apply(Track track) {
        track.setVersion(Version.VERSION0);
    }
}
