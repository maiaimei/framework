package cn.maiaimei.framework.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.FatalBeanException;

import java.util.ArrayList;
import java.util.List;

public class BeanUtils extends org.springframework.beans.BeanUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 忽略未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // java.lang.IllegalArgumentException: Java 8 date/time type `java.time.LocalDateTime` not supported by default
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        try {
            Class<?> clazz = Class.forName(targetClass.getName());
            Object target = clazz.getConstructor().newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
            return OBJECT_MAPPER.convertValue(target, targetClass);
        } catch (Exception ex) {
            throw new FatalBeanException("Could not copy property from source to target", ex);
        }
    }

    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        try {
            List<T> targetList = new ArrayList<>();
            if (null != sourceList && sourceList.size() > 0) {
                Class<?> clazz = Class.forName(targetClass.getName());
                sourceList.forEach(source -> {
                    try {
                        Object target = clazz.getConstructor().newInstance();
                        org.springframework.beans.BeanUtils.copyProperties(source, target);
                        targetList.add(OBJECT_MAPPER.convertValue(target, targetClass));
                    } catch (Exception ex) {
                        throw new FatalBeanException("Could not copy property from source list to target list", ex);
                    }
                });
            }
            return targetList;
        } catch (Exception ex) {
            throw new FatalBeanException("Could not copy property from source list to target list", ex);
        }
    }

    private BeanUtils() {
        throw new UnsupportedOperationException();
    }
}
