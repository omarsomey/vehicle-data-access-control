package tf.cyber.obdii.commands;

import jssc.SerialPortException;
import tf.cyber.obdii.io.OBD2Connection;
import tf.cyber.obdii.exceptions.CANErrorException;
import tf.cyber.obdii.exceptions.InvalidCommandException;

public abstract class OBD2Command<R> {
    protected String rawData = null;

    public abstract String command();

    public abstract R result();

    public abstract String getFriendlyName();

    public void execute(OBD2Connection connection) throws SerialPortException {
        connection.write(this);

        String res = connection.read();

        if (res.equals("?")) {
            throw new InvalidCommandException();
        }

        if (res.equals("CAN ERROR")) {
            throw new CANErrorException();
        }

        this.rawData = res;
    }
}
