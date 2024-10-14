package com.bestprice.pageobject;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    // Locators
    private static final String startFromTxt = "//input[@name='From']";
    private static final String destinationPlaceTxt = "//input[@name='To']";
    private static final String searchBtn = "//div/button[@onclick='return validate_flight_search()']";
    private static final String flightFromSearchBox = "//span/input[@data-id='flight_from']";
    private static final String flightToSearchBox = "//span/input[@data-id='flight_to']";
    private static final String firstSuggestion = "//*[contains(text(),'Sân bay')]";

    // Constructor
    public HomePage(WebDriver driver) {
        super(driver);  // Calls BasePage's constructor
    }

    // Actions
    public void setFromAirport(String fromAirportCode) {
        clickOnElement(startFromTxt);
        waitForElementVisible(flightFromSearchBox);
        sendKeyOnElement(flightFromSearchBox, fromAirportCode);
        waitForElementVisible(firstSuggestion);
        clickElementWithJS(firstSuggestion);
    }

    public void setToAirport(String toAirportCode) {
        clickOnElement(destinationPlaceTxt);
        waitForElementVisible(flightToSearchBox);
        sendKeyOnElement(flightToSearchBox, toAirportCode);
        waitForElementVisible(firstSuggestion);
        clickElementWithJS(firstSuggestion);
    }

    public void clickSearchBtn() {
        clickOnElement(searchBtn);
        try {
            Thread.sleep(1000);//waiting for searching
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
