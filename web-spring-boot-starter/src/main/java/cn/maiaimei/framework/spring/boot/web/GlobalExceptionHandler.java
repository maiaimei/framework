package cn.maiaimei.framework.spring.boot.web;

import cn.maiaimei.framework.exception.BusinessException;
import cn.maiaimei.framework.spring.boot.web.util.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author maiaimei
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private GlobalErrorConfig globalErrorConfig;

    @Autowired
    public void setGlobalErrorConfig(GlobalErrorConfig globalErrorConfig) {
        this.globalErrorConfig = globalErrorConfig;
    }

    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException(HttpServletRequest request, HandlerMethod handlerMethod, BusinessException e) {
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request, handlerMethod, e);
    }

    /**
     * GET请求一般会使用PathVariable/RequestParam传参，
     * 如果RequestParam参数没有传递，会抛出MissingServletRequestParameterException异常。
     *
     * @param request
     * @param handlerMethod
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object handleMissingServletRequestParameterException(HttpServletRequest request, HandlerMethod handlerMethod, MissingServletRequestParameterException e) {
        String message = "请求参数 " + e.getParameterName() + " 不能为空";
        return handleError(HttpStatus.BAD_REQUEST, message, request, handlerMethod, e);
    }

    /**
     * POST、PUT请求一般会使用RequestBody传参，
     * 如果参数没有传递或者参数类型没有无参构造器，会抛出HttpMessageNotReadableException异常。
     *
     * @param request
     * @param handlerMethod
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpServletRequest request, HandlerMethod handlerMethod, HttpMessageNotReadableException e) {
        String message = StringUtils.defaultIfBlank(e.getMessage(), "请求体不能为空");
        return handleError(HttpStatus.BAD_REQUEST, message, request, handlerMethod, e);
    }

    /**
     * GET请求一般会使用PathVariable/RequestParam传参，
     * 如果RequestParam参数需要校验，则必须在Controller类上标注@Validated注解，并在入参上声明约束注解(如@Min等)，
     * 如果校验失败，会抛出ConstraintViolationException异常。
     *
     * @param request
     * @param handlerMethod
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolationException(HttpServletRequest request, HandlerMethod handlerMethod, ConstraintViolationException e) {
        String message = e.getMessage();
        message = message.replaceAll(handlerMethod.getMethod().getName() + ".", "");
        return handleError(HttpStatus.BAD_REQUEST, message, request, handlerMethod, e);
    }

    /**
     * 用于处理请求参数为实体类校验引发的异常
     * MethodArgumentNotValidException, Content-Type为application/json
     * BindException, Content-Type为application/x-www-form-urlencoded
     * <example>@Valid @RequestBody User user</example>
     * <example>@Validated @RequestBody User user</example>
     *
     * @param request
     * @param handlerMethod
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public Object handleMethodArgumentNotValidExceptionOrBindException(HttpServletRequest request, HandlerMethod handlerMethod, Exception e) {
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            bindingResult = ex.getBindingResult();
        } else {
            BindException ex = (BindException) e;
            bindingResult = ex.getBindingResult();
        }
        StringBuilder stringBuilder = new StringBuilder();
        List<ObjectError> errors = bindingResult.getAllErrors();
        errors.forEach(error -> {
            if (error instanceof FieldError) {
                stringBuilder.append(((FieldError) error).getField()).append(": ");
            }
            stringBuilder.append(error.getDefaultMessage()).append(", ");
        });
        String message = stringBuilder.toString();
        message = StringUtils.substring(message, 0, message.length() - 2);
        return handleError(HttpStatus.BAD_REQUEST, message, request, handlerMethod, e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handleMethodArgumentTypeMismatchException(HttpServletRequest request, HandlerMethod handlerMethod, MethodArgumentTypeMismatchException e) {
        return handleError(HttpStatus.BAD_REQUEST, e.getMessage(), request, handlerMethod, e);
    }

    @ExceptionHandler(Throwable.class)
    public Object handleThrowable(HttpServletRequest request, HandlerMethod handlerMethod, Throwable e) {
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request, handlerMethod, e, true);
    }

    private Object handleError(HttpStatus status, String message, HttpServletRequest request, HandlerMethod handlerMethod, Throwable error) {
        return handleError(status, message, request, handlerMethod, error, false);
    }

    private Object handleError(HttpStatus status, String message, HttpServletRequest request, HandlerMethod handlerMethod, Throwable error, Boolean isWriteLog) {
        return ErrorUtils.handleError(status, message, request, handlerMethod, error, globalErrorConfig.isShowTrace(), isWriteLog);
    }
}