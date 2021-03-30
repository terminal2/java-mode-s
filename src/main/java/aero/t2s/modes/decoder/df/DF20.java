package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Altitude;
import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.AltitudeEncoding;
import aero.t2s.modes.decoder.Common;
import aero.t2s.modes.decoder.df.bds.Bds;
import aero.t2s.modes.decoder.df.bds.BdsDecoder;
import aero.t2s.modes.EmptyMessageException;
import aero.t2s.modes.decoder.df.bds.MultipleBdsMatchesFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class DF20 extends DownlinkFormat {
    private Bds bds;
    private boolean alert;
    private boolean spi;
    private Altitude altitude;

    private boolean valid;
    private boolean multipleMatches;
    private List<Bds> matches;

    public DF20(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF20 decode() {
        int flightStatus = data[0] & 0x3;

        alert = Common.isFlightStatusAlert(flightStatus);
        spi = Common.isFlightStatusSpi(flightStatus);
        altitude = AltitudeEncoding.decode((data[2] & 0x1F) << 8 | data[3]);

        try {
            bds = new BdsDecoder(data).decode();
            valid = true;
        } catch (EmptyMessageException e) {
            valid = false;
        } catch (MultipleBdsMatchesFoundException e) {
            matches = e.getMatches();
            multipleMatches = true;
            valid = false;
            logger.debug("Multiple matches BDS matches found {}", matches.stream().map(bds -> bds.getClass().getSimpleName()).reduce((a, b) -> String.join(", ", a, b)));
        } catch (NotImplementedException e) {
            throw new NotImplementedException("ADS-B: DF-20 received but could not determine BDS code " + Common.toHexString(data));
        }

        return this;
    }

    @Override
    public void apply(Track track) {
        track.getFlightStatus().setAlert(alert);
        track.getFlightStatus().setSpi(spi);
        track.setAltitude(altitude);

        if (valid) {
            bds.apply(track);
        }
    }

    public Bds getBds() {
        return bds;
    }

    public boolean isAlert() {
        return alert;
    }

    public boolean isSpi() {
        return spi;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isMultipleMatches() {
        return multipleMatches;
    }

    public List<Bds> getMatches() {
        return matches;
    }

    public Altitude getAltitude() {
        return altitude;
    }

    @Override
    public String toString() {
        String out = "DF20 Altitude Reply (" + getIcao() + ")\n";
        out += "---------------------------------------\n\n";

        out += String.format(
            "Alert: %b\n" +
            "SPI (Ident): %b\n" +
            "Altitude: %s\n\n",
            isAlert(),
            isSpi(),
            getAltitude()
        );

        if (valid) {
            out += bds.toString();
        } else {
            if (multipleMatches) {
                out += "Found " + matches.size() + " matches:\n";
                out += matches.stream().map(Object::toString).collect(Collectors.joining("\n-----\n"));
            } else {
                out += "No matches unknown packet";
            }
        }

        return out;
    }
}
