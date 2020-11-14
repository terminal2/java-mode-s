package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.BinaryHelper;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.SelectedAltitudeSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Bds40Test {

    private Bds40 bds;

    @Test
    public void it_decodes_bds40()
    {
        bds = new Bds40(BinaryHelper.stringToByteArray("A8001627CA3E51F0A80000AEAE24"));

        assertTrue(bds.isValid());
        assertEquals(38048, bds.getSelectedAltitude());
        assertEquals(38000, bds.getFmsAltitude());
        assertEquals(1013.2, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());

        bds = new Bds40(BinaryHelper.stringToByteArray("A0001498BE85F430A8018768F65C"));

        assertTrue(bds.isValid());
        assertEquals(32224, bds.getSelectedAltitude());
        assertEquals(32000, bds.getFmsAltitude());
        assertEquals(1013.2, bds.getBaro());
        assertTrue(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.FMS, bds.getSelectedAltitudeSource());
    }

    @Test
    public void it_decodes_bds40_with_zeros()
    {
        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());
    }

    @Test
    public void it_decodes_bds40_with_all_status_flags_false()
    {
        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b01111111,
            0b11111011,
            0b11111111,
            0b11011111,
            0b11111110,
            0b00000000,
            0b11100011,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_reserved_bits_are_set()
    {
        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00011000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());

        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000001,
            0b11111110,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());

        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000001,
            0b11111110,
            0b00011000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_fcu_altitude_is_above_50000()
    {
        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b11111111,
            0b11111000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_fms_altitude_is_above_50000()
    {
        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000111,
            0b11111111,
            0b11000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_baro_is_zero()
    {
        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00100000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_baro_is_below_850()
    {
        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00100011, // status 1 & baro 499 (499 * 0 x 0.1 = 49.9mb)
            0b11100110,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());
    }

    @Test
    public void it_fails_to_decode_bds40_when_baro_is_above_1100()
    {
        bds = new Bds40(new short[] {
            0x0, 0x0, 0x0, 0x0, // Bits before MB field
            0b00000000,
            0b00000000,
            0b00000000,
            0b00110111, // status 1 & baro 3001 (3001 * 0 x 0.1 = 300.1mb)
            0b01110010,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getSelectedAltitude());
        assertEquals(0, bds.getFmsAltitude());
        assertEquals(0, bds.getBaro());
        assertFalse(bds.isAutopilotVnav());
        assertFalse(bds.isAutopilotAltitudeHold());
        assertFalse(bds.isAutopilotApproach());
        assertEquals(SelectedAltitudeSource.UNKNOWN, bds.getSelectedAltitudeSource());
    }
}
