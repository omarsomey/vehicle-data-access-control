package tf.cyber.obdii.commands.connection;

import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.util.ByteUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SupportedPID0to20 extends OBD2Command<List<OBD2Command<?>>> {
    @Override
    public String command() {
        return "01 00";
    }

    @Override
    public List<OBD2Command<?>> result() {
        List<OBD2Command<?>> supportedCommands = new LinkedList<>();

        String [] bytesStr = ByteUtils.extractBytesRaw(rawData);
        String str = bytesStr[bytesStr.length - 4] + bytesStr[bytesStr.length - 3]
                + bytesStr[bytesStr.length - 2] + bytesStr[bytesStr.length - 1];

        byte[] bytes = str.getBytes(StandardCharsets.US_ASCII);
        System.out.println(str);

        for (int c = 0; c < bytes.length; c++) {
            boolean[] mask = ByteUtils.byteToBoolean(bytes[c]);
            System.out.println(Arrays.toString(mask));
        }

        return null;
    }

    @Override
    public String getFriendlyName() {
        return "Fetch Supported PIDs (0 to 20)";
    }
}
