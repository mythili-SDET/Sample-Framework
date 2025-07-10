package com.automation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * JSON Data Provider utility for reading test data from JSON files
 */
public class JSONDataProvider {
    private static final Logger logger = LogManager.getLogger(JSONDataProvider.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Read JSON data from file
     */
    public static List<Map<String, Object>> readJSONData(String filePath) {
        try {
            logger.info("Reading JSON data from: {}", filePath);
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Read JSON data from input stream
     */
    public static List<Map<String, Object>> readJSONData(InputStream inputStream) {
        try {
            return objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            logger.error("Error reading JSON from input stream", e);
            throw new RuntimeException("Failed to read JSON from input stream", e);
        }
    }

    /**
     * Read JSON data as Map
     */
    public static Map<String, Object> readJSONAsMap(String filePath) {
        try {
            logger.info("Reading JSON data as Map from: {}", filePath);
            return objectMapper.readValue(new File(filePath), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            logger.error("Error reading JSON file as Map: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file as Map: " + filePath, e);
        }
    }

    /**
     * Read JSON data as Map from input stream
     */
    public static Map<String, Object> readJSONAsMap(InputStream inputStream) {
        try {
            return objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            logger.error("Error reading JSON as Map from input stream", e);
            throw new RuntimeException("Failed to read JSON as Map from input stream", e);
        }
    }

    /**
     * Read test data from configured JSON file
     */
    public static List<Map<String, Object>> readTestData() {
        String dataPath = config.getProperty("test.data.path");
        String jsonFile = config.getProperty("test.data.json");
        String fullPath = dataPath + jsonFile;
        
        logger.info("Reading test data from: {}", fullPath);
        return readJSONData(fullPath);
    }

    /**
     * Read test data as Map from configured JSON file
     */
    public static Map<String, Object> readTestDataAsMap() {
        String dataPath = config.getProperty("test.data.path");
        String jsonFile = config.getProperty("test.data.json");
        String fullPath = dataPath + jsonFile;
        
        logger.info("Reading test data as Map from: {}", fullPath);
        return readJSONAsMap(fullPath);
    }

    /**
     * Convert object to JSON string
     */
    public static String toJSONString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Error converting object to JSON string", e);
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }

    /**
     * Convert JSON string to Map
     */
    public static Map<String, Object> fromJSONString(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            logger.error("Error parsing JSON string", e);
            throw new RuntimeException("Failed to parse JSON string", e);
        }
    }

    /**
     * Convert JSON string to List of Maps
     */
    public static List<Map<String, Object>> fromJSONStringToList(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            logger.error("Error parsing JSON string to list", e);
            throw new RuntimeException("Failed to parse JSON string to list", e);
        }
    }
}