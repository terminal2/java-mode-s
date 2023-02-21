package aero.t2s.modes;

import java.time.Instant;

public class CprPosition {
    private double lat;
    private double lon;
    private boolean valid;
    private long time;

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
        this.time = Instant.now().toEpochMilli();
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

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isExpired() {
        return time < Instant.now().minusSeconds(10).toEpochMilli();
    }
}
