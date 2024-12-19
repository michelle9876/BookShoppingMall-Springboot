package com.github.ecommerce.service.exception;


import com.github.ecommerce.web.advice.ErrorCode;
import org.springframework.http.HttpStatus;


public class CAuthenticationEntryPointException extends RuntimeException {
    public CAuthenticationEntryPointException(String msg, Throwable t) {
        super(msg, t);
    }

    public CAuthenticationEntryPointException(String msg) {
        super(msg);
    }

    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public CAuthenticationEntryPointException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.httpStatus = errorCode.getHttpStatus();
    }

}
