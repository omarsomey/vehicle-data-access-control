package tf.cyber.thesis.automotiveaccesscontrol.pep.mapper;

import java.time.LocalDateTime;

public class LocalDateTimeValueMapper implements XACMLValueMapper<LocalDateTime>{
    @Override
    public String map(LocalDateTime obj) {
        return obj.toString();
    }
}
