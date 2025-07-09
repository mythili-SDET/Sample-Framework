package com.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Excel Data Reader utility for reading test data from Excel files
 * Supports both .xls and .xlsx formats
 */
public class ExcelDataReader {
    private static final Logger logger = LogManager.getLogger(ExcelDataReader.class);
    
    /**
     * Read all data from Excel sheet
     * @param filePath Excel file path
     * @param sheetName Sheet name
     * @return List of Maps containing row data
     */
    public static List<Map<String, String>> readExcelData(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = createWorkbook(filePath, fis);
            Sheet sheet = workbook.getSheet(sheetName);
            
            if (sheet == null) {
                logger.error("Sheet '{}' not found in file '{}'", sheetName, filePath);
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
            
            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.error("Header row not found in sheet '{}'", sheetName);
                throw new RuntimeException("Header row not found");
            }
            
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }
            
            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && !isRowEmpty(row)) {
                    Map<String, String> rowData = new HashMap<>();
                    
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j);
                        String cellValue = cell != null ? getCellValueAsString(cell) : "";
                        rowData.put(headers.get(j), cellValue);
                    }
                    data.add(rowData);
                }
            }
            
            workbook.close();
            logger.info("Successfully read {} rows from Excel file: {}", data.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        
        return data;
    }
    
    /**
     * Read specific row from Excel sheet
     * @param filePath Excel file path
     * @param sheetName Sheet name
     * @param rowIndex Row index (0-based, excluding header)
     * @return Map containing row data
     */
    public static Map<String, String> readExcelRow(String filePath, String sheetName, int rowIndex) {
        List<Map<String, String>> allData = readExcelData(filePath, sheetName);
        
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            return allData.get(rowIndex);
        } else {
            logger.error("Row index {} is out of bounds. Total rows: {}", rowIndex, allData.size());
            throw new IndexOutOfBoundsException("Row index out of bounds: " + rowIndex);
        }
    }
    
    /**
     * Read data from Excel and convert to Object array for TestNG DataProvider
     * @param filePath Excel file path
     * @param sheetName Sheet name
     * @return Object[][] for TestNG DataProvider
     */
    public static Object[][] readExcelDataForTestNG(String filePath, String sheetName) {
        List<Map<String, String>> data = readExcelData(filePath, sheetName);
        Object[][] testData = new Object[data.size()][1];
        
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i);
        }
        
        return testData;
    }
    
    /**
     * Get specific cell value from Excel
     * @param filePath Excel file path
     * @param sheetName Sheet name
     * @param rowIndex Row index (0-based)
     * @param columnIndex Column index (0-based)
     * @return Cell value as string
     */
    public static String getCellValue(String filePath, String sheetName, int rowIndex, int columnIndex) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = createWorkbook(filePath, fis);
            Sheet sheet = workbook.getSheet(sheetName);
            
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }
            
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return "";
            }
            
            Cell cell = row.getCell(columnIndex);
            String value = cell != null ? getCellValueAsString(cell) : "";
            
            workbook.close();
            return value;
            
        } catch (IOException e) {
            logger.error("Error reading cell value from Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read cell value", e);
        }
    }
    
    /**
     * Get all sheet names from Excel file
     * @param filePath Excel file path
     * @return List of sheet names
     */
    public static List<String> getSheetNames(String filePath) {
        List<String> sheetNames = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = createWorkbook(filePath, fis);
            
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
            
            workbook.close();
            
        } catch (IOException e) {
            logger.error("Error reading sheet names from Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read sheet names", e);
        }
        
        return sheetNames;
    }
    
    /**
     * Create appropriate workbook based on file extension
     */
    private static Workbook createWorkbook(String filePath, FileInputStream fis) throws IOException {
        if (filePath.endsWith(".xlsx")) {
            return new XSSFWorkbook(fis);
        } else if (filePath.endsWith(".xls")) {
            return new HSSFWorkbook(fis);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only .xls and .xlsx are supported.");
        }
    }
    
    /**
     * Convert cell value to string
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Handle both integer and decimal numbers
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }
    
    /**
     * Check if row is empty
     */
    private static boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (cell != null && !getCellValueAsString(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}