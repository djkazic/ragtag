package org.alopex.ragtag;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.management.MBeanServerConnection;

import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class SysRes {
	
	public static double diskSpace() {
		for(Path root : FileSystems.getDefault().getRootDirectories()) {
		    try {
		        FileStore store = Files.getFileStore(root);
		        return (double) store.getUsableSpace() / store.getTotalSpace();
		    } catch (IOException ex) {
		    	ex.printStackTrace();
		    }
		}
		return -1;
	}

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
