package org.alopex.ragnode.util;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServerConnection;

import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class SysRes {

	public static double load() {
		try {
			MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
			OperatingSystemMXBean osMBean 
				= ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, 
														   OperatingSystemMXBean.class);
			return osMBean.getSystemCpuLoad();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}
	
	//TODO: processor count detection as to report -> front-end
}
