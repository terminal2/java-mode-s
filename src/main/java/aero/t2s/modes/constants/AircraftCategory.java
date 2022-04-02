package aero.t2s.modes.constants;

public enum AircraftCategory {
    // Category A
    NO_ADS_B_EMITTER,
    LIGHT,
    SMALL,
    LARGE,
    HIGH_VORTEX_LARGE,
    HEAVY,
    HIGH_PERFORMANCE,
    ROTORCRAFT,

    // Category B
    GLIDER,
    LIGHTER_THAN_AIR,
    SKYDIVER,
    ULTRALIGHT,
    UNMANNED_AERIAL_VEHICLE,
    SPACE,

    // Category C
    SURFACE_VEHICLE_EMERGENCY,
    SURFACE_VEHICLE_SERVICE,
    POINT_OBSTACLE,
    CLUSTER_OBSTACLE,
    LINE_OBSTACLE,

    //
    RESERVED,
    UNKNOWN,
    ;
}
