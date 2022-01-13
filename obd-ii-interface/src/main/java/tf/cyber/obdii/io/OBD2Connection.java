package tf.cyber.obdii.io;

import jssc.SerialPort;
import jssc.SerialPortException;
import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.commands.connection.DisableHeader;
import tf.cyber.obdii.commands.connection.EnableHeader;
import tf.cyber.obdii.commands.protocol.ProtocolSelector;
import tf.cyber.obdii.exceptions.TimeoutException;

import java.nio.charset.StandardCharsets;

public class OBD2Connection {

    public static final int TIMEOUT_MSEC = 2000;

    private final SerialPort port;

    public static void main(String[] args) throws SerialPortException {
        OBD2Connection conn = new OBD2Connection("/dev/ttyUSB0");

        DisableHeader cmd = new DisableHeader();
        cmd.execute(conn);

        EnableHeader cmd2 = new EnableHeader();
        cmd2.execute(conn);

        ProtocolSelector proto = new ProtocolSelector(ProtocolSelector.Protocol.AUTOMATIC);
        proto.execute(conn);
        System.out.println(proto.result());

        conn.close();
    }

    public String read() throws SerialPortException {
        final StringBuilder buf = new StringBuilder();
        final long start = System.currentTimeMillis();

        while (true) {
            if (start + TIMEOUT_MSEC < System.currentTimeMillis()) {
                throw new TimeoutException("Timed out while reading from serial console!");
            }

            // Is new data available on the serial console?
            // If no, wait and go do something else.
            if (port.getInputBufferBytesCount() == 0) {
                Thread.yield();
            } else {
                byte[] data = port.readBytes();
                String str = new String(data);

                buf.append(str);

                // Has the terminal symbol appeared?
                // If so, we're done reading.
                // Only keep the last line, though.
                if (str.endsWith(">")) {
                    buf.setLength(buf.length() - 1);

                    String[] intermediate = buf.toString().trim().split("\r");
                    return intermediate[intermediate.length - 1];
                }
            }
        }
    }

    public void write(OBD2Command<?> command) throws SerialPortException {
        port.writeBytes((command.command()+ "\r").getBytes(StandardCharsets.US_ASCII));
    }

    public void close() throws SerialPortException {
        port.closePort();
    }

    public OBD2Connection(String portIdentifier) throws SerialPortException {
        this.port = new SerialPort("/dev/ttyUSB0");
        port.openPort();
        port.setParams(SerialPort.BAUDRATE_38400,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        port.setDTR(true);
        port.setRTS(true);
    }
}
