package tf.cyber.xacml.pollingfrequency;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import org.ow2.authzforce.core.pdp.api.*;
import org.ow2.authzforce.core.pdp.api.value.*;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory;
import org.ow2.authzforce.xacml.identifiers.XacmlStatusCode;

import javax.xml.bind.annotation.XmlRegistry;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.*;

@XmlRegistry
public class PollingFrequencyProvider extends BaseNamedAttributeProvider {
    private final String logUrl;

    private final HttpClient client;
    private final HttpRequest request;

    private final NamedAttributeProvider dependencyProvider;

    private final static String PROVIDER_ID = "urn:tf:cyber:xacml:polling-frequency";

    private PollingFrequencyProvider(String logUrl, NamedAttributeProvider dependencyProvider) throws IllegalArgumentException {
        super(PROVIDER_ID);
        this.logUrl = logUrl;
        this.dependencyProvider = dependencyProvider;

        this.client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(logUrl))
                .build();
    }

    @Override
    public Set<AttributeDesignatorType> getProvidedAttributes() {
        AttributeDesignatorType ms = createAttributeDesignator(AttributeFqns.newInstance(XacmlAttributeCategory.XACML_3_0_ENVIRONMENT.value(),
                                                                                              Optional.empty(),
                                                                                         "urn:tf:cyber:xacml:polling-frequency:time-since-last-access:ms"),
                                                                    StandardDatatypes.STRING, false);

        AttributeDesignatorType s = createAttributeDesignator(AttributeFqns.newInstance(XacmlAttributeCategory.XACML_3_0_ENVIRONMENT.value(),
                                                                                         Optional.empty(),
                                                                                         "urn:tf:cyber:xacml:polling-frequency:time-since-last-access:s"),
                                                               StandardDatatypes.STRING, false);

        AttributeDesignatorType hz = createAttributeDesignator(AttributeFqns.newInstance(XacmlAttributeCategory.XACML_3_0_ENVIRONMENT.value(),
                                                                                        Optional.empty(),
                                                                                        "urn:tf:cyber:xacml:polling-frequency:hz"),
                                                              StandardDatatypes.DOUBLE, false);

        return Set.of(ms, s, hz);
    }

    @Override
    public <AV extends AttributeValue> AttributeBag<AV> get(AttributeFqn attributeFqn,
                                                            Datatype<AV> datatype,
                                                            EvaluationContext evaluationContext,
                                                            Optional<EvaluationContext> optional) throws IndeterminateEvaluationException {

        System.out.println(dependencyProvider.getProvidedAttributes());

        throw new IndeterminateEvaluationException("Requested datatype (" + datatype + ") != " +
                                                           "provided by " + this + " (" +
                                                           datatype + ")",
                                                   XacmlStatusCode.MISSING_ATTRIBUTE.value());
    }

    @Override
    public void close() {
    }

    private static AttributeDesignatorType createAttributeDesignator(AttributeFqn attributeFqn,
                                                                     Datatype<?> attributeType,
                                                                     boolean mustBePresent) {
        return new AttributeDesignatorType(attributeFqn.getCategory(), attributeFqn.getId(),
                                           attributeType.getId(),
                                           attributeFqn.getIssuer().orElse(null), mustBePresent);
    }

    public static class DependencyAwareAttributeProviderFactory implements DependencyAwareFactory {
        private final String logPath;

        private DependencyAwareAttributeProviderFactory(final String logPath) {
            this.logPath = logPath;
        }

        @Override
        public Set<AttributeDesignatorType> getDependencies() {
            AttributeDesignatorType subject = createAttributeDesignator(AttributeFqns.newInstance(XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value(),
                                                                Optional.empty(),
                                                                XacmlAttributeCategory.XACML_1_0_ACCESS_SUBJECT.value()),
                                                                StandardDatatypes.STRING, true);

            AttributeDesignatorType action = createAttributeDesignator(AttributeFqns.newInstance(XacmlAttributeCategory.XACML_3_0_ACTION.value(),
                                                                                                  Optional.empty(),
                                                                                                  XacmlAttributeCategory.XACML_3_0_ACTION.value()),
                                                                        StandardDatatypes.STRING, true);

            AttributeDesignatorType resource = createAttributeDesignator(AttributeFqns.newInstance(XacmlAttributeCategory.XACML_3_0_RESOURCE.value(),
                                                                                                 Optional.empty(),
                                                                                                 XacmlAttributeCategory.XACML_3_0_RESOURCE.value()),
                                                                       StandardDatatypes.STRING, true);

            return Set.of(subject, action, resource);
        }

        @Override
        public CloseableNamedAttributeProvider getInstance(AttributeValueFactoryRegistry attributeValueFactoryRegistry,
                                                           NamedAttributeProvider attributeProvider) {
            {
                return new PollingFrequencyProvider(this.logPath, attributeProvider);
            }
        }
    }

    public static class Factory extends CloseableNamedAttributeProvider.FactoryBuilder<PollingFrequencyProviderDescriptor> {
        @Override
        public Class<PollingFrequencyProviderDescriptor> getJaxbClass() {
            return PollingFrequencyProviderDescriptor.class;
        }

        @Override
        public CloseableNamedAttributeProvider.DependencyAwareFactory getInstance(PollingFrequencyProviderDescriptor conf, EnvironmentProperties environmentProperties) throws IllegalArgumentException {
            return new DependencyAwareAttributeProviderFactory(conf.getLogUrl());
        }
    }
}

