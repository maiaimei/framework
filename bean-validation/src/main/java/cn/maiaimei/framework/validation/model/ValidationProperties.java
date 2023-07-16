package cn.maiaimei.framework.validation.model;

import lombok.Data;

@Data
public class ValidationProperties {

  private Boolean required;
  private Integer fixedlength;
  private Integer maxlength;
  private Integer minlength;
  private String length;
  private String regexp;
  private String pattern;
}
