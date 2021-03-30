package example.controllers;

import aero.t2s.modes.Altitude;
import aero.t2s.modes.decoder.Common;
import aero.t2s.modes.decoder.df.*;
import aero.t2s.modes.decoder.df.bds.Bds;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class MessageViewController {
    @FXML
    private Label hexData;
    @FXML
    private Label icao;
    @FXML
    private Label df;
    @FXML
    private Label bds;
    @FXML
    private TextArea hexCopyField;
    @FXML
    private TextArea details;

    private DownlinkFormat packet;

    private void updateInfo() {
        hexData.setText(String.format("HEX: %s", Common.toHexString(packet.getData())));
        icao.setText(String.format("ICAO: %s", packet.getIcao()));
        df.setText(df.getClass().getSimpleName());

        if (packet instanceof DF17) {
            this.bds.setText(((DF17) packet).getExtendedSquitter().getClass().getSimpleName());
        } else if (packet instanceof DF18) {
            this.bds.setText(((DF18) packet).getExtendedSquitter().getClass().getSimpleName());
        } else if (packet instanceof DF20) {
            Bds bds = ((DF20) packet).getBds();

            if (bds == null) {
                this.bds.setText("UNKNOWN");
            } else {
                this.bds.setText(bds.getClass().getSimpleName());
            }
        } else if (packet instanceof DF21) {
            Bds bds = ((DF21) packet).getBds();

            if (bds == null) {
                this.bds.setText("UNKNOWN");
            } else {
                this.bds.setText(bds.getClass().getSimpleName());
            }
        }

        this.details.setText(packet.toString());
        this.hexCopyField.setText(Common.toHexString(packet.getData()));
    }

    public void setPacket(DownlinkFormat packet) {
        this.packet = packet;

        Platform.runLater(() -> updateInfo());
    }

    private void setDetails(DF11 df) {
        this.details.setText(String.format(
            "Capability Report:\n" +
            "--------------------\n\n" +
            "ICAO: %s\n" +
            "Capability: %s",
            df.getAddress(),
            df.getCapability().name()
        ));
    }
}
