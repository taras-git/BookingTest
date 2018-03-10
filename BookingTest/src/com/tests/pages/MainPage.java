package com.tests.pages;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.tests.pages.basepage.BasePage;
import com.tests.utils.Utils;

public class MainPage extends BasePage {

	public MainPage(WebDriver driver) {
		super(driver);
	}

	String iniFile = "/resources/config/testStation.ini";

	/** Url for the main page. */
	String baseUrl = Utils.getIniFileValue("base.url", iniFile);

	By languageButton = By.xpath("//a[@data-title=\"Select your language\"]");
	By currencyButton = By.xpath("//a[@data-title=\"Choose your currency\"]");
	By checkIn = By.xpath("//div[@data-placeholder=\"Check-in Date\"]");
	By checkOut = By.xpath("//div[@data-placeholder=\"Check-out Date\"]");
	By destinationInput = By.xpath("//input[@aria-label=\"Type your destination\"]");
	By adultsCount = By.xpath("//select[@aria-label=\"Number of adults\"]");
	By childrenCount = By.xpath("//select[@aria-label=\"Number of children\"]");
	By childAge = By.xpath("//select[@aria-label=\"Child 1 age\"]");
	By roomsCount = By.xpath("//select[@aria-label=\"Number of rooms\"]");
	By travelingForWork = By.xpath("//input[@value=\"business\"]");
	By searchButton = By.xpath("//button[@type=\"submit\"]");

	/**
	 * Maximize the browser window, set url, and open this url
	 */
	public void openMainPage() {
		maximizeBrowserScreen();
		driver.get(baseUrl);
	}

	private void clickCurrency() {
		waitForElement(currencyButton).click();
	}

	public void chooseCurrency(String currency) {
		clickCurrency();
		By curr = By.xpath("(//a[@data-currency=\"" + currency + "\"])");
		waitForElement(curr).click();
	}

	public void chooseLanguage(String lang) {
		if (isLanguage(lang)) {
			Utils.print("Language: '" + lang + "' already set");
		} else {
			waitForElement(languageButton).click();

			try {
				getElementByTextAndClass(lang).click();
			} catch (NullPointerException e) {
				Utils.print("Element with text: " + lang + " was not found!!!");
			}
		}
	}

	public boolean isLanguage(String lang) {
		String mainPageLanguage = waitForElement(languageButton).getAttribute("aria-label");
		if (mainPageLanguage.contains(lang)) {
			return true;
		}
		return false;
	}

	public void chooseDestination(String whereToGo) {
		sendText(whereToGo, destinationInput);
	}

	public void checkIn(int dayNumber) {
		clickElement(checkIn);
		clickDate(dayNumber);
	}

	public void checkOut(int dayNumber) {
		clickElement(checkOut);
		clickDate(dayNumber);
	}

	private void clickDate(int dayNumber) {
		// date picker table
		WebElement datePicker = driver.findElement(By.xpath(".//div[@class='c2-month']/table/tbody"));
		// columns of date picker table
		List<WebElement> columns = datePicker.findElements(By.tagName("td"));

		// find cell with desired day
		for (WebElement day : columns) {
			// select desired date
			if (day.getText().equals(new Integer(dayNumber).toString())) {
				day.click();
				break;
			}
		}
	}

	public int getLastDayOfCurrentMonth() {
		LocalDate lastDayofCurrentMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		Utils.print("Last day of month: " + lastDayofCurrentMonth.toString());
		int dayNumber = lastDayofCurrentMonth.getDayOfMonth();
		Utils.print("Day number: " + dayNumber);
		return dayNumber;
	}

	public int getFirstDayOfNextMonth() {
		LocalDate firstDayofNextMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
		Utils.print("First day of month: " + firstDayofNextMonth.toString());
		int dayNumber = firstDayofNextMonth.getDayOfMonth();
		Utils.print("Day number: " + dayNumber);
		return dayNumber;
	}

	public void chooseAdults(String num) {
		String prop = " adult";
		// TODO check adult/adults
		selectItem(adultsCount, num, prop);
	}

	public void chooseChildren(String num) {
		String prop = " child";
		// TODO check child/children
		selectItem(childrenCount, num, prop);
	}

	public void chooseChildAge(String num) {
		String prop = " years old";
		// TODO check year/years
		selectItem(childAge, num, prop);
	}

	public void chooseRooms(String num) {
		String prop = " rooms";
		// TODO check room/rooms
		selectItem(roomsCount, num, prop);
	}

	public void checkTravelingForWork(String isTravelingForWork) {
		if (Boolean.parseBoolean(isTravelingForWork)) {
			clickElement(travelingForWork);
		}
	}

	private void selectItem(By elem, String num, String prop) {
		new Select(driver.findElement(elem)).selectByVisibleText(num + prop);
	}

	public void clickSearch() {
		clickElement(searchButton);
	}

}
