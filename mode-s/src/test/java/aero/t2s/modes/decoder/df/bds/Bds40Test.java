package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.SelectedAltitudeSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Bds40Test {

    private Track track;
    private Bds40 bds;

    @BeforeEach
    public void before()
    {
        track = new Track("123456");
        track.getCapabilityReport().all();
        bds = new Bds40();
    }

    @Test
    public void it_decodes_bds40()
    {
        boolean success = bds.attemptDecode(track, BinaryHelper.stringToByteArray("A8001627CA3E51F0A80000AEAE24"));

        assertTrue(success);
        assertEquals(38048, track.getSelectedAltitude());
        assertEquals(38000, track.getFmsSelectedAltitude());
        assertEquals(1013.2, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());

        track = new Track("123456");
        success = bds.attemptDecode(track, BinaryHelper.stringToByteArray("A0001498BE85F430A8018768F65C"));

        assertTrue(success);
        assertEquals(32224, track.getSelectedAltitude());
        assertEquals(32000, track.getFmsSelectedAltitude());
        assertEquals(1013.2, track.getBaroSetting());
        assertTrue(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.FMS, track.getSelectedAltitudeSource());
    }

    @Test
    public void it_decodes_bds40_with_zeros()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());
    }

    @Test
    public void it_decodes_bds40_with_all_status_flags_false()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b01111111,
            0b11111011,
            0b11111111,
            0b11011111,
            0b11111110,
            0b00000000,
            0b11100011,
        });

        assertTrue(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_reserved_bits_are_set()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00011000,
        });

        assertFalse(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000001,
            0b11111110,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000001,
            0b11111110,
            0b00011000,
        });

        assertFalse(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_fcu_altitude_is_above_50000()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b11111111,
            0b11111000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_fms_altitude_is_above_50000()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000111,
            0b11111111,
            0b11000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_baro_is_zero()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00100000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_baro_is_below_850()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00100011, // status 1 & baro 499 (499 * 0 x 0.1 = 49.9mb)
            0b11100110,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_baro_is_above_1100()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00110111, // status 1 & baro 3001 (3001 * 0 x 0.1 = 300.1mb)
            0b01110010,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getSelectedAltitude());
        assertEquals(0, track.getFmsSelectedAltitude());
        assertEquals(0, track.getBaroSetting());
        assertFalse(track.getVnav());
        assertFalse(track.getAltitudeHold());
        assertFalse(track.getApproachMode());
        assertEquals(SelectedAltitudeSource.UNKNOWN, track.getSelectedAltitudeSource());
    }
}
