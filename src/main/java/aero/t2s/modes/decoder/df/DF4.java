package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Altitude;
import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.AltitudeEncoding;
import aero.t2s.modes.decoder.Common;

public class DF4 extends DownlinkFormat {
    private boolean alert;
    private boolean spi;
    private Altitude altitude;

    public DF4(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF4 decode() {
        int fs = data[0] & 0x7;

        alert = Common.isFlightStatusAlert(fs);
        spi = Common.isFlightStatusSpi(fs);
        altitude = AltitudeEncoding.decode((((data[2] << 8) | data[3])) & 0x1FFF);

        return this;
    }

    @Override
    public void apply(Track track) {
        track.getFlightStatus().setAlert(alert);
        track.getFlightStatus().setSpi(spi);
        track.setAltitude(altitude);
    }

    public boolean isAlert() {
        return alert;
    }

    public boolean isSpi() {
        return spi;
    }

    public Altitude getAltitude() {
        return altitude;
    }

    @Override
    public String toString() {
        return String.format(
            "DF4: Altitude Reply (%s):\n" +
            "-----------------\n\n" +
            "Alert: %b\n" +
            "SPI (Ident): %b\n" +
            "Altitude: %s",
            getIcao(),
            isAlert(),
            isSpi(),
            getAltitude()
        );
    }
}
