package aero.t2s.modes.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelectedAltitudeSourceTest {
    @Test
    public void find()
    {
        assertEquals(SelectedAltitudeSource.UNKNOWN, SelectedAltitudeSource.find(0));
        assertEquals(SelectedAltitudeSource.AIRCRAFT, SelectedAltitudeSource.find(1));
        assertEquals(SelectedAltitudeSource.MCP, SelectedAltitudeSource.find(2));
        assertEquals(SelectedAltitudeSource.FMS, SelectedAltitudeSource.find(3));
    }

    @Test
    public void find_throws_exception()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            SelectedAltitudeSource.find(4);
        });
    }
}
