package tf.cyber.thesis.automotiveaccesscontrol.pep.obligation;

import java.util.Map;

public class ObligationService {
    private final static Map<String, Obligation> obligations = Map.of(
            "LogObligation", new LogObligation()
    );

    public static Obligation get(String id) {
        if (!obligations.containsKey(id)) {
            throw new ObligationNotImplementedException();
        }

        return obligations.get(id);
    }
}
