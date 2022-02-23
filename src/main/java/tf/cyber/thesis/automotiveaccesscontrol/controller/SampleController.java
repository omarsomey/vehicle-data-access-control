package tf.cyber.thesis.automotiveaccesscontrol.controller;

import org.springframework.web.bind.annotation.*;
import tf.cyber.thesis.automotiveaccesscontrol.pep.annotation.XACMLAccessControl;
import tf.cyber.thesis.automotiveaccesscontrol.pep.annotation.attributes.Resource;
import tf.cyber.thesis.automotiveaccesscontrol.pep.annotation.attributes.Subject;

@RestController
@XACMLAccessControl
public class SampleController {
    @RequestMapping(value = "/sample",
            method = RequestMethod.GET
    )
    public String samplePost(@RequestParam @Subject("someSubjectAttribute") String i, @RequestParam @Resource("someResourceAttribute") String j) {
        return String.valueOf(i + j);
    }
}
