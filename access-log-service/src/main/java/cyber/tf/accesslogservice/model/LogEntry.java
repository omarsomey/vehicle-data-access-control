package cyber.tf.accesslogservice.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "logs")
public class LogEntry extends BaseEntity {
    public String subject;
    public String action;
    public String resource;

    public long time;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
