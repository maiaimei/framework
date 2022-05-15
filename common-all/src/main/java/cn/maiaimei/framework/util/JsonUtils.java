package cn.maiaimei.framework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static <T> String stringify(T data) {
        return objectMapper.writeValueAsString(data);
    }

    @SneakyThrows
    public static <T> T parse(String s, Class<T> clazz) {
        return objectMapper.readValue(s, clazz);
    }
}