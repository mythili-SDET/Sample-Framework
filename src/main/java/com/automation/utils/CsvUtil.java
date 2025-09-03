package com.automation.utils;

import com.automation.config.LoggerManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Utility class for CSV file operations
 * Provides methods to read, write, and manipulate CSV files
 */
public class CsvUtil {
    
    private static final Logger logger = LoggerManager.getLogger(CsvUtil.class);
    
    /**
     * Read CSV file and return as List of Maps
     * @param filePath Path to the CSV file
     * @return List of Maps where each Map represents a row
     */
    public static List<Map<String, String>> readCsvAsMaps(String filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                Map<String, String> row = new HashMap<>();
                for (String header : csvParser.getHeaderNames()) {
                    row.put(header, record.get(header));
                }
                records.add(row);
            }
            
            logger.info("Successfully read {} records from CSV file: {}", records.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error reading CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
        
        return records;
    }
    
    /**
     * Read CSV file and return as List of Maps with custom delimiter
     * @param filePath Path to the CSV file
     * @param delimiter CSV delimiter character
     * @return List of Maps where each Map represents a row
     */
    public static List<Map<String, String>> readCsvAsMaps(String filePath, char delimiter) {
        List<Map<String, String>> records = new ArrayList<>();
        
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(delimiter).withFirstRecordAsHeader())) {
            
            for (CSVRecord record : csvParser) {
                Map<String, String> row = new HashMap<>();
                for (String header : csvParser.getHeaderNames()) {
                    row.put(header, record.get(header));
                }
                records.add(row);
            }
            
            logger.info("Successfully read {} records from CSV file: {} with delimiter: {}", 
                       records.size(), filePath, delimiter);
            
        } catch (IOException e) {
            logger.error("Error reading CSV file: {} with delimiter: {}", filePath, delimiter, e);
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
        
        return records;
    }
    
    /**
     * Write data to CSV file
     * @param filePath Path to the CSV file
     * @param headers Column headers
     * @param data List of data rows
     */
    public static void writeCsv(String filePath, String[] headers, List<String[]> data) {
        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers))) {
            
            for (String[] row : data) {
                csvPrinter.printRecord((Object[]) row);
            }
            
            logger.info("Successfully wrote {} records to CSV file: {}", data.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error writing CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to write CSV file: " + filePath, e);
        }
    }
    
    /**
     * Write Map data to CSV file
     * @param filePath Path to the CSV file
     * @param data List of Maps where each Map represents a row
     */
    public static void writeCsvFromMaps(String filePath, List<Map<String, String>> data) {
        if (data.isEmpty()) {
            logger.warn("No data to write to CSV file: {}", filePath);
            return;
        }
        
        Set<String> headers = data.get(0).keySet();
        
        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {
            
            for (Map<String, String> row : data) {
                csvPrinter.printRecord(row.values());
            }
            
            logger.info("Successfully wrote {} records to CSV file: {}", data.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error writing CSV file: {}", filePath, e);
            throw new RuntimeException("Failed to write CSV file: " + filePath, e);
        }
    }
    
    /**
     * Get specific column data from CSV
     * @param filePath Path to the CSV file
     * @param columnName Name of the column
     * @return List of values in the specified column
     */
    public static List<String> getColumnData(String filePath, String columnName) {
        List<String> columnData = new ArrayList<>();
        List<Map<String, String>> records = readCsvAsMaps(filePath);
        
        for (Map<String, String> record : records) {
            if (record.containsKey(columnName)) {
                columnData.add(record.get(columnName));
            }
        }
        
        logger.info("Retrieved {} values from column: {} in file: {}", 
                   columnData.size(), columnName, filePath);
        
        return columnData;
    }
    
    /**
     * Filter CSV data based on condition
     * @param filePath Path to the CSV file
     * @param columnName Column to filter on
     * @param value Value to match
     * @return Filtered list of records
     */
    public static List<Map<String, String>> filterCsvData(String filePath, String columnName, String value) {
        List<Map<String, String>> allRecords = readCsvAsMaps(filePath);
        List<Map<String, String>> filteredRecords = new ArrayList<>();
        
        for (Map<String, String> record : allRecords) {
            if (record.containsKey(columnName) && value.equals(record.get(columnName))) {
                filteredRecords.add(record);
            }
        }
        
        logger.info("Filtered {} records from {} total records where {} = {}", 
                   filteredRecords.size(), allRecords.size(), columnName, value);
        
        return filteredRecords;
    }
    
    /**
     * Check if CSV file exists and is readable
     * @param filePath Path to the CSV file
     * @return true if file exists and is readable
     */
    public static boolean isCsvFileReadable(String filePath) {
        try {
            File file = new File(filePath);
            return file.exists() && file.canRead() && file.getName().toLowerCase().endsWith(".csv");
        } catch (Exception e) {
            logger.error("Error checking CSV file: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Get CSV file headers
     * @param filePath Path to the CSV file
     * @return List of header names
     */
    public static List<String> getCsvHeaders(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            List<String> headers = new ArrayList<>(csvParser.getHeaderNames());
            logger.info("Retrieved {} headers from CSV file: {}", headers.size(), filePath);
            return headers;
            
        } catch (IOException e) {
            logger.error("Error reading CSV headers from file: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV headers: " + filePath, e);
        }
    }
}
