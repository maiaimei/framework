package cn.maiaimei.framework.swagger;

import io.swagger.annotations.Api;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.function.Predicate;

public class SwaggerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "swagger")
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    public Docket docket(SwaggerProperties swaggerProperties) {
        Predicate<RequestHandler> selector;
        if (StringUtils.hasText(swaggerProperties.getBasePackage())) {
            selector = RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage());
        } else {
            selector = RequestHandlerSelectors.withClassAnnotation(Api.class);
        }
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(selector)
                .paths(PathSelectors.any()).build()
                .enable(swaggerProperties.getEnabled());
    }

    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .build();
    }

}
