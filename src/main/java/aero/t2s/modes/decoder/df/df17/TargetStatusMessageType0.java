package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;

public class TargetStatusMessageType0 extends TargetStatusMessage {
    private int subType = 0;

    public TargetStatusMessageType0(short[] data) {
        super(data);
    }

    @Override
    public TargetStatusMessageType0 decode() {
        throw new NotImplementedException("BDS6,2 Target State and Status information sub type 0 is not implemented");
    }

    @Override
    public void apply(Track track) {
        throw new NotImplementedException("BDS6,2 Target State and Status information sub type 0 is not implemented");
    }

    public int getSubType() {
        return subType;
    }
}
