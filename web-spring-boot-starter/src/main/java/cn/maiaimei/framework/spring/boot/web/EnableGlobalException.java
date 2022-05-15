package cn.maiaimei.framework.spring.boot.web;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 是否启用全局异常处理
 *
 * @author maiaimei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        GlobalErrorConfig.class,
        GlobalErrorController.class,
        GlobalExceptionHandler.class
})
public @interface EnableGlobalException {
    boolean isShowTrace() default false;
}
