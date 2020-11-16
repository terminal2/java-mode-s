package aero.t2s.modes.decoder.df.df17.data;

import aero.t2s.modes.constants.NavigationUncertaintyCategory;

public class SurfaceCapability {
    private boolean receive1090ES;
    private boolean lowB2Power;
    private boolean uatReceive;
    private NavigationUncertaintyCategory NACv;
    private short NICsuppC;

    public SurfaceCapability(int data) {
        receive1090ES = (data & 0b000100000000) != 0;
        lowB2Power = (data & 0b000000100000) != 0;
        uatReceive = (data & 0b000000010000) != 0;
        NACv = NavigationUncertaintyCategory.from((data & 0b000000001110) >>> 1);
        NICsuppC = (short) (data & 0b000000000001);
    }

    public boolean isReceive1090ES() {
        return receive1090ES;
    }

    public boolean isLowB2Power() {
        return lowB2Power;
    }

    public boolean isUatReceive() {
        return uatReceive;
    }

    public NavigationUncertaintyCategory getNACv() {
        return NACv;
    }

    public short getNICsuppC() {
        return NICsuppC;
    }
}
