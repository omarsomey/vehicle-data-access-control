package tf.cyber.thesis.automotiveaccesscontrol.pep;

import okhttp3.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tf.cyber.thesis.automotiveaccesscontrol.pep.annotation.attributes.*;
import tf.cyber.thesis.automotiveaccesscontrol.pep.data.DataMapper;
import tf.cyber.thesis.automotiveaccesscontrol.pep.data.DataTypes;
import tf.cyber.thesis.automotiveaccesscontrol.pep.util.Pair;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
public class XACMLInterceptor {
    private final Logger logger = LoggerFactory.getLogger(XACMLInterceptor.class);

    private static final String XACML_SUBJECT_CATEGORY = "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";
    private static final String XACML_ACTION_CATEGORY = "urn:oasis:names:tc:xacml:3.0:attribute-category:action";
    private static final String XACML_RESOURCE_CATEGORY = "urn:oasis:names:tc:xacml:3.0:attribute-category:resource";
    private static final String XACML_ENVIRONMENT_CATEGORY = "urn:oasis:names:tc:xacml:3.0:attribute-category:environment";

    private static final String XACML_SUBJECT_ID_ATTRIBUTE = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    private static final String XACML_ACTION_ID_ATTRIBUTE = "urn:oasis:names:tc:xacml:1.0:action:action-id";
    private static final String XACML_RESOURCE_ID_ATTRIBUTE = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";

    private static final List<Class<?>> annotationCandiates = List.of(
            CustomCategory.class,
            Environment.class,
            Resource.class,
            Subject.class
    );

    private static enum DECISION_TYPE {
        PERMIT,
        DENY,
        INDETERMINATE,
        NOT_APPLICABLE
    }

    @Autowired
    OkHttpClient httpClient;

    @Autowired
    org.springframework.core.env.Environment env;

    @Around("@annotation(tf.cyber.thesis.automotiveaccesscontrol.pep.annotation.XACMLAccessControl)" +
            " || @within(tf.cyber.thesis.automotiveaccesscontrol.pep.annotation.XACMLAccessControl)")
    public Object interceptXACML(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Fetch current request from context in order to retrieve information about targeted URI.
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest();

        if (authentication == null) {
            // We assume that entities are authenticated, with their subject id available.
            logger.info("User tried to access XACML-protected route without authentication!");
            //throw new AccessDeniedException("Tried to access XACML-protected route without authentication!");
        }

        // Assemble XACML request attributes.
        Map<String, Map<String, Pair<String, String>>> categories = new HashMap<>();

        categories.put(XACML_SUBJECT_CATEGORY, new HashMap<>());
        categories.put(XACML_ACTION_CATEGORY, new HashMap<>());
        categories.put(XACML_RESOURCE_CATEGORY, new HashMap<>());

        // Add subject id, if available.
        if (authentication.getName() != null) {
            categories.get(XACML_SUBJECT_CATEGORY)
                    .put(XACML_SUBJECT_ID_ATTRIBUTE, Pair.of(DataTypes.map(authentication.getName()), DataMapper.map(authentication.getName())));
        }

        // Add resource id, if available.
        if (request.getRequestURI() != null) {
            categories.get(XACML_RESOURCE_CATEGORY)
                    .put(XACML_RESOURCE_ID_ATTRIBUTE, Pair.of(DataTypes.map(request.getRequestURI()), DataMapper.map(request.getRequestURI())));
        }

        // Add action id, if available.
        if (request.getMethod() != null) {
            categories.get(XACML_ACTION_CATEGORY)
                    .put(XACML_ACTION_ID_ATTRIBUTE, Pair.of(DataTypes.map(request.getMethod()), DataMapper.map(request.getMethod())));
        }

        // Check if called function has annotated parameters that should be evaluated as part
        // of the PDP request.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        AtomicInteger i = new AtomicInteger(0);
        Arrays.stream(signature.getMethod().getParameterAnnotations()).forEach(parameterAnnotations -> {
            Arrays.stream(parameterAnnotations).forEach(annotation -> {
                if (annotation instanceof Subject) {
                    Subject subject = (Subject) annotation;

                    categories.get(XACML_SUBJECT_CATEGORY)
                            .put(subject.value(), Pair.of(DataTypes.map(args[i.get()]), DataMapper.map(args[i.get()])));
                } else if (annotation instanceof Action) {
                    Action action = (Action) annotation;
                    categories.get(XACML_ACTION_CATEGORY)
                            .put(action.value(), Pair.of(DataTypes.map(args[i.get()]), DataMapper.map(args[i.get()])));
                } else if (annotation instanceof Resource) {
                    Resource resource = (Resource) annotation;
                    categories.get(XACML_RESOURCE_CATEGORY)
                            .put(resource.value(), Pair.of(DataTypes.map(args[i.get()]), DataMapper.map(args[i.get()])));
                } else if (annotation instanceof Environment) {
                    Environment resource = (Environment) annotation;
                    categories.putIfAbsent(XACML_ENVIRONMENT_CATEGORY, new HashMap<>());
                    categories.get(XACML_ENVIRONMENT_CATEGORY)
                            .put(resource.value(), Pair.of(DataTypes.map(args[i.get()]), DataMapper.map(args[i.get()])));
                }
                else if (annotation instanceof CustomCategory) {
                    CustomCategory category = (CustomCategory) annotation;
                    categories.putIfAbsent(category.category(), new HashMap<>());
                    categories.get(category.category())
                            .put(category.value(), Pair.of(DataTypes.map(args[i.get()]), DataMapper.map(args[i.get()])));
                }
            });

            i.addAndGet(1);
        });

        // Evaluate XACML request through PDP service.
        JSONObject requestJSON = assembleRequestJSON(categories);
        DECISION_TYPE decision = evaluateRequest(requestJSON);

        if (decision == DECISION_TYPE.PERMIT) {
            return joinPoint.proceed();
        }

        throw new AccessDeniedException("XACML Policy evaluation resulted in DENY.");
    }

