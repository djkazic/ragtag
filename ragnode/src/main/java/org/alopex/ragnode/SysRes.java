package org.alopex.ragnode;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import javax.management.MBeanServerConnection;

public class SysRes {

	public static double load() {
		try {
			MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
			OperatingSystemMXBean osMBean 
				= ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, 
														   OperatingSystemMXBean.class);
			return osMBean.getSystemLoadAverage();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}
}
