package aero.t2s.modes.decoder;

import java.util.Arrays;

public class Common {
    private static final String chars = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ#####_###############0123456789######";
    private static final char[] haxChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final short[] CRC_POLYNOMIAL = {0xFF, 0xF4, 0x09};

    public static String icao(String hex) {
        if (hex.length() < 9) {
            return null;
        }

        return hex.substring(3, 9);
    }

    public static String charToString(int character) {
        if (character > chars.length()) {
            return "#";
        }

        return chars.substring(character, character + 1);
    }

    public static String toHexString(short[] data) {
        final int length = data.length;
        final char[] out = new char[length << 1];

        for (int i = 0, j = 0; i < length; i++) {
            out[j++] = haxChars[(0xF0 & data[i]) >>> 4];
            out[j++] = haxChars[0x0F & data[i]];
        }
        return new String(out);
    }


    /**
     * @param short1 first short
     * @param short2 second short
     * @return short1 xor short2 (bitwise)
     */
    public static short xor(short short1, short short2) {
        return (short)(0xff&(short1^short2));
    }

    /**
     * @param array1 first array
     * @param array2 second array
     * @return array1 xor array2 (bitwise)
     */
    public static short[] xor(short[] array1, short[] array2) {
        assert(array1.length == array2.length);

        short[] res = new short[array1.length];
        for (int i=0; i<array1.length; ++i)
            res[i] = xor(array1[i], array2[i]);

        return res;
    }

    public static String getIcaoAddressFromParity(short[] data) {
        short[] payload = Arrays.copyOfRange(data, 0, data.length - 3);
        short[] parity = Arrays.copyOfRange(data, data.length - 3, data.length);

        return getIcaoAddress(payload, parity);
    }

    public static String getIcaoAddress(short[] data, short[] messageParity) {
        short[] calculatedParity = getParity(data);

        return Common.toHexString(xor(calculatedParity, messageParity));
    }

    public static short[] getParity(short[] data) {
        short[] parity = Arrays.copyOf(data, CRC_POLYNOMIAL.length);

        boolean invert;
        int index;
        int shift;

        for (int i = 0; i < data.length * 8; ++i) { // bit by bit
            invert = ((parity[0] & 0x80) != 0);

            parity[0] <<= 1;
            for (int b = 1; b < CRC_POLYNOMIAL.length; ++b) {
                parity[b - 1] |= (parity[b] >>> 7) & 0x1;
                parity[b] <<= 1;
            }

            index = ((CRC_POLYNOMIAL.length * 8) + i) / 8;
            shift = 7 - (i % 8);
            if (index < data.length) {
                parity[parity.length - 1] |= (data[index] >>> shift) & 0x1;
            }

            // xor
            if (invert) {
                for (int b = 0; b < CRC_POLYNOMIAL.length; ++b) {
                    parity[b] ^= CRC_POLYNOMIAL[b];
                }
            }
        }

        return Arrays.copyOf(parity, CRC_POLYNOMIAL.length);
    }

    public static boolean isNotValid(short[] data) {
        if(data.length < 3) {
            return false;  // fixes negative length bug where data is only 2 bytes (16bit Mode A/C replies, ACAS, ...)
        }
        short[] payload = Arrays.copyOfRange(data, 0, data.length - 3);
        short[] parity = Arrays.copyOfRange(data, data.length - 3, data.length);

        return areEqual(getParity(payload), parity);
    }

    public static boolean areEqual(short[] array1, short[] array2) {
        if (array1.length != array2.length)
            return false;

        for (int i=0; i<array1.length; ++i)
            if (array1[i] != array2[i])
                return false;

        return true;
    }

    public static boolean isFlightStatusAlert(int fs) {
        return fs >= 2 && fs <= 4;
    }

    public static boolean isFlightStatusSpi(int fs) {
        return fs == 4 || fs == 5;
    }

    public static int modeA(int encoded) {
        int c1 = (encoded >>> 12) & 0x1;
        int a1 = (encoded >>> 11) & 0x1;
        int c2 = (encoded >>> 10) & 0x1;
        int a2 = (encoded >>> 9) & 0x1;
        int c4 = (encoded >>> 8) & 0x1;
        int a4 = (encoded >>> 7) & 0x1;
        int x = (encoded >>> 6) & 0x1;
        int b1 = (encoded >>> 5) & 0x1;
        int d1 = (encoded >>> 4) & 0x1;
        int b2 = (encoded >>> 3) & 0x1;
        int d2 = (encoded >>> 2) & 0x1;
        int b4 = (encoded >>> 1) & 0x1;
        int d4 = encoded & 0x1;

        int n1 = ((a4 << 2) | (a2 << 1) | a1) * 1000;
        int n2 = ((b4 << 2) | (b2 << 1) | b1) * 100;
        int n3 = ((c4 << 2) | (c2 << 1) | c1) * 10;
        int n4 = ((d4 << 2) | (d2 << 1) | d1);

        return n1 + n2 + n3 + n4;
    }

    public static double tidr(int encodedDistance) {
        if (encodedDistance == 0) {
            return -1;
        }

        return (encodedDistance - 1.0) / 10.0;
    }

    public static int tidb(int encodedBearing) {
        if (encodedBearing == 0) {
            return -1;
        }

        return 6 * (encodedBearing - 1);
    }
}
