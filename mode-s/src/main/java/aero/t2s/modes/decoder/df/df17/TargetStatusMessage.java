package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;

public class TargetStatusMessage extends ExtendedSquitter {
    private static final double HEADING_RESOLUTION = 180.0 / 256.0;

    @Override
    public void decode(Track track, int typeCode, short[] data) {
        int subtype = (data[4] >>> 1) & 0x3;

        if (subtype != 1) {
            return;
        }

        int silSupplement = data[4] & 0x1;
        boolean selectedAltitudeFms = data[5] >>> 7 == 1; // MCP/FCU on false

        int selectedAltitude = (((data[5] & 0x7F) << 4) | (data[6] >>> 4) - 2) * 32;

        track.setSelectedAltitudeManagedFms(selectedAltitudeFms);
        track.setSelectedAltitudeManagedMcp(!selectedAltitudeFms);
        track.setSelectedAltitude(selectedAltitude);

        double baroSetting = (((((data[6] & 0xF) << 5) | (data[7] >>> 3)) - 2) * 0.8) + 800;
        track.setBaroSetting(baroSetting);

        track.setValidStatus(((data[7] >>> 2) & 0x1) == 1);

        double headingSign = ((data[7] >>> 1) & 0x1) == 0 ? 1.0 : -1.0;
        double heading = (((data[7] & 0x1) << 7) | (data[8] >>> 1)) * HEADING_RESOLUTION * headingSign;
        heading = (heading + 360) % 360; // 0-360 range
        track.setSelectedHeading(heading);

        //TODO NACp

        track.setNICb((data[9] >>> 4) & 0x1);
        track.setSil((data[9] >>> 2) & 0x3);

        // TODO MCP/FCU mode bit valid/invalid

        track.setAutopilot((data[9] & 0x1) == 1);
        track.setVnav((data[10] >>> 7) == 1);
        track.setAltitudeHold(((data[10] >>> 6) & 0x1) == 1);
        track.setApproachMode(((data[10] >>> 4) & 0x1) == 1);
        track.getAcas().setActive(((data[10] >>> 3) & 0x1) == 1);
        track.setLnav(((data[10] >>> 2) & 0x1) == 1);
    }
}
