package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.decoder.df.df17.data.AirborneCapability;
import aero.t2s.modes.decoder.df.df17.data.AirborneOperationalMode;

public class AircraftOperationalStatusVersion1Airborne extends AircraftOperationalStatusVersion1 {
    private int subType = 0;
    private Version version = Version.VERSION1;
    private AirborneCapability capability;
    private AirborneOperationalMode operationalMode;
    private NavigationIntegrityCategory NICp;
    private SourceIntegrityLevel SIL;
    private BarometricAltitudeIntegrityCode NICbaro;
    private Angle horizontalSource;

    public AircraftOperationalStatusVersion1Airborne(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion1Airborne decode() {
        capability = new AirborneCapability((data[5] << 8) | data[6], version);
        operationalMode = new AirborneOperationalMode((data[7] << 8) | data[8]);

        int NICsupp = (data[9] & 0b00010000) >>> 4;
        int NACp = (data[9] & 0b00001111);
        NICp = NavigationIntegrityCategory.airborne(NACp, NICsupp, 0);

        SIL = SourceIntegrityLevel.from((data[10] & 0b000110000) >>> 4);
        NICbaro = BarometricAltitudeIntegrityCode.from((data[10] & 0b00001000) >>> 3);
        horizontalSource = (data[10] & 0b00000100) != 0 ? Angle.TRUE_HEADING : Angle.MAGNETIC_HEADING;

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setVersion(version);
    }

    public int getSubType() {
        return subType;
    }

    public Version getVersion() {
        return version;
    }

    public AirborneCapability getCapability() {
        return capability;
    }

    public AirborneOperationalMode getOperationalMode() {
        return operationalMode;
    }

    public NavigationIntegrityCategory getNICp() {
        return NICp;
    }

    public SourceIntegrityLevel getSIL() {
        return SIL;
    }

    public BarometricAltitudeIntegrityCode getNICbaro() {
        return NICbaro;
    }

    public Angle getHorizontalSource() {
        return horizontalSource;
    }
}
