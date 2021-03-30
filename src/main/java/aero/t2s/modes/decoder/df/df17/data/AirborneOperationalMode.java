package aero.t2s.modes.decoder.df.df17.data;

import aero.t2s.modes.constants.AcasState;
import aero.t2s.modes.constants.SourceIntegrityLevel;

public class AirborneOperationalMode {
    private AcasState acasRA;
    private boolean acasIdent;
    private boolean singleAntennaFlag;
    private SourceIntegrityLevel systemDesignAssurance;

    public AirborneOperationalMode(int data) {
        acasRA = (data & 0b0010000000000000) != 0 ? AcasState.RA_ACTIVE : AcasState.RA_NOT_ACTIVE;
        acasIdent = (data & 0b0001000000000000) != 0;
        singleAntennaFlag = (data & 0b0000010000000000) != 0;
        systemDesignAssurance = SourceIntegrityLevel.from((data & 0b0000001100000000) >>> 8);
    }

    public AcasState getAcasRA() {
        return acasRA;
    }

    public boolean isAcasIdent() {
        return acasIdent;
    }

    public boolean isSingleAntennaFlag() {
        return singleAntennaFlag;
    }

    public SourceIntegrityLevel getSystemDesignAssurance() {
        return systemDesignAssurance;
    }

    @Override
    public String toString() {
        String out = acasRA.name() + " ";

        if (acasIdent) {
            out += "IDENT ";
        }

        if (singleAntennaFlag) {
            out += "SINGLE ";
        }

        out += "(" + systemDesignAssurance.toString() + ")";

        return out;
    }
}
