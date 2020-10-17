package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Meteo;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Hazard;
import aero.t2s.modes.constants.MeteoSource;

public class Bds44 extends Bds {
    private static final double WIND_DIRECTION_ACCURACY = 180d / 256d;
    private static final double SAT_ACCURACY = 0.25;
    private static final double HUMIDITY_ACCURACY = 100d / 64d;

    @Override
    public boolean attemptDecode(Track track, short[] data) {
        MeteoSource source = MeteoSource.find(data[4] >>> 4);

        if (source == MeteoSource.INVALID || source == MeteoSource.RESERVED) {
            return false;
        }

        boolean windSpeedStatus = (data[4] & 0b00001000) != 0;
        int windSpeed = (data[4] & 0b00000111) << 6 | data[5] >> 2;
        double windDirection = ((data[5] & 0b00000011) << 7 | data[6] >> 1) * WIND_DIRECTION_ACCURACY;
        if (!windSpeedStatus && windSpeed != 0) {
            return false;
        }
        // According to Annex 3 wind speed is range 0, 250
        if (windSpeedStatus && windSpeed > 250) {
            return false;
        }

        boolean isSatNegative = (data[6] & 0b00000001) == 1;
        double sat = (data[7] << 2 | data[8] >>> 6) * SAT_ACCURACY * (isSatNegative ? -1 : 1);
        if (sat > 60 || sat < -80) {
            return false;
        }

        boolean averageStaticPressureStatus = (data[8] & 0b00100000) != 0;
        int averageStaticPressure = ((data[8] & 0b00011111) << 6) | data[9] >> 2;
        if (!averageStaticPressureStatus && averageStaticPressure != 0) {
            return false;
        }

        boolean turbulenceStatus = (data[9] & 0b00000010) != 0;
        Hazard turbulence = Hazard.find(((data[9] & 0b00000001) << 1) | data[10] >>> 7);
        if (!turbulenceStatus && turbulence != Hazard.NIL) {
            return false;
        }

        boolean humidityStatus = (data[10] & 0b01000000) != 0;
        double humidity = (data[10] & 0b00111111) * HUMIDITY_ACCURACY;
        if (!humidityStatus && humidity != 0) {
            return false;
        }

        Meteo meteo = track.getMeteo();
        if (windSpeedStatus) {
            meteo.setWindSpeed(windSpeed);
            meteo.setWindDirection(windDirection);
        }

        if (averageStaticPressureStatus)
            meteo.setAverageStaticPressure(averageStaticPressure);

        if (turbulenceStatus)
            meteo.setTurbulence(turbulence);

        meteo.setStaticAirTemperature(sat);

        if (humidityStatus)
            meteo.setHumidity(humidity);

        return true;
    }
}
