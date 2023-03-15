package cn.maiaimei.framework.swagger;

import lombok.Data;

@Data
public class SwaggerProperties {
    private Boolean enabled = false;
    private String title;
    private String description;
    private String version;
    private String basePackage;
}
