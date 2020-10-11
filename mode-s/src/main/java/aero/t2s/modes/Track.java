package aero.t2s.modes;

import aero.t2s.modes.constants.EmergencyState;
import aero.t2s.modes.constants.LengthWidthCode;
import aero.t2s.modes.constants.SelectedAltitudeSource;
import aero.t2s.modes.constants.Version;

import java.time.Instant;

public class Track {
    private String icao;
    private String callsign;
    private int category;
    private boolean groundBit;
    private int baroAltitude;
    private int gnssHeight;
    private double lat;
    private double lon;
    private CprPosition cprPositionEven = new CprPosition();
    private CprPosition cprPositionOdd = new CprPosition();
    Instant updated = Instant.now();
    private boolean singleAntenna;
    private int NIC;
    private int NICb;
    private int NICa;
    private int NICc;
    private RadiusLimit rc = new RadiusLimit(this);
    private int NACv;
    private boolean spi;
    private int tempAlert;
    private int emergency;
    private Version version = Version.VERSION0;
    private Acas acas = new Acas();
    private FlightStatus flightStatus = new FlightStatus();
    private Altitude altitude = new Altitude();
    private int modeA;
    private int geometricHeightOffset;
    private int rocd;
    private boolean rocdAvailable;
    private boolean rocdSourceBaro;
    private int vx;
    private int vy;
    private double gs;
    private boolean headingSourceMagnetic;
    private double magneticHeading;
    private double trueHeading;
    private boolean iasAvailable;
    private int ias;
    private double tas;
    private boolean selectedAltitudeManagedFms;
    private boolean selectedAltitudeManagedMcp;
    private int selectedAltitude;
    private double baroSetting;
    private double selectedHeading;
    private boolean validStatus;
    private int sil;
    private boolean autopilot;
    private boolean vnav;
    private boolean altitudeHold;
    private boolean approachMode;
    private boolean lnav;
    private LengthWidthCode lengthWidthCode = LengthWidthCode.CAT15;
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

    private boolean wasJustCreated = true;
    private SelectedAltitudeSource selectedAltitudeSource = SelectedAltitudeSource.UNKNOWN;

    public Track(String icao) {
        this.icao = icao;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public void touch() {
        updated = Instant.now();
    }

    public void setGroundBit(boolean groundBit) {
        this.groundBit = groundBit;
    }

    public boolean isGroundBit() {
        return groundBit;
    }

    public void setBaroAltitude(int baroAltitude) {
        this.baroAltitude = baroAltitude;
    }

    public int getBaroAltitude() {
        return baroAltitude;
    }

    public int getGnssHeight() {
        return gnssHeight;
    }

    public Track setGnssHeight(int gnssHeight) {
        this.gnssHeight = gnssHeight;
        return this;
    }

    public CprPosition getCprPosition(boolean cprEven) {
        return cprEven ? cprPositionEven : cprPositionOdd;
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

    public void setSingleAntenna(boolean singleAntenna) {
        this.singleAntenna = singleAntenna;
    }

    public boolean getSingleAntenna() {
        return singleAntenna;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public int getNIC() {
        return NIC;
    }

    public int getNICa() {
        return NICa;
    }

    public int getNICb() {
        return NICb;
    }

    public int getNICc() {
        return NICc;
    }

    public void setNIC(int NIC) {
        if (this.NIC != NIC) {
            this.NIC = NIC;
            rc.determine();
        }
    }


    public void setNICa(int NICa) {
        if (this.NICa != NICa) {
            this.NICa = NICa;
            rc.determine();
        }
    }

    public void setNICb(int niCb) {
        if (niCb != this.NICb) {
            this.NICb = niCb;
            rc.determine();
        }
    }

    public void setNICc(int NICc) {
        if (this.NICc != NICc) {
            this.NICc = NICc;
            rc.determine();
        }
    }

    public void setSpi(boolean spi) {
        this.spi = spi;
    }

    public boolean getSpi() {
        return spi;
    }

    public void setTempAlert(int tempAlert) {
        this.tempAlert = tempAlert;
    }

    public int getTempAlert() {
        return tempAlert;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public int getEmergency() {
        return emergency;
    }

    public Acas getAcas() {
        return acas;
    }

    public void setAcas(Acas acas) {
        this.acas = acas;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(FlightStatus flightStatus) {
        this.flightStatus = flightStatus;
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
        return lat != 0 & lon != 0;
    }

    public void setNACv(int naCv) {
        this.NACv = naCv;
    }

    public int getNACv() {
        return NACv;
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

    public boolean isMagneticHeading() {
        return headingSourceMagnetic;
    }

    public void setHeadingSource(boolean magneticHeading) {
        this.headingSourceMagnetic = magneticHeading;
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

    public void setIasAvailable(boolean iasAvailable) {
        this.iasAvailable = iasAvailable;
    }

    public boolean isIasAvailable() {
        return iasAvailable;
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

    public void setValidStatus(boolean validStatus) {
        this.validStatus = validStatus;
    }

    public boolean getValidStatus() {
        return validStatus;
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

    public void setLengthWidthCode(LengthWidthCode lengthWidthCode) {
        this.lengthWidthCode = lengthWidthCode;
    }

    public LengthWidthCode getLengthWidthCode() {
        return lengthWidthCode;
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
