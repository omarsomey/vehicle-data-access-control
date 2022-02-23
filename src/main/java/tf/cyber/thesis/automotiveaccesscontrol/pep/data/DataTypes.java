package tf.cyber.thesis.automotiveaccesscontrol.pep.data;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public final class DataTypes {
    public final static Map<Class<?>, String> dataTypes = Map.of(
            String.class, "http://www.w3.org/2001/XMLSchema#string",
            Boolean.class, "http://www.w3.org/2001/XMLSchema#boolean",
            Integer.class, "http://www.w3.org/2001/XMLSchema#integer",
            Double.class, "http://www.w3.org/2001/XMLSchema#double",
            LocalTime.class, "http://www.w3.org/2001/XMLSchema#time",
            LocalDate.class, "http://www.w3.org/2001/XMLSchema#date",
            LocalDateTime.class, "http://www.w3.org/2001/XMLSchema#dateTime",
            URI.class, "http://www.w3.org/2001/XMLSchema#anyURI"
    );

    private DataTypes() {
        // do not instantiate.
    }

    public static String map(Object o) {
        if (dataTypes.containsKey(o.getClass())) {
            return dataTypes.get(o.getClass());
        } else {
            throw new UnsupportedDataTypeException();
        }
    }
}
