package cn.maiaimei.framework.web.servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "response")
@ConditionalOnWebApplication
public class GlobalResponseProperties {

  private boolean showTrace;

  public void setShowTrace(boolean showTrace) {
    this.showTrace = showTrace;
  }

  public boolean isShowTrace() {
    return showTrace;
  }
}
