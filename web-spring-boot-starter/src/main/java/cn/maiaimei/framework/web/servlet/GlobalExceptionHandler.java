package cn.maiaimei.framework.web.servlet;

import static cn.maiaimei.framework.constant.AppConstant.CODE;
import static cn.maiaimei.framework.constant.AppConstant.DOT;
import static cn.maiaimei.framework.constant.AppConstant.ERROR;
import static cn.maiaimei.framework.constant.AppConstant.MESSAGE;
import static cn.maiaimei.framework.constant.AppConstant.PATH;
import static cn.maiaimei.framework.constant.AppConstant.SOURCE;
import static cn.maiaimei.framework.constant.AppConstant.TRACE;
import static cn.maiaimei.framework.constant.AppConstant.TRACE_ID;

import cn.maiaimei.framework.beans.ErrorResult;
import cn.maiaimei.framework.exception.BusinessException;
import cn.maiaimei.framework.utils.MDCUtil;
import cn.maiaimei.framework.utils.MessageSourceUtils;
import cn.maiaimei.framework.validation.model.ValidationMessage;
import cn.maiaimei.framework.validation.model.ValidationResult;
import cn.maiaimei.framework.web.servlet.http.utils.HttpUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
@ConditionalOnWebApplication
public class GlobalExceptionHandler {

  @Resource
  private GlobalResponseProperties globalResponseProperties;

  @Autowired
  private LocaleResolver localeResolver;

