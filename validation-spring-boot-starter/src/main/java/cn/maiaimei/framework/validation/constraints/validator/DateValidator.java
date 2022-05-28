package cn.maiaimei.framework.validation.constraints.validator;

import cn.maiaimei.framework.util.DateTimeUtils;
import cn.maiaimei.framework.validation.constraints.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<Date, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return DateTimeUtils.isValidDate(value);
    }
}
