package tf.cyber.gps.microservice.gpsmicroservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tf.cyber.vk162.data.GPSData;
import tf.cyber.vk162.io.VK162Connection;

@RestController
public class GPSDataController {
    @RequestMapping("/")
    public GPSData getGpsData() {
        return VK162Connection.getConnection().getGpsData();
    }
}
