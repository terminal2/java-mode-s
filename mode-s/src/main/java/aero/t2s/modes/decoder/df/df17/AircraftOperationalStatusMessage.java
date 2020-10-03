package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.LengthWidthCode;
import aero.t2s.modes.constants.Version;
import org.slf4j.LoggerFactory;

public class AircraftOperationalStatusMessage extends ExtendedSquitter {
    @Override
    public void decode(Track track, int typeCode, short[] data) {
        int subType = data[4] & 0x7;

        track.setVersion(Version.from(data[9] >>> 5));
        track.setNICa((data[9] >>> 4) & 0x1);
        track.setHeadingSource(((data[10] >>> 3) & 0x1) == 1);

        if (subType == 0) {
            decodeAirborne(track, data);
            return;
        }

        if (subType == 1) {
            decodeSurface(track, data);
        }
    }

    private void decodeSurface(Track track, short[] data) {
        // TODO Decode CC (C.2.3.10.3)

        track.setLengthWidthCode(LengthWidthCode.from(data[6] & 0xF));
        track.getAcas().setActive(((data[7] >>> 5) & 0x1) == 1);
        track.setSpi(((data[7] >>> 4) & 0x1) == 1);
        track.setSingleAntenna(((data[7] >>> 2) & 0x1) == 1);
        track.setHeadingSource((data[10] >>> 3 & 0x1) == 1);

        // TODO SDA (c-32)
        // TODO C-33/34 GPS antenna Offset
    }

    private void decodeAirborne(Track track, short[] data) {
        // TODO Decode CC (C.2.3.10.3)

        track.getAcas().setActive(((data[7] >>> 5) & 0x1) == 1);
        track.setSpi(((data[7] >>> 4) & 0x1) == 1);
        track.setSingleAntenna(((data[7] >>> 2) & 0x1) == 1);
        track.setNICb(data[10] >>> 3 & 0x1);

        // TODO SDA (c-32)
    }
}
