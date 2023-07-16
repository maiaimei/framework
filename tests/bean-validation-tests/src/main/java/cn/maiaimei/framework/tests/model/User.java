package cn.maiaimei.framework.tests.model;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class User {

  @NotBlank
  private String username;
  private String password;
}
