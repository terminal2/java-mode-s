package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;

public class AircraftStatusMessage extends ExtendedSquitter {
    public AircraftStatusMessage(short[] data) {
        super(data);
    }

    @Override
    public AircraftStatusMessage decode() {
        int subType = data[4] & 0x7;

        // No Information
        if (subType == 0) {
            return this;
        }

        if (subType == 1) {
            return new AircraftStatusMessageEmergency(data).decode();
        }

        if (subType == 2) {
            return new AircraftStatusMessageAcasRA(data).decode();
        }

        throw new NotImplementedException("BDS6,1 Sub Type " + subType + " not implemented");
    }

    @Override
    public void apply(Track track) {
        // Nothing to do in the base type
    }
}
