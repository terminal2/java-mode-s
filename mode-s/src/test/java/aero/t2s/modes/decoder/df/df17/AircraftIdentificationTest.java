package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AircraftIdentificationTest {
    private Track track;
    private final AircraftIdentification aircraftIdentification = new AircraftIdentification();

    @BeforeEach
    public void before()
    {
        track = new Track("4840D6");
    }

    @Test
    public void it_decodes_callsign()
    {
        assertNull(track.getCallsign());

        aircraftIdentification.decode(track, 4, BinaryHelper.stringToByteArray("8D4840D6202CC371C32CE0576098"));

        assertEquals("KLM1023", track.getCallsign());
    }

}
