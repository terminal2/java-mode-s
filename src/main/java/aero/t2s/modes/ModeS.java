package aero.t2s.modes;

import aero.t2s.modes.database.ModeSDatabase;
import aero.t2s.modes.decoder.df.DownlinkFormat;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ModeS {
    private final ModeSListener listener;
    private final Map<String, Track> tracks = new ConcurrentHashMap<>();
    private final ModeSHandler handler;

    private Consumer<Track> onTrackDeleted = (track) -> {};
    private Consumer<Track> onTrackUpdated = (track) -> {};
    private Consumer<Track> onTrackCreated = (track) -> {};
    private Consumer<DownlinkFormat> onMessage;

    public ModeS(String host, int port, double originLat, double originLon) {
        this(host, port, originLat, originLon, null);
    }

    public ModeS(String host, int port, double originLat, double originLon, ModeSDatabase database) {
        handler = new ModeSTrackHandler(tracks, originLat, originLon, database);

        handler.onTrackCreated(onTrackCreated);
        handler.onTrackCreated(onTrackUpdated);
        handler.onTrackCreated(onTrackDeleted);
        handler.onMessage(onMessage);

        listener = new ModeSListener(new InetSocketAddress(host, port), handler);
    }

    public ModeS(String host, int port, ModeSHandler handler) {
        this.handler = handler;
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

    public void onMessage(Consumer<DownlinkFormat> consumer) {
        handler.onMessage(consumer);
    }

    public void start() {
        this.handler.start();
        this.listener.start();
    }

    public void stop() {
        this.listener.interrupt();
        this.handler.stop();
    }

    public Map<String, Track> getTracks() {
        return tracks;
    }
}
