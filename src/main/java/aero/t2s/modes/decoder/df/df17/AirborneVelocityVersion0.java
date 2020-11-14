package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;

public class AirborneVelocityVersion0 extends AirborneVelocity {
    public AirborneVelocityVersion0(short[] data) {
        super(data);
    }

    @Override
    public AirborneVelocityVersion0 decode() {
        return this;
    }

    @Override
    public void apply(Track track) {
    }
}
