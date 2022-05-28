package cn.maiaimei.framework.validation.constraints.validator;

import cn.maiaimei.framework.util.IdNumberUtils;
import cn.maiaimei.framework.validation.constraints.IdNumber;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdNumberValidator implements ConstraintValidator<IdNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(value)) {
            return IdNumberUtils.isValid(value);
        }
        return true;
    }
}