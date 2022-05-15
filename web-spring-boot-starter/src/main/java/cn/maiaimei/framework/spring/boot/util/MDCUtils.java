package cn.maiaimei.framework.spring.boot.util;

import org.slf4j.MDC;

public class MDCUtils {
    public static final String REQUEST_ID = "req.requestId";
    public static final String REQUEST_METHOD = "req.requestMethod";
    public static final String REQUEST_URI = "req.requestURI";
    public static final String REQUEST_HEADERS = "req.requestHeaders";
    public static final String REQUEST_PARAMS = "req.requestParams";
    public static final String REQUEST_BODY = "req.requestBody";
    public static final String REMOTE_HOST = "req.remoteHost";
    public static final String DEVICE = "req.device";
    public static final String OS = "req.os";

    public static void setRequestId(String requestId) {
        MDC.put(REQUEST_ID, requestId);
    }

    public static void setRemoteHost(String remoteHost) {
        MDC.put(REMOTE_HOST, remoteHost);
    }

    public static void setDevice(String device) {
        MDC.put(DEVICE, device);
    }

    public static void setOs(String os) {
        MDC.put(OS, os);
    }

    public static void setRequestMethod(String requestMethod) {
        MDC.put(REQUEST_METHOD, requestMethod);
    }

    public static void setRequestUri(String requestUri) {
        MDC.put(REQUEST_URI, requestUri);
    }

    public static void setRequestHeaders(String requestHeaders) {
        MDC.put(REQUEST_HEADERS, requestHeaders);
    }

    public static void setRequestParams(String requestParams) {
        MDC.put(REQUEST_PARAMS, requestParams);
    }

    public static void setRequestBody(String requestBody) {
        MDC.put(REQUEST_BODY, requestBody);
    }

    public static String getRequestId() {
        return MDC.get(REQUEST_ID);
    }

    public static String getRemoteHost() {
        return MDC.get(REMOTE_HOST);
    }

    public static String getDevice() {
        return MDC.get(DEVICE);
    }

    public static String getOs() {
        return MDC.get(OS);
    }

    public static String getRequestMethod() {
        return MDC.get(REQUEST_METHOD);
    }

    public static String getRequestUri() {
        return MDC.get(REQUEST_URI);
    }

    public static String getRequestHeaders() {
        return MDC.get(REQUEST_HEADERS);
    }

    public static String getRequestParams() {
        return MDC.get(REQUEST_PARAMS);
    }

    public static String getRequestBody() {
        return MDC.get(REQUEST_BODY);
    }

    public static void removeRequestId() {
        MDC.remove(REQUEST_ID);
    }

    public static void removeRemoteHost() {
        MDC.remove(REMOTE_HOST);
    }

    public static void removeDevice() {
        MDC.remove(DEVICE);
    }

    public static void removeOs() {
        MDC.remove(OS);
    }

    public static void removeRequestMethod() {
        MDC.remove(REQUEST_METHOD);
    }

    public static void removeRequestUri() {
        MDC.remove(REQUEST_URI);
    }

    public static void removeRequestHeaders() {
        MDC.remove(REQUEST_HEADERS);
    }

    public static void removeRequestParams() {
        MDC.remove(REQUEST_PARAMS);
    }

    public static void removeRequestBody() {
        MDC.remove(REQUEST_BODY);
    }
}
