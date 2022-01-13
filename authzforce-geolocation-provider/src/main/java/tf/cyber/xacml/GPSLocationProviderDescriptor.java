package tf.cyber.xacml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Attributes;
import org.ow2.authzforce.xmlns.pdp.ext.AbstractAttributeProvider;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GPSLocationProviderDescriptor", propOrder = {
    "attributes"
})
public class GPSLocationProviderDescriptor
    extends AbstractAttributeProvider
{

    @XmlElement(name = "Attributes", namespace = "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17", required = true)
    protected List<Attributes> attributes;

    public List<Attributes> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return this.attributes;
    }

}
