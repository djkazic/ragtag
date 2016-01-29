package org.alopex.ragnode.module;

import org.alopex.ragnode.core.Utilities;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.jar.Pack200;

import static org.junit.Assert.*;

/**
 * @author Kevin Cai on 1/28/2016.
 */
public class JobTest {

	private static ArrayList<String> testData;
	private static File testFile;
	private static Job testJob;

	@BeforeClass
	public static void testSetup() {
		try {
			testData = new ArrayList<String> ();
			testData.add("a");
			testData.add("b");
			testData.add("c");
			testFile = File.createTempFile("tmp-test", "job");
			FileOutputStream fos = new FileOutputStream(testFile);
			for(int i=0; i < 300; i++) {
				fos.write(i);
				fos.flush();
			}
			fos.close();
			testJob = new Job(testData, testFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@AfterClass
	public static void afterClass() {
		testFile.delete();
	}

	@Test
	public void testDataSet() throws Exception {
		assertEquals(testData, testJob.dataSet());
	}

	@Test
	public void testIsRunning() throws Exception {
		assertEquals(false, testJob.isRunning());
	}

	@Test
	public void testIsValid() throws Exception {
		assertFalse(testJob.isValid());
	}

	@Test
	public void testEquals() throws Exception {
		ArrayList<String> testData = new ArrayList<String> ();
		testData.add("a");
		testData.add("b");
		testData.add("c");
		Job testJobTwo = new Job(testData, File.createTempFile("tmp-test", "equals"));
		assertFalse(testJob.equals(testJobTwo));
	}

	@Test
	public void testGetID() throws Exception {
		assertEquals(40, testJob.getID().length());
	}
}