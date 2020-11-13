package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Decoder;
import aero.t2s.modes.decoder.df.df17.*;

import java.util.HashMap;
import java.util.Map;

public class DF18 extends DownlinkFormat {
    private Map<Integer, ExtendedSquitter> squitterMap = new HashMap<>();

    public DF18(Decoder decoder) {
        super(decoder);

        AircraftIdentification aircraftIdentification = new AircraftIdentification();
        SurfacePosition surfacePosition = new SurfacePosition();
        ReservedMessage reservedMessage = new ReservedMessage();

        squitterMap.put(1, aircraftIdentification);
        squitterMap.put(2, aircraftIdentification);
        squitterMap.put(3, aircraftIdentification);
        squitterMap.put(4, aircraftIdentification);
        squitterMap.put(5, surfacePosition);
        squitterMap.put(6, surfacePosition);
        squitterMap.put(7, surfacePosition);
        squitterMap.put(8, surfacePosition);
        squitterMap.put(19, new AirborneVelocity());

        squitterMap.put(23, new TestMessage());
        squitterMap.put(24, new SurfaceSystemStatus());
        squitterMap.put(25, reservedMessage);
        squitterMap.put(26, reservedMessage);
        squitterMap.put(27, reservedMessage);
        squitterMap.put(28, new AircraftStatusMessage());
        squitterMap.put(29, new TargetStatusMessage());
        squitterMap.put(30, reservedMessage);
        squitterMap.put(31, new AircraftOperationalStatusMessage());
    }

    @Override
    public Track decode(short[] data) {
        Track track = getDecoder().getTrack(getIcaoAddress(data));

        int typeCode = data[4] >>> 3;

        if (!squitterMap.containsKey(typeCode)) {
            logger.warn("Mode-S: No parser found for DF-18 type code {}. Packet ignored", typeCode);
            return track;
        }

        squitterMap.get(typeCode).decode(track, typeCode, data);

        return track;
    }
}
