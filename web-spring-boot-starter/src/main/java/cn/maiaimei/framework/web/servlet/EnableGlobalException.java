package cn.maiaimei.framework.web.servlet;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * 是否启用全局异常处理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
    GlobalResponseProperties.class,
    GlobalErrorController.class,
    GlobalExceptionHandler.class
})
public @interface EnableGlobalException {

}
