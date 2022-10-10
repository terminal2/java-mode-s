package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;

public class DF24 extends DownlinkFormat {
    private int sequenceNo = 0;

    public DF24(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF24 decode() {
        sequenceNo = data[0] & 0b00000111;

        return this;
    }

    @Override
    public void apply(Track track) {
        // Not implemented
    }

    public int getSequenceNo() {
        return sequenceNo;
    }
}
