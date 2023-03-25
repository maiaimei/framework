package cn.maiaimei.framework.beans;

public class ResultUtils {
    public static Result success() {
        return Result.builder()
                .code(ResultStatus.OK.value().toString())
                .build();
    }

    public static Result success(String message) {
        return Result.builder()
                .code(ResultStatus.OK.value().toString())
                .message(message)
                .build();
    }

    public static <T> Result success(T data) {
        return Result.builder()
                .code(ResultStatus.OK.value().toString())
                .data(data)
                .build();
    }

    public static <T> Result success(String message, T data) {
        return Result.builder()
                .code(ResultStatus.OK.value().toString())
                .message(message)
                .data(data)
                .build();
    }

    public static ErrorResult error(String message) {
        final ErrorResult<Object> result = new ErrorResult<>();
        result.setCode(ResultStatus.INTERNAL_SERVER_ERROR.value().toString());
        result.setMessage(message);
        return result;
    }

    public static ErrorResult error(String code, String message) {
        final ErrorResult<Object> result = new ErrorResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
