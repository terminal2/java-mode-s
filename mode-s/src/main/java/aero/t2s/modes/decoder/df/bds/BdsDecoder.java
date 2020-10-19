package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BdsDecoder {
    private List<Bds> bdsDecoder = new ArrayList<>();

    public BdsDecoder() {
        bdsDecoder.add(new Bds10());
        bdsDecoder.add(new Bds20());
        bdsDecoder.add(new Bds17());
        bdsDecoder.add(new Bds30());
        bdsDecoder.add(new Bds40());
        bdsDecoder.add(new Bds44());
        bdsDecoder.add(new Bds45());
        bdsDecoder.add(new Bds50());
        bdsDecoder.add(new Bds53());
        bdsDecoder.add(new Bds60());
        bdsDecoder.add(new Bds21());
    }

    public boolean decode(Track track, short[] data) {
        // Indicates no message is present and is most likely a reply to a uplink request Altitude from ground station.
        if (data[4] == 0 && data[5] == 0 && data[6] == 0 && data[7] == 0 && data[8] == 0 && data[9] == 0 && data[10] == 0) {
            return true;
        }

        for (Bds bds : bdsDecoder) {
            if (bds.attemptDecode(track, data)) {
                return true;
            }
        }

        return false;
    }
}
