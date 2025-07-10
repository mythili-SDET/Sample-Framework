package com.framework.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map<String, Object> readJson(String pathInResources) {
        try (InputStream is = JsonUtil.class.getClassLoader().getResourceAsStream(pathInResources)) {
            if (is == null) {
                throw new IllegalArgumentException("JSON file not found: " + pathInResources);
            }
            return MAPPER.readValue(is, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON: " + pathInResources, e);
        }
    }
}