package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BdsDecoder {
    private List<Bds> bdsDecoder = new ArrayList<>();

    public BdsDecoder() {
        bdsDecoder.add(new Bds10());
        bdsDecoder.add(new Bds20());
        bdsDecoder.add(new Bds17());
        bdsDecoder.add(new Bds30());
        bdsDecoder.add(new Bds40());
        bdsDecoder.add(new Bds50());
        bdsDecoder.add(new Bds60());
    }

    public boolean decode(Track track, short[] data) {
        StringBuilder sb = new StringBuilder();

        printLine(sb);

        for(int i = 4; i<= 10; i++) {
            sb.append("|");

            for (int j = 7; j >= 0; j--) {
                sb.append(String.format(" %2d ", (i-3) * 8 - j));
            }
        }
        sb.append("|");
        printLine(sb);

        for(int i = 4; i<= 10; i++) {
            sb.append("|");

            for (int j = 7; j >= 0; j--) {
                sb.append(String.format(" %2d ", (data[i] >>> j) & 0x1));
            }
        }
        sb.append("|");
        printLine(sb);

        LoggerFactory.getLogger(getClass()).info(sb.toString());


        for (Bds bds : bdsDecoder) {
            if (bds.attemptDecode(track, data)) {
                LoggerFactory.getLogger(getClass()).debug("Matched {}", bds.getClass().getSimpleName());

                return true;
            }
        }

        int tc = (data[4] >>> 4) * 10 + (data[4] & 0xF);
        return false;
    }

    private void printLine(StringBuilder sb) {
        sb.append("\n");

        for (int i = 0; i < 7; i++) {
            sb.append("-");
            for (int j = 0; j < 8; j++) {
                sb.append("----");
            }
        }

        sb.append("-\n");
    }
}
