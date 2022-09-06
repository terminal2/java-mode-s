package example;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import aero.t2s.modes.Track;

class FlightsTable extends AbstractTableModel {
    private static String[] columns = {
        "ICAO",
        "CALLSIGN",
        "LAT",
        "LON",
        "ROCD (ft/min)",
        "Altitude (ft)",
        "HDG",
        "GS (kt)",
    };
    private List<Track> tracks;

    public FlightsTable(List<Track> tracks) {
        this.tracks = tracks;
    }

    public static String[] getColumns() {
        return columns;
    }

    @Override
    public int getRowCount() {
        return tracks.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            switch (columnIndex) {
                case 0: return tracks.get(rowIndex).getIcao();
                case 1: return tracks.get(rowIndex).getCallsign();
                case 2: return String.format("%2.4f", tracks.get(rowIndex).getLat());
                case 3: return String.format("%03.4f", tracks.get(rowIndex).getLon());
                case 4: return tracks.get(rowIndex).getRocd();
                case 5: return tracks.get(rowIndex).getAltitude().getAltitude();
                case 6: return Math.round(tracks.get(rowIndex).getMagneticHeading());
                case 7: return (int)tracks.get(rowIndex).getGs();
            }
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }

        return null;
    }
}
