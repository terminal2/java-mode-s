package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Acas;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.AcasReplyInformation;
import aero.t2s.modes.constants.AcasSensitivity;
import aero.t2s.modes.constants.VerticalStatus;
import aero.t2s.modes.decoder.AltitudeEncoding;
import aero.t2s.modes.decoder.Decoder;

public class DF0 extends DownlinkFormat {
    public DF0(Decoder decoder) {
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

        return track;
    }
}
