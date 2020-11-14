package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;

public class SurfaceSystemStatus extends ExtendedSquitter {
    public SurfaceSystemStatus(short[] data) {
        super(data);
    }

    @Override
    public SurfaceSystemStatus decode() {
        throw new NotImplementedException(getClass().getSimpleName() + " Not implemented");
    }

    @Override
    public void apply(Track track) {
        throw new NotImplementedException(getClass().getSimpleName() + " Not implemented");
    }
}
