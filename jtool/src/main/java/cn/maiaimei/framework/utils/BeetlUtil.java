package cn.maiaimei.framework.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;

/**
 * <a href="http://bbs.ibeetl.com/">Bee Template Language</a> Util
 *
 * @author maiaimei
 */
public final class BeetlUtil {

  private static final GroupTemplate CLASSPATH_RESOURCE_LOADER_GROUP_TEMPLATE;
  private static final GroupTemplate STRING_TEMPLATE_RESOURCE_LOADER_GROUP_TEMPLATE;

  static {
    try {
      ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();
      StringTemplateResourceLoader stringTemplateResourceLoader = new StringTemplateResourceLoader();
      Configuration configuration = Configuration.defaultConfiguration();
      CLASSPATH_RESOURCE_LOADER_GROUP_TEMPLATE = new GroupTemplate(classpathResourceLoader,
          configuration);
      STRING_TEMPLATE_RESOURCE_LOADER_GROUP_TEMPLATE = new GroupTemplate(
          stringTemplateResourceLoader,
          configuration);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private BeetlUtil() {
    throw new UnsupportedOperationException();
  }

  public static String render(String template, Map<String, Object> data) {
    final Template t = STRING_TEMPLATE_RESOURCE_LOADER_GROUP_TEMPLATE.getTemplate(template);
    t.binding(data);
    return t.render();
  }

  public static String renderByTemplatePath(String template, Map<String, Object> data) {
    final Template t = CLASSPATH_RESOURCE_LOADER_GROUP_TEMPLATE.getTemplate(template);
    t.binding(data);
    return t.render();
  }

  public static void renderToFileByTemplatePath(String path, String template,
      Map<String, Object> data) {
    final Template t = CLASSPATH_RESOURCE_LOADER_GROUP_TEMPLATE.getTemplate(template);
    t.binding(data);
    try (OutputStream os = Files.newOutputStream(Paths.get(path))) {
      t.renderTo(os);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
