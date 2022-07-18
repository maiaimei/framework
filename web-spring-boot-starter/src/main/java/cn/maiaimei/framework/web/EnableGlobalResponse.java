package cn.maiaimei.framework.web;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 是否启用接口返回值统一处理
 *
 * @author maiaimei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GlobalResponseHandler.class)
public @interface EnableGlobalResponse {
    String[] basePackages() default {};
}
