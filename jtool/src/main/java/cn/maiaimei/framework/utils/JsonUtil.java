package cn.maiaimei.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.InputStream;
import java.util.Map;
import lombok.SneakyThrows;

public class JsonUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    // ignore unknown properties
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);

    // java.lang.IllegalArgumentException: Java 8 date/time type `java.time.LocalDateTime` not supported by default
    OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    OBJECT_MAPPER.registerModule(new JavaTimeModule());
  }

  private JsonUtil() {
    throw new UnsupportedOperationException();
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
  public static <T> T parse(InputStream inputStream, Class<T> clazz) {
    return OBJECT_MAPPER.readValue(inputStream, clazz);
  }

  @SneakyThrows
  public static <T> T parse(InputStream inputStream, TypeReference<T> valueTypeRef) {
    return OBJECT_MAPPER.readValue(inputStream, valueTypeRef);
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
  public static Map<String, Object> toMap(String value) {
    return OBJECT_MAPPER.readValue(value, new TypeReference<Map<String, Object>>() {
    });
  }
}
