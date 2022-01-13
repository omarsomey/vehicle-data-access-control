package tf.cyber.obdii.util;

import java.util.Arrays;

public class ByteUtils {
    public int[] extractABCD(String input) {
        String[] byteStrings = input.split(" ");

        if (byteStrings.length != 4) {
            throw new IllegalArgumentException("Input must contain 4 space-separated bytes!");
        }

        int[] abcd = new int[4];

        for (int i = 0; i < 4; i++) {
            abcd[i] = Integer.parseInt(byteStrings[i]);
        }

        return abcd;
    }
}
