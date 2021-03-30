package example.controllers.messages;

import aero.t2s.modes.decoder.Common;
import aero.t2s.modes.decoder.df.*;
import aero.t2s.modes.decoder.df.bds.Bds;
import javafx.beans.property.SimpleStringProperty;

public class Message {
    private SimpleStringProperty icao = new SimpleStringProperty("");
    private SimpleStringProperty df = new SimpleStringProperty("");
    private SimpleStringProperty sub = new SimpleStringProperty("");
    private SimpleStringProperty messsage = new SimpleStringProperty("");

    private DownlinkFormat packet;

    public Message () {

    }

    public Message (DownlinkFormat df) {
        update(df);
    }

    public void update(DownlinkFormat df) {
        setIcao(df.getIcao());
        setDf(df.getClass().getSimpleName());
        setMesssage(Common.toHexString(df.getData()));

        if (df instanceof DF17) {
            setSub(((DF17) df).getExtendedSquitter().getClass().getSimpleName());
        } else if (df instanceof DF18) {
            setSub(((DF18) df).getExtendedSquitter().getClass().getSimpleName());
        } else if (df instanceof DF20) {
            Bds bds = ((DF20) df).getBds();

            if (bds == null) {
                setSub("UNKNOWN");
            } else {
                setSub(bds.getClass().getSimpleName());
            }
        } else if (df instanceof DF21) {
            Bds bds = ((DF21) df).getBds();

            if (bds == null) {
                setSub("UNKNOWN");
            } else {
                setSub(bds.getClass().getSimpleName());
            }
        }

        this.packet = df;
    }

    public void setIcao(String icao) {
        this.icao.set(icao);
    }

    public void setDf(String df) {
        this.df.set(df);
    }

    public void setSub(String sub) {
        this.sub.set(sub);
    }

    public void setMesssage(String message) {
        this.messsage.set(message);
    }

    public String getDf() {
        return df.get();
    }

    public String getIcao() {
        return icao.get();
    }

    public String getSub() {
        return sub.get();
    }

    public String getMessage() {
        return messsage.get();
    }

    public DownlinkFormat getPacket() {
        return packet;
    }
}
