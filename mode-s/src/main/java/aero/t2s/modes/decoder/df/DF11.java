package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Decoder;

public class DF11 extends DownlinkFormat {
    public DF11(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Track decode(short[] data, int downlinkFormat) {
        return getDecoder().getTrack(getIcaoAddress(data));
    }
}
