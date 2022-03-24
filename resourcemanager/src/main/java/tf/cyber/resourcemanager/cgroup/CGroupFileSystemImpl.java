package tf.cyber.resourcemanager.cgroup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CGroupFileSystemImpl implements CGroupFileSystem {
    private CGroupCPU cGroupCPU;
    private CGroupMemory cGroupMemory;
    private CGroupIO cGroupIO;

    public CGroupFileSystemImpl(Path cgroupfs) {
        if (cgroupfs == null) {
            throw new IllegalArgumentException("No cgroupfs path specified. Check cgroup.fs.homedir " +
                                                       "in application.properties.");
        }

        if (!Files.isDirectory(cgroupfs)) {
            throw new IllegalArgumentException("Invalid cgroup path specified.");
        }

        if (Files.exists(Paths.get(cgroupfs.toString(), "cgroup.controllers"))) {
            throw new IllegalArgumentException("Could not find cgroup.controllers file." +
                                                       "Check your cgroup.fs.homedir in" +
                                                       "application.properties.");
        }

        if (!Files.isReadable(cgroupfs)) {
            throw new IllegalArgumentException("cgroupfs is not readable. Check your configuration" +
                                                       "in /etc/cgconfig.conf.");
        }

        if (!Files.isWritable(cgroupfs)) {
            throw new IllegalArgumentException("cgroupfs is not writable. Check your configuration" +
                                                       "in /etc/cgconfig.conf.");
        }

        this.cGroupCPU = new CGroupCPU();
        this.cGroupMemory = new CGroupMemory();
        this.cGroupIO = new CGroupIO();
    }

    public CGroupCPU getcGroupCPU() {
        return cGroupCPU;
    }

    public CGroupMemory getcGroupMemory() {
        return cGroupMemory;
    }

    public CGroupIO getcGroupIO() {
        return cGroupIO;
    }
}
