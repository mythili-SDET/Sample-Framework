package com.testframework.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.testframework.config.ConfigManager;

import java.io.*;
import java.util.*;

/**
 * CSV Data Provider to handle CSV file operations
 * Supports reading and writing data from CSV files for test data management
 */
public class CsvDataProvider {
    
    private ConfigManager configManager;
    
    public CsvDataProvider() {
        configManager = ConfigManager.getInstance();
    }
    
    /**
     * Read all data from CSV file
     */
    public List<Map<String, String>> readCsvData(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                return data;
            }
            
            String[] headers = rows.get(0);
            
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < headers.length; j++) {
                    String value = (j < row.length) ? row[j] : "";
                    rowData.put(headers[j], value);
                }
                
                data.add(rowData);
            }
            
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        }
        
        return data;
    }
    
    /**
     * Read data from CSV file using default configuration
     */
    public List<Map<String, String>> readCsvData() {
        String filePath = configManager.getDataFilePath() + configManager.getCsvFileName();
        return readCsvData(filePath);
    }
    
    /**
     * Get specific row data by index
     */
    public Map<String, String> getRowData(String filePath, int rowIndex) {
        List<Map<String, String>> allData = readCsvData(filePath);
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            return allData.get(rowIndex);
        }
        throw new IndexOutOfBoundsException("Row index " + rowIndex + " is out of bounds");
    }
    
    /**
     * Get specific row data by index using default configuration
     */
    public Map<String, String> getRowData(int rowIndex) {
        String filePath = configManager.getDataFilePath() + configManager.getCsvFileName();
        return getRowData(filePath, rowIndex);
    }
    
    /**
     * Find row data by column value
     */
    public Map<String, String> findRowByColumnValue(String filePath, String columnName, String value) {
        List<Map<String, String>> allData = readCsvData(filePath);
        
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
        String filePath = configManager.getDataFilePath() + configManager.getCsvFileName();
        return findRowByColumnValue(filePath, columnName, value);
    }
    
    /**
     * Get all column names from CSV file
     */
    public List<String> getColumnNames(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                return new ArrayList<>();
            }
            
            return Arrays.asList(rows.get(0));
            
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        }
    }
    
    /**
     * Get all column names using default configuration
     */
    public List<String> getColumnNames() {
        String filePath = configManager.getDataFilePath() + configManager.getCsvFileName();
        return getColumnNames(filePath);
    }
    
    /**
     * Write data to CSV file
     */
    public void writeCsvData(String filePath, List<Map<String, String>> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be empty");
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write headers
            Set<String> headers = data.get(0).keySet();
            String[] headerArray = headers.toArray(new String[0]);
            writer.writeNext(headerArray);
            
            // Write data
            for (Map<String, String> row : data) {
                String[] rowArray = new String[headers.size()];
                int i = 0;
                for (String header : headers) {
                    rowArray[i++] = row.getOrDefault(header, "");
                }
                writer.writeNext(rowArray);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file: " + filePath, e);
        }
    }
    
    /**
     * Write data to CSV file using default configuration
     */
    public void writeCsvData(List<Map<String, String>> data) {
        String filePath = configManager.getDataFilePath() + configManager.getCsvFileName();
        writeCsvData(filePath, data);
    }
    
    /**
     * Append data to existing CSV file
     */
    public void appendCsvData(String filePath, List<Map<String, String>> data) {
        if (data.isEmpty()) {
            return;
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            Set<String> headers = data.get(0).keySet();
            
            for (Map<String, String> row : data) {
                String[] rowArray = new String[headers.size()];
                int i = 0;
                for (String header : headers) {
                    rowArray[i++] = row.getOrDefault(header, "");
                }
                writer.writeNext(rowArray);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error appending to CSV file: " + filePath, e);
        }
    }
    
    /**
     * Append data to CSV file using default configuration
     */
    public void appendCsvData(List<Map<String, String>> data) {
        String filePath = configManager.getDataFilePath() + configManager.getCsvFileName();
        appendCsvData(filePath, data);
    }
    
    /**
     * Filter data by column value
     */
    public List<Map<String, String>> filterDataByColumnValue(String filePath, String columnName, String value) {
        List<Map<String, String>> allData = readCsvData(filePath);
        List<Map<String, String>> filteredData = new ArrayList<>();
        
        for (Map<String, String> row : allData) {
            if (value.equals(row.get(columnName))) {
                filteredData.add(row);
            }
        }
        
        return filteredData;
    }
    
    /**
     * Filter data by column value using default configuration
     */
    public List<Map<String, String>> filterDataByColumnValue(String columnName, String value) {
        String filePath = configManager.getDataFilePath() + configManager.getCsvFileName();
        return filterDataByColumnValue(filePath, columnName, value);
    }
}