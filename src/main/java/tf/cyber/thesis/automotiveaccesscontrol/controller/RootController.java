package tf.cyber.thesis.automotiveaccesscontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tf.cyber.thesis.automotiveaccesscontrol.entity.AccessLog;
import tf.cyber.thesis.automotiveaccesscontrol.repository.AccessLogRepository;

@RestController
public class RootController {

    @Autowired
    AccessLogRepository accessLog;

    @RequestMapping("/")
    public String rootPage() {
        accessLog.save(new AccessLog());
        return "Hey there!";
    }
}
