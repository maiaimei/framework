package cn.maiaimei.framework.web.servlet;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import cn.maiaimei.framework.util.MDCUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

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

    private List<String> excludeUriList;

    @Override
    public void init() {
        excludeUriList = new ArrayList<>();
        excludeUriList.add("/favicon.ico");
        excludeUriList.add("/error");
        // swagger
        excludeUriList.add("/v2/api-docs");
        excludeUriList.add("/v3/api-docs");
        excludeUriList.add("/swagger-resources");
        excludeUriList.add("/swagger-resources/configuration/ui");
        excludeUriList.add("/swagger-resources/configuration/security");
        if (!CollectionUtils.isEmpty(excludeUris)) {
            excludeUriList.addAll(excludeUris);
        }
    }

    @SneakyThrows
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        boolean ignoreFilter = checkUri(request) || isMultipartFormData(request, response);
        if (ignoreFilter) {
            chain.doFilter(request, response);
            return;
        }

        RequestWrapper requestWrapper = new RequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        RequestResponseDetail rrd = HttpUtils.getRequestResponseDetail(requestWrapper, excludeHeaderNames);
        long start = 0, end = 0;
        final String source = request.getHeader("source");

        try {
            if (StringUtils.isNotBlank(source)) {
                MDCUtils.setSource(source);
            }
            MDCUtils.setTraceId(UUID.randomUUID().toString().replace("-", ""));

            start = System.currentTimeMillis();
            chain.doFilter(requestWrapper, responseWrapper);
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
            if (StringUtils.isBlank(source)) {
                MDCUtils.removeSource();
            }
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
        final String contextPath = StringUtils.defaultIfBlank(request.getContextPath(), StringUtils.EMPTY);
        final String requestURI = request.getRequestURI();
        return excludeUriList.stream().anyMatch(uri -> contextPath.concat(requestURI).equals(uri));
    }

    private boolean isMultipartFormData(HttpServletRequest request, HttpServletResponse response) {
        return StringUtils.equalsIgnoreCase(request.getContentType(), MediaType.MULTIPART_FORM_DATA_VALUE) ||
                StringUtils.equalsIgnoreCase(response.getContentType(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    private String formatLog(String key, String value) {
        return String.format("%-15s: %s", key, value);
    }

    static class LoggingList<E> extends ArrayList<E> {
        public void add(boolean condition, E e) {
            if (condition) {
                super.add(e);
            }
        }
    }
}
