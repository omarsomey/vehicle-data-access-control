package tf.cyber.thesis.automotiveaccesscontrol.pep.obligation;

import tf.cyber.thesis.automotiveaccesscontrol.pep.data.XACMLAttribute;

import java.util.List;

public class LogObligation extends Obligation{
    @Override
    public void execute(List<XACMLAttribute<?>> args) {
        for (XACMLAttribute<?> arg : args) {
            System.out.println(arg);
        }
    }
}
