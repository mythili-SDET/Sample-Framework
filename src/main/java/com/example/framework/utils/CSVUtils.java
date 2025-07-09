package com.example.framework.utils;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Utility for reading CSV files located on classpath.
 */
public final class CSVUtils {

    private CSVUtils() {}

    public static List<String[]> read(String fileName) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                CSVUtils.class.getResourceAsStream("/" + fileName)))) {
            return reader.readAll();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read CSV file " + fileName, e);
        }
    }
}