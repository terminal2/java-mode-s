package aero.t2s.modes;

import aero.t2s.modes.constants.*;

public class Acas {
    private VerticalStatus verticalStatus = VerticalStatus.AIRBORNE;
    private CrossLinkCapability crossLinkCapability = CrossLinkCapability.UNSUPPORTED;
    private AcasSensitivity sensitivity = AcasSensitivity.INOP;
    private AcasReplyInformation replyInformation = AcasReplyInformation.NO_OPERATING_ACAS;
    private Altitude altitude = new Altitude();
    private ResolutionAdvisory resolutionAdvisory = new ResolutionAdvisory();
    private boolean multipleThreats;
    private boolean RANotPassBelow;
    private boolean RANotPassAbove;
    private boolean RANotTurnLeft;
    private boolean RANotTurnRight;
    private boolean active;
    private ThreatType threatType = ThreatType.NO_ID;
    private String targetModeS;
    private double targetAltitude;
    private double targetRange;
    private int targetBearing;

    public void setVerticalStatus(VerticalStatus verticalStatus) {
        this.verticalStatus = verticalStatus;
    }

    public VerticalStatus getVerticalStatus() {
        return verticalStatus;
    }

    public void setCrossLinkCapability(CrossLinkCapability crossLinkCapability) {
        this.crossLinkCapability = crossLinkCapability;
    }

    public CrossLinkCapability getCrossLinkCapability() {
        return crossLinkCapability;
    }

    public void setSensitivity(AcasSensitivity sensitivity) {
        this.sensitivity = sensitivity;
    }

    public AcasSensitivity getSensitivity() {
        return sensitivity;
    }

    public void setReplyInformation(AcasReplyInformation replyInformation) {
        this.replyInformation = replyInformation;
    }

    public AcasReplyInformation getReplyInformation() {
        return replyInformation;
    }

    public void setAltitude(Altitude altitude) {
        this.altitude = altitude;
    }

    public Altitude getAltitude() {
        return altitude;
    }

    public void setMultipleThreats(boolean multipleThreats) {
        this.multipleThreats = multipleThreats;
    }

    public boolean getMultipleThreats() {
        return multipleThreats;
    }

    public void setRANotPassBelow(boolean RANotPassBelow) {
        this.RANotPassBelow = RANotPassBelow;
    }

    public boolean getRANotPassBelow() {
        return RANotPassBelow;
    }

    public void setRANotPassAbove(boolean raPassAbove) {
        this.RANotPassAbove = raPassAbove;
    }

    public boolean getRANotPassAbove() {
        return RANotPassAbove;
    }

    public void setRANotTurnLeft(boolean RANotTurnLeft) {
        this.RANotTurnLeft = RANotTurnLeft;
    }

    public boolean getRANotTurnLeft() {
        return RANotTurnLeft;
    }

    public void setRANotTurnRight(boolean RANotTurnRight) {
        this.RANotTurnRight = RANotTurnRight;
    }

    public boolean getRANotTurnRight() {
        return RANotTurnRight;
    }

    public ResolutionAdvisory getResolutionAdvisory() {
        return resolutionAdvisory;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void setThreatType(int threatType) {
        this.threatType = ThreatType.values()[threatType];
    }

    public ThreatType getThreatType() {
        return threatType;
    }

    public void setTargetModeS(String targetModeS) {
        this.targetModeS = targetModeS;
    }

    public String getTargetModeS() {
        return targetModeS;
    }

    public void setTargetAltitude(double targetAltitude) {
        this.targetAltitude = targetAltitude;
    }

    public double getTargetAltitude() {
        return targetAltitude;
    }

    public void setTargetRange(double targetRange) {
        this.targetRange = targetRange;
    }

    public double getTargetRange() {
        return targetRange;
    }

    public void setTargetBearing(int targetBearing) {
        this.targetBearing = targetBearing;
    }

    public int getTargetBearing() {
        return targetBearing;
    }

    public enum ThreatType {
        NO_ID,
        MODES,
        ALT_BRG_DIST,
        NOT_ASSIGNED,
        ;
    }
}
