package tf.cyber.vk162.io;

import com.github.jkschneider.netty.jssc.simple.SimpleLineBasedSerialChannel;
import tf.cyber.vk162.data.GPSData;

public class VK162Connection {
    private final SimpleLineBasedSerialChannel channel;

    public GPSData gpsData;

    public static void main(String[] args) {
        VK162Connection conn = new VK162Connection("/dev/ttyACM0");
    }

    public VK162Connection(String portIdentifier) {
        this.channel = new SimpleLineBasedSerialChannel(portIdentifier,
                                                        (ctx, msg) -> {
                                                            synchronized (this) {
                                                                this.gpsData = handleGPSData(msg);
                                                            }
                                                        });
    }

    private GPSData handleGPSData(String rawData) {
        System.out.println(rawData);
        return new GPSData();
    }

    public synchronized GPSData getGpsData() {
        return this.gpsData;
    }
}
