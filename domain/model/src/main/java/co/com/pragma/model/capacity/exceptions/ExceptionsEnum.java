package co.com.pragma.model.capacity.exceptions;

public enum ExceptionsEnum {
    MIN_TECHNOLOGIES(400, "Min 3 technologies mandatory"),
    MAX_TECHNOLOGIES(400, "Max 20 technologies"),
    DUPLICATE_TECHNOLOGIES(400, "Not valid duplicate technologies"),
    DUPLICATE_CAPACITY(409, "Not valid duplicate capacity")
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
