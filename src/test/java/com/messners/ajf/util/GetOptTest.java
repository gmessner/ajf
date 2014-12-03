package com.messners.ajf.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class GetOptTest {

	@Test
	public void testOptArg() {
		
		GetOpt getOpt = new GetOpt("abf:");
		String[] args = { "-f", "Test.txt", "-a" };
		String filename = null;
		boolean aIsSet = false;
		boolean bIsSet = false;
		for (int c = getOpt.getOpt(args); c != GetOpt.EOF; c = getOpt.getOpt(args)) {
			
			switch (c) {
			case 'a':
				aIsSet = true;
				break;
				
			case 'b':
				bIsSet = true;
				break;
				
			case 'f':
				filename = getOpt.getOptArg();
				break;

			case GetOpt.ERROR:
				fail(getOpt.getErrorMsg());
				break;
			}
		}
		
		assertTrue("failed to accept 'a' option", aIsSet);
		assertFalse("'b' option wrongfully set", bIsSet);
		assertNotNull("option parsing failed", filename);
	}
	
	@Test
	public void testEndingArgument() {
		
		GetOpt getOpt = new GetOpt("");		
		String[] args = { "Test.txt" };
		for (int c = getOpt.getOpt(args); c != GetOpt.EOF; c = getOpt.getOpt(args)) {
			
			switch (c) {
		
			case GetOpt.ERROR:
				fail(getOpt.getErrorMsg());
				break;
				
			default:
				fail("not a valid option: " + (char)c);
			}
		}
		
		String filename = null;
		if (getOpt.getOptIndex() < args.length) {
			filename = args[getOpt.getOptIndex()];
		}
	
		assertEquals(args[0], filename);
	}
}
