package cn.maiaimei.framework.web.servlet;

import cn.maiaimei.framework.beans.Result;
import cn.maiaimei.framework.beans.ResultUtils;
import cn.maiaimei.framework.exception.TooManyAnnotationException;
import cn.maiaimei.framework.utils.JsonUtil;
import java.util.Arrays;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 接口返回值统一处理
 */
@ControllerAdvice
@ConditionalOnWebApplication
public class GlobalResponseHandler implements ApplicationContextAware, InitializingBean,
    ResponseBodyAdvice<Object> {

  private String[] basePackages;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterPropertiesSet() {
    String basePackage = "";
    Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(
        EnableGlobalResponse.class);
    if (beansWithAnnotation.size() > 1) {
      throw new TooManyAnnotationException(EnableGlobalResponse.class);
    }
    for (String key : beansWithAnnotation.keySet()) {
      basePackage = beansWithAnnotation.get(key).getClass().getPackage().getName();
      EnableGlobalResponse annotation = AnnotationUtils.findAnnotation(
          beansWithAnnotation.get(key).getClass(), EnableGlobalResponse.class);
      if (null != annotation) {
        basePackages = annotation.basePackages();
      }
    }
    if (null == basePackages || basePackages.length == 0) {
      basePackages = new String[]{basePackage};
    }
  }

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return Arrays.stream(basePackages)
        .anyMatch(basePackage -> returnType.getDeclaringClass().getName().contains(basePackage));
  }

  @SneakyThrows
  @Override
  public Object beforeBodyWrite(Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    if (selectedContentType.includes(MediaType.APPLICATION_OCTET_STREAM)) {
      return body;
    }
    if (body == null) {
      return ResultUtils.success();
    }
    if (body instanceof String) {
      return JsonUtil.stringify(ResultUtils.success(body));
    }
    if (body instanceof Result) {
      return body;
    }
    return ResultUtils.success(body);
  }
}
