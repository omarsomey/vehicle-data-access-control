package tf.cyber.authzforce.time.datatypes;

import net.sf.saxon.s9api.XPathCompiler;
import org.ow2.authzforce.core.pdp.api.value.AttributeDatatype;
import org.ow2.authzforce.core.pdp.api.value.BaseAttributeValueFactory;
import org.ow2.authzforce.core.pdp.api.value.SimpleValue;
import tf.cyber.authzforce.time.datatypes.java.DayOfWeekType;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayOfWeekValue extends SimpleValue<DayOfWeekType> {
    public static final AttributeDatatype<DayOfWeekValue> TYPE =
            new AttributeDatatype<>(DayOfWeekValue.class,
                                    "urn:oasis:names:tc:xacml:3.0:data‑type:dayOfWeek",
                                    "urn:oasis:names:tc:xacml:3.0:data‑type:dayOfWeek");


    protected DayOfWeekValue(DayOfWeekType val) throws IllegalArgumentException {
        super(val);
    }

    @Override
    public String printXML() {
        DayOfWeekType type = this.getUnderlyingValue();

        StringBuilder sb = new StringBuilder();
        sb.append(type.getDayOfWeek().getValue());
        sb.append(type.isPositiveShift() ? "+" : "-");
        sb.append(type.getHours());
        sb.append(type.getMinutes());

        return sb.toString();
    }

    @Override
    public Map<QName, String> getXmlAttributes() {
        return new HashMap<QName, String>();
    }

    public static final class Factory extends BaseAttributeValueFactory<DayOfWeekValue> {
        public Factory () {
            super(TYPE);
        }

        @Override
        public DayOfWeekValue getInstance(List<Serializable> content,
                                          Map<QName, String> otherAttributes,
                                          XPathCompiler xPathCompiler) throws IllegalArgumentException {
            System.out.println(content.toString());
            return new DayOfWeekValue(new DayOfWeekType());
        }
    }
}
