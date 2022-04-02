package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.AircraftCategory;
import aero.t2s.modes.decoder.Common;

public class AircraftIdentification extends ExtendedSquitter {
    private String acid;
    private int aircraftEmitterCategory;

    public AircraftIdentification(short[] data) {
        super(data);
    }

    @Override
    public AircraftIdentification decode() {
        acid = Common.charToString(data[5] >>> 2) +
            Common.charToString(((data[5] & 0x3) << 4) | (data[6] >>> 4)) +
            Common.charToString(((data[6] & 0xF) << 2) | (data[7] >>> 6)) +
            Common.charToString(data[7] & 0x3F) +
            Common.charToString(data[8] >>> 2) +
            Common.charToString(((data[8] & 0x3) << 4) | (data[9] >>> 4)) +
            Common.charToString(((data[9] & 0xF) << 2) | (data[10] >>> 6)) +
            Common.charToString(data[10] & 0x3F);
        acid = acid.replace("_", "");
        aircraftEmitterCategory = data[4] >> 4;

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setCallsign(acid);

        track.register08().update(acid, determineAircraftCategory());
    }

    private AircraftCategory determineAircraftCategory() {
        switch (typeCode) {
            case 4:
                switch (aircraftEmitterCategory) {
                    case 0: return AircraftCategory.NO_ADS_B_EMITTER;
                    case 1: return AircraftCategory.LIGHT;
                    case 2: return AircraftCategory.SMALL;
                    case 3: return AircraftCategory.LARGE;
                    case 4: return AircraftCategory.HIGH_VORTEX_LARGE;
                    case 5: return AircraftCategory.HEAVY;
                    case 6: return AircraftCategory.HIGH_PERFORMANCE;
                    case 7: return AircraftCategory.ROTORCRAFT;
                    default: return AircraftCategory.UNKNOWN;
                }
            case 3:
                switch (aircraftEmitterCategory) {
                    case 0: return AircraftCategory.NO_ADS_B_EMITTER;
                    case 1: return AircraftCategory.GLIDER;
                    case 2: return AircraftCategory.LIGHTER_THAN_AIR;
                    case 3: return AircraftCategory.SKYDIVER;
                    case 4: return AircraftCategory.ULTRALIGHT;
                    case 5: return AircraftCategory.RESERVED;
                    case 6: return AircraftCategory.UNMANNED_AERIAL_VEHICLE;
                    case 7: return AircraftCategory.SPACE;
                    default: return AircraftCategory.UNKNOWN;
                }
            case 2:
                switch (aircraftEmitterCategory) {
                    case 0: return AircraftCategory.NO_ADS_B_EMITTER;
                    case 1: return AircraftCategory.SURFACE_VEHICLE_EMERGENCY;
                    case 2: return AircraftCategory.SURFACE_VEHICLE_SERVICE;
                    case 3: return AircraftCategory.POINT_OBSTACLE;
                    case 4: return AircraftCategory.CLUSTER_OBSTACLE;
                    case 5: return AircraftCategory.LINE_OBSTACLE;
                    case 6: return AircraftCategory.RESERVED;
                    case 7: return AircraftCategory.RESERVED;
                    default: return AircraftCategory.UNKNOWN;
                }
            default:
                return AircraftCategory.UNKNOWN;
        }
    }

    public String getAcid() {
        return acid;
    }
}
