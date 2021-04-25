package aero.t2s.modes;

public class BaroAltitude extends Altitude {
    private double baroSetting;

    public BaroAltitude(double altitude, double baroSetting) {
        super(altitude, false, 1);
        this.baroSetting = baroSetting;
    }

    public BaroAltitude() {
        super();
        this.baroSetting = 1013.2;
    }

    public double getBaroSetting() {
        return baroSetting;
    }

    public BaroAltitude setBaroSetting(double baroSetting) {
        this.baroSetting = baroSetting;
        return this;
    }

    @Override
    public String toString() {
        return getAltitude() + "FT (Q:" + getBaroSetting() + " hpa)";
    }
}
