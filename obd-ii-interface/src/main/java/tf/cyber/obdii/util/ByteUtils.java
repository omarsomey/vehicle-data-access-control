package tf.cyber.obdii.util;

public class ByteUtils {
    public static int[] extractBytes(String input) {
        String[] byteStrings = input.split(" ");

        int[] result = new int[byteStrings.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(byteStrings[i], 16);
        }

        return result;
    }

    public static String[] extractBytesRaw(String input) {
        return input.split(" ");
    }

    public static Boolean[] byteToBoolean(int x) {
        if (x < 0 || x > 255) {
            throw new NumberFormatException("Value out of range!");
        }

        return byteToBoolean((byte) x);
    }

    public static Boolean[] byteToBoolean(byte x) {
        Boolean bool[] = new Boolean[4];

        bool[0] = ((x & 1) != 0);
        bool[1] = ((x & 2) != 0);
        bool[2] = ((x & 4) != 0);
        bool[3] = ((x & 8) != 0);

        return bool;
    }
}
