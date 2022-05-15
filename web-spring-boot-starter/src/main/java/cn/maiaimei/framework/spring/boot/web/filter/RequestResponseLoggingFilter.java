package cn.maiaimei.framework.spring.boot.web.filter;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import cn.maiaimei.framework.spring.boot.util.MDCUtils;
import cn.maiaimei.framework.spring.boot.web.util.HttpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link ContentCachingRequestWrapper}
 * {@link ContentCachingResponseWrapper}
 * {@link MDCInsertingServletFilter}
 */
@Slf4j
public class RequestResponseLoggingFilter extends HttpFilter {
    @SneakyThrows
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        boolean ignoreFilter = checkUri(request) || checkAccept(request) || checkMultipart(request);
        if (ignoreFilter) {
            super.doFilter(request, response, chain);
            return;
        }

        RepeatableHttpServletRequestWrapper requestWrapper = new RepeatableHttpServletRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        RequestResponseDetail rrd = HttpUtils.getRequestDetail(requestWrapper);
        long start = 0, end = 0;

        try {
            insertIntoMDC(rrd);
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
            log.info("{}, completed request in {} ms", new Builder(this).build(rrd), end - start);
            clearMDC();
        }
    }

    private boolean includeRemoteHost = false;
    private boolean includeDevice = false;
    private boolean includeOs = false;
    private boolean includeRequestMethod = true;
    private boolean includeRequestUri = true;
    private boolean includeRequestHeaders = true;
    private boolean includeRequestParams = true;
    private boolean includeRequestBody = true;
    private boolean includeResponseBody = false;
    private List<String> excludeUris;

    public void setIncludeRemoteHost(boolean includeRemoteHost) {
        this.includeRemoteHost = includeRemoteHost;
    }

    public void setIncludeDevice(boolean includeDevice) {
        this.includeDevice = includeDevice;
    }

    public void setIncludeOs(boolean includeOs) {
        this.includeOs = includeOs;
    }

    public void setIncludeRequestMethod(boolean includeRequestMethod) {
        this.includeRequestMethod = includeRequestMethod;
    }

    public void setIncludeRequestUri(boolean includeRequestUri) {
        this.includeRequestUri = includeRequestUri;
    }

    public void setIncludeRequestHeaders(boolean includeRequestHeaders) {
        this.includeRequestHeaders = includeRequestHeaders;
    }

    public void setIncludeRequestParams(boolean includeRequestParams) {
        this.includeRequestParams = includeRequestParams;
    }

    public void setIncludeRequestBody(boolean includeRequestBody) {
        this.includeRequestBody = includeRequestBody;
    }

    public void setIncludeResponseBody(boolean includeResponseBody) {
        this.includeResponseBody = includeResponseBody;
    }

    public void setExcludeUris(List<String> excludeUris) {
        this.excludeUris = excludeUris;
    }

    private boolean checkUri(HttpServletRequest request) {
        if (null != excludeUris && excludeUris.size() > 0) {
            return excludeUris.stream().anyMatch(excludeUri -> request.getRequestURI().startsWith(excludeUri) || request.getRequestURI().equals(excludeUri));
        }
        return false;
    }

    private boolean checkAccept(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (null != accept && accept.length() > 0) {
            return Arrays.stream(accept.split(",")).anyMatch(item -> item.startsWith("image/")
                    || item.startsWith("video/")
                    || item.equals("text/plain")
                    || item.equals("text/html")
                    || item.equals("text/xml")
                    || item.equals("application/xml")
                    || item.equals("application/atom+xml")
                    || item.equals("application/xhtml+xml")
                    || item.equals("application/pdf")
                    || item.equals("application/msword")
                    || item.equals("application/octet-stream"));
        }
        return false;
    }

    private boolean checkMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        return MediaType.APPLICATION_OCTET_STREAM.equals(contentType)
                || StringUtils.startsWithIgnoreCase(contentType, "multipart/");
    }

    private void insertIntoMDC(RequestResponseDetail rrd) {
        MDCUtils.setRequestId(rrd.getRequestId());
        MDCUtils.setRemoteHost(rrd.getRemoteHost());
        MDCUtils.setDevice(rrd.getDevice());
        MDCUtils.setOs(rrd.getOs());
        MDCUtils.setRequestMethod(rrd.getRequestMethod());
        MDCUtils.setRequestUri(rrd.getRequestUri());
        MDCUtils.setRequestHeaders(rrd.getRequestHeaders());
        MDCUtils.setRequestParams(rrd.getRequestParams());
        MDCUtils.setRequestBody(rrd.getRequestBody());
    }

    private void clearMDC() {
        MDCUtils.removeRequestId();
        MDCUtils.removeRemoteHost();
        MDCUtils.removeDevice();
        MDCUtils.removeOs();
        MDCUtils.removeRequestMethod();
        MDCUtils.removeRequestUri();
        MDCUtils.removeRequestHeaders();
        MDCUtils.removeRequestParams();
        MDCUtils.removeRequestBody();
    }

    private static class Builder {
        private RequestResponseLoggingFilter loggingFilter;

        private List<String> logs = new ArrayList<>();

        private Builder remoteHost(String remoteHost) {
            if (loggingFilter.includeRemoteHost) {
                logs.add(String.format("RemoteHost: %s", remoteHost));
            }
            return this;
        }

        private Builder device(String device) {
            if (loggingFilter.includeDevice) {
                logs.add(String.format("Device: %s", device));
            }
            return this;
        }

        private Builder os(String os) {
            if (loggingFilter.includeOs) {
                logs.add(String.format("OS: %s", os));
            }
            return this;
        }

        private Builder requestMethod(String requestMethod) {
            if (loggingFilter.includeRequestMethod) {
                logs.add(String.format("RequestMethod: %s", requestMethod));
            }
            return this;
        }

        private Builder requestUri(String requestUri) {
            if (loggingFilter.includeRequestUri) {
                logs.add(String.format("RequestUri: %s", requestUri));
            }
            return this;
        }

        private Builder requestHeaders(String requestHeaders) {
            if (loggingFilter.includeRequestHeaders) {
                logs.add(String.format("RequestHeaders: %s", requestHeaders));
            }
            return this;
        }

        private Builder requestParams(String requestParams) {
            if (loggingFilter.includeRequestParams) {
                logs.add(String.format("RequestParams: %s", requestParams));
            }
            return this;
        }

        private Builder requestBody(String requestBody) {
            if (loggingFilter.includeRequestBody) {
                logs.add(String.format("RequestBody: %s", requestBody));
            }
            return this;
        }

        private Builder responseBody(String responseBody) {
            if (loggingFilter.includeResponseBody) {
                logs.add(String.format("ResponseBody: %s", responseBody));
            }
            return this;
        }

        public Builder(RequestResponseLoggingFilter loggingFilter) {
            this.loggingFilter = loggingFilter;
        }

        public String build(RequestResponseDetail detail) {
            this.remoteHost(detail.getRemoteHost())
                    .device(detail.getDevice())
                    .os(detail.getOs())
                    .requestMethod(detail.getRequestMethod())
                    .requestUri(detail.getRequestUri())
                    .requestHeaders(detail.getRequestHeaders())
                    .requestParams(detail.getRequestParams())
                    .requestBody(detail.getRequestBody())
                    .responseBody(detail.getResponseBody());
            return String.join(", ", logs);
        }
    }

    @SuppressWarnings("unused")
    private static class PayloadTooLargeException extends RuntimeException {
        private static final long serialVersionUID = 660133056518301142L;
        private final int maxBodySize;

        public PayloadTooLargeException(int maxBodySize) {
            super();
            this.maxBodySize = maxBodySize;
        }
    }
}
