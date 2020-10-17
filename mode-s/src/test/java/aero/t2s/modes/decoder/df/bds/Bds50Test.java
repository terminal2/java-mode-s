package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Bds50Test {

    private Track track;
    private Bds50 bds;

    @BeforeEach
    public void before()
    {
        track = new Track("123456");
        track.getCapabilityReport().all();
        bds = new Bds50();
    }

    @Test
    public void it_does_nothing_with_all_zeros()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }

    @Test
    public void it_decodes_bds50_roll_angle()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b10100000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(45, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b11100000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(-45, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }

    @Test
    public void it_is_not_bds50_when_roll_angle_is_not_available_and_bits_are_set()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00101000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b01000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }

    @Test
    public void it_decodes_bds50_true_track()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00010100,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(90, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00011100,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(270, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }

    @Test
    public void it_is_not_bds50_when_true_track_is_not_available_and_bits_are_set()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000010,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00001000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }

    @Test
    public void it_decodes_bds50_ground_speed()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000001,
            0b10000000,
            0b00000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(1024, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000001,
            0b00000000,
            0b01000000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(2, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }

    @Test
    public void it_is_not_bds50_when_ground_speed_is_not_available_and_bits_are_set()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b01000000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }


    @Test
    public void it_is_not_bds50_when_track_angle_rate_is_not_available_and_bits_are_set()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00001000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00010000,
            0b00000000,
            0b00000000,
        });

        assertFalse(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }

    @Test
    public void it_decodes_bds50_track_angle_rate()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00101000,
            0b00000000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(8, track.getTrackAngleRate());
        assertEquals(0, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00110000,
            0b00001000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(-0.03d, track.getTrackAngleRate(), 0.01);
        assertEquals(0, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000001,
            0b00000000,
            0b01110000,
            0b00001000,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(2, track.getGs());
        assertEquals(-0.03d, track.getTrackAngleRate(), 0.01);
        assertEquals(0, track.getTas());
    }



    @Test
    public void it_decodes_bds50_true_airspeed()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000110,
            0b00000000,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(1024, track.getTas());

        success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000100,
            0b00000001,
        });

        assertTrue(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(2, track.getTas());
    }

    @Test
    public void it_is_not_bds50_when_true_airspeed_is_not_available_and_bits_are_set()
    {
        boolean success = bds.attemptDecode(track, new short[] {
            0x0, 0x0, 0x0, 0x0,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000000,
            0b00000001,
        });

        assertFalse(success);
        assertEquals(0, track.getRollAngle());
        assertEquals(0, track.getTrueHeading());
        assertEquals(0, track.getGs());
        assertEquals(0, track.getTrackAngleRate());
        assertEquals(0, track.getTas());
    }
}
