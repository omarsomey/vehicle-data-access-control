package tf.cyber.thesis.automotiveaccesscontrol.util;

import java.util.Map;

public interface StickyDocument {

    void writer(String xmlPolicyPath, String ciphertext, String destinationPath);
}
