package cn.maiaimei.framework.beans;

public class ResultUtils {
    public static Result success() {
        return Result.builder()
                .code(ResultStatus.OK.value())
                .build();
    }

    public static Result success(String message) {
        return Result.builder()
                .code(ResultStatus.OK.value())
                .message(message)
                .build();
    }

    public static <T> Result success(T data) {
        return Result.builder()
                .code(ResultStatus.OK.value())
                .data(data)
                .build();
    }

    public static <T> Result success(String message, T data) {
        return Result.builder()
                .code(ResultStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public static ErrorResult error(String message) {
        final ErrorResult<Object> result = new ErrorResult<>();
        result.setCode(ResultStatus.INTERNAL_SERVER_ERROR.value());
        result.setMessage(message);
        return result;
    }

    public static ErrorResult error(Integer code, String message) {
        final ErrorResult<Object> result = new ErrorResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
