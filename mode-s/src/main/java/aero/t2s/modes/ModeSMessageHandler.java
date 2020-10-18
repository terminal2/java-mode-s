package aero.t2s.modes;

import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.Decoder;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

class ModeSMessageHandler {
    private final Decoder decoder;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private Consumer<Track> onDeleted = track -> {};
    private Consumer<Track> onCreated = track -> {};
    private Consumer<Track> onUpdated = track -> {};

    ModeSMessageHandler(Map<String, Track> tracks, double originLat, double originLon, ModeSDatabase database) {
        this.decoder = new Decoder(tracks, originLat, originLon, database);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<String> expired = new ArrayList<>();

                tracks.values().stream().filter(Track::isExpired).forEach((track) -> expired.add(track.getIcao()));

                expired.forEach((icao) -> onDeleted.accept(tracks.remove(icao)));
            }
        }, 1000, 5000);

    }

    void handle(final String input) {
        executor.execute(() -> {
            String hex = input.substring(1, input.length() - 1);
            short[] data = BinaryHelper.stringToByteArray(hex);

            Track track = decoder.decode(data);
            if (track != null) {
                track.touch();

                if (track.wasJustCreated()) {
                    this.onCreated.accept(track);
                } else {
                    this.onUpdated.accept(track);
                }
            }
        });
    }

    public void onTrackDeleted(Consumer<Track> onDeleted) {
        this.onDeleted = onDeleted;
    }

    public void onTrackUpdated(Consumer<Track> onUpdated) {
        this.onUpdated = onUpdated;
    }

    public void onTrackCreated(Consumer<Track> onCreated) {
        this.onCreated = onCreated;
    }
}
