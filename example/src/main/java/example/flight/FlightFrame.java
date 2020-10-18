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

    public FlightFrame(Track track) {
        this.track = track;

        setContentPane(mainPanel);
        pack();
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
                "ATYP: %s\n" +
                "OPERATOR: %s\n" +
                "MODE A: %04d\n" +
                "-------------------------------\n" +
                "Altitude: %d%s (step size: %d)\n" +
                "Baro Altitude: %d\n" +
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
                " GS: %dkt\n" +
                "IAS: %dkt\n" +
                "TAS: %dkt\n" +
                "VX, VY: %d, %d\n" +
                "------------------------------\n"
            ,
            track.getIcao(),
            track.getCallsign(),
            track.getWtc(),
            track.getAtype(),
            track.getOperator(),
            track.getModeA(),
            (int)track.getAltitude().getAltitude(), track.getAltitude().isMetric() ? "M" : "FT", track.getAltitude().getStep(),
            track.getBaroAltitude(),
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
            (int)track.getGs(),
            track.getIas(),
            (int)track.getTas(),
            track.getVx(), track.getVy()
        ));
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
