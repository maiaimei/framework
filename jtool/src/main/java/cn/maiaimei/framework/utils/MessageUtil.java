package cn.maiaimei.framework.utils;

import com.google.common.collect.ImmutableMap;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;

public class MessageUtil {

  private MessageUtil() {
    throw new UnsupportedOperationException();
  }

  public static String format(String baseName, String key, String k1, Object v1) {
    Map<String, Object> data = ImmutableMap.of(k1, v1);
    return format(baseName, key, data);
  }

  public static String format(String baseName, String key,
      String k1, Object v1, String k2, Object v2) {
    Map<String, Object> data = ImmutableMap.of(k1, v1, k2, v2);
    return format(baseName, key, data);
  }

  public static String format(String baseName, String key,
      String k1, Object v1, String k2, Object v2, String k3, Object v3) {
    Map<String, Object> data = ImmutableMap.of(k1, v1, k2, v2, k3, v3);
    return format(baseName, key, data);
  }

  public static String format(String baseName, String key, Map<String, Object> data) {
    return format(getBundle(baseName), key, data);
  }

  public static String format(String baseName, String key, Object... data) {
    return format(getBundle(baseName), key, data);
  }

  public static String format(ResourceBundle bundle, String key, String k1, Object v1) {
    Map<String, Object> data = ImmutableMap.of(k1, v1);
    return format(bundle, key, data);
  }

  public static String format(ResourceBundle bundle, String key,
      String k1, Object v1, String k2, Object v2) {
    Map<String, Object> data = ImmutableMap.of(k1, v1, k2, v2);
    return format(bundle, key, data);
  }

  public static String format(ResourceBundle bundle, String key,
      String k1, Object v1, String k2, Object v2, String k3, Object v3) {
    Map<String, Object> data = ImmutableMap.of(k1, v1, k2, v2, k3, v3);
    return format(bundle, key, data);
  }

  public static String format(ResourceBundle bundle, String key, Map<String, Object> data) {
    final String template = bundle.getString(key);
    return BeetlUtil.render(template, data);
  }

  public static String format(ResourceBundle bundle, String key, Object... data) {
    final String template = bundle.getString(key);
    return MessageFormat.format(template, data);
  }

  public static String get(String baseName, String key, String defaultValue) {
    return get(getBundle(baseName), key, defaultValue);
  }

  public static String get(ResourceBundle bundle, String key, String defaultValue) {
    if (Objects.isNull(bundle)) {
      return defaultValue;
    }
    final String result = bundle.getString(key);
    if (StringUtils.isBlank(result)) {
      return defaultValue;
    }
    return result;
  }

  public static ResourceBundle getBundle(String baseName) {
    return ResourceBundle.getBundle(baseName);
  }
}
