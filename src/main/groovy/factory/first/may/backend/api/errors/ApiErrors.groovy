package factory.first.may.backend.api.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CustomAppException extends RuntimeException {

    private String requestId;

    // Custom error message
    private String message;

    // Custom error code representing an error in system
    private String errorCode;

    public CustomAppException(String message) {
        super(message);
        this.message = message;
    }
    public CustomAppException(String message, String errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public CustomAppException(String requestId, String message, String errorCode) {
        super(message);
        this.requestId = requestId;
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
