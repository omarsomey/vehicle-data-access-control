package tf.cyber.obdii.commands.debug;

import tf.cyber.obdii.OBD2Command;

public class ELMVersionInformation extends OBD2Command {

    @Override
    public String getCommand() {
        return "ATZ";
    }
}
