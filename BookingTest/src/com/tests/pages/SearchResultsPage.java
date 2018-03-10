package com.tests.pages;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import com.tests.pages.basepage.BasePage;
import com.tests.utils.Utils;

public class SearchResultsPage extends BasePage {

	public SearchResultsPage(WebDriver driver) {
		super(driver);
	}

	String iniFile = "/resources/config/testStation.ini";

	public void getHotelPrices() {
		String source = driver.getPageSource();
		Document doc = Jsoup.parse(source);
		Elements hotels = doc.select("div[data-hotelid]");
		Utils.print("");
		Utils.print("Found hotels: " + hotels.size());
		Utils.print("");

		String price = "";
		String hotelScore = "";
		String hotelName = "";

		for (Element hotel : hotels) {
			boolean hasGoodScore = false;
			boolean hasGoodPrice = false;

			hotelScore = hotel.attr("data-score");
			if (!hotelScore.isEmpty() && Float.parseFloat(hotelScore) > 8.0) {
				hasGoodScore = true;
			}

			String hotelPriceInfo = hotel.select(".totalPrice.totalPrice_no-rack-rate").text();
			try {
				price = hotelPriceInfo.substring(//
						hotelPriceInfo.indexOf("€") + 2, //
						hotelPriceInfo.indexOf("€") + 5);
				if (Float.parseFloat(price) < 200.0) {
					hasGoodPrice = true;
				}
			} catch (StringIndexOutOfBoundsException e) {
				// Utils.print(" No Price found");
			}

			if (hasGoodPrice && hasGoodScore) {
				Utils.print("Found hotel with score: " + hotelScore + " and with price: " + price);
				hotelName = hotel.select(".sr-hotel__name").text();
				Utils.print("Hotel: " + hotelName);
				Utils.print("");
				break;
			}
		}
	}
}
