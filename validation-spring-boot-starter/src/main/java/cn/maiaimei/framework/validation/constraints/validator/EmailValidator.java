package cn.maiaimei.framework.validation.constraints.validator;

import cn.maiaimei.framework.util.RegexUtils;
import cn.maiaimei.framework.validation.constraints.Email;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value)) {
            return RegexUtils.isValidEmail(value);
        }
        return true;
    }
}
