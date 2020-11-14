package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.DownlinkException;

import java.util.List;

public class MultipleBdsMatchesFoundException extends Throwable implements DownlinkException {
    private final List<Bds> matches;

    public MultipleBdsMatchesFoundException(List<Bds> matches) {
        super("Multiple BDS Matches found");
        this.matches = matches;
    }

    public List<Bds> getMatches() {
        return matches;
    }
}
