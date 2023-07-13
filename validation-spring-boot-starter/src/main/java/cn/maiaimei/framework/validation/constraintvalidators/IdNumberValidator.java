package cn.maiaimei.framework.validation.constraintvalidators;

import cn.maiaimei.framework.util.IdNumberUtils;
import cn.maiaimei.framework.validation.constraints.IdNumber;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class IdNumberValidator implements ConstraintValidator<IdNumber, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isNotBlank(value)) {
      return IdNumberUtils.isValid(value);
    }
    return true;
  }
}
