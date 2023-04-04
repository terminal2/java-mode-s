package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.decoder.df.df17.data.SurfaceCapability;
import aero.t2s.modes.decoder.df.df17.data.SurfaceOperationalMode;

public class AircraftOperationalStatusVersion2Surface extends AircraftOperationalStatusVersion2 {
    private SurfaceCapability surfaceCapability;
    private SurfaceOperationalMode operationalMode;
    private LengthWidthCode lengthWidthCode;
    private NavigationIntegrityCategory NICp;
    private Angle horizontalSource;
    private SourceIntegrityLevelSupplement SILsupp;
    private SourceIntegrityLevel SIL;
    private Version verison;

    public AircraftOperationalStatusVersion2Surface(short[] data) {
        super(data);
    }

    @Override
    public AircraftOperationalStatusVersion2Surface decode() {
        version = Version.VERSION2;
        surfaceCapability = new SurfaceCapability((data[5] << 4) | (data[6] & 0b11110000) >>> 4, version);
        lengthWidthCode = LengthWidthCode.from(data[6] & 0b00001111);
        operationalMode = new SurfaceOperationalMode((data[7] << 8) | data[8]);

        int NICsuppA = (data[9] & 0b00010000) >>> 4;
        int NACp = (data[9] & 0b00001111);
        int GVA = (data[10] & 0b11000000) >>> 6;
        int SIL = (data[10] & 0b00110000) >>> 4;

        int SILsupp = (data[10] & 0b00000010) >>> 1;

        this.SIL = SourceIntegrityLevel.from(SIL);
        this.SILsupp = SourceIntegrityLevelSupplement.from(SILsupp);
        this.NICp = NavigationIntegrityCategory.surface(NACp, NICsuppA);

        if ((data[10] & 0b00001000) != 0) {
            if ((data[10] & 0b00000100) != 0)  {
                horizontalSource = Angle.MAGNETIC_TRACK;
            } else {
                horizontalSource = Angle.TRUE_TRACK;
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
        track.setVersion(Version.VERSION2);
        track.setHorizontalSource(horizontalSource);
    }

    public SurfaceCapability getSurfaceCapability() {
        return surfaceCapability;
    }

    public SurfaceOperationalMode getOperationalMode() {
        return operationalMode;
    }

    public LengthWidthCode getLengthWidthCode() {
        return lengthWidthCode;
    }

    public NavigationIntegrityCategory getNICp() {
        return NICp;
    }

    public Angle getHorizontalSource() {
        return horizontalSource;
    }

    public SourceIntegrityLevelSupplement getSILsupp() {
        return SILsupp;
    }

    public SourceIntegrityLevel getSIL() {
        return SIL;
    }

    public Version getVerison() {
        return verison;
    }
}
