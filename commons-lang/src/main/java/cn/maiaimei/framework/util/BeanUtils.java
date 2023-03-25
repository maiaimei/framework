package cn.maiaimei.framework.util;

import org.springframework.beans.FatalBeanException;

import java.util.ArrayList;
import java.util.List;

public class BeanUtils extends org.springframework.beans.BeanUtils {

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        try {
            Class<?> clazz = Class.forName(targetClass.getName());
            Object target = clazz.getConstructor().newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
            return JSON.convert(target, targetClass);
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
                        targetList.add(JSON.convert(target, targetClass));
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
