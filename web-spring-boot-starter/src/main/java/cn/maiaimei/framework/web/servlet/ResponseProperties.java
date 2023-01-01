package cn.maiaimei.framework.web.servlet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "response")
public class ResponseProperties {
    private boolean showTrace;

    public void setShowTrace(boolean showTrace) {
        this.showTrace = showTrace;
    }

    public boolean isShowTrace() {
        return showTrace;
    }
}
