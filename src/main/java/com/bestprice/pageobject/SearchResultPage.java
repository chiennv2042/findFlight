package com.bestprice.pageobject;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SearchResultPage extends BasePage{

    // Constructor
    public SearchResultPage(WebDriver driver) {
        super(driver);
    }

    // Get search results and export them to Excel
    public void exportListResultsToExcel(String listElements, String sheetName, String outputFileName) throws IOException {
        List<WebElement> elements = waitForElementsVisible(listElements);  // Wait and get elements
        if (elements == null || elements.isEmpty()) {
            System.out.println("No elements found for export.");
            return; // Exit early if no elements found
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        int rowNum = 0;
        for (WebElement element : elements) {
            String elementText = element.getText();
            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(0);
            cell.setCellValue(elementText);
        }

        try (FileOutputStream fileOut = new FileOutputStream(outputFileName)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            System.out.println("Error writing to Excel file: " + e.getMessage());
        } finally {
            workbook.close(); // Ensure workbook is closed
        }
    }

}
