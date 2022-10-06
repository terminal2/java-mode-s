package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class DownlinkFormat {
    protected static final Logger logger = LoggerFactory.getLogger(DownlinkFormat.class);
    protected final short[] data;

    private final String icao;
    private ModeSDatabase.ModeSAircraft aircraft;

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

    public DownlinkFormat aircraft(ModeSDatabase.ModeSAircraft aircraft) {
        this.aircraft = aircraft == null ? new ModeSDatabase.ModeSAircraft(this.getIcao(), null, null, null) : aircraft;

        return this;
    }

    public ModeSDatabase.ModeSAircraft getAircraft() {
        return this.aircraft;
    }

    protected enum IcaoAddress {
        FROM_MESSAGE,
        FROM_PARITY,
    }
}
