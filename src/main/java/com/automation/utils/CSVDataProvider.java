package com.automation.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV Data Provider utility for reading test data from CSV files
 */
public class CSVDataProvider {
    private static final Logger logger = LogManager.getLogger(CSVDataProvider.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Read CSV data from file
     */
    public static List<Map<String, String>> readCSVData(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                logger.warn("CSV file is empty: {}", filePath);
                return data;
            }
            
            // Get headers from first row
            String[] headers = rows.get(0);
            
            // Process data rows
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < headers.length && j < row.length; j++) {
                    rowData.put(headers[j], row[j]);
                }
                
                data.add(rowData);
            }
            
            logger.info("Read {} rows of data from CSV file: {}", data.size(), filePath);
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
        
        return data;
    }

    /**
     * Read CSV data from input stream
     */
    public static List<Map<String, String>> readCSVData(InputStream inputStream) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                logger.warn("CSV input stream is empty");
                return data;
            }
            
            // Get headers from first row
            String[] headers = rows.get(0);
            
            // Process data rows
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < headers.length && j < row.length; j++) {
                    rowData.put(headers[j], row[j]);
                }
                
                data.add(rowData);
            }
            
            logger.info("Read {} rows of data from CSV input stream", data.size());
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV from input stream", e);
            throw new RuntimeException("Failed to read CSV from input stream", e);
        }
        
        return data;
    }

    /**
     * Read specific row by index
     */
    public static Map<String, String> readCSVRow(String filePath, int rowIndex) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty() || rowIndex >= rows.size()) {
                logger.warn("Row index {} not found in CSV file: {}", rowIndex, filePath);
                return new HashMap<>();
            }
            
            String[] headers = rows.get(0);
            String[] dataRow = rows.get(rowIndex);
            
            Map<String, String> rowData = new HashMap<>();
            for (int i = 0; i < headers.length && i < dataRow.length; i++) {
                rowData.put(headers[i], dataRow[i]);
            }
            
            logger.info("Read row {} from CSV file: {}", rowIndex, filePath);
            return rowData;
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV row: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV row: " + filePath, e);
        }
    }

    /**
     * Read data by column name
     */
    public static List<String> readCSVColumn(String filePath, String columnName) {
        List<String> columnData = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                logger.warn("CSV file is empty: {}", filePath);
                return columnData;
            }
            
            String[] headers = rows.get(0);
            
            // Find column index
            int columnIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (columnName.equals(headers[i])) {
                    columnIndex = i;
                    break;
                }
            }
            
            if (columnIndex == -1) {
                logger.warn("Column '{}' not found in CSV file: {}", columnName, filePath);
                return columnData;
            }
            
            // Read column data
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (columnIndex < row.length) {
                    columnData.add(row[columnIndex]);
                }
            }
            
            logger.info("Read {} values from column '{}' in CSV file: {}", 
                       columnData.size(), columnName, filePath);
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV column: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV column: " + filePath, e);
        }
        
        return columnData;
    }

    /**
     * Read CSV data with custom delimiter
     */
    public static List<Map<String, String>> readCSVData(String filePath, char delimiter) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath), delimiter)) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                logger.warn("CSV file is empty: {}", filePath);
                return data;
            }
            
            // Get headers from first row
            String[] headers = rows.get(0);
            
            // Process data rows
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < headers.length && j < row.length; j++) {
                    rowData.put(headers[j], row[j]);
                }
                
                data.add(rowData);
            }
            
            logger.info("Read {} rows of data from CSV file with delimiter '{}': {}", 
                       data.size(), delimiter, filePath);
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV file with delimiter: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file with delimiter: " + filePath, e);
        }
        
        return data;
    }

    /**
     * Read test data from configured CSV file
     */
    public static List<Map<String, String>> readTestData() {
        String dataPath = config.getProperty("test.data.path");
        String csvFile = config.getProperty("test.data.csv");
        String fullPath = dataPath + csvFile;
        
        logger.info("Reading test data from: {}", fullPath);
        return readCSVData(fullPath);
    }

    /**
     * Read test data with custom delimiter
     */
    public static List<Map<String, String>> readTestData(char delimiter) {
        String dataPath = config.getProperty("test.data.path");
        String csvFile = config.getProperty("test.data.csv");
        String fullPath = dataPath + csvFile;
        
        logger.info("Reading test data with delimiter '{}' from: {}", delimiter, fullPath);
        return readCSVData(fullPath, delimiter);
    }

    /**
     * Get column names from CSV file
     */
    public static List<String> getColumnNames(String filePath) {
        List<String> columnNames = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            
            if (!rows.isEmpty()) {
                String[] headers = rows.get(0);
                for (String header : headers) {
                    columnNames.add(header);
                }
            }
            
            logger.info("Found {} columns in CSV file: {}", columnNames.size(), filePath);
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading column names from CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to read column names: " + filePath, e);
        }
        
        return columnNames;
    }

    /**
     * Get row count from CSV file
     */
    public static int getRowCount(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            int rowCount = rows.size() - 1; // Exclude header row
            logger.info("CSV file has {} data rows: {}", rowCount, filePath);
            return rowCount;
        } catch (IOException | CsvException e) {
            logger.error("Error getting row count from CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to get row count: " + filePath, e);
        }
    }
}