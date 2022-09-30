package aero.t2s.modes.examples;

import java.util.Timer;
import java.util.TimerTask;

import aero.t2s.modes.ModeS;
import aero.t2s.modes.decoder.df.DF20;
import aero.t2s.modes.decoder.df.DF21;

public class StdOutExample {
    public static void main(String[] args) {
        ModeS modes = new ModeS(
            "192.168.178.190", // Host IP where the Dump1090 server is running
            30002, // The port with raw output (default 30002)
            51, // Decimal latitude
            4  //  Decimal longitude
        );
//        modes.onTrackCreated(track -> System.out.println("CREATED " + track.toString()));
//        modes.onTrackUpdated(track -> System.out.println("UPDATED " + track.toString()));
//        modes.onTrackDeleted(track -> System.out.println("DELETED " + track.toString()));

        MessageCount count = new MessageCount();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count.total > 0) {
                    System.out.print("Multi BDS messages last 60 seconds: " + count.multi + " ( " + Math.round((float)count.multi / count.total) + "%)\n");
                    count.reset();
                }
            }
        }, 0, 60000);

        modes.onMessage(df -> {
            count.increment();;
            if (df instanceof DF20) {
                if (((DF20) df).isMultipleMatches())
                    count.incrementMulti();
            }
            if (df instanceof DF21) {
                if (((DF21) df).isMultipleMatches())
                    count.incrementMulti();
            }
        });

        modes.start();
    }

    static class MessageCount {
        long total = 0;
        long multi = 0;

        void reset() {
            total = 0;
            multi = 0;
        }

        void increment() {
            total++;
        }

        void incrementMulti() {
            multi++;
        }
    }
}
