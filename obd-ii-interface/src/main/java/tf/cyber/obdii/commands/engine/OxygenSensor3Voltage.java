package tf.cyber.obdii.commands.engine;

import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.util.ByteUtils;

public class OxygenSensor3Voltage extends OBD2Command<Double>{
    @Override
    public String command() {
        return "01 16";
    }

    @Override
    public Double result() {
        int[] bytes = ByteUtils.extractBytes(rawData);
        return bytes[bytes.length - 2] / 200d;
    }

    @Override
    public String getFriendlyName() {
        return "Oxygen Sensor 3 - Voltage";
    }
}
