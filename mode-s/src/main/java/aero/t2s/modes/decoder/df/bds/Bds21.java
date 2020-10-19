package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;
import aero.t2s.modes.decoder.Common;

public class Bds21 extends Bds {
    @Override
    public boolean attemptDecode(Track track, short[] data) {
        if (track.getCapabilityReport().isAvailable() && !track.getCapabilityReport().isBds21()) {
            return false;
        }

        boolean aircraftRegistrationStatus = (data[4] & 0b10000000) != 0;
        boolean airlineRegistrationStatus = (data[9] & 0b00010000) != 0;

        if (!aircraftRegistrationStatus && (data[4] | data[5] | data[6] | data[7] | data[8] | data[9] >>> 5) != 0) {
            return false;
        }

        if (!airlineRegistrationStatus && ((data[9] & 0b00001111) | data[10]) != 0) {
            return false;
        }

        if (aircraftRegistrationStatus) {
            String registration = Common.charToString((data[4] & 0b01111110) >>> 1) // Char 1
                + Common.charToString(((data[4] & 0b00000001) << 5) | (data[5] >>> 3)) // Char 2
                + Common.charToString(((data[5] & 0b00000111) << 3) | (data[6] >>> 5)) // Char 3
                + Common.charToString(((data[6] & 0b00011111) << 1) | (data[7] >>> 7)) // Char 4
                + Common.charToString((data[7] & 0b01111110) >>> 1) // Char 5
                + Common.charToString(((data[7] & 0b00000001) << 5) | (data[8] >>> 3)) // Char 6
                + Common.charToString(((data[8] & 0b00000111) << 3) | (data[9] >>> 3)) // Char 7
                ;


            if (registration.contains("#")) {
                return false;
            }

            track.setRegistration(registration.replace("_", ""));
        }

        if (airlineRegistrationStatus) {
            String airline = Common.charToString(((data[9] & 0b00001111) << 2 | (data[10] >>> 6)))
                + Common.charToString(data[10] & 0b00111111);

            if (airline.contains("_") || airline.contains("#")) {
                return false;
            }

            track.setOperator(airline);
        }

        return true;
    }
}
