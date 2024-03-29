package cn.maiaimei.framework.web.servlet.http.utils;

import cn.maiaimei.framework.web.servlet.http.ReusableRequestWrapper;
import cn.maiaimei.framework.web.servlet.http.model.HttpInformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class HttpUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  public static HttpInformation getRequestResponseDetail(HttpServletRequest request,
      List<String> excludeHeaderNames) {
    String userAgentAsString = request.getHeader("user-agent");
    UserAgent userAgent = UserAgent.parseUserAgentString(userAgentAsString);

    String requestUri = request.getRequestURI();
    String requestMethod = request.getMethod();
    String requestHeaders = getRequestHeaders(request, excludeHeaderNames);
    String requestParams = StringUtils.defaultIfBlank(request.getQueryString(), StringUtils.EMPTY);
    String requestBody = getRequestBody(request);
    String clientIp = request.getRemoteHost();
    String clientDevice = userAgent.getOperatingSystem().getDeviceType().getName();
    String clientOs = userAgent.getOperatingSystem().getName();

    return HttpInformation.builder()
        .requestMethod(requestMethod)
        .requestUri(requestUri)
        .requestHeaders(requestHeaders)
        .requestParams(requestParams)
        .requestBody(requestBody)
        .userAgent(userAgentAsString)
        .clientIP(clientIp)
        .clientDevice(clientDevice)
        .clientOS(clientOs)
        .build();
  }

  public static String getRequestMethodAndUri(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    if (status == HttpStatus.NOT_FOUND) {
      return StringUtils.EMPTY;
    }
    String method = request.getMethod();
    String uri = request.getRequestURI();
    return String.format("[%s]%s", method, uri);
  }

  public static HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    try {
      return HttpStatus.valueOf(statusCode);
    } catch (Exception ex) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }

  @SneakyThrows
  public static String getRequestHeaders(HttpServletRequest request,
      List<String> excludeHeaderNames) {
    if (excludeHeaderNames == null) {
      excludeHeaderNames = new ArrayList<>();
    }
    HashMap<String, String> map = new HashMap<>();
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      if (!excludeHeaderNames.contains(name)) {
        map.put(name, request.getHeader(name));
      }
    }
    if (map.size() == 0) {
      return StringUtils.EMPTY;
    }
    return objectMapper.writeValueAsString(map);
  }

  public static String getRequestBody(HttpServletRequest request) {
    String requestBody = StringUtils.EMPTY;
    if (request instanceof ContentCachingRequestWrapper) {
      requestBody = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray(),
          StandardCharsets.UTF_8);
    }
    if (request instanceof ReusableRequestWrapper) {
      requestBody = ((ReusableRequestWrapper) request).getPayload();
    }
    return requestBody;
  }

  public static boolean isAjaxRequest(HttpServletRequest request) {
    String xRequestedWith = request.getHeader("X-Requested-With");
    return "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
  }

  public static boolean isReturnJson(HttpServletRequest request, HandlerMethod handler) {
    if (handler.getBeanType().isAnnotationPresent(RestController.class)) {
      return true;
    }
    Method method = handler.getMethod();
    if (method.isAnnotationPresent(ResponseBody.class)) {
      return true;
    }
    String accept = request.getHeader("Accept");
    String contentType = request.getHeader("Content-Type");
    return (accept != null && accept.contains("application/json"))
        || (contentType != null && contentType.contains("application/json"));
  }

  public static String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Real-IP");
    if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
      return ip;
    }
    ip = request.getHeader("X-Forwarded-For");
    if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
      int index = ip.indexOf(',');
      if (index != -1) {
        // 只获取第一个值
        return ip.substring(0, index);
      } else {
        return ip;
      }
    } else {
      // 取不到真实ip则返回空，不能返回内网地址。
      return "";
    }
  }

  private static String getUserAgent(HttpServletRequest request) {
    return request.getHeader("user-agent");
  }

}
