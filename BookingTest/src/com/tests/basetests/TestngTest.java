package com.tests.basetests;

import org.testng.Assert;

public class TestngTest {
	/**
	 * Testcase for TestNg framework to check if Assert works correctly. This
	 * testcase should pass always
	 */
	@org.testng.annotations.Test(groups = "testng")
	public void shouldAlwaysPass() {
		Assert.assertTrue(true, "this TestCase is always passed");
	}

	/**
	 * Testcase for TestNg framework to check if throwing new exceptions works
	 * correctly. This testcase should pass always
	 */
	@org.testng.annotations.Test(groups = "testng", expectedExceptions = NullPointerException.class)
	public void nullPointerExseptionShouldPass() {
		throw new NullPointerException();
	}

}