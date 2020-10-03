package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Acas;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.EmergencyState;
import aero.t2s.modes.decoder.AltitudeEncoding;
import aero.t2s.modes.decoder.Common;
import org.slf4j.LoggerFactory;

public class AircraftStatusMessage extends ExtendedSquitter {
    @Override
    public void decode(Track track, int typeCode, short[] data) {
        int subType = data[4] & 0x7;

        if (subType == 1) {
            decodePriorityMessage(track, data);
        }

        if (subType == 2) {
            decodeAcasRaMessage(track, data);
        }
    }

    private void decodePriorityMessage(Track track, short[] data) {
        track.setEmergencyState(EmergencyState.from(data[5] >>> 5));
        track.setModeA(Common.modeA((data[5] & 0x1F) << 8 | data[6]));
    }

    private void decodeAcasRaMessage(Track track, short[] data) {
        Acas acas = track.getAcas();

        int ara = (data[5] << 6) | (data[6] >>> 3);
        int rac = ((data[6] & 0x3) << 2) | data[7] >>> 6;

        int rat = (data[7] >>> 5) & 0x1;
        int mte = (data[7] >>> 4) & 0x1;
        int tti = (data[7] >>> 2) & 0x3;
        int tid = ((data[7] & 0x3) << 24) | (data[8] << 16) | (data[9] << 8) | data[10];

        acas.getResolutionAdvisory().update(ara);
        acas.setMultipleThreats(mte == 1);

        acas.setRANotPassBelow(rac >>> 3 == 1);
        acas.setRANotPassAbove(((rac >>> 2) & 0x1) == 1);
        acas.setRANotTurnLeft(((rac >>> 1) & 0x1) == 1 );
        acas.setRANotTurnRight((rac & 0x1) == 1);
        acas.setThreatType(tti);

        if (acas.getThreatType() == Acas.ThreatType.MODES) {
            acas.setTargetModeS(Common.icao(Common.toHexString(new short[] {
                (short) (data[7] & 0x3),
                (data[8]),
                (data[9]),
                (data[10]),
            })));
        } else if (acas.getThreatType() == Acas.ThreatType.ALT_BRG_DIST) {
            acas.setTargetAltitude(AltitudeEncoding.decodeModeC(tid >>> 13).getAltitude());
            acas.setTargetRange(Common.tidr((tid >>> 6) & 0x7F));
            acas.setTargetBearing(Common.tidb(tid & 0x3F));
        }

        if (rat == 1) {
            acas.getResolutionAdvisory().clear();
            acas.setMultipleThreats(false);
            acas.setRANotPassBelow(false);
            acas.setRANotPassAbove(false);
            acas.setRANotTurnLeft(false);
            acas.setRANotTurnRight(false);
        }

        if (acas.getResolutionAdvisory().isActive()) {
            String target;

            if (acas.getThreatType() == Acas.ThreatType.MODES) {
                target = acas.getTargetModeS();
            } else if (acas.getThreatType() == Acas.ThreatType.ALT_BRG_DIST) {
               target = String.format("%d degrees, %fnm, %fft", acas.getTargetBearing(), acas.getTargetRange(), acas.getTargetAltitude());
            } else {
                target = "UNKNOWN";
            }

            LoggerFactory.getLogger(getClass()).warn(
                    "ADS-B: Active RA {} ({}) {} \n" +
                            "- RAC: \n" +
                            "    - {} \n" +
                            "    - {} \n" +
                            "    - {} \n" +
                            "    - {} \n" +
                            "- ARA: {} \n" +
                            "- Target: {}",
                    track.getIcao(),
                    track.getCallsign(),
                    (acas.getMultipleThreats() ? "multple threats" : "single threat"),
                    acas.getRANotPassAbove() ? "Do not pass below" : "Pass below allowed",
                    acas.getRANotPassBelow() ? "Do not pass above" : "Pass above allowed",
                    acas.getRANotTurnLeft() ? "Do not turn left" : "Left turn allowed",
                    acas.getRANotTurnRight() ? "Do not turn right" : "Right turn allowed",
                    acas.getResolutionAdvisory().toString(),
                    target
            );
        }
    }
}
