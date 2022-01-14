package tf.cyber.thesis.automotiveaccesscontrol.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Time;

@Entity
@Getter
@Setter
public class AccessLog extends BaseEntity {
    String user;
    String resource;
    Time accessTime;
}
