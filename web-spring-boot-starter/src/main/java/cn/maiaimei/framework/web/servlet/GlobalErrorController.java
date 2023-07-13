package cn.maiaimei.framework.web.servlet;

import static cn.maiaimei.framework.constant.AppConstant.CODE;
import static cn.maiaimei.framework.constant.AppConstant.ERROR;
import static cn.maiaimei.framework.constant.AppConstant.MESSAGE;
import static cn.maiaimei.framework.constant.AppConstant.PATH;
import static cn.maiaimei.framework.constant.AppConstant.TRACE;
import static cn.maiaimei.framework.constant.AppConstant.TRACE_ID;

import cn.maiaimei.framework.utils.MDCUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理
 */
@Slf4j
@Controller
@ConditionalOnWebApplication
public class GlobalErrorController extends BasicErrorController {

  @Autowired
  private GlobalResponseProperties globalResponseProperties;

  @Autowired
  public GlobalErrorController(ServerProperties errorAttributes) {
    super(new DefaultErrorAttributes(), errorAttributes.getError());
  }

  @Override
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    HttpStatus status = getStatus(request);
    response.setStatus(status.value());
    Map<String, Object> model = getErrorModel(status, request, MediaType.TEXT_HTML);
    ModelAndView modelAndView = resolveErrorView(request, response, status, model);
    return (modelAndView != null) ? modelAndView : new ModelAndView(ERROR, model);
  }

  @Override
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    if (status == HttpStatus.NO_CONTENT) {
      return new ResponseEntity<>(status);
    }
    Map<String, Object> body = getErrorModel(status, request, MediaType.ALL);
    return new ResponseEntity<>(body, status);
  }

  private Map<String, Object> getErrorModel(HttpStatus status,
      HttpServletRequest request,
      MediaType mediaType) {
    Map<String, Object> errorAttributes = getErrorAttributes(request,
        getErrorAttributeOptions(request, mediaType));
    Object trace = errorAttributes.get(TRACE);
    if (trace != null && StringUtils.isNotBlank(trace.toString())) {
      log.error("{}", trace);
    }

    Map<String, Object> model = new LinkedHashMap<>();
    model.put(CODE, status.value());
    model.put(MESSAGE, Optional.ofNullable(errorAttributes.get(MESSAGE))
        .orElseGet(() -> errorAttributes.get(ERROR)));
    model.put(TRACE_ID, MDCUtil.getTraceId());
    model.put(TRACE, globalResponseProperties.isShowTrace() ? trace : StringUtils.EMPTY);
    model.put(PATH, errorAttributes.get(PATH));
    return model;
  }
}
