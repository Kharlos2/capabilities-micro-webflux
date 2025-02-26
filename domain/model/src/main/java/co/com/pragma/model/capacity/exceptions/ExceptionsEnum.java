package co.com.pragma.model.capacity.exceptions;

public enum ExceptionsEnum {
    MIN_TECHNOLOGIES(400, "Min 3 technologies mandatory"),
    MAX_TECHNOLOGIES(400, "Max 3 technologies"),
    DUPLICATE_TECHNOLOGIES(400, "Not valid duplicate technologies")
    ;

    private final int httpStatus;
    private final String message;

    ExceptionsEnum(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
