package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Acas;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.AcasReplyInformation;
import aero.t2s.modes.constants.AcasSensitivity;
import aero.t2s.modes.constants.VerticalStatus;
import aero.t2s.modes.decoder.AltitudeEncoding;
import aero.t2s.modes.decoder.Decoder;
import org.slf4j.LoggerFactory;

public class DF16 extends DownlinkFormat {
    public DF16(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Track decode(short[] data, int downlinkFormat) {
        Track track = getDecoder().getTrack(getIcaoAddressFromParity(data));

        Acas acas = track.getAcas();
        acas.setVerticalStatus(VerticalStatus.from((data[0] >>> 2) & 0x1));
        acas.setCrossLinkCapability(((data[0] >>> 1) & 0x1) == 1);
        acas.setSensitivity(AcasSensitivity.from(data[1] >>> 5));
        acas.setReplyInformation(AcasReplyInformation.from(((data[1] & 0x7) << 1) | ((data[2] >> 7) & 0x1)));
        acas.setAltitude(AltitudeEncoding.decode((((data[2] << 8) | data[3])) & 0x1FFF));

        int vds1 = data[4] >>> 4;
        int vds2 = data[4] & 0xF;

        int ara = (data[5] << 5) | (data[6] >>> 3);
        int rac = ((data[5] & 0x3) << 2) | data[6] >>> 6;

        int rat = (data[6] >>> 5) & 0x1;
        int mte = (data[6] >>> 4) & 0x1;

        if (vds1 == 3 && vds2 == 0) {
            acas.getResolutionAdvisory().update(ara);
            acas.setMultipleThreats(mte == 1);

            acas.setRANotPassBelow(rac >>> 3 == 1);
            acas.setRANotPassAbove(((rac >>> 2) & 0x1) == 1);
            acas.setRANotTurnLeft(((rac >>> 1) & 0x1) == 1 );
            acas.setRANotTurnRight((rac & 0x1) == 1);
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
            LoggerFactory.getLogger(getClass()).warn(
                    "ADS-B: Active RA {} ({}) {} \n" +
                    "- RAC: \n" +
                    "    - {} \n" +
                    "    - {} \n" +
                    "    - {} \n" +
                    "    - {} \n" +
                    "- ARA: {}",
                    track.getIcao(),
                    track.getCallsign(),
                    (acas.getMultipleThreats() ? "multple threats" : "single threat"),
                    acas.getRANotPassAbove() ? "Do not pass below" : "Pass below allowed",
                    acas.getRANotPassBelow() ? "Do not pass above" : "Pass above allowed",
                    acas.getRANotTurnLeft() ? "Do not turn left" : "Left turn allowed",
                    acas.getRANotTurnRight() ? "Do not turn right" : "Right turn allowed",
                    acas.getResolutionAdvisory().toString()
            );
        }

        return track;
    }
}
