package cn.maiaimei.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

public class FileUtil {

  private FileUtil() {
    throw new UnsupportedOperationException();
  }

  /**
   * readFileToString {@link org.apache.commons.io.FileUtils#readFileToString(File, Charset)}
   *
   * @param pathname pathname
   * @return java.lang.String
   */
  public static String readFileToString(String pathname) {
    return readFile(pathname, is -> {
      try {
        return IOUtils.toString(is, StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @SneakyThrows
  public static <T> T readFileToObject(String pathname, Class<T> clazz) {
    return readFile(pathname, is -> JsonUtil.parse(is, clazz));
  }

  @SneakyThrows
  public static <T> T readFileToObject(String pathname, TypeReference<T> valueTypeRef) {
    return readFile(pathname, is -> JsonUtil.parse(is, valueTypeRef));
  }

  private static <T> T readFile(String pathname, Function<InputStream, T> function) {
    final InputStream is = FileUtil.class.getResourceAsStream(pathname);
    if (is == null) {
      return null;
    }
    return function.apply(is);
  }
}
