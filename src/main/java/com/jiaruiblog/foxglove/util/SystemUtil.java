package com.jiaruiblog.foxglove.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class SystemUtil {

    private static OperatingSystemMXBean operatingSystemMXBean;

    static {
        operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    /**
     * 获取cpu使用率
     * @return
     */
    public static double getSystemCpuLoad() {
        return operatingSystemMXBean.getSystemCpuLoad();
    }

    /**
     * 获取cpu数量
     * @return
     */
    public static int getSystemCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }

}