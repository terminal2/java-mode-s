package aero.t2s.modes.decoder.df;

import aero.t2s.modes.Acas;
import aero.t2s.modes.Track;
import aero.t2s.modes.constants.AcasReplyInformation;
import aero.t2s.modes.constants.AcasSensitivity;
import aero.t2s.modes.constants.CrossLinkCapability;
import aero.t2s.modes.constants.VerticalStatus;
import aero.t2s.modes.decoder.AltitudeEncoding;
import aero.t2s.modes.decoder.Decoder;

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
    public DF0(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Track decode(short[] data, int downlinkFormat) {
        Track track = getDecoder().getTrack(getIcaoAddressFromParity(data));
        Acas acas = track.getAcas();

        acas.setVerticalStatus(VerticalStatus.from((data[0] >>> 2) & 0x1));
        acas.setCrossLinkCapability(CrossLinkCapability.from((data[0] >>> 1) & 0x1));
        acas.setSensitivity(AcasSensitivity.from(data[1] >>> 5));
        acas.setReplyInformation(AcasReplyInformation.from(((data[1] & 0x7) << 1) | ((data[2] >> 7) & 0x1)));
        acas.setAltitude(AltitudeEncoding.decode((((data[2] << 8) | data[3])) & 0x1FFF));
        track.setAltitude(AltitudeEncoding.decode((((data[2] << 8) | data[3])) & 0x1FFF));

        return track;
    }
}
