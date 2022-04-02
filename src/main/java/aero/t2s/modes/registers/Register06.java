package aero.t2s.modes.registers;

public class Register06 extends Register {
    private double groundSpeed;
    private double groundTrack;
    private double lat;
    private double lon;

    @Override
    public String toString() {
        return "Register06{\n" +
            "valid=" + isValid() +
            ",\n groundSpeed=" + groundSpeed +
            ",\n groundTrack=" + groundTrack +
            ",\n lat=" + lat +
            ",\n lon=" + lon +
            "\n}";
    }
}
