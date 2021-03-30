package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Acas;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.ResolutionAdvisory;
import aero.t2s.modes.constants.ThreatTypeIndicator;
import aero.t2s.modes.decoder.AltitudeEncoding;
import aero.t2s.modes.decoder.Common;

public class AircraftStatusMessageAcasRA extends AircraftStatusMessage {
    private int subType = 2;

    private ResolutionAdvisory resolutionAdvisory;

    private boolean multipleThreats;
    private boolean RANotPassBelow;
    private boolean RANotPassAbove;
    private boolean RANotTurnLeft;
    private boolean RANotTurnRight;
    private ThreatTypeIndicator threatTypeIndicator;

    private String threatModeS;
    private double threatAltitude;
    private double threatRange;
    private int threatBearing;

    public AircraftStatusMessageAcasRA(short[] data) {
        super(data);
    }

    @Override
    public AircraftStatusMessage decode() {

        int ara = (data[5] << 6) | (data[6] >>> 3);
        int rac = ((data[6] & 0x3) << 2) | data[7] >>> 6;

        int rat = (data[7] >>> 5) & 0x1;
        multipleThreats = ((data[7] >>> 4) & 0x1) == 1;
        int tti = (data[7] >>> 2) & 0x3;
        int tid = ((data[7] & 0x3) << 24) | (data[8] << 16) | (data[9] << 8) | data[10];

        resolutionAdvisory = new ResolutionAdvisory(ara);
        RANotPassBelow = rac >>> 3 == 1;
        RANotPassAbove = ((rac >>> 2) & 0x1) == 1;
        RANotTurnLeft = ((rac >>> 1) & 0x1) == 1;
        RANotTurnLeft = (rac & 0x1) == 1;
        threatTypeIndicator = ThreatTypeIndicator.from(tti);

        if (rat == 1) {
            resolutionAdvisory = new ResolutionAdvisory();
            RANotPassBelow = false;
            RANotPassAbove = false;
            RANotTurnLeft = false;
            RANotTurnRight = false;
            multipleThreats = false;
        }

        switch (threatTypeIndicator) {
            case MODES:
                threatModeS = Common.icao(Common.toHexString(new short[]{
                    (short) (data[7] & 0x3),
                    (data[8]),
                    (data[9]),
                    (data[10]),
                }));
                break;
            case ALT_BRG_DIST:
                threatAltitude = AltitudeEncoding.decodeModeC(tid >>> 13).getAltitude();
                threatRange = Common.tidr((tid >>> 6) & 0x7F);
                threatBearing = Common.tidb(tid & 0x3F);
                break;
        }

        return this;
    }

    @Override
    public void apply(Track track) {
        Acas acas = track.getAcas();
        acas.getResolutionAdvisory().update(resolutionAdvisory);
        acas.setRANotPassBelow(RANotPassBelow);
        acas.setRANotPassAbove(RANotPassAbove);
        acas.setRANotTurnLeft(RANotTurnLeft);
        acas.setRANotTurnRight(RANotTurnRight);
        acas.setMultipleThreats(multipleThreats);
        acas.setThreatType(threatTypeIndicator);

        switch (threatTypeIndicator) {
            case MODES:
                acas.setTargetModeS(threatModeS);
                break;
            case ALT_BRG_DIST:
                acas.setTargetAltitude(threatAltitude);
                acas.setTargetRange(threatRange);
                acas.setTargetBearing(threatBearing);
                break;
        }
    }

    public int getSubType() {
        return subType;
    }

    public ResolutionAdvisory getResolutionAdvisory() {
        return resolutionAdvisory;
    }

    public boolean isMultipleThreats() {
        return multipleThreats;
    }

    public boolean isRANotPassBelow() {
        return RANotPassBelow;
    }

    public boolean isRANotPassAbove() {
        return RANotPassAbove;
    }

    public boolean isRANotTurnLeft() {
        return RANotTurnLeft;
    }

    public boolean isRANotTurnRight() {
        return RANotTurnRight;
    }

    public ThreatTypeIndicator getThreatTypeIndicator() {
        return threatTypeIndicator;
    }

    public String getThreatModeS() {
        return threatModeS;
    }

    public double getThreatAltitude() {
        return threatAltitude;
    }

    public double getThreatRange() {
        return threatRange;
    }

    public int getThreatBearing() {
        return threatBearing;
    }

    @Override
    public String toString() {
        return String.format(
            "AircraftStatusMessageAcasRA\n" +
            "ACAS RA: %s\n" +
            "Multiple threats: %b\n" +
            "Do not pass below: %b\n",
            "Do not pass above: %b\n",
            "Do not turn left: %b\n",
            "Do not turn right: %b\n",
            "Threat type Indicator: %s\n" +
            "Threat mode-s: %s\n" +
            "Threat altitude: %fFT\n" +
            "Threat range: %f\n" +
            "Threat bearing: %f",
            resolutionAdvisory,
            multipleThreats,
            RANotPassBelow,
            RANotPassAbove,
            RANotTurnLeft,
            RANotTurnRight,
            threatTypeIndicator.name(),
            threatModeS,
            threatAltitude,
            threatRange,
            threatBearing
        );
    }
}
