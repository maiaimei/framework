package cn.maiaimei.framework.validation.constraints;

import cn.maiaimei.framework.validation.constraints.validator.BirthdayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {BirthdayValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Birthday {
    String message() default "{cn.maiaimei.framework.validation.constraints.Birthday.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
