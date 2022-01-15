package tf.cyber.xacml.location.gpslocation;

import de.securedimensions.geoxacml.datatype.GeometryValue;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Attributes;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.ow2.authzforce.core.pdp.api.*;
import org.ow2.authzforce.core.pdp.api.value.*;
import org.ow2.authzforce.xacml.identifiers.XacmlStatusCode;

import javax.xml.bind.annotation.XmlRegistry;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@XmlRegistry
public class GPSLocationProvider extends BaseNamedAttributeProvider {
    //private final Set<AttributeDesignatorType> supportedDesignatorTypes;
    //private final Map<AttributeFqn, AttributeBag<?>> attrMap;

    private final AttributeFqn gpsLocationFqn;
    private final AttributeDesignatorType supportedDesignatorType;
    private final Datatype<?> gpsLocationType = GeometryValue.DATATYPE;

    private final String gpsURL;

    private final HttpClient client;
    private final HttpRequest request;

    public final static String ID = "urn:tf:cyber:xacml:location:gpslocation";

    private GPSLocationProvider(String id, String gpsURL, final Map<AttributeFqn, AttributeBag<?>> attributeMap) throws IllegalArgumentException {
        super(ID);
        this.gpsURL = gpsURL;

        this.client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(gpsURL))
                .build();

        //attrMap = Collections.unmodifiableMap(attributeMap);
        //this.supportedDesignatorTypes =
        //        attrMap.entrySet().stream().map(GPSLocationProvider::createAttributeDesignator)
        //        .collect(Collectors.toUnmodifiableSet());

        this.gpsLocationFqn = AttributeFqns.newInstance("urn:oasis:names:tc:xacml:3" +
                                                                ".0:attribute-category:environment",
                                                        Optional.empty(),
                                                        "urn:tf:cyber:xacml:location:gpslocation");

