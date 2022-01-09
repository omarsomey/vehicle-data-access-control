package tf.cyber.thesis.automotiveaccesscontrol.accesscontrol;

import org.ow2.authzforce.core.pdp.api.*;
import org.ow2.authzforce.core.pdp.api.value.AttributeBag;
import org.ow2.authzforce.core.pdp.api.value.Bags;
import org.ow2.authzforce.core.pdp.api.value.StandardDatatypes;
import org.ow2.authzforce.core.pdp.api.value.StringValue;
import org.ow2.authzforce.core.pdp.impl.BasePdpEngine;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeId;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Optional;

import static org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT;
import static org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory.XACML_3_0_ACTION;

public class XACMLInterceptor {
    public static void XACMLAuthorize(Authentication authentication, HttpMethod action) throws IOException {
        // Instantiate PDP which also loads policies from disk.
        final PdpEngineConfiguration pdpEngineConf = PdpEngineConfiguration.getInstance("C:\\Users\\simon\\Development\\automotive-access-control\\src\\main\\resources\\xacml\\pdp.xml");
        final BasePdpEngine pdp = new BasePdpEngine(pdpEngineConf);

        // Let's build a request object!
        final DecisionRequestBuilder<?> requestBuilder = pdp.newRequestBuilder(-1, -1);

        final AttributeFqn subjectIdAttributeId = AttributeFqns.newInstance(XACML_1_0_ACCESS_SUBJECT.value(), Optional.empty(), XacmlAttributeId.XACML_1_0_SUBJECT_ID.value());
        final AttributeBag<?> subjectIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING, new StringValue("simon"));
        requestBuilder.putNamedAttributeIfAbsent(subjectIdAttributeId, subjectIdAttributeValues);

        // Assume a GET request.
        final AttributeFqn actionIdAttributeId = AttributeFqns.newInstance(XACML_3_0_ACTION.value(), Optional.empty(), XacmlAttributeId.XACML_1_0_ACTION_ID.value());
        final AttributeBag<?> actionIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING, new StringValue("GET"));
        requestBuilder.putNamedAttributeIfAbsent(actionIdAttributeId, actionIdAttributeValues);

        DecisionRequest request = requestBuilder.build(false);

        // Evaluate request.
        DecisionResult res = pdp.evaluate(request);

        System.out.println(res.getDecision());
    }
}
