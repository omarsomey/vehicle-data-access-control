package tf.cyber.thesis.automotiveaccesscontrol.pep.mapper;

public class DoubleValueMapper implements XACMLValueMapper<Double>{
    @Override
    public String map(Double obj) {
        return obj.toString();
    }
}
