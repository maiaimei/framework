package cn.maiaimei.framework.exception;

/**
 * 业务异常
 *
 * @author : maiaimei
 * @date : 2023-1-2
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
