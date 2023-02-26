package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.CprPosition;
import java.util.*;

public class PositionUpdate {
    static Map<String, PositionUpdate> cache = new HashMap<>();
    private static Timer cacheCleanup;

    static final private double dLatEven = 4.0 * 15.0;
    static final private double dLatOdd = 4.0 * 15.0 - 1.0;
    static final private double nlNumerator = 1 - Math.cos(Math.PI / (2.0 * 15.0));
    static final private double nlPi180 = Math.PI / 180.0;

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

        // TODO Should be a sanity-check here to make sure the calculated position isn't outside receiver origin range
        // TODO Should be a sanity-check here to see if the calculated movement since the last update is too far

        current = new CprPosition(newLat, newLon, cpr.getSurface());
        if (current.getSurface()) {
            current.validateSurface();
        }

    }

    private void calculateGlobal() {
        double j = Math.floor(59.0 * even.getLat() - 60.0 * odd.getLat() + 0.5);
        double degrees = even.getSurface() ? 90.0 : 360.0;  // Doesn't matter whether we check odd or even as they must both match by now

        double latEven = (degrees / dLatEven) * (cprMod(j, dLatEven) + even.getLat());
        double latOdd = (degrees / dLatOdd) * (cprMod(j, dLatOdd) + odd.getLat());

        if (latEven >= 270.0 && latEven <= 360.0) {
            latEven -= 360.0;
        }

        if (latOdd >= 270.0 && latOdd <= 360.0) {
            latOdd -= 360.0;
        }

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

        if (newLon > 180d) {
            newLon -= 360d;
        }

        //TODO Should be a sanity-check here to make sure the calculated position isn't outside receiver origin range

        even.setZones(nlLatEven, m);
        odd.setZones(nlLatOdd, m);

        current = new CprPosition(newLat, newLon, even.getSurface());
        if (current.getSurface()) {
            current.validateSurface();
        }
    }

    private double cprMod(double a, double b) {
        return a - b * Math.floor(a/b);
    }

    private double cprN(double nlLat, double isOdd) {
        return Math.max(1, nlLat - isOdd);
    }

    private double NL(double lat) {
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

    public static void start() {

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

    public static void stop() {
        cacheCleanup.cancel();
        cacheCleanup = null;

        cache.clear();
    }
}
