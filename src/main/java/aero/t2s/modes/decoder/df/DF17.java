package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.df.df17.*;

public class DF17 extends DownlinkFormat {
    private ExtendedSquitter extendedSquitter;

    public DF17(short[] data) {
        super(data, IcaoAddress.FROM_MESSAGE);
    }

    @Override
    public DF17 decode() {
        int typeCode = data[4] >>> 3;

        switch (typeCode) {
            case 0:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 20:
            case 21:
            case 22:
                extendedSquitter = new AirbornePosition(data, getIcao());
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                extendedSquitter = new AircraftIdentification(data);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                extendedSquitter = new SurfacePosition(data, getIcao());
                break;
            case 19:
                extendedSquitter = new AirborneVelocity(data);
                break;
            case 23:
                extendedSquitter = new TestMessage(data);
                break;
            case 24:
                extendedSquitter = new SurfaceSystemStatus(data);
                break;
            case 25:
            case 26:
            case 27:
            case 30:
                extendedSquitter = new ReservedMessage(data);
                break;
            case 28:
                extendedSquitter = new AircraftStatusMessage(data);
                break;
            case 29:
                extendedSquitter = new TargetStatusMessage(data);
                break;
            case 31:
                extendedSquitter = new AircraftOperationalStatusMessage(data);
                break;
            default:
                logger.warn("Mode-S: No parser found for DF-17 type code {}. Packet ignored", typeCode);
                throw new InvalidExtendedSquitterTypeCodeException(typeCode);
        }

        extendedSquitter = extendedSquitter.decode();

        return this;
    }

    @Override
    public void apply(Track track) {
        extendedSquitter.apply(track);
    }

    public ExtendedSquitter getExtendedSquitter() {
        return extendedSquitter;
    }
}
