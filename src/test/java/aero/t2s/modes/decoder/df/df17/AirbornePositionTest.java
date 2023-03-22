package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.Decoder;
import aero.t2s.modes.decoder.UnknownDownlinkFormatException;
import aero.t2s.modes.decoder.df.DF17;
import aero.t2s.modes.decoder.df.DF18;
import aero.t2s.modes.decoder.df.DownlinkFormat;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AirbornePositionTest {
    @Test
    public void test_airborne_position_aircraft() throws UnknownDownlinkFormatException {
        // This is an ODD frame with Airborne Position
        DownlinkFormat dfA = testMessage("8d4076635883069b318845770698");

        assertInstanceOf(DF17.class, dfA);
        DF17 df17A = (DF17) dfA;
        assertEquals("407663", df17A.getIcao());
        ExtendedSquitter exSqA = df17A.getExtendedSquitter();
        assertInstanceOf(AirbornePosition.class, exSqA);
        AirbornePosition positionA = (AirbornePosition) exSqA;
        // We should NOT have a valid position after only one frame
        assertFalse(positionA.isPositionAvailable());

        // This is an EVEN frame with Airborne Position... so we should be able to do a global calculation of position
        DownlinkFormat dfB = testMessage("8d407663588303313d84f719b2de");

        assertInstanceOf(DF17.class, dfB);
        DF17 df17B = (DF17) dfB;
        assertEquals("407663", df17B.getIcao());
        ExtendedSquitter exSqB = df17B.getExtendedSquitter();
        assertInstanceOf(AirbornePosition.class, exSqB);
        AirbornePosition positionB = (AirbornePosition) exSqB;
        // We should now have a valid position after receiving both even and odd frames
        assertTrue(positionB.isPositionAvailable());
        assertEquals(52.789, positionB.getLat(), 0.01);
        assertEquals(-2.405, positionB.getLon(), 0.01);

        // This is another EVEN frame with Airborne Position... so we should be able to do a local calculation of position
        DownlinkFormat dfC = testMessage("8d407663588303312385138ddce6");

        assertInstanceOf(DF17.class, dfC);
        DF17 df17C = (DF17) dfC;
        assertEquals("407663", df17C.getIcao());
        ExtendedSquitter exSqC = df17C.getExtendedSquitter();
        assertInstanceOf(AirbornePosition.class, exSqC);
        AirbornePosition positionC = (AirbornePosition) exSqC;
        // We should still have a valid position after receiving another even frame
        assertTrue(positionC.isPositionAvailable());
        assertEquals(52.788, positionC.getLat(), 0.01);
        assertEquals(-2.401, positionC.getLon(), 0.01);
    }

    private DownlinkFormat testMessage(String message) throws UnknownDownlinkFormatException {
        Decoder decoder = new Decoder(new HashMap<>(), 53, -2, ModeSDatabase.createDatabase());

        return decoder.decode(BinaryHelper.stringToByteArray(message));
    }

}
