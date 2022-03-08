package tf.cyber.thesis.automotiveaccesscontrol.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tf.cyber.thesis.automotiveaccesscontrol.pep.annotation.XACMLAccessControl;

@RestController
@XACMLAccessControl
public class LocationController {
    @RequestMapping("/vehicle/location")
    public String getLocation() {
        return "Loction data!";
    }
}
