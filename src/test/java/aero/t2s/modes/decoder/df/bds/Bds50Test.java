package aero.t2s.modes.decoder.df.bds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Bds50Test {
    private Bds50 bds;

    @Test
    public void it_does_nothing_with_all_zeros()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }

    @Test
    public void it_decodes_bds50_roll_angle()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b10100000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(45, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b11100000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(-45, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }

    @Test
    public void it_is_not_bds50_when_roll_angle_is_not_available_and_bits_are_set()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00101000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b01000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }

    @Test
    public void it_decodes_bds50_true_track()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00010100,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(90, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00011100,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(270, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }

    @Test
    public void it_is_not_bds50_when_true_track_is_not_available_and_bits_are_set()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000010,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00001000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }

    @Test
    public void it_decodes_bds50_ground_speed()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000001,
            0b01000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(512, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000001,
            0b00000000,
            0b01000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(2, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }

    @Test
    public void it_is_not_bds50_when_ground_speed_is_not_available_and_bits_are_set()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b01000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }


    @Test
    public void it_is_not_bds50_when_track_angle_rate_is_not_available_and_bits_are_set()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00001000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00010000,
            0b00000000,
            0b00000000,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }

    @Test
    public void it_decodes_bds50_track_angle_rate()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00101000,
            0b00000000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(8, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00110000,
            0b00001000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(-15.9d, bds.getTrackAngleRate(), 0.1);
        assertEquals(0, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000001,
            0b00000000,
            0b01110000,
            0b00001000,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(2, bds.getGs());
        assertEquals(-15.9d, bds.getTrackAngleRate(), 0.1);
        assertEquals(0, bds.getTas());
    }



    @Test
    public void it_decodes_bds50_true_airspeed()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000110,
            0b00000000,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(1024, bds.getTas());

        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000100,
            0b00000001,
        });

        assertTrue(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(2, bds.getTas());
    }

    @Test
    public void it_is_not_bds50_when_true_airspeed_is_not_available_and_bits_are_set()
    {
        bds = new Bds50(new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000001,
        });

        assertFalse(bds.isValid());
        assertEquals(0, bds.getRollAngle());
        assertEquals(0, bds.getTrueTrack());
        assertEquals(0, bds.getGs());
        assertEquals(0, bds.getTrackAngleRate());
        assertEquals(0, bds.getTas());
    }
}
