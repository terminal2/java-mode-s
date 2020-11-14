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

    private boolean cleanupEnabled = true;

    public ModeSTrackHandler(Map<String, Track> tracks, double originLat, double originLon, ModeSDatabase database) {
        this.decoder = new Decoder(tracks, originLat, originLon, database);

        Timer timer = new Timer();
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

    public void handle(final String input) {
        executor.execute(() -> handleSync(input));
    }

    public void handleSync(final String input) {
        try {
            DownlinkFormat df = decoder.decode(toData(input));
            Track track = decoder.getTrack(df.getIcao());

            if (track == null) {
                return;
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
        } catch (EmptyMessageException ignored) {
        } catch (InvalidExtendedSquitterTypeCodeException | UnknownDownlinkFormatException e) {
            LOGGER.error(e.getMessage());
        } catch (Throwable throwable) {
            LOGGER.error("Message could not be parsed", throwable);
        }
    }

    public void enableCleanup() {
        this.cleanupEnabled = true;
    }

    public void disableCleanup() {
        this.cleanupEnabled = false;
    }
}
