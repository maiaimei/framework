package cn.maiaimei.framework.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class JSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    public static <T> String stringify(T value) {
        return objectMapper.writeValueAsString(value);
    }

    @SneakyThrows
    public static <T> T parse(String value, Class<T> clazz) {
        return objectMapper.readValue(value, clazz);
    }

    @SneakyThrows
    public static <T> T parse(String value, TypeReference<T> valueTypeRef) {
        return objectMapper.readValue(value, valueTypeRef);
    }

    @SneakyThrows
    public static String readFileAsString(String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        if (classPathResource.exists()) {
            InputStream inputStream = classPathResource.getInputStream();
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        }
        return null;
    }

    @SneakyThrows
    public static <T> T readFileAsObject(String path, Class<T> clazz) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        if (classPathResource.exists()) {
            InputStream inputStream = classPathResource.getInputStream();
            return objectMapper.readValue(inputStream, clazz);
        }
        return null;
    }

    @SneakyThrows
    public static <T> T readFileAsObject(String path, TypeReference<T> valueTypeRef) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        if (classPathResource.exists()) {
            InputStream inputStream = classPathResource.getInputStream();
            return objectMapper.readValue(inputStream, valueTypeRef);
        }
        return null;
    }

    private JSON() {
        throw new UnsupportedOperationException();
    }
}
