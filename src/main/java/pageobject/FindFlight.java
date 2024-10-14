package pageobject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class FindFlight {

    WebDriver driver;
    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "src/main/java/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.bestprice.vn/");
    }
    @AfterClass
    public void tearDown(){
        driver.quit();
    }
    @Test
    public void findTicket() throws IOException, InterruptedException {
        WebElement searchBtn = driver.findElement(By.xpath("//div/button[@onclick='return validate_flight_search()']"));
        //chon diem di, den
        selectThePlaceToGoAndReturn("han", "sgn");
        //chon ngay di, ngay ve
        selectTheDepartAndReturn("9", "15", "9", "20");
        //chon so luong khach hang (it nhat 1 nguoi lon)
        selectPassengers(1,3,2);
        searchBtn.click();
        //close the file before run
        printOutTheListToExcel();
    }

    public void selectThePlaceToGoAndReturn(String fromAirportCode, String returnAirportCode){
        WebElement startFrom = driver.findElement(By.xpath("//input[@name='From']"));
        WebElement destinationPlace = driver.findElement(By.xpath("//input[@name='To']"));

        //chon diem di
        startFrom.click();
        waitForElementVisible("//span/input[@data-id='flight_from']");
        WebElement chonDiemDi = driver.findElement(By.xpath("//span/input[@data-id='flight_from']"));
        chonDiemDi.sendKeys(fromAirportCode);
        waitForElementVisible("//span[@class='tt-dropdown-menu']");
        WebElement firstSuggestion1 = driver.findElement(By.xpath("//*[contains(text(),'Sân bay')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstSuggestion1);
        //chon diem den
        destinationPlace.click();
        WebElement chonDiemDen = driver.findElement(By.xpath("//span/input[@data-id='flight_to']"));
        chonDiemDen.sendKeys(returnAirportCode);
        waitForElementVisible("//*[contains(text(),'Sân bay')]");
        WebElement firstSuggestion2 = driver.findElement(By.xpath("//*[contains(text(),'Sân bay')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstSuggestion2);
    }

    //khong khu hoi
    public void selectTheDepart(String departMonth, String departDate){
        //ngay di
        WebElement departFrom = driver.findElement(By.xpath("//input[@name='Depart']"));
        departFrom.click();
        WebElement departDay = driver.findElement(By.xpath("//td[@data-month='%s']//span[text()= '%s' and @class='ui-datepicker-day ']"));
        departDay.click();
    }
    //khu hoi
    public void selectTheDepartAndReturn(String departMonth, String departDate, String returnMonth, String returnDate){
        //ngay di
        WebElement departFrom = driver.findElement(By.xpath("//input[@name='Depart']"));
        departFrom.click();
        WebElement returnAt = driver.findElement(By.xpath("//input[@name='Return']"));
        WebElement departDay = driver.findElement(By.xpath(String.format("//td[@data-month='%s']//span[text()= '%s' and @class='ui-datepicker-day ']",departMonth, departDate)));
        departDay.click();
        //ngay khu hoi
        returnAt.click();
        WebElement returnDay = driver.findElement(By.xpath(String.format("//td[@data-month='%s']//span[text()= '%s' and @class='ui-datepicker-day ']",returnMonth, returnDate)));
        returnDay.click();
    }
    //select the quantity passengers
    public void selectPassengers(int adults, int children, int infants) {
        WebElement quantityPassenger = driver.findElement(By.xpath("//input[@id='flight_passenger']"));
        quantityPassenger.click();
        WebElement adultPlusButton = driver.findElement(By.xpath("(//div[@class='mktnd_btn_flight_adult_plus btn btn-plus btn-plus-0 btn-number'])[2]"));
        WebElement adultMinusButton = driver.findElement(By.xpath("(//div[@class='mktnd_btn_flight_adult_minus btn btn-minus btn-minus-0 btn-number'])[2]"));
        WebElement adultCount = driver.findElement(By.xpath("(//input[@class='cr_adults nb_adults form-control text-center input-number'])[3]"));
        adjustCount(adultPlusButton, adultMinusButton, adultCount, adults);

        // Locate and set the number of children
        WebElement childPlusButton = driver.findElement(By.xpath("(//div[@class='mktnd_btn_children_adult_plus btn btn-plus btn-plus-1 btn-number'])[2]"));
        WebElement childMinusButton = driver.findElement(By.xpath("(//div[@class='mktnd_btn_children_adult_minus btn btn-minus btn-minus-1 btn-number'])[2]"));
        WebElement childCount = driver.findElement(By.xpath("(//input[@class='cr_children nb_children form-control text-center input-number'])[3]"));
        adjustCount(childPlusButton, childMinusButton, childCount, children);

        // Locate and set the number of infants
        WebElement infantPlusButton = driver.findElement(By.xpath("(//div[@class='mktnd_btn_flight_infant_plus btn btn-plus btn-plus-2 btn-number'])[2]"));
        WebElement infantMinusButton = driver.findElement(By.xpath("(//div[@class='mktnd_btn_flight_infant_minus btn btn-minus btn-minus-2 btn-number'])[2]"));
        WebElement infantCount = driver.findElement(By.xpath("(//input[@class='cr_infants nb_infants form-control text-center input-number'])[3]"));
        adjustCount(infantPlusButton, infantMinusButton, infantCount, infants);
    }

    private void adjustCount(WebElement plusButton, WebElement minusButton, WebElement countElement, int desiredCount) {
        int currentCount = Integer.parseInt(countElement.getAttribute("value").trim());
        while (currentCount < desiredCount) {
            plusButton.click();
            currentCount++;
        }
        while (currentCount > desiredCount) {
            minusButton.click();
            currentCount--;
        }
    }

    //print to search result to Excel
    public void printOutTheListToExcel() throws IOException, InterruptedException {
        Thread.sleep(5000);//wait for the searching
        List<WebElement> tickets = driver.findElements(By.xpath("//div[@class='bpv-list-item-row']"));
        //print to Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Element Texts");
        // Iterate through the list of elements and extract text
        int rowNum = 0;
        for (WebElement ticket : tickets) {
            String text = ticket.getText(); // Get the text of the element
            // Create a new row in the Excel sheet
            Row row = sheet.createRow(rowNum++);
            // Create a new cell and set the extracted text
            Cell cell = row.createCell(0);
            cell.setCellValue(text);
        }
        // Write the data to an Excel file
        try (FileOutputStream fileOut = new FileOutputStream("elements.xlsx")) {
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Close the workbook
        workbook.close();
    }

    public void waitForElementVisible(String locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
    }
}