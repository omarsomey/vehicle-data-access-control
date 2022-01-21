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

    public static boolean[] byteToBoolean(byte x) {
        boolean bool[] = new boolean[4];

        bool[0] = ((x & 0x01) != 0);
        bool[1] = ((x & 0x02) != 0);
        bool[2] = ((x & 0x04) != 0);
        bool[3] = ((x & 0x08) != 0);

        return bool;
    }
}
