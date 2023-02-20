package aero.t2s.modes;

public class CprPosition {
    private double lat;
    private double lon;
    private boolean valid;
    private int time;

    public CprPosition() {
        this.lat = 0.0;
        this.lon = 0.0;
        this.valid = false;
    }
    public CprPosition(double lat, double lon) {
        setLatLon(lat ,lon);
    }
    public void setLatLon(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.valid = true;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public boolean isValid() {
        return valid;
    }
}
