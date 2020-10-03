package aero.t2s.modes;

import aero.t2s.modes.database.ModeSDatabase;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ModeS {
    private final ModeSListener listener;
    private final Map<String, Track> tracks = new ConcurrentHashMap<>();
    private final ModeSMessageHandler handler;

    private Consumer<Track> onTrackDeleted = (track) -> {};
    private Consumer<Track> onTrackUpdated = (track) -> {};
    private Consumer<Track> onTrackCreated = (track) -> {};

    public ModeS(String host, int port, double originLat, double originLon) {
        this(host, port, originLat, originLon, null);
    }

    public ModeS(String host, int port, double originLat, double originLon, ModeSDatabase database) {
        handler = new ModeSMessageHandler(tracks, originLat, originLon, database);

        handler.onTrackCreated(onTrackCreated);
        handler.onTrackCreated(onTrackUpdated);
        handler.onTrackCreated(onTrackDeleted);

        listener = new ModeSListener(new InetSocketAddress(host, port), handler);
    }

    public void onTrackDeleted(Consumer<Track> consumer) {
        handler.onTrackDeleted(consumer);
    }

    public void onTrackCreated(Consumer<Track> consumer) {
        handler.onTrackCreated(consumer);
    }

    public void onTrackUpdated(Consumer<Track> consumer) {
        handler.onTrackUpdated(consumer);
    }

    public void start() {
        listener.start();
    }

    public void stop() {
        listener.interrupt();
    }

    public Map<String, Track> getTracks() {
        return tracks;
    }
}
