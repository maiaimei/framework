package cn.maiaimei.framework.validation.constraintvalidators;

import cn.maiaimei.framework.util.IdNumberUtils;
import cn.maiaimei.framework.validation.constraints.Birthday;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BirthdayValidator implements ConstraintValidator<Birthday, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return IdNumberUtils.isValidBirthday(value);
  }
}
