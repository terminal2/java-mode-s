package aero.t2s.modes;

import aero.t2s.modes.decoder.df.bds.Bds17;

public class CapabilityReport {
    boolean isAvailable = false;
    boolean bds05 = true;
    boolean bds06 = true;
    boolean bds07 = true;
    boolean bds08 = true;
    boolean bds09 = true;
    boolean bds0A = false;
    boolean bds20 = true;
    boolean bds21 = true;
    boolean bds40 = true;
    boolean bds41 = false;
    boolean bds42 = false;
    boolean bds43 = false;
    boolean bds44 = false;
    boolean bds45 = false;
    boolean bds48 = false;
    boolean bds50 = false;
    boolean bds51 = false;
    boolean bds52 = false;
    boolean bds53 = false;
    boolean bds54 = false;
    boolean bds55 = false;
    boolean bds56 = false;
    boolean bds5F = false;
    boolean bds60 = false;

    public void update(short[] data) {
        isAvailable = true;
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

    public void update(Bds17 bds17) {
        this.bds05 = bds17.isBds05();
        this.bds06 = bds17.isBds06();
        this.bds07 = bds17.isBds07();
        this.bds08 = bds17.isBds08();
        this.bds09 = bds17.isBds09();
        this.bds0A = bds17.isBds0A();
        this.bds20 = bds17.isBds20();
        this.bds21 = bds17.isBds21();

        this.bds40 = bds17.isBds40();
        this.bds41 = bds17.isBds41();
        this.bds42 = bds17.isBds42();
        this.bds43 = bds17.isBds43();
        this.bds44 = bds17.isBds44();
        this.bds45 = bds17.isBds45();
        this.bds48 = bds17.isBds48();
        this.bds50 = bds17.isBds50();

        this.bds51 = bds17.isBds51();
        this.bds52 = bds17.isBds52();
        this.bds53 = bds17.isBds53();
        this.bds54 = bds17.isBds54();
        this.bds55 = bds17.isBds55();
        this.bds56 = bds17.isBds56();
        this.bds5F = bds17.isBds5F();
        this.bds60 = bds17.isBds60();
    }

    public boolean isAvailable() {
        return isAvailable;
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

    public void all() {
        bds05 = true;
        bds06 = true;
        bds07 = true;
        bds08 = true;
        bds09 = true;
        bds0A = true;
        bds20 = true;
        bds21 = true;
        bds40 = true;
        bds41 = true;
        bds42 = true;
        bds43 = true;
        bds44 = true;
        bds45 = true;
        bds48 = true;
        bds50 = true;
        bds51 = true;
        bds52 = true;
        bds53 = true;
        bds54 = true;
        bds55 = true;
        bds56 = true;
        bds5F = true;
        bds60 = true;
    }
}
