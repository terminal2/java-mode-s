package aero.t2s.modes.decoder.df.df17;

import aero.t2s.modes.DownlinkException;

public class InvalidExtendedSquitterTypeCodeException extends RuntimeException implements DownlinkException {
    public InvalidExtendedSquitterTypeCodeException(int typeCode) {
        super("Mode-S: Type " + typeCode + " is unknown");
    }
}
