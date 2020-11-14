package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;

public class ReservedMessage extends ExtendedSquitter {
    public ReservedMessage(short[] data) {
        super(data);
    }

    @Override
    public ReservedMessage decode() {
        return this;
    }

    @Override
    public void apply(Track track) {
        // Nothing to do
    }
}
