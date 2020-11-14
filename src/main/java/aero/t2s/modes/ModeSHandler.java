package aero.t2s.modes;

import aero.t2s.modes.decoder.df.DownlinkFormat;

import java.util.function.Consumer;

abstract public class ModeSHandler {
    protected Consumer<Track> onDeleted = track -> {};
    protected Consumer<Track> onCreated = track -> {};
    protected Consumer<Track> onUpdated = track -> {};
    protected Consumer<DownlinkFormat> onMessage;

    public void onTrackDeleted(Consumer<Track> onDeleted) {
        this.onDeleted = onDeleted;
    }

    public void onTrackUpdated(Consumer<Track> onUpdated) {
        this.onUpdated = onUpdated;
    }

    public void onTrackCreated(Consumer<Track> onCreated) {
        this.onCreated = onCreated;
    }

    public void onMessage(Consumer<DownlinkFormat> onMessage) {
        this.onMessage = onMessage;
    }

    public abstract void handle(String data);
    public abstract void handleSync(String data);


    protected short[] toData(final String input) throws EmptyMessageException {
        if (input.startsWith("*0000")) {
            throw new EmptyMessageException();
        }

        String hex = input.replace("*", "").replace(";", "");

        return BinaryHelper.stringToByteArray(hex);
    }
}
