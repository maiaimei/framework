package cn.maiaimei.framework.validation.model;

import com.google.common.collect.Lists;
import java.util.List;

public class ValidationResult {

  private List<ValidationMessage> validationMessageList;

  public ValidationResult() {
    this.validationMessageList = Lists.newArrayList();
  }

  public static ValidationResult newInstance() {
    ValidationResult result = new ValidationResult();
    result.validationMessageList = Lists.newArrayList();
    return result;
  }

  public void addMessage(String description) {
    ValidationMessage validationMessage = new ValidationMessage();
    validationMessage.setDescription(description);
    this.validationMessageList.add(validationMessage);
  }

  public void addMessage(String type, String code, String name, Object value, String description) {
    ValidationMessage validationMessage = new ValidationMessage();
    validationMessage.setType(type);
    validationMessage.setCode(code);
    validationMessage.setName(name);
    validationMessage.setValue(value);
    validationMessage.setDescription(description);
    this.validationMessageList.add(validationMessage);
  }

  public boolean hasErrors() {
    return !validationMessageList.isEmpty();
  }

  public List<ValidationMessage> getValidationMessageList() {
    return validationMessageList;
  }
}
