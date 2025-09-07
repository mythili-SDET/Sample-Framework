package com.automation.utils;

import com.automation.logger.LoggerManager;
import org.apache.logging.log4j.Logger;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility class for date and time operations
 * Provides methods to handle various date/time formats and calculations
 */
public class DateTimeUtil {
    
    private static final Logger logger = LoggerManager.getLogger(DateTimeUtil.class);
    
    // Common date formats
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String US_DATE_FORMAT = "MM/dd/yyyy";
    public static final String UK_DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    
    /**
     * Get current date as string
     * @param format Date format pattern
     * @return Current date as formatted string
     */
    public static String getCurrentDate(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDate = currentDate.format(formatter);
        logger.debug("Current date: {}", formattedDate);
        return formattedDate;
    }
    
    /**
     * Get current date in default format (yyyy-MM-dd)
     * @return Current date as string
     */
    public static String getCurrentDate() {
        return getCurrentDate(DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Get current time as string
     * @param format Time format pattern
     * @return Current time as formatted string
     */
    public static String getCurrentTime(String format) {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedTime = currentTime.format(formatter);
        logger.debug("Current time: {}", formattedTime);
        return formattedTime;
    }
    
    /**
     * Get current time in default format (HH:mm:ss)
     * @return Current time as string
     */
    public static String getCurrentTime() {
        return getCurrentTime(DEFAULT_TIME_FORMAT);
    }
    
    /**
     * Get current date and time as string
     * @param format DateTime format pattern
     * @return Current date and time as formatted string
     */
    public static String getCurrentDateTime(String format) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDateTime = currentDateTime.format(formatter);
        logger.debug("Current date time: {}", formattedDateTime);
        return formattedDateTime;
    }
    
    /**
     * Get current date and time in default format (yyyy-MM-dd HH:mm:ss)
     * @return Current date and time as string
     */
    public static String getCurrentDateTime() {
        return getCurrentDateTime(DEFAULT_DATETIME_FORMAT);
    }
    
    /**
     * Get current timestamp
     * @return Current timestamp as string (yyyyMMdd_HHmmss)
     */
    public static String getCurrentTimestamp() {
        return getCurrentDateTime(TIMESTAMP_FORMAT);
    }
    
    /**
     * Add days to current date
     * @param days Number of days to add
     * @param format Date format pattern
     * @return Future date as formatted string
     */
    public static String addDaysToCurrentDate(int days, String format) {
        LocalDate futureDate = LocalDate.now().plusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDate = futureDate.format(formatter);
        logger.debug("Date after adding {} days: {}", days, formattedDate);
        return formattedDate;
    }
    
    /**
     * Add days to current date in default format
     * @param days Number of days to add
     * @return Future date as string
     */
    public static String addDaysToCurrentDate(int days) {
        return addDaysToCurrentDate(days, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Subtract days from current date
     * @param days Number of days to subtract
     * @param format Date format pattern
     * @return Past date as formatted string
     */
    public static String subtractDaysFromCurrentDate(int days, String format) {
        LocalDate pastDate = LocalDate.now().minusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDate = pastDate.format(formatter);
        logger.debug("Date after subtracting {} days: {}", days, formattedDate);
        return formattedDate;
    }
    
    /**
     * Subtract days from current date in default format
     * @param days Number of days to subtract
     * @return Past date as string
     */
    public static String subtractDaysFromCurrentDate(int days) {
        return subtractDaysFromCurrentDate(days, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Add months to current date
     * @param months Number of months to add
     * @param format Date format pattern
     * @return Future date as formatted string
     */
    public static String addMonthsToCurrentDate(int months, String format) {
        LocalDate futureDate = LocalDate.now().plusMonths(months);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDate = futureDate.format(formatter);
        logger.debug("Date after adding {} months: {}", months, formattedDate);
        return formattedDate;
    }
    
    /**
     * Add months to current date in default format
     * @param months Number of months to add
     * @return Future date as string
     */
    public static String addMonthsToCurrentDate(int months) {
        return addMonthsToCurrentDate(months, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Add years to current date
     * @param years Number of years to add
     * @param format Date format pattern
     * @return Future date as formatted string
     */
    public static String addYearsToCurrentDate(int years, String format) {
        LocalDate futureDate = LocalDate.now().plusYears(years);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDate = futureDate.format(formatter);
        logger.debug("Date after adding {} years: {}", years, formattedDate);
        return formattedDate;
    }
    
    /**
     * Add years to current date in default format
     * @param years Number of years to add
     * @return Future date as string
     */
    public static String addYearsToCurrentDate(int years) {
        return addYearsToCurrentDate(years, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Calculate difference between two dates in days
     * @param startDate Start date string
     * @param endDate End date string
     * @param format Date format pattern
     * @return Number of days between dates
     */
    public static long getDaysDifference(String startDate, String endDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        long days = ChronoUnit.DAYS.between(start, end);
        logger.debug("Days difference between {} and {}: {}", startDate, endDate, days);
        return days;
    }
    
    /**
     * Calculate difference between two dates in days using default format
     * @param startDate Start date string
     * @param endDate End date string
     * @return Number of days between dates
     */
    public static long getDaysDifference(String startDate, String endDate) {
        return getDaysDifference(startDate, endDate, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Check if date is in the past
     * @param dateString Date string to check
     * @param format Date format pattern
     * @return true if date is in the past
     */
    public static boolean isDateInPast(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateString, formatter);
        boolean isPast = date.isBefore(LocalDate.now());
        logger.debug("Date {} is in past: {}", dateString, isPast);
        return isPast;
    }
    
    /**
     * Check if date is in the past using default format
     * @param dateString Date string to check
     * @return true if date is in the past
     */
    public static boolean isDateInPast(String dateString) {
        return isDateInPast(dateString, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Check if date is in the future
     * @param dateString Date string to check
     * @param format Date format pattern
     * @return true if date is in the future
     */
    public static boolean isDateInFuture(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateString, formatter);
        boolean isFuture = date.isAfter(LocalDate.now());
        logger.debug("Date {} is in future: {}", dateString, isFuture);
        return isFuture;
    }
    
    /**
     * Check if date is in the future using default format
     * @param dateString Date string to check
     * @return true if date is in the future
     */
    public static boolean isDateInFuture(String dateString) {
        return isDateInFuture(dateString, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Convert date string from one format to another
     * @param dateString Original date string
     * @param fromFormat Source format
     * @param toFormat Target format
     * @return Converted date string
     */
    public static String convertDateFormat(String dateString, String fromFormat, String toFormat) {
        DateTimeFormatter fromFormatter = DateTimeFormatter.ofPattern(fromFormat);
        DateTimeFormatter toFormatter = DateTimeFormatter.ofPattern(toFormat);
        LocalDate date = LocalDate.parse(dateString, fromFormatter);
        String convertedDate = date.format(toFormatter);
        logger.debug("Converted date from {} to {}: {} -> {}", fromFormat, toFormat, dateString, convertedDate);
        return convertedDate;
    }
    
    /**
     * Get day of week for a given date
     * @param dateString Date string
     * @param format Date format pattern
     * @return Day of week
     */
    public static String getDayOfWeek(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateString, formatter);
        String dayOfWeek = date.getDayOfWeek().toString();
        logger.debug("Day of week for {}: {}", dateString, dayOfWeek);
        return dayOfWeek;
    }
    
    /**
     * Get day of week for a given date using default format
     * @param dateString Date string
     * @return Day of week
     */
    public static String getDayOfWeek(String dateString) {
        return getDayOfWeek(dateString, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Get month name for a given date
     * @param dateString Date string
     * @param format Date format pattern
     * @return Month name
     */
    public static String getMonthName(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateString, formatter);
        String monthName = date.getMonth().toString();
        logger.debug("Month name for {}: {}", dateString, monthName);
        return monthName;
    }
    
    /**
     * Get month name for a given date using default format
     * @param dateString Date string
     * @return Month name
     */
    public static String getMonthName(String dateString) {
        return getMonthName(dateString, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * Get current epoch time in milliseconds
     * @return Current epoch time
     */
    public static long getCurrentEpochTime() {
        long epochTime = System.currentTimeMillis();
        logger.debug("Current epoch time: {}", epochTime);
        return epochTime;
    }
    
    /**
     * Convert epoch time to date string
     * @param epochTime Epoch time in milliseconds
     * @param format Date format pattern
     * @return Date string
     */
    public static String epochTimeToDateString(long epochTime, String format) {
        Instant instant = Instant.ofEpochMilli(epochTime);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String dateString = dateTime.format(formatter);
        logger.debug("Epoch time {} converted to date: {}", epochTime, dateString);
        return dateString;
    }
    
    /**
     * Convert epoch time to date string using default format
     * @param epochTime Epoch time in milliseconds
     * @return Date string
     */
    public static String epochTimeToDateString(long epochTime) {
        return epochTimeToDateString(epochTime, DEFAULT_DATETIME_FORMAT);
    }
    
    /**
     * Convert Date object to LocalDateTime
     * @param date Date object
     * @return LocalDateTime object
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    /**
     * Convert LocalDateTime to Date object
     * @param localDateTime LocalDateTime object
     * @return Date object
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
