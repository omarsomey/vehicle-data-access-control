package tf.cyber.xacml.location.gpslocation;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Attributes;
import org.ow2.authzforce.core.pdp.api.*;
import org.ow2.authzforce.core.pdp.api.value.*;
import org.ow2.authzforce.xacml.identifiers.XacmlStatusCode;

import javax.xml.bind.annotation.XmlRegistry;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@XmlRegistry
public class GPSLocationProvider extends BaseNamedAttributeProvider {
    private final Set<AttributeDesignatorType> supportedDesignatorTypes;
    private final Map<AttributeFqn, AttributeBag<?>> attrMap;

    public final static String ID = "tf.cyber.xacml.location.gpslocation";

    private GPSLocationProvider(final String id,
                                final Map<AttributeFqn, AttributeBag<?>> attributeMap) throws IllegalArgumentException {
        super(ID);
        attrMap = Collections.unmodifiableMap(attributeMap);

        this.supportedDesignatorTypes =
                attrMap.entrySet().stream().map(GPSLocationProvider::newAttributeDesignator).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<AttributeDesignatorType> getProvidedAttributes() {
        return supportedDesignatorTypes;
    }

    @Override
    public <AV extends AttributeValue> AttributeBag<AV> get(AttributeFqn attributeFqn,
                                                            Datatype<AV> datatype,
                                                            EvaluationContext evaluationContext) throws IndeterminateEvaluationException {
        final AttributeBag<?> attrVals = attrMap.get(attributeFqn);
        if (attrVals == null) {
            return null;
        }

        if (attrVals.getElementDatatype().equals(datatype)) {
            return (AttributeBag<AV>) attrVals;
        }

        throw new IndeterminateEvaluationException("Requested datatype (" + datatype + ") != " +
                                                           "provided by " + this + " (" + attrVals.getElementDatatype() + ")",
                                                   XacmlStatusCode.MISSING_ATTRIBUTE.value());
    }

    @Override
    public void close() throws IOException {
        // used for i.e. closing connection to remote server or other socket
        // can be empty for now
    }

    private static AttributeDesignatorType newAttributeDesignator(final Map.Entry<AttributeFqn,
            AttributeBag<?>> attributeEntry) {
        final AttributeFqn attrKey = attributeEntry.getKey();
        final Bag<?> attrVals = attributeEntry.getValue();
        return new AttributeDesignatorType(attrKey.getCategory(), attrKey.getId(),
                                           attrVals.getElementDatatype().getId(),
                                           attrKey.getIssuer().orElse(null), false);
    }

    public static class DependencyAwareAttributeProviderFactory implements DependencyAwareFactory {
        private final String providerId;
        private final List<Attributes> jaxbAttCats;

        private DependencyAwareAttributeProviderFactory(final String providerId,
                                                        final List<Attributes> jaxbAttributeCategories) {
            assert providerId != null && jaxbAttributeCategories != null;
            this.providerId = providerId;
            this.jaxbAttCats = jaxbAttributeCategories;
        }

        @Override
        public Set<AttributeDesignatorType> getDependencies() {
            // We do not have any dependencies.
            return null;
        }

        @Override
        public CloseableNamedAttributeProvider getInstance(AttributeValueFactoryRegistry attributeValueFactoryRegistry,
                                                           AttributeProvider attributeProvider) {
            {
                /*final NamedXacmlAttributeParser<Attribute> namedXacmlAttParser =
                        new XacmlJaxbParsingUtils.NamedXacmlJaxbAttributeParser(attributeValueFactoryRegistry);
                final XacmlRequestAttributeParser<Attribute, AttributeBag<?>> xacmlAttributeParser = new NonIssuedLikeIssuedStrictXacmlAttributeParser<>(namedXacmlAttParser);
                final Set<String> attrCategoryNames = new HashSet<>();
                final Map<AttributeFqn, AttributeBag<?>> mutableAttMap = new HashMap<>();
                for (final Attributes jaxbAttributes : this.jaxbAttCats) {
                    final String categoryName = jaxbAttributes.getCategory();
                    if (!attrCategoryNames.add(categoryName)) {
                        throw new IllegalArgumentException("Unsupported repetition of " +
                                                                   "Attributes[@Category='" + categoryName + "']");
                    }

                    for (final Attribute jaxbAttr : jaxbAttributes.getAttributes()) {
                        xacmlAttributeParser.parseNamedAttribute(categoryName, jaxbAttr, null,
                                                                 mutableAttMap);
                    }
                }*/

                List<StringValue> lst = new LinkedList<>();
                lst.add(new StringValue("TEST"));

                AttributeBag<?> bag = Bags.newAttributeBag(StandardDatatypes.STRING, lst, AttributeSources.PDP);

                final Map<AttributeFqn, AttributeBag<?>> attributeMap = new HashMap<>();

                AttributeFqn fqn = AttributeFqns.newInstance("urn:oasis:names:tc:xacml:3.0:attribute-category:environment", Optional.empty(), "tf.cyber.xacml.location.gpslocation");
                attributeMap.put(fqn, bag);

                return new GPSLocationProvider(this.providerId, attributeMap);
            }
        }
    }

    public static class Factory extends CloseableNamedAttributeProvider.FactoryBuilder<GPSLocationProviderDescriptor> {
        @Override
        public Class<GPSLocationProviderDescriptor> getJaxbClass() {
            return GPSLocationProviderDescriptor.class;
        }

        @Override
        public CloseableNamedAttributeProvider.DependencyAwareFactory getInstance(GPSLocationProviderDescriptor conf, EnvironmentProperties environmentProperties) throws IllegalArgumentException {
            return new DependencyAwareAttributeProviderFactory(conf.getId(), new LinkedList<>());
        }
    }
}

