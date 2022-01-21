package tf.cyber.obdii.io;

import jssc.SerialPort;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.commands.connection.*;
import tf.cyber.obdii.commands.engine.CalculatedEngineLoad;
import tf.cyber.obdii.commands.engine.EngineCoolantTemperature;
import tf.cyber.obdii.commands.engine.RelativeThrottlePosition;
import tf.cyber.obdii.commands.protocol.ProtocolSelector;
import tf.cyber.obdii.commands.vehicle.AmbientAirTemperature;
import tf.cyber.obdii.commands.vehicle.Odometer;
import tf.cyber.obdii.exceptions.TimeoutException;

import java.nio.charset.StandardCharsets;

public class OBD2Connection {

    public static final int TIMEOUT_MSEC = 2000;
    public static final int READ_BACKOFF_DELAY_MS = 10;

    private final SerialPort port;

    public static void main(String[] args) throws SerialPortException, InterruptedException {
        OBD2Connection conn = new OBD2Connection("/dev/ttyUSB0");

        ResetELM reset = new ResetELM();
        reset.execute(conn);

        DisableHeader cmd = new DisableHeader();
        cmd.execute(conn);

        DisableEcho cmd2 = new DisableEcho();
        cmd2.execute(conn);

        EnableHeader cmd3 = new EnableHeader();
        cmd3.execute(conn);

        DisableHeader cmd4 = new DisableHeader();
        cmd4.execute(conn);

        // With enabled header:
        // 7E8 03 41 46 32

        ProtocolSelector proto = new ProtocolSelector(ProtocolSelector.Protocol.AUTOMATIC);
        proto.execute(conn);

        SupportedPID0to20 temp = new SupportedPID0to20();
        temp.execute(conn);
        System.out.println(temp.result());

        conn.close();
    }

    public String read() throws SerialPortException, InterruptedException {
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
                Thread.sleep(READ_BACKOFF_DELAY_MS);
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
        port.writeBytes((command.command()+ "\r\n").getBytes(StandardCharsets.US_ASCII));
    }

    public void close() throws SerialPortException {
        port.closePort();
    }

    public OBD2Connection(String portIdentifier) throws SerialPortException {
        this.port = new SerialPort(portIdentifier);
        port.openPort();
        port.setParams(SerialPort.BAUDRATE_38400,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        port.setDTR(true);
        port.setRTS(true);
    }
}
