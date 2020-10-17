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
        bdsDecoder.add(new Bds50());
        bdsDecoder.add(new Bds60());
    }

    public boolean decode(Track track, short[] data) {
        for (Bds bds : bdsDecoder) {
            if (bds.attemptDecode(track, data)) {
                LoggerFactory.getLogger(getClass()).debug("Matched {}", bds.getClass().getSimpleName());

                return true;
            }
        }

        int tc = (data[4] >>> 4) * 10 + (data[4] & 0xF);
        return false;
    }
}
