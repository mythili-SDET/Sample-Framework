package com.automation.utils;

import com.automation.config.LoggerManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Utility class for JSON file operations
 * Provides methods to read, write, and manipulate JSON files
 */
public class JsonUtil {
    
    private static final Logger logger = LoggerManager.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    /**
     * Read JSON file and return as Map
     * @param filePath Path to the JSON file
     * @return Map representation of JSON
     */
    public static Map<String, Object> readJsonAsMap(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Map<String, Object> jsonMap = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {});
            logger.info("Successfully read JSON file: {}", filePath);
            return jsonMap;
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Read JSON file and return as List
     * @param filePath Path to the JSON file
     * @return List representation of JSON
     */
    public static List<Object> readJsonAsList(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            List<Object> jsonList = objectMapper.readValue(content, new TypeReference<List<Object>>() {});
            logger.info("Successfully read JSON file as list: {}", filePath);
            return jsonList;
        } catch (IOException e) {
            logger.error("Error reading JSON file as list: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file as list: " + filePath, e);
        }
    }
    
    /**
     * Read JSON file and return as specific type
     * @param filePath Path to the JSON file
     * @param clazz Target class type
     * @return Object of specified type
     */
    public static <T> T readJsonAsObject(String filePath, Class<T> clazz) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            T object = objectMapper.readValue(content, clazz);
            logger.info("Successfully read JSON file as {}: {}", clazz.getSimpleName(), filePath);
            return object;
        } catch (IOException e) {
            logger.error("Error reading JSON file as {}: {}", clazz.getSimpleName(), filePath, e);
            throw new RuntimeException("Failed to read JSON file as " + clazz.getSimpleName() + ": " + filePath, e);
        }
    }
    
    /**
     * Write object to JSON file
     * @param filePath Path to the JSON file
     * @param object Object to write
     */
    public static void writeJson(String filePath, Object object) {
        try {
            objectMapper.writeValue(new File(filePath), object);
            logger.info("Successfully wrote object to JSON file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error writing JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to write JSON file: " + filePath, e);
        }
    }
    
    /**
     * Write Map to JSON file
     * @param filePath Path to the JSON file
     * @param map Map to write
     */
    public static void writeJsonFromMap(String filePath, Map<String, Object> map) {
        try {
            objectMapper.writeValue(new File(filePath), map);
            logger.info("Successfully wrote Map to JSON file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error writing Map to JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to write Map to JSON file: " + filePath, e);
        }
    }
    
    /**
     * Write List to JSON file
     * @param filePath Path to the JSON file
     * @param list List to write
     */
    public static void writeJsonFromList(String filePath, List<Object> list) {
        try {
            objectMapper.writeValue(new File(filePath), list);
            logger.info("Successfully wrote List to JSON file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error writing List to JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to write List to JSON file: " + filePath, e);
        }
    }
    
    /**
     * Get value from JSON using JSONPath-like syntax
     * @param jsonString JSON string
     * @param jsonPath Path to the value (e.g., "user.name", "users[0].email")
     * @return Value at the specified path
     */
    public static Object getValueFromJsonPath(String jsonString, String jsonPath) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            return getValueFromJsonNode(rootNode, jsonPath);
        } catch (IOException e) {
            logger.error("Error parsing JSON string for path: {}", jsonPath, e);
            throw new RuntimeException("Failed to parse JSON string for path: " + jsonPath, e);
        }
    }
    
    /**
     * Get value from JSON file using JSONPath-like syntax
     * @param filePath Path to the JSON file
     * @param jsonPath Path to the value
     * @return Value at the specified path
     */
    public static Object getValueFromJsonPath(String filePath, String jsonPath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return getValueFromJsonPath(content, jsonPath);
        } catch (IOException e) {
            logger.error("Error reading JSON file for path: {} in file: {}", jsonPath, filePath, e);
            throw new RuntimeException("Failed to read JSON file for path: " + jsonPath, e);
        }
    }
    
    /**
     * Helper method to get value from JsonNode using path
     * @param node JsonNode to search
     * @param path Path to the value
     * @return Value at the specified path
     */
    private static Object getValueFromJsonNode(JsonNode node, String path) {
        String[] pathParts = path.split("\\.");
        JsonNode currentNode = node;
        
        for (String part : pathParts) {
            if (part.contains("[") && part.contains("]")) {
                // Handle array access like "users[0]"
                String arrayName = part.substring(0, part.indexOf("["));
                int index = Integer.parseInt(part.substring(part.indexOf("[") + 1, part.indexOf("]")));
                currentNode = currentNode.get(arrayName).get(index);
            } else {
                currentNode = currentNode.get(part);
            }
            
            if (currentNode == null) {
                logger.warn("Path not found: {}", path);
                return null;
            }
        }
        
        if (currentNode.isTextual()) {
            return currentNode.asText();
        } else if (currentNode.isNumber()) {
            return currentNode.asDouble();
        } else if (currentNode.isBoolean()) {
            return currentNode.asBoolean();
        } else {
            return currentNode.toString();
        }
    }
    
    /**
     * Convert object to JSON string
     * @param object Object to convert
     * @return JSON string representation
     */
    public static String objectToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Error converting object to JSON string", e);
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }
    
    /**
     * Convert JSON string to Map
     * @param jsonString JSON string
     * @return Map representation
     */
    public static Map<String, Object> jsonStringToMap(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            logger.error("Error converting JSON string to Map", e);
            throw new RuntimeException("Failed to convert JSON string to Map", e);
        }
    }
    
    /**
     * Check if JSON string is valid
     * @param jsonString JSON string to validate
     * @return true if valid JSON
     */
    public static boolean isValidJson(String jsonString) {
        try {
            objectMapper.readTree(jsonString);
            return true;
        } catch (IOException e) {
            logger.warn("Invalid JSON string: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if JSON file exists and is readable
     * @param filePath Path to the JSON file
     * @return true if file exists and is readable
     */
    public static boolean isJsonFileReadable(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead() || !file.getName().toLowerCase().endsWith(".json")) {
                return false;
            }
            
            // Try to parse the file to ensure it's valid JSON
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            objectMapper.readTree(content);
            return true;
        } catch (Exception e) {
            logger.error("Error checking JSON file: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Pretty print JSON string
     * @param jsonString JSON string to format
     * @return Formatted JSON string
     */
    public static String prettyPrintJson(String jsonString) {
        try {
            Object jsonObject = objectMapper.readValue(jsonString, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (IOException e) {
            logger.error("Error pretty printing JSON string", e);
            return jsonString; // Return original if formatting fails
        }
    }
}
