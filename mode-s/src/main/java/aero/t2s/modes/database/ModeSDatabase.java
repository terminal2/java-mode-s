package aero.t2s.modes.database;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModeSDatabase {
    private final Map<String, ModeSAircraft> modes = new HashMap<>();
    private final AircraftTypeDatabase atypDatbase;

    public ModeSDatabase() {
        atypDatbase = new AircraftTypeDatabase();
    }

    public ModeSDatabase(Path modeSFile, Path atypeFile) {
        this.atypDatbase = new AircraftTypeDatabase(atypeFile);

        loadModeS(modeSFile);
    }

    public static ModeSDatabase createDatabase() {
        return new ModeSDatabase();
    }

    private void loadModeS(Path modeSFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(modeSFile.toUri().toURL().openStream()))) {
            // Skip first line
            if (reader.ready()) {
                reader.readLine();
            }

            while (reader.ready()) {
                String line = reader.readLine().replace("\"", "");
                String[] parts = line.split(",");

                if (parts.length >= 9) {
                    modes.put(parts[0].toUpperCase(), new ModeSAircraft(parts[0].toUpperCase(), parts[1], parts[9], atypDatbase.find(parts[5])));
                }
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("[ADS-B] Failed to read mode-s database", e);
        }

        LoggerFactory.getLogger(getClass()).info("[ADS-B] Mode-S database loaded {} mode-s addresses", modes.size());
    }

    public ModeSAircraft find(String modes) {
        return this.modes.get(modes);
    }

    public static class ModeSAircraft {
        public String modes;
        public String registration;
        public String operator;
        public String atyp;
        public String wtc;

        public ModeSAircraft(String modes, String registration, String operator, AircraftTypeDatabase.Aircraft aircraft) {
            this.modes = modes;
            this.registration = registration;
            this.operator = operator;

            if (aircraft != null) {
                this.atyp = aircraft.icao;
                this.wtc = aircraft.wtc;
            }
        }
    }
}
