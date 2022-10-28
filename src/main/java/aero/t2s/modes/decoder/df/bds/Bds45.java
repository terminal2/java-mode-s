package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Altitude;
import aero.t2s.modes.Meteo;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.Hazard;
import aero.t2s.modes.decoder.AltitudeEncoding;

public class Bds45 extends Bds {
    private static final double SAT_ACCURACY = 0.25d;
    private static final int RADIO_HEIGHT_ACCURACY = 16;

    private boolean statusTurbulence;
    private boolean statusWindShear;
    private boolean statusMicroBurst;
    private boolean statusIcing;
    private boolean statusWake;
    private boolean statusSat;
    private boolean statusAverageStaticPressure;
    private boolean statusRadioHeight;

    private Hazard turbulence;
    private Hazard windShear;
    private Hazard microBurst;
    private Hazard icing;
    private Hazard wake;
    private double sat;
    private int averageStaticPressure;
    private int radioHeight;

    public Bds45(short[] data) {
        super(data);

        statusTurbulence = data[4] >>> 7 == 1;
        statusWindShear = ((data[4] & 0b00010000) >>> 4) == 1;
        statusMicroBurst = ((data[4] & 0b00000010) >>> 1) == 1;
        statusIcing = ((data[5] & 0b01000000) >>> 6) == 1;
        statusWake = ((data[5] & 0b00001000) >>> 3) == 1;
        statusSat = (data[5] & 0b00000001) == 1;
        statusAverageStaticPressure = ((data[7] & 0b00100000) >>> 5) == 1;
        statusRadioHeight = ((data[8] & 0b00000010) >>> 1) == 1;

        // Reserved
        if ((data[10] & 0b00011111) != 0) {
            invalidate();
            return;
        }

        if (! (statusTurbulence || statusWindShear || statusMicroBurst || statusIcing || statusWake || statusSat || statusAverageStaticPressure || statusRadioHeight)) {
            invalidate();
            return;
        }

        turbulence = Hazard.find((data[4] & 0b01100000) >>> 5);
        if (!statusTurbulence && turbulence != Hazard.NIL) {
            invalidate();
            return;
        }

        windShear = Hazard.find((data[4] & 0b00001100) >>> 2);
        if (!statusWindShear && windShear != Hazard.NIL) {
            invalidate();
            return;
        }

        microBurst = Hazard.find((data[4] & 0b00000001) << 1 | data[5] >> 7);
        if (!statusMicroBurst && microBurst != Hazard.NIL) {
            invalidate();
            return;
        }

        icing = Hazard.find((data[5] & 0b00110000) >>> 4);
        if (!statusIcing && icing != Hazard.NIL) {
            invalidate();
            return;
        }

        wake = Hazard.find((data[5] & 0b00000110) >>> 1);
        if (!statusWake && wake != Hazard.NIL) {
            invalidate();
            return;
        }

        boolean isSatNegative = data[6] >>> 7 == 1;
        sat = (((data[6] & 0b01111111) << 2) | data[7] >>> 6) * SAT_ACCURACY * (isSatNegative ? -1 : 1);
        if (!statusSat && sat != 0) {
            invalidate();
            return;
        }
        if (statusSat && (sat > 60 || sat < -80)) {
            invalidate();
            return;
        }

        averageStaticPressure = ((data[7] & 0b00011111) << 6) | data[8] >>> 2;
        if (!statusAverageStaticPressure && averageStaticPressure != 0) {
            invalidate();
            return;
        }
        if (statusAverageStaticPressure && averageStaticPressure >= 1050) {
            invalidate();
            return;
        }

        radioHeight = (((data[8] & 0b00000001) << 11) | (data[9] << 3) | data[10] >>> 5) * RADIO_HEIGHT_ACCURACY;
        if (!statusRadioHeight && radioHeight != 0) {
            invalidate();
            return;
        }

        // Windshear + turbulence + microburst => Very unlikely to be a BDS 45 valid message let's flag it as invalid
        if (statusTurbulence && statusWindShear && statusMicroBurst) {
            invalidate();
            return;
        }

        if (statusMicroBurst) {
            // If message is DF20 (altitude encoding) and altitude is above 10000ft microburst is unlikely flag as invalid
            if (data[0] >>> 3 == 20) {
                Altitude altitude = AltitudeEncoding.decode((data[2] & 0x1F) << 8 | data[3]);
                if (altitude.getAltitude() > 10_000) {
                    invalidate();
                    return;
                }
            }
            // DF 21 message since we do not have altitude info and message is likely to be BDS17 flag BDS45 on DF21 with microburst as invalid
            else {
                invalidate();
                return;
            }
        }

        if (statusTurbulence || statusWindShear || statusMicroBurst || statusIcing || statusWake) {
            boolean nothing = true;

            if (statusTurbulence && turbulence != Hazard.NIL) {
                nothing = false;
            }
            if (statusWindShear && windShear != Hazard.NIL) {
                nothing = false;
            }
            if (statusMicroBurst && microBurst != Hazard.NIL) {
                nothing = false;
            }
            if (statusIcing && icing != Hazard.NIL) {
                nothing = false;
            }
            if (statusWake && wake != Hazard.NIL) {
                nothing = false;
            }

            if (nothing) {
                invalidate();
                return;
            }
        }
    }


    @Override
    public void apply(Track track) {
        Meteo meteo = track.getMeteo();

        if (statusTurbulence)
            meteo.setTurbulence(turbulence);
        if (statusWindShear)
            meteo.setWindShear(windShear);
        if (statusMicroBurst)
            meteo.setMicroBurst(microBurst);
        if (statusIcing)
            meteo.setIcing(icing);
        if (statusWake)
            meteo.setWake(wake);
        if (statusSat)
            meteo.setStaticAirTemperature(sat);
        if (statusAverageStaticPressure)
            meteo.setAverageStaticPressure(averageStaticPressure);
        if (statusRadioHeight)
            meteo.setRadioHeight(radioHeight);
    }

    @Override
    protected void reset() {
        statusTurbulence = false;
        statusWindShear = false;
        statusMicroBurst = false;
        statusIcing = false;
        statusWake = false;
        statusSat = false;
        statusAverageStaticPressure = false;
        statusRadioHeight = false;

        turbulence = null;
        windShear = null;
        microBurst = null;
        icing = null;
        wake = null;

        sat = 0;
        averageStaticPressure = 0;
        radioHeight = 0;
    }

    public boolean isStatusTurbulence() {
        return statusTurbulence;
    }

    public boolean isStatusWindShear() {
        return statusWindShear;
    }

    public boolean isStatusMicroBurst() {
        return statusMicroBurst;
    }

    public boolean isStatusIcing() {
        return statusIcing;
    }

    public boolean isStatusWake() {
        return statusWake;
    }

    public boolean isStatusSat() {
        return statusSat;
    }

    public boolean isStatusAverageStaticPressure() {
        return statusAverageStaticPressure;
    }

    public boolean isStatusRadioHeight() {
        return statusRadioHeight;
    }

    public Hazard getTurbulence() {
        return turbulence;
    }

    public Hazard getWindShear() {
        return windShear;
    }

    public Hazard getMicroBurst() {
        return microBurst;
    }

    public Hazard getIcing() {
        return icing;
    }

    public Hazard getWake() {
        return wake;
    }

    public double getSat() {
        return sat;
    }

    public int getAverageStaticPressure() {
        return averageStaticPressure;
    }

    public int getRadioHeight() {
        return radioHeight;
    }
}
