package cn.maiaimei.framework.validation.constraints.validator;

import cn.maiaimei.framework.validation.constraints.EnumValid;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

public class EnumValidator implements ConstraintValidator<EnumValid, Object> {
    private Class<? extends Enum> enumType;

    private String methodName;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.enumType = constraintAnnotation.enumType();
        this.methodName = constraintAnnotation.methodName();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Object[] objs = enumType.getEnumConstants();
        Method method1 = enumType.getMethod("name");
        for (Object obj : objs) {
            Object code = method1.invoke(obj, null);
            if (value.toString().equals(code.toString())) {
                return true;
            }
        }
        if (StringUtils.isNotBlank(methodName)) {
            Method method2 = enumType.getMethod(methodName);
            for (Object obj : objs) {
                Object code = method2.invoke(obj, null);
                if (value.toString().equals(code.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
