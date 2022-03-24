package tf.cyber.resourcemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tf.cyber.resourcemanager.pep.annotation.XACMLAccessControl;
import tf.cyber.resourcemanager.pep.annotation.attributes.Resource;
import tf.cyber.resourcemanager.service.CGroupCPUService;

import java.io.IOException;

@RestController
@RequestMapping("/cpu")
@XACMLAccessControl
public class CPUController {
    @Autowired
    private CGroupCPUService cGroupCPUService;

    @RequestMapping("/nice")
    public String setNice(@RequestParam @Resource("urn:tf:cyber:resourcecontrol:cpu:nice") Integer niceValue) throws IOException {
        cGroupCPUService.setNiceValue(niceValue);
        return "OK";
    }

    @RequestMapping("/time")
    public String setCPUTime(@RequestParam @Resource("urn:tf:cyber:resourcecontrol:cpu:time") Integer time) throws IOException {
        cGroupCPUService.setCPUMax(time);
        return "OK";
    }

    @RequestMapping("/time_with_fraction")
    public String setCPUTimeWithFraction(@RequestParam @Resource("urn:tf:cyber:resourcecontrol:cpu:time") Integer time,
                                         @RequestParam(required = false) @Resource("urn:tf:cyber:resourcecontrol:cpu:fraction") Integer fraction)
            throws IOException {
        cGroupCPUService.setCPUMax(time, fraction);
        return "OK";
    }

    @RequestMapping("/time_max")
    public String setCPUTimeMax() throws IOException {
        cGroupCPUService.setCPUMaxUnlimited();
        return "OK";
    }
}
