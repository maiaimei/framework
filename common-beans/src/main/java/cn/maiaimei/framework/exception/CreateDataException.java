package cn.maiaimei.framework.exception;

/**
 * 创建数据异常
 *
 * @author : maiaimei
 * @date : 2023-1-2
 */
public class CreateDataException extends RuntimeException {
    public CreateDataException(String message) {
        super(message);
    }
}
