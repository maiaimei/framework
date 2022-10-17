package cn.maiaimei.framework.web.http;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import cn.maiaimei.framework.util.MDCUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 请求响应日志过滤器
 * {@link ContentCachingRequestWrapper}
 * {@link ContentCachingResponseWrapper}
 * {@link MDCInsertingServletFilter}
 */
@Slf4j
public class RequestResponseLoggingFilter extends HttpFilter {
    @SneakyThrows
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        boolean ignoreFilter = checkUri(request) || isMultipartFormData(request, response);
        if (ignoreFilter) {
            super.doFilter(request, response, chain);
            return;
        }

        RequestWrapper requestWrapper = new RequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        RequestResponseDetail rrd = HttpUtils.getRequestResponseDetail(requestWrapper, excludeHeaderNames);
        long start = 0, end = 0;

        try {
            MDCUtils.setTraceId(UUID.randomUUID().toString().replaceAll("-", ""));

            start = System.currentTimeMillis();
            try {
                // 执行请求链
                super.doFilter(requestWrapper, responseWrapper, chain);
            } catch (NestedServletException e) {
                Throwable cause = e.getCause();
                // 请求体超过限制，以文本形式给客户端响应异常信息提示
                if (cause instanceof PayloadTooLargeException) {
                    throw cause;
                } else {
                    throw new RuntimeException(e);
                }
            }
            end = System.currentTimeMillis();

            rrd.setResponseBody(new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8));
            rrd.setResponseStatus(String.valueOf(responseWrapper.getStatus()));

            // 这一步很重要，把缓存的响应内容，输出到客户端
            responseWrapper.copyBodyToResponse();
        } finally {

            LoggingList<String> list = new LoggingList<>();
            list.add(formatLog("RequestUri", rrd.getRequestUri()));
            list.add(formatLog("RequestMethod", rrd.getRequestMethod()));
            list.add(formatLog("RequestHeaders", rrd.getRequestHeaders()));
            list.add(formatLog("RequestParams", rrd.getRequestParams()));
            list.add(formatLog("RequestBody", rrd.getRequestBody()));
            list.add(formatLog("ResponseStatus", rrd.getResponseStatus()));
            list.add(formatLog("ResponseBody", rrd.getResponseBody()));
            list.add(this.includeClientIP, formatLog("ClientIP", rrd.getClientIP()));
            list.add(this.includeClientDevice, formatLog("ClientDevice", rrd.getClientDevice()));
            list.add(this.includeClientOS, formatLog("ClientOS", rrd.getClientOS()));
            list.add(this.includeUserAgent, formatLog("UserAgent", rrd.getUserAgent()));

            log.info("\n{}\ncompleted request in {} ms", String.join("\n", list), end - start);

            MDCUtils.removeTraceId();
        }
    }

    private boolean includeUserAgent = false;
    private boolean includeClientIP = false;
    private boolean includeClientDevice = false;
    private boolean includeClientOS = false;
    private List<String> excludeHeaderNames;
    private List<String> excludeUris;

    public void setIncludeUserAgent(boolean includeUserAgent) {
        this.includeUserAgent = includeUserAgent;
    }

    public void setIncludeClientIP(boolean includeClientIP) {
        this.includeClientIP = includeClientIP;
    }

    public void setIncludeClientDevice(boolean includeClientDevice) {
        this.includeClientDevice = includeClientDevice;
    }

    public void setIncludeClientOS(boolean includeClientOS) {
        this.includeClientOS = includeClientOS;
    }

    public void setExcludeHeaderNames(List<String> excludeHeaderNames) {
        this.excludeHeaderNames = excludeHeaderNames;
    }

    public void setExcludeUris(List<String> excludeUris) {
        this.excludeUris = excludeUris;
    }

    private boolean checkUri(HttpServletRequest request) {
        List<String> uris = new ArrayList<>();
        uris.add("/favicon.ico");
        uris.add("/swagger");
        if (null != excludeUris && excludeUris.size() > 0) {
            uris.addAll(excludeUris);
        }
        return uris.stream().anyMatch(uri -> request.getRequestURI().startsWith(uri));
    }

    private boolean isMultipartFormData(HttpServletRequest request, HttpServletResponse response) {
        return (request.getContentType() != null && request.getContentType().equals(MediaType.MULTIPART_FORM_DATA_VALUE)) ||
                (response.getContentType() != null && response.getContentType().equals(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    private String formatLog(String key, String value) {
        return String.format("%-15s: %s", key, value);
    }

    static class LoggingList<E> extends ArrayList<E> {
        public boolean add(boolean condition, E e) {
            if (condition) {
                return super.add(e);
            }
            return Boolean.FALSE;
        }
    }

    @SuppressWarnings("unused")
    static class PayloadTooLargeException extends RuntimeException {
        private static final long serialVersionUID = 660133056518301142L;
        private final int maxBodySize;

        public PayloadTooLargeException(int maxBodySize) {
            super();
            this.maxBodySize = maxBodySize;
        }
    }
}
