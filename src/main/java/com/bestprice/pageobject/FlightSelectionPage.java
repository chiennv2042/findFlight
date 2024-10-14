package com.bestprice.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightSelectionPage extends BasePage{
    // Locators
    private static final String departLbl = "//input[@name='Depart']";
    private static final String departDay = "//td[@data-month='%s']//span[text()= '%s' and @class='ui-datepicker-day ']";
    private static final String returnLbl = "//input[@name='Return']";
    private static final String returnDay = "//td[@data-month='%s']//span[text()= '%s' and @class='ui-datepicker-day ']";
    private static final String passengerLbl = "//input[@id='flight_passenger']";
    private static final String adultPlusBtn = "(//div[@class='mktnd_btn_flight_adult_plus btn btn-plus btn-plus-0 btn-number'])[2]";
    private static final String adultMinusBtn = "(//div[@class='mktnd_btn_flight_adult_minus btn btn-minus btn-minus-0 btn-number'])[2]";
    private static final String adultCountLbl = "(//input[@class='cr_adults nb_adults form-control text-center input-number'])[3]";
    private static final String childPlusBtn = "(//div[@class='mktnd_btn_children_adult_plus btn btn-plus btn-plus-1 btn-number'])[2]";
    private static final String childMinusBtn = "(//div[@class='mktnd_btn_children_adult_minus btn btn-minus btn-minus-1 btn-number'])[2]";
    private static final String childCountLbl = "(//input[@class='cr_children nb_children form-control text-center input-number'])[3]";
    private static final String infantPlusBtn = "(//div[@class='mktnd_btn_flight_infant_plus btn btn-plus btn-plus-2 btn-number'])[2]";
    private static final String infantMinusBtn = "(//div[@class='mktnd_btn_flight_infant_minus btn btn-minus btn-minus-2 btn-number'])[2]";
    private static final String infantCountLbl = "(//input[@class='cr_infants nb_infants form-control text-center input-number'])[3]";
    // Constructor
    public FlightSelectionPage(WebDriver driver) {
        super(driver);
    }

    // Select dates (departure and return)
    public void selectDepartDate(String departMonth, String departDate) {
        clickOnElement(departLbl);
        String xPathDepartDay = String.format(departDay, departMonth, departDate);
        clickOnElement(xPathDepartDay);
    }

    public void selectReturnDate(String returnMonth, String returnDate) {
        clickOnElement(returnLbl);
        String xPathReturnDay = String.format(returnDay, returnMonth, returnDate);
        clickOnElement(xPathReturnDay);
    }

    // Select number of passengers
    public void selectPassengersQuantity(int adults, int children, int infants) {
        clickOnElement(passengerLbl);
        // Locate and set the number of adults
        adjustCountByValue(adultPlusBtn, adultMinusBtn, adultCountLbl, adults);
        // Locate and set the number of children
        adjustCountByValue(childPlusBtn, childMinusBtn, childCountLbl, children);
        // Locate and set the number of infants
        adjustCountByValue(infantPlusBtn, infantMinusBtn, infantCountLbl, infants);;
    }

    private void adjustCountByValue(String plusButton, String minusButton, String countElement, int desiredCount) {
        WebElement countLabel = waitForElementVisible(countElement);
        if (countLabel != null) {
            int currentCount = Integer.parseInt(countLabel.getAttribute("value").trim());

            while (currentCount < desiredCount) {
                clickOnElement(plusButton);
                currentCount++;
            }
            while (currentCount > desiredCount) {
                clickOnElement(minusButton);
                currentCount--;
            }
        } else {
            System.out.println("Count element not found: " + countElement);
        }
    }
}
