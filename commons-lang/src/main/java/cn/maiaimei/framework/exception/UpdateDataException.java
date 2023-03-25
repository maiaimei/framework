package cn.maiaimei.framework.exception;

/**
 * 更新数据异常
 *
 * @author : maiaimei
 * @date : 2023-1-2
 */
public class UpdateDataException extends RuntimeException {
    public UpdateDataException(String message) {
        super(message);
    }
}
