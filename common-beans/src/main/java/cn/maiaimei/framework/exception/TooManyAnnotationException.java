package cn.maiaimei.framework.exception;

public class TooManyAnnotationException extends RuntimeException {
    public TooManyAnnotationException(Class<?> clazz) {
        super(clazz.getSimpleName() + " annotation is used too many times. It can only be used once.");
    }
}
