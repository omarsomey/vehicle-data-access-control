package cyber.tf.authzforcepdpservice.controller;

import cyber.tf.authzforcepdpservice.service.PDPService;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Request;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Response;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PDPController {

    private final Logger logger = LoggerFactory.getLogger(PDPController.class);

    @Autowired
    private PDPService pdpService;

    @RequestMapping(value = "/authorize",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE}
    )
    public Response evaluateXML(Request request) {
        logger.info("Evaluating XACXML XML request.");
        Response res = pdpService.getXMLAdapter().evaluate(request);
        logger.info("Finished evaluating XACXML XML request.");
        return res;
    }

    @RequestMapping(value = "/authorize",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xacml+json"},
            produces = {"application/json", "application/xacml+json"}
    )
    public String evaluateJSON(@RequestBody String request) {
        JSONObject json = new JSONObject(new JSONTokener(request));
        logger.info("Evaluating XACXML JSON request.");
        JSONObject res = pdpService.getJSONAdapter().evaluate(json);
        logger.info("Finished evaluating XACXML JSON request.");
        return res.toString();
    }
}
