package cn.maiaimei.framework.validation.constraintvalidators;

import cn.maiaimei.framework.util.RegexUtils;
import cn.maiaimei.framework.validation.constraints.Password;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class PasswordValidator implements ConstraintValidator<Password, String> {

  private RegexUtils.PasswordType type;
  private int min;
  private int max;

  @Override
  public void initialize(Password constraintAnnotation) {
    this.type = RegexUtils.PasswordType.CONTAIN_ALPHABET_NUMBER_SYMBOL;
    this.min = constraintAnnotation.min();
    this.max = constraintAnnotation.max();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isNotBlank(value)) {
      return RegexUtils.isValidPassword(type, value, min, max);
    }
    return true;
  }
}
