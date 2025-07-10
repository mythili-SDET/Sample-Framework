package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Excel Data Provider utility for reading test data from Excel files
 */
public class ExcelDataProvider {
    private static final Logger logger = LogManager.getLogger(ExcelDataProvider.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Read all data from Excel file
     */
    public static List<Map<String, String>> readExcelData(String filePath) {
        return readExcelData(filePath, 0); // Default to first sheet
    }

    /**
     * Read data from specific sheet by index
     */
    public static List<Map<String, String>> readExcelData(String filePath, int sheetIndex) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (InputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(inputStream, filePath)) {
            
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Row headerRow = sheet.getRow(0);
            
            if (headerRow == null) {
                logger.warn("No header row found in sheet at index: {}", sheetIndex);
                return data;
            }
            
            // Get column headers
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }
            
            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j);
                        rowData.put(headers.get(j), getCellValueAsString(cell));
                    }
                    data.add(rowData);
                }
            }
            
            logger.info("Read {} rows of data from Excel file: {}", data.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        
        return data;
    }

    /**
     * Read data from specific sheet by name
     */
    public static List<Map<String, String>> readExcelData(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (InputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(inputStream, filePath)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.warn("Sheet '{}' not found in Excel file: {}", sheetName, filePath);
                return data;
            }
            
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.warn("No header row found in sheet: {}", sheetName);
                return data;
            }
            
            // Get column headers
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }
            
            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j);
                        rowData.put(headers.get(j), getCellValueAsString(cell));
                    }
                    data.add(rowData);
                }
            }
            
            logger.info("Read {} rows of data from sheet '{}' in Excel file: {}", 
                       data.size(), sheetName, filePath);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        
        return data;
    }

    /**
     * Read specific row by row number
     */
    public static Map<String, String> readExcelRow(String filePath, int rowNumber) {
        return readExcelRow(filePath, 0, rowNumber);
    }

    /**
     * Read specific row by row number from specific sheet
     */
    public static Map<String, String> readExcelRow(String filePath, int sheetIndex, int rowNumber) {
        try (InputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(inputStream, filePath)) {
            
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Row headerRow = sheet.getRow(0);
            Row dataRow = sheet.getRow(rowNumber);
            
            if (headerRow == null || dataRow == null) {
                logger.warn("Header row or data row not found");
                return new HashMap<>();
            }
            
            Map<String, String> rowData = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell headerCell = headerRow.getCell(i);
                Cell dataCell = dataRow.getCell(i);
                
                if (headerCell != null) {
                    String header = getCellValueAsString(headerCell);
                    String value = getCellValueAsString(dataCell);
                    rowData.put(header, value);
                }
            }
            
            logger.info("Read row {} from Excel file: {}", rowNumber, filePath);
            return rowData;
            
        } catch (IOException e) {
            logger.error("Error reading Excel row: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel row: " + filePath, e);
        }
    }

    /**
     * Read data by column name
     */
    public static List<String> readExcelColumn(String filePath, String columnName) {
        return readExcelColumn(filePath, 0, columnName);
    }

    /**
     * Read data by column name from specific sheet
     */
    public static List<String> readExcelColumn(String filePath, int sheetIndex, String columnName) {
        List<String> columnData = new ArrayList<>();
        
        try (InputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(inputStream, filePath)) {
            
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            Row headerRow = sheet.getRow(0);
            
            if (headerRow == null) {
                logger.warn("No header row found");
                return columnData;
            }
            
            // Find column index
            int columnIndex = -1;
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null && columnName.equals(getCellValueAsString(cell))) {
                    columnIndex = i;
                    break;
                }
            }
            
            if (columnIndex == -1) {
                logger.warn("Column '{}' not found in Excel file", columnName);
                return columnData;
            }
            
            // Read column data
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    columnData.add(getCellValueAsString(cell));
                }
            }
            
            logger.info("Read {} values from column '{}' in Excel file: {}", 
                       columnData.size(), columnName, filePath);
            
        } catch (IOException e) {
            logger.error("Error reading Excel column: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel column: " + filePath, e);
        }
        
        return columnData;
    }

    /**
     * Get sheet names from Excel file
     */
    public static List<String> getSheetNames(String filePath) {
        List<String> sheetNames = new ArrayList<>();
        
        try (InputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(inputStream, filePath)) {
            
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
            
            logger.info("Found {} sheets in Excel file: {}", sheetNames.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error reading sheet names from Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read sheet names: " + filePath, e);
        }
        
        return sheetNames;
    }

    /**
     * Create appropriate workbook based on file extension
     */
    private static Workbook createWorkbook(InputStream inputStream, String filePath) throws IOException {
        if (filePath.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else if (filePath.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("Unsupported Excel file format. Use .xlsx or .xls");
        }
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
                    // Avoid decimal places for whole numbers
                    double value = cell.getNumericCellValue();
                    if (value == (long) value) {
                        return String.valueOf((long) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Read test data from configured Excel file
     */
    public static List<Map<String, String>> readTestData() {
        String dataPath = config.getProperty("test.data.path");
        String excelFile = config.getProperty("test.data.excel");
        String fullPath = dataPath + excelFile;
        
        logger.info("Reading test data from: {}", fullPath);
        return readExcelData(fullPath);
    }

    /**
     * Read test data from specific sheet
     */
    public static List<Map<String, String>> readTestData(String sheetName) {
        String dataPath = config.getProperty("test.data.path");
        String excelFile = config.getProperty("test.data.excel");
        String fullPath = dataPath + excelFile;
        
        logger.info("Reading test data from sheet '{}' in: {}", sheetName, fullPath);
        return readExcelData(fullPath, sheetName);
    }
}