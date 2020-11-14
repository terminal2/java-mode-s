package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Acas;
import aero.t2s.modes.Altitude;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.*;
import aero.t2s.modes.decoder.AltitudeEncoding;

public class DF16 extends DownlinkFormat {

    private VerticalStatus verticalStatus;
    private CrossLinkCapability crossLinkCapability;
    private AcasSensitivity sensitivity;
    private AcasReplyInformation replyInformation;
    private Altitude altitude;
    private ResolutionAdvisory resolutionAdvisory;
    private boolean multipleThreats;

    private boolean RANotPassBelow;
    private boolean RANotPassAbove;
    private boolean RANotTurnLeft;
    private boolean RANotTurnRight;

    public DF16(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF16 decode() {
        verticalStatus = VerticalStatus.from((data[0] >>> 2) & 0x1);
        crossLinkCapability = CrossLinkCapability.from((data[0] >>> 1) & 0x1);
        sensitivity = AcasSensitivity.from(data[1] >>> 5);
        replyInformation = AcasReplyInformation.from(((data[1] & 0x7) << 1) | ((data[2] >> 7) & 0x1));
        altitude = AltitudeEncoding.decode((((data[2] << 8) | data[3])) & 0x1FFF);

        int vds1 = data[4] >>> 4;
        int vds2 = data[4] & 0xF;

        int ara = (data[5] << 5) | (data[6] >>> 3);
        int rac = ((data[5] & 0x3) << 2) | data[6] >>> 6;

        int rat = (data[6] >>> 5) & 0x1;
        multipleThreats = ((data[6] >>> 4) & 0x1) == 1;

        if (vds1 == 3 && vds2 == 0) {
            resolutionAdvisory = new ResolutionAdvisory(ara);

            RANotPassBelow = rac >>> 3 == 1;
            RANotPassAbove = ((rac >>> 2) & 0x1) == 1;
            RANotTurnLeft = ((rac >>> 1) & 0x1) == 1;
            RANotTurnLeft = (rac & 0x1) == 1;
        }

        if (rat == 1 || resolutionAdvisory == null) {
            resolutionAdvisory = new ResolutionAdvisory();
            RANotPassBelow = false;
            RANotPassAbove = false;
            RANotTurnLeft = false;
            RANotTurnRight = false;
            multipleThreats = false;
        }

        return this;
    }

    @Override
    public void apply(Track track) {
        Acas acas = track.getAcas();
        acas.setVerticalStatus(verticalStatus);
        acas.setCrossLinkCapability(crossLinkCapability);
        acas.setSensitivity(sensitivity);
        acas.setReplyInformation(replyInformation);
        acas.setAltitude(altitude);
        acas.getResolutionAdvisory().update(resolutionAdvisory);
        acas.setRANotPassBelow(RANotPassBelow);
        acas.setRANotPassAbove(RANotPassAbove);
        acas.setRANotTurnLeft(RANotTurnLeft);
        acas.setRANotTurnRight(RANotTurnRight);
        acas.setMultipleThreats(multipleThreats);
    }

    public VerticalStatus getVerticalStatus() {
        return verticalStatus;
    }

    public CrossLinkCapability getCrossLinkCapability() {
        return crossLinkCapability;
    }

    public AcasSensitivity getSensitivity() {
        return sensitivity;
    }

    public AcasReplyInformation getReplyInformation() {
        return replyInformation;
    }

    public Altitude getAltitude() {
        return altitude;
    }

    public ResolutionAdvisory getResolutionAdvisory() {
        return resolutionAdvisory;
    }

    public boolean isMultipleThreats() {
        return multipleThreats;
    }

    public boolean isRANotPassBelow() {
        return RANotPassBelow;
    }

    public boolean isRANotPassAbove() {
        return RANotPassAbove;
    }

    public boolean isRANotTurnLeft() {
        return RANotTurnLeft;
    }

    public boolean isRANotTurnRight() {
        return RANotTurnRight;
    }
}
