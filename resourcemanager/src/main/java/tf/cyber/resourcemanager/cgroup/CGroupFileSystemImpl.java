package tf.cyber.resourcemanager.cgroup;

import tf.cyber.resourcemanager.cgroup.controllers.CGroupCPU;
import tf.cyber.resourcemanager.cgroup.controllers.CGroupIO;
import tf.cyber.resourcemanager.cgroup.controllers.CGroupMemory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CGroupFileSystemImpl implements CGroupFileSystem {
    private CGroupCPU cGroupCPU;
    private CGroupMemory cGroupMemory;
    private CGroupIO cGroupIO;

    public CGroupFileSystemImpl(Path baseCgroupFs, String user) {
        if (baseCgroupFs == null) {
            throw new IllegalArgumentException("No base cgroupfs path specified. Check cgroup.fs.homedir " +
                                                       "in application.properties.");
        }

        Path userFs = Path.of(baseCgroupFs.toString(), user);

        if (!Files.isDirectory(baseCgroupFs)) {
            throw new IllegalArgumentException("Invalid base cgroup path specified.");
        }

        if (!Files.isDirectory(userFs)) {
            throw new IllegalArgumentException("cgroup for user " + user + " not found. Check your" +
                                                       "/etc/cgconfig.conf.");
        }

        if (Files.exists(Paths.get(userFs.toString(), "cgroup.controllers"))) {
            throw new IllegalArgumentException("Could not find cgroup.controllers file." +
                                                       "Check your cgroup configuration.");
        }

        if (!Files.isReadable(userFs)) {
            throw new IllegalArgumentException("cgroupfs is not readable. Check your configuration" +
                                                       "in /etc/cgconfig.conf.");
        }

        if (!Files.isWritable(userFs)) {
            throw new IllegalArgumentException("cgroupfs is not writable. Check your configuration" +
                                                       "in /etc/cgconfig.conf.");
        }

        this.cGroupCPU = new CGroupCPU(userFs);
        this.cGroupMemory = new CGroupMemory(userFs);
        this.cGroupIO = new CGroupIO(userFs);
    }

    @Override
    public CGroupCPU getCPUManager() {
        return cGroupCPU;
    }

    @Override
    public CGroupMemory getMemoryManager() {
        return cGroupMemory;
    }

    @Override
    public CGroupIO getIOManager() {
        return cGroupIO;
    }
}