    private JSONObject assembleRequestJSON(Map<String, Map<String, Pair<String, String>>> data) {
        JSONObject root = new JSONObject();
        JSONObject request = new JSONObject();
        JSONArray categoryList = new JSONArray();

        root.put("Request", request);
        request.put("Category", categoryList);

        // Walk through all specified categories.
        data.forEach((key, value) -> {
            JSONObject currentCategory = new JSONObject();
            JSONArray attributes = new JSONArray();

            currentCategory.put("CategoryId", key);
            currentCategory.put("Attribute", attributes);

            value.forEach((attributeId, pair) -> {
                JSONObject attribute = new JSONObject();

                attribute.put("AttributeId", attributeId);
                attribute.put("DataType", pair.getFirst());
                attribute.put("Value", pair.getSecond());

                attributes.put(attribute);
            });

            categoryList.put(currentCategory);
        });

        return root;
    }

    private DECISION_TYPE evaluateRequest(JSONObject request) {
        DECISION_TYPE decision = null;

        Request xacmlHTTPRequest = new Request.Builder()
                .url(env.getProperty("pdp.location"))
                .post(RequestBody.create(request.toString(),
                                         MediaType.get("application/json; charset=utf-8")))
                .build();

        try(Response response = httpClient.newCall(xacmlHTTPRequest).execute()) {
            JSONObject res = new JSONObject(response.body().string());
            JSONArray resArr = (JSONArray) res.get("Response");

            JSONObject evaluation = (JSONObject) resArr.get(0);

            switch(evaluation.get("Decision").toString()) {
                case "Permit":
                    decision = DECISION_TYPE.PERMIT;
                    break;
                case "Deny":
                    decision = DECISION_TYPE.DENY;
                    break;
                case "Indeterminate":
                    decision = DECISION_TYPE.INDETERMINATE;
                    break;
                case "NotApplicable":
                    decision = DECISION_TYPE.NOT_APPLICABLE;
                    break;
                default:
                    throw new XACMLEvaluationException("Failed to determine decision type from PDP response.");
            }

            // TODO: Consider Obligations and Advices.

        } catch (IOException e) {
            throw new XACMLEvaluationException();
        }

        return decision;
    }
}
