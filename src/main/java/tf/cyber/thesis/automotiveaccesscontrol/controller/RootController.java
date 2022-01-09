package tf.cyber.thesis.automotiveaccesscontrol.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @RequestMapping("/")
    public String rootPage() {
        return "Hey there!";
    }

    @RequestMapping("/vehicle")
    public String vehiclePage() {
        return "This is some vehicle data!";
    }
}
