package aero.t2s.modes.registers;

import aero.t2s.modes.constants.AltitudeSource;
import aero.t2s.modes.constants.HorizontalProtectionLimit;
import aero.t2s.modes.constants.SurveillanceStatus;
import aero.t2s.modes.constants.Version;

import java.time.Instant;

public abstract class Register05 extends Register {
    private SurveillanceStatus surveillanceStatus = SurveillanceStatus.NO_CONDITION;
    private int altitude = 0;
    private AltitudeSource altitudeSource = AltitudeSource.BARO;
    private double lat = 0;
    private double lon = 0;
    private HorizontalProtectionLimit hpl = HorizontalProtectionLimit.RC_UNKNOWN;
    private Instant updated = Instant.MIN;

    public SurveillanceStatus getSurveillanceStatus() {
        return surveillanceStatus;
    }

    public Register05 setSurveillanceStatus(SurveillanceStatus surveillanceStatus) {
        this.surveillanceStatus = surveillanceStatus;
        return this;
    }

    public int getAltitude() {
        return altitude;
    }

    public Register05 setAltitude(int altitude) {
        this.altitude = altitude;
        return this;
    }

    public AltitudeSource getAltitudeSource() {
        return altitudeSource;
    }

    public Register05 setAltitudeSource(AltitudeSource altitudeSource) {
        this.altitudeSource = altitudeSource;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Register05 setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Register05 setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public HorizontalProtectionLimit getHpl() {
        return hpl;
    }

    public Register05 setHpl(HorizontalProtectionLimit hpl) {
        this.hpl = hpl;
        return this;
    }

    public Instant getUpdated() {
        return updated;
    }

    public abstract Version getVersion();


    public void update(HorizontalProtectionLimit hpl, int altitude, AltitudeSource source, double lat, double lon, SurveillanceStatus surveillanceStatus) {
        this.hpl = hpl;
        this.altitude = altitude;
        this.lat = lat;
        this.lon = lon;
        this.surveillanceStatus = surveillanceStatus;
        this.updated = Instant.now();
        this.validate();
    }

    @Override
    public String toString() {
        return "Register05{\n" +
            "valid=" + isValid() +
            ",\n surveillanceStatus=" + surveillanceStatus +
            ",\n altitude=" + altitude +
            ",\n altitudeSource=" + altitudeSource.name() +
            ",\n lat=" + lat +
            ",\n lon=" + lon +
            ",\n hpl=" + hpl +
            ",\n updated=" + updated +
            "\n}";
    }
}
