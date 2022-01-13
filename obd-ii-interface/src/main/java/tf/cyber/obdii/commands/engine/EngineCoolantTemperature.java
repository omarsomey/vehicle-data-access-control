package tf.cyber.obdii.commands.engine;

import tf.cyber.obdii.commands.OBD2Command;

public class EngineCoolantTemperature extends OBD2Command<Double> {
    @Override
    public String command() {
        return "01 05";
    }

    @Override
    public Double result() {
        // TODO: Implement this.
        return 0d;
    }

    @Override
    public String getFriendlyName() {
        return "Engine coolant temperature";
    }
}
