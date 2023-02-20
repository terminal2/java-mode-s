package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.CprPosition;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.registers.Register05;
import aero.t2s.modes.registers.Register05V0;
import aero.t2s.modes.registers.Register05V2;

public class AirbornePosition extends ExtendedSquitter {
    private final double originLat;
    private final double originLon;

    private SurveillanceStatus surveillanceStatus;
    private int singleAntennaFlag;

    private boolean altitudeSourceBaro;
    private int altitude;

    private boolean positionAvailable;

    private CprPosition cprEven = new CprPosition();
    private CprPosition cprOdd = new CprPosition();

    private double lat;
    private double lon;

    public AirbornePosition(short[] data, final double originLat, final double originLon) {
        super(data);
        this.originLat = originLat;
        this.originLon = originLon;
    }

    @Override
    public AirbornePosition decode() {
        surveillanceStatus = SurveillanceStatus.from((data[4] >>> 1) & 0x3);
        singleAntennaFlag = data[4] & 0x1;

        // Calculate altitude
        altitude = calculateAltitude(data, typeCode);
        if (typeCode < 20) {
            altitudeSourceBaro = true;
        }

        // Decode position
        if (typeCode == 0) {
            // No position information in TC=0 packets
            return this;
        }

        int time = (data[6] >>> 3) & 0x1;
        boolean isCprEven = ((data[6] >>> 2) & 0x1) == 0;

        int cprLat = (data[6] & 0x3) << 15;
        cprLat = cprLat | (data[7] << 7);
        cprLat = cprLat | (data[8] >>> 1);

        int cprLon = ((data[8] & 0x1) << 16);
        cprLon = cprLon | (data[9] << 8);
        cprLon = cprLon | data[10];

        if (isCprEven) {
            this.cprEven.setLatLon(cprLat/(double)(1 << 17), cprLon/(double)(1 << 17));
        }
        else {
            this.cprOdd.setLatLon(cprLat, cprLon);
        }

        calculatePosition(isCprEven);

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setGroundBit(false);
        track.setSpi(surveillanceStatus == SurveillanceStatus.SPI);
        track.setTempAlert(surveillanceStatus == SurveillanceStatus.TEMPORARY_ALERT);
        track.setEmergency(surveillanceStatus == SurveillanceStatus.PERMANENT_ALERT);
        track.setCprEven(cprEven);
        track.setCprOdd(cprOdd);
        track.setLatLon(lat, lon);

        if (versionChanged(track)) {
            switch (track.getVersion()) {
                case VERSION0:
                case VERSION1:
                    track.register05(new Register05V0());
                    break;
                case VERSION2:
                    track.register05(new Register05V2());
                    break;
            }
        }
        HorizontalProtectionLimit hpl = determineHorizontalProtection(track);
        AltitudeSource altitudeSource = determineAltitudeSource();

        Register05 position = track.register05();

        if (position instanceof Register05V2) {
            position.update(hpl, altitude, altitudeSource, lat, lon, surveillanceStatus);
        } else {
            ((Register05V0) position).update(hpl, altitude, altitudeSource, lat, lon, surveillanceStatus, singleAntennaFlag == 1);
        }
    }

    private boolean versionChanged(Track track) {
        if (track.register05() == null) {
            return true;
        }

        if (track.getVersion() == Version.VERSION2 && track.register05().getVersion() != Version.VERSION2) {
            return true;
        }

        return false;
    }

    public int getSingleAntennaFlag() {
        // Version 0/1
        return singleAntennaFlag;
    }

    public BarometricAltitudeIntegrityCode getNICbaro() {
        // Version 2
        try {
            return BarometricAltitudeIntegrityCode.from(singleAntennaFlag);
        } catch (IllegalArgumentException e) {
            return BarometricAltitudeIntegrityCode.NOT_CROSS_CHECKED;
        }
    }

    public double getOriginLat() {
        return originLat;
    }

    public double getOriginLon() {
        return originLon;
    }

    public SurveillanceStatus getSurveillanceStatus() {
        return surveillanceStatus;
    }

    public boolean isAltitudeSourceBaro() {
        return altitudeSourceBaro;
    }

    public int getAltitude() {
        return altitude;
    }

