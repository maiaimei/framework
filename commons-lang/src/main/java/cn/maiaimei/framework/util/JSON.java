package cn.maiaimei.framework.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class JSON {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // ignore unknown properties
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // java.lang.IllegalArgumentException: Java 8 date/time type `java.time.LocalDateTime` not supported by default
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> T convert(Object fromValue, Class<T> toValueType) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    }

    public static <T> T convert(Object fromValue, TypeReference<T> toValueTypeRef) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
    }

    public static <T> T convert(Object fromValue, JavaType toValueType) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueType);
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
    public static Map<String, Object> toMap(String value) {
        return OBJECT_MAPPER.readValue(value, new TypeReference<Map<String, Object>>() {
        });
    }

    @SneakyThrows
    public static String readClassPathResourceAsString(String path) {
        final InputStream inputStream = FileUtils.readClassPathResourceAsInputStream(path);
        if (inputStream == null) {
            return null;
        }
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }

    @SneakyThrows
    public static <T> T readClassPathResourceAsObject(String path, Class<T> clazz) {
        final InputStream inputStream = FileUtils.readClassPathResourceAsInputStream(path);
        if (inputStream == null) {
            return null;
        }
        return OBJECT_MAPPER.readValue(inputStream, clazz);
    }

    @SneakyThrows
    public static <T> T readClassPathResourceAsObject(String path, TypeReference<T> valueTypeRef) {
        final InputStream inputStream = FileUtils.readClassPathResourceAsInputStream(path);
        if (inputStream == null) {
            return null;
        }
        return OBJECT_MAPPER.readValue(inputStream, valueTypeRef);
    }

    private JSON() {
        throw new UnsupportedOperationException();
    }
}
