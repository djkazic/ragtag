package org.alopex.ragtag.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

import org.alopex.ragtag.core.RagCore;
import org.alopex.ragtag.core.Utilities;

public class Job {

	private String id;
	private String ext;
	private boolean running;
	private byte[] fileBytes;
	private ArrayList<String> data;
	private boolean valid = true;

	public Job() { }

	public Job(ArrayList<String> data, File binary) {
		if(data.size() > 0 && binary != null && binary.exists()) {
			try {
				byte[] potentialBytes;
				if((potentialBytes = Files.readAllBytes(binary.toPath())).length > 0) {
					fileBytes = potentialBytes;
					this.id = Utilities.SHAsum(potentialBytes);
					int ind = binary.getName().lastIndexOf(".");
					if(ind > 0) {
						ext = binary.getName().substring(ind + 1);
					} else {
						valid = false;
						return;
					}
				} else {
					return;
				}
				this.data = data;
				if(!RagCore.jobList.contains(this)) {
					RagCore.jobList.add(this);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				valid = false;
				return;
			}
		} else {
			Utilities.log(this, "Failure to create job: bad data or bad process method specified", false);
			valid = false;
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
			File binaryCacheDir = new File("cache-bin");
			if(!binaryCacheDir.exists()) {
				binaryCacheDir.mkdirs();
			}
			File tempBinary = new File(binaryCacheDir.getAbsolutePath() + "/" + id + "." + ext);
			if(ext.equals("jar")) {
				prefix = System.getProperty("java.home") + "\\bin\\java.exe\" -jar ";
			}
			if(fileBytes != null) {
				FileOutputStream fos = new FileOutputStream(tempBinary);
				Utilities.log(this, "Loading temp binary of size: " + fileBytes.length, false);
				fos.write(fileBytes);
				fos.close();
				Utilities.log(this, "Temporary binary constructed, size: " + tempBinary.length(), false);
			} else {
				Utilities.log(this, "Attempting to look for cached binary...", false);
				if(tempBinary.exists()) {
					Utilities.log(this, "Found cached binary: " + tempBinary, false);
					//TODO: security check on bytes
				}
			}
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

	public boolean isValid() { return valid; }

	public static ArrayList<Job> getJobs() {
		return RagCore.jobList;
	}

	public boolean equals(Job other) {
		return id.equals(other);
	}
	
	public String getID() {
		return id;
	}
	
	public void wipeBinary() {
		fileBytes = null;
	}
}