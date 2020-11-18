package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;

public class TargetStatusMessage extends ExtendedSquitter {
    public TargetStatusMessage(short[] data) {
        super(data);
    }

    @Override
    public TargetStatusMessage decode() {
        int subtype = (data[4] >>> 1) & 0x3;

        if (subtype == 0)
            return new TargetStatusMessageType0(data).decode();
        else if (subtype == 1)
            return new TargetStatusMessageType1(data).decode();
        else
            throw new NotImplementedException("BDS6,2 Subtype " + subtype + " is not implemented");
    }

    @Override
    public void apply(Track track) {
        // Can't apply base message
    }
}
