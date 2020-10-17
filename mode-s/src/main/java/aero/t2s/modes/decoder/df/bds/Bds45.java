package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Meteo;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Hazard;

public class Bds45 extends Bds {
    private static final double SAT_ACCURACY = 0.25d;
    private static final int RADIO_HEIGHT_ACCURACY = 16;

    @Override
    public boolean attemptDecode(Track track, short[] data) {
        if (!track.getCapabilityReport().isBds45()) {
            return false;
        }

        boolean turbulenceStatus = data[4] >>> 7 == 1;
        boolean windShearStatus = ((data[4] & 0b00010000) >>> 4) == 1;
        boolean microBurstStatus = ((data[4] & 0b00000010) >>> 1) == 1;
        boolean icingStatus = ((data[5] & 0b01000000) >>> 6) == 1;
        boolean wakeStatus = ((data[5] & 0b00001000) >>> 3) == 1;
        boolean satStatus = (data[5] & 0b00000001) == 1;
        boolean averageStaticPressureStatus = ((data[7] & 0b00100000) >>> 5) == 1;
        boolean radioHeightStatus = ((data[7] & 0b00000010) >>> 1) == 1;

        // Reserved
        if ((data[10] & 0b00011111) != 0) {
            return false;
        }

        Hazard turbulence = Hazard.find((data[4] & 0b01100000) >>> 5);
        if (!turbulenceStatus && turbulence != Hazard.NIL) {
            return false;
        }

        Hazard windShear = Hazard.find((data[4] & 0b00001100) >>> 2);
        if (!windShearStatus && windShear != Hazard.NIL) {
            return false;
        }

        Hazard microBurst = Hazard.find((data[4] & 0b00000001) << 1 | data[5] >> 7);
        if (!microBurstStatus && microBurst != Hazard.NIL) {
            return false;
        }

        Hazard icing = Hazard.find((data[5] & 0b00110000) >>> 4);
        if (!icingStatus && icing != Hazard.NIL) {
            return false;
        }

        Hazard wake = Hazard.find((data[5] & 0b00000110) >>> 1);
        if (!wakeStatus && wake != Hazard.NIL) {
            return false;
        }

        boolean isSatNegative = data[6] >>> 7 == 1;
        double sat = (((data[6] & 0b01111111) << 2) | data[7] >>> 6) * SAT_ACCURACY * (isSatNegative ? -1 : 1);
        if (!satStatus && sat != 0) {
            return false;
        }
        if (satStatus && (sat > 60 || sat < -80)) {
            return false;
        }

        int averageStaticPressure = ((data[7] & 0b00011111) << 6) | data[8] >>> 2;
        if (!averageStaticPressureStatus && averageStaticPressure != 0) {
            return false;
        }

        int radioHeight = (((data[8] & 0b00000001) << 11) | (data[9] << 3) | data[10] >>> 5) * RADIO_HEIGHT_ACCURACY;
        if (!radioHeightStatus && radioHeight != 0) {
            return false;
        }

        Meteo meteo = track.getMeteo();

        if (turbulenceStatus)
            meteo.setTurbulence(turbulence);
        if (windShearStatus)
            meteo.setWindShear(windShear);
        if (microBurstStatus)
            meteo.setMicroBurst(microBurst);
        if (icingStatus)
            meteo.setIcing(icing);
        if (wakeStatus)
            meteo.setWake(wake);
        if (satStatus)
            meteo.setStaticAirTemperature(sat);
        if (averageStaticPressureStatus)
            meteo.setAverageStaticPressure(averageStaticPressure);
        if (radioHeightStatus)
            meteo.setRadioHeight(radioHeight);

        return true;
    }
}
