package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;
import aero.t2s.modes.decoder.Decoder;
import org.slf4j.LoggerFactory;

public class DF5 extends DownlinkFormat {

    private boolean alert;
    private boolean spi;
    private int modeA;

    public DF5(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF5 decode() {
        int fs = data[0] & 0x7;

        alert = Common.isFlightStatusAlert(fs);
        spi = Common.isFlightStatusSpi(fs);
        modeA = Common.modeA((((data[2] << 8) | data[3])) & 0x1FFF);

        return this;
    }

    @Override
    public void apply(Track track) {
        track.getFlightStatus().setAlert(alert);
        track.getFlightStatus().setSpi(spi);
        track.setModeA(modeA);
    }
}
