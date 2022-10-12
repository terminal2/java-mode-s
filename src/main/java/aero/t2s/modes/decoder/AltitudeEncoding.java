package aero.t2s.modes.decoder;

import aero.t2s.modes.Altitude;

public class AltitudeEncoding {
    public static Altitude decode(int encoded) {
        if (encoded == 0) {
            return new Altitude();
        }

        boolean mBit = (encoded & 0x40) != 0;
        boolean qBit = (encoded & 0x10) != 0;

        if (!mBit && !qBit) {
            return decodeModeC(encoded);
        }

        if (mBit) {
            return decodeMetric(encoded);
        }

        return decodeFeet(encoded);
    }

    private static Altitude decodeFeet(int encoded) {
        // Remove bits 7 & 8 and stitch the binary message together.
        int n = ((encoded & 0b1111110000000) >>> 2) + ((encoded & 0b100000) >>> 1) + (encoded & 0b1111);

        int altitude = (25 * n) - 1000;

        return new Altitude(altitude, false, 25);
    }

    private static Altitude decodeMetric(int encoded) {
        int altitude = ((encoded >>> 7) << 6) | (encoded & 0xFF);

        return new Altitude(altitude, true, 0);
    }

    /**
     * Decode mode C altitude using appendix chapter three of ICAO Annex 10 Volume 9.
     * Using SSR Automtic pressure altitude transmission code (pulse position assignment) table.
     * <p>
     * Oh yes this is using a lengthy table.
     *
     * @param encoded 13 bit encoded altitude with mbit/qbit
     * @return encoded altitude with 100ft accuracy
     */
    public static Altitude decodeModeC(int encoded) {
        int c1 = (encoded >>> 12) & 0x1;
        int a1 = (encoded >>> 11) & 0x1;
        int c2 = (encoded >>> 10) & 0x1;
        int a2 = (encoded >>> 9) & 0x1;
        int c4 = (encoded >>> 8) & 0x1;
        int a4 = (encoded >>> 7) & 0x1;
        int b1 = (encoded >>> 5) & 0x1;
        int b2 = (encoded >>> 3) & 0x1;
        int d2 = (encoded >>> 2) & 0x1;
        int b4 = (encoded >>> 1) & 0x1;
        int d4 = encoded & 0x1;

        int n500 = grayToBin(d2 << 7 | d4 << 6 | a1 << 5 | a2 << 4 | a4 << 3 | b1 << 2 | b2 << 1 | b4, 8);
        int n100 = grayToBin(c1 << 2 | c2 << 1 | c4, 3) - 1;

        if (n100 == 6) {
            n100 = 4;
        }

        if (n500 % 2 != 0) {
            n100 = 4 - n100;
        }

        int altitude = -1200 + n500 * 500 + n100 * 100;

        return new Altitude(altitude, false, 100);
    }

    private static int grayToBin(int gray, int bitSize) {
        int result = 0;

        for (int i = bitSize - 1; i >= 0; --i) {
            result = result | ((((0x1 << (i + 1)) & result) >>> 1) ^ ((1 << i) & gray));
        }

        return result;
    }
}
