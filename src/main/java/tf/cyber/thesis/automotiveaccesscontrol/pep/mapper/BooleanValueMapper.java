package tf.cyber.thesis.automotiveaccesscontrol.pep.mapper;

public class BooleanValueMapper implements XACMLValueMapper<Boolean>{
    @Override
    public String map(Boolean obj) {
        return obj.toString();
    }
}
