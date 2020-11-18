package aero.t2s.modes;

public class CprPosition {
    private double lat;
    private double lon;
    private int time;

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
        return lat != 0d && lon != 0;
    }
}
