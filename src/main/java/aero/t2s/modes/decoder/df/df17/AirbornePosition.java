package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.CprPosition;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.registers.Register05;
import aero.t2s.modes.registers.Register05V0;
import aero.t2s.modes.registers.Register05V2;

import java.util.*;

public class AirbornePosition extends ExtendedSquitter {
    private final String address;
    private SurveillanceStatus surveillanceStatus;
    private int singleAntennaFlag;

    private boolean altitudeSourceBaro;
    private int altitude;

    private double lat;
    private double lon;
    private boolean positionAvailable;

    public AirbornePosition(short[] data, String address) {
        super(data);
        this.address = address;
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

        CprPosition newPosition = PositionUpdate.calculate(address, isCprEven, new CprPosition(cprLat, cprLon, false));
        if (newPosition != null) {
            this.lat = newPosition.getLat();
            this.lon = newPosition.getLon();
            this.positionAvailable = true;
        } else {
            this.positionAvailable = false;
        }
        return this;
    }

    @Override
    public void apply(Track track) {
        track.setGroundBit(false);
        track.setSpi(surveillanceStatus == SurveillanceStatus.SPI);
        track.setTempAlert(surveillanceStatus == SurveillanceStatus.TEMPORARY_ALERT);
        track.setEmergency(surveillanceStatus == SurveillanceStatus.PERMANENT_ALERT);
        if (positionAvailable) {
            track.setLatLon(lat, lon);
        }

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

    private int calculateAltitude(short[] data, int typeCode) {
        // TODO this should use AltitudeEncoding class flagged with mBit false feature.
        int n = (data[5] >>> 1) << 4;
        n = n | (data[6] >>> 4);

        int qBit = (data[5] & 0x1) == 1 ? 25 : 100; // true: 25ft, false: 100

        return (n * qBit) - 1000;
    }
}
