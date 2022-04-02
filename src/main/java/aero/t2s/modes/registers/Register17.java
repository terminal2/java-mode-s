package aero.t2s.modes.registers;

import aero.t2s.modes.CapabilityReport;
import aero.t2s.modes.decoder.df.bds.Bds17;

public class Register17 extends Register {
    private CapabilityReport capabilityReport = new CapabilityReport();

    public CapabilityReport getCapabilityReport() {
        return capabilityReport;
    }

    public Register17 update(Bds17 bds) {
        capabilityReport.update(bds);
        this.validate();
        return this;
    }

    @Override
    public String toString() {
        return "Register17{\n" +
            "valid=" + isValid() +
            "\n" + capabilityReport.toString() +
            "\n}";
    }
}
