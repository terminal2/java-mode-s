package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;
import aero.t2s.modes.decoder.Decoder;
import org.slf4j.LoggerFactory;

public class DF5 extends DownlinkFormat {
    public DF5(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Track decode(short[] data, int downlinkFormat) {
        Track track = getDecoder().getTrack(getIcaoAddressFromParity(data));

        int fs = data[0] & 0x7;

        track.getFlightStatus().setAlert(Common.isFlightStatusAlert(fs));
        track.getFlightStatus().setSpi(Common.isFlightStatusSpi(fs));
        track.setModeA(Common.modeA((((data[2] << 8) | data[3])) & 0x1FFF));

        return null;
    }
}
