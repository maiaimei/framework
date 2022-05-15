package cn.maiaimei.framework.beans;

public enum ResultStatus {
    OK(200, "OK"),

    FORBIDDEN(403, "Forbidden"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int value;
    private final String message;

    ResultStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int value() {
        return this.value;
    }

    public String getMessage() {
        return this.message;
    }
}
