package tf.cyber.resourcemanager.cgroup.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CGroupCPU {
    public static final String CPU_IDLE = "cpu.idle";
    public static final String CPU_MAX = "cpu.max";
    public static final String CPU_MAX_BURST = "cpu.max.burst";
    public static final String CPU_PRESSURE = "cpu.pressure";
    public static final String CPU_STAT = "cpu.stat";
    public static final String CPU_UCLAMP_MAX = "cpu.uclamp.max";
    public static final String CPU_UCLAMP_MIN = "cpu.uclamp.min";
    public static final String CPU_WEIGHT = "cpu.weight";
    public static final String CPU_WEIGHT_NICE = "cpu.nice";

    private Path cgroupPath;

    public CGroupCPU(Path cgroupPath) {
        cgroupPath = cgroupPath;
    }

    public String getCpuIdle() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_IDLE));
    }

    public String getCpuMax() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_MAX));
    }

    public String getCpuMaxBurst() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_MAX_BURST));
    }

    public String getCpuPressure() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_PRESSURE));
    }

    public String getCpuStat() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_STAT));
    }

    public String getCpuUclampMax() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_UCLAMP_MAX));
    }

    public String getCpuUclampMin() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_UCLAMP_MIN));
    }

    public String getCpuWeight() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_WEIGHT));
    }

    public String getCpuWeightNice() throws IOException {
        return Files.readString(Paths.get(cgroupPath.toString(), CPU_WEIGHT_NICE));
    }

    public void setCpuNice(int niceValue) throws IOException {
        if (niceValue < -20 || niceValue > 19) {
            throw new IllegalArgumentException("Nice value must be between -20 and +19.");
        }

        Files.writeString(Paths.get(cgroupPath.toString(), CPU_WEIGHT_NICE),
                          Integer.toString(niceValue),
                          StandardOpenOption.WRITE);
    }

    public void setCpuMax(int time, int fraction) throws IOException {
        if (time < 0 || fraction < 0) {
            throw new IllegalArgumentException("Time and fraction must not be negative.");
        }

        Files.writeString(Paths.get(cgroupPath.toString(), CPU_MAX),
                          Integer.toString(time) + " " + Integer.toString(fraction),
                          StandardOpenOption.WRITE);
    }
}
