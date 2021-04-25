package example.controllers.flight.index;

import aero.t2s.modes.Track;
import javafx.beans.property.SimpleStringProperty;

public class Flight {
    private final SimpleStringProperty icao = new SimpleStringProperty("");
    private final SimpleStringProperty callsign = new SimpleStringProperty("");
    private final SimpleStringProperty registration = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty altitude = new SimpleStringProperty("");
    private final SimpleStringProperty rocd = new SimpleStringProperty("");
    private final SimpleStringProperty position = new SimpleStringProperty("");
    private final SimpleStringProperty modea = new SimpleStringProperty("");
    private final Track track;

    public Flight() {
        this.track = null;
    }

    public Flight(Track track) {
        this.track = track;
        update(track);
    }

    public void update(Track track) {
        setIcao(track.getIcao());
        setCallsign(track.getCallsign());
        setRegistration(track.getRegistration());
        setType(track.getAtype());
        setAltitude(String.valueOf((int)track.getAltitude().getAltitude()));
        setRocd(track.getRocd());
        setPosition(track.getLat(), track.getLon());
        setModea(track.getModeA());
    }

    public void setIcao(String icao) {
        this.icao.set(icao);
    }

    public String getIcao() {
        return this.icao.get();
    }

    public void setCallsign(String callsign) {
        this.callsign.set(callsign);
    }

    public String getCallsign() {
        return this.callsign.get();
    }

    public void setRegistration(String registration) {
        this.registration.set(registration);
    }

    public String getRegistration() {
        return this.registration.get();
    }
    public void setType(String type) {
        this.type.set(type);
    }

    public String getType() {
        return this.type.get();
    }

    public void setAltitude(String altitude) {
        this.altitude.set(altitude);
    }

    public String getAltitude() {
        return this.altitude.get();
    }

    public String getPosition() {
        return this.position.get();
    }

    public void setPosition(double lat, double lon) {
        this.position.set(String.format("%02.4f, %03.3f", lat, lon));
    }

    public String getRocd() {
        return this.rocd.get();
    }

    public void setRocd(int rocd) {
        if (rocd == 0) {
            this.rocd.set("-");
        } else {
            this.rocd.set(String.format("%d fpm", rocd));
        }
    }

    public String getModea() {
        return this.modea.get();
    }

    public void setModea(int modea) {
        this.modea.set(String.format("%04d", modea));
    }

    public Track getTrack() {
        return track;
    }
}
