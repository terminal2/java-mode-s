package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AircraftIdentificationTest {
    @Test
    public void it_decodes_callsign()
    {
        AircraftIdentification aircraftIdentification = new AircraftIdentification(BinaryHelper.stringToByteArray("8D4840D6202CC371C32CE0576098")).decode();

        assertEquals("KLM1023", aircraftIdentification.getAcid());
    }

}
