package aero.t2s.modes.registers;

import aero.t2s.modes.constants.AltitudeSource;
import aero.t2s.modes.constants.HorizontalProtectionLimit;
import aero.t2s.modes.constants.SurveillanceStatus;
import aero.t2s.modes.constants.Version;

public class Register05V0 extends Register05 {
    private boolean singleAntennaFlag = false;

    public boolean isSingleAntennaFlag() {
        return singleAntennaFlag;
    }

    public Register05V0 setSingleAntennaFlag(boolean singleAntennaFlag) {
        this.singleAntennaFlag = singleAntennaFlag;
        return this;
    }

    @Override
    public Version getVersion() {
        return Version.VERSION0;
    }

    public void update(HorizontalProtectionLimit hpl, int altitude, AltitudeSource altitudeSource, double lat, double lon, SurveillanceStatus surveillanceStatus, boolean singleAntennaFlag) {
        super.update(hpl, altitude, altitudeSource, lat, lon, surveillanceStatus);
        this.singleAntennaFlag = singleAntennaFlag;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "Register05V0{" +
            "\nsingleAntennaFlag=" + singleAntennaFlag +
            "\n}";
    }
}
