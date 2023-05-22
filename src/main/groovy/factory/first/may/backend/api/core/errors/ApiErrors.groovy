package factory.first.may.backend.api.core.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class CustomAppException extends RuntimeException {

    private String requestId

    // Custom error message
    private String message

    // Custom error code representing an error in system
    private String errorCode

    CustomAppException(String message) {
        super(message)
        this.message = message
    }

    CustomAppException(String message, String errorCode) {
        super(message)
        this.message = message
        this.errorCode = errorCode
    }

    CustomAppException(String requestId, String message, String errorCode) {
        super(message)
        this.requestId = requestId
        this.message = message
        this.errorCode = errorCode
    }

    String getRequestId() {
        return this.requestId
    }

    void setRequestId(String requestId) {
        this.requestId = requestId
    }

    @Override
    String getMessage() {
        return this.message
    }

    void setMessage(String message) {
        this.message = message
    }

    String getErrorCode() {
        return this.errorCode
    }

    void setErrorCode(String errorCode) {
        this.errorCode = errorCode
    }
}
