package aero.t2s.modes;

import org.slf4j.LoggerFactory;

public class BinaryHelper {
    /**
     * @param input String representation of mode-s input (e.g. 8D4840D6202CC371C32CE0576098)
     * @return byte array representation.
     */
    public static short[] stringToByteArray(String input)
    {
        short[] data = new short[input.length() / 2];

        for (int index = 0; index < input.length() / 2; index++) {
            data[index] = Short.parseShort(input.substring(index * 2, (index * 2) + 2), 16);
        }

        return data;
    }

    public static void printMessageAsBinaryMessage(short[] data)
    {
        StringBuilder sb = new StringBuilder();

        printLine(sb);

        for(int i = 4; i<= 10; i++) {
            sb.append("|");

            for (int j = 7; j >= 0; j--) {
                sb.append(String.format(" %2d ", (i-3) * 8 - j));
            }
        }
        sb.append("|");
        printLine(sb);

        for(int i = 4; i<= 10; i++) {
            sb.append("|");

            for (int j = 7; j >= 0; j--) {
                sb.append(String.format(" %2d ", (data[i] >>> j) & 0x1));
            }
        }
        sb.append("|");
        printLine(sb);

        LoggerFactory.getLogger(BinaryHelper.class).debug(sb.toString());
    }


    private static void printLine(StringBuilder sb) {
        sb.append("\n");

        for (int i = 0; i < 7; i++) {
            sb.append("-");
            for (int j = 0; j < 8; j++) {
                sb.append("----");
            }
        }

        sb.append("-\n");
    }
}
