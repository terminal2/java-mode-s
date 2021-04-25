package example.controllers;

import example.DemoFX;
import example.controllers.flight.index.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

public class FlightsIndexController implements Initializable {
    public Label status;
    public ToggleButton messageLogToggleButton;
    public TableView<Flight> flightsTable;

    private Timer timer = new Timer();
    private ObservableList<Flight> flights = FXCollections.observableArrayList();


    public FlightsIndexController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DemoFX.getModes().onTrackCreated(t -> flights.add(new Flight(t)));
        DemoFX.getModes().onTrackDeleted(t -> flights.removeIf(tr -> tr.getIcao().equals(t.getIcao())));
        DemoFX.getModes().onTrackUpdated(t -> {
            flights.stream().filter(f -> f.getIcao().equals(t.getIcao())).findFirst()
                .ifPresentOrElse(f -> f.update(t), () -> flights.add(new Flight(t)));
        });

        flightsTable.setItems(flights);
        flightsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Flight flight = flightsTable.getSelectionModel().getSelectedItem();

                FXMLLoader loader = new FXMLLoader(DemoFX.class.getResource("flights.view.fxml"));

                try {
                    Stage stage = new Stage();
                    stage.initModality(Modality.NONE);
                    stage.initOwner(DemoFX.getPrimaryStage());
                    stage.setX(DemoFX.getPrimaryStage().getX() + 50);
                    stage.setY(DemoFX.getPrimaryStage().getY() + 50);
                    stage.setScene(new Scene(loader.load()));
                    stage.show();

                    FlightViewController controller = loader.getController();
                    controller.setFlight(flight);
                    stage.setOnCloseRequest(controller::onClose);
                } catch (Exception exception) {
                    LoggerFactory.getLogger(FlightsIndexController.class).error("Failed to open popup", exception);
                }
            }
        });
    }

    public void toggleMessageLog(ActionEvent event) throws IOException {
        LoggerFactory.getLogger(getClass()).info(event.toString());

        FXMLLoader loader = new FXMLLoader(DemoFX.class.getResource("messages.index.fxml"));
        MessageIndexController controller = loader.getController();

        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.initOwner(DemoFX.getPrimaryStage());
        stage.setX(DemoFX.getPrimaryStage().getX() + 50);
        stage.setY(DemoFX.getPrimaryStage().getY() + 50);
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}
