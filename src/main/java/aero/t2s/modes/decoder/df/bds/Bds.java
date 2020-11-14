package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public abstract class Bds {
    private final short[] data;

    private boolean valid = true;

    public Bds(short[] data) {
        this.data = data;
    }

    public abstract void apply(Track track);

    protected void invalidate() {
        this.valid = false;
    }

    public boolean isValid() {
        return valid;
    }
}
