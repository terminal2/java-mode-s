package aero.t2s.modes.database;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AircraftTypeDatabase {
    private Map<String, Aircraft> aircraft = new HashMap<>();

    public AircraftTypeDatabase() {}

    public AircraftTypeDatabase(String csvFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(csvFile)))) {
            // Skip first line
            if (reader.ready()) {
                reader.readLine();
            }

            while (reader.ready()) {
                String line = reader.readLine().replace("\"", "");
                String[] parts = line.split(",");

                if (parts.length >= 8) {
                    aircraft.put(parts[2], new Aircraft(parts[2], parts[7]));
                }
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("[ADS-B] Failed to read aircraft database", e);
        }

        LoggerFactory.getLogger(getClass()).info("[ADS-B] Aircraft database loaded {} aircraft", aircraft.size());
    }

    public Aircraft find(String icao) {
        return aircraft.get(icao);
    }

    public static class Aircraft {
        public String icao;
        public String wtc;

        public Aircraft(String icao, String wtc) {
            this.icao = icao;
            this.wtc = wtc;
        }
    }
}
