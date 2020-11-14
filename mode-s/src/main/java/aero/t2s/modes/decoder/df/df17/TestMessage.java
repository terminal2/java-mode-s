package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;

public class TestMessage extends ExtendedSquitter {
    public TestMessage(short[] data) {
        super(data);
    }

    @Override
    public ExtendedSquitter decode() {
        return this;
    }

    @Override
    public void apply(Track track) {
        // Nothing to do
    }
}
