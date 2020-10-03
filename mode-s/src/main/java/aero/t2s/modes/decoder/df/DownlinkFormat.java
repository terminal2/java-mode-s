package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;
import aero.t2s.modes.decoder.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class DownlinkFormat {
    protected static final Logger logger = LoggerFactory.getLogger(DownlinkFormat.class);
    private final Decoder decoder;

    public DownlinkFormat(Decoder decoder) {
        this.decoder = decoder;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public abstract Track decode(short[] data, int downlinkFormat);

    protected String getIcaoAddressFromParity(short[] data) {
        short[] payload = Arrays.copyOfRange(data, 0, data.length - 3);
        short[] parity = Arrays.copyOfRange(data, data.length - 3, data.length);

        return Common.getIcaoAddress(payload, parity);
    }

    protected String getIcaoAddress(short[] data) {
        return Common.toHexString(Arrays.copyOfRange(data, 1, 4));
    }
}
