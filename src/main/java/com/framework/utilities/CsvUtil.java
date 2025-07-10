package com.framework.utilities;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.util.*;

public class CsvUtil {

    public static List<Map<String, String>> readCsv(String pathInResources) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(Objects.requireNonNull(CsvUtil.class.getClassLoader().getResourceAsStream(pathInResources))))) {
            List<String[]> all = reader.readAll();
            String[] header = all.get(0);
            List<Map<String, String>> data = new ArrayList<>();
            for (int i = 1; i < all.size(); i++) {
                String[] row = all.get(i);
                Map<String, String> map = new HashMap<>();
                for (int j = 0; j < header.length; j++) {
                    map.put(header[j], row[j]);
                }
                data.add(map);
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV: " + pathInResources, e);
        }
    }
}