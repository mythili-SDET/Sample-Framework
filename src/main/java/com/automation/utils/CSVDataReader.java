package com.automation.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * CSV Data Reader utility for reading test data from CSV files
 * Supports various CSV formats and configurations
 */
public class CSVDataReader {
    private static final Logger logger = LogManager.getLogger(CSVDataReader.class);
    
    /**
     * Read all data from CSV file
     * @param filePath CSV file path
     * @return List of Maps containing row data
     */
    public static List<Map<String, String>> readCSVData(String filePath) {
        return readCSVData(filePath, ',', '"', 0);
    }
    
    /**
     * Read all data from CSV file with custom separator
     * @param filePath CSV file path
     * @param separator Field separator character
     * @return List of Maps containing row data
     */
    public static List<Map<String, String>> readCSVData(String filePath, char separator) {
        return readCSVData(filePath, separator, '"', 0);
    }
    
    /**
     * Read all data from CSV file with custom configuration
     * @param filePath CSV file path
     * @param separator Field separator character
     * @param quoteChar Quote character
     * @param skipLines Number of lines to skip from beginning
     * @return List of Maps containing row data
     */
    public static List<Map<String, String>> readCSVData(String filePath, char separator, char quoteChar, int skipLines) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (FileReader fileReader = new FileReader(filePath);
             CSVReader csvReader = new CSVReaderBuilder(fileReader)
                     .withSeparator(separator)
                     .withQuoteChar(quoteChar)
                     .withSkipLines(skipLines)
                     .build()) {
            
            List<String[]> records = csvReader.readAll();
            
            if (records.isEmpty()) {
                logger.warn("CSV file is empty: {}", filePath);
                return data;
            }
            
            // First row as headers
            String[] headers = records.get(0);
            
            // Process data rows
            for (int i = 1; i < records.size(); i++) {
                String[] row = records.get(i);
                
                // Skip empty rows
                if (isRowEmpty(row)) {
                    continue;
                }
                
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < headers.length && j < row.length; j++) {
                    String header = headers[j].trim();
                    String value = j < row.length ? row[j].trim() : "";
                    rowData.put(header, value);
                }
                
                data.add(rowData);
            }
            
            logger.info("Successfully read {} rows from CSV file: {}", data.size(), filePath);
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
        
        return data;
    }
    
    /**
     * Read specific row from CSV file
     * @param filePath CSV file path
     * @param rowIndex Row index (0-based, excluding header)
     * @return Map containing row data
     */
    public static Map<String, String> readCSVRow(String filePath, int rowIndex) {
        List<Map<String, String>> allData = readCSVData(filePath);
        
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            return allData.get(rowIndex);
        } else {
            logger.error("Row index {} is out of bounds. Total rows: {}", rowIndex, allData.size());
            throw new IndexOutOfBoundsException("Row index out of bounds: " + rowIndex);
        }
    }
    
    /**
     * Read data from CSV and convert to Object array for TestNG DataProvider
     * @param filePath CSV file path
     * @return Object[][] for TestNG DataProvider
     */
    public static Object[][] readCSVDataForTestNG(String filePath) {
        List<Map<String, String>> data = readCSVData(filePath);
        Object[][] testData = new Object[data.size()][1];
        
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i);
        }
        
        return testData;
    }
    
    /**
     * Read CSV file and return as List of String arrays
     * @param filePath CSV file path
     * @return List of String arrays
     */
    public static List<String[]> readCSVAsArrays(String filePath) {
        return readCSVAsArrays(filePath, ',', '"', 0);
    }
    
    /**
     * Read CSV file and return as List of String arrays with custom configuration
     * @param filePath CSV file path
     * @param separator Field separator character
     * @param quoteChar Quote character
     * @param skipLines Number of lines to skip from beginning
     * @return List of String arrays
     */
    public static List<String[]> readCSVAsArrays(String filePath, char separator, char quoteChar, int skipLines) {
        try (FileReader fileReader = new FileReader(filePath);
             CSVReader csvReader = new CSVReaderBuilder(fileReader)
                     .withSeparator(separator)
                     .withQuoteChar(quoteChar)
                     .withSkipLines(skipLines)
                     .build()) {
            
            List<String[]> records = csvReader.readAll();
            logger.info("Successfully read {} records from CSV file: {}", records.size(), filePath);
            return records;
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV file as arrays: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
    }
    
    /**
     * Get specific cell value from CSV file
     * @param filePath CSV file path
     * @param rowIndex Row index (0-based)
     * @param columnIndex Column index (0-based)
     * @return Cell value as string
     */
    public static String getCellValue(String filePath, int rowIndex, int columnIndex) {
        List<String[]> records = readCSVAsArrays(filePath);
        
        if (rowIndex >= 0 && rowIndex < records.size()) {
            String[] row = records.get(rowIndex);
            if (columnIndex >= 0 && columnIndex < row.length) {
                return row[columnIndex].trim();
            } else {
                logger.error("Column index {} is out of bounds for row {}. Row length: {}", 
                           columnIndex, rowIndex, row.length);
                throw new IndexOutOfBoundsException("Column index out of bounds: " + columnIndex);
            }
        } else {
            logger.error("Row index {} is out of bounds. Total rows: {}", rowIndex, records.size());
            throw new IndexOutOfBoundsException("Row index out of bounds: " + rowIndex);
        }
    }
    
    /**
     * Get headers from CSV file
     * @param filePath CSV file path
     * @return List of header names
     */
    public static List<String> getHeaders(String filePath) {
        return getHeaders(filePath, ',', '"');
    }
    
    /**
     * Get headers from CSV file with custom configuration
     * @param filePath CSV file path
     * @param separator Field separator character
     * @param quoteChar Quote character
     * @return List of header names
     */
    public static List<String> getHeaders(String filePath, char separator, char quoteChar) {
        try (FileReader fileReader = new FileReader(filePath);
             CSVReader csvReader = new CSVReaderBuilder(fileReader)
                     .withSeparator(separator)
                     .withQuoteChar(quoteChar)
                     .build()) {
            
            String[] headers = csvReader.readNext();
            if (headers != null) {
                List<String> headerList = new ArrayList<>();
                for (String header : headers) {
                    headerList.add(header.trim());
                }
                return headerList;
            } else {
                logger.warn("No headers found in CSV file: {}", filePath);
                return new ArrayList<>();
            }
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading headers from CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to read headers from CSV file: " + filePath, e);
        }
    }
    
    /**
     * Get number of rows in CSV file (excluding header)
     * @param filePath CSV file path
     * @return Number of data rows
     */
    public static int getRowCount(String filePath) {
        List<Map<String, String>> data = readCSVData(filePath);
        return data.size();
    }
    
    /**
     * Filter CSV data based on column value
     * @param filePath CSV file path
     * @param columnName Column name to filter by
     * @param value Value to filter for
     * @return List of matching rows
     */
    public static List<Map<String, String>> filterCSVData(String filePath, String columnName, String value) {
        List<Map<String, String>> allData = readCSVData(filePath);
        List<Map<String, String>> filteredData = new ArrayList<>();
        
        for (Map<String, String> row : allData) {
            if (value.equals(row.get(columnName))) {
                filteredData.add(row);
            }
        }
        
        logger.info("Filtered {} rows from CSV file based on {}={}", filteredData.size(), columnName, value);
        return filteredData;
    }
    
    /**
     * Check if CSV row is empty
     */
    private static boolean isRowEmpty(String[] row) {
        if (row == null) {
            return true;
        }
        
        for (String cell : row) {
            if (cell != null && !cell.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}