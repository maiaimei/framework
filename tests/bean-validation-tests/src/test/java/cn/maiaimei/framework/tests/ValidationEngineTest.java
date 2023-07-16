package cn.maiaimei.framework.tests;

import cn.maiaimei.framework.tests.model.User;
import cn.maiaimei.framework.validation.engine.ValidationEngine;
import cn.maiaimei.framework.validation.model.ValidationProperties;
import cn.maiaimei.framework.validation.model.ValidationResult;
import cn.maiaimei.framework.validation.model.ValidationType;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.junit.jupiter.api.Test;

@Slf4j
public class ValidationEngineTest {

  @Test
  void test_validate() {
    final User user = new User();
    ValidationResult result = ValidationEngine.getValidationEngine(ValidationType.Bean.name())
        .validate(user);
    printValidationResult(result);
  }

  @Test
  void test_validateValue() {
    ValidationResult result = ValidationEngine.getValidationEngine(ValidationType.Bean.name())
        .validateValue(User.class, "password", null);
    printValidationResult(result);
  }

  /**
   * <a
   * href="https://blog.csdn.net/weixin_42286276/article/details/127918404">java利用XPath将XML转为对象，并查询特定节点</a>
   */
  @Test
  void test() {
    final ValidationProperties properties = new ValidationProperties();
    properties.setRequired(Boolean.TRUE);
    if (Objects.nonNull(properties.getRequired()) && properties.getRequired()) {
      // TODO: Class<? extends Annotation> notBlank = NotBlank.class;
      final NotBlankValidator notBlankValidator = new NotBlankValidator();
      final boolean valid = notBlankValidator.isValid(null, null);
      System.out.println(valid);
    }
  }

  private void printValidationResult(ValidationResult result) {
    if (result.hasErrors()) {
      result.getValidationMessageList().forEach(validationMessage -> {
        log.info("Error Code: {}", validationMessage.getCode());
        log.info("Error Name: {}", validationMessage.getName());
        log.info("Error Message: {}", validationMessage.getDescription());
        log.info("Error Value: {}", validationMessage.getValue());
      });
    }
  }
}
