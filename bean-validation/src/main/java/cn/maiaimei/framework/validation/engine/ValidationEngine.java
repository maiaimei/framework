package cn.maiaimei.framework.validation.engine;

import cn.maiaimei.framework.utils.MessageUtil;
import cn.maiaimei.framework.validation.model.ValidationResult;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;

@Slf4j
public class ValidationEngine {

  @Setter
  @Getter
  private String type;

  @Setter
  @Getter
  private ResourceBundle resourceBundle;

  private static final String DOT = ".";
  private static final Validator VALIDATOR;
  private static final Validator FAIL_FAST_VALIDATOR;
  private static final ExecutableValidator EXECUTABLE_VALIDATOR;
  private static final ExecutableValidator FAIL_FAST_EXECUTABLE_VALIDATOR;

  static {
    VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    FAIL_FAST_VALIDATOR = Validation.byProvider(HibernateValidator.class)
        .configure().failFast(Boolean.TRUE)
        .buildValidatorFactory().getValidator();
    EXECUTABLE_VALIDATOR = VALIDATOR.forExecutables();
    FAIL_FAST_EXECUTABLE_VALIDATOR = FAIL_FAST_VALIDATOR.forExecutables();
  }

  private ValidationEngine() {
  }

  public static ValidationEngine getValidationEngine() {
    return getValidationEngine(null, null);
  }

  public static ValidationEngine getValidationEngine(String type) {
    return getValidationEngine(type, null);
  }

  public static ValidationEngine getValidationEngine(String type, String resourceBundleBaseName) {
    final ValidationEngine engine = new ValidationEngine();
    engine.setType(type);
    if (StringUtils.isNotBlank(resourceBundleBaseName)) {
      engine.setResourceBundle(MessageUtil.getBundle(resourceBundleBaseName));
    }
    return engine;
  }

  public <T> ValidationResult validate(T object, Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object, groups);
    return checkConstraintViolations(constraintViolations);
  }

  public <T> ValidationResult validateValue(Class<T> beanType,
      String propertyName,
      Object value,
      Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validateValue(beanType,
        propertyName, value, groups);
    return checkConstraintViolations(constraintViolations);
  }

  public <T> ValidationResult validateProperty(T object,
      String propertyName,
      Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validateProperty(object,
        propertyName, groups);
    return checkConstraintViolations(constraintViolations);
  }

  public <T> ValidationResult validateParameters(T object,
      Method method, Object[] parameterValues, Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = EXECUTABLE_VALIDATOR.validateParameters(
        object, method, parameterValues, groups);
    return checkConstraintViolations(constraintViolations);
  }

  public <T> ValidationResult validateReturnValue(T object,
      Method method, Object returnValue, Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = EXECUTABLE_VALIDATOR.validateReturnValue(
        object, method, returnValue, groups);
    return checkConstraintViolations(constraintViolations);
  }

  public <T> ValidationResult validateByConstraintPath(String constraintPath, T object) {
    String constraints;
    try {
      constraints = IOUtils.toString(ValidationEngine.class.getResourceAsStream(constraintPath),
          StandardCharsets.UTF_8);
    } catch (Exception e) {
      String msg = "Can't load constraints for " + constraintPath;
      log.error(msg, e);
      ValidationResult validationResult = ValidationResult.newInstance();
      validationResult.addMessage(msg);
      return validationResult;
    }
    if (StringUtils.isBlank(constraints)) {
      String msg = "Constraints is blank for " + constraintPath;
      ValidationResult validationResult = ValidationResult.newInstance();
      validationResult.addMessage(msg);
      return validationResult;
    }
    ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure()
        .addMapping(new ByteArrayInputStream(constraints.getBytes()))
        .failFast(Boolean.TRUE)
        .buildValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
    return checkConstraintViolations(constraintViolations);
  }

  public <T> ValidationResult failFastValidate(T object, Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = FAIL_FAST_VALIDATOR.validate(object, groups);
    return checkConstraintViolations(constraintViolations);
  }

  public <T> ValidationResult failFastValidateParameters(T object,
      Method method, Object[] parameterValues, Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = FAIL_FAST_EXECUTABLE_VALIDATOR.validateParameters(
        object, method, parameterValues, groups);
    return checkConstraintViolations(constraintViolations);
  }

  public <T> ValidationResult failFastValidateReturnValue(T object,
      Method method, Object returnValue, Class<?>... groups) {
    Set<ConstraintViolation<T>> constraintViolations = FAIL_FAST_EXECUTABLE_VALIDATOR.validateReturnValue(
        object, method, returnValue, groups);
    return checkConstraintViolations(constraintViolations);
  }

  private <T> ValidationResult checkConstraintViolations(
      Set<ConstraintViolation<T>> constraintViolations) {
    ValidationResult validationResult = new ValidationResult();
    if (Objects.nonNull(constraintViolations) && !constraintViolations.isEmpty()) {
      constraintViolations
          .forEach(cv -> {
            final String code = getPropertyPath(cv);
            String name = MessageUtil.get(resourceBundle, code, code);
            validationResult.addMessage(this.type,
                code,
                name,
                cv.getInvalidValue(),
                cv.getMessage());
          });
    }
    return validationResult;
  }

  private <T> String getPropertyPath(ConstraintViolation<T> constraintViolation) {
    if (Objects.nonNull(constraintViolation.getRootBeanClass())) {
      return constraintViolation.getRootBeanClass().getName().concat(DOT)
          .concat(constraintViolation.getPropertyPath().toString());
    }
    return constraintViolation.getPropertyPath().toString();
  }
}
