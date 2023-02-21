package aero.t2s.modes;

import aero.t2s.modes.constants.*;
import aero.t2s.modes.registers.*;

import java.time.Instant;

public class Track {
    private String icao;
    private String callsign;
    private Altitude altitude = new Altitude();
    private double lat;
    private double lon;
    private boolean positionAvailable = false;
    private int vx;
    private int vy;
    private double gs;
    private Version version = Version.VERSION0;
    private boolean groundBit;
    Instant updated = Instant.now();
    private boolean wasJustCreated = true;

    private Register05 register05 = new Register05V0();
    private Register06 register06 = new Register06();
    private Register07 register07 = new Register07();
    private Register08 register08 = new Register08();
    private Register09 register09 = new Register09();
    private Register17 register17 = new Register17();
    private Register20 register20 = new Register20();
    private Register21 register21 = new Register21();

    private boolean spi;
    private boolean tempAlert;
    private boolean emergency;
    private Acas acas = new Acas();
    private FlightStatus flightStatus = new FlightStatus();
    private SelectedAltitudeSource selectedAltitudeSource = SelectedAltitudeSource.UNKNOWN;
    private Meteo meteo = new Meteo();
    private int modeA;
    private int geometricHeightOffset;
    private int rocd;
    private boolean rocdAvailable;
    private boolean rocdSourceBaro;

    private double magneticHeading;
    private double trueHeading;
    private int ias;
    private double tas;
    private boolean selectedAltitudeManagedFms;
    private boolean selectedAltitudeManagedMcp;
    private int selectedAltitude;
    private double baroSetting;
    private double selectedHeading;
    private int sil;
    private boolean autopilot;
    private boolean vnav;
    private boolean altitudeHold;
    private boolean approachMode;
    private boolean lnav;
    private EmergencyState emergencyState = EmergencyState.NONE;
    private int fmsSelectedAltitude;
    private double rollAngle;
    private double trackAngleRate;
    private double mach;
    private double baroRocd;
    private String atype = "";
    private String wtc = "";
    private String registration;
    private String operator;

