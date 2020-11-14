package aero.t2s.modes;

public class NotImplementedException extends RuntimeException implements DownlinkException {
    public NotImplementedException(String message) {
        super(message);
    }
}
