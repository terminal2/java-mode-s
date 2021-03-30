package aero.t2s.modes.decoder.df;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;

public class DF22 extends DownlinkFormat {
    public DF22(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF22 decode() {
        throw new NotImplementedException(getClass().getSimpleName() + ": Not implemented");
    }

    @Override
    public void apply(Track track) {
        //
    }

    @Override
    public String toString() {
        return "DF22 Military use (not implemented)";
    }
}
