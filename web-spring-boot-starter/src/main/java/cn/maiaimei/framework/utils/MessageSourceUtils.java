package cn.maiaimei.framework.utils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.Nullable;

public class MessageSourceUtils {

  public static String getMessage(String basename, String code, Locale locale) {
    final MessageSource messageSource = getMessageSource(basename);
    return messageSource.getMessage(code, null, locale);
  }

  public static String getMessage(String basename, String code, @Nullable Object[] args,
      Locale locale) {
    //final MessageSource messageSource = getMessageSource(basename);
    //return messageSource.getMessage(code, args, null, locale);
    final PlatformResourceBundleLocator bundleLocator = new PlatformResourceBundleLocator(basename);
    final ResourceBundle resourceBundle = bundleLocator.getResourceBundle(locale);
    return resourceBundle.getString(code);
  }

  public static String getMessage(String basename, String code, @Nullable String defaultMessage,
      Locale locale) {
    final MessageSource messageSource = getMessageSource(basename);
    return messageSource.getMessage(code, null, defaultMessage, locale);
  }

  public static String getMessage(String basename, String code, @Nullable Object[] args,
      @Nullable String defaultMessage, Locale locale) {
    final MessageSource messageSource = getMessageSource(basename);
    return messageSource.getMessage(code, args, defaultMessage, locale);
  }

  private static MessageSource getMessageSource(String basename) {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename(basename);
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    return messageSource;
  }
}
