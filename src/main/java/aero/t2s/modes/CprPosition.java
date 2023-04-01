package aero.t2s.modes;

import java.time.Instant;

public class CprPosition {
    private double lat;
    private double lon;
    private boolean surface = false;
    private boolean valid = false;
    private double latZone;
    private double lonZone;
    private long time;

    public CprPosition() {
        this.lat = 0.0;
        this.lon = 0.0;
        this.surface = false;
        this.valid = false;
    }

    public CprPosition(double lat, double lon, boolean surface) {
        setLatLon(lat ,lon);
        this.surface = surface;
    }

    public CprPosition(double lat, double lon) {
        setLatLon(lat ,lon);
    }

    public CprPosition(int cprLat, int cprLon, boolean surface) {
        setLatLon(cprLat / (double) (1 << 17), cprLon / (double) (1 << 17));
        this.surface = surface;
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

    public void setSurface(boolean surface) {
        this.surface = surface;
    }

    public boolean getSurface() {
        return this.surface;
    }

    public void setZones(double latZone, double lonZone) {
        this.latZone = latZone;
        this.lonZone = lonZone;
    }
    public void setLatZone(double zone) {
        this.latZone = zone;
    }

    public double getLatZone() {
        return this.latZone;
    }

    public void setLonZone(double zone) {
        this.lonZone = zone;
    }

    public double getLonZone() {
        return this.lonZone;
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

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isExpired() {
        return time < Instant.now().minusSeconds(10).toEpochMilli();
    }
}
