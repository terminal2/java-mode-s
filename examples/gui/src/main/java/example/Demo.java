package example;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aero.t2s.modes.ModeS;
import aero.t2s.modes.Track;
import example.flight.FlightFrame;

/*
 * ButtonDemo.java requires the following files:
 *   images/right.gif
 *   images/middle.gif
 *   images/left.gif
 */
public class Demo extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    private static final String IP = "127.0.0.1";
    private static final int PORT = 30002;
    private static final double LAT = 51;
    private static final double LON = 2;

    private List<Track> tracks = new LinkedList<>();
    private Timer timer = new Timer();


    public Demo() {
        super(new GridLayout(1,0));

        logger.info("Application is starting");
        logger.info("Connecting to Mode-S Dump1090 server on ({}:{})", IP, PORT);
        logger.info("Setting location Lat: {} & Lon: {}", LAT, LON);

        ModeS modes = new ModeS(IP, PORT, LAT, LON);


        FlightsTable flightModel = new FlightsTable(tracks);
        JTable flightTable = new JTable(flightModel);
        flightTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        flightTable.setFillsViewportHeight(true);
        flightTable.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        flightTable.getSelectionModel().addListSelectionListener((e) -> {
            if (flightTable.getSelectedRow() != -1)
                new FlightFrame(tracks.get(flightTable.getSelectedRow()));
        });

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(flightTable);

        //Add the scroll pane to this panel.
        add(scrollPane);

        modes.onTrackCreated((t) -> tracks.add(t));
        modes.onTrackUpdated((t) -> { });
        modes.onTrackDeleted((t) -> tracks.remove(t));
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                flightModel.fireTableDataChanged();
            }
        }, 0, 500);

        modes.start();
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ADS-B/Dump1090 Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        //Create and set up the content pane.
        Demo newContentPane = new Demo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
