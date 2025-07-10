package com.automation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * JSON Data Reader utility for reading test data from JSON files
 * Supports various JSON structures and formats
 */
public class JSONDataReader {
    private static final Logger logger = LogManager.getLogger(JSONDataReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Read all data from JSON file as List of Maps
     * @param filePath JSON file path
     * @return List of Maps containing data
     */
    public static List<Map<String, Object>> readJSONData(String filePath) {
        try {
            File file = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(file);
            
            List<Map<String, Object>> data = new ArrayList<>();
            
            if (rootNode.isArray()) {
                // JSON array format: [{"key1": "value1"}, {"key2": "value2"}]
                for (JsonNode node : rootNode) {
                    Map<String, Object> rowData = objectMapper.convertValue(node, 
                        new TypeReference<Map<String, Object>>() {});
                    data.add(rowData);
                }
            } else if (rootNode.isObject()) {
                // Check if it's a test data structure with "testData" key
                if (rootNode.has("testData") && rootNode.get("testData").isArray()) {
                    // Structure: {"testData": [{"key1": "value1"}, {"key2": "value2"}]}
                    JsonNode testDataNode = rootNode.get("testData");
                    for (JsonNode node : testDataNode) {
                        Map<String, Object> rowData = objectMapper.convertValue(node, 
                            new TypeReference<Map<String, Object>>() {});
                        data.add(rowData);
                    }
                } else {
                    // Single object format: {"key1": "value1", "key2": "value2"}
                    Map<String, Object> rowData = objectMapper.convertValue(rootNode, 
                        new TypeReference<Map<String, Object>>() {});
                    data.add(rowData);
                }
            }
            
            logger.info("Successfully read {} records from JSON file: {}", data.size(), filePath);
            return data;
            
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Read JSON data as String Maps for compatibility
     * @param filePath JSON file path
     * @return List of String Maps
     */
    public static List<Map<String, String>> readJSONDataAsStringMaps(String filePath) {
        List<Map<String, Object>> objectData = readJSONData(filePath);
        List<Map<String, String>> stringData = new ArrayList<>();
        
        for (Map<String, Object> objectMap : objectData) {
            Map<String, String> stringMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                stringMap.put(entry.getKey(), value);
            }
            stringData.add(stringMap);
        }
        
        return stringData;
    }
    
    /**
     * Read specific data set from JSON by key
     * @param filePath JSON file path
     * @param dataSetKey Key of the data set to read
     * @return List of Maps containing data
     */
    public static List<Map<String, Object>> readJSONDataSet(String filePath, String dataSetKey) {
        try {
            File file = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(file);
            
            if (rootNode.has(dataSetKey)) {
                JsonNode dataSetNode = rootNode.get(dataSetKey);
                List<Map<String, Object>> data = new ArrayList<>();
                
                if (dataSetNode.isArray()) {
                    for (JsonNode node : dataSetNode) {
                        Map<String, Object> rowData = objectMapper.convertValue(node, 
                            new TypeReference<Map<String, Object>>() {});
                        data.add(rowData);
                    }
                } else if (dataSetNode.isObject()) {
                    Map<String, Object> rowData = objectMapper.convertValue(dataSetNode, 
                        new TypeReference<Map<String, Object>>() {});
                    data.add(rowData);
                }
                
                logger.info("Successfully read {} records from data set '{}' in JSON file: {}", 
                    data.size(), dataSetKey, filePath);
                return data;
            } else {
                logger.error("Data set '{}' not found in JSON file: {}", dataSetKey, filePath);
                throw new RuntimeException("Data set not found: " + dataSetKey);
            }
            
        } catch (IOException e) {
            logger.error("Error reading JSON data set '{}' from file: {}", dataSetKey, filePath, e);
            throw new RuntimeException("Failed to read JSON data set: " + dataSetKey, e);
        }
    }
    
    /**
     * Read specific row from JSON data
     * @param filePath JSON file path
     * @param rowIndex Row index (0-based)
     * @return Map containing row data
     */
    public static Map<String, Object> readJSONRow(String filePath, int rowIndex) {
        List<Map<String, Object>> allData = readJSONData(filePath);
        
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            return allData.get(rowIndex);
        } else {
            logger.error("Row index {} is out of bounds. Total rows: {}", rowIndex, allData.size());
            throw new IndexOutOfBoundsException("Row index out of bounds: " + rowIndex);
        }
    }
    
    /**
     * Read data from JSON and convert to Object array for TestNG DataProvider
     * @param filePath JSON file path
     * @return Object[][] for TestNG DataProvider
     */
    public static Object[][] readJSONDataForTestNG(String filePath) {
        List<Map<String, Object>> data = readJSONData(filePath);
        Object[][] testData = new Object[data.size()][1];
        
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i);
        }
        
