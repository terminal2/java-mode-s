package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.NavigationAccuracyCategoryPosition;

public class TargetStatusMessage extends ExtendedSquitter {
    private static final double HEADING_RESOLUTION = 180.0 / 256.0;

    @Override
    public void decode(Track track, int typeCode, short[] data) {
        int subtype = (data[4] >>> 1) & 0x3;

        if (subtype == 0)
            subtype0(track, typeCode, data);
        else if (subtype == 1)
            subtype1(track, typeCode, data);
    }

    private void subtype1(Track track, int typeCode, short[] data) {
        int silSupplement = data[4] & 0x1;

        boolean statusMcpFcu = (data[9] & 0b00000010) != 0;
        if (statusMcpFcu) {
            boolean selectedAltitudeFms = data[5] >>> 7 == 1; // MCP/FCU on false
            int selectedAltitude = (((data[5] & 0x7F) << 4) | (data[6] >>> 4) - 2) * 32;
            track.setSelectedAltitudeManagedFms(selectedAltitudeFms);
            track.setSelectedAltitudeManagedMcp(!selectedAltitudeFms);
            track.setSelectedAltitude(selectedAltitude);
        }

        double baroSetting = (((((data[6] & 0xF) << 5) | (data[7] >>> 3)) - 2) * 0.8) + 800;
        track.setBaroSetting(baroSetting);

        boolean selectedHeadingStatus = ((data[7] >>> 2) & 0x1) == 1;
        if (selectedHeadingStatus) {
            boolean isNegativeHeading = ((data[7] >>> 1) & 0x1) == 1;
            double heading = (((data[7] & 0x1) << 7) | (data[8] >>> 1)) * HEADING_RESOLUTION;
            if (isNegativeHeading)
                heading += 180;
            track.setSelectedHeading(heading);
        }

        track.setNACp(NavigationAccuracyCategoryPosition.find(((data[8] & 0b00000001) << 3) | data[9] >>> 5));
        track.setNICb((data[9] >>> 4) & 0x1);
        track.setSil((data[9] >>> 2) & 0x3);

        track.setAutopilot((data[9] & 0x1) == 1);
        track.setVnav((data[10] >>> 7) == 1);
        track.setAltitudeHold(((data[10] >>> 6) & 0x1) == 1);
        track.setApproachMode(((data[10] >>> 4) & 0x1) == 1);
        track.getAcas().setActive(((data[10] >>> 3) & 0x1) == 1);
        track.setLnav(((data[10] >>> 2) & 0x1) == 1);
    }

    private void subtype0(Track track, int typeCode, short[] data) {





    }
}
