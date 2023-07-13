package cn.maiaimei.framework.validation.constraintvalidators;

import cn.maiaimei.framework.validation.constraints.FixedLength;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FixedLengthValidator implements ConstraintValidator<FixedLength, String> {

  private int length;

  @Override
  public void initialize(FixedLength constraintAnnotation) {
    this.length = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return Boolean.FALSE;
    }
    return value.length() == this.length;
  }
}
