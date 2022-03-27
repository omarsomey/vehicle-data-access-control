package tf.cyber.resourcemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/nice", method = RequestMethod.POST)
    @ResponseBody
    public String setNice(@RequestParam @Resource("urn:tf:cyber:resourcecontrol:cpu:nice") Integer niceValue) throws IOException {
        cGroupCPUService.setNiceValue(niceValue);
        return "OK";
    }

    @RequestMapping(value = "/time", method = RequestMethod.POST)
    public String setCPUTime(@RequestParam @Resource("urn:tf:cyber:resourcecontrol:cpu:time") Integer time) throws IOException {
        cGroupCPUService.setCPUMax(time);
        return "OK";
    }

    @RequestMapping(value = "/time/unlimited", method = RequestMethod.POST)
    public String setCPUTimeMax() throws IOException {
        cGroupCPUService.setCPUMaxUnlimited();
        return "OK";
    }

    @RequestMapping(value = "/time_with_fraction", method = RequestMethod.POST)
    public String setCPUTimeWithFraction(@RequestParam @Resource("urn:tf:cyber:resourcecontrol:cpu:time") Integer time,
                                         @RequestParam(required = false) @Resource("urn:tf:cyber:resourcecontrol:cpu:fraction") Integer fraction)
            throws IOException {
        cGroupCPUService.setCPUMax(time, fraction);
        return "OK";
    }
}
