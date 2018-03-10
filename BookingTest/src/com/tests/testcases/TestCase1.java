package com.tests.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tests.basetests.BaseTestCase;
import com.tests.utils.Utils;

public class TestCase1 extends BaseTestCase {

	/** The ini file, with values for this testcase */
	String iniFile = "/resources/data/testcases/TestCase1.ini";

	@BeforeMethod
	public void setUp() {
		String browser = getValue("browser", iniFile);
		initDriver(browser);
		initWebPages();
	}

	@AfterMethod
	public void tearDown() {
		quit();
	}

	@Test(dependsOnGroups = "testng")
	public void baseTest() {
		mainPage.openMainPage();

		// check the title of the main page if it is the same as in ini file
		// this check is optional, can be commented out
		String mainPageTitle = getValue("title", iniFile);
		String actualPageTitle = mainPage.getActualTitle();
		Utils.print("Title from ini file:" + mainPageTitle);
		Utils.print("Title from browser :" + actualPageTitle);
		Assert.assertTrue(actualPageTitle.contains(mainPageTitle));

		mainPage.chooseCurrency(getValue("currency", iniFile));
		mainPage.chooseLanguage(getValue("language", iniFile));
		mainPage.chooseDestination(getValue("destination", iniFile));
		mainPage.checkIn(mainPage.getLastDayOfCurrentMonth());
		mainPage.checkOut(mainPage.getFirstDayOfNextMonth());
		mainPage.chooseAdults(getValue("adult", iniFile));
		mainPage.chooseChildren(getValue("child", iniFile));
		mainPage.chooseChildAge(getValue("child_age", iniFile));
		mainPage.chooseRooms(getValue("rooms", iniFile));
		mainPage.checkTravelingForWork(getValue("traveling_for_work", iniFile));

		mainPage.clickSearch();

		waitForPageLoaded();

		searchResultsPage.getHotelPrices();

	}
}
