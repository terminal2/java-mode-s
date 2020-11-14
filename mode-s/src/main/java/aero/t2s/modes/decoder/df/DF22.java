package aero.t2s.modes.decoder.df;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Decoder;
import org.slf4j.LoggerFactory;

public class DF22 extends DownlinkFormat {
    public DF22(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF24 decode() {
        throw new NotImplementedException(getClass().getSimpleName() + ": Not implemented");
    }

    @Override
    public void apply(Track track) {
        //
    }
}
