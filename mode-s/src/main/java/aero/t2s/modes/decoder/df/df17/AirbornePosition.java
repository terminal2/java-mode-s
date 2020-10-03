package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.CprPosition;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Version;

public class AirbornePosition extends ExtendedSquitter {
    private final double originLat;
    private final double originLon;

    public AirbornePosition(final double originLat, final double originLon) {
        this.originLat = originLat;
        this.originLon = originLon;
    }

    @Override
    public void decode(Track track, int typeCode, short[] data) {
        track.setGroundBit(false);

        int surStatus = (data[4] >>> 1) & 0x3; // Interested in 00000xx0 and with 6 and shift by one to get x as int.
        track.setSpi(surStatus == 3);
        track.setTempAlert(surStatus = 2);
        track.setEmergency(surStatus = 1);

        // Determine Antenna or Navigation Integrity Category
        if (track.getVersion().ordinal() < Version.VERSION2.ordinal()) {
            track.setSingleAntenna((data[4] & 0x1) == 0);
        } else {
            track.setNICb(data[4] & 0x1);
        }

        track.setNIC(determineNIC(track, typeCode));

        // Calculate altitude
        int altitude = calculateAltitude(data, typeCode);
        if (typeCode < 20) {
            track.setBaroAltitude(altitude);
        } else {
            track.setGnssHeight(altitude);
        }

        // Decode position
        if (typeCode == 0) {
            // No position information in TC=0 packets
            return;
        }

        int time = (data[6] >>> 3) & 0x1;
        boolean cprEven = ((data[6] >>> 2) & 0x1) == 0;

        int cprLat = (data[6] & 0x3) << 15;
        cprLat = cprLat | (data[7] << 7);
        cprLat = cprLat | (data[8] >>> 1);

        int cprLon = ((data[8] & 0x1) << 16);
        cprLon = cprLon | (data[9] << 8);
        cprLon = cprLon | data[10];

        CprPosition cprPosition = track.getCprPosition(cprEven);
        cprPosition.setLat(((double)cprLat) / ((double)(1 << 17)));
        cprPosition.setLon(((double)cprLon) / ((double)(1 << 17)));
        cprPosition.setTime(time);

        calculatePosition(track, cprEven);
    }

    private int determineNIC(Track track, int typeCode) {
        switch (typeCode) {
            case 9:
            case 20:
                return 11;
            case 10:
            case 21:
                return 10;
            case 11:
                if (track.getNICa() == 1 && track.getNICb() == 1) {
                    return 9;
                } else if (track.getNICa() == 0 && track.getNICb() == 0) {
                    return 8;
                } else {
                    return 0;
                }
            case 12:
                return 7;
            case 13:
                return 6;
            case 14:
                return 5;
            case 15:
                return 4;
            case 16:
                if (track.getNICa() == 0 && track.getNICb() == 0) {
                    return 2;
                } else if (track.getNICa() == 1 && track.getNICb() == 1) {
                    return 3;
                } else {
                    return 0;
                }
            case 17:
                return 1;
            case 18:
            case 22:
            default:
                return 0;
        }
    }

    private void calculatePosition(Track track, boolean isEven) {
        CprPosition cprEven = track.getCprPosition(true);
        CprPosition cprOdd = track.getCprPosition(false);

        if (! (cprEven.isValid() && cprOdd.isValid())) {
            calculateLocal(track, isEven);
            return;
        }

        calculateGlobal(track, cprEven, cprOdd);
    }

    private void calculateLocal(Track track, boolean isEven) {
        boolean isOdd = !isEven;
        CprPosition cpr = track.getCprPosition(isEven);

        double dlat = isOdd ? 360.0 / 59.0 : 360.0 / 60.0;

        double j = Math.floor(originLat / dlat) + Math.floor((originLat % dlat) / dlat -  cpr.getLat() + 0.5);

        double lat = dlat * (j + cpr.getLat());

        double nl = NL(lat) - (isOdd ? 1.0 : 0.0);
        double dlon = nl > 0 ? 360.0 / nl : 360;

        double m = Math.floor(originLon / dlon) + Math.floor((originLon % dlon) / dlon - cpr.getLon() + 0.5);
        double lon = dlon * (m + cpr.getLon());

        track.setLat(lat);
        track.setLon(lon);
    }

    private void calculateGlobal(Track track, CprPosition cprEven, CprPosition cprOdd) {
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

        track.setLat(lat);
        track.setLon(lon);
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
