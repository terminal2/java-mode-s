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
    private Consumer<Track> onDeleted;
    private Consumer<Track> onCreated;
    private Consumer<Track> onUpdated;

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
            short[] data = new short[hex.length() / 2];
            for (int index = 0; index < hex.length() / 2; index++) {
                data[index] = Short.parseShort(hex.substring(index * 2, (index * 2) + 2), 16);
            }

            Track track = decoder.decode(data[0] >>> 3, data);
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
