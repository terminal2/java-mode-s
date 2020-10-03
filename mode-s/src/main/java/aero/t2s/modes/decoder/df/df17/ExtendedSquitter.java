package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;

public abstract class ExtendedSquitter {
    public abstract void decode(Track track, int typeCode, short[] data);
}
