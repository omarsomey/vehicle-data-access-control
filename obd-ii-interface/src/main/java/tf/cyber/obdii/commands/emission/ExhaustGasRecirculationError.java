package tf.cyber.obdii.commands.emission;

import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.util.ByteUtils;

public class ExhaustGasRecirculationError extends OBD2Command<Double> {
    @Override
    public String command() {
        return "01 2D";
    }

    @Override
    public Double result() {
        int[] bytes = ByteUtils.extractBytes(rawData);
        return ((100d/128d) * bytes[bytes.length -1]) - 100;
    }

    @Override
    public String getFriendlyName() {
        return "EGR Error";
    }
}
