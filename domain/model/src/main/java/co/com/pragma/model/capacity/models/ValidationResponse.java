package co.com.pragma.model.capacity.models;

public class ValidationResponse {

    private String message;
    private Boolean valid;

    public ValidationResponse(String message, Boolean valid) {
        this.message = message;
        this.valid = valid;
    }

    public ValidationResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

}
