package aero.t2s.modes.registers;

import aero.t2s.modes.constants.AltitudeSource;
import aero.t2s.modes.constants.TransmissionRate;

public class Register07 extends Register {
    private TransmissionRate transmissionRate = TransmissionRate.UNKNOWN;
    private AltitudeSource altitudeType = AltitudeSource.BARO;

    public TransmissionRate getTransmissionRate() {
        return transmissionRate;
    }

    public Register07 setTransmissionRate(TransmissionRate transmissionRate) {
        this.transmissionRate = transmissionRate;
        return this;
    }

    public AltitudeSource getAltitudeType() {
        return altitudeType;
    }

    public Register07 setAltitudeType(AltitudeSource altitudeType) {
        this.altitudeType = altitudeType;
        return this;
    }

    public void update(TransmissionRate trs, AltitudeSource altitudeType) {
        setTransmissionRate(trs);
        setAltitudeType(altitudeType);
        validate();
    }

    @Override
    public String toString() {
        return "Register07{\n" +
            "valid=" + isValid() +
            ",\ntransmissionRate=" + transmissionRate +
            ",\n altitudeType=" + altitudeType +
            "\n}";
    }
}
