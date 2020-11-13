package aero.t2s.modes.decoder;

import aero.t2s.modes.Track;
import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.df.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Decoder {
    private static final Logger logger = LoggerFactory.getLogger(Decoder.class);

    private ModeSDatabase modeSDatabase;
    private Map<String, Track> tracks;

    private Map<Integer, DownlinkFormat> downlinkFormatDecoders = new HashMap<>();

    public Decoder(Map<String, Track> tracks, double originLat, double originLon, ModeSDatabase database) {
        this.tracks = tracks;

        if (database == null) {
            database = ModeSDatabase.createDatabase();
        }
        this.modeSDatabase = database;

        downlinkFormatDecoders.put(0, new DF0(this));
        downlinkFormatDecoders.put(4, new DF4(this));
        downlinkFormatDecoders.put(5, new DF5(this));
        downlinkFormatDecoders.put(11, new DF11(this));
        downlinkFormatDecoders.put(16, new DF16(this));
        downlinkFormatDecoders.put(17, new DF17(this, originLat, originLon));
        downlinkFormatDecoders.put(18, new DF18(this));
        downlinkFormatDecoders.put(20, new DF20(this));
        downlinkFormatDecoders.put(21, new DF21(this));
        downlinkFormatDecoders.put(22, new DF22(this));
        downlinkFormatDecoders.put(24, new DF24(this));
    }

    public Track decode(short[] data) {
        if (Common.isValid(data)) {
            return null;
        }

        int downlinkFormat = data[0] >>> 3;

        // Remaining bits are used by the data object
        if (downlinkFormat >= 24) {
            downlinkFormat = 24;
        }

        if (downlinkFormatDecoders.containsKey(downlinkFormat)) {
            return downlinkFormatDecoders.get(downlinkFormat).decode(data);
        }

        logger.warn("Unknown Mode S Packet: {} => {}", downlinkFormat, Common.toHexString(data));
        return null;
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
