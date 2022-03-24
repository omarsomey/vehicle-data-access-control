package tf.cyber.resourcemanager.cgroup.controllers;

import java.nio.file.Path;

public class CGroupIO {
    private Path cgroupPath;

    public CGroupIO(Path cgroupPath) {
        this.cgroupPath = cgroupPath;
    }
}
