package com.hackathon.finservice.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;

public interface JsonUtil {

    public static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    public static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    /**
     * Convert an object to a JSON string.
     *
     * @param obj the object to be converted to JSON
     * @return the JSON string representation of the object
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static void logJsonToFile(String filePath, Object obj) {
        String json = toJson(obj);
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(json + "\n");
        } catch (IOException e) {
            log.error("Failed to write Json to file{}", e.getMessage());
        }
    }

}
