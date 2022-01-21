package tf.cyber.obdii.commands.engine;

import tf.cyber.obdii.commands.OBD2Command;

public class RelativeThrottlePosition extends OBD2Command<Double> {
    @Override
    public String command() {
        return "01 45";
    }

    @Override
    public Double result() {
        System.out.println(rawData);
        return 0.0d;
    }

    @Override
    public String getFriendlyName() {
        return "Relative Throttle Position";
    }
}
