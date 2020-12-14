package aero.t2s.modes;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

class ModeSListener extends Thread {
    private final InetSocketAddress address;
    private final ModeSHandler handler;

    private int attempts;

    private boolean running = true;

    public ModeSListener(InetSocketAddress address, ModeSHandler handler) {
        super("Mode S Listener");
        this.address = address;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (!isInterrupted() && running) {
            try (SocketChannel socketChannel = SocketChannel.open()) {
                attempts++;
                LoggerFactory.getLogger(getClass()).info("[ModeSListener] Connecting to {}",  address.toString());
                socketChannel.connect(address);
                BufferedReader is = new BufferedReader(new InputStreamReader(socketChannel.socket().getInputStream()));

                String hex;
                attempts = 0;
                while (!isInterrupted()) {
                    hex = is.readLine();
                    handler.handle(hex);
                }
            } catch (IOException e) {
                e.printStackTrace();

                if (attempts == 5 || isInterrupted()) {
                    LoggerFactory.getLogger(getClass()).info("[ModeSListener] Disconnecting");
                    running = false;
                }
            }
        }
    }
}
