package cn.maiaimei.framework.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class JSON {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 忽略未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // java.lang.IllegalArgumentException: Java 8 date/time type `java.time.LocalDateTime` not supported by default
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    public static <T> String stringify(T value) {
        return OBJECT_MAPPER.writeValueAsString(value);
    }

    @SneakyThrows
    public static <T> T parse(String value, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(value, clazz);
    }

    @SneakyThrows
    public static <T> T parse(String value, TypeReference<T> valueTypeRef) {
        return OBJECT_MAPPER.readValue(value, valueTypeRef);
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
            return OBJECT_MAPPER.readValue(inputStream, clazz);
        }
        return null;
    }

    @SneakyThrows
    public static <T> T readFileAsObject(String path, TypeReference<T> valueTypeRef) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        if (classPathResource.exists()) {
            InputStream inputStream = classPathResource.getInputStream();
            return OBJECT_MAPPER.readValue(inputStream, valueTypeRef);
        }
        return null;
    }

    private JSON() {
        throw new UnsupportedOperationException();
    }
}
