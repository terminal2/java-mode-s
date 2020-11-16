package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AircraftOperationalStatusVersion1Surface extends AircraftOperationalStatusVersion1 {
    public AircraftOperationalStatusVersion1Surface(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion1Surface decode() {
        throw new NotImplementedException("AircraftOperationalStatusVersion1Surface not implemented");
    }

    @Override
    public void apply(Track track) {
        track.setVersion(Version.VERSION1);
    }
}
