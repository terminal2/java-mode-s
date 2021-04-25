package aero.t2s.modes.decoder.df;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.Decoder;
import aero.t2s.modes.decoder.UnknownDownlinkFormatException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class DfTEst {
    @Test
    public void test() throws UnknownDownlinkFormatException {
        String message = "0000142A9D9A66";

        Decoder decoder = new Decoder(new HashMap<>(), 50, 2, ModeSDatabase.createDatabase());

        DownlinkFormat df = decoder.decode(BinaryHelper.stringToByteArray(message));

        System.out.println(df.toString());
    }
}
