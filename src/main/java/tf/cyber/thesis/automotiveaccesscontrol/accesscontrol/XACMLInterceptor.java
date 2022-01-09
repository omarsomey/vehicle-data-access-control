package tf.cyber.thesis.automotiveaccesscontrol.accesscontrol;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.DecisionType;
import org.ow2.authzforce.core.pdp.api.*;
import org.ow2.authzforce.core.pdp.api.value.AttributeBag;
import org.ow2.authzforce.core.pdp.api.value.Bags;
import org.ow2.authzforce.core.pdp.api.value.StandardDatatypes;
import org.ow2.authzforce.core.pdp.api.value.StringValue;
import org.ow2.authzforce.core.pdp.impl.BasePdpEngine;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import static org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory.*;

public class XACMLInterceptor extends OncePerRequestFilter {

    public static final String XACML_PROTECTED_PATH = "/";

    private final Logger logger = LoggerFactory.getLogger(XACMLInterceptor.class);

    // Instantiate PDP which also loads policies from disk.
    private final PdpEngineConfiguration pdpEngineConf;
    final BasePdpEngine pdpEngine;

    public XACMLInterceptor() throws IOException {
        super();

        logger.info("Loading XACML policy configuration.");
        pdpEngineConf = PdpEngineConfiguration
                .getInstance("C:\\Users\\simon\\Development\\automotive-access-control\\src\\main\\resources\\xacml\\pdp.xml");

        logger.info("Instantiating XACML PDP Engine.");
        pdpEngine = new BasePdpEngine(pdpEngineConf);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().startsWith(XACML_PROTECTED_PATH)) {
            Instant prev = Instant.now();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                // Rethink this: Does XACML support anonymous access?
                logger.info("XACML evaluation failed for anonymous user: " + request.getRequestURI());
                response.sendError(403);
            } else {
                DecisionType decision = XACMLAuthorize(authentication, request);

                if (decision == DecisionType.PERMIT) {
                    logger.info("XACML policy evaluation PERMIT: " + request.getRequestURI());
                    filterChain.doFilter(request, response);
                } else {
                    logger.info("XACML policy evaluation DENY: " + request.getRequestURI());
                    response.sendError(403);
                }

                Instant after = Instant.now();

                int timeNs = after.getNano() - prev.getNano();
                logger.info("XACML policy evaluaton took " + timeNs / 1000000 + " ms");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    public DecisionType XACMLAuthorize(Authentication authentication, HttpServletRequest request) throws IOException {
        // Instantiate request document.
        final DecisionRequestBuilder<?> requestBuilder = pdpEngine.newRequestBuilder(-1, -1);

        // Gather information from requesting subject.
        final AttributeFqn subjectIdAttributeId = AttributeFqns.newInstance(XACML_1_0_ACCESS_SUBJECT.value(),
                Optional.empty(),
                XacmlAttributeId.XACML_1_0_SUBJECT_ID.value());
        final AttributeBag<?> subjectIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(authentication.getName()));

        requestBuilder.putNamedAttributeIfAbsent(subjectIdAttributeId, subjectIdAttributeValues);

        // Gather information about action.
        final AttributeFqn actionIdAttributeId = AttributeFqns.newInstance(XACML_3_0_ACTION.value(),
                Optional.empty(),
                XacmlAttributeId.XACML_1_0_ACTION_ID.value());
        final AttributeBag<?> actionIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(request.getMethod()));
        requestBuilder.putNamedAttributeIfAbsent(actionIdAttributeId, actionIdAttributeValues);

        // Gather information about accessed resource.
        final AttributeFqn resourceIdAttributeId = AttributeFqns.newInstance(XACML_3_0_RESOURCE.value(),
                Optional.empty(),
                XacmlAttributeId.XACML_1_0_RESOURCE_ID.value());
        final AttributeBag<?> resourceIdAttributeValues = Bags.singletonAttributeBag(StandardDatatypes.STRING,
                new StringValue(request.getRequestURI()));
        requestBuilder.putNamedAttributeIfAbsent(resourceIdAttributeId,
                resourceIdAttributeValues);

        DecisionRequest decisionRequest = requestBuilder.build(false);

        // Evaluate request.
        DecisionResult res = pdpEngine.evaluate(decisionRequest);
        return res.getDecision();
    }
}
