package tf.cyber.resourcemanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tf.cyber.resourcemanager.cgroup.CGroupFileSystem;

@Service
public class MemoryService {
    @Autowired
    CGroupFileSystem cGroupFileSystem;
}
