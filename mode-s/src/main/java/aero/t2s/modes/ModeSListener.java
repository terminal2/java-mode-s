package aero.t2s.modes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

class ModeSListener extends Thread {
    private InetSocketAddress address;
    private ModeSMessageHandler handler;

    private int attempts;

    public ModeSListener(InetSocketAddress address, ModeSMessageHandler handler) {
        super("Mode S Listener");
        this.address = address;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (! isInterrupted()) {
            try (SocketChannel socketChannel = SocketChannel.open()) {
                attempts++;
                socketChannel.connect(address);
                BufferedReader is = new BufferedReader(new InputStreamReader(socketChannel.socket().getInputStream()));

                String hex;
                attempts = 0;
                while (! isInterrupted()) {
                    hex = is.readLine();
                    handler.handle(hex);
                }
            } catch (IOException e) {
                e.printStackTrace();

                if (attempts == 5) {
                    interrupt();
                }
            }
        }
    }
}
