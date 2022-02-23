package tf.cyber.thesis.automotiveaccesscontrol.pep.mapper;

import java.net.URI;

public class URIValueMapper implements XACMLValueMapper<URI>{
    @Override
    public String map(URI obj) {
        return obj.toString();
    }
}
