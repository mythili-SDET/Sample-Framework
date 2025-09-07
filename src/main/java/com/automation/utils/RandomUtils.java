package com.automation.utils;

import com.automation.logger.LoggerManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating random data
 * Provides methods to generate various types of random data for testing
 */
public class RandomUtils {
    
    private static final Logger logger = LoggerManager.getLogger(RandomUtils.class);
    private static final SecureRandom secureRandom = new SecureRandom();
    
    // Character sets for random string generation
    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String ALPHANUMERIC = ALPHABETS + NUMBERS;
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final String ALL_CHARS = ALPHANUMERIC + SPECIAL_CHARS;
    
    // Common first names and last names for random name generation
    private static final String[] FIRST_NAMES = {
        "John", "Jane", "Michael", "Sarah", "David", "Lisa", "Robert", "Emily",
        "James", "Jessica", "William", "Ashley", "Richard", "Amanda", "Joseph", "Jennifer",
        "Thomas", "Michelle", "Christopher", "Kimberly", "Charles", "Donna", "Daniel", "Carol",
        "Matthew", "Sandra", "Anthony", "Ruth", "Mark", "Sharon", "Donald", "Nancy"
    };
    
    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
        "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White",
        "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young"
    };
    
    // Common email domains
    private static final String[] EMAIL_DOMAINS = {
        "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com",
        "icloud.com", "live.com", "msn.com", "comcast.net", "verizon.net"
    };
    
    // Common cities
    private static final String[] CITIES = {
        "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
        "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville",
        "Fort Worth", "Columbus", "Charlotte", "San Francisco", "Indianapolis", "Seattle",
        "Denver", "Washington", "Boston", "El Paso", "Nashville", "Detroit", "Oklahoma City"
    };
    
    // Common states
    private static final String[] STATES = {
        "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
        "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
        "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan",
        "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire",
        "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio",
        "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
        "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia",
        "Wisconsin", "Wyoming"
    };
    
    /**
     * Generate random integer between min and max (inclusive)
     * @param min Minimum value
     * @param max Maximum value
     * @return Random integer
     */
    public static int getRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min value cannot be greater than max value");
        }
        int randomInt = ThreadLocalRandom.current().nextInt(min, max + 1);
        logger.debug("Generated random integer between {} and {}: {}", min, max, randomInt);
        return randomInt;
    }
    
    /**
     * Generate random integer between 0 and max (inclusive)
     * @param max Maximum value
     * @return Random integer
     */
    public static int getRandomInt(int max) {
        return getRandomInt(0, max);
    }
    
    /**
     * Generate random long between min and max (inclusive)
     * @param min Minimum value
     * @param max Maximum value
     * @return Random long
     */
    public static long getRandomLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("Min value cannot be greater than max value");
        }
        long randomLong = ThreadLocalRandom.current().nextLong(min, max + 1);
        logger.debug("Generated random long between {} and {}: {}", min, max, randomLong);
        return randomLong;
    }
    
    /**
     * Generate random double between min and max (inclusive)
     * @param min Minimum value
     * @param max Maximum value
     * @return Random double
     */
    public static double getRandomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min value cannot be greater than max value");
        }
        double randomDouble = ThreadLocalRandom.current().nextDouble(min, max + 1);
        logger.debug("Generated random double between {} and {}: {}", min, max, randomDouble);
        return randomDouble;
    }
    
    /**
     * Generate random boolean
     * @return Random boolean
     */
    public static boolean getRandomBoolean() {
        boolean randomBoolean = ThreadLocalRandom.current().nextBoolean();
        logger.debug("Generated random boolean: {}", randomBoolean);
        return randomBoolean;
    }
    
    /**
     * Generate random string of specified length
     * @param length Length of the string
     * @param charSet Character set to use
     * @return Random string
     */
    public static String getRandomString(int length, String charSet) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        if (charSet == null || charSet.isEmpty()) {
            throw new IllegalArgumentException("Character set cannot be null or empty");
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(charSet.length());
            sb.append(charSet.charAt(index));
        }
        
        String randomString = sb.toString();
        logger.debug("Generated random string of length {}: {}", length, randomString);
        return randomString;
    }
    
    /**
     * Generate random alphabetic string
     * @param length Length of the string
     * @return Random alphabetic string
     */
    public static String getRandomAlphabeticString(int length) {
        return getRandomString(length, ALPHABETS);
    }
    
    /**
     * Generate random numeric string
     * @param length Length of the string
     * @return Random numeric string
     */
    public static String getRandomNumericString(int length) {
        return getRandomString(length, NUMBERS);
    }
    
    /**
     * Generate random alphanumeric string
     * @param length Length of the string
     * @return Random alphanumeric string
     */
    public static String getRandomAlphanumericString(int length) {
        return getRandomString(length, ALPHANUMERIC);
    }
    
    /**
     * Generate random string with special characters
     * @param length Length of the string
     * @return Random string with special characters
     */
    public static String getRandomStringWithSpecialChars(int length) {
        return getRandomString(length, ALL_CHARS);
    }
    
    /**
     * Generate random email address
     * @return Random email address
     */
    public static String getRandomEmail() {
        String firstName = getRandomElement(FIRST_NAMES).toLowerCase();
        String lastName = getRandomElement(LAST_NAMES).toLowerCase();
        String domain = getRandomElement(EMAIL_DOMAINS);
        String email = firstName + "." + lastName + getRandomInt(1, 999) + "@" + domain;
        logger.debug("Generated random email: {}", email);
        return email;
    }
    
    /**
     * Generate random first name
     * @return Random first name
     */
    public static String getRandomFirstName() {
        return getRandomElement(FIRST_NAMES);
    }
    
    /**
     * Generate random last name
     * @return Random last name
     */
    public static String getRandomLastName() {
        return getRandomElement(LAST_NAMES);
    }
    
    /**
     * Generate random full name
     * @return Random full name
     */
    public static String getRandomFullName() {
        String fullName = getRandomFirstName() + " " + getRandomLastName();
        logger.debug("Generated random full name: {}", fullName);
        return fullName;
    }
    
    /**
     * Generate random phone number
     * @return Random phone number in format (XXX) XXX-XXXX
     */
    public static String getRandomPhoneNumber() {
        String areaCode = getRandomNumericString(3);
        String exchange = getRandomNumericString(3);
        String number = getRandomNumericString(4);
        String phoneNumber = "(" + areaCode + ") " + exchange + "-" + number;
        logger.debug("Generated random phone number: {}", phoneNumber);
        return phoneNumber;
    }
    
    /**
     * Generate random address
     * @return Random address
     */
    public static String getRandomAddress() {
        int streetNumber = getRandomInt(1, 9999);
        String streetName = getRandomElement(FIRST_NAMES) + " " + getRandomElement(
            new String[]{"Street", "Avenue", "Road", "Drive", "Lane", "Boulevard", "Court", "Place"}
        );
        String address = streetNumber + " " + streetName;
        logger.debug("Generated random address: {}", address);
        return address;
    }
    
    /**
     * Generate random city
     * @return Random city
     */
    public static String getRandomCity() {
        return getRandomElement(CITIES);
    }
    
    /**
     * Generate random state
     * @return Random state
     */
    public static String getRandomState() {
        return getRandomElement(STATES);
    }
    
    /**
     * Generate random ZIP code
     * @return Random ZIP code
     */
    public static String getRandomZipCode() {
        String zipCode = getRandomNumericString(5);
        logger.debug("Generated random ZIP code: {}", zipCode);
        return zipCode;
    }
    
    /**
     * Generate random date between start and end dates
     * @param startDate Start date (yyyy-MM-dd format)
     * @param endDate End date (yyyy-MM-dd format)
     * @return Random date string
     */
    public static String getRandomDate(String startDate, String endDate) {
        try {
            long startEpoch = java.time.LocalDate.parse(startDate).toEpochDay();
            long endEpoch = java.time.LocalDate.parse(endDate).toEpochDay();
            long randomEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch + 1);
            String randomDate = java.time.LocalDate.ofEpochDay(randomEpoch).toString();
            logger.debug("Generated random date between {} and {}: {}", startDate, endDate, randomDate);
            return randomDate;
        } catch (Exception e) {
            logger.error("Error generating random date between {} and {}", startDate, endDate, e);
            throw new RuntimeException("Failed to generate random date", e);
        }
    }
    
    /**
     * Generate random date in the past
     * @param daysBack Number of days back from today
     * @return Random date string
     */
    public static String getRandomPastDate(int daysBack) {
        String endDate = java.time.LocalDate.now().toString();
        String startDate = java.time.LocalDate.now().minusDays(daysBack).toString();
        return getRandomDate(startDate, endDate);
    }
    
    /**
     * Generate random date in the future
     * @param daysForward Number of days forward from today
     * @return Random date string
     */
    public static String getRandomFutureDate(int daysForward) {
        String startDate = java.time.LocalDate.now().toString();
        String endDate = java.time.LocalDate.now().plusDays(daysForward).toString();
        return getRandomDate(startDate, endDate);
    }
    
    /**
     * Generate random element from array
     * @param array Array to select from
     * @return Random element
     */
    public static <T> T getRandomElement(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        int index = secureRandom.nextInt(array.length);
        T element = array[index];
        logger.debug("Selected random element from array: {}", element);
        return element;
    }
    
    /**
     * Generate random element from list
     * @param list List to select from
     * @return Random element
     */
    public static <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        int index = secureRandom.nextInt(list.size());
        T element = list.get(index);
        logger.debug("Selected random element from list: {}", element);
        return element;
    }
    
    /**
     * Generate random UUID
     * @return Random UUID string
     */
    public static String getRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        logger.debug("Generated random UUID: {}", uuid);
        return uuid;
    }
    
    /**
     * Generate random password with specified criteria
     * @param length Password length
     * @param includeUppercase Include uppercase letters
     * @param includeLowercase Include lowercase letters
     * @param includeNumbers Include numbers
     * @param includeSpecialChars Include special characters
     * @return Random password
     */
    public static String getRandomPassword(int length, boolean includeUppercase, 
                                         boolean includeLowercase, boolean includeNumbers, 
                                         boolean includeSpecialChars) {
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be positive");
        }
        
        StringBuilder charSet = new StringBuilder();
        if (includeUppercase) charSet.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        if (includeLowercase) charSet.append("abcdefghijklmnopqrstuvwxyz");
        if (includeNumbers) charSet.append("0123456789");
        if (includeSpecialChars) charSet.append("!@#$%^&*()_+-=[]{}|;:,.<>?");
        
        if (charSet.length() == 0) {
            throw new IllegalArgumentException("At least one character type must be included");
        }
        
        String password = getRandomString(length, charSet.toString());
        logger.debug("Generated random password of length {}: {}", length, password);
        return password;
    }
    
    /**
     * Generate random password with default criteria (8-12 chars, mixed case, numbers)
     * @return Random password
     */
    public static String getRandomPassword() {
        int length = getRandomInt(8, 12);
        return getRandomPassword(length, true, true, true, false);
    }
}
