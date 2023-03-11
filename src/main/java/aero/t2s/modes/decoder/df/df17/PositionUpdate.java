package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.CprPosition;
import java.util.*;

public class PositionUpdate {
    private static double originLat;                // Origin is passed-in as a command-line argument as an indication of where the receiver is located
    private static double originLon;

    private static double receiverLat;              // Receiver position is calculated based on data received
    private static double receiverLon;
    private static double receiverSumLat;           // Running-total of valid positions that can be used to calculate the average receiver position
    private static double receiverSumLon;
    private static double receiverSumCount = 0;     // number of valid positions received that are used to calculate the average

    private static Map<String, PositionUpdate> cache = new HashMap<>();
    private static Timer cacheCleanup;

    private static final double dLatEven = 4.0 * 15.0;
    private static final double dLatOdd = 4.0 * 15.0 - 1.0;
    private static final double nlNumerator = 1 - Math.cos(Math.PI / (2.0 * 15.0));
    private static final double nlPi180 = Math.PI / 180.0;

    private CprPosition even;
    private CprPosition odd;
    private CprPosition previous;
    private CprPosition current;

    static public CprPosition calculate(String address, boolean isCprEven, CprPosition newCpr) {
        if (!cache.containsKey(address)) {
            synchronized (cache) {
                cache.putIfAbsent(address, new PositionUpdate(isCprEven, newCpr));
            }
            return null;
        }

        PositionUpdate positionUpdate;
        synchronized (cache) {
            positionUpdate = cache.get(address);
        }
        return positionUpdate.calculate(isCprEven, newCpr);
    }

    private CprPosition calculate(boolean isCprEven, CprPosition newCpr) {
        if (isCprEven) {
            even = newCpr;
            // If the even/odd frames were too many seconds apart, of if their surface bits do not match, discard the previously received frame
            if ((odd != null) && (odd.isExpired() || (odd.getSurface() != even.getSurface()))) {
                odd = null;
                previous = null;
            }
        }  else {
            odd = newCpr;
            if ((even != null) && (even.isExpired() || (even.getSurface() != odd.getSurface()))) {
                even = null;
                previous = null;
            }
        }

        if (isComplete() && previous == null) {
            // We've now got a pair of odd/even frames but there was no previous position... so we must use Global calculation to get accurate position
            calculateGlobal();
            // Update the receiver position only from Global updates (likely to be the first time updating from this aircraft)
            PositionUpdate.receiverUpdate(current.getLat(), current.getLon());
        } else if (previous != null) {
            // We had a previous accurate position, so we can use the most recent frame to do Local calculation
            calculateLocal(!isCprEven);
        } else {
            current = null;
        }

        if (current != null && current.isValid()) {
            previous = current;
        }

        return current;
    }

    private void calculateLocal(boolean isOdd) {
        CprPosition cpr = isOdd ? odd : even;
        CprPosition otherCpr = isOdd ? even : odd;
        double degrees = cpr.getSurface() ? 90.0 : 360.0;
        double dLat = degrees / (isOdd ? dLatOdd : dLatEven);
        double j = Math.floor(previous.getLat() / dLat) + Math.floor(cprMod(previous.getLat(), dLat) / dLat -  cpr.getLat() + 0.5);
        double newLat = dLat * (j + cpr.getLat());
        if (newLat < 0) {
            // Something screwed-up
            current = null;
            return;
        }

        double nl = NL(newLat) - (isOdd ? 1.0 : 0.0);
        double dLon = (nl > 0) ? degrees / nl : degrees;
        double m = Math.floor(previous.getLon() / dLon) + Math.floor(cprMod(previous.getLon(), dLon) / dLon - cpr.getLon() + 0.5);
        double newLon = dLon * (m + cpr.getLon());

        cpr.setZones(j, m);
        if ((j != otherCpr.getLatZone()) || (m != otherCpr.getLonZone())) {
            // The new frame is in a different CPR zone
            if (isOdd) {
                //even = null;        // Keep the current odd frame but discard the previous even frame
            } else {
                //odd = null;
            }
            //previous = null;
            //current = null;
            //return;
        }

        current = new CprPosition(newLat, newLon, cpr.getSurface());
        if (current.getSurface()) {
            validateSurface(current);
        }

        // TODO Should be a sanity-check here to make sure the calculated position isn't outside receiver origin range
        // TODO Should be a sanity-check here to see if the calculated movement since the last update is too far
    }

