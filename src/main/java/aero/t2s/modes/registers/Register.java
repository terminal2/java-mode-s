package aero.t2s.modes.registers;

abstract public class Register {
    private boolean valid = false;

    public void validate() {
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public abstract String toString();
}
