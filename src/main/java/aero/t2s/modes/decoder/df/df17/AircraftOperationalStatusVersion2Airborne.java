package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.decoder.df.df17.data.AirborneCapability;
import aero.t2s.modes.decoder.df.df17.data.AirborneOperationalMode;

public class AircraftOperationalStatusVersion2Airborne extends AircraftOperationalStatusVersion2 {
    private AirborneCapability capability;
    private AirborneOperationalMode operationalMode;
    private SourceIntegrityLevelSupplement SILsupp;
    private BarometricAltitudeIntegrityCode NICbaro;
    private NavigationIntegrityCategory NICp;
    private SourceIntegrityLevel SIL;
    private Angle horizontalSource;
    private GeometricVerticalAccuracy gva;
    private Version version;

    public AircraftOperationalStatusVersion2Airborne(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion2Airborne decode() {
        version = Version.VERSION2;
        capability = new AirborneCapability((data[5] << 8) | data[6], version);
        operationalMode = new AirborneOperationalMode((data[7] << 8) | data[8]);

        int NICsuppA = (data[9] & 0b00010000) >>> 4;
        int NACp = (data[9] & 0b00001111);
        NICp = NavigationIntegrityCategory.airborne(NACp, NICsuppA, 0);

        gva = GeometricVerticalAccuracy.from((data[10] & 0b11000000) >>> 6);
        SIL = SourceIntegrityLevel.from((data[10] & 0b000110000) >>> 4);
        NICbaro = BarometricAltitudeIntegrityCode.from((data[10] & 0b00001000) >>> 3);
        horizontalSource = (data[10] & 0b00000100) != 0 ? Angle.TRUE_HEADING : Angle.MAGNETIC_HEADING;
        SILsupp = SourceIntegrityLevelSupplement.from((data[10] & 0b00000010) >>> 1);

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setVersion(version);
    }

    public AirborneCapability getCapability() {
        return capability;
    }

    public AirborneOperationalMode getOperationalMode() {
        return operationalMode;
    }

    public SourceIntegrityLevelSupplement getSILsupp() {
        return SILsupp;
    }

    public BarometricAltitudeIntegrityCode getNICbaro() {
        return NICbaro;
    }

    public NavigationIntegrityCategory getNICp() {
        return NICp;
    }

    public SourceIntegrityLevel getSIL() {
        return SIL;
    }

    public Angle getHorizontalSource() {
        return horizontalSource;
    }

    public GeometricVerticalAccuracy getGva() {
        return gva;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return String.format(
            "AircraftOperationalStatusVersion1Airborne\n" +
            "Version: %s\n" +
            "Capability: %s\n" +
            "Operational Mode: %s\n" +
            "Navigation Integrity Category: %s\n" +
            "Source Integrity Level: %s\n" +
            "Source Integrity Level Supplement: %s\n" +
            "Barometric Altitude Integrity Code: %s\n" +
            "Horizontal Source: %s",
            "Geometric Vertical Accuracy: %s",
            version.name(),
            capability.toString(),
            operationalMode.toString(),
            NICp.toString(),
            SIL.toString(),
            SILsupp.toString(),
            NICbaro.name(),
            horizontalSource.name(),
            gva.toString()
        );
    }
}
