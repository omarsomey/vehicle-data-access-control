package tf.cyber.resourcemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tf.cyber.resourcemanager.pep.annotation.XACMLAccessControl;
import tf.cyber.resourcemanager.pep.annotation.attributes.Resource;
import tf.cyber.resourcemanager.service.CGroupMemoryService;

import java.io.IOException;

@RestController
@RequestMapping("/memory")
@XACMLAccessControl
public class MemoryController {
    @Autowired
    private CGroupMemoryService cGroupMemoryService;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public String getMemoryCurrent() throws IOException {
        return cGroupMemoryService.getMemoryCurrent();
    }

    @RequestMapping(value = "/high", method = RequestMethod.GET)
    public String getMemoryHigh() throws IOException {
        return cGroupMemoryService.getMemoryHigh();
    }

    @RequestMapping(value = "/max", method = RequestMethod.GET)
    public String getMemoryMax() throws IOException {
        return cGroupMemoryService.getMemoryMax();
    }

    @RequestMapping(value = "/swap_current", method = RequestMethod.GET)
    public String getMemorySwapCurrent() throws IOException {
        return cGroupMemoryService.getMemorySwapCurrent();
    }

    @RequestMapping(value = "/swap_high", method = RequestMethod.GET)
    public String getMemorySwapHigh() throws IOException {
        return cGroupMemoryService.getMemorySwapHigh();
    }

    @RequestMapping(value = "/swap_max", method = RequestMethod.GET)
    public String getMemorySwapMax() throws IOException {
        return cGroupMemoryService.getMemorySwapMax();
    }

    // ----------------------------------------------------------------------------------

    @RequestMapping(value = "/high", method = RequestMethod.POST)
    public String setMemoryHigh(@Resource("urn:tf:cyber:resourcecontrol:memory:high") int memoryHigh) throws IOException {
        cGroupMemoryService.setMemoryHigh(memoryHigh);
        return "OK";
    }

    @RequestMapping(value = "/high/unlimited", method = RequestMethod.POST)
    public String setMemoryHighUnlimited() throws IOException {
        cGroupMemoryService.setMemoryHighUnlimited();
        return "OK";
    }

    @RequestMapping(value = "/max", method = RequestMethod.POST)
    public String setMemoryMax(@Resource("urn:tf:cyber:resourcecontrol:memory:max") int memoryMax) throws IOException {
        cGroupMemoryService.setMemoryMax(memoryMax);
        return "OK";
    }

    @RequestMapping(value = "/max/unlimited", method = RequestMethod.POST)
    public String setMemoryMaxUnlimited() throws IOException {
        cGroupMemoryService.setMemoryMaxUnlimited();
        return "OK";
    }

    @RequestMapping(value = "/swap_high", method = RequestMethod.POST)
    public String setMemorySwapHigh(@Resource("urn:tf:cyber:resourcecontrol:memory:swap:high") int swapHigh) throws IOException {
        cGroupMemoryService.setMemorySwapHigh(swapHigh);
        return "OK";
    }

    @RequestMapping(value = "/swap_high/unlimited", method = RequestMethod.POST)
    public String setMemorySwapHighUnlimited() throws IOException {
        cGroupMemoryService.setMemorySwapHighUnlimited();
        return "OK";
    }

    @RequestMapping(value = "/swap_max", method = RequestMethod.POST)
    public String setMemorySwapMax(@Resource("urn:tf:cyber:resourcecontrol:memory:swap:max") int swapMax) throws IOException {
        cGroupMemoryService.setMemorySwapMax(swapMax);
        return "OK";
    }

    @RequestMapping(value = "/swap_max/unlimited", method = RequestMethod.POST)
    public String setMemorySwapMaxUnlimited() throws IOException {
        cGroupMemoryService.setMemorySwapMaxUnlimited();
        return "OK";
    }
}
