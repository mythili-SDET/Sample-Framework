package com.automation.listeners;

import com.automation.core.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry Analyzer for failed tests
 * Implements retry logic based on configuration
 */
public class RetryListener implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(RetryListener.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private int retryCount = 0;
    private final int maxRetryCount = config.getRetryCount();
    private final int retryInterval = config.getRetryInterval();

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.warn("Retrying test: {} (Attempt {}/{})", 
                result.getName(), retryCount, maxRetryCount);
            
            // Wait before retry
            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Retry wait interrupted", e);
            }
            
            return true;
        }
        
        logger.error("Test failed after {} retry attempts: {}", maxRetryCount, result.getName());
        return false;
    }

    /**
     * Get current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Reset retry count
     */
    public void resetRetryCount() {
        retryCount = 0;
    }
}