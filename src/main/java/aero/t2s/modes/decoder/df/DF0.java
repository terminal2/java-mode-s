package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Acas;
import aero.t2s.modes.Altitude;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.AcasReplyInformation;
import aero.t2s.modes.constants.AcasSensitivity;
import aero.t2s.modes.constants.CrossLinkCapability;
import aero.t2s.modes.constants.VerticalStatus;
import aero.t2s.modes.decoder.AltitudeEncoding;

/**
 * Short Air-Air Surveillance
 *
 * <pre>
 * LSB |1----|6-|7-|-|9--|--|14--|--|20-----------|33----------------------|
 *     | DF  |VS|CC|-| SL|  | RI |  |     AC      |         AP             |
 * MSB |----5|-6|-7|-|-11|--|--17|--|-----------32|----------------------56|
 *
 * DF Downlink format - 5 bits
 * VS Vertical status - 1 bit
 * CS Cross-link capability - 1 bit
 * spare - 1 bit
 * SL Sensitivity level, ACAS - 3 bits
 * spare - 2 bits
 * AC Altitude code - 12 bits
 * AP Address/Parity - 24 bits
 * </pre>
 *
 * <ul>
 *     <li>DF Downlink Format {@link DownlinkFormat}</li>
 *     <li>Vertical Status {@link VerticalStatus#from(int)} for details</li>
 *     <li>Cross-link capability {@link CrossLinkCapability}</li>
 *     <li>Reply information, air-air {@link AcasReplyInformation#from(int)}</li>
 *     <li>Sensitivity Level {@link AcasSensitivity#from(int)}</li>
 *     <li>Altitude code {@link AltitudeEncoding#decode(int)}</li>
 * </ul>
 */
public class DF0 extends DownlinkFormat {
    private VerticalStatus verticalStatus;
    private CrossLinkCapability crossLinkCapability;
    private AcasSensitivity sensitivity;
    private AcasReplyInformation replyInformation;
    private Altitude altitude;

    public DF0(short[] data) {
        super(data, IcaoAddress.FROM_PARITY);
    }

    @Override
    public DF0 decode() {
        verticalStatus = VerticalStatus.from((data[0] >>> 2) & 0x1);
        crossLinkCapability = CrossLinkCapability.from((data[0] >>> 1) & 0x1);
        sensitivity = AcasSensitivity.from(data[1] >>> 5);
        replyInformation = AcasReplyInformation.from(((data[1] & 0x7) << 1) | ((data[2] >> 7) & 0x1));
        altitude = AltitudeEncoding.decode((((data[2] << 8) | data[3])) & 0x1FFF);

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

        track.setAltitude(altitude);
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

    @Override
    public String toString() {
        return String.format(
            "DF0: Short Air-to-Air (%s):\n" +
            "-----------------\n\n" +
            "Vertical Status: %s\n" +
            "Cross link capability Status: %s\n" +
            "ACAS Sensitive: %s\n" +
            "ACAS Reply Info: %s\n" +
            "Altitude: %s",
            getIcao(),
            getVerticalStatus().name(),
            getCrossLinkCapability().name(),
            getSensitivity().name(),
            getReplyInformation().toString(),
            getAltitude()
        );
    }
}
