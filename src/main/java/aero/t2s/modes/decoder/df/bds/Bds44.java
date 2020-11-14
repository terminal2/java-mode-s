package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Meteo;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Hazard;
import aero.t2s.modes.constants.MeteoSource;

public class Bds44 extends Bds {
    private static final double WIND_DIRECTION_ACCURACY = 180d / 256d;
    private static final double SAT_ACCURACY = 0.25;
    private static final double HUMIDITY_ACCURACY = 100d / 64d;

    private final MeteoSource source;
    private int windSpeed;
    private boolean statusWindSpeed;
    private double windDirection;
    private double humidity;
    private boolean statusHumidity;
    private boolean statusTurbulence;
    private Hazard turbulence;
    private int averageStaticPressure;
    private boolean statusAverageStaticPressure;
    private double staticAirTemperature;

    public Bds44(short[] data) {
        super(data);

        source = MeteoSource.find(data[4] >>> 4);

        if (source == MeteoSource.INVALID || source == MeteoSource.RESERVED) {
            invalidate();
            return;
        }

        statusWindSpeed = (data[4] & 0b00001000) != 0;
        windSpeed = (data[4] & 0b00000111) << 6 | data[5] >> 2;
        windDirection = ((data[5] & 0b00000011) << 7 | data[6] >> 1) * WIND_DIRECTION_ACCURACY;
        if (!statusWindSpeed && windSpeed != 0) {
            invalidate();
            return;
        }
        // According to Annex 3 wind speed is range 0, 250
        if (statusWindSpeed && windSpeed > 250) {
            invalidate();
            return;
        }

        boolean isSatNegative = (data[6] & 0b00000001) == 1;
        staticAirTemperature = (data[7] << 2 | data[8] >>> 6) * SAT_ACCURACY * (isSatNegative ? -1 : 1);
        if (staticAirTemperature > 60 || staticAirTemperature < -80) {
            invalidate();
            return;
        }

        statusAverageStaticPressure = (data[8] & 0b00100000) != 0;
        averageStaticPressure = ((data[8] & 0b00011111) << 6) | data[9] >> 2;
        if (!statusAverageStaticPressure && averageStaticPressure != 0) {
            invalidate();
            return;
        }

        statusTurbulence = (data[9] & 0b00000010) != 0;
        turbulence = Hazard.find(((data[9] & 0b00000001) << 1) | data[10] >>> 7);
        if (!statusTurbulence && turbulence != Hazard.NIL) {
            invalidate();
            return;
        }

        statusHumidity = (data[10] & 0b01000000) != 0;
        humidity = (data[10] & 0b00111111) * HUMIDITY_ACCURACY;
        if (!statusHumidity && humidity != 0) {
            invalidate();
            return;
        }
    }

    @Override
    public void apply(Track track) {
        Meteo meteo = track.getMeteo();
        if (statusWindSpeed) {
            meteo.setWindSpeed(windSpeed);
            meteo.setWindDirection(windDirection);
        }

        if (statusAverageStaticPressure)
            meteo.setAverageStaticPressure(averageStaticPressure);

        if (statusTurbulence)
            meteo.setTurbulence(turbulence);

        meteo.setStaticAirTemperature(staticAirTemperature);

        if (statusHumidity)
            meteo.setHumidity(humidity);
    }

    public MeteoSource getSource() {
        return source;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public boolean isStatusWindSpeed() {
        return statusWindSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public double getHumidity() {
        return humidity;
    }

    public boolean isStatusHumidity() {
        return statusHumidity;
    }

    public boolean isStatusTurbulence() {
        return statusTurbulence;
    }

    public Hazard getTurbulence() {
        return turbulence;
    }

    public int getAverageStaticPressure() {
        return averageStaticPressure;
    }

    public boolean isStatusAverageStaticPressure() {
        return statusAverageStaticPressure;
    }

    public double getStaticAirTemperature() {
        return staticAirTemperature;
    }
}
