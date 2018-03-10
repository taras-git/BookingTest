package com.tests.basetests;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.tests.pages.MainPage;
import com.tests.pages.SearchResultsPage;
import com.tests.utils.Utils;

public class BaseTestCase {

	protected WebDriver driver;

	public final String FIREFOX = "firefox";
	public final String CHROME = "chrome";

	/** The ini file with values for current class. */
	String iniFile = "/resources/config/testStation.ini";

	/** The value of implicitly wait seconds, until webelement found */
	String waitSeconds = Utils.getIniFileValue("implicitly.wait.seconds", iniFile);

	public MainPage mainPage;
	public SearchResultsPage searchResultsPage;

	public void initWebPages() {
		mainPage = new MainPage(driver);
		searchResultsPage = new SearchResultsPage(driver);
	}

	/**
	 * Quit all open Webdrivers windows.
	 */
	public void quit() {
		driver.quit();
	}

	/**
	 * Close current Webdriver window
	 */
	public void close() {
		driver.close();
	}

	/**
	 * Inits the Firefox Webdriver and sets the value of implicitly wait seconds.
	 * 
	 * @param browser
	 *            TODO
	 *
	 * @return the Webdriver
	 */
	public WebDriver initDriver(String browser) {
		switch (browser) {

		case FIREFOX:
			driver = getFirefoxDriver();
			break;

		case CHROME:
			driver = getChromeDriver();
			break;

		default:
			driver = getFirefoxDriver();
			break;
		}

		driver.manage().timeouts().implicitlyWait(Integer.parseInt(waitSeconds), TimeUnit.SECONDS);
		return driver;
	}

	private WebDriver getFirefoxDriver() {
		System.setProperty("webdriver.gecko.driver", //
				System.getProperty("user.dir") + getPathToDriver("geckodriver"));

		FirefoxProfile ffprofile = new FirefoxProfile();

		// disable push notifications
		ffprofile.setPreference("dom.webnotifications.enabled", false);
		ffprofile.setPreference("geo.prompt.testing", false);
		ffprofile.setPreference("geo.prompt.testing.allow", false);
		ffprofile.setPreference("geo.enabled", false);

		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(ffprofile);

		WebDriver driver = new FirefoxDriver(options.toCapabilities());
		return driver;
	}

	private WebDriver getChromeDriver() {
		System.setProperty("webdriver.chrome.driver", //
				System.getProperty("user.dir") + getPathToDriver("chromedriver"));

		// disable push notifications
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");

		WebDriver driver = new ChromeDriver(options);
		return driver;
	}

	private static String getPathToDriver(String driverName) {
		if (isUnix()) {
			return "/resources/driver/nix/" + driverName;
		}
		if (isMac()) {
			return "/resources/driver/mac/" + driverName;
		}
		if (isWindows()) {
			return "/resources/driver/win/" + driverName + ".exe";
		}

		return null;
	}

	private static String getOsName() {
		return System.getProperty("os.name").toLowerCase();
	}

	private static boolean isWindows() {
		return (getOsName().indexOf("win") >= 0);
	}

	private static boolean isUnix() {
		return (getOsName().indexOf("nix") >= 0 //
				|| getOsName().indexOf("nux") >= 0 //
				|| getOsName().indexOf("aix") > 0);
	}

	private static boolean isMac() {
		return (getOsName().indexOf("mac") >= 0);
	}

	public void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectationLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver)//
						.executeScript("return document.readyState")//
						.toString()//
						.equals("complete");
			}
		};
		try {
			Thread.sleep(250);
			WebDriverWait waitForLoad = new WebDriverWait(driver, 60);
			waitForLoad.until(expectationLoad);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}

	public void waitForAjaxFinished() {
		ExpectedCondition<Boolean> expectationAjax = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0"));
			}
		};
		try {
			Thread.sleep(250);
			WebDriverWait waitForAjax = new WebDriverWait(driver, 60);
			waitForAjax.until(expectationAjax);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Ajax Finished to complete.");
		}
	}

	protected void openMainPage() {
		mainPage.openMainPage();
		waitForPageLoaded();
	}

	protected String getValue(String value, String propertyFile) {
		return Utils.getIniFileValue(value, propertyFile);
	}
}
