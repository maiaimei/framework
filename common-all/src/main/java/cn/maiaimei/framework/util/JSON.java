package cn.maiaimei.framework.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JSON {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static <T> String stringify(T value) {
        return objectMapper.writeValueAsString(value);
    }

    @SneakyThrows
    public static <T> T parse(String value, Class<T> clazz) {
        return objectMapper.readValue(value, clazz);
    }

    @SneakyThrows
    public static <T> List<T> toList(String value) {
        return objectMapper.readValue(value, new TypeReference<List<T>>() {
        });
    }

    @SneakyThrows
    public static Map<String, Object> toMap(String value) {
        return objectMapper.readValue(value, new TypeReference<HashMap<String, Object>>() {
        });
    }

    @SneakyThrows
    public static <T> Map<String, Object> toMap(T value) {
        return objectMapper.readValue(stringify(value), new TypeReference<HashMap<String, Object>>() {
        });
    }

    @SneakyThrows
    public static String readFileAsString(String path) {
        InputStream inputStream = JSON.class.getResourceAsStream(path);
        Assert.notNull(inputStream, "inputStream is null");
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }

    @SneakyThrows
    public static <T> T readFileAsObject(String path, Class<T> clazz) {
        InputStream inputStream = JSON.class.getResourceAsStream(path);
        return objectMapper.readValue(inputStream, clazz);
    }

    @SneakyThrows
    public static <T> List<T> readFileAsList(String path, Class<T> clazz) {
        InputStream inputStream = JSON.class.getResourceAsStream(path);
        return objectMapper.readValue(inputStream, new TypeReference<List<T>>() {
        });
    }

    @SneakyThrows
    public static Map<String, Object> readFileAsMap(String path) {
        InputStream inputStream = JSON.class.getResourceAsStream(path);
        return objectMapper.readValue(inputStream, new TypeReference<HashMap<String, Object>>() {
        });
    }

    private JSON() {
        throw new UnsupportedOperationException();
    }
}