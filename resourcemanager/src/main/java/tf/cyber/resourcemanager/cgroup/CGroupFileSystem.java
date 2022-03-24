package tf.cyber.resourcemanager.cgroup;

import tf.cyber.resourcemanager.cgroup.controllers.CGroupCPU;
import tf.cyber.resourcemanager.cgroup.controllers.CGroupIO;
import tf.cyber.resourcemanager.cgroup.controllers.CGroupMemory;

public interface CGroupFileSystem {
     CGroupCPU getCPUManager();
     CGroupMemory getMemoryManager();
     CGroupIO getIOManager();
}
