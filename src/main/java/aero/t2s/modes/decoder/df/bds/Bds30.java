package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds30 extends Bds {
    public Bds30(short[] data) {
        super(data);

        if (data[4] >>> 4 != 3 || (data[4] & 0xF) != 0) {
            invalidate();
            return;
        }
    }

    @Override
    public void apply(Track track) {

    }

    @Override
    protected void reset() {

    }
}
