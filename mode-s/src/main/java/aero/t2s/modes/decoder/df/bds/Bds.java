package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public abstract class Bds {
    public abstract boolean attemptDecode(Track track, short[] data);
}
