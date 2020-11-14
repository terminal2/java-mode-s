package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Capability;
import aero.t2s.modes.decoder.Common;

public class DF11 extends DownlinkFormat {
    private Capability capability;
    private String address;

    public DF11(short[] data) {
        super(data, IcaoAddress.FROM_MESSAGE);
    }

    @Override
    public DF11 decode() {
        capability = Capability.from(data[0] & 0b00000111);
        address = Common.toHexString(new short[] {
            data[1],
            data[2],
            data[3],
            data[4],
        });

        return this;
    }

    @Override
    public void apply(Track track) {

    }

    public Capability getCapability() {
        return capability;
    }

    public String getAddress() {
        return address;
    }
}
