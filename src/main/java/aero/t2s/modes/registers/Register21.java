package aero.t2s.modes.registers;

public class Register21 extends Register {
    private String aircraft;
    private String airline;

    public String getAircraft() {
        return aircraft;
    }

    public String getAirline() {
        return airline;
    }

    public void update(String aircraft, String airline) {
        this.aircraft = airline;
        this.airline = airline;
        validate();
    }

    @Override
    public String toString() {
        return "Register21{\n" +
            "valid=" + isValid() +
            ",\n aircraft='" + aircraft + '\'' +
            ",\n airline='" + airline + '\'' +
            "\n}";
    }
}
