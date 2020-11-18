package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;

public abstract class ExtendedSquitter {
    protected short[] data;

    protected int typeCode;

    public ExtendedSquitter(short[] data) {
        this.data = data;
        this.typeCode = data[4] >>> 3;
    }

    public abstract ExtendedSquitter decode();
    public abstract void apply(Track track);

    public int getTypeCode() {
        return typeCode;
    }
}
