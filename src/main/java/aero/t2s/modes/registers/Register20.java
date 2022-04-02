package aero.t2s.modes.registers;

public class Register20 extends Register {
    private String acid;

    public String getAcid() {
        return acid;
    }

    public Register20 setAcid(String acid) {
        this.acid = acid;
        validate();
        return this;
    }

    @Override
    public String toString() {
        return "Register20{\n" +
            "valid=" + isValid() +
            "\nacid='" + acid + '\'' +
            "\n}";
    }
}