        return testData;
    }
    
    /**
     * Read nested JSON data by path
     * @param filePath JSON file path
     * @param jsonPath JSON path (e.g., "users.testData")
     * @return List of Maps containing data
     */
    public static List<Map<String, Object>> readJSONDataByPath(String filePath, String jsonPath) {
        try {
            File file = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(file);
            
            JsonNode targetNode = rootNode;
            String[] pathParts = jsonPath.split("\\.");
            
            for (String part : pathParts) {
                if (targetNode.has(part)) {
                    targetNode = targetNode.get(part);
                } else {
                    logger.error("Path '{}' not found in JSON file: {}", jsonPath, filePath);
                    throw new RuntimeException("JSON path not found: " + jsonPath);
                }
            }
            
            List<Map<String, Object>> data = new ArrayList<>();
            
            if (targetNode.isArray()) {
                for (JsonNode node : targetNode) {
                    Map<String, Object> rowData = objectMapper.convertValue(node, 
                        new TypeReference<Map<String, Object>>() {});
                    data.add(rowData);
                }
            } else if (targetNode.isObject()) {
                Map<String, Object> rowData = objectMapper.convertValue(targetNode, 
                    new TypeReference<Map<String, Object>>() {});
                data.add(rowData);
            }
            
            logger.info("Successfully read {} records from JSON path '{}' in file: {}", 
                data.size(), jsonPath, filePath);
            return data;
            
        } catch (IOException e) {
            logger.error("Error reading JSON data by path '{}' from file: {}", jsonPath, filePath, e);
            throw new RuntimeException("Failed to read JSON data by path: " + jsonPath, e);
        }
    }
    
    /**
     * Get value from JSON by key path
     * @param filePath JSON file path
     * @param keyPath Key path (e.g., "config.database.url")
     * @return Value as Object
     */
    public static Object getJSONValue(String filePath, String keyPath) {
        try {
            File file = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(file);
            
            JsonNode targetNode = rootNode;
            String[] pathParts = keyPath.split("\\.");
            
            for (String part : pathParts) {
                if (targetNode.has(part)) {
                    targetNode = targetNode.get(part);
                } else {
                    logger.error("Key path '{}' not found in JSON file: {}", keyPath, filePath);
                    return null;
                }
            }
            
            if (targetNode.isTextual()) {
                return targetNode.asText();
            } else if (targetNode.isNumber()) {
                return targetNode.asDouble();
            } else if (targetNode.isBoolean()) {
                return targetNode.asBoolean();
            } else {
                return objectMapper.convertValue(targetNode, Object.class);
            }
            
        } catch (IOException e) {
            logger.error("Error reading JSON value by key path '{}' from file: {}", keyPath, filePath, e);
            throw new RuntimeException("Failed to read JSON value by key path: " + keyPath, e);
        }
    }
    
    /**
     * Filter JSON data based on field value
     * @param filePath JSON file path
     * @param fieldName Field name to filter by
     * @param value Value to filter for
     * @return List of matching records
     */
    public static List<Map<String, Object>> filterJSONData(String filePath, String fieldName, Object value) {
        List<Map<String, Object>> allData = readJSONData(filePath);
        List<Map<String, Object>> filteredData = new ArrayList<>();
        
        for (Map<String, Object> record : allData) {
            Object fieldValue = record.get(fieldName);
            if (Objects.equals(value, fieldValue)) {
                filteredData.add(record);
            }
        }
        
        logger.info("Filtered {} records from JSON file based on {}={}", 
            filteredData.size(), fieldName, value);
        return filteredData;
    }
    
    /**
     * Get all available data set keys from JSON file
     * @param filePath JSON file path
     * @return Set of data set keys
     */
    public static Set<String> getDataSetKeys(String filePath) {
        try {
            File file = new File(filePath);
            JsonNode rootNode = objectMapper.readTree(file);
            
            Set<String> keys = new HashSet<>();
            rootNode.fieldNames().forEachRemaining(keys::add);
            
            logger.info("Found {} data set keys in JSON file: {}", keys.size(), filePath);
            return keys;
            
        } catch (IOException e) {
            logger.error("Error reading data set keys from JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read data set keys from JSON file: " + filePath, e);
        }
    }
    
    /**
     * Convert JSON file to pretty formatted string
     * @param filePath JSON file path
     * @return Pretty formatted JSON string
     */
    public static String prettyPrintJSON(String filePath) {
        try {
            File file = new File(filePath);
            Object json = objectMapper.readValue(file, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            
        } catch (IOException e) {
            logger.error("Error pretty printing JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to pretty print JSON file: " + filePath, e);
        }
    }
}