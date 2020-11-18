package aero.t2s.modes.examples;

import aero.t2s.modes.ModeS;

public class StdOutExample {
    public static void main(String[] args) {
        ModeS modes = new ModeS(
            "127.0.0.1", // Host IP where the Dump1090 server is running
            30002, // The port with raw output (default 30002)
            51, // Decimal latitude
            0  //  Decimal longitude
        );
        modes.onTrackCreated(track -> System.out.println("CREATED " + track.toString()));
        modes.onTrackUpdated(track -> System.out.println("UPDATED " + track.toString()));
        modes.onTrackDeleted(track -> System.out.println("DELETED " + track.toString()));

        modes.start();
    }
}
