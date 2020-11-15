package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;

public class Bds21 extends Bds {
    private boolean statusAircraftRegistration;
    private boolean statusAirlineRegistration;
    private String registration;
    private String airline;

    public Bds21(short[] data) {
        super(data);

        statusAircraftRegistration = (data[4] & 0b10000000) != 0;
        statusAirlineRegistration = (data[9] & 0b00010000) != 0;

        if (!statusAircraftRegistration && (data[4] | data[5] | data[6] | data[7] | data[8] | data[9] >>> 5) != 0) {
            invalidate();
            return;
        }

        if (!statusAirlineRegistration && ((data[9] & 0b00001111) | data[10]) != 0) {
            invalidate();
            return;
        }

        if (data[4] != 0b00100000) {
            invalidate();
            return;
        }

        if (statusAircraftRegistration) {
            // Char 7
            registration = Common.charToString((data[4] & 0b01111110) >>> 1) // Char 1
                + Common.charToString(((data[4] & 0b00000001) << 5) | (data[5] >>> 3)) // Char 2
                + Common.charToString(((data[5] & 0b00000111) << 3) | (data[6] >>> 5)) // Char 3
                + Common.charToString(((data[6] & 0b00011111) << 1) | (data[7] >>> 7)) // Char 4
                + Common.charToString((data[7] & 0b01111110) >>> 1) // Char 5
                + Common.charToString(((data[7] & 0b00000001) << 5) | (data[8] >>> 3)) // Char 6
                + Common.charToString(((data[8] & 0b00000111) << 3) | (data[9] >>> 3));


            if (registration.contains("#")) {
                invalidate();
                return;
            }

            registration = registration.replace("_", "");
        }

        if (statusAirlineRegistration) {
            airline = Common.charToString(((data[9] & 0b00001111) << 2 | (data[10] >>> 6)))
                + Common.charToString(data[10] & 0b00111111);

            if (airline.contains("_") || airline.contains("#")) {
                invalidate();
                return;
            }
        }
    }

    @Override
    public void apply(Track track) {
        if (statusAircraftRegistration) {
            track.setRegistration(registration);
        }

        if (statusAirlineRegistration) {
            track.setOperator(airline);
        }
    }

    @Override
    protected void reset() {
        statusAircraftRegistration = false;
        statusAirlineRegistration = false;
        registration = null;
        airline = null;
    }

    public boolean isStatusAircraftRegistration() {
        return statusAircraftRegistration;
    }

    public boolean isStatusAirlineRegistration() {
        return statusAirlineRegistration;
    }

    public String getRegistration() {
        return registration;
    }

    public String getAirline() {
        return airline;
    }
}
