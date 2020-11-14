package aero.t2s.modes.decoder.df.bds;

import aero.t2s.modes.Track;

public class Bds17 extends Bds {
    private boolean bds05;
    private boolean bds06;
    private boolean bds07;
    private boolean bds08;
    private boolean bds09;
    private boolean bds0A;
    private boolean bds20;
    private boolean bds21;
    private boolean bds40;
    private boolean bds41;
    private boolean bds42;
    private boolean bds43;
    private boolean bds44;
    private boolean bds45;
    private boolean bds48;
    private boolean bds50;
    private boolean bds51;
    private boolean bds52;
    private boolean bds53;
    private boolean bds54;
    private boolean bds55;
    private boolean bds56;
    private boolean bds5F;
    private boolean bds60;

    public Bds17(short[] data) {
        super(data);

        // 2,0 Aircraft identification shall always be present
        if ((data[4] & 0b00000010) == 0) {
            invalidate();
            return;
        }

        // F,1 Mil reserved use
        if (data[7] != 0 || data[8] != 0 || data[9] != 0 || data[10] != 0) {
            invalidate();
            return;
        }

        bds05 = (data[4] & 0b10000000) != 0;
        bds06 = (data[4] & 0b01000000) != 0;
        bds07 = (data[4] & 0b00100000) != 0;
        bds08 = (data[4] & 0b00010000) != 0;
        bds09 = (data[4] & 0b00001000) != 0;
        bds0A = (data[4] & 0b00000100) != 0;
        bds20 = (data[4] & 0b00000010) != 0;
        bds21 = (data[4] & 0b00000001) != 0;

        bds40 = (data[5] & 0b10000000) != 0;
        bds41 = (data[5] & 0b01000000) != 0;
        bds42 = (data[5] & 0b00100000) != 0;
        bds43 = (data[5] & 0b00010000) != 0;
        bds44 = (data[5] & 0b00001000) != 0;
        bds45 = (data[5] & 0b00000100) != 0;
        bds48 = (data[5] & 0b00000010) != 0;
        bds50 = (data[5] & 0b00000001) != 0;

        bds51 = (data[6] & 0b10000000) != 0;
        bds52 = (data[6] & 0b01000000) != 0;
        bds53 = (data[6] & 0b00100000) != 0;
        bds54 = (data[6] & 0b00010000) != 0;
        bds55 = (data[6] & 0b00001000) != 0;
        bds56 = (data[6] & 0b00000100) != 0;
        bds5F = (data[6] & 0b00000010) != 0;
        bds60 = (data[6] & 0b00000001) != 0;
    }

    @Override
    public void apply(Track track) {
        track.getCapabilityReport().update(this);
    }

    public boolean isBds05() {
        return bds05;
    }

    public boolean isBds06() {
        return bds06;
    }

    public boolean isBds07() {
        return bds07;
    }

    public boolean isBds08() {
        return bds08;
    }

    public boolean isBds09() {
        return bds09;
    }

    public boolean isBds0A() {
        return bds0A;
    }

    public boolean isBds20() {
        return bds20;
    }

    public boolean isBds21() {
        return bds21;
    }

    public boolean isBds40() {
        return bds40;
    }

    public boolean isBds41() {
        return bds41;
    }

    public boolean isBds42() {
        return bds42;
    }

    public boolean isBds43() {
        return bds43;
    }

    public boolean isBds44() {
        return bds44;
    }

    public boolean isBds45() {
        return bds45;
    }

    public boolean isBds48() {
        return bds48;
    }

    public boolean isBds50() {
        return bds50;
    }

    public boolean isBds51() {
        return bds51;
    }

    public boolean isBds52() {
        return bds52;
    }

    public boolean isBds53() {
        return bds53;
    }

    public boolean isBds54() {
        return bds54;
    }

    public boolean isBds55() {
        return bds55;
    }

    public boolean isBds56() {
        return bds56;
    }

    public boolean isBds5F() {
        return bds5F;
    }

    public boolean isBds60() {
        return bds60;
    }
}
