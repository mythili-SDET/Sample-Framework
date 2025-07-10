package com.automation.utils;

import com.automation.core.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Unified Data Provider utility for all data formats
 * Supports Excel, CSV, and JSON data sources
 */
public class DataProvider {
    private static final Logger logger = LogManager.getLogger(DataProvider.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    public enum DataFormat {
        EXCEL, CSV, JSON
    }
    
    /**
     * Get test data based on format
     * @param filePath Data file path
     * @param format Data format (EXCEL, CSV, JSON)
     * @return List of Maps containing test data
     */
    public static List<Map<String, String>> getTestData(String filePath, DataFormat format) {
        logger.info("Reading test data from {} file: {}", format, filePath);
        
        switch (format) {
            case EXCEL:
                String sheetName = config.getProperty("excel.default.sheet", "Sheet1");
                return ExcelDataReader.readExcelData(filePath, sheetName);
            case CSV:
                return CSVDataReader.readCSVData(filePath);
            case JSON:
                return JSONDataReader.readJSONDataAsStringMaps(filePath);
            default:
                throw new IllegalArgumentException("Unsupported data format: " + format);
        }
    }
    
    /**
     * Get test data with additional parameters
     * @param filePath Data file path
     * @param format Data format
     * @param additionalParam Additional parameter (sheet name for Excel, data set key for JSON)
     * @return List of Maps containing test data
     */
    public static List<Map<String, String>> getTestData(String filePath, DataFormat format, String additionalParam) {
        logger.info("Reading test data from {} file: {} with parameter: {}", format, filePath, additionalParam);
        
        switch (format) {
            case EXCEL:
                return ExcelDataReader.readExcelData(filePath, additionalParam);
            case CSV:
                char separator = additionalParam.charAt(0);
                return CSVDataReader.readCSVData(filePath, separator);
            case JSON:
                return convertObjectMapsToStringMaps(JSONDataReader.readJSONDataSet(filePath, additionalParam));
            default:
                throw new IllegalArgumentException("Unsupported data format: " + format);
        }
    }
    
    /**
     * Get test data for TestNG DataProvider
     * @param filePath Data file path
     * @param format Data format
     * @return Object[][] for TestNG DataProvider
     */
    public static Object[][] getTestDataForTestNG(String filePath, DataFormat format) {
        switch (format) {
            case EXCEL:
                String sheetName = config.getProperty("excel.default.sheet", "Sheet1");
                return ExcelDataReader.readExcelDataForTestNG(filePath, sheetName);
            case CSV:
                return CSVDataReader.readCSVDataForTestNG(filePath);
            case JSON:
                return JSONDataReader.readJSONDataForTestNG(filePath);
            default:
                throw new IllegalArgumentException("Unsupported data format: " + format);
        }
    }
    
    /**
     * Get test data for TestNG DataProvider with additional parameter
     * @param filePath Data file path
     * @param format Data format
     * @param additionalParam Additional parameter
     * @return Object[][] for TestNG DataProvider
     */
    public static Object[][] getTestDataForTestNG(String filePath, DataFormat format, String additionalParam) {
        List<Map<String, String>> data = getTestData(filePath, format, additionalParam);
        Object[][] testData = new Object[data.size()][1];
        
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i);
        }
        
