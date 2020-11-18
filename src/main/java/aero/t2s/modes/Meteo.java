package aero.t2s.modes;

import aero.t2s.modes.constants.Hazard;

public class Meteo {
    private Hazard turbulence = Hazard.NIL;
    private Hazard windShear = Hazard.NIL;
    private Hazard microBurst = Hazard.NIL;
    private Hazard icing = Hazard.NIL;
    private Hazard wake = Hazard.NIL;
    private double staticAirTemperature = 0;
    private int averageStaticPressure = 0;
    private int radioHeight = 0;
    private double humidity = 0;
    private int windSpeed = 0;
    private double windDirection = 0;

    public Hazard getTurbulence() {
        return turbulence;
    }

    public Meteo setTurbulence(Hazard turbulence) {
        this.turbulence = turbulence;
        return this;
    }

    public Hazard getWindShear() {
        return windShear;
    }

    public Meteo setWindShear(Hazard windShear) {
        this.windShear = windShear;
        return this;
    }

    public Hazard getMicroBurst() {
        return microBurst;
    }

    public Meteo setMicroBurst(Hazard microBurst) {
        this.microBurst = microBurst;
        return this;
    }

    public Hazard getIcing() {
        return icing;
    }

    public Meteo setIcing(Hazard icing) {
        this.icing = icing;
        return this;
    }

    public Hazard getWake() {
        return wake;
    }

    public Meteo setWake(Hazard wake) {
        this.wake = wake;
        return this;
    }

    public double getStaticAirTemperature() {
        return staticAirTemperature;
    }

    public Meteo setStaticAirTemperature(double staticAirTemperature) {
        this.staticAirTemperature = staticAirTemperature;
        return this;
    }

    public int getAverageStaticPressure() {
        return averageStaticPressure;
    }

    public Meteo setAverageStaticPressure(int averageStaticPressure) {
        this.averageStaticPressure = averageStaticPressure;
        return this;
    }

    public int getRadioHeight() {
        return radioHeight;
    }

    public Meteo setRadioHeight(int radioHeight) {
        this.radioHeight = radioHeight;
        return this;
    }

    public double getHumidity() {
        return humidity;
    }

    public Meteo setHumidity(double humidity) {
        this.humidity = humidity;
        return this;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public Meteo setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
        return this;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public Meteo setWindDirection(double windDirection) {
        this.windDirection = windDirection;
        return this;
    }
}
