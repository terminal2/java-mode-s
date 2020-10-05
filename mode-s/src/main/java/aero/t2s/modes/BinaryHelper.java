package aero.t2s.modes;

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
}
