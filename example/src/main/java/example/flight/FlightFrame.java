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
    private JTable gcibReportTable;
    private JList<String> meteoHazards;
    private JLabel meteoHumidity;
    private JLabel meteoSat;
    private JLabel meteoStaticPressure;
    private JLabel meteoWind;
    private JLabel meteoRadioHeight;
    private JPanel mainPanel;
    private JTextArea flightModelLeft;
    private JTextArea flightInfoRight;
    private JTextArea infoAccuracy;
    private JTextArea infoAbds;
    private JTextArea acas;

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

        ((GcibTable)gcibReportTable.getModel()).fireTableDataChanged();

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
                "Width & Length Code: %s\n" +
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
                "Baro Altitude: %d\n" +
                "Geometric offset: %d\n" +
                "GNSS Altitude: %d\n" +
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
                "Heading Source: %s\n" +
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
            track.getLengthWidthCode().name(),
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
            track.getBaroAltitude().toString(),
            track.getGeometricHeightOffset(),
            track.getGnssHeight(),
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
            track.isMagneticHeading() ? "MAGNETIC" : "TRUE",
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

        infoAccuracy.setText(String.format(
            "NIC: %d\n" +
            "NICa: %d\n" +
            "NICb: %d\n" +
            "NICc: %d\n" +
            "NACv: %d\n" +
            "NACp: %s\n" +
            "SIL: %d\n" +
            "----------------------------\n",
            track.getNIC(),
            track.getNICa(),
            track.getNICb(),
            track.getNICc(),
            track.getNACv(),
            track.getNACp(),
            track.getSil()
        ));

        infoAbds.setText(String.format(
            "Version: %s\n" +
            "Single Antenna: %s\n",
            track.getVersion().name(),
            track.getSingleAntenna() ? "YES" : "NO"
        ));
    }

    private String formatAcasResolution() {
        if (!track.getAcas().getResolutionAdvisory().isActive()) {
            return "No Resolution";
        }

        return track.getAcas().getResolutionAdvisory().toString();
    }

    private void createUIComponents() {
        gcibReportTable = new JTable(new GcibTable(track));
        gcibReportTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private class GcibTable extends AbstractTableModel {
        private final String[] GCIB = {
            "0,5 Extended squitter airborne position",
            "0,6 Extended squitter surface position",
            "0,7 Extended squitter identification and category",
            "0,7 Extended squitter status",
            "0,9 Extended squitter airborne velocity information",
            "0,A Extended squitter event-driven information",
            "2,0 Aircraft identification",
            "2,1 Aircraft registration number",
            "4,0 Selected vertical intention",
            "4,1 Next waypoint identifier",
            "4,2 Next waypoint position",
            "4,3 Next waypoint information",
            "4,4 Meteorological routine report",
            "4,5 Meteorological hazard report",
            "4,8 VHF channel report",
            "5,0 Track and turn report",
            "5,1 Position coarse",
            "5,2 Position fine",
            "5,3 Air-referenced state vector",
            "5,4 Waypoint 1",
            "5,5 Waypoint 2",
            "5,6 Waypoint 3",
            "5,F Quasi-static parameter monitoring",
            "6,0 Heading and speed report",
        };

        public GcibTable(Track track) {
        }

        @Override
        public int getRowCount() {
            if (track.getCapabilityReport().isAvailable()) {
                return 24;
            }

            return 1;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0: return "GCIB";
                case 1: return "Available";
            }

            return null;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (!track.getCapabilityReport().isAvailable()) {
                if (columnIndex == 0)
                    return "Not Available";
                else
                    return "";
            }

            if (columnIndex == 0) {
                return GCIB[rowIndex];
            }

            switch (rowIndex) {
                case 0: return track.getCapabilityReport().isBds05() ? "YES" : "NO";
                case 1: return track.getCapabilityReport().isBds06() ? "YES" : "NO";
                case 2: return track.getCapabilityReport().isBds07() ? "YES" : "NO";
                case 3: return track.getCapabilityReport().isBds08() ? "YES" : "NO";
                case 4: return track.getCapabilityReport().isBds09() ? "YES" : "NO";
                case 5: return track.getCapabilityReport().isBds0A() ? "YES" : "NO";
                case 6: return track.getCapabilityReport().isBds20() ? "YES" : "NO";
                case 7: return track.getCapabilityReport().isBds21() ? "YES" : "NO";
                case 8: return track.getCapabilityReport().isBds40() ? "YES" : "NO";
                case 9: return track.getCapabilityReport().isBds41() ? "YES" : "NO";
                case 10: return track.getCapabilityReport().isBds42() ? "YES" : "NO";
                case 11: return track.getCapabilityReport().isBds43() ? "YES" : "NO";
                case 12: return track.getCapabilityReport().isBds44() ? "YES" : "NO";
                case 13: return track.getCapabilityReport().isBds45() ? "YES" : "NO";
                case 14: return track.getCapabilityReport().isBds48() ? "YES" : "NO";
                case 15: return track.getCapabilityReport().isBds50() ? "YES" : "NO";
                case 16: return track.getCapabilityReport().isBds51() ? "YES" : "NO";
                case 17: return track.getCapabilityReport().isBds52() ? "YES" : "NO";
                case 18: return track.getCapabilityReport().isBds53() ? "YES" : "NO";
                case 19: return track.getCapabilityReport().isBds54() ? "YES" : "NO";
                case 20: return track.getCapabilityReport().isBds55() ? "YES" : "NO";
                case 21: return track.getCapabilityReport().isBds56() ? "YES" : "NO";
                case 22: return track.getCapabilityReport().isBds5F() ? "YES" : "NO";
                case 23: return track.getCapabilityReport().isBds60() ? "YES" : "NO";
            }

            return null;
        }
    }
}
