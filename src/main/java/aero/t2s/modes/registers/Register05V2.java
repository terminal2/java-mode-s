package aero.t2s.modes.registers;

import aero.t2s.modes.constants.AltitudeSource;
import aero.t2s.modes.constants.HorizontalProtectionLimit;
import aero.t2s.modes.constants.SurveillanceStatus;
import aero.t2s.modes.constants.Version;

public class Register05V2 extends Register05 {
    @Override
    public Version getVersion() {
        return Version.VERSION2;
    }

    @Override
    public void update(HorizontalProtectionLimit hpl, int altitude, AltitudeSource altitudeSource, double lat, double lon, SurveillanceStatus surveillanceStatus) {
        super.update(hpl, altitude, altitudeSource, lat, lon, surveillanceStatus);
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "Register05V2 {}";
    }
}
