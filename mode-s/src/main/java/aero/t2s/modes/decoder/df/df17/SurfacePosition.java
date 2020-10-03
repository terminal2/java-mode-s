package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import org.slf4j.LoggerFactory;

public class SurfacePosition extends ExtendedSquitter {
    @Override
    public void decode(Track track, int typeCode, short[] data) {
        LoggerFactory.getLogger(getClass()).warn("{}: Not implemented", getClass().getSimpleName());
    }
}
