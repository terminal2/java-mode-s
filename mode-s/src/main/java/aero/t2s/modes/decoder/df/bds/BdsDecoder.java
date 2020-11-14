package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.NotImplementedException;
import aero.t2s.modes.decoder.Common;

import java.util.LinkedList;
import java.util.List;

public class BdsDecoder {
    private final short[] data;
    private final List<Bds> bdsMessages = new LinkedList<>();
    private final List<Bds> valid = new LinkedList<>();

    public BdsDecoder(short[] data) {
        this.data = data;
        bdsMessages.add(new Bds10(data));
        bdsMessages.add(new Bds20(data));
        bdsMessages.add(new Bds17(data));
        bdsMessages.add(new Bds30(data));
        bdsMessages.add(new Bds40(data));
        bdsMessages.add(new Bds50(data));
        bdsMessages.add(new Bds53(data));
        bdsMessages.add(new Bds60(data));
        bdsMessages.add(new Bds21(data));
        bdsMessages.add(new Bds44(data));
        bdsMessages.add(new Bds45(data));
    }

    public Bds decode() throws MultipleBdsMatchesFoundException, EmptyMessageException, NotImplementedException {
        // Indicates no message is present and is most likely a reply to a uplink request Altitude from ground station.
        if (data[4] == 0 && data[5] == 0 && data[6] == 0 && data[7] == 0 && data[8] == 0 && data[9] == 0 && data[10] == 0) {
            throw new EmptyMessageException();
        }

        for (Bds message : bdsMessages) {
            if (message.isValid()) {
                valid.add(message);
            }
        }

        if (valid.size() == 0) {
            throw new NotImplementedException("No matching BDS found for " + Common.toHexString(data));
        }

        if (valid.size() > 1) {
            throw new MultipleBdsMatchesFoundException(valid);
        }

        return valid.get(0);
    }
}
