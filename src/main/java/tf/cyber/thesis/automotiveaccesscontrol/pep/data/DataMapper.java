package tf.cyber.thesis.automotiveaccesscontrol.pep.data;

import tf.cyber.thesis.automotiveaccesscontrol.pep.mapper.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class DataMapper {
    public final static Map<Class<?>, XACMLValueMapper> dataMapper = Map.of(
            String.class, new StringValueMapper(),
            Boolean.class, new BooleanValueMapper(),
            Integer.class, new IntegerValueMapper(),
            Double.class, new DoubleValueMapper(),
            LocalTime.class, new LocalTimeValueMapper(),
            LocalDate.class, new LocalDateValueMapper(),
            LocalDateTime.class, new LocalDateTimeValueMapper(),
            URI.class, new URIValueMapper()
    );

    private DataMapper() {
        // do not instantiate.
    }

    public static String map(Object o) {
        if (dataMapper.containsKey(o.getClass())) {
            return dataMapper.get(o.getClass()).map(o);
        } else {
            throw new UnsupportedDataTypeException();
        }
    }
}
