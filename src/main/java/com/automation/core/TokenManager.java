package com.automation.core;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class for managing authentication tokens across the framework
 * Thread-safe implementation for parallel execution
 */
public class TokenManager {
    private static final Logger logger = LogManager.getLogger(TokenManager.class);
    private static volatile TokenManager instance;
    private static final Object lock = new Object();
    
    private final ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();
    private final ConfigManager config = ConfigManager.getInstance();
    
    private TokenManager() {}
    
    /**
     * Get singleton instance
     * @return TokenManager instance
     */
    public static TokenManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new TokenManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get authentication token for current thread
     * @return Authentication token
     */
    public String getToken() {
        String threadId = Thread.currentThread().getName();
        String token = tokens.get(threadId);
        
        if (token == null || token.isEmpty()) {
            token = generateToken();
            if (token != null) {
                tokens.put(threadId, token);
            }
        }
        
        return token;
    }
    
    /**
     * Set authentication token for current thread
     * @param token Authentication token
     */
    public void setToken(String token) {
        String threadId = Thread.currentThread().getName();
        tokens.put(threadId, token);
        logger.info("Token set for thread: {}", threadId);
    }
    
    /**
     * Refresh authentication token for current thread
     * @return New authentication token
     */
    public String refreshToken() {
        String threadId = Thread.currentThread().getName();
        logger.info("Refreshing token for thread: {}", threadId);
        
        String newToken = generateToken();
        if (newToken != null) {
            tokens.put(threadId, newToken);
            logger.info("Token refreshed successfully for thread: {}", threadId);
        }
        
        return newToken;
    }
    
    /**
     * Clear token for current thread
     */
    public void clearToken() {
        String threadId = Thread.currentThread().getName();
        tokens.remove(threadId);
        logger.info("Token cleared for thread: {}", threadId);
    }
    
    /**
     * Clear all tokens
     */
    public void clearAllTokens() {
        tokens.clear();
        logger.info("All tokens cleared");
    }
    
    /**
     * Generate new authentication token
     * @return Generated token
     */
    private String generateToken() {
        try {
            String authUrl = config.getAuthUrl();
            String username = config.getUsername();
            String password = config.getPassword();
            
            if (authUrl == null || username == null || password == null) {
                logger.warn("Authentication credentials not configured");
                return null;
            }
            
            logger.info("Generating authentication token");
            
            // Create authentication request body
            String requestBody = String.format(
                "{ \"username\": \"%s\", \"password\": \"%s\" }",
                username, password
            );
            
            // Execute authentication request
            Response response = RestAssured.given()
                    .contentType("application/json")
                    .body(requestBody)
                    .post(authUrl);
            
            if (response.getStatusCode() == 200) {
                String token = response.jsonPath().getString("token");
                if (token == null) {
                    // Try alternative token field names
                    token = response.jsonPath().getString("access_token");
                    if (token == null) {
                        token = response.jsonPath().getString("accessToken");
                    }
                }
                
                if (token != null) {
                    logger.info("Authentication token generated successfully");
                    return token;
                } else {
                    logger.error("Token not found in authentication response");
                }
            } else {
                logger.error("Authentication failed. Status: {}, Response: {}", 
                    response.getStatusCode(), response.getBody().asString());
            }
            
        } catch (Exception e) {
            logger.error("Error generating authentication token", e);
        }
        
        return null;
    }
    
    /**
     * Check if token exists for current thread
     * @return true if token exists, false otherwise
     */
    public boolean hasToken() {
        String threadId = Thread.currentThread().getName();
        return tokens.containsKey(threadId) && 
               tokens.get(threadId) != null && 
               !tokens.get(threadId).isEmpty();
    }
    
    /**
     * Get token for specific thread
     * @param threadId Thread ID
     * @return Token for the specified thread
     */
    public String getTokenForThread(String threadId) {
        return tokens.get(threadId);
    }
}