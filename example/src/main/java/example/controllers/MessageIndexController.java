package example.controllers;

import example.DemoFX;
import example.controllers.messages.Message;
import example.controllers.messages.TypeFilter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessageIndexController implements Initializable {
    public TableView<Message> messagesTable;
    public Button resetButton;
    public ToggleButton enableLiveToggle;

    public TextField searchFilter;
    public ChoiceBox<TypeFilter> typeFilter;

    private boolean enabled = false;

    // Filters
    private String icaoFilter = "";
    FilteredList<Message> filteredMessageType;
    FilteredList<Message> filteredMessageIcao;

    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private final ObservableList<TypeFilter> types = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DemoFX.getModes().onMessage(df -> {
            if (enabled) {
                messages.add(new Message(df));

                if (types.stream().filter(t -> t.is(df)).findFirst().isEmpty()) {
                    Platform.runLater(() -> types.add(TypeFilter.from(df)));
                }
            }
        });

        setupMessageTypeFilter();
        setupMessageIcaoFilter();
        messagesTable.setItems(filteredMessageIcao);

        messagesTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Message message = messagesTable.getSelectionModel().getSelectedItem();

                if (message == null) {
                    LoggerFactory.getLogger(getClass()).info("No line selected cannot open message details");
                }


                try {
                    LoggerFactory.getLogger(getClass()).info("Open message detail for {}", message.getMessage());


                    FXMLLoader fxml = new FXMLLoader(DemoFX.class.getResource("messages.view.fxml"));

                    Stage stage = new Stage();
                    stage.initModality(Modality.NONE);
                    stage.initOwner(DemoFX.getPrimaryStage());
                    stage.setX(DemoFX.getPrimaryStage().getX() + 50);
                    stage.setY(DemoFX.getPrimaryStage().getY() + 50);
                    stage.setScene(new Scene(fxml.load()));
                    stage.show();
                    fxml.<MessageViewController>getController().setPacket(message.getPacket());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setupMessageIcaoFilter() {
        filteredMessageIcao = new FilteredList<>(filteredMessageType, item -> true);

        searchFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                filteredMessageIcao.setPredicate(item -> true);
            } else {
                filteredMessageIcao.setPredicate(item -> item.getIcao().startsWith(newValue.toUpperCase()));
            }
        });
    }

    private void setupMessageTypeFilter() {
        // Initial types filter with empty record "All"
        types.add(new TypeFilter(null, null));

        // Sort types alphabetical natural order
        SortedList<TypeFilter> sortedTypes = new SortedList<>(types, Comparator.comparing(Objects::toString));

        typeFilter.setItems(sortedTypes);

        // Set the default filtering to All
        filteredMessageType = new FilteredList<>(messages, item -> true);

        // When the dropdown is changed update the filtered results
        typeFilter.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            filteredMessageType.setPredicate(item -> {
                if (typeFilter.getSelectionModel().getSelectedItem() == null) {
                    return true;
                }

                if (typeFilter.getSelectionModel().getSelectedItem().getType() == null) {
                    return true;
                }

                return typeFilter.getSelectionModel().getSelectedItem().is(item.getPacket());
            });
        });
    }

    public void toggleLiveView(ActionEvent actionEvent) {
        enabled = enableLiveToggle.isSelected();
    }

    public void onReset(ActionEvent actionEvent) {
        messages.clear();
    }
}
