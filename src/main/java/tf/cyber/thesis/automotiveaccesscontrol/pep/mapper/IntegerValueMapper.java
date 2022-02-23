package tf.cyber.thesis.automotiveaccesscontrol.pep.mapper;

public class IntegerValueMapper implements XACMLValueMapper<Integer>{
    @Override
    public String map(Integer obj) {
        return obj.toString();
    }
}
