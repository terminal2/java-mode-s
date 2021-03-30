package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class DownlinkFormat {
    protected static final Logger logger = LoggerFactory.getLogger(DownlinkFormat.class);
    protected final short[] data;

    private final String icao;

    public DownlinkFormat(short[] data, IcaoAddress icaoAddressFrom) {
        this.data = data;

        if (icaoAddressFrom == IcaoAddress.FROM_PARITY) {
            this.icao = Common.getIcaoAddressFromParity(data);
        } else {
            this.icao = Common.toHexString(Arrays.copyOfRange(data, 1, 4));
        }
    }

    public abstract DownlinkFormat decode();

    public abstract void apply(Track track);

    public String getIcao() {
        return this.icao;
    }

    public short[] getData() {
        return data;
    }

    protected enum IcaoAddress {
        FROM_MESSAGE,
        FROM_PARITY,
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + getIcao() + ")";
    }
}
