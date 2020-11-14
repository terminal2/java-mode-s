package aero.t2s.modes.decoder.df;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;
import aero.t2s.modes.decoder.df.bds.Bds;
import aero.t2s.modes.decoder.df.bds.BdsDecoder;
import aero.t2s.modes.decoder.df.bds.EmptyMessageException;
import aero.t2s.modes.decoder.df.bds.MultipleBdsMatchesFoundException;

import java.util.List;

public class DF21 extends DownlinkFormat {

    private Bds bds;
    private boolean alert;
    private boolean spi;
    private int modeA;

    private boolean valid;
    private boolean multipleMatches;
    private List<Bds> matches;

    public DF21(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF21 decode() {
        int flightStatus = data[0] & 0x3;

        alert = Common.isFlightStatusAlert(flightStatus);
        spi = Common.isFlightStatusSpi(flightStatus);
        modeA = Common.modeA((data[2] & 0x1F) << 8 | data[3]);

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
            throw new NotImplementedException("ADS-B: DF-21 received but could not determine BDS code " + Common.toHexString(data));
        }

        return this;
    }

    @Override
    public void apply(Track track) {
        track.getFlightStatus().setAlert(alert);
        track.getFlightStatus().setSpi(spi);
        track.setModeA(modeA);

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

    public int getModeA() {
        return modeA;
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
}
