package cn.maiaimei.framework.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

@Deprecated
public class FileUtils {

  @SneakyThrows
  public static InputStream readClassPathResourceAsInputStream(String path) {
    ClassPathResource classPathResource = new ClassPathResource(path);
    if (classPathResource.exists()) {
      return classPathResource.getInputStream();
    }
    return null;
  }

  @SneakyThrows
  public static String readClassPathResourceAsString(String path) {
    final InputStream inputStream = readClassPathResourceAsInputStream(path);
    if (inputStream == null) {
      return null;
    }
    return FileCopyUtils.copyToString(new InputStreamReader(inputStream));
  }

  private FileUtils() {
    throw new UnsupportedOperationException();
  }
}
