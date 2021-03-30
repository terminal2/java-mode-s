package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.decoder.df.df17.data.SurfaceCapability;
import aero.t2s.modes.decoder.df.df17.data.SurfaceOperationalMode;

public class AircraftOperationalStatusVersion1Surface extends AircraftOperationalStatusVersion1 {
    private int subType = 1;
    private Version version = Version.VERSION1;
    private Angle horizontalSource;
    private SurfaceCapability surfaceCapability;
    private LengthWidthCode lengthWidthCode;
    private SurfaceOperationalMode operationalMode;
    private SourceIntegrityLevel SIL;
    private SourceIntegrityLevelSupplement SILsupp;
    private NavigationIntegrityCategory NICp;

    public AircraftOperationalStatusVersion1Surface(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion1Surface decode() {
        surfaceCapability = new SurfaceCapability((data[5] << 4) | (data[6] & 0b11110000) >>> 4, version);
        lengthWidthCode = LengthWidthCode.from(data[6] & 0b00001111);
        operationalMode = new SurfaceOperationalMode((data[7] << 8) | data[8]);

        SIL = SourceIntegrityLevel.from((data[10] & 0b000110000) >>> 4);
        SILsupp = SourceIntegrityLevelSupplement.from((data[10] & 0b00000010) >>> 1);

        int NICsupp = (data[9] & 0b00010000) >>> 4;
        int NACp = (data[9] & 0b00001111);
        NICp = NavigationIntegrityCategory.surface(NACp, NICsupp);

        if ((data[10] & 0b00001000) != 0) {
            if ((data[10] & 0b00000100) != 0)  {
                horizontalSource = Angle.TRUE_TRACK;
            } else {
                horizontalSource = Angle.MAGNETIC_TRACK;
            }
        } else {
            if ((data[10] & 0b00000100) != 0)  {
                horizontalSource = Angle.TRUE_HEADING;
            } else {
                horizontalSource = Angle.MAGNETIC_HEADING;
            }
        }

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setVersion(Version.VERSION1);
    }

    public int getSubType() {
        return subType;
    }

    public Version getVersion() {
        return version;
    }

    public Angle getHorizontalSource() {
        return horizontalSource;
    }

    public SurfaceCapability getSurfaceCapability() {
        return surfaceCapability;
    }

    public LengthWidthCode getLengthWidthCode() {
        return lengthWidthCode;
    }

    public SurfaceOperationalMode getOperationalMode() {
        return operationalMode;
    }

    public SourceIntegrityLevel getSIL() {
        return SIL;
    }

    public SourceIntegrityLevelSupplement getSILsupp() {
        return SILsupp;
    }

    public NavigationIntegrityCategory getNICp() {
        return NICp;
    }

    @Override
    public String toString() {
        return String.format(
            "AircraftOperationalStatusVersion1Airborne\n" +
            "Version: %s\n" +
            "Capability: %s\n" +
            "Operational Mode: %s\n" +
            "Length Width Code: %s\n" +
            "Navigation Integrity Category: %s\n" +
            "Source Integrity Level: %s\n" +
            "Source Integrity Level Supplement: %s\n" +
            "Horizontal Source: %s",
            version.name(),
            surfaceCapability.toString(),
            operationalMode.toString(),
            lengthWidthCode.toString(),
            NICp.toString(),
            SIL.toString(),
            SILsupp.name(),
            horizontalSource.name()
        );
    }
}
