package cn.maiaimei.framework.spring.boot.web.util;

import cn.maiaimei.framework.beans.ErrorMap;
import cn.maiaimei.framework.spring.boot.util.MDCUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class ErrorUtils {
    private static final String LOG_FORMAT = "code:{}, message:{}, trace:{}, path:{}";

    public static Object handleError(HttpStatus status,
                                     String message,
                                     HttpServletRequest request,
                                     HandlerMethod handlerMethod,
                                     Throwable error,
                                     Boolean isPrintTrace,
                                     Boolean isWriteLog) {
        Integer code = status.value();
        String traceId = MDCUtils.getRequestId();
        Object trace = getTrace(error);
        Object path = HttpUtils.getRequestMethodAndUri(request);
        if (isWriteLog) {
            writeLog(code, message, trace, path);
        }
        ErrorMap errorMap = ErrorMap.builder()
                .code(code)
                .message(message)
                .traceId(traceId)
                .trace(isPrintTrace ? trace : StringUtils.EMPTY)
                .path(path)
                .build();
        if (HttpUtils.isAjaxRequest(request) || HttpUtils.isReturnJson(request, handlerMethod)) {
            // ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
            // modelAndView.addAllObjects(errorMap);
            // return modelAndView;
            return new ResponseEntity<Map<String, Object>>(errorMap, status);
        } else {
            //ModelAndView modelAndView = new ModelAndView("error", status);
            //modelAndView.addAllObjects(errorMap);
            //return modelAndView;
            return new ModelAndView("error", errorMap, status);
        }
    }

    public static void writeLog(Object code, Object message, Object trace, Object path) {
        log.error(LOG_FORMAT, code, message, trace, path);
    }

    private static String getTrace(Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        return stackTrace.toString();
    }
}
