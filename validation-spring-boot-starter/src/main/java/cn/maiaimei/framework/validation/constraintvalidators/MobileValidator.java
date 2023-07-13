package cn.maiaimei.framework.validation.constraintvalidators;

import cn.maiaimei.framework.util.RegexUtils;
import cn.maiaimei.framework.validation.constraints.Mobile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class MobileValidator implements ConstraintValidator<Mobile, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isNotBlank(value)) {
      return RegexUtils.isValidMobile(value);
    }
    return true;
  }
}
