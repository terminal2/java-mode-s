package aero.t2s.modes.decoder.df;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.AcasReplyInformation;
import aero.t2s.modes.constants.AcasSensitivity;
import aero.t2s.modes.constants.CrossLinkCapability;
import aero.t2s.modes.constants.VerticalStatus;
import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.Decoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DF0Test {
    private DF0 df0;
    private HashMap<String, Track> tracks;

    @BeforeEach
    void before()
    {
        tracks = new HashMap<>();
        df0 = new DF0(new Decoder(tracks, 0, 0, ModeSDatabase.createDatabase()));
    }

    @Test
    void it_decodes_vertical_status()
    {
        assertEquals(0, tracks.size());

        df0.decode(BinaryHelper.stringToByteArray("02E194979F2C4B"), 0);
        assertEquals(1, tracks.size());
        assertEquals(VerticalStatus.AIRBORNE, tracks.values().stream().findFirst().get().getAcas().getVerticalStatus());

        tracks.clear();
        df0.decode(BinaryHelper.stringToByteArray("6E194979F2C4B"), 0);
        assertEquals(1, tracks.size());
        assertEquals(VerticalStatus.GROUND, tracks.values().stream().findFirst().get().getAcas().getVerticalStatus());
    }

    @Test
    void it_decodes_cross_link_capability()
    {
        assertEquals(0, tracks.size());

        df0.decode(BinaryHelper.stringToByteArray("02E194979F2C4B"), 0);
        assertEquals(1, tracks.size());
        assertEquals(CrossLinkCapability.SUPPORTED, tracks.values().stream().findFirst().get().getAcas().getCrossLinkCapability());

        tracks.clear();
        df0.decode(BinaryHelper.stringToByteArray("00E194979F2C4B"), 0);
        assertEquals(1, tracks.size());
        assertEquals(CrossLinkCapability.UNSUPPORTED, tracks.values().stream().findFirst().get().getAcas().getCrossLinkCapability());
    }

    @Test
    void it_decodes_sensitivity_level()
    {
        assertEquals(0, tracks.size());

        df0.decode(BinaryHelper.stringToByteArray("02E194979F2C4B"), 0);
        assertEquals(1, tracks.size());
        assertEquals(AcasSensitivity.LEVEL7, tracks.values().stream().findFirst().get().getAcas().getSensitivity());
    }

    @Test
    void it_decodes_reply_information()
    {
        assertEquals(0, tracks.size());

        df0.decode(BinaryHelper.stringToByteArray("02E194979F2C4B"), 0);
        assertEquals(1, tracks.size());
        assertEquals(AcasReplyInformation.RESERVED3, tracks.values().stream().findFirst().get().getAcas().getReplyInformation());
    }

    @Test
    void it_decodes_altitude()
    {
        assertEquals(0, tracks.size());

        df0.decode(BinaryHelper.stringToByteArray("02E192B9CB91DF"), 0);
        assertEquals(1, tracks.size());

        Track track = tracks.values().stream().findFirst().get();

        assertEquals(26025, track.getAltitude().getAltitude());
        assertEquals(25, track.getAltitude().getStep());
        assertFalse(track.getAltitude().isMetric());

        assertEquals(26025, track.getAcas().getAltitude().getAltitude());
        assertEquals(25, track.getAcas().getAltitude().getStep());
        assertFalse(track.getAcas().getAltitude().isMetric());
    }
}
