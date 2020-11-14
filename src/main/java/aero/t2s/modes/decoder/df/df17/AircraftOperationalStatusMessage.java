package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AircraftOperationalStatusMessage extends ExtendedSquitter {
    private Version version;

    public AircraftOperationalStatusMessage(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusMessage decode() {

        version = Version.from(data[9] >>> 5);

        switch (version) {
            case VERSION0: return new AircraftOperationalStatusVersion0(data).decode();
            case VERSION1: return new AircraftOperationalStatusVersion1(data).decode();
            case VERSION2: return new AircraftOperationalStatusVersion2(data).decode();
            default: throw new NotImplementedException("Aircraft Status Version " + version.name() + " is not implemented");
        }
    }

    @Override
    public void apply(Track track) {
        // Nothing to do here
    }
}