  @ExceptionHandler(BusinessException.class)
  public Object handleBusinessException(HttpServletRequest request, HandlerMethod handlerMethod,
      BusinessException e) {
    return handleError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request,
        handlerMethod, e, Boolean.FALSE);
  }

  @ExceptionHandler(ServletException.class)
  public Object handleServletException(HttpServletRequest request,
      HandlerMethod handlerMethod, ServletException e) {
    return handleError(HttpStatus.BAD_REQUEST, e.getMessage(), request, handlerMethod,
        e, Boolean.FALSE);
  }

  /**
   * GET请求一般会使用PathVariable/RequestParam传参，
   * 如果RequestParam参数没有传递，会抛出MissingServletRequestParameterException异常。
   *
   * @param request       request
   * @param handlerMethod handlerMethod
   * @param e             e
   * @return java.lang.Object
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public Object handleMissingServletRequestParameterException(HttpServletRequest request,
      HandlerMethod handlerMethod, MissingServletRequestParameterException e) {
    String message = MessageSourceUtils.getMessage("messages",
        "cn.maiaimei.framework.validation.MissingRequestParameter.message", e.getParameterName(),
        localeResolver.resolveLocale(request));
    return handleError(HttpStatus.BAD_REQUEST, message, request, handlerMethod, e, Boolean.FALSE);
  }

  /**
   * POST、PUT请求一般会使用RequestBody传参， 如果参数没有传递或者参数类型没有无参构造器，会抛出HttpMessageNotReadableException异常。
   *
   * @param request       request
   * @param handlerMethod handlerMethod
   * @param e             e
   * @return java.lang.Object
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Object handleHttpMessageNotReadableException(HttpServletRequest request,
      HandlerMethod handlerMethod, HttpMessageNotReadableException e) {
    String message = StringUtils.defaultIfBlank(e.getMessage(),
        MessageSourceUtils.getMessage("messages",
            "cn.maiaimei.framework.validation.MissingRequestBody.message",
            localeResolver.resolveLocale(request)));
    return handleError(HttpStatus.BAD_REQUEST, message, request, handlerMethod, e, Boolean.FALSE);
  }

  /**
   * GET请求一般会使用PathVariable/RequestParam传参，
   * 如果RequestParam参数需要校验，则必须在Controller类上标注@Validated注解，并在入参上声明约束注解(如@Min等)，
   * 如果校验失败，会抛出ConstraintViolationException异常。
   *
   * @param request       request
   * @param handlerMethod handlerMethod
   * @param e             e
   * @return java.lang.Object
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public Object handleConstraintViolationException(HttpServletRequest request,
      HandlerMethod handlerMethod, ConstraintViolationException e) {
    String message = e.getMessage();
    message = message.replace(handlerMethod.getMethod().getName() + DOT, StringUtils.EMPTY);
    return handleError(HttpStatus.BAD_REQUEST, message, request, handlerMethod, e, Boolean.FALSE);
  }

  /**
   * 用于处理请求参数为实体类校验引发的异常 MethodArgumentNotValidException, Content-Type为application/json
   * BindException, Content-Type为application/x-www-form-urlencoded
   * <example>@Valid @RequestBody User user</example>
   * <example>@Validated @RequestBody User user</example>
   *
   * @param request       request
   * @param handlerMethod handlerMethod
   * @param e             e
   * @return java.lang.Object
   */
  @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
  public Object handleMethodArgumentNotValidExceptionOrBindException(HttpServletRequest request,
      HandlerMethod handlerMethod, Exception e) {
    BindingResult bindingResult;
    if (e instanceof MethodArgumentNotValidException) {
      MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
      bindingResult = ex.getBindingResult();
    } else {
      BindException ex = (BindException) e;
      bindingResult = ex.getBindingResult();
    }
    List<ObjectError> errors = bindingResult.getAllErrors();
    final ValidationResult validationResult = new ValidationResult();
    errors.forEach(error -> {
      final ValidationMessage validationMessage = new ValidationMessage();
      if (error instanceof FieldError) {
        validationMessage.setCode(((FieldError) error).getField());
        validationMessage.setValue(((FieldError) error).getRejectedValue());
      }
      validationMessage.setDescription(error.getDefaultMessage());
      validationResult.addError(validationMessage);
    });
    return handleError(HttpStatus.BAD_REQUEST, validationResult, request, handlerMethod,
        e, Boolean.FALSE);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public Object handleMethodArgumentTypeMismatchException(HttpServletRequest request,
      HandlerMethod handlerMethod, MethodArgumentTypeMismatchException e) {
    return handleError(HttpStatus.BAD_REQUEST, e.getMessage(), request, handlerMethod,
        e, Boolean.FALSE);
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public Object handleHttpClientErrorException(HttpServletRequest request,
      HandlerMethod handlerMethod, HttpClientErrorException e) {
    return handleError(e.getStatusCode(), e.getMessage(), request, handlerMethod, e, Boolean.TRUE);
  }

  @ExceptionHandler(HttpServerErrorException.class)
  public Object handleHttpServerErrorException(HttpServletRequest request,
      HandlerMethod handlerMethod, HttpServerErrorException e) {
    return handleError(e.getStatusCode(), e.getMessage(), request, handlerMethod, e, Boolean.TRUE);
  }

  @ExceptionHandler(Throwable.class)
  public Object handleThrowable(HttpServletRequest request, HandlerMethod handlerMethod,
      Throwable e) {
    return handleError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request, handlerMethod, e,
        Boolean.TRUE);
  }

  private Object handleError(HttpStatus status, Object message, HttpServletRequest request,
      HandlerMethod handlerMethod, Throwable error, Boolean isNeedWriteLog) {
    int code = status.value();
    final String source = MDCUtil.getSource();
    String traceId = MDCUtil.getTraceId();
    String trace = getTrace(error);
    String path = HttpUtil.getRequestMethodAndUri(request);

    if (isNeedWriteLog && StringUtils.isNotBlank(trace)) {
      log.error("{}", trace);
    }
    if (!globalResponseProperties.isShowTrace()) {
      trace = StringUtils.EMPTY;
    }

    if (HttpUtil.isAjaxRequest(request) || HttpUtil.isReturnJson(request, handlerMethod)) {
      final ErrorResult<Object> result = new ErrorResult<>();
      result.setSource(source);
      result.setCode(code);
      result.setMessage(message);
      result.setTraceId(traceId);
      result.setTrace(trace);
      result.setPath(path);
      return new ResponseEntity<>(result, status);
    } else {
      Map<String, Object> model = new LinkedHashMap<>();
      model.put(SOURCE, source);
      model.put(CODE, code);
      model.put(MESSAGE, message);
      model.put(TRACE_ID, traceId);
      model.put(TRACE, trace);
      model.put(PATH, path);
      return new ModelAndView(ERROR, model, status);
    }
  }

  private String getTrace(Throwable error) {
    StringWriter stackTrace = new StringWriter();
    error.printStackTrace(new PrintWriter(stackTrace));
    stackTrace.flush();
    return stackTrace.toString();
  }
}
