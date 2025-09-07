package com.automation.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Centralized Logger Manager for UI, API, and DB operations
 * Provides thread-safe logging with different log levels and categories
 */
public class LoggerManager {
    
    private static final ConcurrentHashMap<String, Logger> loggerCache = new ConcurrentHashMap<>();
    private static final String DEFAULT_CONFIG_FILE = "src/main/resources/log4j2.xml";
    private static boolean initialized = false;
    
    /**
     * Initialize logger configuration
     */
    public static synchronized void initialize() {
        if (!initialized) {
            try {
                File configFile = new File(DEFAULT_CONFIG_FILE);
                if (configFile.exists()) {
                    Configurator.initialize(null, DEFAULT_CONFIG_FILE);
                }
                initialized = true;
            } catch (Exception e) {
                System.err.println("Failed to initialize logger configuration: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get logger for a specific class
     * @param clazz Class to get logger for
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        initialize();
        return getLogger(clazz.getName());
    }
    
    /**
     * Get logger for a specific name
     * @param name Logger name
     * @return Logger instance
     */
    public static Logger getLogger(String name) {
        initialize();
        return loggerCache.computeIfAbsent(name, LogManager::getLogger);
    }
    
    /**
     * Get UI logger
     * @return UI-specific logger
     */
    public static Logger getUILogger() {
        return getLogger("UI");
    }
    
    /**
     * Get API logger
     * @return API-specific logger
     */
    public static Logger getAPILogger() {
        return getLogger("API");
    }
    
    /**
     * Get DB logger
     * @return DB-specific logger
     */
    public static Logger getDBLogger() {
        return getLogger("DB");
    }
    
    /**
     * Get Test logger
     * @return Test-specific logger
     */
    public static Logger getTestLogger() {
        return getLogger("TEST");
    }
    
    /**
     * Get Framework logger
     * @return Framework-specific logger
     */
    public static Logger getFrameworkLogger() {
        return getLogger("FRAMEWORK");
    }
    
    /**
     * Log UI operation
     * @param operation UI operation description
     * @param element Element being interacted with
     * @param value Value being set (if applicable)
     */
    public static void logUIOperation(String operation, String element, String value) {
        Logger logger = getUILogger();
        if (value != null && !value.isEmpty()) {
            logger.info("UI Operation: {} on element '{}' with value '{}'", operation, element, value);
        } else {
            logger.info("UI Operation: {} on element '{}'", operation, element);
        }
    }
    
    /**
     * Log API request
     * @param method HTTP method
     * @param url Request URL
     * @param requestBody Request body (if applicable)
     */
    public static void logAPIRequest(String method, String url, String requestBody) {
        Logger logger = getAPILogger();
        if (requestBody != null && !requestBody.isEmpty()) {
            logger.info("API Request: {} {} with body: {}", method, url, requestBody);
        } else {
            logger.info("API Request: {} {}", method, url);
        }
    }
    
    /**
     * Log API response
     * @param statusCode Response status code
     * @param responseBody Response body
     * @param responseTime Response time in milliseconds
     */
    public static void logAPIResponse(int statusCode, String responseBody, long responseTime) {
        Logger logger = getAPILogger();
        logger.info("API Response: Status {} in {}ms, Body: {}", statusCode, responseTime, responseBody);
    }
    
    /**
     * Log database operation
     * @param operation DB operation type
     * @param query SQL query
     * @param parameters Query parameters (if applicable)
     */
    public static void logDBOperation(String operation, String query, Object... parameters) {
        Logger logger = getDBLogger();
        if (parameters != null && parameters.length > 0) {
            logger.info("DB Operation: {} - Query: {} with parameters: {}", operation, query, java.util.Arrays.toString(parameters));
        } else {
            logger.info("DB Operation: {} - Query: {}", operation, query);
        }
    }
    
    /**
     * Log test step
     * @param stepDescription Step description
     * @param status Step status (PASS/FAIL/SKIP)
     */
    public static void logTestStep(String stepDescription, String status) {
        Logger logger = getTestLogger();
        logger.info("Test Step: {} - Status: {}", stepDescription, status);
    }
    
    /**
     * Log framework event
     * @param event Event description
     * @param details Event details
     */
    public static void logFrameworkEvent(String event, String details) {
        Logger logger = getFrameworkLogger();
        logger.info("Framework Event: {} - Details: {}", event, details);
    }
    
    /**
     * Log error with context
     * @param logger Logger instance
     * @param context Context where error occurred
     * @param error Error message
     * @param throwable Exception (if applicable)
     */
    public static void logError(Logger logger, String context, String error, Throwable throwable) {
        if (throwable != null) {
            logger.error("Error in {}: {} - Exception: {}", context, error, throwable.getMessage(), throwable);
        } else {
            logger.error("Error in {}: {}", context, error);
        }
    }
    
    /**
     * Log warning with context
     * @param logger Logger instance
     * @param context Context where warning occurred
     * @param warning Warning message
     */
    public static void logWarning(Logger logger, String context, String warning) {
        logger.warn("Warning in {}: {}", context, warning);
    }
    
    /**
     * Log info with context
     * @param logger Logger instance
     * @param context Context where info occurred
     * @param info Info message
     */
    public static void logInfo(Logger logger, String context, String info) {
        logger.info("Info in {}: {}", context, info);
    }
    
    /**
     * Log debug with context
     * @param logger Logger instance
     * @param context Context where debug occurred
     * @param debug Debug message
     */
    public static void logDebug(Logger logger, String context, String debug) {
        logger.debug("Debug in {}: {}", context, debug);
    }
    
    /**
     * Clear logger cache (useful for testing)
     */
    public static void clearCache() {
        loggerCache.clear();
    }
    
    /**
     * Shutdown all loggers
     */
    public static void shutdown() {
        LogManager.shutdown();
        loggerCache.clear();
        initialized = false;
    }
}