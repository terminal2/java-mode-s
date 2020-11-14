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
import java.util.function.Consumer;

public class ModeSMessageHandler extends ModeSHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModeSMessageHandler.class);

    private final Decoder decoder;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private Consumer<DownlinkFormat> onMessage;

    public ModeSMessageHandler(double originLat, double originLon) {
        this.decoder = new Decoder(new HashMap<>(), originLat, originLon, ModeSDatabase.createDatabase());
    }

    public void handle(final String input) {
        executor.execute(() -> {
            String hex = input.substring(1, input.length() - 1);
            short[] data = BinaryHelper.stringToByteArray(hex);

            try {
                DownlinkFormat df = decoder.decode(data);

                if (onMessage != null) {
                    onMessage.accept(df);
                }

            } catch (InvalidExtendedSquitterTypeCodeException | UnknownDownlinkFormatException e) {
                LOGGER.error(e.getMessage());
            } catch (Throwable throwable) {
                LOGGER.error("Message could not be parsed", throwable);
            }
        });
    }

    public void onMessage(Consumer<DownlinkFormat> onMessage) {
        this.onMessage = onMessage;
    }
}
