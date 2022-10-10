package aero.t2s.modes.decoder.df;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.decoder.UnknownDownlinkFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DF24Test {
    @Test
    public void test_df24_elm() throws UnknownDownlinkFormatException {
        DF24 df = new DF24(BinaryHelper.stringToByteArray("C33D2901090141AE21C600180121"));
        df.decode();

        assertEquals("76CEFA", df.getIcao());
        assertEquals(3, df.getSequenceNo());
    }
}
