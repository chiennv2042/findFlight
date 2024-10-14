package com.bestprice.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class BasePage {
    Actions action;
    WebElement element;
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor jse;
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);


    // Constructor
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.action = new Actions(driver);
        this.jse = (JavascriptExecutor) driver;
    }

    // Common method for waiting for an element to be visible
    public WebElement waitForElementVisible(String locator) {
        try {
            // Wait until the element becomes visible and return it
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        } catch (Exception e) {
            // Handle the case where the element is not found or not visible
            System.out.println("Element not visible: " + locator);
            return null;
        }
    }

    // Common method for waiting for multiple elements to be visible
    public List<WebElement> waitForElementsVisible(String locator) {
        try {
            // Wait until all elements matching the locator become visible and return them
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(locator)));
        } catch (Exception e) {
            // Handle the case where elements are not found or not visible
            System.out.println("Elements not visible: " + locator);
            return null;
        }
    }

    // Common method for clicking an element with JavaScript
    protected void clickElementWithJS(String locator) {
        WebElement element = driver.findElement(By.xpath(locator));
        jse.executeScript("arguments[0].click();", element); // Use WebElement instead of locator string
    }

    // Generic method for clicking elements
    protected void clickOnElement(String locator) {
        WebElement element = waitForElementVisible(locator);
        if (element != null && element.isEnabled()) {
            action.moveToElement(element).perform();
            element.click();
        } else {
            System.out.println("Unable to click the element: " + locator);
        }
    }

    // Generic method for entering text in input fields
    public void sendKeyOnElement(String locator, String value) {
        WebElement element = waitForElementVisible(locator);
        if (element != null && element.isDisplayed()) {
            try {
                    element.clear();
                    element.sendKeys(value);
            } catch (Exception e) {
                logger.error("Error while sending keys to element: " + locator, e);
            }
        } else {
            logger.warn("Unable to send keys to element: " + locator + " (Element is null or not visible)");
        }
    }
    public boolean isElementDisplayed(String locator) {
        try {
            // Handle element exists in DOM, display or not
            element = driver.findElement(By.xpath(locator));
            return element.isDisplayed() || element.isEnabled();
        } catch (NoSuchElementException e) {
            // Handle element not exist in DOM
            e.printStackTrace();
            return false;
        }
    }
    public boolean areElementsDisplayed(String locator, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            // Wait until all elements become visible
            List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(locator)));
            // Return true if all elements are displayed
            return elements.stream().allMatch(WebElement::isDisplayed);
        } catch (NoSuchElementException e) {
            System.out.println("No such element found: " + locator);
            return false;
        } catch (Exception e) {
            System.out.println("An error occurred while checking elements: " + locator);
            return false;
        }
    }
}
