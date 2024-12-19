package com.github.ecommerce.service.exception;


import com.github.ecommerce.web.advice.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotAcceptException extends RuntimeException{
    public NotAcceptException(String message) {
        super(message);
    }
    public NotAcceptException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.httpStatus = errorCode.getHttpStatus();
    }

    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
