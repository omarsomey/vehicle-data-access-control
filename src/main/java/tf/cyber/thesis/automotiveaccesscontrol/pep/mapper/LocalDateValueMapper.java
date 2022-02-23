package tf.cyber.thesis.automotiveaccesscontrol.pep.mapper;

import java.time.LocalDate;

public class LocalDateValueMapper implements XACMLValueMapper<LocalDate>{
    @Override
    public String map(LocalDate obj) {
        return obj.toString();
    }
}
