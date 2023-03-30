package cn.maiaimei.framework.util;

import org.slf4j.MDC;

public class MDCUtils {
    private static final String TRACE_ID = "traceId";
    private static final String SOURCE = "source";

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void removeTraceId() {
        MDC.remove(TRACE_ID);
    }

    public static void setSource(String source) {
        MDC.put(SOURCE, source);
    }

    public static String getSource() {
        return MDC.get(SOURCE);
    }

    public static void removeSource() {
        MDC.remove(SOURCE);
    }

}
