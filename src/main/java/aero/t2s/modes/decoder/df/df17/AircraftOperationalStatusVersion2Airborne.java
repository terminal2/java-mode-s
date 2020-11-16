package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AircraftOperationalStatusVersion2Airborne extends AircraftOperationalStatusVersion2 {
    public AircraftOperationalStatusVersion2Airborne(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion2Airborne decode() {
        throw new NotImplementedException("AircraftOperationalStatusVersion2Airborne not implemented");
    }

    @Override
    public void apply(Track track) {
        track.setVersion(Version.VERSION2);
    }
}
