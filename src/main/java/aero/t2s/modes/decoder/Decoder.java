package aero.t2s.modes.decoder;

import aero.t2s.modes.Track;
import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.df.*;

import java.util.Map;

public class Decoder {
    private final double originLat;
    private final double originLon;

    private final ModeSDatabase modeSDatabase;
    private final Map<String, Track> tracks;

    public Decoder(Map<String, Track> tracks, double originLat, double originLon, ModeSDatabase database) {
        this.tracks = tracks;
        this.originLat = originLat;
        this.originLon = originLon;
        this.modeSDatabase = database == null ? ModeSDatabase.createDatabase() : database;
    }

    public DownlinkFormat decode(short[] data) throws UnknownDownlinkFormatException {
        if (Common.isNotValid(data)) {
            return null;
        }

        int downlinkFormat = data[0] >>> 3;

        // Remaining bits are used by the data object
        if (downlinkFormat >= 24) {
            downlinkFormat = 24;
        }

        DownlinkFormat df;
        switch (downlinkFormat) {
            case 0:
                df = new DF0(data);
                break;
            case 4:
                df = new DF4(data);
                break;
            case 5:
                df = new DF5(data);
                break;
            case 11:
                df = new DF11(data);
                break;
            case 16:
                df = new DF16(data);
                break;
            case 17:
                df = new DF17(data, originLat, originLon);
                break;
            case 18:
                df = new DF18(data);
                break;
            case 20:
                df = new DF20(data);
                break;
            case 21:
                df = new DF21(data);
                break;
            case 22:
                df = new DF22(data);
                break;
            case 24:
                df = new DF24(data);
                break;
            default:
                throw new UnknownDownlinkFormatException(downlinkFormat, data);
        }

        return df.decode().aircraft(modeSDatabase.find(df.getIcao()));
    }

    public Track getTrack(String icao) {
        Track track = tracks.get(icao);

        if (track == null) {
            track = new Track(icao);
            tracks.putIfAbsent(icao, track);

            ModeSDatabase.ModeSAircraft modeSAircraft = modeSDatabase.find(icao);
            if (modeSAircraft != null) {
                track.setWtc(modeSAircraft.wtc);
                track.setAtype(modeSAircraft.atyp);
                track.setOperator(modeSAircraft.operator);
                track.setRegistration(modeSAircraft.registration);
            }
        } else {
            track.setWasJustCreated(false);
        }

        return track;
    }
}
