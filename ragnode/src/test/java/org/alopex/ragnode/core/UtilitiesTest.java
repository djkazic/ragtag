package org.alopex.ragnode.core;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Kevin Cai on 1/28/2016.
 */
public class UtilitiesTest {

	private static String testInput;

	@BeforeClass
	public static void testSetup() {
		testInput = "abc123";
	}

	@Test
	public void testLog() throws Exception {
		Object obj = new Object();
		assertTrue(Utilities.log(obj, testInput, false));
	}

	@Test
	public void testLog1() throws Exception {
		assertTrue(Utilities.log("SomeClass", testInput, false));
	}

	@Test
	public void testBase64() throws Exception {
		assertEquals("YWJjMTIz", Utilities.base64(testInput));
	}

	@Test
	public void testSHAsum() throws Exception {
		assertEquals("6367c48dd193d56ea7b0baad25b19455e529f5ee", Utilities.SHAsum(testInput.getBytes()));
	}
}