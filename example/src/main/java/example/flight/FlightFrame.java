package example.flight;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Hazard;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Timer;
import java.util.TimerTask;

public class FlightFrame extends JFrame {
    java.util.Timer timer = new Timer();
    private Track track;

    private JTabbedPane tabbedPane1;
    private JList<String> meteoHazards;
    private JLabel meteoHumidity;
    private JLabel meteoSat;
    private JLabel meteoStaticPressure;
    private JLabel meteoWind;
    private JLabel meteoRadioHeight;
    private JPanel mainPanel;
    private JTextArea flightModelLeft;
    private JTextArea infoAbds;
    private JTextArea acas;

    private JTextArea register05;
    private JTextArea register06;
    private JTextArea register07;
    private JTextArea register08;
    private JTextArea register09;
    private JTextArea register17;

    public FlightFrame(Track track) {
        this.track = track;

        setContentPane(mainPanel);
        setSize(1024, 1024);

        setVisible(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateContent();
            }
        }, 500, 500);
    }

    private void updateContent() {
        setTitle(track.getCallsign() + " - " + track.getIcao());

        // Meteo Hazard
        ((DefaultListModel<String>)meteoHazards.getModel()).clear();
        if (track.getMeteo().getTurbulence() != Hazard.NIL) {
            ((DefaultListModel<String>)meteoHazards.getModel()).addElement(track.getMeteo().getTurbulence().toString() + " TURBULENCE");
        }
        if (track.getMeteo().getIcing() != Hazard.NIL) {
            ((DefaultListModel<String>)meteoHazards.getModel()).addElement(track.getMeteo().getIcing().toString() + " ICING");
        }
        if (track.getMeteo().getMicroBurst() != Hazard.NIL) {
            ((DefaultListModel<String>)meteoHazards.getModel()).addElement(track.getMeteo().getMicroBurst().toString() + " MICROBURST");
        }
        if (track.getMeteo().getWake() != Hazard.NIL) {
            ((DefaultListModel<String>)meteoHazards.getModel()).addElement(track.getMeteo().getWake().toString() + " WAKE VORTEX");
        }
        if (track.getMeteo().getWindShear() != Hazard.NIL) {
            ((DefaultListModel<String>)meteoHazards.getModel()).addElement(track.getMeteo().getWindShear().toString() + " WINDSHEAR");
        }
        if (meteoHazards.getModel().getSize() == 0) {
            ((DefaultListModel<String>)meteoHazards.getModel()).addElement("NIL");
        }

        // Meteo Info
        meteoHumidity.setText(track.getMeteo().getHumidity() != 0 ? String.format("Humidity: %2.0f%%", track.getMeteo().getHumidity()) : "Humidity: N/A");
        meteoSat.setText(track.getMeteo().getStaticAirTemperature() != 0 ? String.format("SAT: %2.0f", track.getMeteo().getStaticAirTemperature()) : "SAT: N/A");
        meteoStaticPressure.setText(track.getMeteo().getAverageStaticPressure() != 0 ? String.format("Average pressure: %d", track.getMeteo().getAverageStaticPressure()) : "Average Pressure: N/A");
        meteoRadioHeight.setText(track.getMeteo().getRadioHeight() != 0 ? String.format("Radio Height: %d", track.getMeteo().getRadioHeight()) : "Radio Height: N/A");
        meteoWind.setText(String.format("Wind: %d / %dkt", (int)track.getMeteo().getWindDirection(), track.getMeteo().getWindSpeed()));

        // Flight info left
        flightModelLeft.setText(String.format(
                "ICAO: %s\n" +
                "CALLSIGN: %s\n" +
                "WTC: %s\n" +
                "ATYP: %s\n (REG: %s)\n" +
                "OPERATOR: %s\n" +
                "MODE A: %04d\n" +
                "State: %s\n" +
                "SPI: %s\n" +
                "Temp Alert: %s\n" +
                "Emergency: %s\n" +
                "Emergency Status: %s\n" +
                "Flight Status Alert: %s\n" +
                "Flight Status SPI: %s\n" +
                "-------------------------------\n" +
                "Altitude: %d%s (step size: %d)\n" +
                "Geometric offset: %d\n" +
                "Selected Altitude Source: %s\n" +
                "Selected Altitude: %d\n" +
                "Selected Altitude FMS: %s\n" +
                "Selected Altitude MCP: %s\n" +
                "FMS SelectedAltitude: %d\n" +
                "------------------------------\n" +
                "BARO: %4.1f\n" +
                "------------------------------\n" +
                "ROCD Available: %s\n" +
                "ROCD: %dft/min\n" +
                "ROCD Baro Available: %s\n" +
                "Baro ROCD: %dft/min\n" +
                "------------------------------\n" +
                "Magnetic Heading: %d\n" +
                "True Heading: %d\n" +
                "Selected Heading: %d\n" +
                "------------------------------\n" +
                "Roll Angle: %2.1f\n" +
                "Track rate of change: %2.1f\n" +
                "------------------------------\n" +
                " GS: %dkt\n" +
                "IAS: %dkt\n" +
                "TAS: %dkt\n" +
                "Mach: %1.3fkt\n" +
                "VX, VY: %d, %d\n" +
                "------------------------------\n" +
                "Autopilot: %s\n" +
                "LNAV: %s\n" +
                "VNAV: %s\n" +
                "ALT HOLD: %s\n" +
                "APP: %s\n" +
                "------------------------------\n"
            ,
            track.getIcao(),
            track.getCallsign(),
            track.getWtc(),
            track.getAtype(), track.getRegistration(),
            track.getOperator(),
            track.getModeA(),
            track.isGroundBit() ? "GROUND" : "AIRBORNE",
            track.getSpi() ? "YES" : "NO",
            track.getTempAlert() ? "YES" : "NO",
            track.getEmergency() ? "YES" : "NO",
            track.getEmergencyState().name(),
            track.getFlightStatus().isAlert() ? "YES" : "NO",
            track.getFlightStatus().isSpi() ? "YES" : "NO",
            (int)track.getAltitude().getAltitude(), track.getAltitude().isMetric() ? "M" : "FT", track.getAltitude().getStep(),
            track.getGeometricHeightOffset(),
            track.getSelectedAltitudeSource().toString(),
            track.getSelectedAltitude(),
            track.getSelectedAltitudeManagedFms() ? "YES" : "NO",
            track.getSelectedAltitudeManagedMcp() ? "YES" : "NO",
            track.getFmsSelectedAltitude(),
            track.getBaroSetting(),
            track.getRocdAvailable() ? "YES" : "NO",
            track.getRocd(),
            track.getRocdSourceBaro() ? "YES" : "NO",
            (int)track.getBaroRocd(),
            (int)track.getMagneticHeading(),
            (int)track.getTrueHeading(),
            (int)track.getSelectedHeading(),
            track.getRollAngle(),
            track.getTrackAngleRate(),
            (int)track.getGs(),
            track.getIas(),
            (int)track.getTas(),
            track.getMach(),
            track.getVx(), track.getVy(),
            track.getAutopilot() ? "YES" : "NO",
            track.getLnav() ? "YES" : "NO",
            track.getVnav() ? "YES" : "NO",
            track.getAltitudeHold() ? "YES" : "NO",
            track.getApproachMode() ? "YES" : "NO"
        ));

        acas.setText(String.format(
            "Vertical Status: %s\n" +
            "Cross-Link Capability: %s\n" +
            "Sensitivity: %s\n" +
            "Reply Information: %s\n" +
            "Altitude: %d%s (step %dft)\n" +
            "Active: %s\n" +
            "Resolution: %s\n" +
            "Multiple threats: %s\n" +
            "RA do not pass below: %s\n" +
            "RA do not pass above: %s\n" +
            "RA do not turn left: %s\n" +
            "RA do not turn right: %s\n" +
            "Threat type: %s\n" +
            "Target MODE-S: %s\n" +
            "Target Altitude: %d\n" +
            "Target Bearing: %d\n" +
            "Target Range: %2.1f\n" +
            "------------------------------\n",
            track.getAcas().getVerticalStatus().name(),
            track.getAcas().getCrossLinkCapability().name(),
            track.getAcas().getSensitivity().name(),
            track.getAcas().getReplyInformation().name(),
            (int)track.getAcas().getAltitude().getAltitude(), track.getAcas().getAltitude().isMetric() ? "M" : "FT", track.getAcas().getAltitude().getStep(),
            track.getAcas().getActive() ? "YES" : "NO",
            formatAcasResolution(),
            track.getAcas().getMultipleThreats() ? "YES" : "NO",
            track.getAcas().getRANotPassBelow() ? "YES" : "NO",
            track.getAcas().getRANotPassAbove() ? "YES" : "NO",
            track.getAcas().getRANotTurnLeft() ? "YES" : "NO",
            track.getAcas().getRANotTurnRight() ? "YES" : "NO",
            track.getAcas().getThreatType().name(),
            track.getAcas().getTargetModeS(),
            (int)track.getAcas().getTargetAltitude(),
            track.getAcas().getTargetBearing(),
            track.getAcas().getTargetRange()
        ));

        infoAbds.setText(
            "Version: %s\n" +
            track.getVersion().name()
        );

        register05.setText(track.register05().toString());
        register06.setText(track.register06().toString());
        register07.setText(track.register07().toString());
        register08.setText(track.register08().toString());
        register09.setText(track.register09().toString());
        register17.setText(track.register17().toString());
    }

    private String formatAcasResolution() {
        if (!track.getAcas().getResolutionAdvisory().isActive()) {
            return "No Resolution";
        }

        return track.getAcas().getResolutionAdvisory().toString();
    }
}
