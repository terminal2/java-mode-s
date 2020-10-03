package aero.t2s.modes;

public class RadiusLimit {
    private final Track track;

    private double radiusLimitMetres;
    private boolean isUnknown;

    public RadiusLimit(Track track) {
        this.track = track;
    }

    public void determine() {
        radiusLimitMetres = 0;
        isUnknown = false;

        // Surface Position
        if (track.isGroundBit()) {
            determineSurface();
            return;
        }

        determineAirborne();
    }

    private void determineAirborne() {
        if (track.getNIC() == 11 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 7.5;
        } else if (track.getNIC() == 10 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 25;
        } else if (track.getNIC() == 9 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 42;
        } else if (track.getNIC() == 8 && track.getNICa() == 1 && track.getNICb() == 1) {
            radiusLimitMetres = 185.2;
        } else if (track.getNIC() == 7 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 370.4;
        } else if (track.getNIC() == 6 && track.getNICa() == 0 && track.getNICb() == 1) {
            radiusLimitMetres = 555.6;
        } else if (track.getNIC() == 6 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 926;
        } else if (track.getNIC() == 6 && track.getNICa() == 1 && track.getNICb() == 1) {
            radiusLimitMetres = 1111.2;
        } else if (track.getNIC() == 5 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 1852;
        } else if (track.getNIC() == 4 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 3704;
        } else if (track.getNIC() == 3 && track.getNICa() == 1 && track.getNICb() == 1) {
            radiusLimitMetres = 7408;
        } else if (track.getNIC() == 2 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 14816;
        } else if (track.getNIC() == 1 && track.getNICa() == 0 && track.getNICb() == 0) {
            radiusLimitMetres = 37040;
        } else if (track.getNIC() == 0 && track.getNICa() == 0 && track.getNICb() == 0) {
            isUnknown = true;
        } else {
            isUnknown = true;
        }
    }

    private void determineSurface() {
        if (track.getNIC() == 11 && track.getNICa() == 0 && track.getNICc() == 0) {
            radiusLimitMetres = 7.5;
        } else if (track.getNIC() == 10 && track.getNICa() == 0 && track.getNICc() == 0) {
            radiusLimitMetres = 25;
        } else if (track.getNIC() == 9 && track.getNICa() == 1 && track.getNICc() == 0) {
            radiusLimitMetres = 75;
        } else if (track.getNIC() == 8 && track.getNICa() == 0 && track.getNICc() == 0) {
            radiusLimitMetres = 185.2;
        } else if (track.getNIC() == 7 && track.getNICa() == 1 && track.getNICc() == 1) {
            radiusLimitMetres = 370.4;
        } else if (track.getNIC() == 6 && track.getNICa() == 1 && track.getNICc() == 0) {
            radiusLimitMetres = 555.6;
        } else if (track.getNIC() == 6 && track.getNICa() == 0 && track.getNICc() == 1) {
            radiusLimitMetres = 1111.2;
        } else if (track.getNIC() == 0 && track.getNICa() == 0 && track.getNICc() == 0) {
            isUnknown = true;
        } else {
            isUnknown = true;
        }
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public double getRadiusLimitMetres() {
        return radiusLimitMetres;
    }
}