    public Track(String icao) {
        this.icao = icao;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getCallsign() {
        return callsign;
    }

    public Register05 register05() {
        return register05;
    }

    public void register05(Register05 register05) {
        this.register05 = register05;
    }

    public Register06 register06() {
        return register06;
    }

    public void register06(Register06 register06) {
        this.register06 = register06;
    }

    public Register07 register07() {
        return register07;
    }

    public void register07(Register07 register07) {
        this.register07 = register07;
    }

    public Register08 register08() {
        return register08;
    }

    public void register08(Register08 register08) {
        this.register08 = register08;
    }

    public Register09 register09() {
        return register09;
    }

    public void register09(Register09 register09) {
        this.register09 = register09;
    }

    public Register17 register17() {
        return register17;
    }

    public void register17(Register17 register17) {
        this.register17 = register17;
    }

    public Register20 register20() {
        return register20;
    }

    public void register20(Register20 register20) {
        this.register20 = register20;
    }

    public Register21 register21() {
        return register21;
    }

    public void register21(Register21 register21) {
        this.register21 = register21;
    }

    public String getIcao() {
        return icao;
    }

    public boolean isExpired() {
        return Instant.now().minusSeconds(15).isAfter(updated);
    }

    public Instant getUpdatedAt() {
        return updated;
    }

    public Track setUpdatedAt(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void touch() {
        updated = Instant.now();
    }

    public void setGroundBit(boolean groundBit) {
        this.groundBit = groundBit;
    }

    public boolean isGroundBit() {
        return groundBit;
    }

    public void setLatLon(double lat, double lon) {
        this.lon = lat;
        this.lon = lon;
        this.positionAvailable = true;
    }
    public void setLat(double lat) {
        //TODO How do we know if position really is available if we only set the lat? Can we remove this method?
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLon(double lon) {
        //TODO How do we know if position really is available if we only set the lon? Can we remove this method?
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setSpi(boolean spi) {
        this.spi = spi;
    }

    public boolean getSpi() {
        return spi;
    }

    public void setTempAlert(boolean tempAlert) {
        this.tempAlert = tempAlert;
    }

    public boolean getTempAlert() {
        return tempAlert;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public boolean getEmergency() {
        return emergency;
    }

    public Acas getAcas() {
        return acas;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public void setAltitude(Altitude altitude) {
        this.altitude = altitude;
    }

    public Altitude getAltitude() {
        return altitude;
    }

    public void setModeA(int modeA) {
        this.modeA = modeA;
    }

    public int getModeA() {
        return modeA;
    }

    public boolean isPositionAvailable() {
        return positionAvailable;
    }

    public void setGeometricHeightOffset(int geometricHeightOffset) {
        this.geometricHeightOffset = geometricHeightOffset;
    }

    public int getGeometricHeightOffset() {
        return geometricHeightOffset;
    }

    public void setRocd(int rocd) {
        this.rocd = rocd;
    }

    public int getRocd() {
        return rocd;
    }

    public void setRocdAvailable(boolean rocdAvailable) {
        this.rocdAvailable = rocdAvailable;
    }

    public boolean getRocdAvailable() {
        return rocdAvailable;
    }

    public void setRocdSourceBaro(boolean rocdSourceBaro) {
        this.rocdSourceBaro = rocdSourceBaro;
    }

    public boolean getRocdSourceBaro() {
        return rocdSourceBaro;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVx() {
        return vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public int getVy() {
        return vy;
    }

    public void setGs(double gs) {
        this.gs = gs;
    }

    public double getGs() {
        return gs;
    }

    public void setMagneticHeading(double magneticHeading) {
        this.magneticHeading = magneticHeading;
    }

    public double getMagneticHeading() {
        return magneticHeading;
    }

    public void setTrueHeading(double trueHeading) {
        this.trueHeading = trueHeading;
    }

    public double getTrueHeading() {
        return trueHeading;
    }

    public void setIas(int ias) {
        this.ias = ias;
    }

    public int getIas() {
        return ias;
    }

    public void setTas(double tas) {
        this.tas = tas;
    }

    public double getTas() {
        return tas;
    }

    public void setSelectedAltitudeManagedFms(boolean selectedAltitudeManagedFms) {
        this.selectedAltitudeManagedFms = selectedAltitudeManagedFms;
    }

    public boolean getSelectedAltitudeManagedFms() {
        return selectedAltitudeManagedFms;
    }

    public void setSelectedAltitudeManagedMcp(boolean selectedAltitudeManagedMcp) {
        this.selectedAltitudeManagedMcp = selectedAltitudeManagedMcp;
    }

    public boolean getSelectedAltitudeManagedMcp() {
        return selectedAltitudeManagedMcp;
    }

    public void setSelectedAltitude(int selectedAltitude) {
        this.selectedAltitude = selectedAltitude;
    }

    public int getSelectedAltitude() {
        return selectedAltitude;
    }

    public void setBaroSetting(double baroSetting) {
        this.baroSetting = baroSetting;
    }

    public double getBaroSetting() {
        return baroSetting;
    }

    public void setSelectedHeading(double selectedHeading) {
        this.selectedHeading = selectedHeading;
    }

    public double getSelectedHeading() {
        return selectedHeading;
    }

    public void setSil(int sil) {
        this.sil = sil;
    }

    public int getSil() {
        return sil;
    }

    public void setAutopilot(boolean autopilot) {
        this.autopilot = autopilot;
    }

    public boolean getAutopilot() {
        return autopilot;
    }

    public void setVnav(boolean vnav) {
        this.vnav = vnav;
    }

    public boolean getVnav() {
        return vnav;
    }

    public void setAltitudeHold(boolean altitudeHold) {
        this.altitudeHold = altitudeHold;
    }

    public boolean getAltitudeHold() {
        return altitudeHold;
    }

    public void setApproachMode(boolean approachMode) {
        this.approachMode = approachMode;
    }

    public boolean getApproachMode() {
        return approachMode;
    }

    public void setLnav(boolean lnav) {
        this.lnav = lnav;
    }

    public boolean getLnav() {
        return lnav;
    }

    public void setEmergencyState(EmergencyState emergencyState) {
        this.emergencyState = emergencyState;
    }

    public EmergencyState getEmergencyState() {
        return emergencyState;
    }

    public void setFmsSelectedAltitude(int fmsSelectedAltitude) {
        this.fmsSelectedAltitude = fmsSelectedAltitude;
    }

    public int getFmsSelectedAltitude() {
        return fmsSelectedAltitude;
    }

    public void setSelectedAltitudeSource(SelectedAltitudeSource selectedAltitudeSource) {
        this.selectedAltitudeSource = selectedAltitudeSource;
    }

    public SelectedAltitudeSource getSelectedAltitudeSource() {
        return selectedAltitudeSource;
    }

    public void setRollAngle(double rollAngle) {
        this.rollAngle = rollAngle;
    }

    public double getRollAngle() {
        return rollAngle;
    }

    public void setTrackAngleRate(double trackAngleRate) {
        this.trackAngleRate = trackAngleRate;
    }

    public double getTrackAngleRate() {
        return trackAngleRate;
    }

    public void setMach(double mach) {
        this.mach = mach;
    }

    public double getMach() {
        return mach;
    }

    public void setBaroRocd(double baroRocd) {
        this.baroRocd = baroRocd;
    }

    public double getBaroRocd() {
        return baroRocd;
    }

    public String getAtype() {
        return atype;
    }

    public Track setAtype(String atype) {
        this.atype = atype;
        return this;
    }

    public String getWtc() {
        return wtc;
    }

    public Track setWtc(String wtc) {
        this.wtc = wtc;
        return this;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getRegistration() {
        return registration;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public boolean wasJustCreated() {
        return wasJustCreated;
    }

    public void setWasJustCreated(boolean state) {
        this.wasJustCreated = state;
    }

    public Meteo getMeteo() {
        return meteo;
    }

    @Override
    public String toString() {
        return String.format(
            "%8s(%s) | %6.0f%s | %4.0fft/min| %4.0fKT | LAT: %02.4f LON: %03.4f",
            callsign != null ? callsign : "",
            icao,
            altitude.getAltitude(),
            altitude.isMetric() ? "M " : "FT",
            rocdAvailable ? rocd : 0f,
            gs,
            lat,
            lon
        );
    }
}
