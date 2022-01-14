package tf.cyber.thesis.automotiveaccesscontrol.repository;

import org.springframework.data.repository.CrudRepository;
import tf.cyber.thesis.automotiveaccesscontrol.entity.AccessLog;

public interface AccessLogRepository extends CrudRepository <AccessLog, Long> {
}
