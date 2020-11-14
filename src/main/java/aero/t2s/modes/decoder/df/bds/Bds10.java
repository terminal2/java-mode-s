package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds10 extends Bds {
    public Bds10(short[] data) {
        super(data);

        if (data[4] >>> 4 != 1 || (data[4] & 0xF) != 0) {
            invalidate();
            return;
        }

        if (((data[5] >>> 2) & 0x1F) == 0) {
            return;
        }

        invalidate();
    }

    @Override
    public void apply(Track track) {

    }

    @Override
    protected void reset() {

    }
}
