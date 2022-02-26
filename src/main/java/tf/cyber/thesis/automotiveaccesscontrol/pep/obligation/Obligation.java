package tf.cyber.thesis.automotiveaccesscontrol.pep.obligation;

import tf.cyber.thesis.automotiveaccesscontrol.pep.data.XACMLAttribute;

import java.util.List;

public abstract class Obligation {
    public abstract void execute(List<XACMLAttribute<?>> args);
}
