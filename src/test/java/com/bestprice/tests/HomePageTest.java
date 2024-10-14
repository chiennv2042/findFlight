package com.bestprice.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.bestprice.pageobject.BasePage;
import com.bestprice.pageobject.FlightSelectionPage;
import com.bestprice.pageobject.HomePage;
import com.bestprice.pageobject.SearchResultPage;

import java.io.IOException;

public class HomePageTest extends BaseTest {

    HomePage homePage;
    FlightSelectionPage flightSelectionPage;
    SearchResultPage searchResultPage;
    BasePage basePage;

    @Test
    public void findAvailableFlightAndPrintOut() throws IOException {
        // Instantiate page objects
        homePage = new HomePage(driver);
        flightSelectionPage = new FlightSelectionPage(driver);
        searchResultPage = new SearchResultPage(driver);
        basePage = new BasePage(driver);

        // Select from and to locations
        String from = "han";
        String to = "sgn";
        homePage.setFromAirport(from);
        homePage.setToAirport(to);

        // Select departure and return dates (in BestPrice, input your expected month + 1)
        //Start on 18/10 and return on 25/10
        flightSelectionPage.selectDepartDate("9", "18");
        flightSelectionPage.selectReturnDate("9", "25");

        // Select passengers
        flightSelectionPage.selectPassengersQuantity(1, 1, 1);

        // Click search button
        homePage.clickSearchBtn();

        String listAvailableTickets = "//div[@class='bpv-list-item-row']";
        Assert.assertTrue(basePage.areElementsDisplayed(listAvailableTickets, 20));

        // Export search results to Excel
        searchResultPage.exportListResultsToExcel(listAvailableTickets,"Flight Results", "flight_results.xlsx");
    }
}
