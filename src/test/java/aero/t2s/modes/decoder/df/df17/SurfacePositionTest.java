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

class SurfacePositionTest {
    @Test
    public void test_surface_position_aircraft() throws UnknownDownlinkFormatException {
        // This is an EVEN frame with Surface Position
        DownlinkFormat dfA = testMessage("8c4ca6a938dd224b663c964f058e");

        assertInstanceOf(DF17.class, dfA);
        DF17 df17A = (DF17) dfA;
        assertEquals("4CA6A9", df17A.getIcao());
        ExtendedSquitter exSqA = df17A.getExtendedSquitter();
        assertInstanceOf(SurfacePosition.class, exSqA);
        SurfacePosition positionA = (SurfacePosition) exSqA;
        assertEquals(13, positionA.getVelocityEncoded());
        assertEquals(2.0, positionA.getVelocity());
        assertTrue(positionA.isTrackAvailable());
        assertEquals(82, positionA.getTrackEncoded());
        assertEquals(230.625, positionA.getTrack(), 0.001);
        // We should NOT have a valid position after only one frame
        assertFalse(positionA.isPositionAvailable());

        // This is an ODD frame with Surface Position
        DownlinkFormat dfB = testMessage("8c4ca6a938cd27ec46497d947b4d");

        assertInstanceOf(DF17.class, dfB);
        DF17 df17B = (DF17) dfB;
        assertEquals("4CA6A9", df17B.getIcao());
        ExtendedSquitter exSqB = df17B.getExtendedSquitter();
        assertInstanceOf(SurfacePosition.class, exSqB);
        SurfacePosition positionB = (SurfacePosition) exSqB;
        // We should now have a valid position after receiving both even and odd frames
        assertTrue(positionB.isPositionAvailable());
        assertEquals(53.3604, positionB.getLat(), 0.001);
        assertEquals(-2.2671, positionB.getLon(), 0.001);

        assertTrue(positionB.isVelocityAvailable());
        assertEquals(1.75, positionB.getVelocity(), 0.1);

        assertTrue(positionB.isTrackAvailable());
        assertEquals(230.625, positionB.getTrack(), 0.1);
    }

    @Test
    public void test_surface_position_vehicle() throws UnknownDownlinkFormatException {
        DownlinkFormat df = testMessage("90425854281007f03e44e4ba62ea");

        assertInstanceOf(DF18.class, df);
        DF18 df18 = (DF18) df;
        assertEquals("425854", df.getIcao());
        ExtendedSquitter exSq = df18.getExtendedSquitter();
        assertInstanceOf(SurfacePosition.class, exSq);
        SurfacePosition position = (SurfacePosition) exSq;
        assertEquals(1, position.getVelocityEncoded());
        assertEquals(0.0, position.getVelocity());
        assertFalse(position.isTrackAvailable());
    }

    private DownlinkFormat testMessage(String message) throws UnknownDownlinkFormatException {
        Decoder decoder = new Decoder(new HashMap<>(), 53, -2, ModeSDatabase.createDatabase());

        return decoder.decode(BinaryHelper.stringToByteArray(message));
    }
}
