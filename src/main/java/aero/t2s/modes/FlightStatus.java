package aero.t2s.modes;

public class FlightStatus {
    private boolean alert;
    private boolean spi;

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setSpi(boolean spi) {
        this.spi = spi;
    }

    public boolean isSpi() {
        return spi;
    }
}
