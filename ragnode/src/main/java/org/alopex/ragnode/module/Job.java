package org.alopex.ragnode.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

import org.alopex.ragnode.Utilities;

public class Job {

	private String ext;
	private boolean running;
	private byte[] fileBytes;
	private ArrayList<String> data;
	
	public Job() { }
	
	public Job(ArrayList<String> data, File binary) {
		if(data.size() > 0 && binary != null && binary.exists()) {
			try {
				byte[] potentialBytes;
				if((potentialBytes = Files.readAllBytes(binary.toPath())).length > 0) {
					fileBytes = potentialBytes;
					int ind = binary.getName().lastIndexOf(".");
					if(ind > 0) {
						ext = binary.getName().substring(ind + 1);
					} else {
						return;
					}
				} else {
					return;
				}
				this.data = data;
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
		} else {
			Utilities.log(this, "Failure to create job: bad data or bad process method specified", false);
			//TODO: Throw
			return;
		}
	}
	
	/**
	 * Executes, using a Runtime object, the binary provided
	 * @return an Object[long runtime, String json_output]
	 */
	public final Object[] execute() {		
		try {
			String prefix = "";
			File tempBinary = File.createTempFile("tmp-bin", "." + ext);
			if(ext.equals("jar")) {
				prefix = System.getProperty("java.home") + "\\bin\\java.exe\" -jar ";
			}
			FileOutputStream fos = new FileOutputStream(tempBinary);
			Utilities.log(this, "Loading temp binary of size: " + fileBytes.length, false);
			fos.write(fileBytes);
			fos.close();
			Utilities.log(this, "Temporary binary constructed, size: " + tempBinary.length(), false);
			Utilities.log(this, "Assembling input string...", false);
			StringBuilder sb = new StringBuilder();
			for(int i=0; i < data.size(); i++) {
				sb.append(data.get(i) + " ");
			}
			String input = sb.toString().replaceAll("^\\s+|\\s+$", "");
			Utilities.log(this, "\t" + prefix + tempBinary.getPath() + " " + input, false);
			long start = System.nanoTime();
			//RUNTIME EXECUTE
			Process ps = new ProcessBuilder(prefix + tempBinary.getPath() + " " + input).start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ps.getInputStream()));
			int read;
		    char[] buffer = new char[4096];
		    StringBuffer output = new StringBuffer();
		    while((read = reader.read(buffer)) > 0) {
		        output.append(buffer, 0, read);
		    }
		    reader.close();
		    ps.waitFor();
			long end = System.nanoTime();
			return new Object[] {end - start, output.toString()};
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> dataSet() {
		return data;
	}
	
	public boolean isRunning() {
		return running;
	}
}