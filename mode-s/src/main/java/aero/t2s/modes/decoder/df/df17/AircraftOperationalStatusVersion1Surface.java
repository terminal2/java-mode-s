package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AircraftOperationalStatusVersion1Surface extends AircraftOperationalStatusMessage {
    public AircraftOperationalStatusVersion1Surface(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusMessage decode() {
        return this;
    }

    @Override
    public void apply(Track track) {
        track.setVersion(Version.VERSION1);
    }
}
