package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;

public class AirborneVelocityVersion2 extends AirborneVelocity {
    public AirborneVelocityVersion2(short[] data) {
        super(data);
    }

    @Override
    public AirborneVelocityVersion2 decode() {
        return this;
    }

    @Override
    public void apply(Track track) {
    }
}
