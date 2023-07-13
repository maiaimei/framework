package cn.maiaimei.framework.utils;

import com.google.common.collect.ImmutableMap;
import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;

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

  public static String get(String baseName, String key) {
    return getBundle(baseName).getString(key);
  }

  public static ResourceBundle getBundle(String baseName) {
    return ResourceBundle.getBundle(baseName);
  }
}
