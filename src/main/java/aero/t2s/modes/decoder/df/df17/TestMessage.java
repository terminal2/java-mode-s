package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;

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

    @Override
    public String toString() {
        return "Test Message\n" + Common.toHexString(data);
    }
}
