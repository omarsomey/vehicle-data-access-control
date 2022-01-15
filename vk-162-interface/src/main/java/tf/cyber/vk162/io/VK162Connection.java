package tf.cyber.vk162.io;

import com.github.jkschneider.netty.jssc.simple.SimpleLineBasedSerialChannel;
import tf.cyber.vk162.data.GPSData;

public class VK162Connection {
    private static VK162Connection connection;

    private SimpleLineBasedSerialChannel channel;
    private GPSData gpsData;

    public static void main(String[] args) {
        init("/dev/ttyACM0");
    }

    public static void init(String portIdentifier) {
        if (connection == null) {
            connection = new VK162Connection(portIdentifier);
        }
    }

    public static VK162Connection getConnection() {
        return connection;
    }

    private VK162Connection(String portIdentifier) {
        // Set update rate to 10Hz (highest supported by this dongle).
        // It actually cannot do 10Hz stable, it appears.
        //byte[] updateRateCmd = {(byte) 0xB5, 0x62, 0x06, 0x08, 0x06, 0x00,
        //        0x64, 0x00, 0x01, 0x00, 0x01, 0x00, 0x7A, 0x12};

        this.channel = new SimpleLineBasedSerialChannel(portIdentifier,
                                                        (ctx, msg) -> {
                                                            synchronized (this) {
                                                                if (msg.startsWith("$GPGGA")) {
                                                                    this.gpsData =
                                                                            handleGPSData(msg);
                                                                }
                                                            }
                                                        });

        //this.channel.write(Unpooled.wrappedBuffer(updateRateCmd));
    }

    private GPSData handleGPSData(String rawData) {
        String[] fragments = rawData.split(",");

        String header = fragments[0];
        double timestamp = Double.parseDouble(fragments[1]);

        double latitude = -1.0d;
        if (!fragments[2].equals("")) {
            latitude = Double.parseDouble(fragments[2]);
        }

        double longitude = -1.0d;
        if (!fragments[4].equals("")) {
            longitude = Double.parseDouble(fragments[4]);
        }

        GPSData.GPSQuality quality =
                GPSData.GPSQuality.fromResponse(Integer.parseInt(fragments[6]));
        int satelliteCount = Integer.parseInt(fragments[7]);

        double antennaAltitude = -1;
        GPSData.AltitudeUnit antennaAltitudeUnit = null;

        if (!fragments[9].equals("")) {
            antennaAltitude = Double.parseDouble(fragments[9]);
            antennaAltitudeUnit = GPSData.AltitudeUnit.fromResponse(fragments[10]);
        }

        GPSData data = GPSData.builder()
                .header(header)
                .timestamp(timestamp)
                .latitude(latitude)
                .longitude(longitude)
                .quality(quality)
                .usedSatellites(satelliteCount)
                .antennaAltitude(antennaAltitude)
                .antennaAltitudeUnit(antennaAltitudeUnit)
                .build();

        return data;
    }

    public synchronized GPSData getGpsData() {
        return this.gpsData;
    }

    public void close() {
        this.channel.close();
        connection = null;
    }
}