    public boolean isPositionAvailable() {
        return positionAvailable;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    private HorizontalProtectionLimit determineHorizontalProtection(Track track) {
        switch (typeCode) {
            case 9:
            case 20:
                return HorizontalProtectionLimit.RC_7_5;
            case 10:
            case 21:
                return HorizontalProtectionLimit.RC_25;
            case 11:
                if (singleAntennaFlag == 1 && track.getVersion() == Version.VERSION2) {
                    return HorizontalProtectionLimit.RC_75;
                } else {
                    return HorizontalProtectionLimit.RC_185;
                }
            case 12:
                return HorizontalProtectionLimit.RC_370;
            case 13:
                if (singleAntennaFlag == 1 && track.getVersion() == Version.VERSION2) {
                    return HorizontalProtectionLimit.RC_555;
                } else {
                    return HorizontalProtectionLimit.RC_926;
                }
            case 14:
                return HorizontalProtectionLimit.RC_1852;
            case 15:
                return HorizontalProtectionLimit.RC_3704;
            case 16:
                if (singleAntennaFlag == 1 && track.getVersion() == Version.VERSION2) {
                    return HorizontalProtectionLimit.RC_7408;
                } else {
                    return HorizontalProtectionLimit.RC_14816;
                }
            case 17:
                return HorizontalProtectionLimit.RC_37040;
            case 18:
            case 22:
            default:
                return HorizontalProtectionLimit.RC_UNKNOWN;
        }
    }

    private AltitudeSource determineAltitudeSource() {
        if (typeCode < 19) {
            return AltitudeSource.BARO;
        }

        if (typeCode == 19) {
            return AltitudeSource.BARO_GNSS_DIFF;
        }

        return AltitudeSource.GNSS_HAE;
    }

    private void calculatePosition(boolean isEven) {
        if (!positionAvailable) {
            //TODO Could be other cases where we need to do global calculation, such as too much time elapsed since last position update
            calculateGlobal(cprEven, cprOdd);
            positionAvailable = true;
        }
        else {
            if (isEven) {
                if (cprOdd.isValid()) {
                    calculateLocal(cprEven, false, this.lat, this.lon);
                }
            } else {
                if (cprEven.isValid()) {
                    calculateLocal(cprOdd, true, this.lat, this.lon);
                }
            }
        }
    }

    private void calculateLocal(CprPosition cpr, boolean isOdd, double previousLat, double previousLon) {

        double dlat = isOdd ? 360.0 / 59.0 : 360.0 / 60.0;

        double j = Math.floor(previousLat / dlat) + Math.floor((previousLat % dlat) / dlat -  cpr.getLat() + 0.5);

        double newLat = dlat * (j + previousLat);

        double nl = NL(newLat) - (isOdd ? 1.0 : 0.0);
        double dlon = nl > 0 ? 360.0 / nl : 360;

        double m = Math.floor(previousLon / dlon) + Math.floor((previousLon % dlon) / dlon - cpr.getLon() + 0.5);
        double newLon = dlon * (m + lon);

        //TODO Should be a sanity-check here to make sure the calculated position isn't outside receiver origin range
        //TODO Should be a sanity-check here to see if the calculated movement since the last update is too far
        this.lat = newLat;
        this.lon = newLon;
    }

    private void calculateGlobal(CprPosition cprEven, CprPosition cprOdd) {
        double dLat0 = 360.0 / 60.0;
        double dLat1 = 360.0 / 59.0;

        double j = Math.floor(59.0 * cprEven.getLat() - 60.0 * cprOdd.getLat() + 0.5);

        double latEven = dLat0 * (j % 60.0 + cprEven.getLat());
        double latOdd = dLat1 * (j % 59.0 + cprOdd.getLat());

        if (latEven >= 270.0 && latEven <= 360.0) {
            latEven -= 360.0;
        }

        if (latOdd >= 270.0 && latOdd <= 360.0) {
            latOdd -= 360.0;
        }

        if (NL(latEven) != NL(latOdd)) {
            return;
        }

        double lat;
        double lon;
        if (cprEven.getTime() > cprOdd.getTime()) {
            double ni = cprN(latEven, 0);
            double m = Math.floor(cprEven.getLon() * (NL(latEven) - 1) - cprOdd.getLon() * NL(latEven) + 0.5);

            lat = latEven;
            lon = (360d / ni) * (m % ni + cprEven.getLon());
        } else {
            double ni = cprN(latOdd, 1);
            double m = Math.floor(cprEven.getLon() * (NL(latOdd) - 1) - cprOdd.getLon() * NL(latOdd) + 0.5);

            lat = latOdd;
            lon = (360d / ni) * (m % ni + cprOdd.getLon());
        }

        if (lon > 180d) {
            lon -= 360d;
        }

        //TODO Should be a sanity-check here to make sure the calculated position isn't outside receiver origin range,
        this.lat = lat;
        this.lon = lon;
    }
    private double cprN(double lat, double isOdd) {
        double nl = NL(lat) - isOdd;

        return nl > 1 ? nl : 1;
    }

    private double NL(double lat) {
        if (lat == 0) return 59;
        else if (Math.abs(lat) == 87) return 2;
        else if (Math.abs(lat) > 87) return 1;

        double tmp = 1 - (1 - Math.cos(Math.PI / (2.0 * 15.0))) / Math.pow(Math.cos(Math.PI / 180.0 * Math.abs(lat)), 2);
        return Math.floor(2 * Math.PI / Math.acos(tmp));
    }

    private int calculateAltitude(short[] data, int typeCode) {
        // TODO this should use AltitudeEncoding class flagged with mBit false feature.
        int n = (data[5] >>> 1) << 4;
        n = n | (data[6] >>> 4);

        int qBit = (data[5] & 0x1) == 1 ? 25 : 100; // true: 25ft, false: 100

        return (n * qBit) - 1000;
    }
}
