package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.Track;
import aero.t2s.modes.constants.EmergencyState;
import aero.t2s.modes.decoder.Common;

public class AircraftStatusMessageEmergency extends AircraftStatusMessage {
    private int subType = 1;
    private EmergencyState emergencyState;
    private int modeA;

    public AircraftStatusMessageEmergency(short[] data) {
        super(data);
    }

    @Override
    public AircraftStatusMessage decode() {

        emergencyState = EmergencyState.from(data[5] >>> 5);
        modeA = Common.modeA((data[5] & 0x1F) << 8 | data[6]);

        return this;
    }

    @Override
    public void apply(Track track) {
        track.setEmergencyState(emergencyState);
        if (modeA != 0) {
            track.setModeA(modeA);
        }
    }

    public int getSubType() {
        return subType;
    }

    public EmergencyState getEmergencyState() {
        return emergencyState;
    }

    public int getModeA() {
        return modeA;
    }
}
