package com.testframework.utils;

import com.testframework.config.ConfigManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Excel Data Provider to handle Excel file operations
 * Supports reading data from Excel files for test data management
 */
public class ExcelDataProvider {
    
    private ConfigManager configManager;
    
    public ExcelDataProvider() {
        configManager = ConfigManager.getInstance();
    }
    
    /**
     * Read all data from a specific sheet
     */
    public List<Map<String, String>> readExcelData(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in Excel file");
            }
            
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row not found in sheet '" + sheetName + "'");
            }
            
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }
            
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
            
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + filePath, e);
        }
        
        return data;
    }
    
    /**
     * Read data from Excel file using default configuration
     */
    public List<Map<String, String>> readExcelData() {
        String filePath = configManager.getDataFilePath() + configManager.getExcelFileName();
        return readExcelData(filePath, "Sheet1");
    }
    
    /**
     * Read data from specific sheet using default file
     */
    public List<Map<String, String>> readExcelData(String sheetName) {
        String filePath = configManager.getDataFilePath() + configManager.getExcelFileName();
        return readExcelData(filePath, sheetName);
    }
    
    /**
     * Get specific row data by index
     */
    public Map<String, String> getRowData(String filePath, String sheetName, int rowIndex) {
        List<Map<String, String>> allData = readExcelData(filePath, sheetName);
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            return allData.get(rowIndex);
        }
        throw new IndexOutOfBoundsException("Row index " + rowIndex + " is out of bounds");
    }
    
    /**
     * Get specific row data by index using default configuration
     */
    public Map<String, String> getRowData(int rowIndex) {
        String filePath = configManager.getDataFilePath() + configManager.getExcelFileName();
        return getRowData(filePath, "Sheet1", rowIndex);
    }
    
    /**
     * Find row data by column value
     */
    public Map<String, String> findRowByColumnValue(String filePath, String sheetName, String columnName, String value) {
        List<Map<String, String>> allData = readExcelData(filePath, sheetName);
        
        for (Map<String, String> row : allData) {
            if (value.equals(row.get(columnName))) {
                return row;
            }
        }
        
        return null;
    }
    
    /**
     * Find row data by column value using default configuration
     */
    public Map<String, String> findRowByColumnValue(String columnName, String value) {
        String filePath = configManager.getDataFilePath() + configManager.getExcelFileName();
        return findRowByColumnValue(filePath, "Sheet1", columnName, value);
    }
    
    /**
     * Get all sheet names from Excel file
     */
    public List<String> getSheetNames(String filePath) {
        List<String> sheetNames = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + filePath, e);
        }
        
        return sheetNames;
    }
    
    /**
     * Get cell value as string
     */
    private String getCellValueAsString(Cell cell) {
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
                    return String.valueOf(cell.getNumericCellValue());
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
     * Write data to Excel file
     */
    public void writeExcelData(String filePath, String sheetName, List<Map<String, String>> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            
            if (!data.isEmpty()) {
                // Write headers
                Set<String> headers = data.get(0).keySet();
                Row headerRow = sheet.createRow(0);
                int colIndex = 0;
                for (String header : headers) {
                    Cell cell = headerRow.createCell(colIndex++);
                    cell.setCellValue(header);
                }
                
                // Write data
                for (int i = 0; i < data.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    Map<String, String> rowData = data.get(i);
                    colIndex = 0;
                    for (String header : headers) {
                        Cell cell = row.createCell(colIndex++);
                        cell.setCellValue(rowData.get(header));
                    }
                }
            }
            
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error writing Excel file: " + filePath, e);
        }
    }
}