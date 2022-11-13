package cn.maiaimei.framework.web;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 是否启用全局异常处理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        ResponseProperties.class,
        GlobalErrorController.class,
        GlobalExceptionHandler.class
})
public @interface EnableGlobalException {
}