        this.supportedDesignatorType = createAttributeDesignator(
                this.gpsLocationFqn,
                GeometryValue.DATATYPE
        );
    }

    @Override
    public Set<AttributeDesignatorType> getProvidedAttributes() {
        //return supportedDesignatorTypes;
        return Set.of(this.supportedDesignatorType);
    }

    @Override
    public <AV extends AttributeValue> AttributeBag<AV> get(AttributeFqn attributeFqn,
                                                            Datatype<AV> datatype,
                                                            EvaluationContext evaluationContext) throws IndeterminateEvaluationException {
        /*final AttributeBag<?> attrVals = attrMap.get(attributeFqn);
        if (attrVals == null) {
            return null;
        }

        if (attrVals.getElementDatatype().equals(datatype)) {
            return (AttributeBag<AV>) (attrVals);
        }*/

        //throw new IndeterminateEvaluationException("Requested datatype (" + datatype + ") != " +
        //                                                   "provided by " + this + " (" +
        //                                                   attrVals.getElementDatatype() + ")",
        //                                           XacmlStatusCode.MISSING_ATTRIBUTE.value());

        if (gpsLocationType.equals(datatype)) {
            // Fetch location information from microservice API
            HttpResponse<String> response;

            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new IndeterminateEvaluationException("Failed to fetch GPS information!",
                                                           XacmlStatusCode.MISSING_ATTRIBUTE.value());
            }

            JSONObject responseJson = new JSONObject(new String(response.body()));

            List<GeometryValue> lst = new LinkedList<>();
            Point point = new GeometryFactory()
                    .createPoint(new Coordinate(responseJson.getDouble("latitude"),
                                                responseJson.getDouble("longitude")));

            point.setSRID(4326);
            GeometryValue sample = new GeometryValue(point);
            lst.add(sample);

            AttributeBag<AV> bag = (AttributeBag<AV>) Bags.newAttributeBag(GeometryValue.DATATYPE, lst,
                                                                           AttributeSources.PDP);

            return bag;
        }

        throw new IndeterminateEvaluationException("Requested datatype (" + datatype + ") != " +
                                                          "provided by " + this + " (" +
                                                           datatype + ")",
                                                  XacmlStatusCode.MISSING_ATTRIBUTE.value());
    }

    @Override
    public void close() throws IOException {
        // used for i.e. closing connection to remote server or other socket
        // can be empty for now
    }

    /*
    private static AttributeDesignatorType createAttributeDesignator(final Map.Entry<AttributeFqn,
            AttributeBag<?>> attributeEntry) {
        final AttributeFqn attrKey = attributeEntry.getKey();
        final Bag<?> attrVals = attributeEntry.getValue();
        return new AttributeDesignatorType(attrKey.getCategory(), attrKey.getId(),
                                           attrVals.getElementDatatype().getId(),
                                           attrKey.getIssuer().orElse(null), false);
    }
    */

    private static AttributeDesignatorType createAttributeDesignator(AttributeFqn attributeFqn,
                                                                     Datatype<?> attributeType) {
        return new AttributeDesignatorType(attributeFqn.getCategory(), attributeFqn.getId(),
                                           attributeType.getId(),
                                           attributeFqn.getIssuer().orElse(null), false);
    }

    public static class DependencyAwareAttributeProviderFactory implements DependencyAwareFactory {
        private final String providerId;
        private final String serialPath;
        //private final List<Attributes> jaxbAttCats;

        private DependencyAwareAttributeProviderFactory(final String providerId,
                                                        final String serialPath,
                                                        final List<Attributes> jaxbAttributeCategories) {
            assert providerId != null && jaxbAttributeCategories != null;
            this.providerId = providerId;
            //this.jaxbAttCats = jaxbAttributeCategories;
            this.serialPath = serialPath;
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
                // Might be required for the future if we decide to do some fancy XML
                // configuration stuff...
                /*final NamedXacmlAttributeParser<Attribute> namedXacmlAttParser =
                        new XacmlJaxbParsingUtils.NamedXacmlJaxbAttributeParser
                        (attributeValueFactoryRegistry);
                final XacmlRequestAttributeParser<Attribute, AttributeBag<?>>
                xacmlAttributeParser = new NonIssuedLikeIssuedStrictXacmlAttributeParser<>
                (namedXacmlAttParser);
                final Set<String> attrCategoryNames = new HashSet<>();
                final Map<AttributeFqn, AttributeBag<?>> mutableAttMap = new HashMap<>();
                for (final Attributes jaxbAttributes : this.jaxbAttCats) {
                    final String categoryName = jaxbAttributes.getCategory();
                    if (!attrCategoryNames.add(categoryName)) {
                        throw new IllegalArgumentException("Unsupported repetition of " +
                                                                   "Attributes[@Category='" +
                                                                   categoryName + "']");
                    }

                    for (final Attribute jaxbAttr : jaxbAttributes.getAttributes()) {
                        xacmlAttributeParser.parseNamedAttribute(categoryName, jaxbAttr, null,
                                                                 mutableAttMap);
                    }
                }*/

                /*
                List<GeometryValue> lst = new LinkedList<>();
                GeometryValue sample = new GeometryValue(new GeometryFactory().createPoint(new
                Coordinate(-30.0d, 0.0d)));
                lst.add(sample);

                AttributeBag<?> bag = Bags.newAttributeBag(GeometryValue.DATATYPE, lst,
                AttributeSources.PDP);

                final Map<AttributeFqn, AttributeBag<?>> attributeMap = new HashMap<>();
                AttributeFqn fqn = AttributeFqns.newInstance("urn:oasis:names:tc:xacml:3
                .0:attribute-category:environment", Optional.empty(),
                "urn:tf:cyber:xacml:location:gpslocation");
                attributeMap.put(fqn, bag);


                */
                return new GPSLocationProvider(this.providerId, this.serialPath,null);
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
            return new DependencyAwareAttributeProviderFactory(conf.getId(), conf.getGpsURL() ,new LinkedList<>());
        }
    }
}

