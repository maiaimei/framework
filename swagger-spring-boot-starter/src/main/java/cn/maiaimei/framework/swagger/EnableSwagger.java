package cn.maiaimei.framework.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        SwaggerConfigProperties.class,
        SwaggerConfig.class
})
public @interface EnableSwagger {
}
