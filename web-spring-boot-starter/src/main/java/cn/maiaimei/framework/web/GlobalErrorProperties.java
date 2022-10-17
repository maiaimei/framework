package cn.maiaimei.framework.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "global-error")
public class GlobalErrorProperties {
    private boolean showTrace;

    public void setShowTrace(boolean showTrace) {
        this.showTrace = showTrace;
    }

    public boolean isShowTrace() {
        return showTrace;
    }
}
