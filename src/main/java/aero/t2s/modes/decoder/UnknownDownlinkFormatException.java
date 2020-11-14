package aero.t2s.modes.decoder;

import aero.t2s.modes.DownlinkException;

public class UnknownDownlinkFormatException extends Exception implements DownlinkException {
    public UnknownDownlinkFormatException(int downlinkFormat, short[] data) {
        super(String.format("Unknown Mode S Packet: %d => %s", downlinkFormat, Common.toHexString(data)));
    }
}
