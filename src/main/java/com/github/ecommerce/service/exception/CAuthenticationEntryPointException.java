package com.github.ecommerce.service.exception;


import com.github.ecommerce.web.advice.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class CAuthenticationEntryPointException extends RuntimeException {
    private final HttpStatus httpStatus;
    public CAuthenticationEntryPointException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.httpStatus = errorCode.getHttpStatus();
    }
}

