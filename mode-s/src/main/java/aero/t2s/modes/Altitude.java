package aero.t2s.modes;

public class Altitude {
    private double altitude;
    private boolean isMetric;
    private int step;
    private boolean isValid;

    public Altitude(double altitude, boolean isMetric, int step) {
        this.altitude = altitude;
        this.isMetric = isMetric;
        this.step = step;
        this.isValid = true;
    }

    public Altitude() {
        this.isValid = false;
    }

    public double getAltitude() {
        return altitude;
    }

    public boolean isMetric() {
        return isMetric;
    }

    public int getStep() {
        return step;
    }

    public boolean isValid() {
        return isValid;
    }
}
