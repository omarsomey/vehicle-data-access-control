package tf.cyber.resourcemanager.cgroup.controllers;

import java.nio.file.Path;

public class CGroupMemory {
    private Path cgroupPath;

    public CGroupMemory(Path cgroupPath) {
        this.cgroupPath = cgroupPath;
    }
}
