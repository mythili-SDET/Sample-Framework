package com.automation.utils;

import com.automation.config.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * TestDataProvider utility for TestNG DataProvider methods
 * Provides Excel-based test data with execution control
 */
public class TestDataProvider {
    private static final Logger logger = LoggerManager.getInstance().getLogger(TestDataProvider.class);
    
    private static final String DEFAULT_EXCEL_PATH = "src/test/resources/testdata/TestData.xlsx";
    
    /**
     * DataProvider for UI test data
     */
    @DataProvider(name = "uiTestData")
    public static Iterator<Object[]> getUITestData() {
        return getTestData("UITestData");
    }
    
    /**
     * DataProvider for API test data
     */
    @DataProvider(name = "apiTestData")
    public static Iterator<Object[]> getAPITestData() {
        return getTestData("APITestData");
    }
    
    /**
     * DataProvider for search test data
     */
    @DataProvider(name = "searchTestData")
    public static Iterator<Object[]> getSearchTestData() {
        return getTestData("SearchData");
    }
    
    /**
     * DataProvider for login test data
     */
    @DataProvider(name = "loginTestData")
    public static Iterator<Object[]> getLoginTestData() {
        return getTestData("LoginData");
    }
    
    /**
     * DataProvider for database test data
     */
    @DataProvider(name = "dbTestData")
    public static Iterator<Object[]> getDBTestData() {
        return getTestData("DBTestData");
    }
    
    /**
     * Generic method to get test data from Excel sheet
     */
    private static Iterator<Object[]> getTestData(String sheetName) {
        List<Object[]> data = new ArrayList<>();
        
        try {
            // Read executable data from Excel
            List<Map<String, String>> excelData = ExcelUtil.readExecutableData(DEFAULT_EXCEL_PATH, sheetName);
            
            // Convert to TestNG DataProvider format
            for (Map<String, String> row : excelData) {
                data.add(new Object[]{row});
                logger.debug("Added test data for TestCaseId: {}", row.get("TestCaseId"));
            }
            
            logger.info("Loaded {} test data sets from sheet: {}", data.size(), sheetName);
            
        } catch (Exception e) {
            logger.error("Error loading test data from sheet: {}", sheetName, e);
            // Return empty data instead of failing
            data.add(new Object[]{Map.of("error", "Failed to load test data", "message", e.getMessage())});
        }
        
        return data.iterator();
    }
    
    /**
     * Get test data by TestCaseId
     */
    public static Map<String, String> getTestDataByTestCaseId(String testCaseId, String sheetName) {
        return ExcelUtil.getTestDataByTestCaseId(DEFAULT_EXCEL_PATH, sheetName, testCaseId);
    }
    
    /**
     * Get all executable test data from a sheet
     */
    public static List<Map<String, String>> getAllTestData(String sheetName) {
        return ExcelUtil.readExecutableData(DEFAULT_EXCEL_PATH, sheetName);
    }
    
    /**
     * Update test result in Excel
     */
    public static void updateTestResult(String testCaseId, String result, String sheetName) {
        try {
            ExcelUtil.updateTestResult(DEFAULT_EXCEL_PATH, sheetName, testCaseId, "Result", result);
            ExcelUtil.updateTestResult(DEFAULT_EXCEL_PATH, sheetName, testCaseId, "ExecutionTime", 
                                    java.time.LocalDateTime.now().toString());
            logger.info("Updated test result for TestCaseId: {} = {} in sheet: {}", testCaseId, result, sheetName);
        } catch (Exception e) {
            logger.error("Failed to update test result for TestCaseId: {} in sheet: {}", testCaseId, sheetName, e);
        }
    }
    
    /**
     * Update test result with additional details
     */
    public static void updateTestResult(String testCaseId, String result, String sheetName, 
                                      String details, String executionTime) {
        try {
            ExcelUtil.updateTestResult(DEFAULT_EXCEL_PATH, sheetName, testCaseId, "Result", result);
            ExcelUtil.updateTestResult(DEFAULT_EXCEL_PATH, sheetName, testCaseId, "Details", details);
            ExcelUtil.updateTestResult(DEFAULT_EXCEL_PATH, sheetName, testCaseId, "ExecutionTime", executionTime);
            logger.info("Updated detailed test result for TestCaseId: {} = {} in sheet: {}", testCaseId, result, sheetName);
        } catch (Exception e) {
            logger.error("Failed to update detailed test result for TestCaseId: {} in sheet: {}", testCaseId, sheetName, e);
        }
    }
    
    /**
     * Get test data with custom Excel path
     */
    public static Iterator<Object[]> getTestDataFromFile(String filePath, String sheetName) {
        List<Object[]> data = new ArrayList<>();
        
        try {
            List<Map<String, String>> excelData = ExcelUtil.readExecutableData(filePath, sheetName);
            
            for (Map<String, String> row : excelData) {
                data.add(new Object[]{row});
            }
            
            logger.info("Loaded {} test data sets from file: {} sheet: {}", data.size(), filePath, sheetName);
            
        } catch (Exception e) {
            logger.error("Error loading test data from file: {} sheet: {}", filePath, sheetName, e);
            data.add(new Object[]{Map.of("error", "Failed to load test data", "message", e.getMessage())});
        }
        
        return data.iterator();
    }
    
    /**
     * Get test data filtered by specific criteria
     */
    public static Iterator<Object[]> getFilteredTestData(String sheetName, String filterColumn, String filterValue) {
        List<Object[]> data = new ArrayList<>();
        
        try {
            List<Map<String, String>> allData = ExcelUtil.readExecutableData(DEFAULT_EXCEL_PATH, sheetName);
            
            for (Map<String, String> row : allData) {
                if (filterValue.equals(row.get(filterColumn))) {
                    data.add(new Object[]{row});
                }
            }
            
            logger.info("Filtered {} test data sets from sheet: {} where {} = {}", 
                       data.size(), sheetName, filterColumn, filterValue);
            
        } catch (Exception e) {
            logger.error("Error filtering test data from sheet: {}", sheetName, e);
            data.add(new Object[]{Map.of("error", "Failed to filter test data", "message", e.getMessage())});
        }
        
        return data.iterator();
    }
    
    /**
     * Check if test data file exists and is readable
     */
    public static boolean isTestDataAvailable() {
        return ExcelUtil.isExcelFileReadable(DEFAULT_EXCEL_PATH);
    }
    
    /**
     * Get available sheet names from test data file
     */
    public static List<String> getAvailableSheets() {
        return ExcelUtil.getSheetNames(DEFAULT_EXCEL_PATH);
    }

    private static final String TESTDATA_BASE = "src/test/resources/testdata/";

    public static Object[][] fromExcel(String fileName, String sheetName) {
        ExcelDataProvider provider = new ExcelDataProvider();
        return provider.getData(TESTDATA_BASE + fileName, sheetName);
    }

    public static List<Map<String, String>> fromCsv(String fileName) {
        CSVDataProvider provider = new CSVDataProvider();
        return provider.getData(TESTDATA_BASE + fileName);
    }

    public static List<Map<String, Object>> fromJson(String fileName, String rootKey) {
        JSONDataProvider provider = new JSONDataProvider();
        return provider.getData(TESTDATA_BASE + fileName, rootKey);
    }
}
