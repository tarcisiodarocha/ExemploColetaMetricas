package com.example.metrics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.List;

public class MetricsApp {
    public static void main(String[] args) {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        Metrics metrics = new Metrics();

        // CPU
        CentralProcessor cpu = hal.getProcessor();
        CentralProcessor.ProcessorIdentifier pid = cpu.getProcessorIdentifier();
        metrics.cpu.model = pid.getName();
        metrics.cpu.logicalProcessorCount = cpu.getLogicalProcessorCount();
        metrics.cpu.physicalPackageCount = cpu.getPhysicalPackageCount();

        double[] load = cpu.getSystemCpuLoadTicks();
        // Note: CPU load ticks returns cumulative values; for a simple sample we use getSystemCpuLoadBetweenTicks
        metrics.cpu.systemLoad = cpu.getSystemCpuLoadBetweenTicks() * 100.0;

        // Memory
        GlobalMemory memory = hal.getMemory();
        metrics.memory.total = memory.getTotal();
        metrics.memory.available = memory.getAvailable();
        metrics.memory.used = metrics.memory.total - metrics.memory.available;

        // Disks
        HWDiskStore[] diskStores = hal.getDiskStores();
        for (HWDiskStore ds : diskStores) {
            Metrics.Disk d = new Metrics.Disk();
            d.name = ds.getName();
            d.model = ds.getModel();
            d.size = ds.getSize();
            d.reads = ds.getReads();
            d.writes = ds.getWrites();
            metrics.disks.add(d);
        }

        // File system (mounts)
        FileSystem fs = os.getFileSystem();
        for (OSFileStore fsStore : fs.getFileStores()) {
            Metrics.Mount m = new Metrics.Mount();
            m.name = fsStore.getName();
            m.mountPoint = fsStore.getMount();
            m.totalSpace = fsStore.getTotalSpace();
            m.freeSpace = fsStore.getUsableSpace();
            metrics.mounts.add(m);
        }

        // Network interfaces
        NetworkIF[] nifs = hal.getNetworkIFs();
        for (NetworkIF nif : nifs) {
            Metrics.NetIf ni = new Metrics.NetIf();
            ni.name = nif.getName();
            ni.displayName = nif.getDisplayName();
            ni.mac = nif.getMacaddr();
            ni.rxBytes = nif.getBytesRecv();
            ni.txBytes = nif.getBytesSent();
            metrics.netIfs.add(ni);
        }

        // Basic OS info
        metrics.os.family = os.getFamily();
        metrics.os.version = os.getVersionInfo().getVersion();
        metrics.os.name = os.getVersionInfo().getCodeName();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(metrics);
        System.out.println(json);
    }
}
