package org.alopex.ragnode;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import javax.management.MBeanServerConnection;

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
}
