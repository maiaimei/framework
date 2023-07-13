package cn.maiaimei.framework.validation.constraintvalidators;

import cn.maiaimei.framework.util.DateTimeUtils;
import cn.maiaimei.framework.validation.constraints.Time;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeValidator implements ConstraintValidator<Time, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return DateTimeUtils.isValidTime(value);
  }
}
