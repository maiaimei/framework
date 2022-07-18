package cn.maiaimei.framework.util;

import org.slf4j.MDC;

public class MDCUtils {
    public static final String TRACE_ID = "traceId";

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void removeTraceId() {
        MDC.remove(TRACE_ID);
    }
}
