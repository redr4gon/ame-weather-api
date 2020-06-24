package br.com.amedigital.weather.api.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

/**
 * Helper class to convert JSON in POJO object
 * <p>
 * Put the JSON file in {@link #JSON_FOLDER}
 */
public class SupportUtils {

    /**
     * Folder where the json must be placed
     */
    public static final String JSON_FOLDER = "json-files";
    private static ObjectMapper mapper;

    /**
     * Find json string to specific filename
     *
     * @param fileName file name
     * @return json string from file
     */
    public static String getSimpleJson(String fileName) {
        try {
            return new JSONObject(FileUtils.readFileToString(getJsonFile(fileName), StandardCharsets.UTF_8)).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Find json string to specific filename using {@link JSONArray}
     *
     * @param fileName file name
     * @return json string from file
     */
    public static String getSimpleJsonArray(String fileName) {

        try {
            return new JSONArray(FileUtils.readFileToString(getJsonFile(fileName), StandardCharsets.UTF_8)).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Find json string in specific filename and convert to pojo object
     *
     * @param fileName file name
     * @param target   targe class
     * @param <T>      target type
     * @return object with type T
     */
    public static <T> T getPojo(String fileName, Class<?> target) {
        try {
            String content = new JSONObject(FileUtils.readFileToString(getJsonFile(fileName), StandardCharsets.UTF_8)).toString();
            return getDefaultMapper().readValue(content, getDefaultMapper().getTypeFactory().constructType(Class.forName(target.getName())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the JSON file from the classpat and put in a {@link File}
     *
     * @param fileName file name
     * @return File file from file name
     * @throws FileNotFoundException file not found exception
     */
    public static File getJsonFile(String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile(String.format("%s%s/%s", ResourceUtils.CLASSPATH_URL_PREFIX, JSON_FOLDER, fileName));
    }

    private static ObjectMapper getDefaultMapper() {

        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        return mapper;
    }

}