    private void calculateGlobal() {
        double j = Math.floor(59.0 * even.getLat() - 60.0 * odd.getLat() + 0.5);
        double degrees = even.getSurface() ? 90.0 : 360.0;  // Doesn't matter whether we check odd or even as they must both match by now

        double latEven = (degrees / dLatEven) * (cprMod(j, dLatEven) + even.getLat());
        double latOdd = (degrees / dLatOdd) * (cprMod(j, dLatOdd) + odd.getLat());

        latEven = normaliseLat(latEven);
        latOdd = normaliseLat(latOdd);

        double nlLatEven = NL(latEven);
        double nlLatOdd = NL(latOdd);

        if (nlLatEven != nlLatOdd) {
            // The even and odd frames are in different Longitude zones, so we need to discard BOTH
            odd = null;
            even = null;
            previous = null;
            current = null;
            return;
        }

        double newLat, newLon, ni, m;
        if (even.getTime() > odd.getTime()) {
            ni = cprN(nlLatEven, 0);
            m = Math.floor(even.getLon() * (nlLatEven - 1) - odd.getLon() * nlLatEven + 0.5);

            newLat = latEven;
            newLon = (degrees / ni) * (cprMod(m, ni) + even.getLon());
        } else {
            ni = cprN(nlLatOdd, 1);
            m = Math.floor(even.getLon() * (nlLatOdd - 1) - odd.getLon() * nlLatOdd + 0.5);

            newLat = latOdd;
            newLon = (degrees / ni) * (cprMod(m, ni) + odd.getLon());
        }
        newLon = normaliseLon(newLon);

        even.setZones(nlLatEven, m);
        odd.setZones(nlLatOdd, m);

        current = new CprPosition(newLat, newLon, even.getSurface());
        if (current.getSurface()) {
            validateSurface(current);
        }
        //TODO Should be a sanity-check here to make sure the calculated position isn't outside receiver origin range
    }

    static private double cprMod(double a, double b) {
        return a - b * Math.floor(a/b);
    }

    static private double cprN(double nlLat, double isOdd) {
        return Math.max(1, nlLat - isOdd);
    }

    static private double NL(double lat) {
        if (lat == 0) return 59;
        else if (Math.abs(lat) == 87) return 2;
        else if (Math.abs(lat) > 87) return 1;

        double tmpCos = Math.cos(nlPi180 * Math.abs(lat));
        double tmpArc = 1 - nlNumerator / (tmpCos * tmpCos);
        return Math.floor(2 * Math.PI / Math.acos(tmpArc));
    }

    public PositionUpdate(boolean isEven, CprPosition cpr) {
        if (isEven) {
            this.even = cpr;
        } else {
            this.odd = cpr;
        }
    }

    public boolean isComplete() {
        return even != null && odd != null;
    }

    public boolean isExpired() {
        return even.isExpired() || odd.isExpired();
    }

    static public double normaliseLat(double lat) {
        if (lat >= 270.0 && lat <= 360.0) {
            lat -= 360.0;
        }
        return lat;
    }

    static public double normaliseLon(double lon) {
        if (lon > 180.0) {
            lon -= 360.0;
        }
        return lon;
    }

    private void validateSurface(CprPosition pos) {
        // For SurfacePositions we get 8 possible solutions: 2x latitude, 4x longitude zones at 90 degree increments
        double diff;
        double test;
        double testDiff;

        // Pick the lat which is closest to the receiver
        test = pos.getLat();
        diff = Math.abs(normaliseLat(test) - receiverLat);
        for (int i = 0 ; i < 1 ; i++){
            test = normaliseLat(test + 90.0);
            testDiff = Math.abs(test - receiverLat);
            if (testDiff < diff) {
                diff = testDiff;
                pos.setLat(test);
            }
        }

        // Pick the lon which is closest to the receiver
        test = pos.getLon();
        diff = Math.abs(normaliseLon(test) - receiverLon);
        for (int i = 0 ; i < 3 ; i++){
            test = normaliseLon(test + 90.0);
            testDiff = Math.abs(test - receiverLon);
            if (testDiff < diff) {
                diff = testDiff;
                pos.setLon(test);
            }
        }
    }

    static private void receiverUpdate(double lat, double lon) {
        if (!((lat == 0) && (lon == 0))) {
            receiverSumCount ++;
            receiverSumLat += lat;
            receiverSumLon += lon;
            receiverLat = receiverSumLat / receiverSumCount;
            receiverLon = receiverSumLon / receiverSumCount;
        }
    }

    static public void start(double originLat, double originLon) {
        PositionUpdate.originLat = originLat;
        PositionUpdate.originLon = originLon;

        receiverUpdate(originLat, originLon);

        cache.clear();
        cacheCleanup.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> expired = new LinkedList<>();

                synchronized (cache) {
                    cache.entrySet().stream().filter(entry -> entry.getValue().isExpired()).forEach(entry -> expired.add(entry.getKey()));
                    expired.forEach(cache::remove);
                }
            }
        }, 0, 10_000);
    }

    static public void stop() {
        cacheCleanup.cancel();
        cacheCleanup = null;

        cache.clear();
    }
}
