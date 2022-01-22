package tf.cyber.thesis.automotiveaccesscontrol.controller.environment;

import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tf.cyber.obdii.commands.environment.AmbientAirTemperature;
import tf.cyber.obdii.io.OBD2Connection;

import java.util.Map;

@RestController
@RequestMapping("/environment/")
public class EnvironmentController {
    @Autowired
    OBD2Connection obd2Connection;

    @RequestMapping("/ambient")
    public Map<String, Object> ambientAirTemperature() throws SerialPortException, InterruptedException {
        AmbientAirTemperature ambientAirTemperature = new AmbientAirTemperature();
        ambientAirTemperature.execute(obd2Connection);

        return Map.of(
                "ambient_air_temp", ambientAirTemperature.result()
        );
    }
}
