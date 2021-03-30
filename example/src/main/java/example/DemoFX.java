package example;

import aero.t2s.modes.ModeS;
import aero.t2s.modes.Track;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoFX extends Application {
    private static final Logger logger = LoggerFactory.getLogger(DemoFX.class);

    private static final String IP = "192.168.178.190";
    private static final int PORT = 30002;
    private static final double LAT = 51;
    private static final double LON = 2;

    private static ObservableList<Track> tracks = FXCollections.emptyObservableList();
    private static ModeS modes;
    private static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    public static Window getPrimaryStage() {
        return window;
    }

    @Override
    public void start(Stage stage) throws Exception {
        DemoFX.window = stage;

        // Connect to Mode S Module
        logger.info("Application is starting");
        logger.info("Connecting to Mode-S Dump1090 server on ({}:{})", IP, PORT);
        logger.info("Setting location Lat: {} & Lon: {}", LAT, LON);

        modes = new ModeS(IP, PORT, LAT, LON);

        modes.onTrackCreated((t) -> tracks.add(t));
        modes.onTrackUpdated((t) -> { });
        modes.onTrackDeleted((t) -> tracks.remove(t));

        modes.start();


        // Set up JavaFX
        Parent root = FXMLLoader.load(getClass().getResource("flights.index.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Mode-S Example");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> {
            modes.stop();
            Platform.exit();
        });
    }

    public static ObservableList<Track> getTracks() {
        return tracks;
    }

    public static ModeS getModes() {
        return modes;
    }
}
