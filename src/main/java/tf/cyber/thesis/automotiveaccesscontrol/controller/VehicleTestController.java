package tf.cyber.thesis.automotiveaccesscontrol.controller;

import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.commands.engine.CalculatedEngineLoad;
import tf.cyber.obdii.commands.engine.EngineOilTemperature;
import tf.cyber.obdii.commands.engine.EngineSpeed;
import tf.cyber.obdii.commands.engine.ThrottlePosition;
import tf.cyber.obdii.commands.environment.AmbientAirTemperature;
import tf.cyber.obdii.commands.vehicle.RuntimeSinceEngineStart;
import tf.cyber.obdii.commands.vehicle.VehicleSpeed;
import tf.cyber.obdii.io.OBD2Connection;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VehicleTestController {
    @Autowired
    OBD2Connection obd2Connection;

    private final List<OBD2Command<?>> basicCommands = List.of(
            new VehicleSpeed(),
            new EngineSpeed(),
            new AmbientAirTemperature(),
            //new CalculatedEngineLoad(),
            //new EngineOilTemperature(),
            new ThrottlePosition(),
            new RuntimeSinceEngineStart()
    );

    @RequestMapping("/info")
    public Map<String, Object> getBasicVehicleInfo() {
        Map<String, Object> res = new HashMap<>();

        basicCommands.forEach(obd2Command -> {
            try {
                System.out.println(obd2Command.getClass().getCanonicalName());
                obd2Command.execute(obd2Connection);

                res.put(obd2Command.getKey(), obd2Command.result());
            } catch (SerialPortException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        res.put("time", Instant.now());

        return res;
    }
}
