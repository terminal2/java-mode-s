package aero.t2s.modes.decoder.df.df17.data;

import aero.t2s.modes.constants.NavigationUncertaintyCategory;
import aero.t2s.modes.constants.Version;

public class SurfaceCapability {
    private boolean receive1090ES;
    private boolean lowB2Power;
    private boolean uatReceive;
    private boolean cockpitDisplayOfTraffic;
    private boolean positionOffsetApplied;
    private NavigationUncertaintyCategory NACv;
    private short NICsuppC;
    private Version version;

    public SurfaceCapability(int data, Version version) {
        this.version = version;
        lowB2Power = (data & 0b000000100000) != 0;

        if (version == Version.VERSION1) {
            positionOffsetApplied = (data & 0b001000000000) != 0;
            cockpitDisplayOfTraffic = (data & 0b000100000000) != 0;
        } else {
            receive1090ES = (data & 0b000100000000) != 0;
            uatReceive = (data & 0b000000010000) != 0;
            NACv = NavigationUncertaintyCategory.from((data & 0b000000001110) >>> 1);
            NICsuppC = (short) (data & 0b000000000001);
        }
    }

    public boolean isReceive1090ES() {
        return receive1090ES;
    }

    public boolean isLowB2Power() {
        return lowB2Power;
    }

    public boolean isUatReceive() {
        return uatReceive;
    }

    public NavigationUncertaintyCategory getNACv() {
        return NACv;
    }

    public short getNICsuppC() {
        return NICsuppC;
    }

    public Version getVersion() {
        return version;
    }

    public boolean isCockpitDisplayOfTraffic() {
        return cockpitDisplayOfTraffic;
    }

    public boolean isPositionOffsetApplied() {
        return positionOffsetApplied;
    }

    @Override
    public String toString() {
        if (version == Version.VERSION1) {
            return String.format(
                "Airborne Capability\n" +
                "\tVersion: %s\n" +
                "\tLow B2 Power: %b\n" +
                "\tPosition Offset Applied: %b\n" +
                version.name(),
                lowB2Power,
                positionOffsetApplied,
                cockpitDisplayOfTraffic
            );
        }

        return String.format(
            "Airborne Capability\n" +
            "\tVersion: %s\n" +
            "\tLow B2 Power: %b\n" +
            "\tReceive 1090ES: %b\n" +
            "\tNavigation Uncertainty Velocity: %s\n" +
            "\tNavigation Uncertainty Supplement: %d\n" +
            "\tUAT receive: %b",
            version.name(),
            lowB2Power,
            receive1090ES,
            NACv.toString(),
            NICsuppC,
            uatReceive
        );
    }
}
