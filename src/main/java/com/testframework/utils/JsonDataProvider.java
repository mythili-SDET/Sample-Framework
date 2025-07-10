package com.testframework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testframework.config.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * JSON Data Provider to handle JSON file operations
 * Supports reading and writing data from JSON files for test data management
 */
public class JsonDataProvider {
    
    private ConfigManager configManager;
    private ObjectMapper objectMapper;
    
    public JsonDataProvider() {
        configManager = ConfigManager.getInstance();
        objectMapper = new ObjectMapper();
    }
    
    /**
     * Read JSON data from file
     */
    public List<Map<String, Object>> readJsonData(String filePath) {
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }
    
    /**
     * Read JSON data using default configuration
     */
    public List<Map<String, Object>> readJsonData() {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        return readJsonData(filePath);
    }
    
    /**
     * Read JSON data as Map
     */
    public Map<String, Object> readJsonDataAsMap(String filePath) {
        try {
            return objectMapper.readValue(new File(filePath), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }
    
    /**
     * Read JSON data as Map using default configuration
     */
    public Map<String, Object> readJsonDataAsMap() {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        return readJsonDataAsMap(filePath);
    }
    
    /**
     * Get specific row data by index
     */
    public Map<String, Object> getRowData(String filePath, int rowIndex) {
        List<Map<String, Object>> allData = readJsonData(filePath);
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            return allData.get(rowIndex);
        }
        throw new IndexOutOfBoundsException("Row index " + rowIndex + " is out of bounds");
    }
    
    /**
     * Get specific row data by index using default configuration
     */
    public Map<String, Object> getRowData(int rowIndex) {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        return getRowData(filePath, rowIndex);
    }
    
    /**
     * Find row data by key value
     */
    public Map<String, Object> findRowByKeyValue(String filePath, String key, Object value) {
        List<Map<String, Object>> allData = readJsonData(filePath);
        
        for (Map<String, Object> row : allData) {
            if (value.equals(row.get(key))) {
                return row;
            }
        }
        
        return null;
    }
    
    /**
     * Find row data by key value using default configuration
     */
    public Map<String, Object> findRowByKeyValue(String key, Object value) {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        return findRowByKeyValue(filePath, key, value);
    }
    
    /**
     * Write data to JSON file
     */
    public void writeJsonData(String filePath, List<Map<String, Object>> data) {
        try {
            objectMapper.writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file: " + filePath, e);
        }
    }
    
    /**
     * Write data to JSON file using default configuration
     */
    public void writeJsonData(List<Map<String, Object>> data) {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        writeJsonData(filePath, data);
    }
    
    /**
     * Write Map data to JSON file
     */
    public void writeJsonData(String filePath, Map<String, Object> data) {
        try {
            objectMapper.writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file: " + filePath, e);
        }
    }
    
    /**
     * Write Map data to JSON file using default configuration
     */
    public void writeJsonData(Map<String, Object> data) {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        writeJsonData(filePath, data);
    }
    
    /**
     * Filter data by key value
     */
    public List<Map<String, Object>> filterDataByKeyValue(String filePath, String key, Object value) {
        List<Map<String, Object>> allData = readJsonData(filePath);
        List<Map<String, Object>> filteredData = new ArrayList<>();
        
        for (Map<String, Object> row : allData) {
            if (value.equals(row.get(key))) {
                filteredData.add(row);
            }
        }
        
        return filteredData;
    }
    
    /**
     * Filter data by key value using default configuration
     */
    public List<Map<String, Object>> filterDataByKeyValue(String key, Object value) {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        return filterDataByKeyValue(filePath, key, value);
    }
    
    /**
     * Get all keys from JSON data
     */
    public Set<String> getAllKeys(String filePath) {
        List<Map<String, Object>> allData = readJsonData(filePath);
        Set<String> allKeys = new HashSet<>();
        
        for (Map<String, Object> row : allData) {
            allKeys.addAll(row.keySet());
        }
        
        return allKeys;
    }
    
    /**
     * Get all keys using default configuration
     */
    public Set<String> getAllKeys() {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        return getAllKeys(filePath);
    }
    
    /**
     * Convert Map<String, String> to Map<String, Object>
     */
    public Map<String, Object> convertStringMapToObjectMap(Map<String, String> stringMap) {
        Map<String, Object> objectMap = new HashMap<>();
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            objectMap.put(entry.getKey(), entry.getValue());
        }
        return objectMap;
    }
    
    /**
     * Convert List<Map<String, String>> to List<Map<String, Object>>
     */
    public List<Map<String, Object>> convertStringMapListToObjectMapList(List<Map<String, String>> stringMapList) {
        List<Map<String, Object>> objectMapList = new ArrayList<>();
        for (Map<String, String> stringMap : stringMapList) {
            objectMapList.add(convertStringMapToObjectMap(stringMap));
        }
        return objectMapList;
    }
    
    /**
     * Pretty print JSON data
     */
    public String prettyPrintJson(String filePath) {
        try {
            Object prettyMapper = objectMapper.readValue(new File(filePath), Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(prettyMapper);
        } catch (IOException e) {
            throw new RuntimeException("Error pretty printing JSON file: " + filePath, e);
        }
    }
    
    /**
     * Pretty print JSON data using default configuration
     */
    public String prettyPrintJson() {
        String filePath = configManager.getDataFilePath() + configManager.getJsonFileName();
        return prettyPrintJson(filePath);
    }
}