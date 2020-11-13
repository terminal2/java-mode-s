package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.AltitudeEncoding;
import aero.t2s.modes.decoder.Common;
import aero.t2s.modes.decoder.Decoder;

public class DF4 extends DownlinkFormat {
    public DF4(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Track decode(short[] data) {
        Track track = getDecoder().getTrack(getIcaoAddressFromParity(data));

        int fs = data[0] & 0x7;

        track.getFlightStatus().setAlert(Common.isFlightStatusAlert(fs));
        track.getFlightStatus().setSpi(Common.isFlightStatusSpi(fs));
        track.setAltitude(AltitudeEncoding.decode((((data[2] << 8) | data[3])) & 0x1FFF));

        return null;
    }
}
