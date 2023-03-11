package aero.t2s.modes;

import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.Decoder;
import aero.t2s.modes.decoder.UnknownDownlinkFormatException;
import aero.t2s.modes.decoder.df.DownlinkFormat;
import aero.t2s.modes.decoder.df.df17.InvalidExtendedSquitterTypeCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ModeSTrackHandler extends ModeSHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModeSTrackHandler.class);

    private final Decoder decoder;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Map<String, Track> tracks;

    private boolean cleanupEnabled = true;
    private final Timer timer;

    public ModeSTrackHandler(Map<String, Track> tracks, double originLat, double originLon, ModeSDatabase database) {
        this.tracks = tracks;
        this.originLat = originLat;
        this.originLon = originLon;
        this.decoder = new Decoder(tracks, originLat, originLon, database);

        timer = new Timer();
    }

    @Override
    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!cleanupEnabled) {
                    return;
                }

                List<String> expired = new ArrayList<>();

                tracks.values().stream().filter(Track::isExpired).forEach((track) -> expired.add(track.getIcao()));

                expired.forEach((icao) -> onDeleted.accept(tracks.remove(icao)));
            }
        }, 1000, 5000);
    }

    @Override
    public void stop() {
        timer.cancel();
    }

    public void handle(final String input) {
        executor.execute(() -> handleSync(input));
    }

    public DownlinkFormat handleSync(final String input) {
        try {
            DownlinkFormat df = decoder.decode(toData(input));
            if(df == null) {
                // invalid packet (Mode A/C like *21D2; *0200; *0101;)
                LOGGER.debug("DF Message could not be parsed: [{}]", input);
                return null;
            }

            Track track = decoder.getTrack(df.getIcao());

            if (track == null) {
                return null;
            }

            df.apply(track);
            track.touch();

            if (track.wasJustCreated()) {
                this.onCreated.accept(track);
            } else {
                this.onUpdated.accept(track);
            }

            if (onMessage != null) {
                onMessage.accept(df);
            }

            return df;
        } catch (ModeAcMessageException | EmptyMessageException ignored) {
        } catch (InvalidExtendedSquitterTypeCodeException | UnknownDownlinkFormatException e) {
            LOGGER.error(e.getMessage());
        } catch (Throwable throwable) {
            LOGGER.error("DF Message could not be parsed: [" + input + "]", throwable);
        }

        return null;
    }

    public void enableCleanup() {
        this.cleanupEnabled = true;
    }

    public void disableCleanup() {
        this.cleanupEnabled = false;
    }
}
