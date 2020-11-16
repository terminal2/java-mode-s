package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AircraftOperationalStatusVersion2Surface extends AircraftOperationalStatusVersion2 {
    public AircraftOperationalStatusVersion2Surface(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion2Surface decode() {
        throw new NotImplementedException("AircraftOperationalStatusVersion2Airborne not implemented");
    }

    @Override
    public void apply(Track track) {
        track.setVersion(Version.VERSION2);
    }
}
