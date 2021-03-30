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

    public boolean isAlert() {
        return alert;
    }

    public boolean isSpi() {
        return spi;
    }

    public int getModeA() {
        return modeA;
    }

    @Override
    public String toString() {
        return String.format(
            "DF5: Identity Reply (%s):\n" +
            "-----------------\n\n" +
            "Alert: %b\n" +
            "SPI (Ident): %b\n" +
            "Mode A: %04d",
            getIcao(),
            isAlert(),
            isSpi(),
            getModeA()
        );
    }
}
