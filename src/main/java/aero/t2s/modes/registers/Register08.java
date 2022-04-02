package aero.t2s.modes.registers;

import aero.t2s.modes.constants.AircraftCategory;

public class Register08 extends Register {
    private String acid;
    private AircraftCategory aircraftCategory = AircraftCategory.UNKNOWN;

    public String getAcid() {
        return acid;
    }

    public Register08 setAcid(String acid) {
        this.acid = acid;
        return this;
    }

    public AircraftCategory getAircraftCategory() {
        return aircraftCategory;
    }

    public Register08 setAircraftCategory(AircraftCategory aircraftCategory) {
        this.aircraftCategory = aircraftCategory;
        return this;
    }

    public void update(String acid, AircraftCategory aircraftCategory) {
        setAcid(acid);
        setAircraftCategory(aircraftCategory);
        validate();
    }

    @Override
    public String toString() {
        return "Register08{\n" +
            "valid=" + isValid() +
            "\nacid='" + acid + '\'' +
            ",\n aircraftCategory=" + aircraftCategory +
            "\n}";
    }
}
