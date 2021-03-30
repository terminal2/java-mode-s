package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;

public class SurfacePosition extends ExtendedSquitter {
    public SurfacePosition(short[] data) {
        super(data);
    }

    @Override
    public SurfacePosition decode() {
        throw new NotImplementedException(getClass().getSimpleName() + " Not implemented");
    }

    @Override
    public void apply(Track track) {
        throw new NotImplementedException(getClass().getSimpleName() + " Not implemented");
    }

    @Override
    public String toString() {
        return "SurfacePosition";
    }
}
