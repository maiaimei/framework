package cn.maiaimei.framework.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfigProperties {
    private Boolean enabled;
    private String title;
    private String description;
    private String version;
}
