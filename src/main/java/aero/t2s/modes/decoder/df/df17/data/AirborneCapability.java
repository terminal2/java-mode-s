package aero.t2s.modes.decoder.df.df17.data;

import aero.t2s.modes.constants.TargetChangeReportCapability;
import aero.t2s.modes.constants.Version;

public class AirborneCapability {
    private boolean acasOperational;
    private boolean receive1090ES;
    private boolean cockpitDisplayOfTraffic;
    private boolean sendAirReferencedVelocityReport;
    private boolean sendTargetSateReport;
    private TargetChangeReportCapability targetChangeReportCapability;
    private boolean uatReceive;
    private Version version;

    public AirborneCapability(int data, Version version) {
        this.version = version;
        acasOperational = (data & 0b0010000000000000) != 0;
        receive1090ES = (data & 0b0001000000000000) != 0;
        cockpitDisplayOfTraffic = (data & 0b0001000000000000) != 0;
        sendAirReferencedVelocityReport = (data & 0b0000001000000000) != 0;
        sendTargetSateReport = (data & 0b0000000100000000) != 0;
        targetChangeReportCapability = TargetChangeReportCapability.from((data & 0b0000000011000000) >>> 6);
        uatReceive = (data & 0b0000000000100000) != 0;
    }

    public boolean isAcasOperational() {
        return acasOperational;
    }

    public boolean isReceive1090ES() {
        return receive1090ES;
    }

    public boolean isSendAirReferencedVelocityReport() {
        return sendAirReferencedVelocityReport;
    }

    public boolean isSendTargetSateReport() {
        return sendTargetSateReport;
    }

    public TargetChangeReportCapability getTargetChangeReportCapability() {
        return targetChangeReportCapability;
    }

    public boolean isUatReceive() {
        return uatReceive;
    }

    public boolean isCockpitDisplayOfTraffic() {
        return cockpitDisplayOfTraffic;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return String.format(
            "Airborne Capability\n" +
            "\tVersion: %s\n" +
            "\tACAS Operational: %b\n" +
            "\tReceive 1090ES: %b\n" +
            "\tCockpit Display Traffic: %b\n" +
            "\tSends Air referenced Velocity Report: %b\n" +
            "\tSends Target State Report: %b" +
            "\tTarget Change Report Capability: %s\n" +
            "\tUAT receive: %b",
            version.name(),
            acasOperational,
            receive1090ES,
            cockpitDisplayOfTraffic,
            sendAirReferencedVelocityReport,
            sendTargetSateReport,
            targetChangeReportCapability.name(),
            uatReceive
        );
    }
}
