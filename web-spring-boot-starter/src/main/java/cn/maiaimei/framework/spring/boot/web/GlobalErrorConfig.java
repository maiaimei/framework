package cn.maiaimei.framework.spring.boot.web;

import cn.maiaimei.framework.exception.TooManyAnnotationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;

public class GlobalErrorConfig implements ApplicationContextAware, InitializingBean {
    private boolean isShowTrace = false;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableGlobalException.class);
        if (beansWithAnnotation.size() > 1) {
            throw new TooManyAnnotationException(EnableGlobalException.class);
        }
        for (String key : beansWithAnnotation.keySet()) {
            EnableGlobalException annotation = AnnotationUtils.findAnnotation(beansWithAnnotation.get(key).getClass(), EnableGlobalException.class);
            if (null != annotation) {
                isShowTrace = annotation.isShowTrace();
            }
        }
    }

    public boolean isShowTrace() {
        return isShowTrace;
    }
}
