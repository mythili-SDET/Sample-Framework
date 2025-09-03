package com.automation.utils;

import com.automation.config.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Excel utility for reading and writing Excel files
 * Provides HashMap-based data handling for test automation
 */
public class ExcelUtil {
    private static final Logger logger = LoggerManager.getInstance().getLogger(ExcelUtil.class);
    
    /**
     * Read Excel sheet as List of HashMaps
     */
    public static List<Map<String, String>> readSheetAsMaps(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
            
            // Get headers from first row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row not found in sheet: " + sheetName);
            }
            
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }
            
            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Map<String, String> rowData = new LinkedHashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j);
                        rowData.put(headers.get(j), getCellValueAsString(cell));
                    }
                    data.add(rowData);
                }
            }
            
            logger.info("Read {} rows from sheet: {} in file: {}", data.size(), sheetName, filePath);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        
        return data;
    }
    
    /**
     * Read Excel sheet as List of HashMaps with execution filter
     */
    public static List<Map<String, String>> readExecutableData(String filePath, String sheetName) {
        List<Map<String, String>> allData = readSheetAsMaps(filePath, sheetName);
        List<Map<String, String>> executableData = new ArrayList<>();
        
        for (Map<String, String> row : allData) {
            if ("Y".equalsIgnoreCase(row.getOrDefault("Execute", "N"))) {
                executableData.add(row);
            }
        }
        
        logger.info("Filtered {} executable rows from {} total rows in sheet: {}", 
                   executableData.size(), allData.size(), sheetName);
        
        return executableData;
    }
    
    /**
     * Get test data by TestCaseID
     */
    public static Map<String, String> getTestDataByTestCaseId(String filePath, String sheetName, String testCaseId) {
        List<Map<String, String>> allData = readSheetAsMaps(filePath, sheetName);
        
        for (Map<String, String> row : allData) {
            if (testCaseId.equals(row.get("TestCaseId"))) {
                logger.info("Found test data for TestCaseId: {}", testCaseId);
                return row;
            }
        }
        
        logger.warn("Test data not found for TestCaseId: {} in sheet: {}", testCaseId, sheetName);
        return null;
    }
    
    /**
     * Update test result in Excel
     */
    public static void updateTestResult(String filePath, String sheetName, String testCaseId, 
                                      String resultColumn, String resultValue) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
            
            // Find the row with matching TestCaseId
            int rowIndex = findRowByTestCaseId(sheet, testCaseId);
            if (rowIndex == -1) {
                logger.warn("TestCaseId not found: {} in sheet: {}", testCaseId, sheetName);
                return;
            }
            
            // Find the result column
            int colIndex = findColumnByName(sheet, resultColumn);
            if (colIndex == -1) {
                logger.warn("Column not found: {} in sheet: {}", resultColumn, sheetName);
                return;
            }
            
            // Update the cell
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                cell = row.createCell(colIndex);
            }
            cell.setCellValue(resultValue);
            
            // Save the workbook
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            
            logger.info("Updated {} = {} for TestCaseId: {} in sheet: {}", 
                       resultColumn, resultValue, testCaseId, sheetName);
            
        } catch (IOException e) {
            logger.error("Error updating Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to update Excel file: " + filePath, e);
        }
    }
    
    /**
     * Find row index by TestCaseId
     */
    private static int findRowByTestCaseId(Sheet sheet, String testCaseId) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0); // Assuming TestCaseId is in first column
                if (cell != null && testCaseId.equals(getCellValueAsString(cell))) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Find column index by name
     */
    private static int findColumnByName(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null && columnName.equals(getCellValueAsString(cell))) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Get cell value as string
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    /**
     * Get all sheet names from Excel file
     */
    public static List<String> getSheetNames(String filePath) {
        List<String> sheetNames = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
            
            logger.info("Found {} sheets in file: {}", sheetNames.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        
        return sheetNames;
    }
    
    /**
     * Check if Excel file exists and is readable
     */
    public static boolean isExcelFileReadable(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            return true;
        } catch (IOException e) {
            logger.warn("Excel file not readable: {} - {}", filePath, e.getMessage());
            return false;
        }
    }
}
