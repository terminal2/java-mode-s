package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Decoder;
import org.slf4j.LoggerFactory;

public class DF24 extends DownlinkFormat {
    public DF24(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Track decode(short[] data, int downlinkFormat) {
        LoggerFactory.getLogger(getClass()).warn("{}: Not implemented", getClass().getSimpleName());

        return null;
    }
}
