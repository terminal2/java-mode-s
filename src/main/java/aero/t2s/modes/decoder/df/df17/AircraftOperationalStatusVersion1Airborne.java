package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AircraftOperationalStatusVersion1Airborne extends AircraftOperationalStatusVersion1 {
    public AircraftOperationalStatusVersion1Airborne(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion1Airborne decode() {
        throw new NotImplementedException("AircraftOperationalStatusVersion1Airborne not implemented");
    }

    @Override
    public void apply(Track track) {
        track.setVersion(Version.VERSION1);
    }
}
