package aero.t2s.modes.registers;

import aero.t2s.modes.constants.Angle;
import aero.t2s.modes.constants.NavigationAccuracyCategoryVelocity;
import aero.t2s.modes.constants.RocdSource;
import aero.t2s.modes.constants.Speed;

public class Register09 extends Register {
    private NavigationAccuracyCategoryVelocity NACv;

    /**
     * An intent change event shall be triggered 4 seconds after the detection of new information being inserted in registers 4016 to 4216.
     * The code shall remain set for 18 ±1 second following an intent change.
     */
    private boolean intentChangeFlag;
    private int heading = 0;
    private Angle headingSource = Angle.UNAVAILABLE;
    private int airspeed = 0;
    private Speed airspeedSource = Speed.IAS;
    private int vx = 0;
    private int vy = 0;
    private int gnssDifferenceFromBaro = 0;
    private int verticalRate;
    private RocdSource verticalRateSource = RocdSource.GNSS;

    public NavigationAccuracyCategoryVelocity getNACv() {
        return NACv;
    }

    public Register09 setNACv(NavigationAccuracyCategoryVelocity NACv) {
        this.NACv = NACv;
        return this;
    }

    public boolean isIntentChangeFlag() {
        return intentChangeFlag;
    }

    public Register09 setIntentChangeFlag(boolean intentChangeFlag) {
        this.intentChangeFlag = intentChangeFlag;
        return this;
    }

    public int getHeading() {
        return heading;
    }

    public Register09 setHeading(int heading) {
        this.heading = heading;
        return this;
    }

    public Angle getHeadingSource() {
        return headingSource;
    }

    public Register09 setHeadingSource(Angle headingSource) {
        this.headingSource = headingSource;
        return this;
    }

    public int getAirspeed() {
        return airspeed;
    }

    public Register09 setAirspeed(int airspeed) {
        this.airspeed = airspeed;
        return this;
    }

    public Speed getAirspeedSource() {
        return airspeedSource;
    }

    public Register09 setAirspeedSource(Speed airspeedSource) {
        this.airspeedSource = airspeedSource;
        return this;
    }

    public int getVx() {
        return vx;
    }

    public Register09 setVx(int vx) {
        this.vx = vx;
        return this;
    }

    public int getVy() {
        return vy;
    }

    public Register09 setVy(int vy) {
        this.vy = vy;
        return this;
    }

    public int getGnssDifferenceFromBaro() {
        return gnssDifferenceFromBaro;
    }

    public Register09 setGnssDifferenceFromBaro(int gnssDifferenceFromBaro) {
        this.gnssDifferenceFromBaro = gnssDifferenceFromBaro;
        return this;
    }

    public int getVerticalRate() {
        return verticalRate;
    }

    public Register09 setVerticalRate(int verticalRate) {
        this.verticalRate = verticalRate;
        return this;
    }

    public RocdSource getVerticalRateSource() {
        return verticalRateSource;
    }

    public Register09 setVerticalRateSource(RocdSource verticalRateSource) {
        this.verticalRateSource = verticalRateSource;
        return this;
    }

    @Override
    public String toString() {
        return "Register09{\n" +
            "valid=" + isValid() +
            ",\n NACv=" + NACv +
            ",\n intentChangeFlag=" + intentChangeFlag +
            ",\n heading=" + heading +
            ",\n headingSource=" + headingSource +
            ",\n airspeed=" + airspeed +
            ",\n airspeedSource=" + airspeedSource +
            ",\n vx=" + vx +
            ",\n vy=" + vy +
            ",\n gnssDifferenceFromBaro=" + gnssDifferenceFromBaro +
            ",\n verticalRate=" + verticalRate +
            ",\n verticalRateSource=" + verticalRateSource +
            "\n}";
    }
}
