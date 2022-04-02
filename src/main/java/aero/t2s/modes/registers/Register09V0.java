package aero.t2s.modes.registers;

public class Register09V0 extends Register09 {
    /**
     * The IFR capability flag shall be a 1-bit (bit 10) subfield in the subtypes 1, 2, 3 and 4 airborne velocity messages.
     * IFR=1 shall signify that the transmitting aircraft has a capability for applications requiring ADS-B equipage class A1 or above.
     * Otherwise, IFR shall be set to 0.
     */
    private boolean ifrCapability;

    public boolean isIfrCapability() {
        return ifrCapability;
    }

    public Register09V0 setIfrCapability(boolean ifrCapability) {
        this.ifrCapability = ifrCapability;
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + "\nRegister09V0{\n" +
            "ifrCapability=" + ifrCapability +
            "\n}";
    }
}
