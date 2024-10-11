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

import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class aaa {

    WebDriver driver;
    @Test
    public void setup() throws InterruptedException, IOException {
        System.setProperty("webdriver.chrome.driver", "src/main/java/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.bestprice.vn/");

        WebElement diemDi = driver.findElement(By.xpath("//input[@name='From']"));
        WebElement diemDen = driver.findElement(By.xpath("//input[@name='To']"));
        WebElement ngayDi = driver.findElement(By.xpath("//input[@name='Depart']"));
        WebElement khuHoi = driver.findElement(By.xpath("//input[@name='Return']"));
        WebElement chonSoLuong = driver.findElement(By.xpath("//input[@id='flight_passenger']"));
        WebElement timChuyenBtn = driver.findElement(By.xpath("//div/button[@onclick='return validate_flight_search()']"));


        //chon diem di
        diemDi.click();
        waitForElementVisible("//span/input[@data-id='flight_from']");
        WebElement chonDiemDi = driver.findElement(By.xpath("//span/input[@data-id='flight_from']"));
        chonDiemDi.sendKeys("han");
        waitForElementVisible("//span[@class='tt-dropdown-menu']");
        WebElement Hanoi = driver.findElement(By.xpath("//*[contains(text(),'Sân bay Nội Bài')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Hanoi);

        //chon diem den
        diemDen.click();
        WebElement chonDiemDen = driver.findElement(By.xpath("//span/input[@data-id='flight_to']"));
        chonDiemDen.sendKeys("sgn");
        waitForElementVisible("//*[contains(text(),'Sân bay Tân Sơn Nhất')]");
        WebElement HCM = driver.findElement(By.xpath("//*[contains(text(),'Sân bay Tân Sơn Nhất')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", HCM);

        //chon ngay di
        ngayDi.click();
        WebElement ngay12DuongLich = driver.findElement(By.xpath("//td[@data-month='9']//span[text()= '12' and @class='ui-datepicker-day ']"));
        ngay12DuongLich.click();
        //ngay khu hoi
        khuHoi.click();
        WebElement ngay20DuongLich = driver.findElement(By.xpath("//td[@data-month='9']//span[text()= '20' and @class='ui-datepicker-day ']"));
        ngay20DuongLich.click();
//        chon so luong khach hang
        chonSoLuong.click();
        WebElement AddMoreAdult = driver.findElement(By.xpath("(//div[@class='mktnd_btn_flight_adult_plus btn btn-plus btn-plus-0 btn-number'])[2]"));
        WebElement AddMoreChildren = driver.findElement(By.xpath("(//div[@class='mktnd_btn_children_adult_plus btn btn-plus btn-plus-1 btn-number'])[2]"));
        AddMoreAdult.click();
        AddMoreChildren.click();
        timChuyenBtn.click();
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
        Thread.sleep(5000);
        driver.quit();
    }

    public void waitForElementVisible(String locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
    }
}