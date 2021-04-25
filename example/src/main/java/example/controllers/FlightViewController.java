package example.controllers;

import aero.t2s.modes.Track;
import example.controllers.flight.index.Flight;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class FlightViewController implements Initializable {
    public TextField icao;
    public TextField callsign;
    public TextField reg;
    public TextField opr;
    public TextField atype;
    public TextField modea;
    public TextField speed;
    public TextField altitude;
    public TextField vertSpeed;
    public TextField heading;
    public TextField lat;
    public TextField lon;

    // Capability Report
    public CheckBox crAvailable;
    public CheckBox crBds05;
    public CheckBox crBds06;
    public CheckBox crBds07;
    public CheckBox crBds08;
    public CheckBox crBds09;
    public CheckBox crBds0A;
    public CheckBox crBds20;
    public CheckBox crBds21;
    public CheckBox crBds40;
    public CheckBox crBds41;
    public CheckBox crBds42;
    public CheckBox crBds43;
    public CheckBox crBds44;
    public CheckBox crBds45;
    public CheckBox crBds48;
    public CheckBox crBds50;
    public CheckBox crBds51;
    public CheckBox crBds52;
    public CheckBox crBds53;
    public CheckBox crBds54;
    public CheckBox crBds55;
    public CheckBox crBds56;
    public CheckBox crBds5F;
    public CheckBox crBds60;
    // Meteo
    public TextField meteoDirection;
    public TextField meteoSpeed;
    public TextField meteoHumidity;
    public TextField meteoRadioHeight;
    public TextField meteoStaticPressure;
    public TextField meteoSat;
    public TextField meteoShear;
    public TextField meteoTurbulence;
    public TextField meteoBurst;
    public TextField meteoIcing;
    public TextField meteoWake;
    // ACAS
    public CheckBox acasRaActive;
    public CheckBox acasCorrectionUp;
    public CheckBox acasRequireClimb;
    public CheckBox acasSenseReversal;
    public CheckBox acasRequiresCrossing;
    public CheckBox acasRequireDescend;
    public CheckBox acasCorrectionDown;
    public CheckBox acasActive;
    public TextField acasVerticalStatus;
    public TextField acasCrossLink;
    public TextField acasSensitivity;
    public TextField acasReplyInfo;
    public TextField acasAltitude;
    public CheckBox acasNotPassBelow;
    public CheckBox acasNotPassAbove;
    public CheckBox acasNotTurnLeft;
    public CheckBox acasNotTurnRight;
    public CheckBox acasMultipleThreats;
    public TextField acasType;
    public TextField acasModeS;
    public TextField acasTargetAltitude;
    public TextField acasRange;
    public TextField acasBearing;
    // Basic
    public TextField basicAltitude;
    public TextField basicBaroAltitude;
    public TextField basicGnssAltitude;
    // APP / ATT
    public CheckBox apManagedFms;
    public TextField apSelAlt;
    public TextField apFmsSelAlt;
    public TextField apSelAltSource;
    public TextField apSelHeading;
    public CheckBox apManagedMcp;
    public CheckBox apEnabled;
    public CheckBox apLnav;
    public CheckBox apVnav;
    public CheckBox apAlt;
    public CheckBox apApp;
    public TextField apRollAngle;
    public TextField apTrackAngleRate;


    private Flight flight;
    private Track track;
    private Timer timer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void setFlight(Flight flight) {
        this.flight = flight;
        this.track = flight.getTrack();
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> update());
            }
        }, 0, 1000);
    }

    private void update() {
        icao.setText(track.getIcao());
        callsign.setText(track.getCallsign());
        reg.setText(track.getRegistration());
        opr.setText(track.getOperator());
        atype.setText(track.getAtype());
        modea.setText(String.format("%04d", track.getModeA()));
        if (track.isGroundBit()) {
            altitude.setText("on ground");
        } else {
            altitude.setText(track.getAltitude().toString());
        }

        vertSpeed.setText(String.format("%dft/min", track.getRocd()));
        speed.setText(String.format("%dkts", (int) track.getGs()));
        heading.setText(String.format("%d", (int) track.getTrueHeading()));
        lat.setText(String.valueOf(track.getLat()));
        lon.setText(String.valueOf(track.getLon()));

        // Capability
        crAvailable.setSelected(track.getCapabilityReport().isAvailable());
        crBds05.setSelected(track.getCapabilityReport().isBds05());
        crBds06.setSelected(track.getCapabilityReport().isBds06());
        crBds07.setSelected(track.getCapabilityReport().isBds07());
        crBds08.setSelected(track.getCapabilityReport().isBds08());
        crBds09.setSelected(track.getCapabilityReport().isBds09());
        crBds0A.setSelected(track.getCapabilityReport().isBds0A());
        crBds20.setSelected(track.getCapabilityReport().isBds20());
        crBds21.setSelected(track.getCapabilityReport().isBds21());
        crBds40.setSelected(track.getCapabilityReport().isBds40());
        crBds41.setSelected(track.getCapabilityReport().isBds41());
        crBds42.setSelected(track.getCapabilityReport().isBds42());
        crBds43.setSelected(track.getCapabilityReport().isBds43());
        crBds44.setSelected(track.getCapabilityReport().isBds44());
        crBds45.setSelected(track.getCapabilityReport().isBds45());
        crBds48.setSelected(track.getCapabilityReport().isBds48());
        crBds50.setSelected(track.getCapabilityReport().isBds50());
        crBds51.setSelected(track.getCapabilityReport().isBds51());
        crBds52.setSelected(track.getCapabilityReport().isBds52());
        crBds53.setSelected(track.getCapabilityReport().isBds53());
        crBds54.setSelected(track.getCapabilityReport().isBds54());
        crBds55.setSelected(track.getCapabilityReport().isBds55());
        crBds56.setSelected(track.getCapabilityReport().isBds56());
        crBds5F.setSelected(track.getCapabilityReport().isBds5F());
        crBds60.setSelected(track.getCapabilityReport().isBds60());

        // Meteo
        meteoDirection.setText(String.valueOf(track.getMeteo().getWindDirection()));
        meteoSpeed.setText(String.valueOf(track.getMeteo().getWindSpeed()));
        meteoHumidity.setText(String.valueOf(track.getMeteo().getHumidity()));
        meteoRadioHeight.setText(String.valueOf(track.getMeteo().getRadioHeight()));
        meteoStaticPressure.setText(String.valueOf(track.getMeteo().getAverageStaticPressure()));
        meteoSat.setText(String.valueOf(track.getMeteo().getStaticAirTemperature()));
        meteoTurbulence.setText(track.getMeteo().getTurbulence().name());
        meteoShear.setText(track.getMeteo().getWindShear().name());
        meteoBurst.setText(track.getMeteo().getMicroBurst().name());
        meteoIcing.setText(track.getMeteo().getIcing().name());
        meteoWake.setText(track.getMeteo().getWake().name());

        // ACAS
        acasActive.setSelected(track.getAcas().getActive());
        acasVerticalStatus.setText(track.getAcas().getVerticalStatus().name());
        acasCrossLink.setText(track.getAcas().getCrossLinkCapability().name());
        acasSensitivity.setText(track.getAcas().getSensitivity().name());
        acasReplyInfo.setText(track.getAcas().getReplyInformation().name());
        acasAltitude.setText(track.getAcas().getAltitude().toString());
        acasRaActive.setSelected(track.getAcas().getResolutionAdvisory().isActive());
        acasCorrectionUp.setSelected(track.getAcas().getResolutionAdvisory().isRequiresCorrectionUpwards());
        acasRequireClimb.setSelected(track.getAcas().getResolutionAdvisory().isRequiresPositiveClimb());
        acasCorrectionDown.setSelected(track.getAcas().getResolutionAdvisory().isRequiresCorrectionDownwards());
        acasRequireDescend.setSelected(track.getAcas().getResolutionAdvisory().isRequiresPositiveDescend());
        acasRequiresCrossing.setSelected(track.getAcas().getResolutionAdvisory().isRequiresCrossing());
        acasSenseReversal.setSelected(track.getAcas().getResolutionAdvisory().isSenseReversal());
        acasType.setText(track.getAcas().getThreatType().name());
        acasModeS.setText(track.getAcas().getTargetModeS());
        acasTargetAltitude.setText(String.valueOf(track.getAcas().getTargetAltitude()));
        acasRange.setText(String.valueOf(track.getAcas().getTargetRange()));
        acasBearing.setText(String.valueOf(track.getAcas().getTargetBearing()));
        acasNotPassBelow.setSelected(track.getAcas().getRANotPassBelow());
        acasNotPassAbove.setSelected(track.getAcas().getRANotPassAbove());
        acasNotTurnLeft.setSelected(track.getAcas().getRANotTurnLeft());
        acasNotTurnRight.setSelected(track.getAcas().getRANotTurnRight());

        // Basic
        basicAltitude.setText(track.getAltitude().toString());
        basicBaroAltitude.setText(track.getBaroAltitude().toString());
        basicGnssAltitude.setText(String.valueOf(track.getGnssHeight()));

        // AP / ATT
        apEnabled.setSelected(track.getAutopilot());
        apVnav.setSelected(track.getLnav());
        apVnav.setSelected(track.getVnav());
        apApp.setSelected(track.getApproachMode());
        apAlt.setSelected(track.getAltitudeHold());
        apRollAngle.setText(String.valueOf(track.getRollAngle()));
        apTrackAngleRate.setText(String.valueOf(track.getTrackAngleRate()));
        apManagedFms.setSelected(track.getSelectedAltitudeManagedFms());
        apManagedMcp.setSelected(track.getSelectedAltitudeManagedMcp());
        apSelAlt.setText(String.valueOf(track.getSelectedAltitude()));
        apFmsSelAlt.setText(String.valueOf(track.getFmsSelectedAltitude()));
        apSelHeading.setText(String.valueOf(track.getSelectedHeading()));
        apSelAltSource.setText(track.getSelectedAltitudeSource().name());
    }

    public void onClose(WindowEvent e) {
        this.timer.cancel();
    }
}
