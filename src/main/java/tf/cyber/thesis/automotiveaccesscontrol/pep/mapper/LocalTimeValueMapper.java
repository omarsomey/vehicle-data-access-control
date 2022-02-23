package tf.cyber.thesis.automotiveaccesscontrol.pep.mapper;

import java.time.LocalTime;

public class LocalTimeValueMapper implements XACMLValueMapper<LocalTime>{
    @Override
    public String map(LocalTime obj) {
        return obj.toString();
    }
}