        return testData;
    }
    
    /**
     * Auto-detect data format based on file extension
     * @param filePath Data file path
     * @return DataFormat enum
     */
    public static DataFormat autoDetectFormat(String filePath) {
        String lowerCasePath = filePath.toLowerCase();
        
        if (lowerCasePath.endsWith(".xlsx") || lowerCasePath.endsWith(".xls")) {
            return DataFormat.EXCEL;
        } else if (lowerCasePath.endsWith(".csv")) {
            return DataFormat.CSV;
        } else if (lowerCasePath.endsWith(".json")) {
            return DataFormat.JSON;
        } else {
            throw new IllegalArgumentException("Cannot auto-detect format for file: " + filePath);
        }
    }
    
    /**
     * Get test data with auto-detection
     * @param filePath Data file path
     * @return List of Maps containing test data
     */
    public static List<Map<String, String>> getTestDataAutoDetect(String filePath) {
        DataFormat format = autoDetectFormat(filePath);
        return getTestData(filePath, format);
    }
    
    /**
     * Get specific row of test data
     * @param filePath Data file path
     * @param format Data format
     * @param rowIndex Row index (0-based)
     * @return Map containing row data
     */
    public static Map<String, String> getTestDataRow(String filePath, DataFormat format, int rowIndex) {
        switch (format) {
            case EXCEL:
                String sheetName = config.getProperty("excel.default.sheet", "Sheet1");
                return ExcelDataReader.readExcelRow(filePath, sheetName, rowIndex);
            case CSV:
                return CSVDataReader.readCSVRow(filePath, rowIndex);
            case JSON:
                Map<String, Object> objectMap = JSONDataReader.readJSONRow(filePath, rowIndex);
                return convertObjectMapToStringMap(objectMap);
            default:
                throw new IllegalArgumentException("Unsupported data format: " + format);
        }
    }
    
    /**
     * Filter test data based on column value
     * @param filePath Data file path
     * @param format Data format
     * @param columnName Column name to filter by
     * @param value Value to filter for
     * @return List of Maps containing filtered data
     */
    public static List<Map<String, String>> filterTestData(String filePath, DataFormat format, 
                                                           String columnName, String value) {
        switch (format) {
            case CSV:
                return CSVDataReader.filterCSVData(filePath, columnName, value);
            case JSON:
                return convertObjectMapsToStringMaps(JSONDataReader.filterJSONData(filePath, columnName, value));
            case EXCEL:
                // For Excel, we'll read all data and filter it
                List<Map<String, String>> allData = getTestData(filePath, format);
                return allData.stream()
                        .filter(row -> value.equals(row.get(columnName)))
                        .collect(java.util.stream.Collectors.toList());
            default:
                throw new IllegalArgumentException("Unsupported data format: " + format);
        }
    }
    
    /**
     * Get row count from data file
     * @param filePath Data file path
     * @param format Data format
     * @return Number of data rows
     */
    public static int getRowCount(String filePath, DataFormat format) {
        switch (format) {
            case CSV:
                return CSVDataReader.getRowCount(filePath);
            case EXCEL:
            case JSON:
                return getTestData(filePath, format).size();
            default:
                throw new IllegalArgumentException("Unsupported data format: " + format);
        }
    }
    
    /**
     * Get data from environment-specific file
     * @param baseFileName Base file name without environment suffix
     * @param format Data format
     * @return List of Maps containing test data
     */
    public static List<Map<String, String>> getEnvironmentData(String baseFileName, DataFormat format) {
        String environment = config.getEnvironment();
        String extension = getFileExtension(format);
        String environmentFile = baseFileName + "_" + environment + extension;
        
        logger.info("Loading environment-specific data for {}: {}", environment, environmentFile);
        return getTestData(environmentFile, format);
    }
    
    /**
     * Get data from classpath resources
     * @param resourcePath Resource path in classpath
     * @param format Data format
     * @return List of Maps containing test data
     */
    public static List<Map<String, String>> getResourceData(String resourcePath, DataFormat format) {
        String fullPath = DataProvider.class.getClassLoader().getResource(resourcePath).getPath();
        return getTestData(fullPath, format);
    }
    
    /**
     * Combine data from multiple files
     * @param filePaths Array of file paths
     * @param format Data format
     * @return Combined list of Maps containing test data
     */
    public static List<Map<String, String>> combineTestData(String[] filePaths, DataFormat format) {
        List<Map<String, String>> combinedData = new java.util.ArrayList<>();
        
        for (String filePath : filePaths) {
            List<Map<String, String>> fileData = getTestData(filePath, format);
            combinedData.addAll(fileData);
        }
        
        logger.info("Combined test data from {} files. Total rows: {}", filePaths.length, combinedData.size());
        return combinedData;
    }
    
    /**
     * Convert Object Maps to String Maps for compatibility
     */
    private static List<Map<String, String>> convertObjectMapsToStringMaps(List<Map<String, Object>> objectMaps) {
        List<Map<String, String>> stringMaps = new java.util.ArrayList<>();
        
        for (Map<String, Object> objectMap : objectMaps) {
            stringMaps.add(convertObjectMapToStringMap(objectMap));
        }
        
        return stringMaps;
    }
    
    /**
     * Convert Object Map to String Map
     */
    private static Map<String, String> convertObjectMapToStringMap(Map<String, Object> objectMap) {
        Map<String, String> stringMap = new java.util.HashMap<>();
        
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            stringMap.put(entry.getKey(), value);
        }
        
        return stringMap;
    }
    
    /**
     * Get file extension based on format
     */
    private static String getFileExtension(DataFormat format) {
        switch (format) {
            case EXCEL:
                return ".xlsx";
            case CSV:
                return ".csv";
            case JSON:
                return ".json";
            default:
                throw new IllegalArgumentException("Unsupported data format: " + format);
        }
    }
    
    // TestNG DataProvider methods for easy integration
    
    @org.testng.annotations.DataProvider(name = "excelData")
    public static Object[][] excelDataProvider() {
        String filePath = config.getProperty("test.data.excel.path");
        if (filePath != null) {
            return getTestDataForTestNG(filePath, DataFormat.EXCEL);
        }
        throw new RuntimeException("Excel data file path not configured");
    }
    
    @org.testng.annotations.DataProvider(name = "csvData")
    public static Object[][] csvDataProvider() {
        String filePath = config.getProperty("test.data.csv.path");
        if (filePath != null) {
            return getTestDataForTestNG(filePath, DataFormat.CSV);
        }
        throw new RuntimeException("CSV data file path not configured");
    }
    
    @org.testng.annotations.DataProvider(name = "jsonData")
    public static Object[][] jsonDataProvider() {
        String filePath = config.getProperty("test.data.json.path");
        if (filePath != null) {
            return getTestDataForTestNG(filePath, DataFormat.JSON);
        }
        throw new RuntimeException("JSON data file path not configured");
    }
}